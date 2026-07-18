package tubes.view.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import tubes.util.AppColors;

public class VirtualKeyboard extends JWindow {

    private JTextField targetField;
    private boolean shiftOn = false;

    private static final String[][] ROWS = {
        {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"},
        {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p"},
        {"a", "s", "d", "f", "g", "h", "j", "k", "l"},
        {"SHIFT", "z", "x", "c", "v", "b", "n", "m", "BACKSPACE"},
        {"SPACE", "ENTER", "CLOSE"}
    };

    private static final int KEY_W = 52;
    private static final int KEY_H = 46;
    private static final int GAP = 6;

    public VirtualKeyboard(JTextField targetField) {
        this.targetField = targetField;
        setFocusableWindowState(false);
        buildUI();
        pack();
    }

    private JPanel keysContainer;

    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout(0, 6));
        main.setBackground(AppColors.MC_DARK);
        main.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.MC_YELLOW, 2),
                BorderFactory.createEmptyBorder(10, 14, 12, 14)));

        JLabel header = new JLabel("Keyboard Layar Sentuh", SwingConstants.CENTER);
        header.setForeground(AppColors.MC_YELLOW);
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        main.add(header, BorderLayout.NORTH);

        keysContainer = new JPanel();
        keysContainer.setLayout(new BoxLayout(keysContainer, BoxLayout.Y_AXIS));
        keysContainer.setBackground(AppColors.MC_DARK);
        rebuildKeys();

        main.add(keysContainer, BorderLayout.CENTER);
        add(main);
    }

    private void rebuildKeys() {
        keysContainer.removeAll();
        for (String[] row : ROWS) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, GAP, 4));
            rowPanel.setBackground(AppColors.MC_DARK);
            for (String key : row) {
                rowPanel.add(createKeyButton(key));
            }
            keysContainer.add(rowPanel);
        }
        keysContainer.revalidate();
        keysContainer.repaint();
        pack();
    }

    private JButton createKeyButton(String key) {
        JButton btn = new JButton();
        btn.setFocusable(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        switch (key) {
            case "SPACE":
                btn.setText("SPASI");
                btn.setPreferredSize(new Dimension(KEY_W * 4 + GAP * 3, KEY_H));
                btn.setBackground(new Color(0x55, 0x55, 0x55));
                btn.setForeground(Color.WHITE);
                btn.addActionListener(e -> typeChar(' '));
                break;
            case "BACKSPACE":
                btn.setText("⌫");
                btn.setFont(new Font("SansSerif", Font.BOLD, 20));
                btn.setPreferredSize(new Dimension(KEY_W + 20, KEY_H));
                btn.setBackground(new Color(0xCC, 0x33, 0x33));
                btn.setForeground(Color.WHITE);
                btn.addActionListener(e -> backspace());
                break;
            case "ENTER":
                btn.setText("ENTER");
                btn.setPreferredSize(new Dimension(KEY_W * 2 + GAP, KEY_H));
                btn.setBackground(AppColors.MC_YELLOW);
                btn.setForeground(AppColors.MC_DARK);
                btn.addActionListener(e -> pressEnter());
                break;
            case "CLOSE":
                btn.setText("TUTUP");
                btn.setPreferredSize(new Dimension(KEY_W * 2, KEY_H));
                btn.setBackground(AppColors.MC_RED);
                btn.setForeground(Color.WHITE);
                btn.addActionListener(e -> dispose());
                break;
            case "SHIFT":
                btn.setText(shiftOn ? "▲ ABC" : "▲ abc");
                btn.setPreferredSize(new Dimension(KEY_W + 30, KEY_H));
                btn.setBackground(shiftOn ? AppColors.MC_YELLOW : new Color(0x55, 0x55, 0x55));
                btn.setForeground(shiftOn ? AppColors.MC_DARK : Color.WHITE);
                btn.addActionListener(e -> {
                    shiftOn = !shiftOn;
                    rebuildKeys();
                });
                break;
            default:
                String label = shiftOn ? key.toUpperCase() : key;
                btn.setText(label);
                btn.setPreferredSize(new Dimension(KEY_W, KEY_H));
                btn.setBackground(new Color(0x44, 0x44, 0x44));
                btn.setForeground(Color.WHITE);
                btn.addActionListener(e -> typeChar(label.charAt(0)));
                break;
        }

        Color original = btn.getBackground();
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(original.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(original);
            }
        });
        return btn;
    }

    private void typeChar(char c) {
        if (targetField == null) {
            return;
        }
        int pos = Math.max(0, Math.min(targetField.getCaretPosition(), targetField.getText().length()));
        String cur = targetField.getText();
        targetField.setText(cur.substring(0, pos) + c + cur.substring(pos));
        targetField.setCaretPosition(pos + 1);
        targetField.requestFocusInWindow();
    }

    private void backspace() {
        if (targetField == null) {
            return;
        }
        String text = targetField.getText();
        int pos = Math.max(0, Math.min(targetField.getCaretPosition(), text.length()));
        if (pos > 0) {
            targetField.setText(text.substring(0, pos - 1) + text.substring(pos));
            targetField.setCaretPosition(pos - 1);
        }
        targetField.requestFocusInWindow();
    }

    private void pressEnter() {
        if (targetField == null) {
            dispose();
            return;
        }
        ActionListener[] listeners = targetField.getActionListeners();
        if (listeners.length > 0) {
            for (ActionListener al : listeners) {
                al.actionPerformed(new ActionEvent(targetField, ActionEvent.ACTION_PERFORMED, "enter"));
            }
        }
        dispose();
    }

    public void setTargetField(JTextField field) {
        this.targetField = field;
    }

    public void showBelow(Component comp) {
        Window owner = SwingUtilities.getWindowAncestor(comp);
        Rectangle bounds;
        if (owner != null) {
            bounds = owner.getBounds();
        } else {
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            bounds = new Rectangle(0, 0, screen.width, screen.height);
        }
        int x = bounds.x + (bounds.width - getWidth()) / 2;
        int y = bounds.y + bounds.height - getHeight() - 12;
        setLocation(Math.max(bounds.x, x), Math.max(bounds.y, y));
        setVisible(true);
        toFront();
    }

    public static void attach(JTextField field) {
        field.addMouseListener(new MouseAdapter() {
            private VirtualKeyboard kb;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (kb == null || !kb.isDisplayable()) {
                    kb = new VirtualKeyboard(field);
                }
                kb.setTargetField(field);
                kb.showBelow(field);
            }
        });
    }
}
