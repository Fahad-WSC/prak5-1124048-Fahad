package tubes.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import tubes.controller.AuthController;
import tubes.util.AppColors;
import tubes.view.components.RoundedButton;
import tubes.view.components.VirtualKeyboard;

public class LoginView extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblError;
    private RoundedButton btnLogin;
    private VirtualKeyboard keyboard;

    private final AuthController authController;
    private final Runnable onCancel;

    public LoginView(Runnable onCancel) {
        this.authController = new AuthController();
        this.onCancel = onCancel;
        initFrame();
        buildUI();
        attachKeyboard();
    }

    private void initFrame() {
        setTitle("McDonald's Kiosk — Admin Login");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(480, 620);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel();
            }
        });
    }

    private void buildUI() {
        JPanel bgPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, AppColors.MC_RED,
                        0, getHeight(), new Color(0x8B, 0x00, 0x10));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bgPanel.setOpaque(false);
        bgPanel.add(buildCard(), new GridBagConstraints());
        add(bgPanel, BorderLayout.CENTER);
    }

    private JPanel buildCard() {
        JPanel card = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(6, 6, getWidth() - 6, getHeight() - 6, 28, 28);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 28, 28);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(420, 540));

        int cx = 210;

        JLabel lblM = new JLabel("M", SwingConstants.CENTER);
        lblM.setFont(new Font("SansSerif", Font.BOLD, 70));
        lblM.setForeground(AppColors.MC_YELLOW);
        lblM.setBounds(cx - 50, 24, 100, 80);
        card.add(lblM);

        JLabel lblBrand = new JLabel("Admin Login", SwingConstants.CENTER);
        lblBrand.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblBrand.setForeground(AppColors.MC_RED);
        lblBrand.setBounds(cx - 120, 104, 240, 34);
        card.add(lblBrand);

        JLabel lblSub = new JLabel("Khusus staf/pengelola kiosk", SwingConstants.CENTER);
        lblSub.setFont(AppColors.FONT_SMALL);
        lblSub.setForeground(AppColors.MC_GRAY);
        lblSub.setBounds(cx - 120, 138, 240, 22);
        card.add(lblSub);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(0xEE, 0xEE, 0xEE));
        sep.setBounds(30, 170, 360, 2);
        card.add(sep);

        JLabel lUser = new JLabel("Username");
        lUser.setFont(new Font("SansSerif", Font.BOLD, 14));
        lUser.setForeground(AppColors.MC_DARK);
        lUser.setBounds(30, 188, 360, 22);
        card.add(lUser);

        txtUsername = new JTextField();
        styleField(txtUsername);
        txtUsername.setBounds(30, 212, 360, 50);
        card.add(txtUsername);

        JLabel lPass = new JLabel("Password");
        lPass.setFont(new Font("SansSerif", Font.BOLD, 14));
        lPass.setForeground(AppColors.MC_DARK);
        lPass.setBounds(30, 272, 360, 22);
        card.add(lPass);

        txtPassword = new JPasswordField();
        styleField(txtPassword);
        txtPassword.setBounds(30, 296, 360, 50);
        card.add(txtPassword);

        lblError = new JLabel("", SwingConstants.CENTER);
        lblError.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblError.setForeground(AppColors.MC_RED);
        lblError.setBounds(30, 354, 360, 22);
        card.add(lblError);

        btnLogin = new RoundedButton("MASUK", AppColors.MC_RED, Color.WHITE);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnLogin.setBounds(30, 386, 360, 56);
        btnLogin.addActionListener(e -> doLogin());
        card.add(btnLogin);

        RoundedButton btnBack = new RoundedButton("Kembali ke Kiosk", AppColors.MC_GRAY, AppColors.MC_DARK);
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnBack.setBounds(30, 452, 360, 44);
        btnBack.addActionListener(e -> cancel());
        card.add(btnBack);

        JLabel lblHint = new JLabel(
                "<html><center><font color='#AAAAAA'>Demo: admin / admin123</font></center></html>",
                SwingConstants.CENTER);
        lblHint.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblHint.setBounds(20, 504, 380, 26);
        card.add(lblHint);

        txtPassword.addActionListener(e -> doLogin());
        return card;
    }

    private void styleField(JTextField field) {
        field.setFont(AppColors.FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDD, 0xDD, 0xDD), 2),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        field.setBackground(AppColors.MC_LIGHT_GRAY);
        field.setForeground(AppColors.MC_DARK);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AppColors.MC_YELLOW, 2),
                        BorderFactory.createEmptyBorder(8, 14, 8, 14)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0xDD, 0xDD, 0xDD), 2),
                        BorderFactory.createEmptyBorder(8, 14, 8, 14)));
            }
        });
    }

    private void attachKeyboard() {
        keyboard = new VirtualKeyboard(txtUsername);
        txtUsername.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                keyboard.setTargetField(txtUsername);
                keyboard.showBelow(LoginView.this);
            }
        });
        txtPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                keyboard.setTargetField(txtPassword);
                keyboard.showBelow(LoginView.this);
            }
        });

        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(e -> cancel(), esc, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void doLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        String error = authController.login(username, password);
        if (error == null) {
            onLoginSuccess();
        } else {
            lblError.setText(error);
            txtPassword.setText("");
            shakeFrame();
        }
    }

    private void onLoginSuccess() {
        if (keyboard != null) {
            keyboard.dispose();
        }
        SwingUtilities.invokeLater(() -> {
            new tubes.view.admin.AdminDashboard(onCancel).setVisible(true);
            dispose();
        });
    }

    private void cancel() {
        if (keyboard != null) {
            keyboard.dispose();
        }
        dispose();
        if (onCancel != null) {
            onCancel.run();
        }
    }

    private void shakeFrame() {
        final int[] dx = {10, -10, 10, -10, 10, -10, 10, 0};
        Point orig = getLocation();
        javax.swing.Timer timer = new javax.swing.Timer(40, null);
        final int[] i = {0};
        timer.addActionListener(e -> {
            if (i[0] < dx.length) {
                setLocation(orig.x + dx[i[0]], orig.y);
                i[0]++;
            } else {
                setLocation(orig);
                timer.stop();
            }
        });
        timer.start();
    }
}
