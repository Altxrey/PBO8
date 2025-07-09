package com.literanusa.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private Role role;
    private LocalDateTime createdAt;

    // **PERBAIKAN: Mengembalikan semua field opsional**
    private String phone;
    private String address;
    private String profilePicture;
    private LocalDate dateOfBirth;
    private Gender gender;

    public enum Role {
        USER, ADMIN
    }

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public User() {}

    public User(String username, String password, String email, String fullName, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }

    // --- Getters and Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // **PERBAIKAN: Getters dan Setters yang hilang dikembalikan**
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    // **PERBAIKAN: Helper method yang dibutuhkan UserProfileView dikembalikan**

    public String getProfilePicturePath() {
        if (profilePicture != null && !profilePicture.isEmpty()) {
            return "/images/profiles/" + profilePicture;
        }
        return "/images/default-avatar.png";
    }

    public boolean hasProfilePicture() {
        return profilePicture != null && !profilePicture.isEmpty();
    }

    public String getDisplayName() {
        return fullName != null && !fullName.isEmpty() ? fullName : username;
    }
}