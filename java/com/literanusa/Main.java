package com.literanusa;

import com.formdev.flatlaf.FlatLightLaf;
import com.literanusa.util.DatabaseConnection;
import com.literanusa.view.SplashScreen;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingSource;
import org.jdesktop.core.animation.timing.sources.ScheduledExecutorTimingSource; // 1. Import yang diperlukan
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource; // 2. Import yang diperlukan

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Atur Look and Feel Modern
                FlatLightLaf.setup();
                UIManager.put("Button.arc", 12);
                UIManager.put("Component.arc", 12);
                UIManager.put("ProgressBar.arc", 12);
                UIManager.put("TextComponent.arc", 12);

                // **PERBAIKAN UTAMA: Mengatur "Detak Jantung" untuk semua animasi**
                final TimingSource timingSource = new SwingTimerTimingSource();
                timingSource.init(); // Inisialisasi sumber timer
                Animator.setDefaultTimingSource(timingSource); // Atur sebagai default

                // Inisialisasi koneksi database
                DatabaseConnection.getInstance();

                // Tampilkan SplashScreen
                new SplashScreen().setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}