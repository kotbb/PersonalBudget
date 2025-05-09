package com.investwise;
import java.util.Date;
public class User {
    private String username;
    private String password;
    private String email;
    private Date registrationDate;
    private String currency;  
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.registrationDate = new Date();
        this.currency = "USD";  
    }
    public User(String username, String password, String email, Date registrationDate, String currency) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.registrationDate = registrationDate;
        this.currency = currency;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Date getRegistrationDate() {
        return registrationDate;
    }
    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }
    @Override
    public String toString() {
        return "User [username=" + username + ", email=" + email + ", registrationDate=" + registrationDate + "]";
    }
}
