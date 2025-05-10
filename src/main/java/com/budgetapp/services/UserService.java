package com.budgetapp.services;

import com.budgetapp.exceptions.AuthenticationException;
import com.budgetapp.models.User;

public class UserService {
    private PersistenceManager persistenceManager;
    
    public UserService(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }
    
    public User signUp(String username, String email, String password, String phoneNumber) throws AuthenticationException {
        // Validate inputs
        if (username == null || username.trim().isEmpty()) {
            throw new AuthenticationException("Username cannot be empty");
        }
        
        if (email == null || email.trim().isEmpty()) {
            throw new AuthenticationException("Email cannot be empty");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new AuthenticationException("Password cannot be empty");
        }
        
        // Check if username or email already exists
        if (persistenceManager.findUserByUsername(username) != null) {
            throw new AuthenticationException("Username already exists");
        }
        
        if (persistenceManager.findUserByEmail(email) != null) {
            throw new AuthenticationException("Email already exists");
        }
        
        // Create and register new user
        User newUser = new User(username, email, password, phoneNumber);
        newUser.signUp(); // Set login status to true
        persistenceManager.addUser(newUser);
        
        try {
            persistenceManager.saveDataToFile();
        } catch (Exception e) {
            throw new AuthenticationException("Failed to save user data: " + e.getMessage());
        }
        
        return newUser;
    }
    
    public User login(String usernameOrEmail, String password) throws AuthenticationException {
        // Find user by username or email
        User user = persistenceManager.findUserByUsername(usernameOrEmail);
        
        if (user == null) {
            user = persistenceManager.findUserByEmail(usernameOrEmail);
        }
        
        if (user == null) {
            throw new AuthenticationException("User not found");
        }
        
        // Authenticate
        if (!user.authenticate(password)) {
            throw new AuthenticationException("Invalid password");
        }
        
        // Login
        user.login(password);
        return user;
    }
}
