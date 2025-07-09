package com.literanusa.controller;

import com.literanusa.dao.UserDAO;
import com.literanusa.factory.DAOFactory;
import com.literanusa.model.User;

public class AuthController {
    private UserDAO userDAO;

    public AuthController() {
        this.userDAO = DAOFactory.getInstance().getUserDAO();
    }

    public User login(String username, String password) {
        return userDAO.authenticate(username, password);
    }

    public String register(String username, String password, String email, String fullName) {
        User user = new User(username, password, email, fullName, User.Role.USER);
        return userDAO.register(user);
    }

    public boolean updateProfile(User user) {
        return userDAO.updateUser(user);
    }

    public boolean changePassword(int userId, String newPassword) {
        return userDAO.updatePassword(userId, newPassword);
    }

    public boolean updateProfilePicture(int userId, String profilePicture) {
        return userDAO.updateProfilePicture(userId, profilePicture);
    }

    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }
}