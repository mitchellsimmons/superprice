DROP TABLE IF EXISTS Orders;
CREATE TABLE Orders(
                        ID INT AUTO_INCREMENT,
                        CustomerName VARCHAR(32) NOT NULL,
                        CustomerEmail VARCHAR(255) NOT NULL,
                        CustomerPhone CHAR(12) NOT NULL,                   -- Match AU telephone format ('+' optional):
                                                                           -- landline:  613xxxxxxxx
                                                                           -- or mobile: 614xxxxxxxx
                        CustomerAddress VARCHAR(255) NOT NULL,
                        CustomerPostcode CHAR(4) NOT NULL,                 -- Match MEL postcode format
                        DeliveryWindowStart CHAR(5) NOT NULL DEFAULT '00:00', -- Start time (HH:MM) of the 4-hour delivery window.
                        DeliveryDate CHAR(10) NOT NULL DEFAULT '1970-01-01', -- Use the UNIX Epoch as the default date
                        CustomMessage CHAR(255) NOT NULL,
                        OrderStatus INT NOT NULL,
                        PRIMARY KEY (ID),
                        CONSTRAINT RESTRICT_ORDER_STATUS                   -- To match the values of OrderStatus enum
                            CHECK (OrderStatus IN (0,                      -- Approved, sent to delivery partner org.
                                                   1,                      -- Approved by SuperPrice, not emailed
                                                   2))                     -- Newly submitted

);