package tubes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tubes.dao.interfaces.IKategoriDAO;
import tubes.database.DatabaseConnection;
import tubes.model.Kategori;

public class KategoriDAO implements IKategoriDAO {

    @Override
    public List<Kategori> getAll() {
        List<Kategori> list = new ArrayList<>();
        String sql = "SELECT id, nama FROM kategori ORDER BY id";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Kategori(rs.getInt("id"), rs.getString("nama")));
            }
        } catch (SQLException e) {
            System.err.println("[KategoriDAO] getAll: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Kategori findById(int id) {
        String sql = "SELECT id, nama FROM kategori WHERE id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Kategori(rs.getInt("id"), rs.getString("nama"));
                }
            }
        } catch (SQLException e) {
            System.err.println("[KategoriDAO] findById: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean save(Kategori kategori) {
        String sql = "INSERT INTO kategori (nama) VALUES (?)";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, kategori.getNama());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        kategori.setId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[KategoriDAO] save: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Kategori kategori) {
        String sql = "UPDATE kategori SET nama = ? WHERE id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kategori.getNama());
            ps.setInt(2, kategori.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[KategoriDAO] update: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM kategori WHERE id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[KategoriDAO] delete: " + e.getMessage());
            return false;
        }
    }
}
