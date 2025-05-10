package com.budgetapp.models;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double amount;
    private String type; // debit or credit
    private PaymentMethod paymentMethod;
    private Date date;
    
    public Transaction(double amount, String type, PaymentMethod paymentMethod, Date date) {
        this.amount = amount;
        this.type = type;
        this.paymentMethod = paymentMethod;
        this.date = date;
    }
    
    // Static factory method for recording transactions
    public static Transaction recordTransaction(double amount, String type, PaymentMethod paymentMethod, Date date) {
        return new Transaction(amount, type, paymentMethod, date);
    }
    
    // Getters and setters
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", type='" + type + '\'' +
                ", paymentMethod=" + (paymentMethod != null ? paymentMethod.getType() : "none") +
                ", date=" + date +
                '}';
    }
}
