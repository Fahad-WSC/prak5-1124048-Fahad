package tubes.dao.interfaces;

import tubes.model.Kategori;
import tubes.model.Menu;
import java.util.List;

public interface IMenuDAO {

    List<Menu> getAll();

    List<Menu> getByKategori(Kategori kategori);

    List<Menu> search(String keyword);

    Menu findById(int id);

    boolean save(Menu menu);

    boolean update(Menu menu);

    boolean delete(int id);

    int getTotalMenu();
}
