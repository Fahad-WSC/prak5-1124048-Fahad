DROP DATABASE IF EXISTS mcdonald_kiosk;
CREATE DATABASE mcdonald_kiosk CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mcdonald_kiosk;

-- ------------------------------------------------------------
-- Tabel kategori
-- ------------------------------------------------------------
CREATE TABLE kategori (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL UNIQUE
);

-- ------------------------------------------------------------
-- Tabel menu
-- ------------------------------------------------------------
CREATE TABLE menu (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    nama_menu    VARCHAR(150) NOT NULL,
    kategori_id  INT,
    harga        BIGINT NOT NULL,
    stok         INT NOT NULL DEFAULT 0,
    gambar       VARCHAR(255),
    tersedia     BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (kategori_id) REFERENCES kategori(id) ON DELETE SET NULL
);

-- ------------------------------------------------------------
-- Tabel admin (akun staff/kasir)
-- ------------------------------------------------------------
CREATE TABLE admin (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nama     VARCHAR(100) NOT NULL
);

-- ------------------------------------------------------------
-- Tabel orders
-- ------------------------------------------------------------
CREATE TABLE orders (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    customer       VARCHAR(100) NOT NULL,
    user_id        INT NOT NULL DEFAULT 0,
    status         VARCHAR(20) NOT NULL,
    total          BIGINT NOT NULL DEFAULT 0,
    pajak          BIGINT NOT NULL DEFAULT 0,
    bayar          BIGINT NOT NULL DEFAULT 0,
    kembalian      BIGINT NOT NULL DEFAULT 0,
    payment_method VARCHAR(20),
    catatan        TEXT,
    tanggal        DATETIME NOT NULL
);

-- ------------------------------------------------------------
-- Tabel order_items
-- ------------------------------------------------------------
CREATE TABLE order_items (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    order_id  INT NOT NULL,
    menu_id   INT NOT NULL,
    qty       INT NOT NULL,
    harga     BIGINT NOT NULL,
    subtotal  BIGINT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (menu_id) REFERENCES menu(id)
);

-- ============================================================
--  DATA AWAL (SEED) - setara dengan DataStore.seed() versi ORM
-- ============================================================

-- Kategori
INSERT INTO kategori (nama) VALUES ('Burger');
INSERT INTO kategori (nama) VALUES ('Chicken');
INSERT INTO kategori (nama) VALUES ('Fries & Sides');
INSERT INTO kategori (nama) VALUES ('Drinks');
INSERT INTO kategori (nama) VALUES ('Dessert & McCafe');
INSERT INTO kategori (nama) VALUES ('Paket Hemat');
INSERT INTO kategori (nama) VALUES ('Breakfast');
INSERT INTO kategori (nama) VALUES ('Nasi & Ayam');

