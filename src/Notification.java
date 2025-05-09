package com.investwise;
import java.util.Date;
public class Notification {
    private String type;  
    private String message;
    private Date date;
    private boolean read;
    private String category;  
    private String actionRequired;  
    public Notification(String type, String message, String category, String actionRequired) {
        this.type = type;
        this.message = message;
        this.date = new Date();
        this.read = false;
        this.category = category;
        this.actionRequired = actionRequired;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public boolean isRead() {
        return read;
    }
    public void setRead(boolean read) {
        this.read = read;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getActionRequired() {
        return actionRequired;
    }
    public void setActionRequired(String actionRequired) {
        this.actionRequired = actionRequired;
    }
    public void markAsRead() {
        this.read = true;
    }
    public static Notification createBudgetAlert(String category, double budgetAmount, double spentAmount) {
        String message = "You have exceeded your " + category + " budget of " + budgetAmount + 
                        ". You have spent " + spentAmount + " so far.";
        String action = "Review your " + category + " expenses and adjust your spending.";
        return new Notification("Alert", message, "Budget", action);
    }
    public static Notification createGoalCompletionNotification(String goalName) {
        String message = "Congratulations! You have reached your goal: " + goalName;
        return new Notification("Achievement", message, "Goal", "Set a new financial goal.");
    }
    public static Notification createZakatReminder(double zakatAmount) {
        String message = "Reminder: Your calculated zakat amount is " + zakatAmount;
        String action = "Review your zakat report and plan for payment.";
        return new Notification("Reminder", message, "Zakat", action);
    }
    @Override
    public String toString() {
        String readStatus = read ? "Read" : "Unread";
        return "[" + readStatus + "] " + type + ": " + message + 
                " (" + date + ") - Category: " + category;
    }
}
