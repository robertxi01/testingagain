package com.bookstore.team17bookstore.service;

import com.bookstore.team17bookstore.model.User;
import com.bookstore.team17bookstore.repository.UserRepository;
import com.bookstore.team17bookstore.repository.PaymentCardRepository;
import com.bookstore.team17bookstore.model.PaymentCard;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

// Service for managing users
@Service
public class UserService {
    private final UserRepository repo;
    private final JavaMailSender mailSender;
    private final PaymentCardRepository cardRepo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // simple in-memory reset token store
    private final Map<String, String> resetTokens = new HashMap<>();

    public UserService(UserRepository repo, PaymentCardRepository cardRepo, JavaMailSender mailSender) {
        this.repo = repo;
        this.cardRepo = cardRepo;
        this.mailSender = mailSender;
    }

    /**
     * Registers a new user.
     * @param user the user to register
     * @return the registered user with its ID set
     * @throws SQLException on error
     */
    public User register(User user) throws SQLException {
        // hash password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = repo.save(user);

        // send confirmation email
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(saved.getEmail());
        msg.setSubject("Welcome to the Bookstore");
        msg.setText("Your account has been created.");
        mailSender.send(msg);

        return saved;
    }

    /**
     * Finds a user by email.
     * @param email the email to search for
     * @return an Optional containing the user if found, or empty if not found
     * @throws SQLException on error
     */
    public Optional<User> findByEmail(String email) throws SQLException {
        return repo.findByEmail(email);
    }

    /**
     * Finds a user by ID.
     * @param id the ID of the user
     * @return an Optional containing the user if found, or empty if not found
     * @throws SQLException on error
     */
    public Optional<User> findById(Long id) throws SQLException {
        return repo.findById(id);
    }

    /**
     * Retrieves the ID of a user by their email.
     * @param email the email of the user
     * @return the user ID
     * @throws SQLException if there is an error accessing the database
     * @throws RuntimeException if no user is found with the given email
     */
    public Long idByEmail(String email) throws SQLException {
        return findByEmail(email).map(User::getId).orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
    
    /**
     * Verifies user credentials.
     * @param email the user's email
     * @param password the user's password
     * @return true if credentials are valid, false otherwise
     * @throws SQLException if there is an error accessing the database
     */
    public boolean verifyCredentials(String email, String password) throws SQLException {
        Optional<User> userOpt = findByEmail(email);
        return userOpt.map(user -> passwordEncoder.matches(password, user.getPassword()))
                      .orElse(false);
    }

    /**
     * Updates a user profile.
     * @param user the user with updated fields
     * @return the updated user
     */
    public User update(User user) throws SQLException {
        User existing = repo.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        existing.setName(user.getName());
        existing.setPhone(user.getPhone());
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        existing.setAddress(user.getAddress());
        existing.setPromotions(user.isPromotions());
        existing.setRole(user.getRole());
        existing.setStatus(user.getStatus());
        User updated = repo.update(existing);

        // notify user of profile update
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(updated.getEmail());
        msg.setSubject("Profile Updated");
        msg.setText("Your profile information has been changed.");
        mailSender.send(msg);

        return updated;
    }

    /**
     * Returns payment cards for a user.
     */
    public java.util.List<PaymentCard> cards(Long userId) throws SQLException {
        return cardRepo.findByUser(userId);
    }

    /**
     * Adds a payment card ensuring max 4 cards per user.
     */
    public void addCard(Long userId, String cardNumber) throws SQLException {
        if (cardRepo.findByUser(userId).size() >= 4) {
            throw new RuntimeException("Maximum cards reached");
        }
        String hash = passwordEncoder.encode(cardNumber);
        String last4 = cardNumber.length() >= 4 ? cardNumber.substring(cardNumber.length() - 4) : cardNumber;
        cardRepo.addCard(userId, hash, last4);
    }

    /**
     * Deletes a payment card for the user.
     */
    public void deleteCard(Long userId, Long cardId) throws SQLException {
        cardRepo.delete(userId, cardId);
    }

    /**
     * Initiates a password reset by generating a token and emailing it.
     * @param email the user's email
     */
    public void sendResetToken(String email) throws SQLException {
        Optional<User> userOpt = findByEmail(email);
        if (userOpt.isEmpty()) return; // silently ignore

        String token = java.util.UUID.randomUUID().toString();
        resetTokens.put(token, email);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Password Reset");
        msg.setText("Use this token to reset your password: " + token);
        mailSender.send(msg);
    }

    /**
     * Resets the user's password using the given token.
     */
    public void resetPassword(String token, String newPassword) throws SQLException {
        String email = resetTokens.remove(token);
        if (email == null) throw new RuntimeException("Invalid token");

        User user = findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(newPassword);
        update(user);
    }
}
