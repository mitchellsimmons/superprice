DROP TABLE IF EXISTS Store;
CREATE TABLE Store(
                      ID INT AUTO_INCREMENT,
                      Name VARCHAR(255) NOT NULL,
                      Postcode VARCHAR(4) NOT NULL,
                      PRIMARY KEY (ID)
);