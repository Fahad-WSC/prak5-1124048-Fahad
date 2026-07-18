package tubes.dao;

import tubes.dao.interfaces.IKategoriDAO;
import tubes.database.DataStore;
import tubes.model.Kategori;

import java.util.ArrayList;
import java.util.List;

public class KategoriDAO implements IKategoriDAO {

    private final DataStore store = DataStore.getInstance();

    @Override
    public List<Kategori> getAll() {
        return new ArrayList<>(store.getKategoriList());
    }

    @Override
    public Kategori findById(int id) {
        for (Kategori k : store.getKategoriList()) {
            if (k.getId() == id) {
                return k;
            }
        }
        return null;
    }

    @Override
    public boolean save(Kategori kategori) {
        kategori.setId(store.nextKategoriId());
        return store.getKategoriList().add(kategori);
    }

    @Override
    public boolean update(Kategori kategori) {
        Kategori existing = findById(kategori.getId());
        if (existing == null) {
            return false;
        }
        existing.setNama(kategori.getNama());
        return true;
    }

    @Override
    public boolean delete(int id) {
        return store.getKategoriList().removeIf(k -> k.getId() == id);
    }
}
