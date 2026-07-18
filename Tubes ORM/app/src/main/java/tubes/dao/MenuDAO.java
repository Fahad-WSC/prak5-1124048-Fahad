package tubes.dao;

import tubes.dao.interfaces.IMenuDAO;
import tubes.database.DataStore;
import tubes.model.Kategori;
import tubes.model.Menu;

import java.util.ArrayList;
import java.util.List;

public class MenuDAO implements IMenuDAO {

    private final DataStore store = DataStore.getInstance();

    @Override
    public List<Menu> getAll() {
        return new ArrayList<>(store.getMenuList());
    }

    @Override
    public List<Menu> getByKategori(Kategori kategori) {
        List<Menu> result = new ArrayList<>();
        for (Menu m : store.getMenuList()) {
            if (m.getKategori() != null && m.getKategori().getId() == kategori.getId() && m.isTersedia()) {
                result.add(m);
            }
        }
        return result;
    }

    @Override
    public List<Menu> search(String keyword) {
        List<Menu> result = new ArrayList<>();
        String kw = keyword.toLowerCase();
        for (Menu m : store.getMenuList()) {
            if (m.getNamaMeu().toLowerCase().contains(kw)) {
                result.add(m);
            }
        }
        return result;
    }

    @Override
    public Menu findById(int id) {
        for (Menu m : store.getMenuList()) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    @Override
    public boolean save(Menu menu) {
        menu.setId(store.nextMenuId());
        return store.getMenuList().add(menu);
    }

    @Override
    public boolean update(Menu menu) {
        Menu existing = findById(menu.getId());
        if (existing == null) {
            return false;
        }
        existing.setNamaMeu(menu.getNamaMeu());
        existing.setKategori(menu.getKategori());
        existing.setHarga(menu.getHarga());
        existing.setStok(menu.getStok());
        existing.setGambar(menu.getGambar());
        existing.setTersedia(menu.isTersedia());
        return true;
    }

    @Override
    public boolean delete(int id) {
        return store.getMenuList().removeIf(m -> m.getId() == id);
    }

    @Override
    public int getTotalMenu() {
        return store.getMenuList().size();
    }
}
