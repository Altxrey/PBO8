package com.literanusa.view;

import com.literanusa.controller.LoanController;
import com.literanusa.model.Book;
import com.literanusa.model.Loan;
import com.literanusa.model.User;
import com.literanusa.util.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.time.format.DateTimeFormatter;

public class LoanCard extends AnimatedCard {
    private final Loan loan;
    private final Book book;
    private final UserDashboardView dashboard;

    public LoanCard(Loan loan, Book book, User currentUser, UserDashboardView dashboard) {
        super();
        this.loan = loan;
        this.book = book;
        this.dashboard = dashboard;

        setLayout(new BorderLayout(0, 5));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        setPreferredSize(new Dimension(180, 340));

        JLabel coverLabel = ImageUtils.createBookCoverLabel(book.getCoverImage(), 180, 220);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 8, 8, 8));

        JLabel titleLabel = new JLabel("<html><b>" + book.getTitle() + "</b></html>");
        infoPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel loanDetailsPanel = new JPanel();
        loanDetailsPanel.setOpaque(false);
        loanDetailsPanel.setLayout(new BoxLayout(loanDetailsPanel, BoxLayout.Y_AXIS));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        JLabel dueDateLabel = new JLabel("Tenggat: " + loan.getDueDate().format(formatter));
        dueDateLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));

        loanDetailsPanel.add(dueDateLabel);
        infoPanel.add(loanDetailsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        buttonPanel.setOpaque(false);

        if (loan.getStatus() == Loan.Status.ACTIVE) {
            JButton readButton = new JButton("Baca Buku");
            readButton.addActionListener(e -> readBook());
            buttonPanel.add(readButton);

            JButton returnButton = new JButton("Kembalikan");
            returnButton.addActionListener(e -> returnBook());
            buttonPanel.add(returnButton);
        }

        infoPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(coverLabel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
    }

    private void returnBook() {
        int confirm = JOptionPane.showConfirmDialog(this, "Anda yakin ingin mengembalikan buku ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION) {
            LoanController loanController = new LoanController();
            if(loanController.returnBook(loan.getId())) {
                JOptionPane.showMessageDialog(this, "Buku berhasil dikembalikan.");
                dashboard.refreshPeminjamanPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengembalikan buku.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void readBook() {
        if (book.getPdfPath() == null || book.getPdfPath().isEmpty()) {
            JOptionPane.showMessageDialog(this, "File PDF untuk buku ini tidak tersedia.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            String pdfFileName = "/pdfs/" + book.getPdfPath();
            URL pdfUrl = getClass().getResource(pdfFileName);

            if (pdfUrl != null) {
                File pdfFile = new File(pdfUrl.toURI());
                if (Desktop.isDesktopSupported() && pdfFile.exists()) {
                    Desktop.getDesktop().open(pdfFile);
                }
            } else {
                JOptionPane.showMessageDialog(this, "File PDF tidak ditemukan: " + pdfFileName, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal membuka file PDF.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}