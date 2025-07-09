package com.literanusa.dao;

import com.literanusa.model.Book;
import com.literanusa.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WishlistDAO {
    private Connection connection;

    public WishlistDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public boolean addToWishlist(int userId, int bookId) {
        String sql = "INSERT IGNORE INTO wishlist (user_id, book_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // **FITUR BARU: Metode untuk menghapus buku dari wishlist**
    public boolean removeFromWishlist(int userId, int bookId) {
        String sql = "DELETE FROM wishlist WHERE user_id = ? AND book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Book> getWishlistByUserId(int userId) {
        List<Book> wishlistBooks = new ArrayList<>();
        String sql = "SELECT b.* FROM books b JOIN wishlist w ON b.id = w.book_id WHERE w.user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setGenre(rs.getString("genre"));
                book.setRating(rs.getDouble("rating"));
                book.setCoverImage(rs.getString("cover_image"));
                wishlistBooks.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishlistBooks;
    }
}