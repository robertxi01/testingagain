package com.bookstore.team17bookstore.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Represents an order in the bookstore system
@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
    private double totalBeforeTax;
    private double tax;
    private double totalAfterTax;

    @Transient
    private List<OrderItem> items = new ArrayList<>();

    // Default constructor for JPA
    public Order() { }

    // Constructor to initialize an Order with userId, totalBeforeTax, tax, and totalAfterTax
    public Order(Long userId, double totalBeforeTax, double tax, double totalAfterTax) {
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.totalBeforeTax = totalBeforeTax;
        this.tax = tax;
        this.totalAfterTax = totalAfterTax;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public double getTotalBeforeTax() { return totalBeforeTax; }
    public void setTotalBeforeTax(double totalBeforeTax) { this.totalBeforeTax = totalBeforeTax; }

    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }

    public double getTotalAfterTax() { return totalAfterTax; }
    public void setTotalAfterTax(double totalAfterTax) { this.totalAfterTax = totalAfterTax; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    
}
