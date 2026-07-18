package tubes.main;

import java.awt.Color;

import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

import tubes.model.Customer;
import tubes.util.AppColors;
import tubes.util.SessionManager;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                setLookAndFeel();
                setUIDefaults();

                SplashScreen splash = new SplashScreen();
                splash.setVisible(true);

                Timer timer = new Timer(1800, e -> {
                    splash.dispose();
                    startKiosk();
                });
                timer.setRepeats(false);
                timer.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void startKiosk() {
        SessionManager.getInstance().login(new Customer("guest", "", "Pelanggan"));
        tubes.view.customer.CustomerDashboard dashboard = new tubes.view.customer.CustomerDashboard();
        dashboard.setVisible(true);
    }

    private static void setLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }

    private static void setUIDefaults() {
        UIManager.put("ScrollBar.width", 8);
        UIManager.put("Table.alternateRowColor", new Color(0xFF, 0xFC, 0xF0));
        UIManager.put("OptionPane.messageFont", AppColors.FONT_BODY);
        UIManager.put("OptionPane.buttonFont", AppColors.FONT_BUTTON);
        UIManager.put("ComboBox.selectionBackground", AppColors.MC_YELLOW);
        UIManager.put("ComboBox.selectionForeground", AppColors.MC_DARK);
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }
}
