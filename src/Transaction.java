package com.investwise;
import java.util.Date;
public class Transaction {
    private String type;  
    private double amount;
    private String currency;
    private Date date;
    private String description;
    private PaymentMethod sourceMethod;
    private PaymentMethod destinationMethod;
    public Transaction(String type, double amount, String currency, Date date, String description,
                      PaymentMethod sourceMethod, PaymentMethod destinationMethod) {
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.date = date;
        this.description = description;
        this.sourceMethod = sourceMethod;
        this.destinationMethod = destinationMethod;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public PaymentMethod getSourceMethod() {
        return sourceMethod;
    }
    public void setSourceMethod(PaymentMethod sourceMethod) {
        this.sourceMethod = sourceMethod;
    }
    public PaymentMethod getDestinationMethod() {
        return destinationMethod;
    }
    public void setDestinationMethod(PaymentMethod destinationMethod) {
        this.destinationMethod = destinationMethod;
    }
    @Override
    public String toString() {
        return "Transaction [type=" + type + ", amount=" + amount + " " + currency + 
                ", date=" + date + ", from=" + sourceMethod.getName() + 
                ", to=" + destinationMethod.getName() + "]";
    }
}
