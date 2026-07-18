package tubes.view.customer;

import tubes.model.Order;
import tubes.model.OrderItem;
import tubes.model.PaymentMethod;
import tubes.util.AppColors;
import tubes.util.CurrencyUtil;
import tubes.view.components.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class OrderSuccessView extends JPanel {

    public OrderSuccessView(CustomerDashboard parent, Order order) {
        setLayout(new GridBagLayout());
        setBackground(AppColors.MC_LIGHT_GRAY);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xEE, 0xEE, 0xEE), 1),
                new EmptyBorder(36, 48, 36, 48)));
        card.setPreferredSize(new Dimension(460, 0));

        JLabel check = new JLabel("\u2714", SwingConstants.CENTER);
        check.setFont(new Font("SansSerif", Font.BOLD, 56));
        check.setForeground(new Color(0x2E, 0x7D, 0x32));
        check.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(check);

        JLabel title = new JLabel("Pembayaran Berhasil!", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(AppColors.MC_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(8));
        card.add(title);

        JLabel queue = new JLabel("Nomor Antrean: #" + order.getId(), SwingConstants.CENTER);
        queue.setFont(new Font("SansSerif", Font.BOLD, 32));
        queue.setForeground(AppColors.MC_RED);
        queue.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(12));
        card.add(queue);
        card.add(Box.createVerticalStrut(20));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        card.add(sep);
        card.add(Box.createVerticalStrut(16));

        for (OrderItem item : order.getItems()) {
            card.add(receiptRow(item.getQty() + "x " + item.getMenu().getNamaMeu(),
                    CurrencyUtil.formatShort(item.getSubtotal())));
        }
        card.add(Box.createVerticalStrut(10));
        card.add(receiptRow("Subtotal", CurrencyUtil.formatShort(order.getTotal())));
        card.add(receiptRow("PPN (11%)", CurrencyUtil.formatShort(order.getPajak())));
        card.add(receiptRow("Metode Bayar", order.getPaymentMethod().getLabel()));
        if (order.getPaymentMethod() == PaymentMethod.CASH) {
            card.add(receiptRow("Uang Diterima", CurrencyUtil.formatShort(order.getBayar())));
            card.add(receiptRow("Kembalian", CurrencyUtil.formatShort(order.getKembalian())));
        }
        card.add(Box.createVerticalStrut(8));

        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setOpaque(false);
        totalRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        totalRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        JLabel lblT = new JLabel("Total Dibayar");
        lblT.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel valT = new JLabel(CurrencyUtil.formatShort(order.getGrandTotal()));
        valT.setFont(new Font("SansSerif", Font.BOLD, 18));
        valT.setForeground(AppColors.MC_RED);
        totalRow.add(lblT, BorderLayout.WEST);
        totalRow.add(valT, BorderLayout.EAST);
        card.add(totalRow);
        card.add(Box.createVerticalStrut(24));

        JLabel note = new JLabel("<html><center>Terima kasih, " + order.getCustomer()
                + "! Silakan tunggu nomor antrean Anda dipanggil.</center></html>", SwingConstants.CENTER);
        note.setFont(AppColors.FONT_BODY);
        note.setForeground(AppColors.MC_GRAY);
        note.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(note);
        card.add(Box.createVerticalStrut(24));

        RoundedButton btnDone = new RoundedButton("Pesan Lagi", AppColors.MC_RED, Color.WHITE);
        btnDone.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnDone.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDone.setMaximumSize(new Dimension(260, 50));
        btnDone.addActionListener(e -> parent.finishOrderingAndReturnToMenu());
        card.add(btnDone);

        add(card);
    }

    private JPanel receiptRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        JLabel l = new JLabel(label);
        l.setFont(AppColors.FONT_SMALL);
        JLabel v = new JLabel(value);
        v.setFont(new Font("SansSerif", Font.BOLD, 13));
        row.add(l, BorderLayout.WEST);
        row.add(v, BorderLayout.EAST);
        return row;
    }
}
