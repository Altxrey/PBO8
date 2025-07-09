package com.literanusa.dao;

import com.literanusa.model.Review;
import com.literanusa.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {
    private Connection connection;

    public ReviewDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public boolean addReview(Review review) {
        // **PERBAIKAN: Menggunakan nama kolom yang benar 'review_text'**
        String sql = "INSERT INTO reviews (book_id, user_id, rating, review_text) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, review.getBookId());
            stmt.setInt(2, review.getUserId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getReviewText()); // Menggunakan getter yang benar
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Review> getReviewsByBookId(int bookId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name FROM reviews r JOIN users u ON r.user_id = u.id WHERE r.book_id = ? ORDER BY r.review_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Review review = new Review();
                review.setId(rs.getInt("id"));
                review.setBookId(rs.getInt("book_id"));
                review.setUserId(rs.getInt("user_id"));
                review.setRating(rs.getInt("rating"));
                review.setReviewText(rs.getString("review_text"));

                Timestamp reviewTimestamp = rs.getTimestamp("review_date");
                if (reviewTimestamp != null) {
                    review.setReviewDate(reviewTimestamp.toLocalDateTime());
                }

                review.setUserFullName(rs.getString("full_name"));
                reviews.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}