package tubes.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private int id;
    private String customer;
    private int userId;
    private Status status;
    private long total;
    private String catatan;
    private LocalDateTime tanggal;
    private List<OrderItem> items;

    private PaymentMethod paymentMethod;
    private long bayar;
    private long kembalian;
    private long pajak;

    private static final DateTimeFormatter FMT
            = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Order(String customer, int userId) {
        this.customer = customer;
        this.userId = userId;
        this.status = Status.PAID;
        this.total = 0;
        this.tanggal = LocalDateTime.now();
        this.items = new ArrayList<>();
    }

    public Order(int id, String customer, int userId,
            Status status, long total,
            String catatan, LocalDateTime tanggal) {
        this.id = id;
        this.customer = customer;
        this.userId = userId;
        this.status = status;
        this.total = total;
        this.catatan = catatan;
        this.tanggal = tanggal;
        this.items = new ArrayList<>();
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public long hitungTotal() {
        this.total = items.stream().mapToLong(OrderItem::getSubtotal).sum();
        return this.total;
    }

    public String getTanggalFormatted() {
        return tanggal != null ? tanggal.format(FMT) : "-";
    }

    public int getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }

    public int getUserId() {
        return userId;
    }

    public Status getStatus() {
        return status;
    }

    public long getTotal() {
        return total;
    }

    public String getCatatan() {
        return catatan;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public long getBayar() {
        return bayar;
    }

    public long getKembalian() {
        return kembalian;
    }

    public long getPajak() {
        return pajak;
    }

    public void setPajak(long pajak) {
        this.pajak = pajak;
    }

    public long getGrandTotal() {
        return total + pajak;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomer(String c) {
        this.customer = c;
    }

    public void setUserId(int u) {
        this.userId = u;
    }

    public void setStatus(Status s) {
        this.status = s;
    }

    public void setTotal(long t) {
        this.total = t;
    }

    public void setCatatan(String c) {
        this.catatan = c;
    }

    public void setTanggal(LocalDateTime t) {
        this.tanggal = t;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setBayar(long bayar) {
        this.bayar = bayar;
    }

    public void setKembalian(long kembalian) {
        this.kembalian = kembalian;
    }

    @Override
    public String toString() {
        return "Order#" + id + " [" + customer + "] " + status.getLabel();
    }
}