-- Menu
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Big Mac', (SELECT id FROM kategori WHERE nama = 'Burger'), 39000, 40, 'bigmac.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Cheeseburger', (SELECT id FROM kategori WHERE nama = 'Burger'), 22000, 50, 'cheese_burger.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Double Cheeseburger', (SELECT id FROM kategori WHERE nama = 'Burger'), 30000, 35, 'double_burger.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('McDouble', (SELECT id FROM kategori WHERE nama = 'Burger'), 28000, 35, 'mcdouble.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Quarter Pounder Cheese', (SELECT id FROM kategori WHERE nama = 'Burger'), 45000, 25, 'quarter_pounder.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('McChicken', (SELECT id FROM kategori WHERE nama = 'Chicken'), 25000, 40, 'mcchicken.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Spicy McCrispy', (SELECT id FROM kategori WHERE nama = 'Chicken'), 32000, 30, 'spicymccrispy.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Crispy Chicken', (SELECT id FROM kategori WHERE nama = 'Chicken'), 28000, 35, 'crispy_chicken.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Chicken McNuggets (6pcs)', (SELECT id FROM kategori WHERE nama = 'Chicken'), 27000, 45, 'nuggets.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('McWings (2pcs)', (SELECT id FROM kategori WHERE nama = 'Chicken'), 24000, 30, 'mcwings.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('McSpicy Chicken Burger', (SELECT id FROM kategori WHERE nama = 'Chicken'), 34000, 30, 'mcspicy.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Chicken Fillet-O', (SELECT id FROM kategori WHERE nama = 'Chicken'), 29000, 30, 'fillet_o.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('French Fries (M)', (SELECT id FROM kategori WHERE nama = 'Fries & Sides'), 18000, 60, 'fries_m.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('French Fries (L)', (SELECT id FROM kategori WHERE nama = 'Fries & Sides'), 23000, 60, 'fries_l.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Curly Fries', (SELECT id FROM kategori WHERE nama = 'Fries & Sides'), 25000, 40, 'curly_fries.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Coca-Cola', (SELECT id FROM kategori WHERE nama = 'Drinks'), 12000, 80, 'cocacola.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Sprite', (SELECT id FROM kategori WHERE nama = 'Drinks'), 12000, 80, 'sprite.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Orange Juice', (SELECT id FROM kategori WHERE nama = 'Drinks'), 15000, 50, 'orange_juice.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Air Mineral', (SELECT id FROM kategori WHERE nama = 'Drinks'), 8000, 100, 'mineral_water.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('McFlurry Oreo', (SELECT id FROM kategori WHERE nama = 'Dessert & McCafe'), 20000, 30, 'mcflurry.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('McFlurry Chocolate', (SELECT id FROM kategori WHERE nama = 'Dessert & McCafe'), 20000, 30, 'mcflurry_choco.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Sundae Cone', (SELECT id FROM kategori WHERE nama = 'Dessert & McCafe'), 10000, 40, 'sundae_cone.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Apple Pie', (SELECT id FROM kategori WHERE nama = 'Dessert & McCafe'), 14000, 35, 'apple_pie.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Paket Hemat 1', (SELECT id FROM kategori WHERE nama = 'Paket Hemat'), 35000, 25, 'paket_hemat1.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Paket McSaver', (SELECT id FROM kategori WHERE nama = 'Paket Hemat'), 30000, 25, 'paket_mcsaver.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Paket Happy Meal', (SELECT id FROM kategori WHERE nama = 'Paket Hemat'), 32000, 30, 'paket_happymeal.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Paket Family', (SELECT id FROM kategori WHERE nama = 'Paket Hemat'), 89000, 15, 'paket_family.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Egg McMuffin', (SELECT id FROM kategori WHERE nama = 'Breakfast'), 24000, 30, 'egg_mcmuffin.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Sausage McMuffin', (SELECT id FROM kategori WHERE nama = 'Breakfast'), 22000, 30, 'sausage_mcmuffin.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Hotcakes', (SELECT id FROM kategori WHERE nama = 'Breakfast'), 20000, 25, 'hotcakes.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Hash Brown', (SELECT id FROM kategori WHERE nama = 'Breakfast'), 10000, 50, 'hash_brown.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('McCafe Latte', (SELECT id FROM kategori WHERE nama = 'Dessert & McCafe'), 22000, 30, 'mccafe_latte.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Iced Coffee', (SELECT id FROM kategori WHERE nama = 'Dessert & McCafe'), 18000, 30, 'iced_coffee.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('PaNas Ayam Goreng', (SELECT id FROM kategori WHERE nama = 'Nasi & Ayam'), 27000, 35, 'panas_ayam.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('Ayam Goreng McD (1pc)', (SELECT id FROM kategori WHERE nama = 'Nasi & Ayam'), 18000, 40, 'ayam_goreng.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('PaMer 5', (SELECT id FROM kategori WHERE nama = 'Nasi & Ayam'), 33000, 30, 'pamer5.png', TRUE);
INSERT INTO menu (nama_menu, kategori_id, harga, stok, gambar, tersedia) VALUES ('PaMer 7', (SELECT id FROM kategori WHERE nama = 'Nasi & Ayam'), 38000, 25, 'pamer7.png', TRUE);

-- Admin
INSERT INTO admin (username, password, nama) VALUES ('admin', 'admin123', 'Administrator');

-- Sample Orders (dummy data)
INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Valtteri Bottas', 0, 'PAID', 210000, 23100, 233100, 0, 'QRIS', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 1 DAY), INTERVAL 357 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 27, 2, 89000, 178000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 20, 1, 20000, 20000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 16, 1, 12000, 12000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Sergio Perez', 0, 'FINISHED', 51000, 5610, 60000, 3390, 'CASH', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 0 DAY), INTERVAL 340 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 6, 1, 25000, 25000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 23, 1, 14000, 14000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 17, 1, 12000, 12000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Arvid Lindblad', 0, 'FINISHED', 50000, 5500, 55500, 0, 'QRIS', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 3 DAY), INTERVAL 323 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 7, 1, 32000, 32000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 22, 1, 10000, 10000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 19, 1, 8000, 8000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Liam Lawson', 0, 'PAID', 65000, 7150, 72150, 0, 'DEBIT', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 2 DAY), INTERVAL 306 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 5, 1, 45000, 45000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 21, 1, 20000, 20000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Oliver Bearman', 0, 'FINISHED', 71000, 7810, 80000, 1190, 'CASH', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 1 DAY), INTERVAL 289 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 1, 1, 39000, 39000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 20, 1, 20000, 20000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 16, 1, 12000, 12000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Esteban Ocon', 0, 'FINISHED', 44000, 4840, 48840, 0, 'QRIS', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 0 DAY), INTERVAL 272 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 26, 1, 32000, 32000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 17, 1, 12000, 12000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Franco Colapinto', 0, 'PAID', 42000, 4620, 50000, 3380, 'CASH', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 3 DAY), INTERVAL 255 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 30, 1, 20000, 20000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 32, 1, 22000, 22000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Pierre Gasly', 0, 'FINISHED', 54000, 5940, 59940, 0, 'DEBIT', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 2 DAY), INTERVAL 238 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 29, 1, 22000, 22000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 31, 1, 10000, 10000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 32, 1, 22000, 22000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Gabriel Bortoleto', 0, 'FINISHED', 52000, 5720, 57720, 0, 'QRIS', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 1 DAY), INTERVAL 221 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 28, 1, 24000, 24000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 31, 1, 10000, 10000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 33, 1, 18000, 18000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Nico Hulkenberg', 0, 'PAID', 49000, 5390, 55000, 610, 'CASH', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 0 DAY), INTERVAL 204 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 10, 1, 24000, 24000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 15, 1, 25000, 25000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Carlos Sainz', 0, 'FINISHED', 55000, 6050, 61050, 0, 'QRIS', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 3 DAY), INTERVAL 187 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 2, 1, 22000, 22000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 13, 1, 18000, 18000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 18, 1, 15000, 15000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Alex Albon', 0, 'FINISHED', 42000, 4620, 46620, 0, 'DEBIT', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 2 DAY), INTERVAL 170 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 25, 1, 30000, 30000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 16, 1, 12000, 12000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Lance Stroll', 0, 'PAID', 43000, 4730, 50000, 2270, 'CASH', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 1 DAY), INTERVAL 153 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 24, 1, 35000, 35000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 19, 1, 8000, 8000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Fernando Alonso', 0, 'FINISHED', 84000, 9240, 93240, 0, 'QRIS', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 0 DAY), INTERVAL 136 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 9, 2, 27000, 54000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 13, 1, 18000, 18000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 17, 1, 12000, 12000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Kimi Antonelli', 0, 'FINISHED', 65000, 7150, 75000, 2850, 'CASH', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 3 DAY), INTERVAL 119 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 8, 1, 28000, 28000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 15, 1, 25000, 25000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 16, 1, 12000, 12000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('George Russell', 0, 'PAID', 46000, 5060, 51060, 0, 'DEBIT', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 2 DAY), INTERVAL 102 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 4, 1, 28000, 28000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 13, 1, 18000, 18000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Isack Hadjar', 0, 'FINISHED', 40000, 4400, 44400, 0, 'QRIS', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 1 DAY), INTERVAL 85 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 7, 1, 32000, 32000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 19, 1, 8000, 8000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Max Verstappen', 0, 'FINISHED', 101000, 11110, 115000, 2890, 'CASH', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 0 DAY), INTERVAL 68 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 27, 1, 89000, 89000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 17, 1, 12000, 12000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Charles Leclerc', 0, 'PAID', 125000, 13750, 138750, 0, 'QRIS', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 3 DAY), INTERVAL 51 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 5, 2, 45000, 90000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 14, 1, 23000, 23000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 16, 1, 12000, 12000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Lewis Hamilton', 0, 'FINISHED', 70000, 7700, 77700, 0, 'DEBIT', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 2 DAY), INTERVAL 34 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 3, 1, 30000, 30000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 15, 1, 25000, 25000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 18, 1, 15000, 15000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Oscar Piastri', 0, 'FINISHED', 55000, 6050, 65000, 3950, 'CASH', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 1 DAY), INTERVAL 17 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 6, 1, 25000, 25000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 13, 1, 18000, 18000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 17, 1, 12000, 12000);

INSERT INTO orders (customer, user_id, status, total, pajak, bayar, kembalian, payment_method, catatan, tanggal) VALUES ('Lando Norris', 0, 'PAID', 74000, 8140, 82140, 0, 'QRIS', NULL, DATE_SUB(DATE_SUB(NOW(), INTERVAL 0 DAY), INTERVAL 0 MINUTE));
SET @last_order_id = LAST_INSERT_ID();
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 1, 1, 39000, 39000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 14, 1, 23000, 23000);
INSERT INTO order_items (order_id, menu_id, qty, harga, subtotal) VALUES (@last_order_id, 16, 1, 12000, 12000);

