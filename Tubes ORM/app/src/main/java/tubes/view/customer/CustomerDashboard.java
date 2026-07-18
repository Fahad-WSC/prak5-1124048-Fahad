package tubes.view.customer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import tubes.model.Customer;
import tubes.model.Order;
import tubes.util.AppColors;
import tubes.util.SessionManager;
import tubes.view.LoginView;

public class CustomerDashboard extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private MenuBrowseView menuBrowseView;
    private CartView cartView;
    private PaymentView paymentView;
    private OrderSuccessView successView;

    private JLabel lblCartCount;
    private final Customer guestCustomer;
    private String pendingNama;
    private String pendingCatatan;

    public static final String PAGE_MENU = "MENU";
    public static final String PAGE_CART = "CART";
    public static final String PAGE_PAYMENT = "PAYMENT";
    public static final String PAGE_SUCCESS = "SUCCESS";

    public CustomerDashboard() {
        Object user = SessionManager.getInstance().getCurrentUser();
        this.guestCustomer = (user instanceof Customer) ? (Customer) user : new Customer("guest", "", "Pelanggan");
        initFrame();
        buildUI();
        showPage(PAGE_MENU);
    }

    private void initFrame() {
        setTitle("McDonald's Kiosk");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setLayout(new BorderLayout());
    }

    private void buildUI() {
        add(buildTopBar(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
        add(buildBottomNav(), BorderLayout.SOUTH);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(AppColors.MC_RED);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(0, 64));
        bar.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);

        JLabel logo = new JLabel("M");
        logo.setFont(new Font("SansSerif", Font.BOLD, 42));
        logo.setForeground(AppColors.MC_YELLOW);

        JLabel greeting = new JLabel("Selamat Datang!");
        greeting.setFont(new Font("SansSerif", Font.BOLD, 16));
        greeting.setForeground(Color.WHITE);

        left.add(logo);
        left.add(greeting);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);

        JPanel cartBadge = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        cartBadge.setOpaque(false);
        cartBadge.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel cartIcon = new JLabel("Keranjang:");
        cartIcon.setFont(new Font("SansSerif", Font.BOLD, 16));
        cartIcon.setForeground(Color.WHITE);

        lblCartCount = new JLabel("0");
        lblCartCount.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblCartCount.setForeground(AppColors.MC_YELLOW);

        cartBadge.add(cartIcon);
        cartBadge.add(lblCartCount);
        cartBadge.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showPage(PAGE_CART);
            }
        });

        JButton btnStaff = new JButton("Staff");
        btnStaff.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btnStaff.setForeground(new Color(255, 255, 255, 150));
        btnStaff.setContentAreaFilled(false);
        btnStaff.setBorderPainted(false);
        btnStaff.setFocusPainted(false);
        btnStaff.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnStaff.addActionListener(e -> openStaffLogin());

        JButton btnExit = new JButton("\u2715");
        btnExit.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnExit.setForeground(Color.WHITE);
        btnExit.setContentAreaFilled(false);
        btnExit.setBorderPainted(false);
        btnExit.setFocusPainted(false);
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.setToolTipText("Keluar dari aplikasi kiosk");
        btnExit.addActionListener(e -> confirmExit());

        right.add(cartBadge);
        right.add(btnStaff);
        right.add(btnExit);

        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildContent() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(AppColors.MC_LIGHT_GRAY);

        menuBrowseView = new MenuBrowseView(this);
        cartView = new CartView(this);
        paymentView = new PaymentView(this);

        contentPanel.add(menuBrowseView, PAGE_MENU);
        contentPanel.add(cartView, PAGE_CART);
        contentPanel.add(paymentView, PAGE_PAYMENT);

        return contentPanel;
    }

    private JPanel buildBottomNav() {
        JPanel nav = new JPanel(new GridLayout(1, 2));
        nav.setPreferredSize(new Dimension(0, 60));
        nav.setBackground(AppColors.MC_DARK);
        nav.add(buildNavBtn("Menu", PAGE_MENU));
        nav.add(buildNavBtn("Keranjang", PAGE_CART));
        return nav;
    }

    private JButton buildNavBtn(String text, String page) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(AppColors.MC_DARK);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> showPage(page));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(AppColors.MC_RED);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(AppColors.MC_DARK);
            }
        });
        return btn;
    }

    public void showPage(String page) {
        cardLayout.show(contentPanel, page);
        if (PAGE_CART.equals(page)) {
            cartView.refresh();
        }
        if (PAGE_PAYMENT.equals(page)) {
            paymentView.startPayment();
        }
        updateCartBadge();
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void goToOrderSuccess(Order order) {
        if (successView != null) {
            contentPanel.remove(successView);
        }
        successView = new OrderSuccessView(this, order);
        contentPanel.add(successView, PAGE_SUCCESS);
        cardLayout.show(contentPanel, PAGE_SUCCESS);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void confirmExit() {
        int pilihan = JOptionPane.showConfirmDialog(this,
                "Keluar dari aplikasi kiosk?",
                "Konfirmasi Keluar", JOptionPane.YES_NO_OPTION);
        if (pilihan == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public void finishOrderingAndReturnToMenu() {
        showPage(PAGE_MENU);
    }

    public void updateCartBadge() {
        int count = guestCustomer.getCartItemCount();
        lblCartCount.setText(String.valueOf(count));
        lblCartCount.setForeground(count > 0 ? AppColors.MC_YELLOW : Color.WHITE);
    }

    public void setPendingOrder(String nama, String catatan) {
        this.pendingNama = nama;
        this.pendingCatatan = catatan;
    }

    public String getPendingNama() {
        return pendingNama;
    }

    public String getPendingCatatan() {
        return pendingCatatan;
    }

    public Customer getGuestCustomer() {
        return guestCustomer;
    }

    private void openStaffLogin() {
        setVisible(false);
        Runnable returnToKiosk = () -> {
            SessionManager.getInstance().login(guestCustomer);
            updateCartBadge();
            setVisible(true);
            toFront();
        };
        new LoginView(returnToKiosk).setVisible(true);
    }
}
