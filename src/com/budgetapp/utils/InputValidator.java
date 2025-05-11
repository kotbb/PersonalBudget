package com.budgetapp.utils;

import java.util.regex.Pattern;

public class InputValidator {
    private static final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    private static final String PHONE_PATTERN = 
        "^\\+?[0-9]{10,15}$";
    
    private static final String CARD_NUMBER_PATTERN = 
        "^[0-9]{16}$";
    
    public static boolean isValidEmail(String email) {
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }
    
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return Pattern.compile(PHONE_PATTERN).matcher(phoneNumber).matches();
    }
    
    public static boolean isValidCardNumber(String cardNumber) {
        // Remove any spaces or dashes
        String cleanedCardNumber = cardNumber.replaceAll("[ -]", "");
        return Pattern.compile(CARD_NUMBER_PATTERN).matcher(cleanedCardNumber).matches();
    }
    
    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }
    
    public static boolean isValidPassword(String password) {
        // Password should be at least 8 characters long
        // and contain at least one digit, one lowercase and one uppercase letter
        if (password.length() < 8) {
            return false;
        }
        
        boolean hasDigit = false;
        boolean hasLower = false;
        boolean hasUpper = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isUpperCase(c)) {
                hasUpper = true;
            }
        }
        
        return hasDigit && hasLower && hasUpper;
    }
}
