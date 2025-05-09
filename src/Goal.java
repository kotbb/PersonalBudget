package com.investwise;
import java.util.Date;
public class Goal {
    private String name;
    private double targetAmount;
    private double currentAmount;
    private String currency;
    private Date targetDate;
    private boolean completed;
    private String category;
    public Goal(String name, double targetAmount, double currentAmount, String currency, Date targetDate, String category) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.currency = currency;
        this.targetDate = targetDate;
        this.completed = false;
        this.category = category;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getTargetAmount() {
        return targetAmount;
    }
    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
        this.completed = currentAmount >= targetAmount;
    }
    public double getCurrentAmount() {
        return currentAmount;
    }
    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
        this.completed = currentAmount >= targetAmount;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public Date getTargetDate() {
        return targetDate;
    }
    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }
    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void addFunds(double amount) {
        this.currentAmount += amount;
        this.completed = currentAmount >= targetAmount;
    }
    public double getProgressPercentage() {
        if (targetAmount == 0) {
            return 0;
        }
        return (currentAmount / targetAmount) * 100;
    }
    public double getRemainingAmount() {
        return Math.max(0, targetAmount - currentAmount);
    }
    public boolean isOverdue() {
        return !completed && new Date().after(targetDate);
    }
    @Override
    public String toString() {
        return "Goal [name=" + name + ", progress=" + String.format("%.1f", getProgressPercentage()) + 
                "%, remaining=" + getRemainingAmount() + " " + currency + 
                ", completed=" + completed + ", overdue=" + isOverdue() + "]";
    }
}
