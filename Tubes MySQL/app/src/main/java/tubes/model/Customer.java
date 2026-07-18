package tubes.model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {

    private List<OrderItem> cart;

    public Customer(int id, String username, String password, String nama) {
        super(id, username, password, nama, Role.CUSTOMER);
        this.cart = new ArrayList<>();
    }

    public Customer(String username, String password, String nama) {
        super(username, password, nama, Role.CUSTOMER);
        this.cart = new ArrayList<>();
    }

    @Override
    public void showDashboard() {
        System.out.println("Membuka Customer Kiosk untuk: " + getNama());
    }

    @Override
    public String getRoleDescription() {
        return "Customer — Memesan menu melalui kiosk";
    }

    public void addToCart(Menu menu, int qty) {
        for (OrderItem item : cart) {
            if (item.getMenu().getId() == menu.getId()) {
                item.setQty(item.getQty() + qty);
                item.hitungSubtotal();
                return;
            }
        }
        cart.add(new OrderItem(menu, qty));
    }

    public void removeFromCart(Menu menu, int qty) {
        cart.removeIf(item -> {
            if (item.getMenu().getId() == menu.getId()) {
                item.setQty(item.getQty() - qty);
                item.hitungSubtotal();
                return item.getQty() <= 0;
            }
            return false;
        });
    }

    public void clearCart() {
        cart.clear();
    }

    public List<OrderItem> getCart() {
        return new ArrayList<>(cart);
    }

    public boolean isCartEmpty() {
        return cart.isEmpty();
    }

    public int getCartItemCount() {
        return cart.stream().mapToInt(OrderItem::getQty).sum();
    }

    public long getCartTotal() {
        return cart.stream().mapToLong(OrderItem::getSubtotal).sum();
    }
}
