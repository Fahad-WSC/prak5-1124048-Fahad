package tubes.view.customer;

import tubes.model.OrderItem;
import tubes.util.AppColors;
import tubes.util.CurrencyUtil;
import tubes.view.components.RoundedButton;
import tubes.view.components.VirtualKeyboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CartView extends JPanel {

    private final CustomerDashboard parent;

    private DefaultTableModel tableModel;
    private JLabel lblTotal;
    private JTextField txtNama;
    private JTextField txtCatatan;

    public CartView(CustomerDashboard parent) {
        this.parent = parent;
        setLayout(new BorderLayout(16, 0));
        setBackground(AppColors.MC_LIGHT_GRAY);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        buildUI();
    }

    private void buildUI() {
        JLabel title = new JLabel("Keranjang Belanja");
        title.setFont(AppColors.FONT_TITLE);
        title.setForeground(AppColors.MC_DARK);
        title.setBorder(new EmptyBorder(0, 0, 16, 0));
        add(title, BorderLayout.NORTH);

        add(buildCartTable(), BorderLayout.CENTER);
        add(buildCheckoutPanel(), BorderLayout.EAST);
    }

    private JScrollPane buildCartTable() {
        String[] cols = {"Menu", "Harga", "Qty", "Subtotal", "Hapus"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 4;
            }
        };

        JTable table = new JTable(tableModel);
        table.setFont(AppColors.FONT_BODY);
        table.setRowHeight(50);
        table.setShowGrid(false);
        table.setSelectionBackground(new Color(0xFF, 0xF0, 0xCC));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(AppColors.MC_RED);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);

        int[] widths = {260, 120, 80, 140, 70};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        table.getColumnModel().getColumn(4).setCellRenderer((t, val, sel, foc, row, col) -> {
            JButton btn = new JButton("X");
            btn.setBackground(new Color(0xFF, 0xEE, 0xEE));
            btn.setBorderPainted(false);
            return btn;
        });
        table.getColumnModel().getColumn(4).setCellEditor(
                new ButtonEditor(() -> {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        removeItem(row);
                    }
                })
        );

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xEE, 0xEE, 0xEE), 1));
        return scroll;
    }

    private JPanel buildCheckoutPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(300, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xEE, 0xEE, 0xEE), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel h = new JLabel("Ringkasan Pesanan");
        h.setFont(AppColors.FONT_HEADING);
        h.setForeground(AppColors.MC_DARK);
        h.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(h);
        panel.add(Box.createVerticalStrut(16));

        JLabel lNama = new JLabel("Nama Pemesan *");
        lNama.setFont(new Font("SansSerif", Font.BOLD, 13));
        lNama.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lNama);
        panel.add(Box.createVerticalStrut(6));

        txtNama = new JTextField();
        txtNama.setFont(AppColors.FONT_BODY);
        txtNama.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        txtNama.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDD, 0xDD, 0xDD), 1),
                new EmptyBorder(6, 10, 6, 10)));
        VirtualKeyboard.attach(txtNama);
        panel.add(txtNama);
        panel.add(Box.createVerticalStrut(12));

        JLabel lCat = new JLabel("Catatan (opsional)");
        lCat.setFont(new Font("SansSerif", Font.BOLD, 13));
        lCat.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lCat);
        panel.add(Box.createVerticalStrut(6));

        txtCatatan = new JTextField();
        txtCatatan.setFont(AppColors.FONT_BODY);
        txtCatatan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        txtCatatan.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDD, 0xDD, 0xDD), 1),
                new EmptyBorder(6, 10, 6, 10)));
        VirtualKeyboard.attach(txtCatatan);
        panel.add(txtCatatan);
        panel.add(Box.createVerticalStrut(20));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        panel.add(sep);
        panel.add(Box.createVerticalStrut(12));

        lblTotal = new JLabel("Total: Rp 0");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTotal.setForeground(AppColors.MC_RED);
        lblTotal.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTotal);
        panel.add(Box.createVerticalStrut(20));

        RoundedButton btnCheckout = new RoundedButton("Bayar Sekarang", AppColors.MC_RED, Color.WHITE);
        btnCheckout.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnCheckout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        btnCheckout.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCheckout.addActionListener(e -> goToPayment());
        panel.add(btnCheckout);
        panel.add(Box.createVerticalStrut(10));

        RoundedButton btnClear = new RoundedButton("Kosongkan", AppColors.MC_GRAY, AppColors.MC_DARK);
        btnClear.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnClear.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnClear.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnClear.addActionListener(e -> clearCart());
        panel.add(btnClear);

        return panel;
    }

    public void refresh() {
        tableModel.setRowCount(0);
        var customer = parent.getGuestCustomer();
        for (OrderItem item : customer.getCart()) {
            tableModel.addRow(new Object[]{
                item.getMenu().getNamaMeu(),
                CurrencyUtil.formatShort(item.getHarga()),
                item.getQty(),
                CurrencyUtil.formatShort(item.getSubtotal()),
                "X"
            });
        }
        lblTotal.setText("Total: " + CurrencyUtil.formatShort(customer.getCartTotal()));
    }

    private void removeItem(int row) {
        var customer = parent.getGuestCustomer();
        List<OrderItem> items = customer.getCart();
        if (row < items.size()) {
            customer.removeFromCart(items.get(row).getMenu(), items.get(row).getQty());
        }
        refresh();
        parent.updateCartBadge();
    }

    private void clearCart() {
        int ok = JOptionPane.showConfirmDialog(this, "Kosongkan keranjang?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            parent.getGuestCustomer().clearCart();
            refresh();
            parent.updateCartBadge();
        }
    }

    private void goToPayment() {
        String nama = txtNama.getText().trim();
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama pemesan tidak boleh kosong.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (parent.getGuestCustomer().isCartEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang masih kosong!", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        parent.setPendingOrder(nama, txtCatatan.getText().trim());
        parent.showPage(CustomerDashboard.PAGE_PAYMENT);
    }

    private static class ButtonEditor extends DefaultCellEditor {

        private final Runnable action;

        ButtonEditor(Runnable action) {
            super(new JCheckBox());
            this.action = action;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            JButton btn = new JButton("X");
            btn.setBackground(new Color(0xFF, 0xEE, 0xEE));
            btn.setBorderPainted(false);
            btn.addActionListener(e -> {
                fireEditingStopped();
                action.run();
            });
            return btn;
        }
    }
}
