package com.bookstore.team17bookstore.repository;

import com.bookstore.team17bookstore.model.OrderItem;
import com.bookstore.team17bookstore.model.Order;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

// Repository for managing orders and order items
@Repository
public class OrderRepository {

    private final DataSource dataSource;

    private static final String INSERT_ORDER =
        "INSERT INTO orders (userId, createdAt, totalBeforeTax, tax, totalAfterTax) " +
        "VALUES (?, ?, ?, ?, ?)";

    private static final String INSERT_ORDER_ITEM =
        "INSERT INTO order_items (orderId, bookId, title, quantity, price, lineTotal) " +
        "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ORDER_BY_USER =
        "SELECT * FROM orders WHERE userId = ? ORDER BY createdAt DESC";

    private static final String SELECT_ORDER_ITEMS_BY_ORDER =
        "SELECT * FROM order_items WHERE orderId = ?";

    public OrderRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     *  Save an order and its items to the database.
     * @param o the order to save
     * @return the saved order
     * @throws SQLException on error
     */
    public Order save(Order o) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, o.getUserId());
                ps.setTimestamp(2, Timestamp.valueOf(o.getCreatedAt()));
                ps.setBigDecimal(3, BigDecimal.valueOf(o.getTotalBeforeTax()));
                ps.setBigDecimal(4, BigDecimal.valueOf(o.getTax()));
                ps.setBigDecimal(5, BigDecimal.valueOf(o.getTotalAfterTax()));
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        o.setId(rs.getLong(1));
                    } else {
                        throw new SQLException("Failed to retrieve generated order ID");
                    }
                }
            }

            //batch-insert items
            try (PreparedStatement ps = conn.prepareStatement(INSERT_ORDER_ITEM)) {
                for (OrderItem item : o.getItems()) {
                    ps.setLong(1, o.getId());
                    ps.setLong(2, item.getBookId());
                    ps.setString(3, item.getTitle());
                    ps.setInt(4, item.getQuantity());
                    ps.setBigDecimal(5, BigDecimal.valueOf(item.getPrice()));
                    ps.setBigDecimal(6, BigDecimal.valueOf(item.getLineTotal()));
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            conn.commit();
        }
        return o;
    }

    /**
     * Find orders by user ID.
     * @param userId the ID of the user
     * @return a list of orders for the user
     * @throws SQLException on error
     */
    public List<Order> findByUserId(Long userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ORDER_BY_USER)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = mapOrder(rs);
                    o.setItems(findItemsByOrderId(o.getId(), conn));
                    orders.add(o);
                }
            }
        }
        return orders;
    }

    /**
     * Find items by order ID.
     * @param orderId the ID of the order
     * @param conn the database connection
     * @return a list of items for the order
     * @throws SQLException on error
     */
    private List<OrderItem> findItemsByOrderId(Long orderId, Connection conn) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ORDER_ITEMS_BY_ORDER)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem(
                        rs.getLong("book_id"),
                        rs.getString("title"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                    );
                    items.add(item);
                }
            }
        }
        return items;
    }

    /**
     * Map a ResultSet to an Order object.
     * @param rs the ResultSet to map
     * @return the mapped Order object
     * @throws SQLException on error
     */
    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getLong("id"));
        o.setUserId(rs.getLong("user_id"));
        o.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        o.setTotalBeforeTax(rs.getBigDecimal("total_before_tax").doubleValue());
        o.setTax(rs.getBigDecimal("tax").doubleValue());
        o.setTotalAfterTax(rs.getBigDecimal("total_after_tax").doubleValue());
        return o;
    }
}
