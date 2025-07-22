package com.bookstore.team17bookstore.model;

// Represents an item in an order, containing details about the book and its quantity
public class OrderItem {
    private Long bookId;
    private String title;
    private int quantity;
    private double price;

    // Default constructor for JPA and serialization
    public OrderItem() { }
    // Constructor to initialize an OrderItem with bookId, title, quantity, and price
    public OrderItem(Long bookId, String title, int quantity, double price) {
        this.bookId = bookId;
        this.title = title;
        this.quantity = quantity;
        this.price = price;
    }

    // Calculates the total price for this order item
    public double getLineTotal() {
        return quantity * price;
    }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        if (quantity > 0) {
            this.quantity = quantity;
        } else {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }

    public double getPrice() { return price; }
    public void setPrice(double price) {
        if (price > 0) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Price must be positive");
        }
    }

}
