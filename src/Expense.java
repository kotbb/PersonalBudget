package com.investwise;
import java.util.Date;
public class Expense {
    private String category;
    private double amount;
    private String currency;
    private Date date;
    private String description;
    private PaymentMethod paymentMethod;
    public Expense(String category, double amount, String currency, Date date, String description, PaymentMethod paymentMethod) {
        this.category = category;
        this.amount = amount;
        this.currency = currency;
        this.date = date;
        this.description = description;
        this.paymentMethod = paymentMethod;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
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
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    @Override
    public String toString() {
        return "Expense [category=" + category + ", amount=" + amount + " " + currency + 
                ", date=" + date + ", paymentMethod=" + paymentMethod.getName() + "]";
    }
}
