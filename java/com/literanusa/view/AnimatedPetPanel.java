package com.literanusa.view;

import com.literanusa.util.ImageUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnimatedPetPanel extends JPanel implements ActionListener {

    private final int PANEL_WIDTH = 800; // Lebar panel, bisa disesuaikan
    private final int PANEL_HEIGHT = 100; // Tinggi panel

    private Image petSpriteSheet;
    private Timer timer;

    // Properti untuk animasi sprite
    private final int FRAME_WIDTH = 48;  // Lebar satu frame gambar kucing
    private final int FRAME_HEIGHT = 48; // Tinggi satu frame gambar kucing
    private int currentFrame = 0;
    private final int TOTAL_FRAMES_PER_ROW = 4; // Ada 4 frame per baris di sprite sheet

    // Properti untuk gerakan
    private int x = 0; // Posisi x kucing
    private int y = PANEL_HEIGHT - FRAME_HEIGHT - 10; // Posisi y kucing (di dasar panel)
    private int xVelocity = 2; // Kecepatan gerak horizontal

    public AnimatedPetPanel() {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(new Color(0, 0, 0, 0)); // Buat panel transparan
        this.setOpaque(false);

        // Muat gambar sprite sheet
        petSpriteSheet = ImageUtils.loadImageIcon("/images/cat_sprite.png", -1, -1).getImage();

        // Mulai "detak jantung" animasi, setiap 100 milidetik
        timer = new Timer(100, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (petSpriteSheet != null) {
            drawPet(g);
        }
    }

    private void drawPet(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Menentukan baris mana dari sprite sheet yang akan digunakan
        // 0 = jalan ke kanan, 1 = jalan ke kiri
        int spriteRow = (xVelocity > 0) ? 0 : 1;

        // Mengambil potongan gambar (sprite) yang sesuai dari sprite sheet
        int sx1 = currentFrame * FRAME_WIDTH;
        int sy1 = spriteRow * FRAME_HEIGHT;
        int sx2 = sx1 + FRAME_WIDTH;
        int sy2 = sy1 + FRAME_HEIGHT;

        // Menggambar sprite ke panel pada posisi x dan y
        g2d.drawImage(petSpriteSheet, x, y, x + FRAME_WIDTH, y + FRAME_HEIGHT,
                sx1, sy1, sx2, sy2, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Logika yang dijalankan setiap detak timer

        // Gerakkan kucing secara horizontal
        x += xVelocity;

        // Jika kucing mencapai batas kanan atau kiri, balik arah
        if (x >= getWidth() - FRAME_WIDTH || x < 0) {
            xVelocity = xVelocity * -1; // Balik kecepatan (misal: 2 menjadi -2)
        }

        // Ganti frame animasi untuk menciptakan ilusi berjalan
        currentFrame = (currentFrame + 1) % TOTAL_FRAMES_PER_ROW;

        // Minta panel untuk menggambar ulang dirinya dengan posisi dan frame baru
        repaint();
    }
}