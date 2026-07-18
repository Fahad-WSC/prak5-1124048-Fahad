package tubes.database;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import tubes.model.Admin;
import tubes.model.Kategori;
import tubes.model.Menu;
import tubes.model.Order;
import tubes.model.OrderItem;
import tubes.model.PaymentMethod;
import tubes.model.Status;

public final class DataStore {

    private static final DataStore INSTANCE = new DataStore();

    private final List<Kategori> kategoriList = new ArrayList<>();
    private final List<Menu> menuList = new ArrayList<>();
    private final List<Order> orderList = new ArrayList<>();
    private final List<Admin> adminList = new ArrayList<>();

    private final AtomicInteger kategoriIdSeq = new AtomicInteger(0);
    private final AtomicInteger menuIdSeq = new AtomicInteger(0);
    private final AtomicInteger orderIdSeq = new AtomicInteger(0);
    private final AtomicInteger userIdSeq = new AtomicInteger(0);

    private DataStore() {
        seed();
    }

    public static DataStore getInstance() {
        return INSTANCE;
    }

    public List<Kategori> getKategoriList() {
        return kategoriList;
    }

    public int nextKategoriId() {
        return kategoriIdSeq.incrementAndGet();
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public int nextMenuId() {
        return menuIdSeq.incrementAndGet();
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public int nextOrderId() {
        return orderIdSeq.incrementAndGet();
    }

    public List<Admin> getAdminList() {
        return adminList;
    }

    public int nextUserId() {
        return userIdSeq.incrementAndGet();
    }

    private void seed() {
        Kategori burger = new Kategori(kategoriIdSeq.incrementAndGet(), "Burger");
        Kategori chicken = new Kategori(kategoriIdSeq.incrementAndGet(), "Chicken");
        Kategori fries = new Kategori(kategoriIdSeq.incrementAndGet(), "Fries & Sides");
        Kategori drinks = new Kategori(kategoriIdSeq.incrementAndGet(), "Drinks");
        Kategori dessert = new Kategori(kategoriIdSeq.incrementAndGet(), "Dessert & McCafe");
        Kategori paket = new Kategori(kategoriIdSeq.incrementAndGet(), "Paket Hemat");
        kategoriList.add(burger);
        kategoriList.add(chicken);
        kategoriList.add(fries);
        kategoriList.add(drinks);
        kategoriList.add(dessert);
        kategoriList.add(paket);

        addMenu("Big Mac", burger, 39000, 40, "bigmac.png");
        addMenu("Cheeseburger", burger, 22000, 50, "cheese_burger.png");
        addMenu("Double Cheeseburger", burger, 30000, 35, "double_burger.png");
        addMenu("McDouble", burger, 28000, 35, "mcdouble.png");
        addMenu("Quarter Pounder Cheese", burger, 45000, 25, "quarter_pounder.png");

        addMenu("McChicken", chicken, 25000, 40, "mcchicken.png");
        addMenu("Spicy McCrispy", chicken, 32000, 30, "spicymccrispy.png");
        addMenu("Crispy Chicken", chicken, 28000, 35, "crispy_chicken.png");
        addMenu("Chicken McNuggets (6pcs)", chicken, 27000, 45, "nuggets.png");
        addMenu("McWings (2pcs)", chicken, 24000, 30, "mcwings.png");
        addMenu("McSpicy Chicken Burger", chicken, 34000, 30, "mcspicy.png");
        addMenu("Chicken Fillet-O", chicken, 29000, 30, "fillet_o.png");

        addMenu("French Fries (M)", fries, 18000, 60, "fries_m.png");
        addMenu("French Fries (L)", fries, 23000, 60, "fries_l.png");
        addMenu("Curly Fries", fries, 25000, 40, "curly_fries.png");

        addMenu("Coca-Cola", drinks, 12000, 80, "cocacola.png");
        addMenu("Sprite", drinks, 12000, 80, "sprite.png");
        addMenu("Orange Juice", drinks, 15000, 50, "orange_juice.png");
        addMenu("Air Mineral", drinks, 8000, 100, "mineral_water.png");

        addMenu("McFlurry Oreo", dessert, 20000, 30, "mcflurry.png");
        addMenu("McFlurry Chocolate", dessert, 20000, 30, "mcflurry_choco.png");
        addMenu("Sundae Cone", dessert, 10000, 40, "sundae_cone.png");
        addMenu("Apple Pie", dessert, 14000, 35, "apple_pie.png");

        addMenu("Paket Hemat 1", paket, 35000, 25, "paket_hemat1.png");
        addMenu("Paket McSaver", paket, 30000, 25, "paket_mcsaver.png");
        addMenu("Paket Happy Meal", paket, 32000, 30, "paket_happymeal.png");
        addMenu("Paket Family", paket, 89000, 15, "paket_family.png");

        Kategori breakfast = new Kategori(kategoriIdSeq.incrementAndGet(), "Breakfast");
        kategoriList.add(breakfast);
        addMenu("Egg McMuffin", breakfast, 24000, 30, "egg_mcmuffin.png");
        addMenu("Sausage McMuffin", breakfast, 22000, 30, "sausage_mcmuffin.png");
        addMenu("Hotcakes", breakfast, 20000, 25, "hotcakes.png");
        addMenu("Hash Brown", breakfast, 10000, 50, "hash_brown.png");

        addMenu("McCafe Latte", dessert, 22000, 30, "mccafe_latte.png");
        addMenu("Iced Coffee", dessert, 18000, 30, "iced_coffee.png");

        Kategori nasiAyam = new Kategori(kategoriIdSeq.incrementAndGet(), "Nasi & Ayam");
        kategoriList.add(nasiAyam);
        addMenu("PaNas Ayam Goreng", nasiAyam, 27000, 35, "panas_ayam.png");
        addMenu("Ayam Goreng McD (1pc)", nasiAyam, 18000, 40, "ayam_goreng.png");
        addMenu("PaMer 5", nasiAyam, 33000, 30, "pamer5.png");
        addMenu("PaMer 7", nasiAyam, 38000, 25, "pamer7.png");

        adminList.add(new Admin(userIdSeq.incrementAndGet(), "admin", "admin123", "Administrator"));

        seedOrders();
    }

    private void addMenu(String nama, Kategori kategori, long harga, int stok, String gambar) {
        menuList.add(new Menu(menuIdSeq.incrementAndGet(), nama, kategori, harga, stok, gambar, true));
    }

    private Menu menuByNama(String nama) {
        for (Menu m : menuList) {
            if (m.getNamaMeu().equalsIgnoreCase(nama)) {
                return m;
            }
        }
        return menuList.get(0);
    }

    private void seedOrders() {
        String[] pembalap = {
            "Lando Norris", "Oscar Piastri", "Lewis Hamilton", "Charles Leclerc",
            "Max Verstappen", "Isack Hadjar", "George Russell", "Kimi Antonelli",
            "Fernando Alonso", "Lance Stroll", "Alex Albon", "Carlos Sainz",
            "Nico Hulkenberg", "Gabriel Bortoleto", "Pierre Gasly", "Franco Colapinto",
            "Esteban Ocon", "Oliver Bearman", "Liam Lawson", "Arvid Lindblad",
            "Sergio Perez", "Valtteri Bottas"
        };

        String[][] itemPlan = {
            {"Big Mac", "French Fries (L)", "Coca-Cola"},
            {"McChicken", "French Fries (M)", "Sprite"},
            {"Double Cheeseburger", "Curly Fries", "Orange Juice"},
            {"Quarter Pounder Cheese", "French Fries (L)", "Coca-Cola"},
            {"Paket Family", "Sprite"},
            {"Spicy McCrispy", "Air Mineral"},
            {"McDouble", "French Fries (M)"},
            {"Crispy Chicken", "Curly Fries", "Coca-Cola"},
            {"Chicken McNuggets (6pcs)", "French Fries (M)", "Sprite"},
            {"Paket Hemat 1", "Air Mineral"},
            {"Paket McSaver", "Coca-Cola"},
            {"Cheeseburger", "French Fries (M)", "Orange Juice"},
            {"McWings (2pcs)", "Curly Fries"},
            {"Egg McMuffin", "Hash Brown", "Iced Coffee"},
            {"Sausage McMuffin", "Hash Brown", "McCafe Latte"},
            {"Hotcakes", "McCafe Latte"},
            {"Paket Happy Meal", "Sprite"},
            {"Big Mac", "McFlurry Oreo", "Coca-Cola"},
            {"Quarter Pounder Cheese", "McFlurry Chocolate"},
            {"Spicy McCrispy", "Sundae Cone", "Air Mineral"},
            {"McChicken", "Apple Pie", "Sprite"},
            {"Paket Family", "McFlurry Oreo", "Coca-Cola"}
        };

        int[] qtyPlan = {1, 1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2};
        PaymentMethod[] metodePlan = {
            PaymentMethod.QRIS, PaymentMethod.CASH, PaymentMethod.DEBIT, PaymentMethod.QRIS,
            PaymentMethod.CASH, PaymentMethod.QRIS, PaymentMethod.DEBIT, PaymentMethod.CASH,
            PaymentMethod.QRIS, PaymentMethod.CASH, PaymentMethod.DEBIT, PaymentMethod.QRIS,
            PaymentMethod.CASH, PaymentMethod.QRIS, PaymentMethod.DEBIT, PaymentMethod.CASH,
            PaymentMethod.QRIS, PaymentMethod.CASH, PaymentMethod.DEBIT, PaymentMethod.QRIS,
            PaymentMethod.CASH, PaymentMethod.QRIS
        };

        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < pembalap.length; i++) {
            Order order = new Order(pembalap[i], 0);
            order.setId(orderIdSeq.incrementAndGet());

            for (String namaMenu : itemPlan[i]) {
                Menu menu = menuByNama(namaMenu);
                int qty = (namaMenu.equals(itemPlan[i][0])) ? qtyPlan[i] : 1;
                order.addItem(new OrderItem(menu, qty));
            }

            long subtotal = order.hitungTotal();
            long pajak = Math.round(subtotal * 0.11);
            long grandTotal = subtotal + pajak;
            PaymentMethod metode = metodePlan[i];

            order.setPajak(pajak);
            order.setPaymentMethod(metode);
            if (metode == PaymentMethod.CASH) {
                long bayar = ((grandTotal / 5000) + 1) * 5000;
                order.setBayar(bayar);
                order.setKembalian(bayar - grandTotal);
            } else {
                order.setBayar(grandTotal);
                order.setKembalian(0);
            }

            Status status = (i % 3 == 0) ? Status.PAID : Status.FINISHED;
            order.setStatus(status);

            LocalDateTime tanggal = now.minusDays(i % 4).minusMinutes(i * 17L);
            order.setTanggal(tanggal);

            orderList.add(0, order);
        }
    }
}
