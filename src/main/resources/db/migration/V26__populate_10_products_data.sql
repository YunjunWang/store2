INSERT INTO categories (id, name)
VALUES (1, 'Produce'),
       (2, 'Dairy'),
       (3, 'Bakery'),
       (4, 'Beverages'),
       (5, 'Frozen Foods');

INSERT INTO products (name, price, description, category_id)
VALUES
-- Produce
('Organic Bananas (1 lb)', 0.69, 'Fresh USDA-certified organic bananas, perfect for snacking or smoothies.', 1),
('Red Seedless Grapes (2 lb bag)', 3.99, 'Juicy, sweet red grapes great for kids and lunchboxes.', 1),

-- Dairy
('2% Reduced Fat Milk (1 gallon)', 3.29, 'Creamy and fresh 2% milk sourced from local farms.', 2),
('Greek Yogurt (Strawberry, 5.3 oz)', 1.29, 'Thick, protein-rich yogurt with real strawberry fruit.', 2),

-- Bakery
('French Baguette', 2.49, 'Crispy crust with a soft interior, baked fresh daily.', 3),
('Blueberry Muffins (4-pack)', 4.79, 'Moist muffins loaded with wild blueberries.', 3),

-- Beverages
('Sparkling Water (Lime, 12-pack cans)', 5.99, 'Zero-calorie carbonated water with a hint of lime.', 4),
('Cold Brew Coffee (32 oz bottle)', 4.99, 'Smooth, bold coffee brewed cold for a refreshing taste.', 4),

-- Frozen Foods
('Frozen Chicken Nuggets (2 lb)', 6.49, 'Crispy breaded chicken nuggets, ready in minutes.', 5),
('Frozen Mixed Berries (16 oz)', 3.89, 'Blend of strawberries, blueberries, and raspberries for smoothies or desserts.',
 5);