package com.literanusa.controller;

import com.literanusa.dao.BookDAO;
import com.literanusa.factory.DAOFactory;
import com.literanusa.model.Book;
import java.util.List;

public class BookController {
    private BookDAO bookDAO;

    public BookController() {
        this.bookDAO = DAOFactory.getInstance().getBookDAO();
    }

    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    public List<Book> searchBooks(String keyword) {
        return bookDAO.searchBooks(keyword);
    }

    public boolean addBook(Book book) {
        return bookDAO.addBook(book);
    }

    public boolean updateBook(Book book) {
        return bookDAO.updateBook(book);
    }

    public Book getBookById(int id) {
        return bookDAO.getBookById(id);
    }
}
