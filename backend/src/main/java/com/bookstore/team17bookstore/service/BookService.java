package com.bookstore.team17bookstore.service;

import com.bookstore.team17bookstore.model.Book;
import com.bookstore.team17bookstore.repository.BookRepository;

import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// Service for managing books
@Service
public class BookService {
    private final BookRepository repo;
    public BookService(BookRepository repo) { this.repo = repo; }   
    
    /**
     * Adds a new book to the store.
     * If the book has no ID, it will be inserted; otherwise, it will be updated.
     * @param b the Book object to add
     * @return the added Book object with its ID set
     * @throws SQLException on error
     */
    public Book add(Book b) throws SQLException {return repo.save(b);}

    /**
     * Updates an existing book in the store.
     * @param b the Book object to update
     * @return the updated Book object
     * @throws SQLException on error
     */
    public List<Book> list() throws SQLException { return repo.findAll(); }

    /**
     * Deletes a book by its ID.
     * @param id the ID of the book to delete
     * @return true if the book was deleted, false if it was not found
     * @throws SQLException on error
     */
    public boolean delete(Long id) throws SQLException {
        return repo.deleteById(id);
    }

    /**
     * Finds books by author.
     * @param author the author's name
     * @return a list of books by the specified author
     * @throws SQLException on error
     */
    public List<Book> findByAuthor(String author) throws SQLException {
        return repo.findByAuthor(author);
    }

    /**
     * Finds books by title.
     * @param title the title of the book
     * @return a list of books with the specified title
     * @throws SQLException on error
     */
    public List<Book> findByTitle(String title) throws SQLException {
        return repo.findByTitle(title);
    }

    /**
     * Finds a book by its ISBN.
     * @param isbn the ISBN of the book
     * @return an Optional containing the book if found, or empty if not found
     * @throws SQLException on error
     */
    public Optional<Book> findByISBN(String isbn) throws SQLException {
        return repo.findByISBN(isbn);
    }

    /**
     * Finds books by category.
     * @param category the category of the book
     * @return a list of books in the specified category
     * @throws SQLException on error
     */
    public List<Book> findByCategory(String category) throws SQLException {
        return repo.findByCategory(category);
    }

    /**
     * Finds a book by its ID.
     * @param id the ID of the book
     * @return an Optional containing the book if found, or empty if not found
     * @throws SQLException on error
     */
    public Optional<Book> findById(Long id) throws SQLException {
        return repo.findById(id);
    }

}
