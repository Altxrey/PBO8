package com.literanusa.view;

import com.literanusa.controller.BookController;
import com.literanusa.controller.WishlistController;
import com.literanusa.dao.LoanDAO;
import com.literanusa.factory.DAOFactory;
import com.literanusa.model.Book;
import com.literanusa.model.Loan;
import com.literanusa.model.User;
import com.literanusa.util.WrapLayout;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class UserDashboardView extends JFrame {
    private User currentUser;
    private BookController bookController;
    private LoanDAO loanDAO;
    private WishlistController wishlistController;
    private List<Book> allBooks;
    private List<NavButton> navButtons = new ArrayList<>();

    private CardLayout mainCardLayout;
    private JPanel mainContentPanel;
    private JPanel booksPanel; // Panel khusus untuk katalog
    private JTextField searchField;
    private JComboBox<String> genreFilter;

    // Konstanta untuk nama panel di CardLayout
    private static final String PANEL_KATALOG = "Katalog";
    private static final String PANEL_PEMINJAMAN = "Peminjaman Saya";
    private static final String PANEL_WISHLIST = "Wishlist";
    private static final String PANEL_PROFIL = "Profil Saya";

    // Skema Warna
    private final Color NAV_BAR_COLOR = new Color(39, 55, 77);
    private final Color BACKGROUND_COLOR = new Color(244, 247, 250);

    public UserDashboardView(User user) {
        this.currentUser = user;
        this.bookController = new BookController();
        this.loanDAO = DAOFactory.getInstance().getLoanDAO();
        this.wishlistController = new WishlistController();
        initializeComponents();
        loadBooks();
    }

    private void initializeComponents() {
        setTitle("LiteraNusa Digital Library");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1280, 720));
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);

        mainPanel.add(createSideNavBar(), BorderLayout.WEST);
        mainPanel.add(createMainContentPanel(), BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createSideNavBar() {
        JPanel navBar = new JPanel();
        navBar.setLayout(new BoxLayout(navBar, BoxLayout.Y_AXIS));
        navBar.setBackground(NAV_BAR_COLOR);
        navBar.setPreferredSize(new Dimension(240, 0));
        navBar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel titleLabel = new JLabel("LiteraNusa");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 40, 0));
        navBar.add(titleLabel);

        addNavButton(navBar, "Katalog", true);
        addNavButton(navBar, "Peminjaman Saya", false);
        addNavButton(navBar, "Wishlist", false);
        addNavButton(navBar, "Profil Saya", false);

        navBar.add(Box.createVerticalGlue());

        NavButton logoutButton = new NavButton("Logout", "/images/icons/logout.png");
        logoutButton.addActionListener(e -> {
            this.dispose();
            new LoginView().setVisible(true);
        });
        navBar.add(logoutButton);

        return navBar;
    }

    private void addNavButton(JPanel parent, String text, boolean isActive) {
        String iconPath = "/images/icons/" + text.toLowerCase().replace(" ", "_") + ".png";
        NavButton button = new NavButton(text, iconPath);
        button.addActionListener(e -> {
            setActiveButton(button);
            handleNavigation(text);
        });
        navButtons.add(button);
        parent.add(button);
        if (isActive) {
            button.setActive(true);
        }
    }

    private void setActiveButton(NavButton selectedButton) {
        for (NavButton btn : navButtons) {
            btn.setActive(btn == selectedButton);
        }
    }

    // **PERBAIKAN UTAMA: Logika navigasi yang lebih stabil dan benar**
    private void handleNavigation(String panelName) {
        // Jika yang diklik adalah "Profil Saya", buka jendela baru
        if (PANEL_PROFIL.equals(panelName)) {
            new UserProfileView(currentUser).setVisible(true);
            return; // Hentikan eksekusi agar tidak melanjutkan ke CardLayout
        }

        // Untuk panel lain, gunakan CardLayout
        // Buat panel baru jika belum ada atau jika perlu di-refresh
        if (PANEL_PEMINJAMAN.equals(panelName)) {
            mainContentPanel.add(createPeminjamanPanel(), PANEL_PEMINJAMAN);
        } else if (PANEL_WISHLIST.equals(panelName)) {
            mainContentPanel.add(createWishlistPanel(), PANEL_WISHLIST);
        }

        // Tampilkan panel yang sesuai
        mainCardLayout.show(mainContentPanel, panelName);
    }

    private JPanel createMainContentPanel() {
        mainCardLayout = new CardLayout();
        mainContentPanel = new JPanel(mainCardLayout);

        JPanel catalogPanel = createKatalogPanel();
        catalogPanel.setName(PANEL_KATALOG);
        mainContentPanel.add(catalogPanel, PANEL_KATALOG);

        return mainContentPanel;
    }

    private JPanel createKatalogPanel() {
        JPanel catalogPanel = new JPanel(new BorderLayout(10, 15));
        catalogPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 25, 25));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel headerLabel = new JLabel("Katalog Buku");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headerPanel.add(headerLabel, BorderLayout.WEST);
        catalogPanel.add(headerPanel, BorderLayout.NORTH);

        catalogPanel.add(createFilterAndSearchPanel(), BorderLayout.CENTER);

        booksPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 20, 20));

        JScrollPane scrollPane = new JScrollPane(booksPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        catalogPanel.add(scrollPane, BorderLayout.SOUTH);
        return catalogPanel;
    }

    private JPanel createFilterAndSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { filterAndDisplayBooks(); }
        });

        String[] genres = {"Semua Genre", "Drama", "Fantasi", "Romance", "Thriller", "Sejarah", "Religi"};
        genreFilter = new JComboBox<>(genres);
        genreFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        genreFilter.addActionListener(e -> filterAndDisplayBooks());

        panel.add(new JLabel("Cari:"), BorderLayout.WEST);
        panel.add(searchField, BorderLayout.CENTER);
        panel.add(genreFilter, BorderLayout.EAST);

        return panel;
    }

    private void loadBooks() {
        allBooks = bookController.getAllBooks();
        allBooks.sort(Comparator.comparing(Book::getTitle));
        filterAndDisplayBooks();
    }

    private void filterAndDisplayBooks() {
        if (booksPanel == null) return;
        String searchText = searchField.getText().toLowerCase();
        String selectedGenre = (String) genreFilter.getSelectedItem();

        List<Book> filteredBooks = allBooks.stream()
                .filter(book -> (book.getTitle().toLowerCase().contains(searchText) || book.getAuthor().toLowerCase().contains(searchText)) &&
                        ("Semua Genre".equals(selectedGenre) || (book.getGenre() != null && book.getGenre().equalsIgnoreCase(selectedGenre))))
                .collect(Collectors.toList());

        displayBooksWithAnimation(booksPanel, filteredBooks, false);
    }

    private void displayBooksWithAnimation(JPanel panel, List<Book> books, boolean isLoanCard) {
        panel.removeAll();
        if (books.isEmpty()) {
            panel.add(new JLabel("Tidak ada buku untuk ditampilkan."));
        } else {
            for (int i = 0; i < books.size(); i++) {
                Book book = books.get(i);

                final AnimatedCard card;
                if (isLoanCard) {
                    Loan loan = loanDAO.getLoansByUserId(currentUser.getId()).stream()
                            .filter(l -> l.getBookId() == book.getId() && l.getStatus() == Loan.Status.ACTIVE)
                            .findFirst().orElse(null);
                    card = new LoanCard(loan, book, currentUser, this);
                } else {
                    card = new BookCard(book, currentUser);
                }

                panel.add(card);

                Animator animator = new Animator.Builder()
                        .setDuration(500, TimeUnit.MILLISECONDS)
                        .setStartDelay(i * 50L, TimeUnit.MILLISECONDS)
                        .addTarget(new TimingTargetAdapter() {
                            @Override
                            public void timingEvent(Animator source, double fraction) {
                                card.setOpacity((float) fraction);
                            }
                        }).build();
                animator.start();
            }
        }
        panel.revalidate();
        panel.repaint();
    }

    private JPanel createPeminjamanPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setName(PANEL_PEMINJAMAN);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 25, 25));

        JLabel headerLabel = new JLabel("Buku Pinjaman Saya");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        panel.add(headerLabel, BorderLayout.NORTH);

        JPanel loanBooksPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 20, 20));

        List<Loan> userLoans = loanDAO.getLoansByUserId(currentUser.getId());
        List<Book> borrowedBooks = userLoans.stream()
                .filter(loan -> loan.getStatus() == Loan.Status.ACTIVE)
                .map(loan -> bookController.getBookById(loan.getBookId()))
                .collect(Collectors.toList());

        displayBooksInPanel(loanBooksPanel, borrowedBooks, true);

        panel.add(new JScrollPane(loanBooksPanel), BorderLayout.CENTER);
        return panel;
    }

    private void displayBooksInPanel(JPanel loanBooksPanel, List<Book> borrowedBooks, boolean b) {
    }

    private JPanel createWishlistPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setName(PANEL_WISHLIST);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 25, 25));

        JLabel headerLabel = new JLabel("Wishlist Saya");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        panel.add(headerLabel, BorderLayout.NORTH);

        JPanel wishlistContentPanel = new JPanel();
        wishlistContentPanel.setLayout(new BoxLayout(wishlistContentPanel, BoxLayout.Y_AXIS));

        List<Book> wishlistBooks = wishlistController.getWishlistForUser(currentUser.getId());

        if (wishlistBooks.isEmpty()) {
            wishlistContentPanel.add(new JLabel("Wishlist Anda masih kosong."));
        } else {
            for (Book book : wishlistBooks) {
                wishlistContentPanel.add(new WishlistCard(book, currentUser, this));
            }
        }

        panel.add(new JScrollPane(wishlistContentPanel), BorderLayout.CENTER);
        return panel;
    }

    public void refreshWishlistPanel() {
        refreshAndShowPanel(PANEL_WISHLIST);
    }

    private void refreshAndShowPanel(String panelWishlist) {
    }

    public void refreshPeminjamanPanel() {
        refreshAndShowPanel(PANEL_PEMINJAMAN);
    }
}