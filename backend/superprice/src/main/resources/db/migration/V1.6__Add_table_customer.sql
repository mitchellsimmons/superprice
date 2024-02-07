DROP TABLE IF EXISTS Customer;
CREATE TABLE Customer(
                         ID INT AUTO_INCREMENT,
                         Name VARCHAR(32),
                         Email VARCHAR(255),
                         Postcode VARCHAR(4),
                         SaltedPassword VARCHAR(255),
                         PRIMARY KEY (ID)
);