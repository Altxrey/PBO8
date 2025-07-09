package com.literanusa.model;

import java.time.LocalDateTime;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private String synopsis;
    private double rating;
    private int availableCopies;
    private int totalCopies;
    private String coverImage;
    private LocalDateTime createdAt;

    // **PERBAIKAN: Field pdfPath dikembalikan**
    private String pdfPath;

    public Book() {}

    public Book(String title, String author, String isbn, String genre, String synopsis) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.synopsis = synopsis;
        this.rating = 0.0;
        this.availableCopies = 1;
        this.totalCopies = 1;
    }

    // --- Getters and Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getSynopsis() { return synopsis; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // **PERBAIKAN: Getter dan Setter untuk pdfPath dikembalikan**
    public String getPdfPath() { return pdfPath; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }

    // --- Helper Methods ---
    public String getCoverImagePath() {
        if (coverImage != null && !coverImage.isEmpty()) {
            return "/images/covers/" + coverImage;
        }
        return "/images/covers/default-book-cover.jpeg";
    }
}