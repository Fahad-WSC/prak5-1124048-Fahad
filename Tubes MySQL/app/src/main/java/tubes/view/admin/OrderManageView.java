package tubes.view.admin;

import tubes.controller.OrderController;
import tubes.model.Order;
import tubes.model.OrderItem;
import tubes.model.Status;
import tubes.util.AppColors;
import tubes.util.CurrencyUtil;
import tubes.view.components.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class OrderManageView extends JPanel {

    private final AdminDashboard parent;
    private final OrderController orderCtrl;

    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<String> cbFilter;
    private JTextArea detailArea;
    private RoundedButton btnFinish;
    private int selectedOrderId = -1;

    public OrderManageView(AdminDashboard parent) {
        this.parent = parent;
        this.orderCtrl = new OrderController();
        setLayout(new BorderLayout());
        setBackground(AppColors.MC_LIGHT_GRAY);
        buildUI();
    }

    private void buildUI() {
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel title = new JLabel("Kelola Order");
        title.setFont(AppColors.FONT_TITLE);
        title.setForeground(AppColors.MC_DARK);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        cbFilter = new JComboBox<>(new String[]{"Semua", "Sudah Dibayar", "Selesai"});
        cbFilter.addActionListener(e -> refresh());
        RoundedButton btnRefresh = new RoundedButton("Refresh", AppColors.MC_DARK, Color.WHITE);
        btnRefresh.setPreferredSize(new Dimension(110, 38));
        btnRefresh.addActionListener(e -> refresh());
        right.add(cbFilter);
        right.add(btnRefresh);

        header.add(title, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildTablePanel(), buildDetailPanel());
        split.setDividerLocation(680);
        split.setDividerSize(4);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(16, 16, 16, 8));

        String[] cols = {"No.Order", "Customer", "Total Bayar", "Status", "Waktu"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFont(AppColors.FONT_BODY);
        table.setRowHeight(44);
        table.setShowGrid(false);
        table.setSelectionBackground(new Color(0xFF, 0xF0, 0xCC));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(AppColors.MC_RED);
        table.getTableHeader().setForeground(Color.WHITE);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    showDetail((int) tableModel.getValueAt(row, 0));
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xEE, 0xEE, 0xEE), 1));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(16, 8, 16, 16));

        JLabel title = new JLabel("Detail Pesanan");
        title.setFont(AppColors.FONT_HEADING);
        title.setForeground(AppColors.MC_DARK);
        title.setBorder(new EmptyBorder(0, 0, 12, 0));
        panel.add(title, BorderLayout.NORTH);

        detailArea = new JTextArea("Pilih salah satu pesanan pada tabel di sebelah kiri untuk melihat rinciannya.");
        detailArea.setFont(AppColors.FONT_BODY);
        detailArea.setEditable(false);
        detailArea.setLineWrap(true);
        detailArea.setWrapStyleWord(true);
        detailArea.setBorder(new EmptyBorder(8, 8, 8, 8));

        panel.add(new JScrollPane(detailArea), BorderLayout.CENTER);

        btnFinish = new RoundedButton("Tandai Selesai (Finished)", AppColors.STATUS_FINISHED, Color.WHITE);
        btnFinish.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnFinish.setPreferredSize(new Dimension(0, 50));
        btnFinish.setEnabled(false);
        btnFinish.addActionListener(e -> finishSelectedOrder());
        panel.add(btnFinish, BorderLayout.SOUTH);

        return panel;
    }

    public void refresh() {
        List<Order> orders = orderCtrl.getAllOrders();
        String filter = (String) cbFilter.getSelectedItem();
        if ("Sudah Dibayar".equals(filter)) {
            orders = orders.stream().filter(o -> o.getStatus() == Status.PAID).collect(Collectors.toList());
        } else if ("Selesai".equals(filter)) {
            orders = orders.stream().filter(o -> o.getStatus() == Status.FINISHED).collect(Collectors.toList());
        }

        tableModel.setRowCount(0);
        for (Order o : orders) {
            tableModel.addRow(new Object[]{
                o.getId(), o.getCustomer(),
                CurrencyUtil.formatShort(o.getGrandTotal()),
                o.getStatus().getLabel(),
                o.getTanggalFormatted()
            });
        }
        selectedOrderId = -1;
        btnFinish.setEnabled(false);
        detailArea.setText("Pilih salah satu pesanan pada tabel di sebelah kiri untuk melihat rinciannya.");
    }

    private void showDetail(int orderId) {
        Order order = orderCtrl.getOrderById(orderId);
        if (order == null) {
            return;
        }
        selectedOrderId = orderId;

        StringBuilder sb = new StringBuilder();
        sb.append("Order #").append(order.getId()).append("\n");
        sb.append("Pemesan : ").append(order.getCustomer()).append("\n");
        sb.append("Waktu   : ").append(order.getTanggalFormatted()).append("\n");
        sb.append("Status  : ").append(order.getStatus().getLabel()).append("\n\n");
        sb.append("Item:\n");
        for (OrderItem item : order.getItems()) {
            sb.append(" - ").append(item.getQty()).append("x ")
                    .append(item.getMenu().getNamaMeu()).append(" = ")
                    .append(CurrencyUtil.formatShort(item.getSubtotal())).append("\n");
        }
        sb.append("\nSubtotal : ").append(CurrencyUtil.formatShort(order.getTotal())).append("\n");
        sb.append("PPN 11%  : ").append(CurrencyUtil.formatShort(order.getPajak())).append("\n");
        sb.append("Total    : ").append(CurrencyUtil.formatShort(order.getGrandTotal())).append("\n");
        sb.append("Metode   : ").append(order.getPaymentMethod() != null ? order.getPaymentMethod().getLabel() : "-").append("\n");
        if (order.getCatatan() != null && !order.getCatatan().isEmpty()) {
            sb.append("Catatan  : ").append(order.getCatatan()).append("\n");
        }

        detailArea.setText(sb.toString());
        btnFinish.setEnabled(order.getStatus() == Status.PAID);
    }

    private void finishSelectedOrder() {
        if (selectedOrderId <= 0) {
            return;
        }
        boolean ok = orderCtrl.finishOrder(selectedOrderId);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Pesanan #" + selectedOrderId + " ditandai selesai.",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            refresh();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui status pesanan.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
