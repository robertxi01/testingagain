package com.bookstore.team17bookstore.repository;

import com.bookstore.team17bookstore.model.CartItem;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CartRepository {
    private final DataSource dataSource;

    private static final String SELECT_BY_USER =
        "SELECT * FROM cart_items WHERE user_id=?";
    private static final String INSERT_OR_UPDATE =
        "INSERT INTO cart_items (user_id, book_id, quantity) VALUES (?, ?, ?) " +
        "ON DUPLICATE KEY UPDATE quantity=VALUES(quantity)";
    private static final String DELETE_ITEM =
        "DELETE FROM cart_items WHERE user_id=? AND book_id=?";
    private static final String CLEAR =
        "DELETE FROM cart_items WHERE user_id=?";

    public CartRepository(DataSource ds) { this.dataSource = ds; }

    public List<CartItem> findByUser(Long userId) throws SQLException {
        List<CartItem> list = new ArrayList<>();
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_USER)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem ci = new CartItem();
                    ci.setBookId(rs.getLong("book_id"));
                    ci.setQuantity(rs.getInt("quantity"));
                    list.add(ci);
                }
            }
        }
        return list;
    }

    public void upsert(Long userId, Long bookId, int qty) throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT_OR_UPDATE)) {
            ps.setLong(1, userId);
            ps.setLong(2, bookId);
            ps.setInt(3, qty);
            ps.executeUpdate();
        }
    }

    public void delete(Long userId, Long bookId) throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(DELETE_ITEM)) {
            ps.setLong(1, userId);
            ps.setLong(2, bookId);
            ps.executeUpdate();
        }
    }

    public void clear(Long userId) throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(CLEAR)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        }
    }
}
