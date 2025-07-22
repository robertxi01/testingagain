package com.bookstore.team17bookstore.service;

import com.bookstore.team17bookstore.model.Cart;
import com.bookstore.team17bookstore.model.Book;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

// CartService for managing user shopping carts
@Service
public class CartService {
    
    private final Map<String, Cart> carts = new HashMap<>();
    private final BookService bookService;

    public CartService(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Retrieves the cart for a given user ID, creating a new one if it doesn't exist.
     * @param userId the ID of the user
     * @return the user's cart
     */
    public Cart getCart(String userId) {
        return carts.computeIfAbsent(userId, Cart::new);
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
    public Cart addItem(String userId, Long bookId, int quantity) throws SQLException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        Book book = bookService.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId));

        Cart cart = getCart(userId);
        cart.addItem(bookId, quantity, book.getSellingPrice());
        return cart;
    }
    
    /**
     * Updates the quantity of a specific book in the user's cart.
     * @param userId the ID of the user
     * @param bookId the ID of the book to update
     * @param quantity the new quantity of the book
     * @return the updated cart
     * @throws IllegalArgumentException if the quantity is negative
     */
    public Cart updateItemQuantity(String userId, Long bookId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        Cart cart = getCart(userId);
        cart.setQuantity(bookId, quantity);
        return cart;
    }

    /**
     * Removes an item from the user's cart.
     * @param userId the ID of the user
     * @param bookId the ID of the book to remove
     * @return the updated cart
     */
    public Cart removeItem(String userId, Long bookId) {
        Cart cart = getCart(userId);
        cart.removeItem(bookId);
        return cart;
    }

    /**
     * Clears the user's cart.
     * @param userId the ID of the user
     * @return the updated cart
     */
    public Cart clearCart(String userId) {
        Cart cart = getCart(userId);
        cart.clear();
        return cart;
    }
}
