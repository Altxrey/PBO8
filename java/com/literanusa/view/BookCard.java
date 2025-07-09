package com.literanusa.view;

import com.literanusa.model.Book;
import com.literanusa.model.User;
import com.literanusa.util.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BookCard extends AnimatedCard {
    private final Book book;
    private final User currentUser;

    public BookCard(Book book, User currentUser) {
        super();

        // **PERBAIKAN: Menginisialisasi variabel final**
        this.book = book;
        this.currentUser = currentUser;

        setLayout(new BorderLayout(0, 8));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        setPreferredSize(new Dimension(180, 280));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setToolTipText("Klik untuk melihat detail " + book.getTitle());

        JLabel coverLabel = ImageUtils.createBookCoverLabel(book.getCoverImage(), 180, 220);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("<html><body style='width: 110px'><b>" + book.getTitle() + "</b></body></html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel authorLabel = new JLabel(book.getAuthor());
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        authorLabel.setForeground(new Color(108, 117, 125));

        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        textPanel.add(authorLabel);

        JLabel ratingLabel = new JLabel(String.format("⭐ %.1f", book.getRating()));
        ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        infoPanel.add(textPanel, BorderLayout.CENTER);
        infoPanel.add(ratingLabel, BorderLayout.EAST);

        add(coverLabel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new BookDetailView(book, currentUser).setVisible(true);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(new Color(95, 158, 160), 2));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
            }
        });
    }
}