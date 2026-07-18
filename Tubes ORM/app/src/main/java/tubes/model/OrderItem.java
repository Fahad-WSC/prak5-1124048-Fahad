package tubes.model;

public class OrderItem {

    private int id;
    private int orderId;
    private Menu menu;
    private int qty;
    private long harga;
    private long subtotal;

    public OrderItem(Menu menu, int qty) {
        this.menu = menu;
        this.qty = qty;
        this.harga = menu.getHarga();
        hitungSubtotal();
    }

    public OrderItem(int id, int orderId, Menu menu,
            int qty, long harga, long subtotal) {
        this.id = id;
        this.orderId = orderId;
        this.menu = menu;
        this.qty = qty;
        this.harga = harga;
        this.subtotal = subtotal;
    }

    public void hitungSubtotal() {
        this.subtotal = this.harga * this.qty;
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public Menu getMenu() {
        return menu;
    }

    public int getQty() {
        return qty;
    }

    public long getHarga() {
        return harga;
    }

    public long getSubtotal() {
        return subtotal;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrderId(int o) {
        this.orderId = o;
    }

    public void setMenu(Menu m) {
        this.menu = m;
    }

    public void setHarga(long h) {
        this.harga = h;
    }

    public void setQty(int qty) {
        this.qty = qty;
        hitungSubtotal();
    }

    @Override
    public String toString() {
        return menu.getNamaMeu() + " x" + qty + " = Rp " + subtotal;
    }
}
