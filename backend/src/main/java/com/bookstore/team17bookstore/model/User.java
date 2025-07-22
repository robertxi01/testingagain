package com.bookstore.team17bookstore.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import com.bookstore.team17bookstore.model.UserRole;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//Represents a user in the bookstore system
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String password;

    // Single billing address for the user
    private String address;

    // Account status (ACTIVE or INACTIVE)
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    // Whether the user opted in for promotional emails
    private boolean promotions;

    // Role of the user (USER or ADMIN)
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;
    
    //@OneToOne(optional = true, cascade = CascadeType.ALL)
    //private Address address;

    //@OneToOne(optional = true, cascade = CascadeType.ALL)
    //private PaymentInfo paymentInfo;

    // Default constructor for JPA
    public User() { }
    // Constructor to initialize a User with name, email, phone, and password
    public User(String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public boolean isPromotions() { return promotions; }
    public void setPromotions(boolean promotions) { this.promotions = promotions; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
}
