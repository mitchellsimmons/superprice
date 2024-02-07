DROP TABLE IF EXISTS Inventory;
CREATE TABLE Inventory(
                          ID INT AUTO_INCREMENT,
                          ProductID INT NOT NULL,
                          StoreID INT NOT NULL,
                          Time TIME NOT NULL,
                          PriceInCents INT NOT NULL,
                          StockLevel INT NOT NULL,
                          PRIMARY KEY (ID),
                          FOREIGN KEY (ProductID) REFERENCES Product(ID),
                          FOREIGN KEY (StoreID) REFERENCES Store(ID)
);