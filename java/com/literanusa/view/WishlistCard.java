package com.literanusa.view;

import com.literanusa.controller.WishlistController;
import com.literanusa.model.Book;
import com.literanusa.model.User;
import com.literanusa.util.ImageUtils;

import javax.swing.*;
import java.awt.*;

public class WishlistCard extends AnimatedCard {
    private final Book book;
    private final User currentUser;
    private final UserDashboardView dashboard;

    public WishlistCard(Book book, User currentUser, UserDashboardView dashboard) {
        super();
        this.book = book;
        this.currentUser = currentUser;
        this.dashboard = dashboard;

        setLayout(new BorderLayout(15, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel coverLabel = ImageUtils.createBookCoverLabel(book.getCoverImage(), 60, 90);
        add(coverLabel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(book.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel authorLabel = new JLabel(book.getAuthor());
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        authorLabel.setForeground(Color.GRAY);

        infoPanel.add(titleLabel);
        infoPanel.add(authorLabel);
        add(infoPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);

        JButton removeButton = new JButton("Hapus");
        removeButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Hapus dari wishlist?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                WishlistController controller = new WishlistController();
                if (controller.removeFromWishlist(currentUser.getId(), book.getId())) {
                    dashboard.refreshWishlistPanel();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus dari wishlist.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton detailButton = new JButton("Lihat Detail");
        detailButton.addActionListener(e -> new BookDetailView(book, currentUser).setVisible(true));

        actionPanel.add(removeButton);
        actionPanel.add(detailButton);
        add(actionPanel, BorderLayout.EAST);
    }
}