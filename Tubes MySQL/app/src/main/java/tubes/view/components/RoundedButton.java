package tubes.view.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import tubes.util.AppColors;

public class RoundedButton extends JButton {

    private Color bgColor;
    private Color hoverColor;
    private int radius;

    public RoundedButton(String text, Color bgColor, Color textColor) {
        super(text);
        this.bgColor = bgColor;
        this.hoverColor = bgColor.darker();
        this.radius = AppColors.CORNER_RADIUS;

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(textColor);
        setFont(AppColors.FONT_BUTTON);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(160, AppColors.BUTTON_HEIGHT));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                RoundedButton.this.bgColor = hoverColor;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                RoundedButton.this.bgColor = hoverColor.brighter();
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        super.paintComponent(g2);
        g2.dispose();
    }

    public void setBgColor(Color c) {
        this.bgColor = c;
        repaint();
    }
}
