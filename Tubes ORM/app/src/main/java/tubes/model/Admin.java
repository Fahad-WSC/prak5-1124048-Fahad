package tubes.model;

import java.util.Arrays;
import java.util.List;

public class Admin extends User {

    public Admin(int id, String username, String password, String nama) {
        super(id, username, password, nama, Role.ADMIN);
    }

    public Admin(String username, String password, String nama) {
        super(username, password, nama, Role.ADMIN);
    }

    @Override
    public void showDashboard() {
        System.out.println("Membuka Admin Dashboard untuk: " + getNama());
    }

    @Override
    public String getRoleDescription() {
        return "Administrator — Akses penuh ke semua modul";
    }

    public List<String> getPermissions() {
        return Arrays.asList("MANAGE_KATEGORI", "MANAGE_MENU", "MANAGE_ORDER", "VIEW_REPORT");
    }
}
