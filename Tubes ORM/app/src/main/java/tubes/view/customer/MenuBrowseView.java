package tubes.view.customer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import tubes.controller.KategoriController;
import tubes.controller.MenuController;
import tubes.model.Kategori;
import tubes.model.Menu;
import tubes.util.AppColors;
import tubes.util.WrapLayout;
import tubes.view.components.MenuCard;
import tubes.view.components.RoundedButton;
import tubes.view.components.VirtualKeyboard;

public class MenuBrowseView extends JPanel {

    private final CustomerDashboard parent;
    private final MenuController menuCtrl;
    private final KategoriController kategoriCtrl;

    private JPanel gridPanel;
    private JPanel sidebar;
    private JTextField txtSearch;
    private JLabel lblKategori;
    private Kategori activeKategori = null;

    public MenuBrowseView(CustomerDashboard parent) {
        this.parent = parent;
        this.menuCtrl = new MenuController();
        this.kategoriCtrl = new KategoriController();
        setLayout(new BorderLayout());
        setBackground(AppColors.MC_LIGHT_GRAY);
        buildUI();
        loadMenu(null);
    }

    private void buildUI() {
        add(buildSearchBar(), BorderLayout.NORTH);
        add(buildSidebar(), BorderLayout.WEST);
        add(buildMenuArea(), BorderLayout.CENTER);
    }

    private JPanel buildSearchBar() {
        JPanel bar = new JPanel(new BorderLayout(10, 0));
        bar.setBackground(Color.WHITE);
        bar.setBorder(new EmptyBorder(12, 20, 12, 20));

        txtSearch = new JTextField();
        txtSearch.setFont(AppColors.FONT_BODY);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDD, 0xDD, 0xDD), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String kw = txtSearch.getText().trim();
                if (kw.isEmpty()) {
                    loadMenu(activeKategori);
                } else {
                    loadMenuByKeyword(kw);
                }
            }
        });
        VirtualKeyboard.attach(txtSearch);

        JLabel icon = new JLabel("Cari: ");
        icon.setFont(new Font("SansSerif", Font.BOLD, 14));

        RoundedButton btnSearch = new RoundedButton("Cari", AppColors.MC_RED, Color.WHITE);
        btnSearch.setPreferredSize(new Dimension(90, 44));
        btnSearch.addActionListener(e -> loadMenuByKeyword(txtSearch.getText().trim()));

        bar.add(icon, BorderLayout.WEST);
        bar.add(txtSearch, BorderLayout.CENTER);
        bar.add(btnSearch, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildSidebar() {
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(AppColors.MC_DARK);
        sidebar.setPreferredSize(new Dimension(170, 0));
        sidebar.setBorder(new EmptyBorder(16, 0, 16, 0));
        rebuildSidebar();
        return sidebar;
    }

    private void rebuildSidebar() {
        sidebar.removeAll();

        JLabel title = new JLabel("KATEGORI", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 12));
        title.setForeground(AppColors.MC_GRAY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 12, 0));
        sidebar.add(title);

        sidebar.add(buildCatBtn("Semua Menu", null));
        sidebar.add(Box.createVerticalStrut(4));
        for (Kategori k : kategoriCtrl.getAllKategori()) {
            sidebar.add(buildCatBtn(k.getNama(), k));
            sidebar.add(Box.createVerticalStrut(4));
        }
        sidebar.revalidate();
        sidebar.repaint();
    }

    private JButton buildCatBtn(String text, Kategori kategori) {
        boolean active = (kategori == null && activeKategori == null)
                || (kategori != null && kategori.equals(activeKategori));

        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(active ? AppColors.MC_RED : new Color(0x3A, 0x3A, 0x3A));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(160, 46));
        btn.setPreferredSize(new Dimension(160, 46));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            activeKategori = kategori;
            txtSearch.setText("");
            loadMenu(kategori);
            rebuildSidebar();
        });
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!active) {
                    btn.setBackground(new Color(0x55, 0x55, 0x55));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!active) {
                    btn.setBackground(new Color(0x3A, 0x3A, 0x3A));
                }
            }
        });
        return btn;
    }

    private JScrollPane buildMenuArea() {
        gridPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 16, 16));
        gridPanel.setBackground(AppColors.MC_LIGHT_GRAY);

        lblKategori = new JLabel("Semua Menu");
        lblKategori.setFont(AppColors.FONT_HEADING);
        lblKategori.setForeground(AppColors.MC_DARK);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(AppColors.MC_LIGHT_GRAY);
        wrapper.setBorder(new EmptyBorder(16, 16, 16, 16));
        wrapper.add(lblKategori, BorderLayout.NORTH);
        wrapper.add(gridPanel, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    public void refreshCategories() {
        rebuildSidebar();
    }

    private void loadMenu(Kategori kategori) {
        SwingWorker<List<Menu>, Void> w = new SwingWorker<>() {
            @Override
            protected List<Menu> doInBackground() {
                return (kategori == null) ? menuCtrl.getAllMenu() : menuCtrl.getMenuByKategori(kategori);
            }

            @Override
            protected void done() {
                try {
                    renderGrid(get());
                    lblKategori.setText(kategori == null ? "Semua Menu" : kategori.getNama());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        w.execute();
    }

    private void loadMenuByKeyword(String keyword) {
        SwingWorker<List<Menu>, Void> w = new SwingWorker<>() {
            @Override
            protected List<Menu> doInBackground() {
                return menuCtrl.searchMenu(keyword);
            }

            @Override
            protected void done() {
                try {
                    renderGrid(get());
                    lblKategori.setText("Hasil: \"" + keyword + "\"");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        w.execute();
    }

    private void renderGrid(List<Menu> list) {
        gridPanel.removeAll();
        if (list.isEmpty()) {
            JLabel empty = new JLabel("Tidak ada menu.", SwingConstants.CENTER);
            empty.setFont(AppColors.FONT_BODY);
            empty.setForeground(AppColors.MC_GRAY);
            gridPanel.add(empty);
        } else {
            for (Menu m : list) {
                MenuCard card = new MenuCard(m);
                card.setListener((menu, qty) -> addToCart(menu, qty));
                gridPanel.add(card);
            }
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void addToCart(Menu menu, int qty) {
        parent.getGuestCustomer().addToCart(menu, qty);
        parent.updateCartBadge();
        showToast(menu.getNamaMeu() + " ditambahkan ke keranjang!");
    }

    private void showToast(String message) {
        JWindow toast = new JWindow(SwingUtilities.getWindowAncestor(this));
        JLabel lbl = new JLabel("  " + message + "  ", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);
        lbl.setBackground(new Color(0x33, 0x33, 0x33));
        lbl.setOpaque(true);
        lbl.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        toast.add(lbl);
        toast.pack();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        toast.setLocation((screen.width - toast.getWidth()) / 2, screen.height - 160);
        toast.setVisible(true);
        Timer t = new Timer(2000, e -> toast.dispose());
        t.setRepeats(false);
        t.start();
    }
}
