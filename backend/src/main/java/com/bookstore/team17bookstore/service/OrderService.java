package com.bookstore.team17bookstore.service;

import com.bookstore.team17bookstore.model.*;
import com.bookstore.team17bookstore.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Service for managing orders
@Service
public class OrderService {
    private static final double TAX_RATE = 0.08;

    private final OrderRepository orderRepo;
    private final BookService bookSvc;
    private final CartService cartSvc;
    private final JavaMailSender mailer;
    private final UserService userSvc;

    public OrderService(OrderRepository orderRepo, BookService bookSvc, CartService cartSvc, JavaMailSender mailer, UserService userSvc) {
        this.orderRepo = orderRepo;
        this.bookSvc = bookSvc;
        this.cartSvc = cartSvc;
        this.mailer = mailer;
        this.userSvc = userSvc;
    }

    /**
     * Creates a new order based on the user's cart.
     * @param userEmail the email of the user
     * @param shippingAddress the shipping address for the order
     * @param paymentMethod the payment method for the order
     * @return the created order
     * @throws SQLException if there is an error accessing the database
     * @throws RuntimeException if the cart is empty or a book is not found
     */
    public Order checkout(String userEmail, String shippingAddress, String paymentMethod) throws SQLException {
        Cart cart = cartSvc.getCart(userEmail);
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot create order.");
        }

        Long userId = userSvc.idByEmail(userEmail);

        List<OrderItem> orderItems = new ArrayList<>();
        double subtotal = 0.0;

        for (CartItem item : cart.getItems()) {
            String title = bookSvc.findById(item.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found with ID: " + item.getBookId()))
                    .getTitle();
            OrderItem orderItem = new OrderItem(item.getBookId(), title, item.getQuantity(), item.getUnitPrice());
            orderItems.add(orderItem);
            subtotal += item.getLineTotal();
        }

        double tax = round2(subtotal * TAX_RATE);
        double finalAmount = round2(subtotal + tax);

        Order order = new Order();
        order.setUserId(userId);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalBeforeTax(subtotal);
        order.setTax(tax);
        order.setTotalAfterTax(finalAmount);
        order.setItems(orderItems);

        // Clear the cart after successful checkout
        Order saved = orderRepo.save(order);
        cartSvc.clearCart(userEmail);

        // Send confirmation email
        sendConfirmationEmail(userEmail, saved);

        return saved;
    }

    /**
     * Retrieves the order history for a user.
     * @param userEmail the email of the user
     * @return a list of orders for the user
     * @throws SQLException if there is an error accessing the database
     * @throws IllegalArgumentException if no user is found with the given email
     */
    public List<Order> historyByEmail(String userEmail) throws SQLException {
        Long userId = userSvc.idByEmail(userEmail);
        if (userId == null) {
            throw new IllegalArgumentException("No user found with email: " + userEmail);
        }
        return orderRepo.findByUserId(userId);
    }

    /**
     * Reorders items from a previous order into the user's cart.
     * @param userEmail the email of the user
     * @param oldOrder the order to reorder
     * @return the updated cart after reordering
     * @throws SQLException if there is an error accessing the database
     */
    public Cart reorder(String userEmail, Order oldOrder) throws SQLException {
        for (OrderItem item : oldOrder.getItems()) {
            cartSvc.addItem(userEmail, item.getBookId(), item.getQuantity());
        }
        return cartSvc.getCart(userEmail);
    }

    /**
     * Rounds a double value to two decimal places.
     * @param value the value to round
     * @return the rounded value
     */
    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    /**
     * Sends a confirmation email to the user after an order is placed.
     * @param to the email address of the user
     * @param order the order details
     */
    private void sendConfirmationEmail(String to, Order order) {
        StringBuilder body = new StringBuilder();
        body.append("Thank you for your order!\n\n");
        body.append("Order Summary:\n");

        for (OrderItem item : order.getItems()) {
            body.append("- ").append(item.getTitle())
                .append(" (Qty: ").append(item.getQuantity())
                .append(" @ $").append(item.getPrice()).append(")\n");
        }

        body.append("\nSubtotal: $").append(order.getTotalBeforeTax())
            .append("\nTax: $").append(order.getTax())
            .append("\nTotal: $").append(order.getTotalAfterTax());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Order Confirmation");
        message.setText(body.toString());
        mailer.send(message);
    }
}