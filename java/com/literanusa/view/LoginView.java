package com.literanusa.view;

import com.literanusa.controller.AuthController;
import com.literanusa.model.User;
import com.literanusa.util.ImageUtils;
import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private final AuthController authController;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    // Definisikan warna-warna modern
    private final Color PRIMARY_TEAL = new Color(95, 158, 160);
    private final Color SECONDARY_TEAL = new Color(72, 201, 176);
    private final Color LIGHT_GRAY = new Color(248, 249, 250);
    private final Color WHITE = Color.WHITE;
    private final Color DARK_TEAL = new Color(47, 79, 79);

    public LoginView() {
        this.authController = new AuthController();
        initializeFrame();
        addComponents();
        // Set tombol login default setelah semua ;komponen ditambahkan ke frame
        getRootPane().setDefaultButton(loginButton);
    }

    private void initializeFrame() {
        setTitle("LiteraNusa - Sistem Perpustakaan Digital");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(LIGHT_GRAY);
        setLayout(new BorderLayout());
    }

    private void addComponents() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createLoginPanel(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_TEAL);
        headerPanel.setPreferredSize(new Dimension(0, 100));

        // Memuat gambar header dengan lebih aman
        ImageIcon headerIcon = ImageUtils.loadImageIcon("/images/literanusa-header.jpeg", 800, 100);
        JLabel headerLabel = new JLabel(headerIcon);

        // Menambahkan logo dan judul di atas gambar header (jika gambar ada)
        headerLabel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        ImageIcon logoIcon = ImageUtils.loadImageIcon("/images/literanusa-logo.jpeg", 60, 60);
        JLabel logoLabel = new JLabel("LiteraNusa", logoIcon, SwingConstants.LEFT);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logoLabel.setForeground(WHITE);
        headerLabel.add(logoLabel);

        headerPanel.add(headerLabel, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel createLoginPanel() {
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(LIGHT_GRAY);

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(WHITE);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));

        // Inisialisasi komponen
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        JButton registerButton = new JButton("Belum punya akun? Daftar di sini");

        // Style tombol
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(PRIMARY_TEAL);
        loginButton.setForeground(WHITE);
        styleLinkButton(registerButton);

        // Event Listeners
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> showRegisterDialog());
        passwordField.addActionListener(e -> handleLogin());

        // Tata Letak
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel titleLabel = new JLabel("Selamat Datang");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(DARK_TEAL);
        loginPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 1; loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridy = 2; loginPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1; loginPanel.add(usernameField, gbc);
        gbc.gridy = 2; loginPanel.add(passwordField, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);
        gbc.gridy = 4;
        loginPanel.add(registerButton, gbc);

        wrapperPanel.add(loginPanel);
        return wrapperPanel;
    }

    private void styleLinkButton(JButton button) {
        button.setBorder(null);
        button.setOpaque(false);
        button.setBackground(new Color(0,0,0,0));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setForeground(PRIMARY_TEAL);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(DARK_TEAL);
        footerPanel.setPreferredSize(new Dimension(0, 40));
        JLabel footerLabel = new JLabel("© 2025 LiteraNusa. All Rights Reserved.");
        footerLabel.setForeground(WHITE);
        footerPanel.add(footerLabel);
        return footerPanel;
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = authController.login(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login berhasil! Selamat datang, " + user.getDisplayName(), "Sukses", JOptionPane.INFORMATION_MESSAGE);
            if (user.getRole() == User.Role.ADMIN) {
                new AdminDashboardView(user).setVisible(true);
            } else {
                new UserDashboardView(user).setVisible(true);
            }
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Username atau password salah!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showRegisterDialog() {
        JDialog registerDialog = new JDialog(this, "Daftar Akun Baru", true);
        registerDialog.setSize(400, 350);
        registerDialog.setLocationRelativeTo(this);
        registerDialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField regUsernameField = new JTextField(20);
        JPasswordField regPasswordField = new JPasswordField(20);
        JTextField regEmailField = new JTextField(20);
        JTextField regFullNameField = new JTextField(20);
        JButton submitButton = new JButton("Daftar");
        submitButton.setBackground(SECONDARY_TEAL);
        submitButton.setForeground(Color.BLACK);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(regUsernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(regPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(regEmailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Nama Lengkap:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(regFullNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(submitButton, gbc);

        submitButton.addActionListener(e -> {
            String username = regUsernameField.getText().trim();
            String password = new String(regPasswordField.getPassword());
            String email = regEmailField.getText().trim();
            String fullName = regFullNameField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
                JOptionPane.showMessageDialog(registerDialog, "Semua kolom wajib diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String result = authController.register(username, password, email, fullName);
            switch (result) {
                case "SUCCESS":
                    JOptionPane.showMessageDialog(registerDialog, "Registrasi berhasil! Silakan login.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    registerDialog.dispose();
                    break;
                case "DUPLICATE_USERNAME":
                    JOptionPane.showMessageDialog(registerDialog, "Username '" + username + "' sudah digunakan.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                case "DUPLICATE_EMAIL":
                    JOptionPane.showMessageDialog(registerDialog, "Email '" + email + "' sudah terdaftar.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    JOptionPane.showMessageDialog(registerDialog, "Terjadi kesalahan pada server. Registrasi gagal.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        });

        registerDialog.add(panel, BorderLayout.CENTER);
        registerDialog.setVisible(true);
    }
}