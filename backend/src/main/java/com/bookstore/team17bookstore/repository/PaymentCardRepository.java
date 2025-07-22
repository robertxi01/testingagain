package com.bookstore.team17bookstore.repository;

import com.bookstore.team17bookstore.model.PaymentCard;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PaymentCardRepository {
    private final DataSource dataSource;

    private static final String INSERT =
        "INSERT INTO payment_cards (user_id, card_number, last_four) VALUES (?, ?, ?)";
    private static final String SELECT_BY_USER =
        "SELECT id, user_id, last_four FROM payment_cards WHERE user_id=?";
    private static final String DELETE =
        "DELETE FROM payment_cards WHERE id=? AND user_id=?";

    public PaymentCardRepository(DataSource ds) { this.dataSource = ds; }

    public void addCard(Long userId, String cardNumberHash, String lastFour) throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT)) {
            ps.setLong(1, userId);
            ps.setString(2, cardNumberHash);
            ps.setString(3, lastFour);
            ps.executeUpdate();
        }
    }

    public List<PaymentCard> findByUser(Long userId) throws SQLException {
        List<PaymentCard> list = new ArrayList<>();
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_USER)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PaymentCard pc = new PaymentCard();
                    pc.setId(rs.getLong("id"));
                    pc.setUserId(rs.getLong("user_id"));
                    pc.setLastFour(rs.getString("last_four"));
                    list.add(pc);
                }
            }
        }
        return list;
    }

    public void delete(Long userId, Long cardId) throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(DELETE)) {
            ps.setLong(1, cardId);
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }
}
