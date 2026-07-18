package tubes.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tubes.database.DatabaseConnection;
import tubes.model.Admin;
import tubes.util.SessionManager;

public class AuthController {

    public String login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return "Username tidak boleh kosong.";
        }
        if (password == null || password.trim().isEmpty()) {
            return "Password tidak boleh kosong.";
        }

        String sql = "SELECT id, username, password, nama FROM admin WHERE username = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return "Username tidak ditemukan.";
                }
                String dbPassword = rs.getString("password");
                if (!dbPassword.equals(password)) {
                    return "Password salah.";
                }
                Admin admin = new Admin(
                        rs.getInt("id"),
                        rs.getString("username"),
                        dbPassword,
                        rs.getString("nama"));
                SessionManager.getInstance().login(admin);
                return null;
            }
        } catch (SQLException e) {
            System.err.println("[AuthController] login: " + e.getMessage());
            return "Gagal terhubung ke database.";
        }
    }

    public void logout() {
        SessionManager.getInstance().logout();
    }
}
