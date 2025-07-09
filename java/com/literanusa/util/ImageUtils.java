package com.literanusa.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class ImageUtils {

    public static ImageIcon loadImageIcon(String path, int width, int height) {
        try {
            BufferedImage originalImage = null;

            // **PERBAIKAN: Coba muat dari resources terlebih dahulu**
            InputStream is = ImageUtils.class.getResourceAsStream(path);
            if (is != null) {
                originalImage = ImageIO.read(is);
            } else {
                // Jika tidak ada di resources, coba dari sistem file (untuk development)
                File file = new File(path);
                if (file.exists()) {
                    originalImage = ImageIO.read(file);
                }
            }

            if (originalImage != null) {
                Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }

        } catch (IOException e) {
            System.err.println("Error loading image: " + path);
            e.printStackTrace();
        }
        return null;
    }

    // **PERBAIKAN: Path yang benar untuk memuat gambar sampul**
    public static JLabel createBookCoverLabel(String coverImageName, int width, int height) {
        // Path harus dimulai dari root resources, yaitu "/"
        String imagePath = "/images/covers/" + (coverImageName != null && !coverImageName.isEmpty() ? coverImageName : "default-book-cover.jpg");
        ImageIcon coverIcon = loadImageIcon(imagePath, width, height);

        if (coverIcon != null) {
            return new JLabel(coverIcon);
        } else {
            // Fallback jika gambar tetap tidak ditemukan
            JLabel fallbackLabel = new JLabel("<html><div style='text-align: center;'>Cover<br>Not Available</div></html>", SwingConstants.CENTER);
            fallbackLabel.setPreferredSize(new Dimension(width, height));
            fallbackLabel.setOpaque(true);
            fallbackLabel.setBackground(new Color(230, 230, 230));
            fallbackLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            fallbackLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            return fallbackLabel;
        }
    }
}