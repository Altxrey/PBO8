package com.literanusa.util;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        // Muat gambar dari resources
        ImageIcon icon = ImageUtils.loadImageIcon(imagePath, -1, -1);
        if (icon != null) {
            this.backgroundImage = icon.getImage();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Gambar pola secara berulang (tiled)
            int width = getWidth();
            int height = getHeight();
            int imgWidth = backgroundImage.getWidth(this);
            int imgHeight = backgroundImage.getHeight(this);

            if (imgWidth <= 0 || imgHeight <= 0) return;

            for (int y = 0; y < height; y += imgHeight) {
                for (int x = 0; x < width; x += imgWidth) {
                    g.drawImage(backgroundImage, x, y, this);
                }
            }
        }
    }
}