DELETE FROM categories;
ALTER TABLE categories ALTER COLUMN id RESTART WITH 1;
