package com.budgetapp.exceptions;

public class BudgetExceededException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public BudgetExceededException(String message) {
        super(message);
    }
    
    public BudgetExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
