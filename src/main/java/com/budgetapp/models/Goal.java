package com.budgetapp.models;

import java.io.Serializable;
import java.util.Date;

public class Goal implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String goalName;
    private double targetAmount;
    private Date targetDate;
    
    public Goal(String goalName, double targetAmount, Date targetDate) {
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.targetDate = targetDate;
    }
    
    // Calculate progress based on current amount
    public String trackGoalProgress(double currentAmount) {
        double progressPercentage = (currentAmount / targetAmount) * 100;
        
        if (progressPercentage >= 100) {
            return "Congratulations! Goal '" + goalName + "' has been reached!";
        } else {
            return "Progress towards '" + goalName + "': " + 
                    String.format("%.1f", progressPercentage) + "% (" + 
                    String.format("$%.2f", currentAmount) + " of " + 
                    String.format("$%.2f", targetAmount) + ")";
        }
    }
    
    // Calculate days left until target date
    public long daysLeft() {
        Date now = new Date();
        long diffInMillies = targetDate.getTime() - now.getTime();
        return diffInMillies / (24 * 60 * 60 * 1000);
    }
    
    // Getters and setters
    public String getGoalName() {
        return goalName;
    }
    
    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }
    
    public double getTargetAmount() {
        return targetAmount;
    }
    
    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }
    
    public Date getTargetDate() {
        return targetDate;
    }
    
    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }
    
    @Override
    public String toString() {
        return "Goal{" +
                "goalName='" + goalName + '\'' +
                ", targetAmount=" + targetAmount +
                ", targetDate=" + targetDate +
                '}';
    }
}
