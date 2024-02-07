DROP TABLE IF EXISTS Category;
CREATE TABLE Category(
                         ID INT AUTO_INCREMENT,
                         ParentCategoryID INT,
                         Name VARCHAR(255) NOT NULL,
                         PRIMARY KEY (ID),
                         FOREIGN KEY (ParentCategoryID) REFERENCES Category(ID)
);