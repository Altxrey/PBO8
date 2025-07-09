package com.literanusa.factory;

import com.literanusa.dao.BookDAO;
import com.literanusa.dao.LoanDAO;
import com.literanusa.dao.UserDAO;

public class DAOFactory {
    private static DAOFactory instance;

    private DAOFactory() {}

    public static DAOFactory getInstance() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }

    public UserDAO getUserDAO() {
        return new UserDAO();
    }

    public BookDAO getBookDAO() {
        return new BookDAO();
    }

    public LoanDAO getLoanDAO() {
        return new LoanDAO();
    }
}
