package com.budgetapp.models;

import java.io.Serializable;
import java.util.Date;

public class Expense implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double amount;
    private String category;
    private Date date;
    private boolean isRecurring;
    
    public Expense(double amount, String category, Date date, boolean isRecurring) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.isRecurring = isRecurring;
    }
    
    // Static factory method for creating Expense instances
    public static Expense addExpense(double amount, String category, Date date, boolean isRecurring) {
        return new Expense(amount, category, date, isRecurring);
    }
    
    // Getters and setters
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public boolean isRecurring() {
        return isRecurring;
    }
    
    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }
    
    @Override
    public String toString() {
        return "Expense{" +
                "amount=" + amount +
                ", category='" + category + '\'' +
                ", date=" + date +
                ", isRecurring=" + isRecurring +
                '}';
    }
}
