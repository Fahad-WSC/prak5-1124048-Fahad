package tubes.controller;

import tubes.dao.KategoriDAO;
import tubes.dao.interfaces.IKategoriDAO;
import tubes.model.Kategori;

import java.util.List;

public class KategoriController {

    private final IKategoriDAO kategoriDAO;

    public KategoriController() {
        this.kategoriDAO = new KategoriDAO();
    }

    public List<Kategori> getAllKategori() {
        return kategoriDAO.getAll();
    }

    public Kategori getKategoriById(int id) {
        return kategoriDAO.findById(id);
    }

    public String tambahKategori(String nama) {
        String err = validate(nama, -1);
        if (err != null) {
            return err;
        }
        return kategoriDAO.save(new Kategori(nama.trim())) ? null : "Gagal menyimpan kategori.";
    }

    public String updateKategori(int id, String nama) {
        String err = validate(nama, id);
        if (err != null) {
            return err;
        }
        Kategori k = kategoriDAO.findById(id);
        if (k == null) {
            return "Kategori tidak ditemukan.";
        }
        k.setNama(nama.trim());
        return kategoriDAO.update(k) ? null : "Gagal mengupdate kategori.";
    }

    public String hapusKategori(int id, MenuController menuCtrl) {
        Kategori k = kategoriDAO.findById(id);
        if (k == null) {
            return "Kategori tidak ditemukan.";
        }
        boolean dipakai = menuCtrl.getAllMenu().stream()
                .anyMatch(m -> m.getKategori() != null && m.getKategori().getId() == id);
        if (dipakai) {
            return "Kategori masih dipakai oleh menu lain, tidak bisa dihapus.";
        }
        return kategoriDAO.delete(id) ? null : "Gagal menghapus kategori.";
    }

    private String validate(String nama, int excludeId) {
        if (nama == null || nama.trim().isEmpty()) {
            return "Nama kategori tidak boleh kosong.";
        }
        boolean dup = kategoriDAO.getAll().stream()
                .anyMatch(k -> k.getId() != excludeId && k.getNama().equalsIgnoreCase(nama.trim()));
        if (dup) {
            return "Nama kategori sudah dipakai.";
        }
        return null;
    }
}
