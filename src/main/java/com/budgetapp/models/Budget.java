package com.budgetapp.models;

import java.io.Serializable;
import com.budgetapp.exceptions.BudgetExceededException;

public class Budget implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String category;
    private double limitAmount;
    private double spentAmount;
    
    public Budget(String category, double limitAmount, double spentAmount) {
        this.category = category;
        this.limitAmount = limitAmount;
        this.spentAmount = spentAmount;
    }
    
    // Add to spent amount
    public void addSpent(double amount) {
        this.spentAmount += amount;
    }
    
    // Check if budget is exceeded
    public boolean checkBudget() {
        return spentAmount <= limitAmount;
    }
    
    // Calculate percentage of budget used
    public double calculateUsagePercentage() {
        if (limitAmount == 0) {
            return 0;
        }
        return (spentAmount / limitAmount) * 100;
    }
    
    public void resetAll() {
        this.spentAmount = 0.0;
    }
    
    public void reduceLimit() throws BudgetExceededException {
        double percentUsed = calculateUsagePercentage();
        
        if (percentUsed > 100) {
            throw new BudgetExceededException("Budget for " + category + " has been exceeded! (" +
                    String.format("%.1f", percentUsed) + "% used)");
        }
        
        if (percentUsed >= 90) {
            double newLimit = (spentAmount * 100) / 89.0;
            this.limitAmount = newLimit;
            System.out.println("Budget limit for " + category + " has been increased to $" + 
                    String.format("%.2f", newLimit) + " to keep usage below 90%");
        }
    }
    
    public String analyzeSpending() {
        double percentUsed = calculateUsagePercentage();
        
        if (percentUsed >= 100) {
            return "ALERT: Budget for " + category + " has been exceeded! (" + 
                    String.format("%.1f", percentUsed) + "% used)";
        } else if (percentUsed >= 90) {
            return "WARNING: Budget for " + category + " is nearly exceeded! (" + 
                    String.format("%.1f", percentUsed) + "% used)";
        } else if (percentUsed >= 75) {
            return "CAUTION: Budget for " + category + " is being consumed quickly! (" + 
                    String.format("%.1f", percentUsed) + "% used)";
        } else {
            return "Budget for " + category + " is on track. (" + 
                    String.format("%.1f", percentUsed) + "% used)";
        }
    }
    
    // Getters and setters
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public double getLimitAmount() {
        return limitAmount;
    }
    
    public void setLimitAmount(double limitAmount) {
        this.limitAmount = limitAmount;
    }
    
    public double getSpentAmount() {
        return spentAmount;
    }
    
    public void setSpentAmount(double spentAmount) {
        this.spentAmount = spentAmount;
    }
    
    @Override
    public String toString() {
        return "Budget{" +
                "category='" + category + '\'' +
                ", limitAmount=" + limitAmount +
                ", spentAmount=" + spentAmount +
                ", usagePercentage=" + String.format("%.1f", calculateUsagePercentage()) + "%" +
                '}';
    }
}
