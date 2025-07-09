package com.literanusa.dao;

import com.literanusa.model.Loan;
import com.literanusa.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {
    private final Connection connection;

    public LoanDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Membuat data pinjaman baru dan mengurangi stok buku yang tersedia.
     * @param loan Objek Loan yang akan dibuat.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean createLoan(Loan loan) {
        String sql = "INSERT INTO loans (user_id, book_id, loan_date, due_date, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, loan.getUserId());
            stmt.setInt(2, loan.getBookId());
            stmt.setDate(3, Date.valueOf(loan.getLoanDate()));
            stmt.setDate(4, Date.valueOf(loan.getDueDate()));
            stmt.setString(5, loan.getStatus().toString());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Jika pinjaman berhasil, kurangi stok buku (-1)
                updateBookAvailability(loan.getBookId(), -1);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Memperbarui status pinjaman menjadi 'RETURNED' dan menambah stok buku.
     * @param loanId ID dari pinjaman yang akan dikembalikan.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean returnBook(int loanId) {
        String sql = "UPDATE loans SET return_date = ?, status = 'RETURNED' WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setInt(2, loanId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                Loan loan = getLoanById(loanId);
                if (loan != null) {
                    // Tambah stok buku (+1) saat buku dikembalikan
                    updateBookAvailability(loan.getBookId(), 1);
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Mengubah jumlah buku yang tersedia (menambah atau mengurangi stok).
     * @param bookId ID buku yang akan diubah stoknya.
     * @param change Jumlah perubahan (+1 untuk menambah, -1 untuk mengurangi).
     * @throws SQLException jika terjadi error pada database.
     */
    private void updateBookAvailability(int bookId, int change) throws SQLException {
        String sql = "UPDATE books SET available_copies = available_copies + ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, change);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        }
    }

    /**
     * Mendapatkan semua pinjaman yang dilakukan oleh seorang pengguna.
     * @param userId ID pengguna.
     * @return List objek Loan.
     */
    public List<Loan> getLoansByUserId(int userId) {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE user_id = ? ORDER BY loan_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    /**
     * Mendapatkan semua data pinjaman dari database.
     * @return List semua objek Loan.
     */
    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans ORDER BY loan_date DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    /**
     * Mendapatkan satu data pinjaman berdasarkan ID-nya.
     * @param id ID pinjaman.
     * @return Objek Loan jika ditemukan, null jika tidak.
     */
    private Loan getLoanById(int id) {
        String sql = "SELECT * FROM loans WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToLoan(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Memeriksa apakah sebuah buku sedang aktif dipinjam oleh pengguna tertentu.
     * @param bookId ID buku yang akan diperiksa.
     * @param userId ID pengguna.
     * @return true jika ada pinjaman aktif, false jika tidak.
     */
    public boolean isBookCurrentlyLoanedByUser(int bookId, int userId) {
        String sql = "SELECT 1 FROM loans WHERE book_id = ? AND user_id = ? AND status = 'ACTIVE' LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Mengembalikan true jika ditemukan data
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Helper method untuk memetakan baris ResultSet ke objek Loan.
     * @param rs ResultSet dari query database.
     * @return Objek Loan yang sudah diisi data.
     * @throws SQLException jika terjadi error saat membaca data.
     */
    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        Loan loan = new Loan();
        loan.setId(rs.getInt("id"));
        loan.setUserId(rs.getInt("user_id"));
        loan.setBookId(rs.getInt("book_id"));
        loan.setLoanDate(rs.getDate("loan_date").toLocalDate());
        loan.setDueDate(rs.getDate("due_date").toLocalDate());

        Date returnDate = rs.getDate("return_date");
        if (returnDate != null) {
            loan.setReturnDate(returnDate.toLocalDate());
        }

        loan.setStatus(Loan.Status.valueOf(rs.getString("status")));
        return loan;
    }
}