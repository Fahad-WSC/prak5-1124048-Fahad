package tubes.view.customer;

import tubes.controller.OrderController;
import tubes.model.Order;
import tubes.model.PaymentMethod;
import tubes.util.AppColors;
import tubes.util.CurrencyUtil;
import tubes.view.components.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PaymentView extends JPanel {

    private final CustomerDashboard parent;
    private final OrderController orderCtrl = new OrderController();

    private final CardLayout stepLayout = new CardLayout();
    private final JPanel stepPanel = new JPanel();

    private JLabel lblSubtotal;
    private JLabel lblPajak;
    private JLabel lblGrandTotal;

    private JLabel lblCashAmount;
    private JLabel lblCashChange;
    private StringBuilder cashInput = new StringBuilder();

    private static final String STEP_METHOD = "METHOD";
    private static final String STEP_CASH = "CASH";
    private static final String STEP_PROCESSING = "PROCESSING";

    public PaymentView(CustomerDashboard parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(AppColors.MC_LIGHT_GRAY);
        setBorder(new EmptyBorder(20, 40, 20, 40));
        buildUI();
    }

    private void buildUI() {
        JLabel title = new JLabel("Pembayaran");
        title.setFont(AppColors.FONT_TITLE);
        title.setForeground(AppColors.MC_DARK);
        title.setBorder(new EmptyBorder(0, 0, 16, 0));
        add(title, BorderLayout.NORTH);

        stepPanel.setLayout(stepLayout);
        stepPanel.setOpaque(false);
        stepPanel.add(buildSummaryAndMethodStep(), STEP_METHOD);
        stepPanel.add(buildCashStep(), STEP_CASH);
        stepPanel.add(buildProcessingStep(), STEP_PROCESSING);

        add(stepPanel, BorderLayout.CENTER);
    }

    private JPanel buildSummaryAndMethodStep() {
        JPanel root = new JPanel(new BorderLayout(24, 0));
        root.setOpaque(false);

        JPanel billCard = new JPanel();
        billCard.setLayout(new BoxLayout(billCard, BoxLayout.Y_AXIS));
        billCard.setBackground(Color.WHITE);
        billCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xEE, 0xEE, 0xEE), 1),
                new EmptyBorder(24, 24, 24, 24)));
        billCard.setPreferredSize(new Dimension(340, 0));

        JLabel h = new JLabel("Rincian Tagihan");
        h.setFont(AppColors.FONT_HEADING);
        h.setAlignmentX(Component.LEFT_ALIGNMENT);
        billCard.add(h);
        billCard.add(Box.createVerticalStrut(16));

        lblSubtotal = billRow(billCard, "Subtotal", "Rp 0");
        lblPajak = billRow(billCard, "PPN (11%)", "Rp 0");
        billCard.add(Box.createVerticalStrut(8));
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        billCard.add(sep);
        billCard.add(Box.createVerticalStrut(8));

        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setOpaque(false);
        totalRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        totalRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        JLabel lblTotalTitle = new JLabel("Total Bayar");
        lblTotalTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblGrandTotal = new JLabel("Rp 0");
        lblGrandTotal.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblGrandTotal.setForeground(AppColors.MC_RED);
        totalRow.add(lblTotalTitle, BorderLayout.WEST);
        totalRow.add(lblGrandTotal, BorderLayout.EAST);
        billCard.add(totalRow);

        JPanel methodPanel = new JPanel();
        methodPanel.setLayout(new BoxLayout(methodPanel, BoxLayout.Y_AXIS));
        methodPanel.setOpaque(false);

        JLabel mh = new JLabel("Pilih Metode Pembayaran");
        mh.setFont(AppColors.FONT_HEADING);
        mh.setAlignmentX(Component.LEFT_ALIGNMENT);
        methodPanel.add(mh);
        methodPanel.add(Box.createVerticalStrut(16));

        methodPanel.add(buildMethodButton("Tunai (Cash)", "Bayar dengan uang tunai di kasir kiosk", PaymentMethod.CASH));
        methodPanel.add(Box.createVerticalStrut(12));
        methodPanel.add(buildMethodButton("Kartu Debit / Kredit", "Tap / gesek kartu pada mesin EDC", PaymentMethod.DEBIT));
        methodPanel.add(Box.createVerticalStrut(12));
        methodPanel.add(buildMethodButton("QRIS", "Pindai kode QR menggunakan e-wallet", PaymentMethod.QRIS));
        methodPanel.add(Box.createVerticalStrut(20));

        RoundedButton btnBack = new RoundedButton("Kembali ke Keranjang", AppColors.MC_GRAY, AppColors.MC_DARK);
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnBack.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnBack.setMaximumSize(new Dimension(320, 46));
        btnBack.addActionListener(e -> parent.showPage(CustomerDashboard.PAGE_CART));
        methodPanel.add(btnBack);

        root.add(billCard, BorderLayout.WEST);
        root.add(methodPanel, BorderLayout.CENTER);
        return root;
    }

    private JLabel billRow(JPanel container, String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        JLabel l = new JLabel(label);
        l.setFont(AppColors.FONT_BODY);
        JLabel v = new JLabel(value);
        v.setFont(new Font("SansSerif", Font.BOLD, 14));
        row.add(l, BorderLayout.WEST);
        row.add(v, BorderLayout.EAST);
        container.add(row);
        container.add(Box.createVerticalStrut(6));
        return v;
    }

    private JButton buildMethodButton(String title, String subtitle, PaymentMethod method) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout());
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(360, 66));
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDD, 0xDD, 0xDD), 1),
                new EmptyBorder(10, 16, 10, 16)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);

        JLabel lt = new JLabel(title);
        lt.setFont(new Font("SansSerif", Font.BOLD, 16));
        lt.setForeground(AppColors.MC_DARK);
        JLabel ls = new JLabel(subtitle);
        ls.setFont(AppColors.FONT_SMALL);
        ls.setForeground(AppColors.MC_GRAY);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(lt);
        text.add(ls);

        btn.add(text, BorderLayout.WEST);
        btn.addActionListener(e -> selectMethod(method));
        return btn;
    }

    private PaymentMethod selectedMethod;

    private void selectMethod(PaymentMethod method) {
        this.selectedMethod = method;
        if (method == PaymentMethod.CASH) {
            cashInput.setLength(0);
            updateCashLabels();
            stepLayout.show(stepPanel, STEP_CASH);
        } else {
            stepLayout.show(stepPanel, STEP_PROCESSING);
            runProcessingSimulation(method);
        }
    }

    private JPanel buildCashStep() {
        JPanel root = new JPanel(new BorderLayout(24, 0));
        root.setOpaque(false);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setPreferredSize(new Dimension(340, 0));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xEE, 0xEE, 0xEE), 1),
                new EmptyBorder(24, 24, 24, 24)));

        JLabel h = new JLabel("Pembayaran Tunai");
        h.setFont(AppColors.FONT_HEADING);
        h.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(h);
        infoPanel.add(Box.createVerticalStrut(16));

        JLabel totalLabel = new JLabel("Total Tagihan");
        totalLabel.setFont(AppColors.FONT_BODY);
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(totalLabel);

        JLabel totalValue = new JLabel();
        totalValue.setName("cashTotalValue");
        totalValue.setFont(new Font("SansSerif", Font.BOLD, 26));
        totalValue.setForeground(AppColors.MC_RED);
        totalValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(totalValue);
        infoPanel.add(Box.createVerticalStrut(20));

        JLabel cashLabel = new JLabel("Uang Diterima");
        cashLabel.setFont(AppColors.FONT_BODY);
        cashLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(cashLabel);

        lblCashAmount = new JLabel("Rp 0");
        lblCashAmount.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblCashAmount.setForeground(AppColors.MC_DARK);
        lblCashAmount.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(lblCashAmount);
        infoPanel.add(Box.createVerticalStrut(20));

        JLabel changeLabel = new JLabel("Kembalian");
        changeLabel.setFont(AppColors.FONT_BODY);
        changeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(changeLabel);

        lblCashChange = new JLabel("Rp 0");
        lblCashChange.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblCashChange.setForeground(new Color(0x2E, 0x7D, 0x32));
        lblCashChange.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(lblCashChange);

        this.cashTotalValueLabel = totalValue;

        JPanel padPanel = new JPanel(new BorderLayout(0, 16));
        padPanel.setOpaque(false);

        JPanel pad = new JPanel(new GridLayout(4, 3, 10, 10));
        pad.setOpaque(false);
        String[] keys = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "C", "0", "⌫"};
        for (String k : keys) {
            JButton btn = new JButton(k);
            btn.setFont(new Font("SansSerif", Font.BOLD, 22));
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> onCashKey(k));
            pad.add(btn);
        }
        pad.setPreferredSize(new Dimension(320, 260));

        JPanel quickAmounts = new JPanel(new GridLayout(1, 3, 10, 0));
        quickAmounts.setOpaque(false);
        long[] presets = {50000, 100000, 200000};
        for (long p : presets) {
            JButton qb = new JButton(CurrencyUtil.formatShort(p));
            qb.setFont(new Font("SansSerif", Font.BOLD, 14));
            qb.setFocusPainted(false);
            qb.setBackground(AppColors.MC_YELLOW);
            qb.addActionListener(e -> {
                cashInput.setLength(0);
                cashInput.append(p);
                updateCashLabels();
            });
            quickAmounts.add(qb);
        }

        JPanel buttons = new JPanel(new GridLayout(1, 2, 10, 0));
        buttons.setOpaque(false);
        RoundedButton btnBack = new RoundedButton("Batal", AppColors.MC_GRAY, AppColors.MC_DARK);
        btnBack.addActionListener(e -> stepLayout.show(stepPanel, STEP_METHOD));
        RoundedButton btnPay = new RoundedButton("Konfirmasi Bayar", AppColors.MC_RED, Color.WHITE);
        btnPay.addActionListener(e -> confirmCashPayment());
        buttons.add(btnBack);
        buttons.add(btnPay);

        padPanel.add(quickAmounts, BorderLayout.NORTH);
        padPanel.add(pad, BorderLayout.CENTER);
        padPanel.add(buttons, BorderLayout.SOUTH);

        root.add(infoPanel, BorderLayout.WEST);
        root.add(padPanel, BorderLayout.CENTER);
        return root;
    }

    private JLabel cashTotalValueLabel;

    private void onCashKey(String key) {
        if (key.equals("C")) {
            cashInput.setLength(0);
        } else if (key.equals("⌫")) {
            if (cashInput.length() > 0) {
                cashInput.deleteCharAt(cashInput.length() - 1);
            }
        } else {
            if (cashInput.length() < 9) {
                cashInput.append(key);
            }
        }
        updateCashLabels();
    }

    private void updateCashLabels() {
        long total = currentGrandTotal();
        long cash = cashInput.length() == 0 ? 0 : Long.parseLong(cashInput.toString());
        lblCashAmount.setText(CurrencyUtil.formatShort(cash));
        long change = cash - total;
        lblCashChange.setText(CurrencyUtil.formatShort(Math.max(change, 0)));
        lblCashChange.setForeground(change >= 0 ? new Color(0x2E, 0x7D, 0x32) : AppColors.MC_RED);
        if (cashTotalValueLabel != null) {
            cashTotalValueLabel.setText(CurrencyUtil.formatShort(total));
        }
    }

    private void confirmCashPayment() {
        long total = currentGrandTotal();
        long cash = cashInput.length() == 0 ? 0 : Long.parseLong(cashInput.toString());
        if (cash < total) {
            JOptionPane.showMessageDialog(this,
                    "Uang yang diterima kurang dari total tagihan.",
                    "Uang Tidak Cukup", JOptionPane.WARNING_MESSAGE);
            return;
        }
        finalizeOrder(PaymentMethod.CASH, cash);
    }

    private JPanel buildProcessingStep() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setOpaque(false);

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xEE, 0xEE, 0xEE), 1),
                new EmptyBorder(40, 60, 40, 60)));

        processingLabel = new JLabel("Memproses pembayaran...");
        processingLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        processingLabel.setForeground(AppColors.MC_DARK);
        processingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hint = new JLabel("Mohon tunggu sebentar");
        hint.setFont(AppColors.FONT_BODY);
        hint.setForeground(AppColors.MC_GRAY);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(processingLabel);
        box.add(Box.createVerticalStrut(8));
        box.add(hint);

        root.add(box);
        return root;
    }

    private JLabel processingLabel;

    private void runProcessingSimulation(PaymentMethod method) {
        final int[] dots = {0};
        Timer timer = new Timer(400, null);
        timer.addActionListener((ActionEvent e) -> {
            dots[0] = (dots[0] + 1) % 4;
            StringBuilder sb = new StringBuilder("Memproses pembayaran");
            for (int i = 0; i < dots[0]; i++) {
                sb.append('.');
            }
            processingLabel.setText(sb.toString());
        });
        timer.start();

        Timer done = new Timer(1600, e -> {
            timer.stop();
            finalizeOrder(method, currentGrandTotal());
        });
        done.setRepeats(false);
        done.start();
    }

    private long currentGrandTotal() {
        long subtotal = parent.getGuestCustomer().getCartTotal();
        return OrderController.hitungGrandTotal(subtotal);
    }

    private void finalizeOrder(PaymentMethod method, long bayar) {
        String nama = parent.getPendingNama();
        String catatan = parent.getPendingCatatan();
        int orderId = orderCtrl.checkout(nama, catatan, method, bayar);
        if (orderId > 0) {
            Order order = orderCtrl.getOrderById(orderId);
            parent.goToOrderSuccess(order);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Pembayaran gagal diproses. Silakan coba lagi.",
                    "Gagal", JOptionPane.ERROR_MESSAGE);
            stepLayout.show(stepPanel, STEP_METHOD);
        }
    }

    public void startPayment() {
        long subtotal = parent.getGuestCustomer().getCartTotal();
        long pajak = OrderController.hitungPajak(subtotal);
        long grandTotal = subtotal + pajak;

        lblSubtotal.setText(CurrencyUtil.formatShort(subtotal));
        lblPajak.setText(CurrencyUtil.formatShort(pajak));
        lblGrandTotal.setText(CurrencyUtil.formatShort(grandTotal));

        cashInput.setLength(0);
        stepLayout.show(stepPanel, STEP_METHOD);
    }
}
