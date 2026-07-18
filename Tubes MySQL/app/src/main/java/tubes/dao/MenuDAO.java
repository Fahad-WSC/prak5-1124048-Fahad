package tubes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import tubes.dao.interfaces.IMenuDAO;
import tubes.database.DatabaseConnection;
import tubes.model.Kategori;
import tubes.model.Menu;

public class MenuDAO implements IMenuDAO {

    private static final String BASE_SELECT
            = "SELECT m.id, m.nama_menu, m.harga, m.stok, m.gambar, m.tersedia, "
            + "k.id AS kategori_id, k.nama AS kategori_nama "
            + "FROM menu m LEFT JOIN kategori k ON m.kategori_id = k.id ";

    @Override
    public List<Menu> getAll() {
        List<Menu> list = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY m.id";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[MenuDAO] getAll: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Menu> getByKategori(Kategori kategori) {
        List<Menu> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE m.kategori_id = ? AND m.tersedia = TRUE AND m.stok > 0 ORDER BY m.id";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, kategori.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[MenuDAO] getByKategori: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Menu> search(String keyword) {
        List<Menu> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE LOWER(m.nama_menu) LIKE ? ORDER BY m.id";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[MenuDAO] search: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Menu findById(int id) {
        String sql = BASE_SELECT + "WHERE m.id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[MenuDAO] findById: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean save(Menu menu) {
        String sql = "INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindMenu(ps, menu);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        menu.setId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[MenuDAO] save: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Menu menu) {
        String sql = "UPDATE menu SET nama_menu = ?, kategori_id = ?, harga = ?, stok = ?, "
                + "gambar = ?, tersedia = ? WHERE id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            bindMenu(ps, menu);
            ps.setInt(7, menu.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[MenuDAO] update: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM menu WHERE id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[MenuDAO] delete: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int getTotalMenu() {
        String sql = "SELECT COUNT(*) FROM menu";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[MenuDAO] getTotalMenu: " + e.getMessage());
        }
        return 0;
    }

    private void bindMenu(PreparedStatement ps, Menu menu) throws SQLException {
        ps.setString(1, menu.getNamaMeu());
        if (menu.getKategori() != null) {
            ps.setInt(2, menu.getKategori().getId());
        } else {
            ps.setNull(2, Types.INTEGER);
        }
        ps.setLong(3, menu.getHarga());
        ps.setInt(4, menu.getStok());
        ps.setString(5, menu.getGambar());
        ps.setBoolean(6, menu.isTersedia());
    }

    private Menu mapRow(ResultSet rs) throws SQLException {
        Kategori kategori = null;
        int kategoriId = rs.getInt("kategori_id");
        if (!rs.wasNull()) {
            kategori = new Kategori(kategoriId, rs.getString("kategori_nama"));
        }
        return new Menu(
                rs.getInt("id"),
                rs.getString("nama_menu"),
                kategori,
                rs.getLong("harga"),
                rs.getInt("stok"),
                rs.getString("gambar"),
                rs.getBoolean("tersedia"));
    }
}
