package com.budgetapp.services;

import com.budgetapp.exceptions.PersistenceException;
import com.budgetapp.models.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenceManager {
    private String filePath;
    private List<User> users;
    
    public PersistenceManager(String filePath) {
        this.filePath = filePath;
        this.users = new ArrayList<>();
    }
    
    public void saveDataToFile() throws PersistenceException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(users);
            System.out.println("Data saved successfully to " + filePath);
        } catch (IOException e) {
            throw new PersistenceException("Error saving data: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public void loadDataFromFile() throws PersistenceException {
        File file = new File(filePath);
        
        if (!file.exists()) {
            users = new ArrayList<>();
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            Object readObject = ois.readObject();
            
            if (readObject instanceof List<?>) {
                users = (List<User>) readObject;
                System.out.println("Loaded " + users.size() + " users from " + filePath);
            } else {
                throw new PersistenceException("Invalid data format in file");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenceException("Error loading data: " + e.getMessage());
        }
    }
    
    public List<User> getUsers() {
        return users;
    }
    
    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    
    public User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
    
    public void addUser(User user) {
        users.add(user);
    }
}
