DELIMITER $$

CREATE PROCEDURE getProductsBetweenPrices(
    minPrice DECIMAL(10, 2),
    maxPrice DECIMAL(10, 2)
)
BEGIN
    SELECT id, name, price, description, category_id from products
    WHERE price between minPrice AND maxPrice
    ORDER BY name;

END $$