package com.literanusa.view;

import javax.swing.*;
import java.awt.*;

public class AnimatedCard extends JPanel {
    private float opacity = 0.0f;

    public AnimatedCard() {
        setOpaque(false);
    }

    public void setOpacity(float opacity) {
        this.opacity = Math.max(0.0f, Math.min(1.0f, opacity));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g2d);
        g2d.dispose();
    }
}