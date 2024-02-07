DROP TABLE IF EXISTS Product;
CREATE TABLE Product(
                        ID INT AUTO_INCREMENT,
                        Name VARCHAR(255) NOT NULL,
                        CategoryID INT NOT NULL,
                        Description VARCHAR(1023) NOT NULL,
                        PRIMARY KEY (ID),
                        FOREIGN KEY (CategoryID) REFERENCES Category(ID)
);