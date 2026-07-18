package tubes.dao;

import tubes.dao.interfaces.IOrderDAO;
import tubes.database.DataStore;
import tubes.model.Order;
import tubes.model.Status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO implements IOrderDAO {

    private final DataStore store = DataStore.getInstance();

    @Override
    public int save(Order order) {
        order.setId(store.nextOrderId());
        store.getOrderList().add(0, order);
        return order.getId();
    }

    @Override
    public boolean updateStatus(int id, Status status) {
        Order order = findById(id);
        if (order == null) {
            return false;
        }
        order.setStatus(status);
        return true;
    }

    @Override
    public List<Order> getAll() {
        return new ArrayList<>(store.getOrderList());
    }

    @Override
    public List<Order> getByStatus(Status status) {
        List<Order> result = new ArrayList<>();
        for (Order o : store.getOrderList()) {
            if (o.getStatus() == status) {
                result.add(o);
            }
        }
        return result;
    }

    @Override
    public List<Order> getByUserId(int userId) {
        List<Order> result = new ArrayList<>();
        for (Order o : store.getOrderList()) {
            if (o.getUserId() == userId) {
                result.add(o);
            }
        }
        return result;
    }

    @Override
    public List<Order> getTodayOrders() {
        List<Order> result = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Order o : store.getOrderList()) {
            if (o.getTanggal() != null && o.getTanggal().toLocalDate().isEqual(today)) {
                result.add(o);
            }
        }
        return result;
    }

    @Override
    public Order findById(int id) {
        for (Order o : store.getOrderList()) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }

    @Override
    public long getTotalPendapatan() {
        long total = 0;
        for (Order o : store.getOrderList()) {
            total += o.getTotal();
        }
        return total;
    }

    @Override
    public int getTotalOrder() {
        return store.getOrderList().size();
    }
}
