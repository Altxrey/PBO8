package com.literanusa.controller;

import com.literanusa.dao.ReviewDAO;
import com.literanusa.model.Review;

import java.util.List;

/**
 * Controller ini berfungsi sebagai jembatan antara tampilan (View) dan
 * logika database (DAO) untuk semua hal yang berkaitan dengan ulasan (review).
 */
public class ReviewController {

    private ReviewDAO reviewDAO;

    public ReviewController() {
        // Inisialisasi DAO untuk berinteraksi dengan tabel 'reviews'
        this.reviewDAO = new ReviewDAO();
    }

    /**
     * Menambahkan ulasan baru ke dalam database.
     * @param review Objek Review yang berisi data ulasan baru.
     * @return true jika berhasil disimpan, false jika gagal.
     */
    public boolean addReview(Review review) {
        // Validasi sederhana sebelum mengirim ke DAO
        if (review.getRating() < 1 || review.getRating() > 5) {
            return false; // Rating harus antara 1 dan 5
        }
        return reviewDAO.addReview(review);
    }

    /**
     * Mengambil semua ulasan untuk sebuah buku berdasarkan ID buku.
     * @param bookId ID dari buku yang ulasannya ingin ditampilkan.
     * @return Daftar (List) objek Review.
     */
    public List<Review> getReviewsByBookId(int bookId) {
        return reviewDAO.getReviewsByBookId(bookId);
    }
}