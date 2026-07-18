package tubes.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL
            = "jdbc:mysql://localhost:3306/mcdonald_kiosk"
            + "?useSSL=false"
            + "&serverTimezone=Asia/Jakarta"
            + "&allowPublicKeyRetrieval=true"
            + "&characterEncoding=utf8";

    private static final String USER = "root";
    private static final String PASS = "";

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("[DB] Koneksi berhasil.");
        } catch (ClassNotFoundException e) {
            System.err.println("[DB] Driver tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("[DB] Koneksi gagal: " + e.getMessage());
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASS);
            }
        } catch (SQLException e) {
            System.err.println("[DB] Reconnect gagal: " + e.getMessage());
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Koneksi ditutup.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Error menutup koneksi: " + e.getMessage());
        }
    }
}
