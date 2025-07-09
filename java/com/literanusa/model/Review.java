package com.literanusa.model;

import java.time.LocalDateTime;

public class Review {
    private int id;
    private int bookId;
    private int userId;
    private int rating;
    private String reviewText;
    private LocalDateTime reviewDate;
    private String userFullName; // Untuk menampilkan nama pemberi ulasan

    public int getRating() {
        return 0;
    }

    public int getBookId() {
        return 0;
    }

    public int getUserId() {
        return 0;
    }

    public String getReviewText() {
        return "";
    }

    public void setId(int id) {
    }

    public void getBookId(int bookId) {
    }

    public void getUserId(int userId) {
    }

    public void setRating(int rating) {
    }

    public void setReviewText(String reviewText) {
    }

    public void setReviewDate(LocalDateTime reviewDate) {
    }

    public void setUserFullName(String fullName) {
    }

    public void setBookId(int bookId) {
    }

    public void setUserId(int userId) {
    }

    public Object getUserFullName() {
        return null;
    }

    // Getters and Setters untuk semua field di atas...
    // (Anda bisa membuatnya dengan cepat di IDE Anda)
}