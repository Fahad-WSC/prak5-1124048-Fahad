package tubes.controller;

import tubes.database.DataStore;
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

        for (Admin admin : DataStore.getInstance().getAdminList()) {
            if (admin.getUsername().equalsIgnoreCase(username.trim())) {
                if (admin.getPassword().equals(password)) {
                    SessionManager.getInstance().login(admin);
                    return null;
                }
                return "Password salah.";
            }
        }
        return "Username tidak ditemukan.";
    }

    public void logout() {
        SessionManager.getInstance().logout();
    }
}
