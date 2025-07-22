package com.bookstore.team17bookstore.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.bookstore.team17bookstore.model.Book;

@Repository
public class BookRepository {

    //SQL statements
     private static final String INSERT =
        "INSERT INTO book (isbn, category, author, title, coverImageUrl, edition," +
        " publisher, publicationYear, quantityInStock, minimumThreshold," +
        " buyingPrice, sellingPrice) " +
        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

     private static final String UPDATE =
        "UPDATE book SET isbn=?, category=?, author=?, title=?, coverImageUrl=?," +
        " edition=?, publisher=?, publicationYear=?, quantityInStock=?, " +
        " minimumThreshold=?, buyingPrice=?, sellingPrice=? WHERE id=?";

    private static final String SELECT_BY_ID =
        "SELECT * FROM book WHERE id = ?";

    private static final String SELECT_BY_ISBN =
        "SELECT * FROM book WHERE isbn = ?";
    
    private static final String SELECT_ALL =
        "SELECT * FROM book";

    private static final String DELETE_BY_ID = 
        "DELETE FROM book WHERE id = ?";

    // DataSource (connection pool/ provider)
    private final DataSource dataSource;

    public BookRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Save a book to the database.
     * If the book has no ID, it will be inserted; otherwise, it will be updated.
     * @param b the book to save
     * @return the saved book with its ID set if it was newly created
     * @throws SQLException on error
     */
    public Book save(Book b) throws SQLException{
        if (b.getId() == null) {
            try (Connection c = dataSource.getConnection();
                PreparedStatement ps = c.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

                bindCommonFields(ps, b);
                ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
               if (rs.next()) {
                    b.setId(rs.getLong(1)); // Set the generated ID
                } else {
                    throw new SQLException("Failed to retrieve generated key for book");
                }
            }
        }
    } else {
        try (Connection c = dataSource.getConnection();
            PreparedStatement ps = c.prepareStatement(UPDATE)) {

            bindCommonFields(ps, b);
            ps.setLong(13, b.getId());
            ps.executeUpdate();
        }
    }
    return b;
    }

    /**
     * Delete a book by ID
     * @return true if the book was deleted, false if it didn't exist
     * @throws SQLException on error
     */
    public boolean deleteById(long id) throws SQLException {
        try (Connection c = dataSource.getConnection();
            PreparedStatement ps = c.prepareStatement(DELETE_BY_ID)) {
            ps.setLong(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
            }
    }

    /**
     * Find a book by its ID.
     * @param id the ID of the book
     * @return an Optional containing the found book, or empty if not found
     * @throws SQLException on error
     */
    public Optional<Book> findById(Long id) throws SQLException {
        return queryOne(SELECT_BY_ID, ps -> ps.setLong(1, id));
    }

    /**
     * Find a book by its ISBN.
     * @param isbn the ISBN of the book
     * @return an Optional containing the found book, or empty if not found
     * @throws SQLException on error
     */
    public Optional<Book> findByISBN(String isbn) throws SQLException {
        return queryOne(SELECT_BY_ISBN, ps -> ps.setString(1, isbn));
    }

    /**
     * Find books by category.
     * @param category the category to search for
     * @return a list of books matching the category
     * @throws SQLException on error
     */
    public List<Book> findByCategory(String category) throws SQLException {
        String sql = "SELECT * FROM book WHERE LOWER(category) LIKE LOWER(?)";
        return queryMany(sql, ps -> ps.setString(1, "%" + category + "%"));
    }

    /**
     * Find books by title.
     * @param title the title to search for
     * @return a list of books matching the title
     * @throws SQLException on error
     */
    public List<Book> findByTitle(String title) throws SQLException {
        String sql = "SELECT * FROM book WHERE LOWER(title) LIKE LOWER(?)";
        return queryMany(sql, ps -> ps.setString(1, "%" + title + "%"));
    }

    /**
     * Find books by author.
     * @param author the author to search for
     * @return a list of books matching the author
     * @throws SQLException on error
     */
    public List<Book> findByAuthor(String author) throws SQLException {
        String sql = "SELECT * FROM book WHERE LOWER(author) LIKE LOWER(?)";
        return queryMany(sql, ps -> ps.setString(1, "%" + author + "%"));
    }

    /**
     * Find all books.
     * @return a list of all books
     * @throws SQLException on error
     */
    public List<Book> findAll() throws SQLException {
        List<Book> list = new ArrayList<>();
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
                
            while (rs.next()) list.add(rowToBook(rs));
        }
        return list;
    }

    /**
     * Query a single book using a SQL statement and a binder function.
     * @param sql the SQL query
     * @param binder the function to bind parameters to the PreparedStatement
     * @return an Optional containing the found book, or empty if not found
     * @throws SQLException on error
     */
    private Optional<Book> queryOne(String sql, ThrowingConsumer<PreparedStatement> binder) throws SQLException {
        try (Connection c = dataSource.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {

                binder.accept(ps);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return Optional.of(rowToBook(rs));
                    return Optional.empty();
                }
        }
    }

    /**
     * Query multiple books using a SQL statement and a binder function.
     * @param sql the SQL query
     * @param binder the function to bind parameters to the PreparedStatement
     * @return a list of books matching the query
     * @throws SQLException on error
     */
    private List<Book> queryMany(String sql, ThrowingConsumer<PreparedStatement> binder) throws SQLException {
    List<Book> list = new ArrayList<>();
    try (Connection c = dataSource.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        binder.accept(ps);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(rowToBook(rs));
        }
    }
    return list;
}


    // Bind fields 1-12 for INSERT / UPDATE
    private void bindCommonFields(PreparedStatement ps, Book b) throws SQLException {
        ps.setString(1, b.getIsbn());
        ps.setString(2, b.getCategory());
        ps.setString(3, b.getAuthor());
        ps.setString(4, b.getTitle());
        ps.setString(5, b.getCoverImageUrl());
        ps.setString(6, b.getEdition());
        ps.setString(7, b.getPublisher());
        ps.setInt(8, b.getPublicationYear());
        ps.setInt(9, b.getQuantityInStock());
        ps.setInt(10, b.getMinimumThreshold());
        ps.setDouble(11, b.getBuyingPrice());
        ps.setDouble(12, b.getSellingPrice());
    }

    // Map a ResultSet row to a Book object
    private Book rowToBook(ResultSet rs) throws SQLException {
        Book b = new Book();
        b.setId(rs.getLong("id"));
        b.setIsbn(rs.getString("isbn"));
        b.setCategory(rs.getString("category"));
        b.setAuthor(rs.getString("author"));
        b.setTitle(rs.getString("title"));
        b.setCoverImageUrl(rs.getString("coverImageUrl"));
        b.setEdition(rs.getString("edition"));
        b.setPublisher(rs.getString("publisher"));
        b.setPublicationYear(rs.getInt("publicationYear"));
        b.setQuantityInStock(rs.getInt("quantityInStock"));
        b.setMinimumThreshold(rs.getInt("minimumThreshold"));
        b.setBuyingPrice(rs.getDouble("buyingPrice"));
        b.setSellingPrice(rs.getDouble("sellingPrice"));
        return b;
    }

    // Functional interface for lambda-safe SQLException throwing
    @FunctionalInterface
    private interface ThrowingConsumer<T> {
        void accept(T t) throws SQLException;
    }
}