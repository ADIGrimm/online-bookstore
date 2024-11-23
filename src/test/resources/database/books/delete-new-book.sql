DELETE FROM books_categories WHERE book_id IN (
    SELECT id FROM books WHERE title = 'New Book'
);
DELETE FROM books WHERE title = 'New Book';
