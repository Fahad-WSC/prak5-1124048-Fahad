package tubes.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtil {

    private static final String[] IMAGE_DIRS = {
        "assets/images/",
        "../assets/images/",
        "app/assets/images/",
    };

    public static ImageIcon loadMenuImage(String fileName, int w, int h) {
        if (fileName != null && !fileName.isEmpty()) {
            for (String dir : IMAGE_DIRS) {
                try {
                    File f = new File(dir + fileName);
                    if (f.exists()) {
                        Image img = ImageIO.read(f).getScaledInstance(w, h, Image.SCALE_SMOOTH);
                        return new ImageIcon(img);
                    }
                } catch (Exception ignored) {}
            }
            System.err.println("[ImageUtil] Gambar tidak ditemukan: " + fileName
                + " (cari di: " + new File(".").getAbsolutePath() + ")");
        }
        return createPlaceholder(w, h);
    }

    private static ImageIcon createPlaceholder(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0xF0, 0xF0, 0xF0));
        g2.fillRoundRect(0, 0, w, h, 12, 12);

        g2.setColor(new Color(0xCC, 0xCC, 0xCC));
        g2.drawRoundRect(0, 0, w-1, h-1, 12, 12);

        g2.setColor(new Color(0xDA, 0x02, 0x1A));
        g2.setFont(new Font("SansSerif", Font.BOLD, Math.max(w/4, 20)));
        g2.drawString("M", w/2 - w/8, h/2 + h/10);
        g2.dispose();
        return new ImageIcon(img);
    }
}
