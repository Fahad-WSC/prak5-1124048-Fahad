package tubes.view.components;

import tubes.model.Menu;
import tubes.util.AppColors;
import tubes.util.CurrencyUtil;
import tubes.util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuCard extends JPanel {

    private final Menu menu;
    private int qty = 0;
    private MenuCardListener listener;
    private JLabel lblQty;
    private boolean hovered = false;

    public interface MenuCardListener {

        void onAddToCart(Menu menu, int qty);
    }

    public MenuCard(Menu menu) {
        this.menu = menu;
        setPreferredSize(new Dimension(AppColors.CARD_WIDTH, AppColors.CARD_HEIGHT));
        setLayout(new BorderLayout(0, 4));
        setOpaque(false);
        buildUI();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }

    private void buildUI() {
        JLabel imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imgLabel.setIcon(ImageUtil.loadMenuImage(menu.getGambar(), 130, 110));
        imgLabel.setPreferredSize(new Dimension(AppColors.CARD_WIDTH, 120));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 8, 4, 8));

        JLabel lblName = new JLabel(menu.getNamaMeu(), SwingConstants.CENTER);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblName.setForeground(AppColors.MC_DARK);

        JLabel lblPrice = new JLabel(CurrencyUtil.formatShort(menu.getHarga()), SwingConstants.CENTER);
        lblPrice.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblPrice.setForeground(AppColors.MC_RED);

        infoPanel.add(lblName);
        infoPanel.add(lblPrice);

        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        qtyPanel.setOpaque(false);

        JButton btnMinus = makeQtyBtn("-");
        lblQty = new JLabel("0", SwingConstants.CENTER);
        lblQty.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblQty.setPreferredSize(new Dimension(30, 30));
        JButton btnPlus = makeQtyBtn("+");

        btnMinus.addActionListener(e -> {
            if (qty > 0) {
                qty--;
                lblQty.setText(String.valueOf(qty));
            }
        });
        btnPlus.addActionListener(e -> {
            qty++;
            lblQty.setText(String.valueOf(qty));
        });

        qtyPanel.add(btnMinus);
        qtyPanel.add(lblQty);
        qtyPanel.add(btnPlus);

        RoundedButton btnAdd = new RoundedButton("+ Tambah", AppColors.MC_YELLOW, AppColors.MC_DARK);
        btnAdd.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnAdd.setPreferredSize(new Dimension(140, 34));
        btnAdd.addActionListener(e -> {
            int addQty = (qty == 0) ? 1 : qty;
            if (listener != null) {
                listener.onAddToCart(menu, addQty);
            }
            qty = 0;
            lblQty.setText("0");
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);
        btnPanel.add(btnAdd);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(infoPanel, BorderLayout.NORTH);
        bottomPanel.add(qtyPanel, BorderLayout.CENTER);
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);

        add(imgLabel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton makeQtyBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 18));
        btn.setPreferredSize(new Dimension(34, 34));
        btn.setFocusPainted(false);
        btn.setBackground(AppColors.MC_LIGHT_GRAY);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0, 0, 0, 20));
        g2.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, AppColors.CORNER_RADIUS, AppColors.CORNER_RADIUS);
        g2.setColor(hovered ? new Color(0xFF, 0xF8, 0xE1) : Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, AppColors.CORNER_RADIUS, AppColors.CORNER_RADIUS);
        if (hovered) {
            g2.setColor(AppColors.MC_YELLOW);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, getWidth() - 6, getHeight() - 6, AppColors.CORNER_RADIUS, AppColors.CORNER_RADIUS);
        }
        g2.dispose();
        super.paintComponent(g);
    }

    public void setListener(MenuCardListener l) {
        this.listener = l;
    }

    public Menu getMenu() {
        return menu;
    }
}
