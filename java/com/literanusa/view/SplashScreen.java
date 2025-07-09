package com.literanusa.view;

import com.literanusa.util.ImageUtils;
import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    // Skema Warna yang Konsisten
    private final Color PRIMARY_COLOR = new Color(39, 55, 77); // Biru Tua
    private final Color SECONDARY_COLOR = new Color(82, 109, 130);
    private final Color ACCENT_COLOR = new Color(0, 150, 199); // Biru Terang untuk Aksen
    private final Color WHITE = Color.WHITE;

    public SplashScreen() {
        createSplashScreen();
        showSplashScreen();
    }

    private void createSplashScreen() {
        // Menggunakan JPanel sebagai container utama
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        // Memberikan bingkai yang bagus
        panel.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 3));

        // Logo di tengah atas
        ImageIcon logoIcon = ImageUtils.loadImageIcon("/images/literanusa-logo.jpeg", 150, 150);
        JLabel logoLabel;
        if (logoIcon != null) {
            logoLabel = new JLabel(logoIcon);
        } else {
            // Fallback jika gambar tidak ditemukan
            logoLabel = new JLabel("LiteraNusa");
            logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
            logoLabel.setForeground(WHITE);
        }
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        // Panel untuk teks di bawah logo
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false); // Membuat panel transparan

        JLabel titleLabel = new JLabel("Sistem Perpustakaan Digital", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel loadingLabel = new JLabel("Memuat aplikasi...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        loadingLabel.setForeground(new Color(220, 220, 220));
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadingLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 20, 0));

        textPanel.add(titleLabel);
        textPanel.add(loadingLabel);

        // Progress bar di bagian bawah
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true); // Membuat progress bar bergerak terus
        progressBar.setStringPainted(false);
        progressBar.setBackground(SECONDARY_COLOR);
        progressBar.setForeground(ACCENT_COLOR);
        progressBar.setPreferredSize(new Dimension(0, 10));
        progressBar.setBorder(null);

        // Menambahkan semua komponen ke panel utama
        panel.add(logoLabel, BorderLayout.NORTH);
        panel.add(textPanel, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);

        // Menambahkan panel utama ke JWindow
        add(panel);
        setSize(450, 350); // Ukuran yang lebih proporsional
        setLocationRelativeTo(null); // Posisi di tengah layar
    }

    private void showSplashScreen() {
        // Tampilkan splash screen
        setVisible(true);

        // Atur timer untuk menampilkan splash screen selama 2.5 detik
        Timer splashTimer = new Timer(2500, e -> {
            // Setelah selesai, tutup splash screen
            this.dispose();
            // Buka halaman login
            new LoginView().setVisible(true);
        });

        splashTimer.setRepeats(false); // Hanya berjalan sekali
        splashTimer.start();
    }
}