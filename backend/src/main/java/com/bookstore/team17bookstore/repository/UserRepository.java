package com.bookstore.team17bookstore.repository;

import com.bookstore.team17bookstore.model.User;
import com.bookstore.team17bookstore.model.UserStatus;
import com.bookstore.team17bookstore.model.UserRole;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

// Repository for managing users
@Repository
public class UserRepository {
    private final DataSource dataSource;

    private static final String INSERT =
        "INSERT INTO users (name, email, phone, password, address, status, promotions, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_EMAIL =
        "SELECT * FROM users WHERE email = ?";

    private static final String SELECT_BY_ID =
        "SELECT * FROM users WHERE id = ?";

    private static final String UPDATE =
        "UPDATE users SET name=?, phone=?, password=?, address=?, status=?, promotions=?, role=? WHERE id=?";

    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Save a user to the database.
     * @param user the user to save
     * @return the saved user with its ID set if it was newly created
     * @throws SQLException on error
     */
    public User save(User user) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getAddress());
            ps.setString(6, user.getStatus().name());
            ps.setBoolean(7, user.isPromotions());
            ps.setString(8, user.getRole().name());
            ps.executeUpdate();

           try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getLong(1));
                }
            }
        }
        return user;
    }

    /**
     * Find a user by email.
     * @param email the email to search for
     * @return an Optional containing the user if found, or empty if not
     * @throws SQLException on error
     */
    public Optional<User> findByEmail(String email) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_EMAIL)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                else return Optional.empty();
            }
        }
    }

    /**
     * Find a user by ID.
     * @param id the ID of the user
     * @return an Optional containing the user if found, or empty if not
     * @throws SQLException on error
     */
    public Optional<User> findById(Long id) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                else return Optional.empty();
            }
        }
    }

    /**
     * Update an existing user.
     * @param user the user with updated fields
     * @return the same user
     */
    public User update(User user) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getAddress());
            ps.setString(5, user.getStatus().name());
            ps.setBoolean(6, user.isPromotions());
            ps.setString(7, user.getRole().name());
            ps.setLong(8, user.getId());
            ps.executeUpdate();
        }
        return user;
    }

    /**
     * Map a ResultSet row to a User object.
     * @param rs the ResultSet containing user data
     * @return a User object populated with data from the ResultSet
     * @throws SQLException on error
     */
    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPhone(rs.getString("phone"));
        u.setPassword(rs.getString("password"));
        u.setAddress(rs.getString("address"));
        u.setStatus(UserStatus.valueOf(rs.getString("status")));
        u.setPromotions(rs.getBoolean("promotions"));
        u.setRole(UserRole.valueOf(rs.getString("role")));
        return u;
    }
}
