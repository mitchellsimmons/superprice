DROP TABLE IF EXISTS CartItem;
CREATE TABLE CartItem(
                         CartID INT NOT NULL,
                         InventoryID INT NOT NULL,
                         Quantity INT NOT NULL,
                         PRIMARY KEY (CartID, InventoryID),
                         FOREIGN KEY (InventoryID) REFERENCES Inventory(ID)
);