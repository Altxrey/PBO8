package com.literanusa.controller;

import com.literanusa.dao.WishlistDAO;
import com.literanusa.model.Book;
import java.util.List;

public class WishlistController {
    private WishlistDAO wishlistDAO;

    public WishlistController() {
        this.wishlistDAO = new WishlistDAO();
    }

    public boolean addToWishlist(int userId, int bookId) {
        return wishlistDAO.addToWishlist(userId, bookId);
    }

    // **FITUR BARU: Metode untuk menghapus buku dari wishlist**
    public boolean removeFromWishlist(int userId, int bookId) {
        return wishlistDAO.removeFromWishlist(userId, bookId);
    }

    public List<Book> getWishlistForUser(int userId) {
        return wishlistDAO.getWishlistByUserId(userId);
    }
}