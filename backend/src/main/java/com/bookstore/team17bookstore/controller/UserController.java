package com.bookstore.team17bookstore.controller;

import com.bookstore.team17bookstore.model.User;
import com.bookstore.team17bookstore.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

// Controller for managing user accounts
// Provides endpoints for user registration, login, and logout
@RestController
@RequestMapping("/users")
public class UserController {
    
    private final UserService svc;

    public UserController(UserService userService) {
        this.svc = userService;
    }

    /**
     * Registers a new user account.
     * @param user the User object containing registration details
     * @return the registered User object
     * @throws SQLException if there is an error accessing the database
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody User user) throws SQLException {
        if (svc.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        return svc.register(user);
    }

    /**
     * Logs in a user.
     * @param email the user's email address
     * @param password the user's password
     * @return a map containing the session token
     * @throws SQLException if there is an error accessing the database
     */
    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String email, @RequestParam String password) throws SQLException {
        boolean ok = svc.verifyCredentials(email, password);
        if (!ok) throw new RuntimeException("Invalid email or password");

        User user = svc.findByEmail(email).orElseThrow();
        if (user.getStatus() == com.bookstore.team17bookstore.model.UserStatus.INACTIVE) {
            throw new RuntimeException("Account inactive");
        }

        Map<String, String> resp = new HashMap<>();
        resp.put("token", email); // placeholder token
        resp.put("userId", String.valueOf(user.getId()));
        resp.put("role", user.getRole().name());
        resp.put("status", user.getStatus().name());
        return resp;
    }

    /**
     * Logs out the current user.
     */
    @PostMapping("/logout")
    public void logout() {
        //stateless stub; in a real system you'd invalidate the session/JWT
    }

    /**
     * Retrieve a user by id.
     */
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) throws SQLException {
        return svc.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Updates an existing user profile.
     * @param id the user id
     * @param user the user data
     * @return the updated user
     */
    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) throws SQLException {
        user.setId(id);
        return svc.update(user);
    }

    /** Get payment cards for a user */
    @GetMapping("/{id}/cards")
    public java.util.List<com.bookstore.team17bookstore.model.PaymentCard> cards(@PathVariable Long id) throws SQLException {
        return svc.cards(id);
    }

    /** Add a new payment card */
    @PostMapping("/{id}/cards")
    public void addCard(@PathVariable Long id, @RequestParam String cardNumber) throws SQLException {
        svc.addCard(id, cardNumber);
    }

    /** Delete a payment card */
    @DeleteMapping("/{id}/cards/{cardId}")
    public void deleteCard(@PathVariable Long id, @PathVariable Long cardId) throws SQLException {
        svc.deleteCard(id, cardId);
    }

    /**
     * Sends a password reset token to the user's email.
     */
    @PostMapping("/forgot-password")
    public void forgotPassword(@RequestParam String email) throws SQLException {
        svc.sendResetToken(email);
    }

    /**
     * Resets the user's password using a token.
     */
    @PostMapping("/reset-password")
    public void resetPassword(@RequestParam String token, @RequestParam String password) throws SQLException {
        svc.resetPassword(token, password);
    }
}
