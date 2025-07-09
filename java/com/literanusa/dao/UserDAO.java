package com.literanusa.dao;

import com.literanusa.model.User;
import com.literanusa.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private final Connection connection;

    public UserDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Mengisi objek User dari ResultSet database dengan aman.
     * @param user Objek User yang akan diisi.
     * @param rs ResultSet yang berisi data dari database.
     * @throws SQLException jika terjadi error saat mengakses data.
     */
    private void populateUserFromResultSet(User user, ResultSet rs) throws SQLException {
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setFullName(rs.getString("full_name"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        user.setProfilePicture(rs.getString("profile_picture"));

        String roleString = rs.getString("role");
        if (roleString != null) {
            try {
                user.setRole(User.Role.valueOf(roleString.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.err.println("Nilai role tidak valid di database: " + roleString + " untuk user ID: " + user.getId());
                user.setRole(User.Role.USER); // Default ke USER jika role tidak valid
            }
        } else {
            user.setRole(User.Role.USER); // Default jika role null
        }
    }

    /**
     * Mengautentikasi pengguna berdasarkan username dan password.
     * @param username Username pengguna.
     * @param password Password pengguna.
     * @return Objek User jika berhasil, null jika gagal.
     */
    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                populateUserFromResultSet(user, rs);
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error saat autentikasi: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mendaftarkan pengguna baru ke database.
     * @param user Objek User yang akan didaftarkan.
     * @return String status: "SUCCESS", "DUPLICATE_USERNAME", "DUPLICATE_EMAIL", atau "ERROR".
     */
    public String register(User user) {
        String sql = "INSERT INTO users (username, password, email, full_name, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFullName());
            stmt.setString(5, user.getRole().toString());

            stmt.executeUpdate();
            return "SUCCESS";
        } catch (SQLException e) {
            // Cek pesan error dari MySQL untuk duplikasi data
            if (e.getMessage().toLowerCase().contains("duplicate entry")) {
                if (e.getMessage().toLowerCase().contains("username")) {
                    return "DUPLICATE_USERNAME";
                } else if (e.getMessage().toLowerCase().contains("email")) {
                    return "DUPLICATE_EMAIL";
                }
            }
            e.printStackTrace();
            return "ERROR";
        }
    }

    /**
     * Memperbarui profil pengguna (email, nama, telepon, alamat).
     * @param user Objek User dengan data yang sudah diperbarui.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET email = ?, full_name = ?, phone = ?, address = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getFullName());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getAddress());
            stmt.setInt(5, user.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Memperbarui password pengguna.
     * @param userId ID pengguna.
     * @param newPassword Password baru.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Memperbarui foto profil pengguna.
     * @param userId ID pengguna.
     * @param profilePicture Nama file foto profil.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean updateProfilePicture(int userId, String profilePicture) {
        String sql = "UPDATE users SET profile_picture = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, profilePicture);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mendapatkan semua pengguna dari database.
     * @return List objek User.
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User user = new User();
                populateUserFromResultSet(user, rs);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Mendapatkan satu pengguna berdasarkan ID.
     * @param id ID pengguna.
     * @return Objek User jika ditemukan, null jika tidak.
     */
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                populateUserFromResultSet(user, rs);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}