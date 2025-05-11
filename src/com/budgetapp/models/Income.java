package com.budgetapp.models;

import java.io.Serializable;
import java.util.Date;

public class Income implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String source;
    private double amount;
    private Date date;
    private boolean isRecurring;
    
    public Income(String source, double amount, Date date, boolean isRecurring) {
        this.source = source;
        this.amount = amount;
        this.date = date;
        this.isRecurring = isRecurring;
    }
    
    // Static factory method for creating Income instances
    public static Income addIncome(String source, double amount, Date date, boolean isRecurring) {
        return new Income(source, amount, date, isRecurring);
    }
    
    // Getters and setters
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
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
        return "Income{" +
                "source='" + source + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", isRecurring=" + isRecurring +
                '}';
    }
}
