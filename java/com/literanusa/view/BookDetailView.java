package com.literanusa.view;

import com.literanusa.controller.LoanController;
import com.literanusa.controller.ReviewController;
import com.literanusa.controller.WishlistController;
import com.literanusa.model.Book;
import com.literanusa.model.Review;
import com.literanusa.model.User;
import com.literanusa.util.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BookDetailView extends JFrame {
    private final Book book;
    private final User currentUser;
    private final WishlistController wishlistController;
    private final LoanController loanController;
    private final ReviewController reviewController;
    private JPanel reviewsDisplayPanel;

    private final Color PRIMARY_TEAL = new Color(95, 158, 160);
    private final Color LIGHT_GRAY_BG = new Color(245, 245, 245);
    private final Color DARK_TEXT = new Color(33, 37, 41);
    private final Color LIGHT_TEXT = new Color(108, 117, 125);
    private final Color ORANGE_RATING = new Color(255, 165, 0);

    public BookDetailView(Book book, User currentUser) {
        this.book = book;
        this.currentUser = currentUser;
        this.wishlistController = new WishlistController();
        this.loanController = new LoanController();
        this.reviewController = new ReviewController();

        initializeComponents();
        loadReviews(); // Muat ulasan saat jendela dibuka
    }

    private void initializeComponents() {
        setTitle("Detail Buku - " + book.getTitle());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 0));
        mainPanel.setBackground(LIGHT_GRAY_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(createLeftPanel(), BorderLayout.WEST);
        mainPanel.add(createRightPanel(), BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(220, 0));

        JLabel coverLabel = ImageUtils.createBookCoverLabel(book.getCoverImage(), 200, 280);
        coverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(coverLabel);
        leftPanel.add(Box.createVerticalStrut(20));

        JButton borrowButton = new JButton("📖 Pinjam Buku");
        styleActionButton(borrowButton, PRIMARY_TEAL);
        borrowButton.setEnabled(book.getAvailableCopies() > 0);
        borrowButton.addActionListener(e -> borrowBook());
        leftPanel.add(borrowButton);
        leftPanel.add(Box.createVerticalStrut(10));

        JButton addToWishlistButton = new JButton("❤️ Tambah ke Wishlist");
        styleActionButton(addToWishlistButton, new Color(255, 105, 180));
        addToWishlistButton.addActionListener(e -> addToWishlist());
        leftPanel.add(addToWishlistButton);

        return leftPanel;
    }

    private void styleActionButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JScrollPane scrollPane = new JScrollPane(createContentContainer());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        return rightPanel;
    }

    private JPanel createContentContainer() {
        JPanel infoContainer = new JPanel();
        infoContainer.setLayout(new BoxLayout(infoContainer, BoxLayout.Y_AXIS));
        infoContainer.setBackground(Color.WHITE);
        infoContainer.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        infoContainer.add(createBookInfoHeader());
        infoContainer.add(Box.createVerticalStrut(20));
        infoContainer.add(createBookInfoDetails());
        infoContainer.add(Box.createVerticalStrut(20));
        infoContainer.add(createSynopsisPanel());
        infoContainer.add(Box.createVerticalStrut(20));
        infoContainer.add(createReviewsPanel());

        return infoContainer;
    }

    private JPanel createBookInfoHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(book.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(DARK_TEXT);

        JLabel ratingLabel = new JLabel(String.format("⭐ %.1f / 5.0", book.getRating()));
        ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        ratingLabel.setForeground(ORANGE_RATING);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(ratingLabel, BorderLayout.EAST);
        return headerPanel;
    }

    private JPanel createBookInfoDetails() {
        JPanel detailsPanel = new JPanel(new GridLayout(2, 2, 20, 15));
        detailsPanel.setOpaque(false);

        detailsPanel.add(createInfoBlock("Penulis", book.getAuthor()));
        detailsPanel.add(createInfoBlock("Genre", book.getGenre()));
        detailsPanel.add(createInfoBlock("ISBN", book.getIsbn() != null ? book.getIsbn() : "-"));
        detailsPanel.add(createInfoBlock("Ketersediaan", book.getAvailableCopies() + " dari " + book.getTotalCopies() + " eksemplar"));

        return detailsPanel;
    }

    private JPanel createInfoBlock(String title, String value) {
        JPanel block = new JPanel();
        block.setLayout(new BoxLayout(block, BoxLayout.Y_AXIS));
        block.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(LIGHT_TEXT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueLabel.setForeground(DARK_TEXT);

        block.add(titleLabel);
        block.add(valueLabel);

        return block;
    }

    private JPanel createSynopsisPanel() {
        JPanel synopsisPanel = new JPanel(new BorderLayout());
        synopsisPanel.setOpaque(false);
        synopsisPanel.setBorder(BorderFactory.createTitledBorder("Sinopsis"));

        JTextArea synopsisArea = new JTextArea(book.getSynopsis() != null ? book.getSynopsis() : "Sinopsis tidak tersedia.");
        synopsisArea.setEditable(false);
        synopsisArea.setLineWrap(true);
        synopsisArea.setWrapStyleWord(true);
        synopsisArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        synopsisArea.setOpaque(false);

        JScrollPane synopsisScroll = new JScrollPane(synopsisArea);
        synopsisScroll.setBorder(null);
        synopsisScroll.setOpaque(false);
        synopsisScroll.getViewport().setOpaque(false);

        synopsisPanel.add(synopsisScroll, BorderLayout.CENTER);
        return synopsisPanel;
    }

    private JPanel createReviewsPanel() {
        JPanel reviewsPanel = new JPanel(new BorderLayout(10, 10));
        reviewsPanel.setOpaque(false);
        reviewsPanel.setBorder(BorderFactory.createTitledBorder("Ulasan Pembaca"));

        reviewsDisplayPanel = new JPanel();
        reviewsDisplayPanel.setLayout(new BoxLayout(reviewsDisplayPanel, BoxLayout.Y_AXIS));
        reviewsDisplayPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(reviewsDisplayPanel);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(0, 150));

        JButton rateButton = new JButton("Beri Ulasan Anda");
        rateButton.addActionListener(e -> rateBook());

        reviewsPanel.add(scrollPane, BorderLayout.CENTER);
        reviewsPanel.add(rateButton, BorderLayout.SOUTH);

        return reviewsPanel;
    }

    private void borrowBook() {
        int confirm = JOptionPane.showConfirmDialog(this, "Pinjam buku '" + book.getTitle() + "'?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (loanController.createLoan(currentUser.getId(), book.getId())) {
                JOptionPane.showMessageDialog(this, "Buku berhasil dipinjam!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal meminjam buku.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addToWishlist() {
        if (wishlistController.addToWishlist(currentUser.getId(), book.getId())) {
            JOptionPane.showMessageDialog(this, "Berhasil ditambahkan ke wishlist!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Buku ini sudah ada di wishlist Anda.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void rateBook() {
        String[] ratings = {"5 ⭐⭐⭐⭐⭐", "4 ⭐⭐⭐⭐", "3 ⭐⭐⭐", "2 ⭐⭐", "1 ⭐"};
        String selectedRatingStr = (String) JOptionPane.showInputDialog(this, "Berikan rating Anda:", "Beri Rating", JOptionPane.QUESTION_MESSAGE, null, ratings, ratings[0]);

        if (selectedRatingStr != null) {
            int rating = Integer.parseInt(selectedRatingStr.substring(0, 1));
            String reviewText = JOptionPane.showInputDialog(this, "Tulis ulasan singkat Anda (opsional):");

            Review newReview = new Review();
            newReview.setBookId(book.getId());
            newReview.setUserId(currentUser.getId());
            newReview.setRating(rating);
            newReview.setReviewText(reviewText);

            if (reviewController.addReview(newReview)) {
                JOptionPane.showMessageDialog(this, "Terima kasih atas ulasan Anda!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadReviews();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengirim ulasan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadReviews() {
        reviewsDisplayPanel.removeAll();
        List<Review> reviews = reviewController.getReviewsByBookId(book.getId());

        if (reviews.isEmpty()) {
            reviewsDisplayPanel.add(new JLabel("  Belum ada ulasan untuk buku ini."));
        } else {
            for (Review review : reviews) {
                JPanel reviewCard = new JPanel(new BorderLayout(10, 2));
                reviewCard.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));

                String ratingStars = "⭐".repeat(review.getRating());
                String reviewHeader = String.format("<html><b>%s</b> <font color='gray'>(%s)</font></html>", review.getUserFullName(), ratingStars);

                reviewCard.add(new JLabel(reviewHeader), BorderLayout.NORTH);

                JTextArea reviewTextArea = new JTextArea(review.getReviewText());
                reviewTextArea.setLineWrap(true);
                reviewTextArea.setWrapStyleWord(true);
                reviewTextArea.setEditable(false);
                reviewTextArea.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                reviewTextArea.setOpaque(false);

                reviewCard.add(reviewTextArea, BorderLayout.CENTER);
                reviewsDisplayPanel.add(reviewCard);
            }
        }
        reviewsDisplayPanel.revalidate();
        reviewsDisplayPanel.repaint();
    }
}