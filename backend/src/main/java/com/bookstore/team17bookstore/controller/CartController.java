package com.bookstore.team17bookstore.controller;

import com.bookstore.team17bookstore.model.Cart;
import com.bookstore.team17bookstore.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

// Controller for managing the shopping cart
// Provides endpoints for viewing, adding, updating, removing items, and clearing the cart
@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService svc;

    public CartController(CartService svc) {
        this.svc = svc;
    }

    /**
     * Retrieves the shopping cart for a given user.
     * If the cart does not exist, a new one is created.
     * @param userId the ID of the user
     * @return the user's shopping cart
     * @throws SQLException if there is an error accessing the database
     */
    @GetMapping("/{userId}")
    public Cart viewCart(@PathVariable String userId) throws SQLException {
        return svc.getCart(userId);
    }

    /**
     * Adds an item to the user's shopping cart.
     * If the item already exists in the cart, its quantity is updated.
     * If the item does not exist, it is added with the specified quantity.
     * @param userId the ID of the user
     * @param bookId the ID of the book
     * @param quantity the quantity of the book to add
     * @return the updated shopping cart
     * @throws SQLException if there is an error accessing the database
     */
    @PostMapping("/{userId}/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Cart addItem(@PathVariable String userId, 
                          @RequestParam Long bookId, 
                          @RequestParam int quantity) throws SQLException {
        return svc.addItem(userId, bookId, quantity);
    }

    /**
     * Updates the quantity of a specific book in the user's shopping cart.
     * If the quantity is zero or negative, the item is removed from the cart.
     * @param userId the ID of the user
     * @param bookId the ID of the book
     * @param quantity the new quantity of the book
     * @return the updated shopping cart
     * @throws SQLException if there is an error accessing the database
     */
    @PostMapping("/{userId}/update")
    public Cart updateItemQuantity(@PathVariable String userId,
                                   @RequestParam Long bookId,
                                   @RequestParam int quantity) throws SQLException {
        return svc.updateItemQuantity(userId, bookId, quantity);
    }

    /**
     * Removes a specific book from the user's shopping cart.
     * @param userId the ID of the user
     * @param bookId the ID of the book to remove
     * @return the updated shopping cart
     * @throws SQLException if there is an error accessing the database
     */
    @PostMapping("/{userId}/remove")
    public Cart removeItem(@PathVariable String userId,
                           @RequestParam Long bookId) throws SQLException {
        return svc.removeItem(userId, bookId);
    }

    /**
     * Clears all items from the user's shopping cart.
     * @param userId the ID of the user
     * @return the cleared shopping cart
     * @throws SQLException if there is an error accessing the database
     */
    @PostMapping("/{userId}/clear")
    public Cart clearCart(@PathVariable String userId) throws SQLException {
        return svc.clearCart(userId);
    }
}
