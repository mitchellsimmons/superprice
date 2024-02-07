DROP TABLE IF EXISTS OrderItem;
CREATE TABLE OrderItem(
    OrderID INT NOT NULL,
    InventoryID INT NOT NULL,
    Quantity INT NOT NULL,
    PRIMARY KEY (OrderID, InventoryID),
    FOREIGN KEY (InventoryID) REFERENCES Inventory(ID)
);