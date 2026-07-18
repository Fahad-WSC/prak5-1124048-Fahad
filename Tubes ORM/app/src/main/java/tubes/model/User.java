package tubes.model;

public abstract class User {

    private int id;
    private String username;
    private String password;
    private String nama;
    private Role role;

    public User(int id, String username, String password, String nama, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nama = nama;
        this.role = role;
    }

    public User(String username, String password, String nama, Role role) {
        this(0, username, password, nama, role);
    }

    public abstract void showDashboard();

    public abstract String getRoleDescription();

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNama() {
        return nama;
    }

    public Role getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String u) {
        this.username = u;
    }

    public void setPassword(String p) {
        this.password = p;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', role=" + role + "}";
    }
}
