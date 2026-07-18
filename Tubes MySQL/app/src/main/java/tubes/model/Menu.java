package tubes.model;

public class Menu {

    private int id;
    private String namaMenu;
    private Kategori kategori;
    private long harga;
    private int stok;
    private String gambar;
    private boolean tersedia;

    public Menu(int id, String namaMenu, Kategori kategori,
            long harga, int stok, String gambar, boolean tersedia) {
        this.id = id;
        this.namaMenu = namaMenu;
        this.kategori = kategori;
        this.harga = harga;
        this.stok = stok;
        this.gambar = gambar;
        this.tersedia = tersedia;
    }

    public Menu(String namaMenu, Kategori kategori, long harga, int stok, String gambar) {
        this(0, namaMenu, kategori, harga, stok, gambar, true);
    }

    public int getId() {
        return id;
    }

    public String getNamaMeu() {
        return namaMenu;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public long getHarga() {
        return harga;
    }

    public int getStok() {
        return stok;
    }

    public String getGambar() {
        return gambar;
    }

    public boolean isTersedia() {
        return tersedia && stok > 0;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNamaMeu(String n) {
        this.namaMenu = n;
    }

    public void setKategori(Kategori k) {
        this.kategori = k;
    }

    public void setHarga(long harga) {
        this.harga = harga;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public void setTersedia(boolean t) {
        this.tersedia = t;
    }

    public void kurangiStok(int qty) {
        if (this.stok >= qty) {
            this.stok -= qty;
        }
    }

    @Override
    public String toString() {
        return namaMenu + " - Rp " + harga;
    }
}
