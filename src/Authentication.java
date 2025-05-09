package com.investwise;
import java.util.HashMap;
import java.util.Map;
public class Authentication {
    private PersistenceLayer persistenceLayer;
    public Authentication() {
        this.persistenceLayer = new PersistenceLayer();
    }
    public User registerUser(String username, String password, String email) throws AppException {
        if (username == null || username.trim().isEmpty()) {
            throw new AppException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new AppException("Password cannot be empty");
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new AppException("Please provide a valid email address");
        }
        Map<String, User> users = persistenceLayer.getAllUsers();
        if (users.containsKey(username)) {
            throw new AppException("Username already exists. Please choose a different one.");
        }
        User newUser = new User(username, password, email);
        return newUser;
    }
    public User loginUser(String username, String password) throws AppException {
        if (username == null || username.trim().isEmpty()) {
            throw new AppException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new AppException("Password cannot be empty");
        }
        Map<String, User> users = persistenceLayer.getAllUsers();
        User user = users.get(username);
        if (user == null) {
            throw new AppException("User not found. Please check your username.");
        }
        if (!user.validatePassword(password)) {
            throw new AppException("Incorrect password. Please try again.");
        }
        return user;
    }
    public void changePassword(String username, String oldPassword, String newPassword) throws AppException {
        User user = loginUser(username, oldPassword);
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new AppException("New password cannot be empty");
        }
        user.setPassword(newPassword);
        persistenceLayer.updateUser(user);
    }
}
