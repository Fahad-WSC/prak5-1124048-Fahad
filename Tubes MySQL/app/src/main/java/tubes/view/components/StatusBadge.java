package tubes.view.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import tubes.model.Status;
import tubes.util.AppColors;

public class StatusBadge extends JLabel {

    public StatusBadge(Status status) {
        super(status.getLabel(), SwingConstants.CENTER);
        setOpaque(false);
        setFont(new Font("SansSerif", Font.BOLD, 13));
        setForeground(Color.WHITE);
        setPreferredSize(new Dimension(120, 28));
        updateStatus(status);
    }

    public void updateStatus(Status status) {
        setText(status.getLabel());
        switch (status) {
            case PAID:
                setBackground(AppColors.STATUS_PAID);
                break;
            case FINISHED:
                setBackground(AppColors.STATUS_FINISHED);
                break;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
        super.paintComponent(g2);
        g2.dispose();
    }
}
