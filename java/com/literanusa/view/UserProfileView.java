package com.literanusa.view;

import com.literanusa.controller.AuthController;
import com.literanusa.controller.BookController;
import com.literanusa.dao.LoanDAO;
import com.literanusa.factory.DAOFactory;
import com.literanusa.model.Book;
import com.literanusa.model.Loan;
import com.literanusa.model.User;
import com.literanusa.util.ImageUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserProfileView extends JFrame {
    private User currentUser;
    private AuthController authController;
    private BookController bookController;
    private LoanDAO loanDAO;

    // Komponen UI
    private JTextField usernameField, emailField, fullNameField, phoneField;
    private JTextArea addressArea;
    private JPasswordField currentPasswordField, newPasswordField, confirmPasswordField;
    private DefaultTableModel loanHistoryModel;
    private JLabel profilePictureLabel;
    private JPanel mainPanel;

    // Skema Warna
    private final Color PRIMARY_COLOR = new Color(39, 55, 77);
    private final Color LIGHT_GRAY_BG = new Color(244, 247, 250);
    private final Color WHITE = Color.WHITE;
    private final Color DARK_TEXT = new Color(33, 37, 41);
    private final Color LIGHT_TEXT = new Color(108, 117, 125);
    private final Color SUCCESS_GREEN = new Color(40, 167, 69);
    private final Color WARNING_ORANGE = new Color(255, 193, 7);
    private final Color DANGER_RED = new Color(220, 53, 69);
    private final Color PRIMARY_TEAL = new Color(255, 193, 7);

    public UserProfileView(User user) {
        this.currentUser = user;
        this.authController = new AuthController();
        this.bookController = new BookController();
        this.loanDAO = DAOFactory.getInstance().getLoanDAO();

        initializeFrame();
        initializeComponents();
        add(mainPanel); // Tambahkan panel utama ke frame

        loadUserData();
        loadLoanHistory();
    }

    private void initializeFrame() {
        setTitle("Profil Pengguna - " + currentUser.getDisplayName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Agar tidak menutup seluruh aplikasi
        setSize(1000, 700);
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(WHITE);
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(createTabbedPane(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Profil & Pengaturan Akun");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        return headerPanel;
    }

    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabbedPane.addTab("👤 Profil Saya", createProfilePanel());
        tabbedPane.addTab("🔒 Keamanan", createSecurityPanel());
        tabbedPane.addTab("📚 Riwayat Peminjaman", createHistoryPanel());
        tabbedPane.addTab("📊 Statistik", createStatisticsPanel());

        return tabbedPane;
    }

    // --- Panel Profil Saya ---
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(LIGHT_GRAY_BG);

        panel.add(createProfilePictureSection(), BorderLayout.WEST);
        panel.add(createProfileFormSection(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProfilePictureSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        section.setPreferredSize(new Dimension(150, 0));

        profilePictureLabel = new JLabel();
        profilePictureLabel.setPreferredSize(new Dimension(120, 120));
        profilePictureLabel.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2));
        profilePictureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePictureLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton changePhotoButton = new JButton("Ubah Foto");
        changePhotoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        changePhotoButton.addActionListener(e -> changeProfilePicture());

        section.add(profilePictureLabel);
        section.add(Box.createVerticalStrut(15));
        section.add(changePhotoButton);

        return section;
    }

    private JPanel createProfileFormSection() {
        JPanel section = new JPanel(new BorderLayout(10, 10));
        section.setBorder(BorderFactory.createTitledBorder("Informasi Pribadi"));
        section.setBackground(WHITE);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        usernameField = new JTextField(25);
        usernameField.setEditable(false);
        emailField = new JTextField(25);
        fullNameField = new JTextField(25);
        phoneField = new JTextField(25);
        addressArea = new JTextArea(4, 25);

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y; formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; formPanel.add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = y; formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; formPanel.add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy = y; formPanel.add(new JLabel("Nama Lengkap:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; formPanel.add(fullNameField, gbc);
        gbc.gridx = 0; gbc.gridy = y; formPanel.add(new JLabel("Telepon:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; formPanel.add(phoneField, gbc);
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.NORTHWEST; formPanel.add(new JLabel("Alamat:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; gbc.weighty = 1.0; formPanel.add(new JScrollPane(addressArea), gbc);

        JButton updateButton = new JButton("Update Profil");
        updateButton.addActionListener(e -> updateProfile());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(WHITE);
        buttonPanel.add(updateButton);

        section.add(formPanel, BorderLayout.CENTER);
        section.add(buttonPanel, BorderLayout.SOUTH);

        return section;
    }

    // --- Panel Keamanan ---
    private JPanel createSecurityPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        currentPasswordField = new JPasswordField(20);
        newPasswordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        JButton changePasswordButton = new JButton("Ubah Password");
        changePasswordButton.addActionListener(e -> changePassword());

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Password Saat Ini:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; panel.add(currentPasswordField, gbc);
        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Password Baru:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; panel.add(newPasswordField, gbc);
        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Konfirmasi Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; panel.add(confirmPasswordField, gbc);
        gbc.gridx = 1; gbc.gridy = y; gbc.anchor = GridBagConstraints.EAST; panel.add(changePasswordButton, gbc);

        return panel;
    }

    // --- Panel Riwayat Peminjaman ---
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        String[] columns = {"ID Buku", "Judul Buku", "Tgl Pinjam", "Tgl Kembali", "Status"};
        loanHistoryModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable historyTable = new JTable(loanHistoryModel);
        panel.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        return panel;
    }

    // --- Panel Statistik ---
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(createStatisticsCards(), BorderLayout.NORTH);
        panel.add(new BarChartPanel(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatisticsCards() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        List<Loan> userLoans = loanDAO.getLoansByUserId(currentUser.getId());

        long totalLoans = userLoans.size();
        long activeLoans = userLoans.stream().filter(l -> l.getStatus() == Loan.Status.ACTIVE).count();
        long returnedBooks = userLoans.stream().filter(l -> l.getStatus() == Loan.Status.RETURNED).count();
        long overdueBooks = userLoans.stream().filter(l -> l.getStatus() == Loan.Status.OVERDUE).count();

        statsPanel.add(createStatCard("Total Pinjaman", String.valueOf(totalLoans), PRIMARY_COLOR));
        statsPanel.add(createStatCard("Sedang Dipinjam", String.valueOf(activeLoans), WARNING_ORANGE));
        statsPanel.add(createStatCard("Sudah Kembali", String.valueOf(returnedBooks), SUCCESS_GREEN));
        statsPanel.add(createStatCard("Terlambat", String.valueOf(overdueBooks), DANGER_RED));

        return statsPanel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.brighter(), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(valueLabel);
        card.add(titleLabel);
        return card;
    }

    // --- Logika dan Data ---
    private void loadUserData() {
        ImageIcon profileIcon = ImageUtils.loadImageIcon(currentUser.getProfilePicturePath(), 110, 110);
        if (profileIcon != null) {
            profilePictureLabel.setIcon(profileIcon);
        } else {
            profilePictureLabel.setText("No Image");
        }

        usernameField.setText(currentUser.getUsername());
        emailField.setText(currentUser.getEmail());
        fullNameField.setText(currentUser.getFullName());
        phoneField.setText(currentUser.getPhone());
        addressArea.setText(currentUser.getAddress());
    }

    private void loadLoanHistory() {
        if(loanHistoryModel == null) return;
        loanHistoryModel.setRowCount(0);
        List<Loan> loans = loanDAO.getLoansByUserId(currentUser.getId());
        for (Loan loan : loans) {
            Book book = bookController.getBookById(loan.getBookId());
            loanHistoryModel.addRow(new Object[]{
                    loan.getBookId(),
                    book != null ? book.getTitle() : "Buku Tidak Dikenal",
                    loan.getLoanDate(),
                    loan.getReturnDate() != null ? loan.getReturnDate() : "-",
                    loan.getStatus()
            });
        }
    }

    private void updateProfile() {
        JOptionPane.showMessageDialog(this, "Fitur ini sedang dalam pengembangan.");
    }
    private void changePassword() {
        JOptionPane.showMessageDialog(this, "Fitur ini sedang dalam pengembangan.");
    }
    private void changeProfilePicture() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Pilih Foto Profil Baru");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                String extension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
                String newFileName = "user_" + currentUser.getId() + "_" + System.currentTimeMillis() + extension;
                Path targetPath = Paths.get("src/main/resources/images/profiles/" + newFileName);

                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                if (authController.updateProfilePicture(currentUser.getId(), newFileName)) {
                    currentUser.setProfilePicture(newFileName);
                    loadUserData();
                    JOptionPane.showMessageDialog(this, "Foto profil berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal mengupdate foto profil di database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memproses file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    // Inner Class untuk Bar Chart
    private class BarChartPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            List<Loan> userLoans = loanDAO.getLoansByUserId(currentUser.getId());
            Map<String, Long> loansByMonth = userLoans.stream()
                    .collect(Collectors.groupingBy(
                            loan -> loan.getLoanDate().format(DateTimeFormatter.ofPattern("MMM")),
                            Collectors.counting()
                    ));

            if (loansByMonth.isEmpty()) {
                g.drawString("Belum ada data untuk ditampilkan.", 10, 20);
                return;
            }

            int barWidth = 40;
            int x = 50;
            g2d.setColor(PRIMARY_TEAL);
            for (Map.Entry<String, Long> entry : loansByMonth.entrySet()) {
                long count = entry.getValue();
                int barHeight = (int) (count * 30);
                g2d.fillRect(x, getHeight() - barHeight - 30, barWidth, barHeight);
                g2d.setColor(Color.BLACK);
                g2d.drawString(entry.getKey(), x + 10, getHeight() - 10);
                x += 60;
                g2d.setColor(PRIMARY_TEAL);
            }
        }
    }
}