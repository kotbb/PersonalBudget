package com.budgetapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    
    public static Date parseDate(String dateString) {
        try {
            return DATE_FORMAT.parse(dateString);
        } catch (ParseException e) {
            System.out.println("Error parsing date. Using current date.");
            return new Date();
        }
    }
    
    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }
    
    public static Date getCurrentDate() {
        return new Date();
    }
    
    public static boolean isSameMonth(Date date1, Date date2) {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM/yyyy");
        return monthFormat.format(date1).equals(monthFormat.format(date2));
    }
    
    public static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat dayFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dayFormat.format(date1).equals(dayFormat.format(date2));
    }
    
    public static Date addDays(Date date, int days) {
        return new Date(date.getTime() + (long) days * 24 * 60 * 60 * 1000);
    }
}
