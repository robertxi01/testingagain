package com.bookstore.team17bookstore.controller;

import com.bookstore.team17bookstore.model.Cart;
import com.bookstore.team17bookstore.model.Order;
import com.bookstore.team17bookstore.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.List;

// Controller for managing orders in the bookstore
// Provides endpoints for checking out, viewing order history, and reordering
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService svc;

    public OrderController(OrderService orderService) {
        this.svc = orderService;
    }

    /**
     * Checks out the user's cart and creates an order.
     * This method requires the user's email, shipping address, and payment method.
     * It retrieves the user's cart, calculates the order total, and creates an Order object.
     * It also sends a confirmation email to the user.
     * @param email the user's email address
     * @param address the shipping address
     * @param paymentMethod the payment method
     * @return the created Order object
     * @throws SQLException if there is an error accessing the database
     */
    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public Order checkout(@RequestParam String email, 
                          @RequestParam String address, 
                          @RequestParam String paymentMethod) throws SQLException {
        return svc.checkout(email, address, paymentMethod);
    }

    /**
     * Retrieves the order history for a given user.
     * @param email the user's email address
     * @return a list of the user's past orders
     * @throws SQLException if there is an error accessing the database
     */
    @GetMapping("/history/{email}")
        public List<Order> getOrderHistory(@PathVariable String email) throws SQLException {
        try {
            return svc.historyByEmail(email);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    /**
     * Reorders a previous order by its ID for a given user.
     * This method retrieves the user's order history, finds the specified order,
     * and creates a new cart based on that order.
     * @param orderId the ID of the order to reorder
     * @param email the user's email address
     * @return the new Cart object created from the reordered items
     * @throws SQLException if there is an error accessing the database
     */
    @PostMapping("/reorder/{orderId}")
    public Cart reorder(@PathVariable Long orderId, 
                          @RequestParam String email) throws SQLException {
        List<Order> history = svc.historyByEmail(email);
        Order order = history.stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        return svc.reorder(email, order);
    }
}
