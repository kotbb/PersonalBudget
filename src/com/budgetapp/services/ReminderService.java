package com.budgetapp.services;

import com.budgetapp.models.User;

import java.io.Serializable;
import java.util.*;

public class ReminderService implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public void setReminder(User user, String title, String description, Date reminderDate) {
        // Store the reminder with title and date
        user.addReminder(title + ": " + description, reminderDate);
        System.out.println("Reminder set for " + title);
    }
    
    public void checkReminders(User user) {
        Map<String, Date> reminders = user.getReminders();
        Date now = new Date();
        
        System.out.println("\nChecking reminders...");
        
        boolean hasUpcomingReminders = false;
        
        for (Map.Entry<String, Date> entry : reminders.entrySet()) {
            String reminderTitle = entry.getKey();
            Date reminderDate = entry.getValue();
            
            // If the reminder date is today or in the past
            if (reminderDate.before(new Date(now.getTime() + 24 * 60 * 60 * 1000))) {
                System.out.println("REMINDER: " + reminderTitle + 
                        " - Due " + (reminderDate.before(now) ? "NOW!" : "TODAY!"));
                hasUpcomingReminders = true;
            }
            // If the reminder is coming up in the next 3 days
            else if (reminderDate.before(new Date(now.getTime() + 3 * 24 * 60 * 60 * 1000))) {
                long diffInMillies = reminderDate.getTime() - now.getTime();
                long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
                
                System.out.println("UPCOMING: " + reminderTitle + 
                        " - Due in " + diffInDays + " days");
                hasUpcomingReminders = true;
            }
        }
        
        if (!hasUpcomingReminders) {
            System.out.println("No upcoming reminders in the next 3 days.");
        }
    }
}
