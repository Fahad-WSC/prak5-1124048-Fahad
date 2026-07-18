package tubes.dao.interfaces;

import tubes.model.Kategori;
import java.util.List;

public interface IKategoriDAO {

    List<Kategori> getAll();

    Kategori findById(int id);

    boolean save(Kategori kategori);

    boolean update(Kategori kategori);

    boolean delete(int id);
}
