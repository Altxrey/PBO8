package com.literanusa.dao;

import com.literanusa.model.Book;
import com.literanusa.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private final Connection connection;

    public BookDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setIsbn(rs.getString("isbn"));
        book.setGenre(rs.getString("genre"));
        book.setSynopsis(rs.getString("synopsis"));
        book.setRating(rs.getDouble("rating"));
        book.setAvailableCopies(rs.getInt("available_copies"));
        book.setTotalCopies(rs.getInt("total_copies"));
        book.setCoverImage(rs.getString("cover_image"));
        book.setPdfPath(rs.getString("pdf_path"));
        return book;
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY title ASC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToBook(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, genre, synopsis, available_copies, total_copies, cover_image, pdf_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setString(4, book.getGenre());
            stmt.setString(5, book.getSynopsis());
            stmt.setInt(6, book.getAvailableCopies());
            stmt.setInt(7, book.getTotalCopies());
            stmt.setString(8, book.getCoverImage());
            stmt.setString(9, book.getPdfPath());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR genre LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title=?, author=?, isbn=?, genre=?, synopsis=?, available_copies=?, total_copies=?, cover_image=?, pdf_path=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setString(4, book.getGenre());
            stmt.setString(5, book.getSynopsis());
            stmt.setInt(6, book.getAvailableCopies());
            stmt.setInt(7, book.getTotalCopies());
            stmt.setString(8, book.getCoverImage());
            stmt.setString(9, book.getPdfPath());
            stmt.setInt(10, book.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} // **<-- INI PERBAIKANNYA: Kurung kurawal penutup kelas yang hilang**