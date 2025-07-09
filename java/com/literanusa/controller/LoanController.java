package com.literanusa.controller;

import com.literanusa.dao.LoanDAO;
import com.literanusa.factory.DAOFactory;
import com.literanusa.model.Loan;
import java.time.LocalDate;

public class LoanController {
    private LoanDAO loanDAO;

    public LoanController() {
        this.loanDAO = DAOFactory.getInstance().getLoanDAO();
    }

    public boolean createLoan(int userId, int bookId) {
        // **FITUR BARU: Menentukan tenggat waktu 7 hari dari sekarang**
        LocalDate loanDate = LocalDate.now();
        LocalDate dueDate = loanDate.plusDays(7);

        Loan newLoan = new Loan(userId, bookId, loanDate, dueDate);

        return loanDAO.createLoan(newLoan);
    }

    // **FITUR BARU: Metode untuk mengembalikan buku**
    public boolean returnBook(int loanId) {
        return loanDAO.returnBook(loanId);
    }
}