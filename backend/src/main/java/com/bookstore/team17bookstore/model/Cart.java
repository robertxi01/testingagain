package com.bookstore.team17bookstore.model;

import java.util.ArrayList;
import java.util.List;

/*
 * Cart class represents a user's shopping cart in the bookstore application.
 */
public class Cart {
    private String userEmail;
    private List<CartItem> items = new ArrayList<>();

    // Default constructor for Cart
    public Cart() { }
    // Constructor that initializes the cart with a user's email
    public Cart(String userEmail) {
        this.userEmail = userEmail; 
    }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    /**
     * Adds an item to the cart. If the item already exists, it updates the quantity.
     * @param bookId The ID of the book to add.
     * @param quantity The quantity of the book to add.
     * @param price The price of the book.
     */
    public void addItem(Long bookId, int quantity, double price) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        for (CartItem item : items) {
            if (item.getBookId().equals(bookId)) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(bookId, quantity, price));
    }

    /**
     * Sets the quantity of a specific book in the cart.
     * If the quantity is set to zero, the item is removed from the cart.
     * @param bookId The ID of the book.
     * @param quantity The new quantity of the book.
     */
    public void setQuantity(Long bookId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        items.stream()
            .filter(item -> item.getBookId().equals(bookId))
            .findFirst()
            .ifPresent(item -> {
                if (quantity == 0) {
                    items.remove(item);
                } else {
                    item.setQuantity(quantity);
                }
            });
    }

    /**
     * Calculates the total price of all items in the cart.
     * @return The total price.
     */
    public double getTotalPrice() {
        return items.stream()
            .mapToDouble(CartItem::getLineTotal)
            .sum();
    }

    /**
     * Removes an item from the cart by its book ID.
     * @param bookId The ID of the book to remove.
     */
    public void removeItem(Long bookId) {
        items.removeIf(item -> item.getBookId().equals(bookId));
    }

    /**
     * Clears all items from the cart.
     */ 
    public void clear() {
        items.clear();
    }
}
