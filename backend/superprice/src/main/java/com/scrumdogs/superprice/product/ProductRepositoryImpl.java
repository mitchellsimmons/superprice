package com.scrumdogs.superprice.product;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.init.UncategorizedScriptException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor// This also performs @Autowiring if only one constructor
@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final DataSource dataSource;

    @Override
    public List<Product> findAll() {
        Connection connection;
        try {
            connection = dataSource.getConnection();
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM Product;");
            List<Product> products = new ArrayList<>();
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Product p = new Product(rs.getLong(1), rs.getString(2), rs.getLong(3), rs.getString(4));
                products.add(p);
            }
            connection.close();
            return products;

        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error in findAll", e);
        }
    }

    @Override
    public Optional<Product> findById(long id) {
        Connection connection;
        try {
            connection = dataSource.getConnection();
            String sql = "SELECT * FROM Product WHERE id = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setLong(1, id);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                Product p = new Product(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getLong(3),
                        rs.getString(4)
                );
                connection.close();
                return Optional.of(p);
            } else {
                connection.close();
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error in findById", e);
        }
    }

    @Override
    public Optional<List<Product>> findByName(String name) {
        Connection connection;
        try {
            connection = dataSource.getConnection();
            String sql = "SELECT * from Product WHERE UPPER(name) LIKE UPPER(?)";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, "%" + name + "%");
            List<Product> products = new ArrayList<>();
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Product p = new Product(rs.getLong(1), rs.getString(2), rs.getLong(3), rs.getString(4));
                products.add(p);
            }
            connection.close();
            return products.isEmpty() ? Optional.empty() : Optional.of(products);
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error in findByName", e);
        }
    }

    /**
     * @param id - the id of the product to find the inventory for
     * @param time - the current time (set by service as LocalTime.now()) used for testing query at different times
     * @return - Empty optional or Optional of list product inventories
     */
    @Override
    public Optional<List<ProductInventory>> findInventoryById(Long id, LocalTime time) {
        Connection connection;
        try {
            connection = dataSource.getConnection();
            // This query will return the inventory for the products with the latest time which is not in the future
            PreparedStatement stm = getFindInventoryByIdSQLStatement(connection, id, time);
            ResultSet rs = stm.executeQuery();
            List<ProductInventory> productInventory = new ArrayList<>();
            // do while as we are checking the first item
            if (!rs.next()) {
                connection.close();
                return Optional.empty();
            } else {
                do {
                    // add product inventories to list
                    ProductInventory i = new ProductInventory(
                        rs.getLong(1),
                        rs.getLong(2),
                        rs.getLong(3),
                        rs.getLong(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getTime(7).toLocalTime(),
                        rs.getLong(8),
                        rs.getLong(9),
                        rs.getString(10),
                        rs.getLong(11)
                    );
                    productInventory.add(i);
                } while (rs.next());
            }
            connection.close();
            return Optional.of(productInventory);
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error in findInventoryById", e);
        }
    }

    private static PreparedStatement getFindInventoryByIdSQLStatement(Connection connection, Long id, LocalTime time) {
        String sql = """
            SELECT i.id AS INVENTORYID, p.ID AS PRODUCTID, STOREID, CATEGORYID, p.NAME, DESCRIPTION , TIME, PRICEINCENTS, STOCKLEVEL, Store.NAME , POSTCODE
            FROM Product p
            JOIN (
                SELECT I.*
                FROM Inventory I
                INNER JOIN (
                    SELECT PRODUCTID, STOREID, MAX(TIME) AS TIME
                    FROM Inventory
                    WHERE TIME <= ?
                    GROUP BY PRODUCTID, STOREID
                ) T ON I.PRODUCTID = T.PRODUCTID AND I.STOREID = T.STOREID AND I.TIME = T.TIME
            ) i ON p.id = i.PRODUCTID
            JOIN Store ON i.STOREID = Store.id
            WHERE PRODUCTID = ?
            ORDER BY PRODUCTID, STOREID
        """;

        PreparedStatement stm;
        try {
            stm = connection.prepareStatement(sql);
            stm.setTime(1, Time.valueOf(time));
            stm.setLong(2, id);
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error producing SQL statement for findInventoryById", e);
        }
        return stm;
    }

    @Override
    public Optional<List<ProductInventory>> findInventoryByIdAndPostcode(Long id, Long postcode, LocalTime time) {
        Connection connection;
        try {
            connection = dataSource.getConnection();
            // This query will return the inventory for the products with the latest time which is not in the future
            PreparedStatement stm = getFindInventoryByIdAndPostcodeSQLStatement(connection, id, postcode, time);
            ResultSet rs = stm.executeQuery();
            List<ProductInventory> productInventory = new ArrayList<>();
            // do while as we are checking the first item
            if (!rs.next()) {
                connection.close();
                return Optional.empty();
            } else {
                do {
                // add product inventories to list
                    ProductInventory i = new ProductInventory(
                        rs.getLong(1),
                        rs.getLong(2),
                        rs.getLong(3),
                        rs.getLong(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getTime(7).toLocalTime(),
                        rs.getLong(8),
                        rs.getLong(9),
                        rs.getString(10),
                        rs.getLong(11)
                    );
                    productInventory.add(i);

                } while (rs.next());
            }
            connection.close();
            return Optional.of(productInventory);
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error in findInventoryByIdAndPostcode", e);
        }
    }

    private static PreparedStatement getFindInventoryByIdAndPostcodeSQLStatement(Connection connection, Long id, Long postcode, LocalTime time) {
        String sql = """
            SELECT i.id AS INVENTORYID, p.ID AS PRODUCTID, STOREID, CATEGORYID, p.NAME, DESCRIPTION , TIME, PRICEINCENTS, STOCKLEVEL , Store.NAME , POSTCODE
            FROM Product p
            JOIN (
            SELECT I.*
            FROM Inventory I
                     INNER JOIN (
                SELECT PRODUCTID, STOREID, MAX(TIME) AS TIME
                FROM Inventory
                WHERE TIME <= ?
                GROUP BY PRODUCTID, STOREID
            ) T ON I.PRODUCTID = T.PRODUCTID AND I.STOREID = T.STOREID AND I.TIME = T.TIME
            ORDER BY I.PRODUCTID, I.STOREID
            ) i ON p.id = i.PRODUCTID
            JOIN Store ON i.STOREID = Store.id
            WHERE PRODUCTID = ? AND POSTCODE = ?
            ORDER BY I.PRODUCTID, I.STOREID;
        """;

        PreparedStatement stm;
        try {
            stm = connection.prepareStatement(sql);
            stm.setTime(1, Time.valueOf(time));
            stm.setLong(2, id);
            stm.setLong(3, postcode);
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error producing SQL statement for findInventoryByIdAndPostcode", e);
        }
        return stm;
    }

    /**
     * @param name - the name of the product to find the inventory for (can be a partial match)
     * @param time - the current time (set by service as LocalTime.now()) used for testing query at different times
     * @return - Empty optional or Optional of list product inventories
     */
    @Override
    public Optional<List<ProductInventory>> findInventoryByName(String name, LocalTime time) {
        Connection connection;
        try {
            connection = dataSource.getConnection();
            // This query will return the inventory for the products with the latest time which is not in the future
            PreparedStatement stm = getFindInventoryByNameSQLStatement(connection, name, time);
            ResultSet rs = stm.executeQuery();
            List<ProductInventory> productInventory = new ArrayList<>();
            // do while as we are checking the first item
            if (!rs.next()) {
                connection.close();
                return Optional.empty();
            } else {
                do {
                    ProductInventory i = new ProductInventory(
                        rs.getLong(1),
                        rs.getLong(2),
                        rs.getLong(3),
                        rs.getLong(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getTime(7).toLocalTime(),
                        rs.getLong(8),
                        rs.getLong(9),
                        rs.getString(10),
                        rs.getLong(11)
                    );
                    productInventory.add(i);
                } while (rs.next());
            }
            connection.close();
            return Optional.of(productInventory);
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error in findByInventoryByName", e);
        }
    }

    private static PreparedStatement getFindInventoryByNameSQLStatement(Connection connection, String name, LocalTime time) {
        String sql = """
            SELECT i.id AS INVENTORYID, p.ID AS PRODUCTID, STOREID, CATEGORYID, p.NAME, DESCRIPTION, TIME, PRICEINCENTS, STOCKLEVEL, Store.NAME, POSTCODE
            FROM Product p
            JOIN (
                SELECT I.*
                FROM Inventory I
                         INNER JOIN (
                    SELECT PRODUCTID, STOREID, MAX(TIME) AS TIME
                    FROM Inventory
                    WHERE TIME <= ?
                    GROUP BY PRODUCTID, STOREID
                ) T ON I.PRODUCTID = T.PRODUCTID AND I.STOREID = T.STOREID AND I.TIME = T.TIME
            ) i ON p.id = i.PRODUCTID
            JOIN Store ON i.STOREID = Store.id
            WHERE UPPER(p.NAME) LIKE UPPER(?)
            ORDER BY p.ID, STOREID;
        """;

        PreparedStatement stm;
        try {
            stm = connection.prepareStatement(sql);
            stm.setTime(1, Time.valueOf(time));
            // adding the wildcards allow for partial match - anything before or after name
            stm.setString(2, "%" + name + "%");
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error producing SQL statement for findInventoryByName", e);
        }
        return stm;
    }

    @Override
    public Optional<List<ProductInventory>> findInventoryByNameAndPostcode(String name, Long postcode, LocalTime time) {
        Connection connection;
        try {
            connection = dataSource.getConnection();
            // This query will return the inventory for the products with the latest time which is not in the future
            PreparedStatement stm = getFindInventoryByNameAndPostcodeSQLStatement(connection, name, postcode, time);
            ResultSet rs = stm.executeQuery();
            List<ProductInventory> productInventory = new ArrayList<>();
            // do while as we are checking the first item
            if (!rs.next()) {
                connection.close();
                return Optional.empty();
            } else {
                do {
                    ProductInventory i = new ProductInventory(
                        rs.getLong(1),
                        rs.getLong(2),
                        rs.getLong(3),
                        rs.getLong(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getTime(7).toLocalTime(),
                        rs.getLong(8),
                        rs.getLong(9),
                        rs.getString(10),
                        rs.getLong(11)
                    );
                    productInventory.add(i);

                } while (rs.next());
            }
            connection.close();
            return Optional.of(productInventory);
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error in findInventoryByNameAndPostcode", e);
        }
    }

    private static PreparedStatement getFindInventoryByNameAndPostcodeSQLStatement(Connection connection, String name, Long postcode, LocalTime time) {
        String sql = """
            SELECT i.id AS INVENTORYID, p.ID AS PRODUCTID, STOREID, CATEGORYID, p.NAME, DESCRIPTION, TIME, PRICEINCENTS, STOCKLEVEL, Store.NAME, POSTCODE
            FROM Product p
            JOIN (
                SELECT I.*
                FROM Inventory I
                         INNER JOIN (
                    SELECT PRODUCTID, STOREID, MAX(TIME) AS TIME
                    FROM Inventory
                    WHERE TIME <= ?
                    GROUP BY PRODUCTID, STOREID
                ) T ON I.PRODUCTID = T.PRODUCTID AND I.STOREID = T.STOREID AND I.TIME = T.TIME
            ) i ON p.id = i.PRODUCTID
            JOIN Store ON i.STOREID = Store.id
            WHERE UPPER(p.NAME) LIKE CONCAT('%', UPPER(?), '%') AND POSTCODE = ?
            ORDER BY p.ID, STOREID;
        """;

        PreparedStatement stm;
        try {
            stm = connection.prepareStatement(sql);
            stm.setTime(1, Time.valueOf(time));
            // adding the wildcards allow for partial match - anything before or after name
            stm.setString(2, name);
            stm.setLong(3, postcode);
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error producing SQL statement for findInventoryByNameAndPostcode", e);
        }
        return stm;
    }

    @Override
    public Optional<List<Product>> findByCategory(Long categoryId) {
        Connection connection;
        try {
            connection = dataSource.getConnection();
            PreparedStatement stm = getFindByCategorySQLStatement(connection, categoryId);

            List<Product> products = new ArrayList<>();
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Product p = new Product(rs.getLong(1), rs.getString(2), rs.getLong(3), rs.getString(4));
                products.add(p);
            }
            connection.close();
            return products.isEmpty() ? Optional.empty() : Optional.of(products);
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error in findByCategory", e);
        }
    }

    private static PreparedStatement getFindByCategorySQLStatement(Connection connection, Long categoryId) {
        String sql = """
            WITH RECURSIVE category_hierarchy (ID, Name, ParentCategoryID) AS (
                -- initial step
                SELECT
                    ID,
                    Name,
                    ParentCategoryID
                FROM Category
                WHERE ID = ?
                
                UNION ALL
                -- recursive step
                SELECT
                    c.ID,
                    c.Name,
                    c.ParentCategoryID
                FROM Category c, category_hierarchy
                WHERE c.ParentCategoryID = category_hierarchy.ID
            )
            SELECT p.ID, p.Name, ch.id as categoryID, p.Description
            FROM category_hierarchy ch
            JOIN Product p ON ch.ID = p.CategoryId;
        """;

        PreparedStatement stm;
        try {
            stm = connection.prepareStatement(sql);
            stm.setLong(1, categoryId); // Search the final category id
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error producing SQL statement for findByCategory", e);
        }
        return stm;
    }

    @Override
    public Optional<List<ProductInventory>> findPopularProductsInventory(Long postcode, LocalTime time) {
        Connection connection;
        try {
            connection = dataSource.getConnection();
            PreparedStatement stm = getFindPopularProductsInventorySQLStatement(connection, postcode, time);

            List<ProductInventory> productInventory = new ArrayList<>();
            ResultSet rs = stm.executeQuery();

            if (!rs.next()) {
                connection.close();
                return Optional.empty();
            } else {
                do {
                    ProductInventory i = new ProductInventory(
                        rs.getLong(1),
                        rs.getLong( 2),
                        rs.getLong(3),
                        rs.getLong(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getTime(7).toLocalTime(),
                        rs.getLong(8),
                        rs.getLong(9),
                        rs.getString(10),
                        rs.getLong(11)
                    );
                    productInventory.add(i);
                } while (rs.next());
            }
            connection.close();
            return Optional.of(productInventory);
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error in findPopularProductsInventory", e);
        }
    }

    private static PreparedStatement getFindPopularProductsInventorySQLStatementOld(Connection connection, Long postcode, LocalTime time) {
        String sql = """
            WITH LatestInventory AS (
                SELECT I.*
                FROM Inventory I
                INNER JOIN (
                    SELECT PRODUCTID, STOREID, MAX(TIME) AS TIME
                    FROM Inventory
                    WHERE TIME <= ?
                    GROUP BY PRODUCTID, STOREID
                ) T ON I.PRODUCTID = T.PRODUCTID AND I.STOREID = T.STOREID AND I.TIME = T.TIME
                JOIN Store S ON T.STOREID = S.ID
        """;
        if (postcode != null) {
            sql += "WHERE POSTCODE = " + postcode + "\n";
        }
        sql += """
            )

            SELECT
                i1.id AS INVENTORYID,
                i1.PRODUCTID AS PRODUCTID,
                i1.STOREID,
                CATEGORYID,
                P.NAME,
                DESCRIPTION,
                i1.TIME,
                i1.PRICEINCENTS,
                i1.STOCKLEVEL,
                S.NAME,
                POSTCODE
            FROM LatestInventory i1
            LEFT OUTER JOIN LatestInventory i2
            ON i1.PRODUCTID = i2.PRODUCTID AND ((i1.PRICEINCENTS > i2.PRICEINCENTS) OR (i1.PriceInCents = i2.PriceInCents AND i1.Time < i2.Time))
            JOIN Product P ON i1.PRODUCTID = P.ID
            JOIN Store S ON i1.STOREID = S.ID
            WHERE i2.PRODUCTID IS NULL
            ORDER BY POPULARITY DESC, PRICEINCENTS, TIME DESC
            LIMIT 10;
        """;

        PreparedStatement stm;
        try {
            stm = connection.prepareStatement(sql);
            Time t = Time.valueOf(time);
            stm.setTime(1, t);
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error producing SQL statement for findPopularProductsInventoryOld", e);
        }
        return stm;
    }

    private static PreparedStatement getFindPopularProductsInventorySQLStatement(Connection connection, Long postcode, LocalTime time) {
        String sql = """
            SELECT
                i1.id AS INVENTORYID,
                i1.PRODUCTID AS PRODUCTID,
                i1.STOREID,
                CATEGORYID,
                P.NAME,
                DESCRIPTION,
                i1.TIME,
                i1.PRICEINCENTS,
                i1.STOCKLEVEL,
                S.NAME,
                POSTCODE
            FROM (
                --  I1: should return only the most recent inventory items for each store
                SELECT I.*
                FROM Inventory I
                INNER JOIN (
                    SELECT PRODUCTID, STOREID, MAX(TIME) AS TIME
                    FROM Inventory
                    WHERE TIME <= ?
                    GROUP BY PRODUCTID, STOREID
                ) T ON I.PRODUCTID = T.PRODUCTID AND I.STOREID = T.STOREID AND I.TIME = T.TIME
                JOIN Store S ON T.STOREID = S.ID
        """;
        if (postcode != null) {
            sql += "WHERE POSTCODE = " + postcode + "\n";
        }
        sql += """
            ) i1
            LEFT OUTER JOIN (
                -- I2: same as query above, return only the most recent inventory items for each store
                -- we're joining to ourselves on i1.price > i2.price so the cheapest rows have a blank i2.price
                SELECT I.*
                FROM Inventory I
                INNER JOIN (
                    SELECT PRODUCTID, STOREID, MAX(TIME) AS TIME
                    FROM Inventory
                    WHERE TIME <= ?
                    GROUP BY PRODUCTID, STOREID
                ) T ON I.PRODUCTID = T.PRODUCTID AND I.STOREID = T.STOREID AND I.TIME = T.TIME
                JOIN Store S ON T.STOREID = S.ID
        """;
        if (postcode != null) {
            sql += "WHERE POSTCODE = " + postcode + "\n";
        }
        sql += """
            ) i2
            ON i1.PRODUCTID = i2.PRODUCTID AND ((i1.PRICEINCENTS > i2.PRICEINCENTS) OR (i1.PriceInCents = i2.PriceInCents AND i1.Time < i2.Time))
            JOIN Product P ON i1.PRODUCTID = P.ID
            JOIN Store S ON i1.STOREID = S.ID
            WHERE i2.PRODUCTID IS NULL
            ORDER BY POPULARITY DESC, PRICEINCENTS, TIME DESC
            LIMIT 10;
        """;

        PreparedStatement stm;
        try {
            stm = connection.prepareStatement(sql);
            Time t = Time.valueOf(time);
            stm.setTime(1, t);
            stm.setTime(2, t);
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error producing SQL statement for findPopularProductsInventoryOld", e);
        }
        return stm;
    }

    @Override
    public List<ProductInventory> findChange(LocalTime startTime, LocalTime endTime) {
        Connection connection;
        try {
            connection = dataSource.getConnection();
            PreparedStatement stm = getFindChangeSQLStatement(connection, startTime, endTime);
            ResultSet rs = stm.executeQuery();
            List<ProductInventory> inventoryList = new ArrayList<>();

            while(rs.next()) {
                ProductInventory i = new ProductInventory(
                        rs.getLong(1),
                        rs.getLong(2),
                        rs.getLong(3),
                        rs.getLong(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getTime(7).toLocalTime(),
                        rs.getLong(8),
                        rs.getLong(9),
                        rs.getString(10),
                        rs.getLong(11)
                );
                inventoryList.add(i);
            }
            connection.close();
            return inventoryList;
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error in findInventory Notifications", e);
        }
    }

    private static PreparedStatement getFindChangeSQLStatement(Connection connection, LocalTime startTime, LocalTime endTime) {
        String sql = """
            SELECT i.id AS INVENTORYID, p.ID AS PRODUCTID, STOREID, CATEGORYID, p.NAME, DESCRIPTION , TIME, PRICEINCENTS, STOCKLEVEL , Store.NAME , POSTCODE
            FROM Product p
            JOIN (
                SELECT I.*
                FROM Inventory I
                         INNER JOIN (
                    SELECT PRODUCTID, STOREID, MAX(TIME) AS TIME
                    FROM Inventory
                    WHERE TIME >= ? AND TIME <= ?
                    GROUP BY PRODUCTID, STOREID
                ) T ON I.PRODUCTID = T.PRODUCTID AND I.STOREID = T.STOREID AND I.TIME = T.TIME
                GROUP BY I.PRODUCTID, I.ID
                ORDER BY I.PRODUCTID, I.STOREID
            ) i ON p.id = i.PRODUCTID
            JOIN Store ON i.STOREID = Store.id
            ORDER BY I.TIME, I.PRODUCTID, I.STOREID;
        """;

        PreparedStatement stm;
        try {
            stm = connection.prepareStatement(sql);
            stm.setTime(1, Time.valueOf(startTime));
            stm.setTime(2, Time.valueOf(endTime));
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error producing SQL statement for findChange", e);
        }
        return stm;
    }

}