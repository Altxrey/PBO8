package com.literanusa.view;

import com.literanusa.controller.BookController;
import com.literanusa.dao.LoanDAO;
import com.literanusa.dao.UserDAO;
import com.literanusa.factory.DAOFactory;
import com.literanusa.model.Book;
import com.literanusa.model.Loan;
import com.literanusa.model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class AdminDashboardView extends JFrame {
    private User currentUser;
    private BookController bookController;
    private LoanDAO loanDAO;
    private JTabbedPane tabbedPane;
    private JTable booksTable;
    private JTable loansTable;
    private JTable usersTable;
    private DefaultTableModel booksTableModel;
    private DefaultTableModel loansTableModel;
    private DefaultTableModel usersTableModel;

    // Skema Warna Modern
    private final Color PRIMARY_TEAL = new Color(47, 79, 79);
    private final Color SECONDARY_TEAL = new Color(95, 158, 160);
    private final Color LIGHT_GRAY = new Color(248, 249, 250);
    private final Color WHITE = Color.WHITE;
    private final Color DARK_TEXT = new Color(33, 37, 41);
    private final Color SUCCESS_GREEN = new Color(40, 167, 69);
    private final Color WARNING_ORANGE = new Color(255, 193, 7);
    private final Color DANGER_RED = new Color(220, 53, 69);

    public AdminDashboardView(User user) {
        this.currentUser = user;
        this.bookController = new BookController();
        this.loanDAO = DAOFactory.getInstance().getLoanDAO();
        initializeComponents();
        loadData();
    }

    private void initializeComponents() {
        setTitle("Admin Dashboard - LiteraNusa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(WHITE);
        mainPanel.add(createAdminHeader(), BorderLayout.NORTH);

        // **PERBAIKAN UTAMA: Menggunakan JTabbedPane untuk navigasi**
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        initializeTables();

        // Menambahkan panel-panel sebagai tab
        tabbedPane.addTab("📊 Dashboard", createDashboardPanel());
        tabbedPane.addTab("📚 Manajemen Buku", createBookManagementPanel());
        tabbedPane.addTab("👤 Manajemen Pengguna", createUserManagementPanel());
        tabbedPane.addTab("🔄 Manajemen Pinjaman", createLoanManagementPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void initializeTables() {
        // Inisialisasi model dan tabel untuk buku
        String[] booksColumns = {"ID", "Judul", "Penulis", "Genre", "Rating", "Tersedia", "Total"};
        booksTableModel = new DefaultTableModel(booksColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        booksTable = new JTable(booksTableModel);

        // Inisialisasi model dan tabel untuk pinjaman
        String[] loansColumns = {"ID Pinjaman", "ID Pengguna", "ID Buku", "Tgl Pinjam", "Jatuh Tempo", "Status"};
        loansTableModel = new DefaultTableModel(loansColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        loansTable = new JTable(loansTableModel);

        // Inisialisasi model dan tabel untuk pengguna
        String[] usersColumns = {"ID", "Username", "Email", "Nama Lengkap", "Role"};
        usersTableModel = new DefaultTableModel(usersColumns, 0){
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        usersTable = new JTable(usersTableModel);
    }

    private JPanel createAdminHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_TEAL);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("🛡️ LiteraNusa Admin Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(WHITE);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Selamat datang, " + currentUser.getFullName());
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeLabel.setForeground(LIGHT_GRAY);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutButton.setBackground(DANGER_RED);
        logoutButton.setForeground(WHITE);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> {
            new LoginView().setVisible(true);
            this.dispose();
        });

        rightPanel.add(welcomeLabel);
        rightPanel.add(logoutButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    // Panel untuk tab Dashboard
    private JScrollPane createDashboardPanel() {
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BoxLayout(dashboardPanel, BoxLayout.Y_AXIS));
        dashboardPanel.setBackground(LIGHT_GRAY);
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        dashboardPanel.add(createStatsSection());
        JScrollPane scrollPane = new JScrollPane(dashboardPanel);
        scrollPane.setBorder(null);
        return scrollPane;
    }

    // Panel untuk tab Manajemen Buku
    private JPanel createBookManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBookButton = new JButton("➕ Tambah Buku Baru");
        addBookButton.addActionListener(e -> showAddBookDialog());
        toolbar.add(addBookButton);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(booksTable), BorderLayout.CENTER);
        return panel;
    }

    // Panel untuk tab Manajemen Pengguna
    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JScrollPane(usersTable), BorderLayout.CENTER);
        return panel;
    }

    // Panel untuk tab Manajemen Pinjaman
    private JPanel createLoanManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JScrollPane(loansTable), BorderLayout.CENTER);
        return panel;
    }

    // ... (sisa method seperti loadData, createStatsSection, dll tetap sama)

    private void loadData() {
        loadBooks();
        loadAllLoans();
        loadAllUsers();
    }

    private void loadBooks() {
        booksTableModel.setRowCount(0);
        List<Book> books = bookController.getAllBooks();
        for (Book book : books) {
            booksTableModel.addRow(new Object[]{
                    book.getId(), book.getTitle(), book.getAuthor(), book.getGenre(),
                    book.getRating(), book.getAvailableCopies(), book.getTotalCopies()
            });
        }
    }

    private void loadAllLoans() {
        loansTableModel.setRowCount(0);
        List<Loan> loans = loanDAO.getAllLoans();
        for (Loan loan : loans) {
            loansTableModel.addRow(new Object[]{
                    loan.getId(), loan.getUserId(), loan.getBookId(),
                    loan.getLoanDate(), loan.getDueDate(), loan.getStatus()
            });
        }
    }

    private void loadAllUsers() {
        usersTableModel.setRowCount(0);
        List<User> users = new UserDAO().getAllUsers();
        for (User user : users) {
            usersTableModel.addRow(new Object[]{
                    user.getId(), user.getUsername(), user.getEmail(), user.getFullName(), user.getRole()
            });
        }
    }

    private JPanel createStatsSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(WHITE);
        section.setBorder(BorderFactory.createTitledBorder("Statistik Perpustakaan"));

        JPanel statsGrid = new JPanel(new GridLayout(1, 4, 20, 0));
        statsGrid.setBackground(WHITE);
        statsGrid.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        List<Book> allBooks = bookController.getAllBooks();
        List<Loan> allLoans = loanDAO.getAllLoans();

        int totalBooks = allBooks.size();
        int availableBooks = allBooks.stream().mapToInt(Book::getAvailableCopies).sum();
        long activeLoans = allLoans.stream().filter(l -> l.getStatus() == Loan.Status.ACTIVE).count();
        long overdueLoans = allLoans.stream().filter(l -> l.getStatus() == Loan.Status.OVERDUE).count();

        statsGrid.add(createStatCard("📚", "Total Judul Buku", String.valueOf(totalBooks), PRIMARY_TEAL));
        statsGrid.add(createStatCard("✅", "Stok Tersedia", String.valueOf(availableBooks), SUCCESS_GREEN));
        statsGrid.add(createStatCard("📖", "Buku Dipinjam", String.valueOf(activeLoans), WARNING_ORANGE));
        statsGrid.add(createStatCard("⚠️", "Pinjaman Terlambat", String.valueOf(overdueLoans), DANGER_RED));

        section.add(statsGrid, BorderLayout.CENTER);
        return section;
    }

    private JPanel createStatCard(String icon, String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(DARK_TEXT);

        textPanel.add(valueLabel);
        textPanel.add(titleLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    private void showAddBookDialog() {
        JDialog dialog = new JDialog(this, "Tambah Buku Baru", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JTextField titleField = new JTextField(20);
        JTextField authorField = new JTextField(20);
        JTextField isbnField = new JTextField(20);
        JTextField genreField = new JTextField(20);
        JTextField copiesField = new JTextField(5);
        JTextArea synopsisArea = new JTextArea(5, 20);

        JTextField pdfPathField = new JTextField(20);
        pdfPathField.setEditable(false);
        JButton choosePdfButton = new JButton("Pilih PDF...");

        gbc.gridy = 0; formPanel.add(new JLabel("Judul:"), gbc);
        gbc.gridy++; formPanel.add(titleField, gbc);
        gbc.gridy++; formPanel.add(new JLabel("Penulis:"), gbc);
        gbc.gridy++; formPanel.add(authorField, gbc);
        gbc.gridy++; formPanel.add(new JLabel("ISBN:"), gbc);
        gbc.gridy++; formPanel.add(isbnField, gbc);
        gbc.gridy++; formPanel.add(new JLabel("Genre:"), gbc);
        gbc.gridy++; formPanel.add(genreField, gbc);
        gbc.gridy++; formPanel.add(new JLabel("Jumlah Eksemplar:"), gbc);
        gbc.gridy++; formPanel.add(copiesField, gbc);
        gbc.gridy++; formPanel.add(new JLabel("Sinopsis:"), gbc);
        gbc.gridy++; formPanel.add(new JScrollPane(synopsisArea), gbc);

        gbc.gridy++; formPanel.add(new JLabel("File PDF:"), gbc);
        gbc.gridy++;
        JPanel pdfPanel = new JPanel(new BorderLayout());
        pdfPanel.add(pdfPathField, BorderLayout.CENTER);
        pdfPanel.add(choosePdfButton, BorderLayout.EAST);
        formPanel.add(pdfPanel, gbc);

        choosePdfButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Pilih File PDF");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Documents", "pdf"));
            int result = fileChooser.showOpenDialog(dialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                pdfPathField.setText(selectedFile.getName());
            }
        });

        JButton saveButton = new JButton("Simpan");
        saveButton.addActionListener(e -> {
            try {
                Book newBook = new Book();
                newBook.setTitle(titleField.getText());
                newBook.setAuthor(authorField.getText());
                newBook.setIsbn(isbnField.getText());
                newBook.setGenre(genreField.getText());
                newBook.setSynopsis(synopsisArea.getText());
                int copies = Integer.parseInt(copiesField.getText());
                newBook.setAvailableCopies(copies);
                newBook.setTotalCopies(copies);
                newBook.setPdfPath(pdfPathField.getText());

                if (bookController.addBook(newBook)) {
                    JOptionPane.showMessageDialog(dialog, "Buku berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    loadBooks(); // Muat ulang tabel
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Gagal menambahkan buku.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Jumlah eksemplar harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        dialog.add(saveButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}