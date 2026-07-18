package tubes.main;

import tubes.util.AppColors;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {

    private JProgressBar progressBar;
    private Timer animTimer;
    private int progress = 0;

    public SplashScreen() {
        buildUI();
        setSize(500, 320);
        setLocationRelativeTo(null);
        startAnimation();
    }

    private void buildUI() {
        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, AppColors.MC_RED,
                        0, getHeight(), new Color(0x6B, 0x00, 0x10));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillOval(-60, -60, 280, 280);
                g2.fillOval(300, 180, 240, 240);
                g2.dispose();
            }
        };
        panel.setPreferredSize(new Dimension(500, 320));

        JLabel lblM = new JLabel("M", SwingConstants.CENTER);
        lblM.setFont(new Font("SansSerif", Font.BOLD, 100));
        lblM.setForeground(AppColors.MC_YELLOW);
        lblM.setBounds(150, 20, 200, 120);
        panel.add(lblM);

        JLabel lblName = new JLabel("McDonald's Kiosk System", SwingConstants.CENTER);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblName.setForeground(Color.WHITE);
        lblName.setBounds(50, 148, 400, 32);
        panel.add(lblName);

        JLabel lblVer = new JLabel("Java Swing  |  MVC + DAO", SwingConstants.CENTER);
        lblVer.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblVer.setForeground(new Color(255, 255, 255, 160));
        lblVer.setBounds(50, 182, 400, 22);
        panel.add(lblVer);

        progressBar = new JProgressBar(0, 100);
        progressBar.setBounds(60, 228, 380, 8);
        progressBar.setBorderPainted(false);
        progressBar.setForeground(AppColors.MC_YELLOW);
        progressBar.setBackground(new Color(255, 255, 255, 40));
        panel.add(progressBar);

        JLabel lblLoad = new JLabel("Memuat sistem...", SwingConstants.CENTER);
        lblLoad.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblLoad.setForeground(new Color(255, 255, 255, 140));
        lblLoad.setBounds(60, 244, 380, 20);
        panel.add(lblLoad);

        JLabel lblCopy = new JLabel("Tugas Besar PBO  |  2025", SwingConstants.CENTER);
        lblCopy.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblCopy.setForeground(new Color(255, 255, 255, 100));
        lblCopy.setBounds(60, 284, 380, 20);
        panel.add(lblCopy);

        add(panel);
    }

    private void startAnimation() {
        animTimer = new Timer(25, e -> {
            progress += 2;
            progressBar.setValue(progress);
            if (progress >= 100) {
                animTimer.stop();
            }
        });
        animTimer.start();
    }
}
