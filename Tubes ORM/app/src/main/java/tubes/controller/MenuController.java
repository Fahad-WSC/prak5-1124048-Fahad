package tubes.controller;

import tubes.dao.MenuDAO;
import tubes.dao.interfaces.IMenuDAO;
import tubes.model.Kategori;
import tubes.model.Menu;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class MenuController {

    private final IMenuDAO menuDAO;
    private static final String IMAGE_DIR = "assets/images/";

    public MenuController() {
        this.menuDAO = new MenuDAO();
    }

    public List<Menu> getAllMenu() {
        return menuDAO.getAll();
    }

    public List<Menu> getMenuByKategori(Kategori k) {
        return menuDAO.getByKategori(k);
    }

    public Menu getMenuById(int id) {
        return menuDAO.findById(id);
    }

    public int getTotalMenu() {
        return menuDAO.getTotalMenu();
    }

    public List<Menu> searchMenu(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return menuDAO.getAll();
        }
        return menuDAO.search(keyword.trim());
    }

    public String tambahMenu(String nama, Kategori kategori,
            String hargaStr, String stokStr, File fileGambar) {
        String err = validateInput(nama, kategori, hargaStr, stokStr);
        if (err != null) {
            return err;
        }

        long harga = Long.parseLong(hargaStr.replaceAll("[^0-9]", ""));
        int stok = Integer.parseInt(stokStr.trim());
        String namaGambar = copyGambar(fileGambar);

        Menu menu = new Menu(nama.trim(), kategori, harga, stok, namaGambar);
        return menuDAO.save(menu) ? null : "Gagal menyimpan menu.";
    }

    public String updateMenu(int id, String nama, Kategori kategori,
            String hargaStr, String stokStr, File fileGambar) {
        String err = validateInput(nama, kategori, hargaStr, stokStr);
        if (err != null) {
            return err;
        }

        Menu existing = menuDAO.findById(id);
        if (existing == null) {
            return "Menu tidak ditemukan.";
        }

        long harga = Long.parseLong(hargaStr.replaceAll("[^0-9]", ""));
        int stok = Integer.parseInt(stokStr.trim());
        String namaGambar = (fileGambar != null) ? copyGambar(fileGambar) : existing.getGambar();

        existing.setNamaMeu(nama.trim());
        existing.setKategori(kategori);
        existing.setHarga(harga);
        existing.setStok(stok);
        existing.setGambar(namaGambar);
        existing.setTersedia(true);

        return menuDAO.update(existing) ? null : "Gagal mengupdate menu.";
    }

    public String hapusMenu(int id) {
        if (menuDAO.findById(id) == null) {
            return "Menu tidak ditemukan.";
        }
        return menuDAO.delete(id) ? null : "Gagal menghapus menu.";
    }

    private String validateInput(String nama, Kategori kategori,
            String hargaStr, String stokStr) {
        if (nama == null || nama.trim().isEmpty()) {
            return "Nama menu tidak boleh kosong.";
        }
        if (kategori == null) {
            return "Kategori tidak valid.";
        }
        try {
            long h = Long.parseLong(hargaStr.replaceAll("[^0-9]", ""));
            if (h <= 0) {
                return "Harga harus lebih dari 0.";
            }
        } catch (NumberFormatException e) {
            return "Harga tidak valid.";
        }
        try {
            int s = Integer.parseInt(stokStr.trim());
            if (s < 0) {
                return "Stok tidak boleh negatif.";
            }
        } catch (NumberFormatException e) {
            return "Stok tidak valid.";
        }
        return null;
    }

    private static final String[] IMAGE_DIR_CANDIDATES = {
        "assets/images/", "../assets/images/", "app/assets/images/"
    };

    private String resolveImageDir() {
        for (String dir : IMAGE_DIR_CANDIDATES) {
            if (new File(dir).isDirectory()) {
                return dir;
            }
        }
        return IMAGE_DIR;
    }

    private String copyGambar(File file) {
        if (file == null) {
            return null;
        }
        try {
            String dirPath = resolveImageDir();
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String ext = getExt(file.getName());
            String fileName = System.currentTimeMillis() + "." + ext;
            Files.copy(file.toPath(), Paths.get(dirPath + fileName),
                    StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            System.err.println("[MenuController] copyGambar: " + e.getMessage());
            return null;
        }
    }

    private String getExt(String name) {
        int dot = name.lastIndexOf('.');
        return (dot >= 0) ? name.substring(dot + 1).toLowerCase() : "jpg";
    }
}
