package com.bookstore.team17bookstore.model;

// Represents an item in the shopping cart
public class CartItem {
    private Long bookId;
    private int quantity;
    private double unitPrice;

    // Default constructor
    public CartItem() { }
    // Constructor to initialize a CartItem with bookId, quantity, and unitPrice
    public CartItem(Long bookId, int quantity, double unitPrice) {
        this.bookId = bookId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    //Add a method to check quantity vs stock

    /**
     * Calculates the total price for this cart item.
     * @return The total price (quantity * unitPrice).
     */
    public double getLineTotal() {
        return quantity * unitPrice;
    }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
}
