DROP TABLE IF EXISTS Cart;
CREATE TABLE Cart(
                     ID INT AUTO_INCREMENT,
                     CustomerID INT NOT NULL,
                     OrderStatus VARCHAR(15) NOT NULL,
                     PRIMARY KEY (ID),
                     FOREIGN KEY (CustomerID) REFERENCES Customer(ID)
);