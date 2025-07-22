package com.bookstore.team17bookstore.model;

// Simple payment card representation
public class PaymentCard {
    private Long id;
    private Long userId;
    // hashed card number
    private String cardNumber;
    // last four digits for display
    private String lastFour;

    public PaymentCard() {}

    public PaymentCard(Long userId, String cardNumber) {
        this.userId = userId;
        this.cardNumber = cardNumber;
        if (cardNumber != null && cardNumber.length() >= 4) {
            this.lastFour = cardNumber.substring(cardNumber.length() - 4);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getLastFour() { return lastFour; }
    public void setLastFour(String lastFour) { this.lastFour = lastFour; }
}
