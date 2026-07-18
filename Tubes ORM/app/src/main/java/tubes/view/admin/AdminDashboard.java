package tubes.view.admin;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import tubes.controller.AdminController;
import tubes.controller.OrderController;
import tubes.model.User;
import tubes.util.AppColors;
import tubes.util.CurrencyUtil;
import tubes.util.SessionManager;
import tubes.view.components.RoundedButton;

public class AdminDashboard extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private final AdminController adminCtrl;
    private final OrderController orderCtrl;
    private final Runnable returnToKiosk;

    private JButton btnNavDashboard, btnNavKategori, btnNavMenu, btnNavOrder;
    private JPanel dashboardPanel;
    private OrderManageView orderManageView;
    private MenuManageView menuManageView;

    public static final String PAGE_DASHBOARD = "DASHBOARD";
    public static final String PAGE_KATEGORI = "KATEGORI";
    public static final String PAGE_MENU = "MENU";
    public static final String PAGE_ORDER = "ORDER";

    public AdminDashboard(Runnable returnToKiosk) {
        this.returnToKiosk = returnToKiosk;
        this.adminCtrl = new AdminController();
        this.orderCtrl = new OrderController();
        initFrame();
        buildUI();
        showPage(PAGE_DASHBOARD);
    }

    private void initFrame() {
        setTitle("McDonald's Kiosk — Admin");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setLayout(new BorderLayout());
    }

    private void buildUI() {
        add(buildTopBar(), BorderLayout.NORTH);
        add(buildSidebar(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(AppColors.MC_RED);
        bar.setPreferredSize(new Dimension(0, 64));
        bar.setBorder(new EmptyBorder(0, 24, 0, 24));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);

        JLabel logo = new JLabel("M");
        logo.setFont(new Font("SansSerif", Font.BOLD, 42));
        logo.setForeground(AppColors.MC_YELLOW);

        JLabel title = new JLabel("Admin Panel");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        User user = SessionManager.getInstance().getCurrentUser();
        JLabel lblName = new JLabel(user != null ? "— " + user.getNama() : "");
        lblName.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblName.setForeground(new Color(255, 255, 255, 180));

        left.add(logo);
        left.add(title);
        left.add(lblName);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        RoundedButton btnLogout = new RoundedButton("Keluar ke Kiosk", new Color(0x55, 0x00, 0x00), Color.WHITE);
        btnLogout.setPreferredSize(new Dimension(150, 36));
        btnLogout.addActionListener(e -> doLogout());

        JButton btnExit = new JButton("\u2715");
        btnExit.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnExit.setForeground(Color.WHITE);
        btnExit.setContentAreaFilled(false);
        btnExit.setBorderPainted(false);
        btnExit.setFocusPainted(false);
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.setToolTipText("Keluar dari aplikasi");
        btnExit.addActionListener(e -> confirmExitApp());

        right.add(btnLogout);
        right.add(btnExit);

        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(AppColors.MC_DARK);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(new EmptyBorder(24, 0, 24, 0));

        JLabel lbl = new JLabel("NAVIGASI");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(AppColors.MC_GRAY);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(0, 0, 16, 0));
        sidebar.add(lbl);

        btnNavDashboard = buildNavBtn("Dashboard", PAGE_DASHBOARD);
        btnNavKategori = buildNavBtn("Kelola Kategori", PAGE_KATEGORI);
        btnNavMenu = buildNavBtn("Kelola Menu", PAGE_MENU);
        btnNavOrder = buildNavBtn("Kelola Order", PAGE_ORDER);

        sidebar.add(btnNavDashboard);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(btnNavKategori);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(btnNavMenu);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(btnNavOrder);
        return sidebar;
    }

    private JButton buildNavBtn(String text, String page) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0x3A, 0x3A, 0x3A));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(192, 50));
        btn.setPreferredSize(new Dimension(192, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 20, 0, 0));
        btn.addActionListener(e -> showPage(page));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!btn.getBackground().equals(AppColors.MC_RED)) {
                    btn.setBackground(new Color(0x55, 0x55, 0x55));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!btn.getBackground().equals(AppColors.MC_RED)) {
                    btn.setBackground(new Color(0x3A, 0x3A, 0x3A));
                }
            }
        });
        return btn;
    }

    private JPanel buildContent() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(AppColors.MC_LIGHT_GRAY);

        dashboardPanel = buildDashboardPanel();
        orderManageView = new OrderManageView(this);
        menuManageView = new MenuManageView(this);

        contentPanel.add(dashboardPanel, PAGE_DASHBOARD);
        contentPanel.add(new KategoriManageView(this), PAGE_KATEGORI);
        contentPanel.add(menuManageView, PAGE_MENU);
        contentPanel.add(orderManageView, PAGE_ORDER);
        return contentPanel;
    }

    public void refreshMenuKategoriCombo() {
        if (menuManageView != null) {
            menuManageView.refreshKategoriCombo();
        }
    }

    private JPanel buildDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppColors.MC_LIGHT_GRAY);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Ringkasan Sistem");
        title.setFont(AppColors.FONT_TITLE);
        title.setForeground(AppColors.MC_DARK);
        title.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel cards = new JPanel(new GridLayout(1, 4, 20, 0));
        cards.setOpaque(false);
        cards.setPreferredSize(new Dimension(0, 130));
        cards.add(buildDashCard("Total Menu", String.valueOf(adminCtrl.getTotalMenu()), AppColors.MC_RED));
        cards.add(buildDashCard("Total Order", String.valueOf(adminCtrl.getTotalOrder()), AppColors.STATUS_PAID));
        cards.add(buildDashCard("Perlu Diselesaikan", String.valueOf(orderCtrl.countByStatus(tubes.model.Status.PAID)), AppColors.MC_YELLOW.darker()));
        cards.add(buildDashCard("Total Pendapatan", CurrencyUtil.formatShort(adminCtrl.getTotalPendapatan()), AppColors.STATUS_FINISHED));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.NORTH);
        top.add(cards, BorderLayout.CENTER);

        JLabel hint = new JLabel("Buka menu \"Kelola Order\" di samping untuk melihat dan memperbarui status pesanan.");
        hint.setFont(AppColors.FONT_BODY);
        hint.setForeground(AppColors.MC_GRAY);
        hint.setBorder(new EmptyBorder(24, 0, 0, 0));

        panel.add(top, BorderLayout.NORTH);
        panel.add(hint, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildDashCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(12, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 16, 16);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 16, 16);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth() - 4, 6, 4, 4);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(18, 20, 18, 20));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        textPanel.setOpaque(false);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTitle.setForeground(AppColors.MC_GRAY);
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblValue.setForeground(color);
        textPanel.add(lblTitle);
        textPanel.add(lblValue);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    public void showPage(String page) {
        if (PAGE_DASHBOARD.equals(page)) {
            contentPanel.remove(dashboardPanel);
            dashboardPanel = buildDashboardPanel();
            contentPanel.add(dashboardPanel, PAGE_DASHBOARD);
        }
        if (PAGE_ORDER.equals(page)) {
            orderManageView.refresh();
        }
        cardLayout.show(contentPanel, page);

        Color active = AppColors.MC_RED;
        Color inactive = new Color(0x3A, 0x3A, 0x3A);
        btnNavDashboard.setBackground(PAGE_DASHBOARD.equals(page) ? active : inactive);
        btnNavKategori.setBackground(PAGE_KATEGORI.equals(page) ? active : inactive);
        btnNavMenu.setBackground(PAGE_MENU.equals(page) ? active : inactive);
        btnNavOrder.setBackground(PAGE_ORDER.equals(page) ? active : inactive);
    }

    private void confirmExitApp() {
        int pilihan = JOptionPane.showConfirmDialog(this,
                "Keluar dari aplikasi?",
                "Konfirmasi Keluar", JOptionPane.YES_NO_OPTION);
        if (pilihan == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void doLogout() {
        int ok = JOptionPane.showConfirmDialog(this, "Keluar dari panel admin dan kembali ke kiosk?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            SessionManager.getInstance().logout();
            dispose();
            if (returnToKiosk != null) {
                returnToKiosk.run();
            }
        }
    }
}
