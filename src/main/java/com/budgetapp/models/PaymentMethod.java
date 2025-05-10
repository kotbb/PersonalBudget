package com.budgetapp.models;

import java.io.Serializable;

public class PaymentMethod implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String cardNumber;
    private double balance;
    private String type; // Credit, Debit, etc.
    
    public PaymentMethod(String cardNumber, double balance, String type) {
        this.cardNumber = cardNumber;
        this.balance = balance;
        this.type = type;
    }
    
    // Static factory method for linking a card
    public static PaymentMethod linkCard(String cardNumber, double balance, String type) {
        return new PaymentMethod(cardNumber, balance, type);
    }
    
    // Get current balance
    public double getBalance() {
        return balance;
    }
    
    // Update balance
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    // Getters and setters
    public String getCardNumber() {
        return cardNumber;
    }
    
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return "PaymentMethod{" +
                "cardNumber='" + cardNumber + '\'' +
                ", balance=" + balance +
                ", type='" + type + '\'' +
                '}';
    }
}
