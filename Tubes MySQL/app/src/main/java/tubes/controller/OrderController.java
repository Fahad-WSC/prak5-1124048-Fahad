package tubes.controller;

import tubes.dao.OrderDAO;
import tubes.dao.interfaces.IOrderDAO;
import tubes.model.Customer;
import tubes.model.Order;
import tubes.model.OrderItem;
import tubes.model.PaymentMethod;
import tubes.model.Status;
import tubes.util.SessionManager;

import java.util.Collections;
import java.util.List;

public class OrderController {

    private final IOrderDAO orderDAO;

    public OrderController() {
        this.orderDAO = new OrderDAO();
    }

    public int checkout(String namaCustomer, String catatan,
            PaymentMethod method, long bayar) {
        var user = SessionManager.getInstance().getCurrentUser();
        if (!(user instanceof Customer)) {
            return -1;
        }

        Customer customer = (Customer) user;
        if (customer.isCartEmpty()) {
            return -2;
        }

        Order order = new Order(namaCustomer, customer.getId());
        order.setCatatan(catatan);
        for (OrderItem item : customer.getCart()) {
            order.addItem(item);
        }
        long subtotal = order.hitungTotal();
        long pajak = Math.round(subtotal * 0.11);
        long grandTotal = subtotal + pajak;

        order.setPajak(pajak);
        order.setPaymentMethod(method);
        order.setBayar(bayar);
        order.setKembalian(method == PaymentMethod.CASH ? Math.max(0, bayar - grandTotal) : 0);
        order.setStatus(Status.PAID);

        int orderId = orderDAO.save(order);
        if (orderId > 0) {
            customer.clearCart();
        }
        return orderId;
    }

    public static long hitungPajak(long subtotal) {
        return Math.round(subtotal * 0.11);
    }

    public static long hitungGrandTotal(long subtotal) {
        return subtotal + hitungPajak(subtotal);
    }

    public boolean finishOrder(int orderId) {
        return orderDAO.updateStatus(orderId, Status.FINISHED);
    }

    public boolean updateStatus(int orderId, Status status) {
        return orderDAO.updateStatus(orderId, status);
    }

    public List<Order> getAllOrders() {
        return orderDAO.getAll();
    }

    public List<Order> getOrdersByStatus(Status s) {
        return orderDAO.getByStatus(s);
    }

    public List<Order> getTodayOrders() {
        return orderDAO.getTodayOrders();
    }

    public Order getOrderById(int id) {
        return orderDAO.findById(id);
    }

    public List<Order> getMyOrders() {
        var user = SessionManager.getInstance().getCurrentUser();
        if (user == null) {
            return Collections.emptyList();
        }
        return orderDAO.getByUserId(user.getId());
    }

    public int getTotalOrder() {
        return orderDAO.getTotalOrder();
    }

    public long getTotalPendapatan() {
        return orderDAO.getTotalPendapatan();
    }

    public int countByStatus(Status status) {
        return orderDAO.getByStatus(status).size();
    }

    public int countTodayByStatus(Status status) {
        return (int) orderDAO.getTodayOrders().stream()
                .filter(o -> o.getStatus() == status).count();
    }
}
