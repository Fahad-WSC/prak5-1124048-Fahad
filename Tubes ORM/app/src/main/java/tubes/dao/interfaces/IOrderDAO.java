package tubes.dao.interfaces;

import tubes.model.Order;
import tubes.model.Status;
import java.util.List;

public interface IOrderDAO {

    int save(Order order);

    boolean updateStatus(int id, Status status);

    List<Order> getAll();

    List<Order> getByStatus(Status status);

    List<Order> getByUserId(int userId);

    List<Order> getTodayOrders();

    Order findById(int id);

    long getTotalPendapatan();

    int getTotalOrder();
}
