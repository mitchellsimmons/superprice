package com.scrumdogs.superprice.order;

import com.scrumdogs.superprice.order.cart.Cart;
import com.scrumdogs.superprice.order.cart.InsufficientStockException;
import com.scrumdogs.superprice.order.controllers.EmailNotSentException;
import com.scrumdogs.superprice.order.controllers.OrderNotCreatedException;
import com.scrumdogs.superprice.order.customer.CustomerDetails;
import com.scrumdogs.superprice.order.cart.OrderItem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@RequiredArgsConstructor// This also performs @Autowiring if only one constructor
@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final DataSource source;

    public static final String PK_ERROR_MSG = "\nFailed to generate primary key for order.";
    public static final String UNKNOWN_INSERT_ERROR_MSG = "\nError in insertCustomerDetails: ";
    public static final String INV_ID_NOT_EXIST_ERR_MSG = "\nFailed to get details for inventory id: ";
    public static final String UNKNOWN_CALC_TOTAL_ERROR_MSG = "\nError in calculateTotal: ";
    public static final String UNKNOWN_CHECKOUT_ERROR_MSG = "\nError in calculateTotal: ";

    @Override
    public long insertCustomerDetails(CustomerDetails details) {

        String sql = "INSERT INTO Orders " +
                "(CustomerName, CustomerEmail, CustomerPhone, CustomerAddress, CustomerPostcode, " +
                "DeliveryDate, DeliveryWindowStart, CustomMessage, OrderStatus)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = this.source.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, details.getName());
            stmt.setString(2, details.getEmail());
            stmt.setString(3, details.getPhone());
            stmt.setString(4, details.getAddress());
            stmt.setString(5, details.getPostcode());
            stmt.setString(6, details.getDeliveryDate());
            stmt.setString(7, details.getDeliveryWindowStart());
            stmt.setString(8, details.getCustomMessage());
            stmt.setInt(9, OrderStatus.SUBMITTED.getCode());

            int rowsAffected = stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if (rowsAffected == 1 && rs.next()) {
                //Return the PK of this new DB entry as the orderID.
                return rs.getLong(1);
            } else {
                //Given the layers of validation before this point - unlikely this will get thrown...
                //...leave it in - in case the DB pitches a fit for some unforeseen reason.
                throw new SQLException(PK_ERROR_MSG);
            }

        } catch (SQLException e) {
            throw new OrderNotCreatedException(UNKNOWN_INSERT_ERROR_MSG + e.getMessage());
        }
    }

    @Override
    public int calculateTotal(Cart cart) {
        int totalInCents = 0;

        String sql = "SELECT Inventory.PriceInCents, Product.Name AS ProductName, " +
                        "Store.Name AS StoreName, Inventory.StockLevel " +
                        "FROM Inventory " +
                        "JOIN Product ON Inventory.ProductID = Product.ID " +
                        "JOIN Store ON Inventory.StoreID = Store.ID " +
                        "WHERE Inventory.ID = ?";

        try (Connection connection = this.source.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {

            for(OrderItem item : cart.getItems()) {
                stmt.setLong(1, item.getInventoryID());
                ResultSet rs = stmt.executeQuery();

                 if(rs.next()) {
                     int priceInCents = rs.getInt(1);
                     String productName = rs.getString(2);
                     String storeName = rs.getString(3);
                     int stockLevel = rs.getInt(4);

                     //Check items are in stock - if so add their cost to the total.
                     if (stockLevel >= item.getQuantity()) {
                         item.setProductName(productName);
                         item.setStoreName(storeName);
                         item.setItemPriceInCents(priceInCents);
                         totalInCents += item.getSubTotalInCents();
                     } else {
                         throw new InsufficientStockException(item.getInventoryID(), stockLevel, item.getQuantity());
                     }
                 } else {
                     //Given the layers of validation before this point - unlikely this will get thrown...
                     //...leave it in - in case the DB pitches a fit for some unforeseen reason.
                     throw new SQLException(INV_ID_NOT_EXIST_ERR_MSG + item.getInventoryID());
                 }
            }

            return totalInCents;

        } catch (SQLException e) {
            throw new OrderNotCreatedException(UNKNOWN_CALC_TOTAL_ERROR_MSG + e.getMessage());
        }
    }

    @Override
    public void checkout(Order order) {
        String sqlUpdateInventory = "UPDATE Inventory SET StockLevel = StockLevel - ? WHERE ID = ?";
        String sqlInsertOrderItems = "INSERT INTO OrderItem (OrderID, InventoryId, Quantity) VALUES (?, ?, ?)";
        String sqlUpdateOrderStatus = "UPDATE Orders SET OrderStatus = ? WHERE ID = ?";

        try (Connection connection = this.source.getConnection();
             PreparedStatement updateInventoryStmt = connection.prepareStatement(sqlUpdateInventory);
             PreparedStatement insertOrderItemsStmt = connection.prepareStatement(sqlInsertOrderItems);
             PreparedStatement updateOrderStatusStmt = connection.prepareStatement(sqlUpdateOrderStatus)) {

            //Run the update and insert as a single transaction.
            connection.setAutoCommit(false);

            for(OrderItem item : order.getCart().getItems()) {
                //Decrement the stock level.
                updateInventoryStmt.setInt(1, item.getQuantity());
                updateInventoryStmt.setLong(2, item.getInventoryID());
                updateInventoryStmt.executeUpdate();

                //Add the items in the cart to the DB against the order ID.
                insertOrderItemsStmt.setLong(1, order.getId());
                insertOrderItemsStmt.setLong(2, item.getInventoryID());
                insertOrderItemsStmt.setInt(3, item.getQuantity());
                insertOrderItemsStmt.executeUpdate();
            }

            //Update the order status
            updateOrderStatusStmt.setInt(1, order.getStatus().getCode());
            updateOrderStatusStmt.setLong(2, order.getId());
            connection.commit();

        } catch (SQLException e) {
            //Given the layers of validation before this point - unlikely this will get thrown...
            //...leave it in - in case the DB pitches a fit for some unforeseen reason.
            throw new OrderNotCreatedException(UNKNOWN_CHECKOUT_ERROR_MSG + e.getMessage());
        }
    }

    @Override
    public void sendEmail(Order order) throws EmailNotSentException {
        //Create the email file name according to the orderID
        String filepath = "/tmp/_delivery_email_" + order.getId() + ".txt";

        OrderEmail email = new OrderEmail(order);

        System.out.println(email); //Leave in for debug purposes

        //Try to "send" the email.
        try (PrintWriter writer = new PrintWriter(filepath)) {
            writer.write(email.toString());

        } catch (IOException e) {
            throw new EmailNotSentException(e.getMessage());
        }

        //If successful, set the order status as 'EMAILED' in the DB
        String sqlUpdateOrderStatus = "UPDATE Orders SET OrderStatus = ? WHERE ID = ?";

        try (Connection connection = this.source.getConnection();
             PreparedStatement updateOrderStatusStmt = connection.prepareStatement(sqlUpdateOrderStatus)) {

            //Update the order status
            updateOrderStatusStmt.setInt(1, OrderStatus.EMAILED.getCode());
            updateOrderStatusStmt.setLong(2, order.getId());

        } catch (SQLException e) {
            //Given the layers of validation before this point - unlikely this will get thrown...
            //...leave it in - in case the DB pitches a fit for some unforeseen reason.
            throw new EmailNotSentException(e.getMessage());
        }
    }
}
