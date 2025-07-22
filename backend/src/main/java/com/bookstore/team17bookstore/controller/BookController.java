package com.bookstore.team17bookstore.controller;

import com.bookstore.team17bookstore.model.Book;
import com.bookstore.team17bookstore.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

// Controller for managing books in the bookstore
// Provides endpoints for adding, searching, and deleting books
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService svc;
    public BookController(BookService svc) { this.svc = svc; }

    //make a book editor method

    /**
     * Returns a list of all books in the store.
     * @return a list of Book objects
     * @throws SQLException
     */
    @GetMapping
    public List<Book> all() throws SQLException { return svc.list(); }

    /**
     * Retrieve a single book by its ID.
     * @param id the book ID
     * @return the found Book
     * @throws SQLException on error
     */
    @GetMapping("/{id}")
    public Book one(@PathVariable Long id) throws SQLException {
        return svc.findById(id)
            .orElseThrow(() -> new RuntimeException("Book not found"));
    }
    
    /**
     * Adds a new book to the store.
     * This method will insert a new book if it has no ID, or update an existing book if it has an ID.
     * @param b the Book object to add
     * @return the added Book object
     * @throws SQLException on error
     */
    @PostMapping
    public Book add(@RequestBody Book b) throws SQLException { return svc.add(b); }

    /**
     * Update an existing book.
     * @param id the book ID
     * @param b  the updated Book body
     * @return the saved Book
     * @throws SQLException on error
     */
    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @RequestBody Book b) throws SQLException {
        b.setId(id);
        return svc.add(b);
    }

    /**
     * Finds a book by its ISBN.
     * @param isbn the ISBN to search for
     * @return the Book object with the specified ISBN
     * @throws SQLException on error
     */
    @GetMapping("/search/isbn")
    public Book findByISBN(@RequestParam String isbn) throws SQLException {
        return svc.findByISBN(isbn)
            .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    /**
     * Finds books by author.
     * @param author the author's name to search for
     * @return a list of books by the specified author
     * @throws SQLException on error
     */
    @GetMapping("/search/author")
    public List<Book> findByAuthor(@RequestParam String author) throws SQLException {
        return svc.findByAuthor(author);
    }

    /**
     * Finds books by title.
     * @param title the title to search for
     * @return a list of books with the specified title
     * @throws SQLException on error
     */
    @GetMapping("/search/title")
    public List<Book> findByTitle(@RequestParam String title) throws SQLException {
        return svc.findByTitle(title);
    }

    /**
     * Finds books by category.
     * @param category the category to search for
     * @return a list of books in the specified category
     * @throws SQLException on error
     */
    @GetMapping("/search/category")
    public List<Book> findByCategory(@RequestParam String category) throws SQLException {
        return svc.findByCategory(category);
    }

    /**
     * Deletes a book by its ID.
     * @param id the ID of the book to delete
     * @throws SQLException on error
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws SQLException {
        boolean removed = svc.delete(id);
        if (!removed) {
            throw new RuntimeException("Book not found"); 
        }
    }
}
