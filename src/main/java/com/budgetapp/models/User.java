package com.budgetapp.models;

import com.budgetapp.exceptions.AuthenticationException;
import com.budgetapp.exceptions.BudgetExceededException;

import java.io.Serializable;
import java.util.*;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private boolean isLoggedIn;
    
    private List<Income> incomes;
    private List<Expense> expenses;
    private List<Budget> budgets;
    private List<Goal> goals;
    private List<PaymentMethod> paymentMethods;
    private List<Transaction> transactions;
    private Map<String, Date> reminders;
    
    public User(String username, String email, String password, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isLoggedIn = false;
        
        this.incomes = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.budgets = new ArrayList<>();
        this.goals = new ArrayList<>();
        this.paymentMethods = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.reminders = new HashMap<>();
    }
    
    // User authentication methods
    public boolean signUp() throws AuthenticationException {
        if (username == null || username.isEmpty() || 
            email == null || email.isEmpty() || 
            password == null || password.isEmpty()) {
            throw new AuthenticationException("Username, email and password are required");
        }
        
        // Additional validation could be added here
        this.isLoggedIn = true;
        return true;
    }
    
    public boolean login(String password) throws AuthenticationException {
        if (this.password.equals(password)) {
            this.isLoggedIn = true;
            return true;
        } else {
            throw new AuthenticationException("Invalid password");
        }
    }
    
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
    
    public void logout() {
        this.isLoggedIn = false;
    }
    
    // Income methods
    public void addIncome(Income income) {
        incomes.add(income);
    }
    
    public List<Income> getIncomes() {
        return Collections.unmodifiableList(incomes);
    }
    
    // Expense methods
    public void addExpense(Expense expense) {
        expenses.add(expense);
        
        // Update budget spent amounts
        for (Budget budget : budgets) {
            if (budget.getCategory().equalsIgnoreCase(expense.getCategory())) {
                budget.addSpent(expense.getAmount());
                break;
            }
        }
    }
    
    public List<Expense> getExpenses() {
        return Collections.unmodifiableList(expenses);
    }
    
    // Budget methods
    public void setBudget(Budget budget) {
        // Check if budget for this category already exists
        for (int i = 0; i < budgets.size(); i++) {
            if (budgets.get(i).getCategory().equalsIgnoreCase(budget.getCategory())) {
                budgets.set(i, budget); // Replace existing budget
                return;
            }
        }
        // If not, add new budget
        budgets.add(budget);
    }
    
    public void checkBudgetLimits(String category, double amount) throws BudgetExceededException {
        for (Budget budget : budgets) {
            if (budget.getCategory().equalsIgnoreCase(category)) {
                double newTotal = budget.getSpentAmount() + amount;
                if (newTotal > budget.getLimitAmount()) {
                    // We still add the expense but throw an exception to notify the user
                    budget.setSpentAmount(newTotal); // Update the spent amount anyway
                    throw new BudgetExceededException("This expense exceeds your budget for " + 
                            category + "! Budget: $" + budget.getLimitAmount());

                }
            }
        }
    }
    
    public List<Budget> getBudgets() {
        return Collections.unmodifiableList(budgets);
    }
    
    public void updateBudgetSpentAmounts() {
        // Reset all spent amounts
        for (Budget budget : budgets) {
            budget.setSpentAmount(0.0);
        }
        
        // Recalculate based on expenses
        for (Expense expense : expenses) {
            for (Budget budget : budgets) {
                if (budget.getCategory().equalsIgnoreCase(expense.getCategory())) {
                    budget.addSpent(expense.getAmount());
                    break;
                }
            }
        }
    }
    
    // Edit Budget Methods
    public void editBudget(String category, String action) throws BudgetExceededException {
        Budget budgetToEdit = null;
        
        // Find the budget with the specified category
        for (Budget budget : budgets) {
            if (budget.getCategory().equalsIgnoreCase(category)) {
                budgetToEdit = budget;
                break;
            }
        }
        
        if (budgetToEdit == null) {
            System.out.println("No budget found for category: " + category);
            return;
        }
        
        // Execute the requested action
        if (action.equalsIgnoreCase("resetAll")) {
            budgetToEdit.resetAll();
            
            // Create a copy of the expenses list to avoid ConcurrentModificationException
            List<Expense> expensesToRemove = new ArrayList<>();
            for (Expense expense : expenses) {
                if (expense.getCategory().equalsIgnoreCase(category)) {
                    expensesToRemove.add(expense);
                }
            }
            
            // Remove all expenses for this category
            expenses.removeAll(expensesToRemove);
            
            System.out.println("Budget for " + category + " has been reset and all related expenses have been cleared.");
        } else if (action.equalsIgnoreCase("reduceLimit")) {

            budgetToEdit.reduceLimit();
            System.out.println("Budget limit has been adjusted if needed.");

        } else {
            System.out.println("Invalid action. Use 'resetAll' or 'reduceLimit'.");
        }
    }
    
    public Map<String, Double> getExpensesByCategory() {
        Map<String, Double> result = new HashMap<>();
        
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            double amount = expense.getAmount();
            
            result.put(category, result.getOrDefault(category, 0.0) + amount);
        }
        
        return result;
    }
    
    // Goal methods
    public void addGoal(Goal goal) {
        goals.add(goal);
    }
    
    public List<Goal> getGoals() {
        return Collections.unmodifiableList(goals);
    }
    
    public double calculateCurrentGoalAmount(Goal goal) {
        // This is a simple simulation - in a real app, 
        // you'd have specific savings or investment transactions linked to goals
        double totalSavings = 0.0;
        
        // Calculate total income
        double totalIncome = incomes.stream()
                .mapToDouble(Income::getAmount)
                .sum();
        
        // Calculate total expenses
        double totalExpenses = expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
        
        // Simple formula: assume a percentage of savings goes towards this goal
        // This is just a placeholder logic - real apps would have explicit savings allocation
        double savingsRate = 0.1; // 10% of net income
        totalSavings = (totalIncome - totalExpenses) * savingsRate;
        
        // Ensure we don't return negative values
        return Math.max(0, totalSavings);
    }
    
    // Payment Method methods
    public void addPaymentMethod(PaymentMethod paymentMethod) {
        paymentMethods.add(paymentMethod);
    }
    
    public List<PaymentMethod> getPaymentMethods() {
        return Collections.unmodifiableList(paymentMethods);
    }
    
    // Transaction methods
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        
        // Update payment method balance
        PaymentMethod method = transaction.getPaymentMethod();
        if (method != null) {
            if (transaction.getType().equalsIgnoreCase("debit")) {
                method.setBalance(method.getBalance() - transaction.getAmount());
            } else if (transaction.getType().equalsIgnoreCase("credit")) {
                method.setBalance(method.getBalance() + transaction.getAmount());
            }
        }
    }
    
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }
    
    // Reminder methods
    public void addReminder(String title, Date reminderDate) {
        reminders.put(title, reminderDate);
    }
    
    public Map<String, Date> getReminders() {
        return Collections.unmodifiableMap(reminders);
    }
    
    // Getters and setters
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", isLoggedIn=" + isLoggedIn +
                '}';
    }
}
