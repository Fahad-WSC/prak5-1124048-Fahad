package tubes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import tubes.dao.interfaces.IOrderDAO;
import tubes.database.DatabaseConnection;
import tubes.model.Kategori;
import tubes.model.Menu;
import tubes.model.Order;
import tubes.model.OrderItem;
import tubes.model.PaymentMethod;
import tubes.model.Status;

public class OrderDAO implements IOrderDAO {

    private static final String SELECT_ORDER
            = "SELECT id, customer, user_id, status, total, pajak, bayar, kembalian, "
            + "payment_method, catatan, tanggal FROM orders ";

    private static final String SELECT_ITEMS
            = "SELECT oi.id AS item_id, oi.qty, oi.harga, oi.subtotal, "
            + "m.id AS menu_id, m.nama_menu, m.harga AS menu_harga, m.stok, m.gambar, m.tersedia, "
            + "k.id AS kategori_id, k.nama AS kategori_nama "
            + "FROM order_items oi "
            + "JOIN menu m ON oi.menu_id = m.id "
            + "LEFT JOIN kategori k ON m.kategori_id = k.id "
            + "WHERE oi.order_id = ? ORDER BY oi.id";

    @Override
    public int save(Order order) {
        String sqlOrder = "INSERT INTO orders (customer, user_id, status, total, pajak, bayar, "
                + "kembalian, payment_method, catatan, tanggal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) "
                + "VALUES (?, ?, ?, ?, ?)";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, order.getCustomer());
                ps.setInt(2, order.getUserId());
                ps.setString(3, order.getStatus().name());
                ps.setLong(4, order.getTotal());
                ps.setLong(5, order.getPajak());
                ps.setLong(6, order.getBayar());
                ps.setLong(7, order.getKembalian());
                ps.setString(8, order.getPaymentMethod() != null ? order.getPaymentMethod().name() : null);
                ps.setString(9, order.getCatatan());
                LocalDateTime tanggal = order.getTanggal() != null ? order.getTanggal() : LocalDateTime.now();
                ps.setTimestamp(10, Timestamp.valueOf(tanggal));
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        order.setId(keys.getInt(1));
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlItem)) {
                for (OrderItem item : order.getItems()) {
                    ps.setInt(1, order.getId());
                    ps.setInt(2, item.getMenu().getId());
                    ps.setInt(3, item.getQty());
                    ps.setLong(4, item.getHarga());
                    ps.setLong(5, item.getSubtotal());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
            return order.getId();
        } catch (SQLException e) {
            System.err.println("[OrderDAO] save: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("[OrderDAO] rollback: " + ex.getMessage());
            }
            return -1;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                System.err.println("[OrderDAO] setAutoCommit(true): " + ex.getMessage());
            }
        }
    }

    @Override
    public boolean updateStatus(int id, Status status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[OrderDAO] updateStatus: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Order> getAll() {
        return queryOrders(SELECT_ORDER + "ORDER BY tanggal DESC, id DESC");
    }

    @Override
    public List<Order> getByStatus(Status status) {
        return queryOrders(SELECT_ORDER + "WHERE status = ? ORDER BY tanggal DESC, id DESC", status.name());
    }

    @Override
    public List<Order> getByUserId(int userId) {
        return queryOrders(SELECT_ORDER + "WHERE user_id = ? ORDER BY tanggal DESC, id DESC", userId);
    }

    @Override
    public List<Order> getTodayOrders() {
        return queryOrders(SELECT_ORDER + "WHERE DATE(tanggal) = CURDATE() ORDER BY tanggal DESC, id DESC");
    }

    @Override
    public Order findById(int id) {
        List<Order> result = queryOrders(SELECT_ORDER + "WHERE id = ?", id);
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public long getTotalPendapatan() {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM orders";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            System.err.println("[OrderDAO] getTotalPendapatan: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public int getTotalOrder() {
        String sql = "SELECT COUNT(*) FROM orders";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[OrderDAO] getTotalOrder: " + e.getMessage());
        }
        return 0;
    }

    private List<Order> queryOrders(String sql, Object... params) {
        List<Order> orders = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
            for (Order order : orders) {
                loadItems(conn, order);
            }
        } catch (SQLException e) {
            System.err.println("[OrderDAO] queryOrders: " + e.getMessage());
        }
        return orders;
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("tanggal");
        Order order = new Order(
                rs.getInt("id"),
                rs.getString("customer"),
                rs.getInt("user_id"),
                Status.valueOf(rs.getString("status")),
                rs.getLong("total"),
                rs.getString("catatan"),
                ts != null ? ts.toLocalDateTime() : null);
        order.setPajak(rs.getLong("pajak"));
        order.setBayar(rs.getLong("bayar"));
        order.setKembalian(rs.getLong("kembalian"));
        String metode = rs.getString("payment_method");
        if (metode != null) {
            order.setPaymentMethod(PaymentMethod.valueOf(metode));
        }
        return order;
    }

    private void loadItems(Connection conn, Order order) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ITEMS)) {
            ps.setInt(1, order.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Kategori kategori = null;
                    int kategoriId = rs.getInt("kategori_id");
                    if (!rs.wasNull()) {
                        kategori = new Kategori(kategoriId, rs.getString("kategori_nama"));
                    }
                    Menu menu = new Menu(
                            rs.getInt("menu_id"),
                            rs.getString("nama_menu"),
                            kategori,
                            rs.getLong("menu_harga"),
                            rs.getInt("stok"),
                            rs.getString("gambar"),
                            rs.getBoolean("tersedia"));
                    OrderItem item = new OrderItem(
                            rs.getInt("item_id"),
                            order.getId(),
                            menu,
                            rs.getInt("qty"),
                            rs.getLong("harga"),
                            rs.getLong("subtotal"));
                    items.add(item);
                }
            }
        }
        order.setItems(items);
    }
}
