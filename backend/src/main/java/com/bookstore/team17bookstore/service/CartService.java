package com.bookstore.team17bookstore.service;

import com.bookstore.team17bookstore.model.Cart;
import com.bookstore.team17bookstore.model.CartItem;
import com.bookstore.team17bookstore.model.Book;
import com.bookstore.team17bookstore.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

// CartService for managing user shopping carts
@Service
public class CartService {

    private final CartRepository repo;
    private final BookService bookService;
    private final UserService userService;

    public CartService(CartRepository repo, BookService bookService, UserService userService) {
        this.repo = repo;
        this.bookService = bookService;
        this.userService = userService;
    }

    /**
     * Retrieves the cart for a given user ID, creating a new one if it doesn't exist.
     * @param userId the ID of the user
     * @return the user's cart
     */
    public Cart getCart(String userEmail) throws SQLException {
        Long uid = userService.idByEmail(userEmail);
        List<CartItem> rows = repo.findByUser(uid);
        Cart cart = new Cart(userEmail);
        for (CartItem r : rows) {
            Book b = bookService.findById(r.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            double price = b.getSellingPrice();
            CartItem item = new CartItem(r.getBookId(), r.getQuantity(), price);
            item.setTitle(b.getTitle());
            item.setAuthor(b.getAuthor());
            cart.getItems().add(item);
        }
        return cart;
    }

    /**
     * Adds an item to the user's cart.
     * @param userId the ID of the user
     * @param bookId the ID of the book to add
     * @param quantity the quantity of the book to add
     * @return the updated cart
     * @throws SQLException if there is an error accessing the database
     * @throws RuntimeException if the book is not found
     * @throws IllegalArgumentException if the quantity is not positive
     */
    public Cart addItem(String userEmail, Long bookId, int quantity) throws SQLException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        Book book = bookService.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId));
        Long uid = userService.idByEmail(userEmail);
        repo.upsert(uid, bookId, quantity);
        return getCart(userEmail);
    }
    
    /**
     * Updates the quantity of a specific book in the user's cart.
     * @param userId the ID of the user
     * @param bookId the ID of the book to update
     * @param quantity the new quantity of the book
     * @return the updated cart
     * @throws IllegalArgumentException if the quantity is negative
     */
    public Cart updateItemQuantity(String userEmail, Long bookId, int quantity) throws SQLException {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        Long uid = userService.idByEmail(userEmail);
        if (quantity == 0) {
            repo.delete(uid, bookId);
        } else {
            repo.upsert(uid, bookId, quantity);
        }
        return getCart(userEmail);
    }

    /**
     * Removes an item from the user's cart.
     * @param userId the ID of the user
     * @param bookId the ID of the book to remove
     * @return the updated cart
     */
    public Cart removeItem(String userEmail, Long bookId) throws SQLException {
        Long uid = userService.idByEmail(userEmail);
        repo.delete(uid, bookId);
        return getCart(userEmail);
    }

    /**
     * Clears the user's cart.
     * @param userId the ID of the user
     * @return the updated cart
     */
    public Cart clearCart(String userEmail) throws SQLException {
        Long uid = userService.idByEmail(userEmail);
        repo.clear(uid);
        return getCart(userEmail);
    }
}
