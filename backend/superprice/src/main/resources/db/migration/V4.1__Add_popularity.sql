ALTER TABLE Product
ADD COLUMN popularity INT;

UPDATE Product
SET popularity = 50
WHERE id = 1;

UPDATE Product
SET popularity = 34
WHERE id = 2;

UPDATE Product
SET popularity = 110
WHERE id = 3;

UPDATE Product
SET popularity = 59
WHERE id = 4;

UPDATE Product
SET popularity = 11
WHERE id = 5;

UPDATE Product
SET popularity = 84
WHERE id = 6;