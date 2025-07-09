package com.literanusa.model;

import java.time.LocalDate;

public class Loan {
    private int id;
    private int userId;
    private int bookId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Status status;

    public enum Status {
        ACTIVE, RETURNED, OVERDUE
    }

    // Constructors
    public Loan() {}

    public Loan(int userId, int bookId, LocalDate loanDate, LocalDate dueDate) {
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.status = Status.ACTIVE;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public LocalDate getLoanDate() { return loanDate; }
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
