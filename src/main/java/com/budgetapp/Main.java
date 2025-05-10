package com.budgetapp;

import com.budgetapp.exceptions.AuthenticationException;
import com.budgetapp.exceptions.BudgetExceededException;
import com.budgetapp.exceptions.PersistenceException;
import com.budgetapp.models.*;
import com.budgetapp.services.FinancialReport;
import com.budgetapp.services.PersistenceManager;
import com.budgetapp.services.ReminderService;
import com.budgetapp.services.UserService;
import com.budgetapp.utils.DateUtils;
import com.budgetapp.utils.InputValidator;

import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static UserService userService;
    private static PersistenceManager persistenceManager;
    private static ReminderService reminderService;
    private static User currentUser = null;
    private static final String DATA_FILE = "budget_app_data.ser";

    public static void main(String[] args) {
        System.out.println("=== Personal Budget Management App ===");
        
        try {
            initialize();
            showMainMenu();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void initialize() {
        persistenceManager = new PersistenceManager(DATA_FILE);
        userService = new UserService(persistenceManager);
        reminderService = new ReminderService();
        
        try {
            persistenceManager.loadDataFromFile();
            System.out.println("Data loaded successfully.");
        } catch (PersistenceException e) {
            System.out.println("Unable to load previous data: " + e.getMessage());
            System.out.println("Starting with fresh data.");
        }
    }

    private static void showMainMenu() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n=== Main Menu ===");
            if (currentUser == null || !currentUser.isLoggedIn()) {
                System.out.println("1. Sign Up");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");

                int choice = readIntInput();
                switch (choice) {
                    case 1:
                        signUp();
                        break;
                    case 2:
                        login();
                        break;
                    case 3:
                        exit = true;
                        System.out.println("Thank you for using the Budget App. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                showUserDashboard();
            }
        }
    }

    private static void signUp() {
        System.out.println("\n=== Sign Up ===");
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine().trim();
        
        try {
            if (!InputValidator.isValidEmail(email)) {
                System.out.println("Invalid email format. Please try again.");
                return;
            }
            
            if (!InputValidator.isValidPhoneNumber(phoneNumber)) {
                System.out.println("Invalid phone number format. Please try again.");
                return;
            }
            
            if (simulateOTPVerification(phoneNumber)) {
                User newUser = userService.signUp(username, email, password, phoneNumber);
                currentUser = newUser;
                System.out.println("Sign-up successful! Welcome, " + username + "!");
            } else {
                System.out.println("OTP verification failed. Please try again.");
            }
        } catch (AuthenticationException e) {
            System.out.println("Sign-up failed: " + e.getMessage());
        }
    }

    private static boolean simulateOTPVerification(String phoneNumber) {
        System.out.println("\nSimulating OTP verification...");
        System.out.println("An OTP has been sent to " + phoneNumber);
        
        String otp = String.format("%06d", new Random().nextInt(999999));
        System.out.println("For simulation purposes, the OTP is: " + otp);
        
        System.out.print("Enter the OTP you received: ");
        String enteredOtp = scanner.nextLine().trim();
        
        return enteredOtp.equals(otp);
    }

    private static void login() {
        System.out.println("\n=== Login ===");
        System.out.print("Enter username or email: ");
        String usernameOrEmail = scanner.nextLine().trim();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        
        try {
            currentUser = userService.login(usernameOrEmail, password);
            System.out.println("Login successful! Welcome back, " + currentUser.getUsername() + "!");
        } catch (AuthenticationException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private static void showUserDashboard() {
        boolean logout = false;
        
        while (!logout && currentUser != null && currentUser.isLoggedIn()) {
            System.out.println("\n=== User Dashboard (" + currentUser.getUsername() + ") ===");

            System.out.println("1. Income Management");
            System.out.println("2. Expense Management");
            System.out.println("3. Budget Management");
            System.out.println("4. Financial Goals");
            System.out.println("5. Payment Methods");
            System.out.println("6. Financial Reports");
            System.out.println("7. Reminders");
            System.out.println("8. Logout");
            System.out.print("Choose an option: ");
            
            int choice = readIntInput();
            switch (choice) {
                case 1:
                    manageIncome();
                    break;
                case 2:
                    manageExpenses();
                    break;
                case 3:
                    manageBudget();
                    break;
                case 4:
                    manageGoals();
                    break;
                case 5:
                    managePaymentMethods();
                    break;
                case 6:
                    viewFinancialReports();
                    break;
                case 7:
                    manageReminders();
                    break;
                case 8:
                    currentUser.logout();
                    currentUser = null;
                    logout = true;
                    System.out.println("Logged out successfully!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void manageIncome() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n=== Income Management ===");
            System.out.println("1. Add Income");
            System.out.println("2. List All Incomes");
            System.out.println("3. Back to Dashboard");
            System.out.print("Choose an option: ");
            
            int choice = readIntInput();
            switch (choice) {
                case 1:
                    addIncome();
                    break;
                case 2:
                    listIncomes();
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addIncome() {
        System.out.println("\n=== Add Income ===");
        
        System.out.print("Enter income source: ");
        String source = scanner.nextLine().trim();
        
        System.out.print("Enter amount: ");
        double amount = readDoubleInput();
        
        System.out.print("Enter date (MM/DD/YYYY): ");
        String dateStr = scanner.nextLine().trim();
        Date date = DateUtils.parseDate(dateStr);
        
        System.out.print("Is this a recurring income? (y/n): ");
        boolean isRecurring = scanner.nextLine().trim().toLowerCase().startsWith("y");
        
        Income income = new Income(source, amount, date, isRecurring);
        currentUser.addIncome(income);
        
        try {
            persistenceManager.saveDataToFile();
            System.out.println("Income added successfully!");
        } catch (PersistenceException e) {
            System.out.println("Failed to save income: " + e.getMessage());
        }
    }

    private static void listIncomes() {
        System.out.println("\n=== Income List ===");
        List<Income> incomes = currentUser.getIncomes();
        
        if (incomes.isEmpty()) {
            System.out.println("No incomes recorded yet.");
            return;
        }
        
        System.out.println("----------------------------------------------------------");
        System.out.printf("%-20s %-10s %-15s %-10s\n", "Source", "Amount", "Date", "Recurring");
        System.out.println("----------------------------------------------------------");
        
        for (Income income : incomes) {
            System.out.printf("%-20s $%-9.2f %-15s %-10s\n", 
                income.getSource(), 
                income.getAmount(), 
                DateUtils.formatDate(income.getDate()),
                income.isRecurring() ? "Yes" : "No");
        }
        System.out.println("----------------------------------------------------------");
    }

    private static void manageExpenses() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n=== Expense Management ===");
            System.out.println("1. Add Expense");
            System.out.println("2. List All Expenses");
            System.out.println("3. Back to Dashboard");
            System.out.print("Choose an option: ");
            
            int choice = readIntInput();
            switch (choice) {
                case 1:
                    addExpense();
                    break;
                case 2:
                    listExpenses();
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addExpense() {
        System.out.println("\n=== Add Expense ===");
        
        System.out.print("Enter category: ");
        String category = scanner.nextLine().trim();
        
        System.out.print("Enter amount: ");
        double amount = readDoubleInput();
        
        System.out.print("Enter date (MM/DD/YYYY): ");
        String dateStr = scanner.nextLine().trim();
        Date date = DateUtils.parseDate(dateStr);
        
        System.out.print("Is this a recurring expense? (y/n): ");
        boolean isRecurring = scanner.nextLine().trim().toLowerCase().startsWith("y");
        
        PaymentMethod paymentMethod = null;
        List<PaymentMethod> paymentMethods = currentUser.getPaymentMethods();
        
        if (!paymentMethods.isEmpty()) {
            System.out.println("Select payment method:");
            for (int i = 0; i < paymentMethods.size(); i++) {
                System.out.println((i + 1) + ". " + paymentMethods.get(i).getType() + " ending in " + 
                        paymentMethods.get(i).getCardNumber().substring(Math.max(0, paymentMethods.get(i).getCardNumber().length() - 4)));
            }
            System.out.println((paymentMethods.size() + 1) + ". None/Cash");
            System.out.print("Choose an option: ");
            
            int pmChoice = readIntInput();
            if (pmChoice > 0 && pmChoice <= paymentMethods.size()) {
                paymentMethod = paymentMethods.get(pmChoice - 1);
            }
        }
        
        Expense expense = new Expense(amount, category, date, isRecurring);
        currentUser.addExpense(expense);
        
        // If payment method was selected, record a transaction
        if (paymentMethod != null) {
            Transaction transaction = new Transaction(amount, "debit", paymentMethod, date);
            currentUser.addTransaction(transaction);
        }
        
        // Check if we're exceeding budget, but still add the expense
        boolean exceededBudget = false;
        String warningMessage = "";
        try {
            currentUser.checkBudgetLimits(category, amount);
        } catch (BudgetExceededException e) {
            exceededBudget = true;
            warningMessage = e.getMessage();
        }
        
        try {
            persistenceManager.saveDataToFile();
            System.out.println("Expense added successfully!");
            
            if (exceededBudget) {
                System.out.println("\nWARNING: " + warningMessage);
                System.out.println("You may want to adjust your budget limits or spending habits.");
                System.out.println("Go to Budget Management > Edit Budget to make adjustments.");
            }
        } catch (PersistenceException e) {
            System.out.println("Failed to save expense: " + e.getMessage());
        }
    }

    private static void listExpenses() {
        System.out.println("\n=== Expense List ===");
        List<Expense> expenses = currentUser.getExpenses();
        
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }
        
        System.out.println("----------------------------------------------------------");
        System.out.printf("%-20s %-10s %-15s %-10s\n", "Category", "Amount", "Date", "Recurring");
        System.out.println("----------------------------------------------------------");
        
        for (Expense expense : expenses) {
            System.out.printf("%-20s $%-9.2f %-15s %-10s\n", 
                expense.getCategory(), 
                expense.getAmount(), 
                DateUtils.formatDate(expense.getDate()),
                expense.isRecurring() ? "Yes" : "No");
        }
        System.out.println("----------------------------------------------------------");
    }

    private static void manageBudget() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n=== Budget Management ===");
            System.out.println("1. Set Budget for Category");
            System.out.println("2. View Budgets");
            System.out.println("3. Analyze Spending");
            System.out.println("4. Edit Budget");
            System.out.println("5. Back to Dashboard");
            System.out.print("Choose an option: ");
            
            int choice = readIntInput();
            switch (choice) {
                case 1:
                    setBudget();
                    break;
                case 2:
                    viewBudgets();
                    break;
                case 3:
                    analyzeSpending();
                    break;
                case 4:
                    editBudget();
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void editBudget() {
        System.out.println("\n=== Edit Budget ===");
        List<Budget> budgets = currentUser.getBudgets();
        
        if (budgets.isEmpty()) {
            System.out.println("No budgets set up yet. Please create a budget first.");
            return;
        }
        
        // List all budgets
        System.out.println("Available budgets:");
        System.out.println("----------------------------------------------------------");
        System.out.printf("%-4s %-20s %-10s %-10s %-10s\n", "No.", "Category", "Limit", "Spent", "Usage %");
        System.out.println("----------------------------------------------------------");
        
        for (int i = 0; i < budgets.size(); i++) {
            Budget budget = budgets.get(i);
            System.out.printf("%-4d %-20s $%-9.2f $%-9.2f %.1f%%\n", 
                (i + 1),
                budget.getCategory(), 
                budget.getLimitAmount(), 
                budget.getSpentAmount(),
                budget.calculateUsagePercentage());
        }
        System.out.println("----------------------------------------------------------");
        
        System.out.print("Select budget to edit (number): ");
        int budgetIndex = readIntInput() - 1;
        
        if (budgetIndex < 0 || budgetIndex >= budgets.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        
        Budget selectedBudget = budgets.get(budgetIndex);
        System.out.println("\nSelected budget: " + selectedBudget.getCategory());
        System.out.println("Current usage: " + String.format("%.1f", selectedBudget.calculateUsagePercentage()) + "%");
        
        System.out.println("\nActions:");
        System.out.println("1. Reset budget and clear all related expenses");
        System.out.println("2. Increase Limit");
        System.out.println("3. Back to Budget Management");
        System.out.print("Choose an option: ");
        
        int actionChoice = readIntInput();
        try {
            switch (actionChoice) {
                case 1:
                    System.out.println("\nWARNING: This will permanently delete all expenses in this category.");
                    System.out.print("Are you sure you want to proceed? (y/n): ");
                    String confirmation = scanner.nextLine().trim().toLowerCase();
                    
                    if (confirmation.startsWith("y")) {
                        currentUser.editBudget(selectedBudget.getCategory(), "resetAll");
                        // The success message is now handled in the editBudget method
                    } else {
                        System.out.println("Reset operation cancelled.");
                    }
                    break;
                case 2:
                    try {
                        currentUser.editBudget(selectedBudget.getCategory(), "reduceLimit");
                    } catch (BudgetExceededException e) {
                        System.out.println("Budget has been exceeded. What would you like to do?");
                        System.out.println("1. Reset budget and clear all related expenses");
                        System.out.println("2. Increase budget limit");
                        System.out.print("Choose an option: ");
                        
                        int exceptionChoice = readIntInput();
                        if (exceptionChoice == 1) {
                            System.out.println("\nWARNING: This will permanently delete all expenses in this category.");
                            System.out.print("Are you sure you want to proceed? (y/n): ");
                            String confirmationReset = scanner.nextLine().trim().toLowerCase();
                            
                            if (confirmationReset.startsWith("y")) {
                                currentUser.editBudget(selectedBudget.getCategory(), "resetAll");
                            } else {
                                System.out.println("Reset operation cancelled.");
                            }
                        } else if (exceptionChoice == 2) {
                            System.out.print("Enter the amount you want to add on the limit:");
                            double amount = readDoubleInput();
                            double newLimit = selectedBudget.getSpentAmount() + amount;
                            Budget newBudget = new Budget(selectedBudget.getCategory(), newLimit, selectedBudget.getSpentAmount());
                            currentUser.setBudget(newBudget);
                            System.out.println("Budget limit has been increased to $" + String.format("%.2f", newLimit));
                        } else {
                            System.out.println("Invalid choice. No changes made.");
                        }
                    }
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Invalid choice. No changes made.");
            }
            
            try {
                persistenceManager.saveDataToFile();
            } catch (PersistenceException e) {
                System.out.println("Failed to save changes: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("Error editing budget: " + e.getMessage());
        }
    }

    private static void setBudget() {
        System.out.println("\n=== Set Budget for Category ===");
        
        System.out.print("Enter category: ");
        String category = scanner.nextLine().trim();
        
        System.out.print("Enter budget limit amount: ");
        double limitAmount = readDoubleInput();
        
        Budget budget = new Budget(category, limitAmount, 0.0);
        currentUser.setBudget(budget);
        
        try {
            persistenceManager.saveDataToFile();
            System.out.println("Budget set successfully for " + category + "!");
        } catch (PersistenceException e) {
            System.out.println("Failed to save budget: " + e.getMessage());
        }
    }

    private static void viewBudgets() {
        System.out.println("\n=== Budget Summary ===");
        List<Budget> budgets = currentUser.getBudgets();
        
        if (budgets.isEmpty()) {
            System.out.println("No budgets set yet.");
            return;
        }
        
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("%-20s %-15s %-15s %-10s %-20s\n", "Category", "Limit", "Spent", "% Used", "Status");
        System.out.println("-------------------------------------------------------------------------");
        
        for (Budget budget : budgets) {
            double percentUsed = budget.calculateUsagePercentage();

            String statusMessage = budget.analyzeSpending();
            String status = "";
            
            if (statusMessage.contains("ALERT:")) {
                status = "ALERT";
            } else if (statusMessage.contains("WARNING:")) {
                status = "WARNING";
            } else if (statusMessage.contains("CAUTION:")) {
                status = "CAUTION";
            } else {
                status = "ON TRACK";
            }
            
            System.out.printf("%-20s $%-14.2f $%-14.2f %.1f%%     %-20s\n", 
                budget.getCategory(), 
                budget.getLimitAmount(), 
                budget.getSpentAmount(),
                percentUsed,
                status);
        }
        System.out.println("-------------------------------------------------------------------------");
        
        System.out.println("\nBudget Alerts:");
        boolean hasAlerts = false;
        
        for (Budget budget : budgets) {
            String message = budget.analyzeSpending();
            if (message.contains("ALERT:") || message.contains("WARNING:") || message.contains("CAUTION:")) {
                System.out.println("- " + message);
                hasAlerts = true;
            }
        }
        
        if (!hasAlerts) {
            System.out.println("No budget alerts at this time. All budgets are on track!");
        }
    }

    private static void analyzeSpending() {
        System.out.println("\n=== Spending Analysis ===");
        
        currentUser.updateBudgetSpentAmounts();
        
        Map<String, Double> expensesByCategory = currentUser.getExpensesByCategory();
        
        if (expensesByCategory.isEmpty()) {
            System.out.println("No expenses recorded yet for analysis.");
            return;
        }
        
        System.out.println("----------------------------------------------------------");
        System.out.printf("%-20s %-15s %-15s\n", "Category", "Amount", "% of Total");
        System.out.println("----------------------------------------------------------");
        
        double totalExpenses = expensesByCategory.values().stream().mapToDouble(Double::doubleValue).sum();
        
        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(expensesByCategory.entrySet());
        sortedEntries.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())); // Sort by amount descending
        
        double percentageSum = 0.0;
        for (Map.Entry<String, Double> entry : sortedEntries) {
            double percentage = (entry.getValue() / totalExpenses) * 100;
            percentageSum += percentage;
            System.out.printf("%-20s $%-14.2f %.1f%%\n", 
                entry.getKey(), 
                entry.getValue(), 
                percentage);
        }
        
        System.out.println("----------------------------------------------------------");
        System.out.printf("%-20s $%-14.2f 100.0%%\n", "TOTAL", totalExpenses);
        System.out.println("----------------------------------------------------------");
    }

    private static void manageGoals() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n=== Financial Goals ===");
            System.out.println("1. Set New Goal");
            System.out.println("2. View Goals Progress");
            System.out.println("3. Back to Dashboard");
            System.out.print("Choose an option: ");
            
            int choice = readIntInput();
            switch (choice) {
                case 1:
                    setGoal();
                    break;
                case 2:
                    viewGoals();
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void setGoal() {
        System.out.println("\n=== Set New Financial Goal ===");
        
        System.out.print("Enter goal name: ");
        String goalName = scanner.nextLine().trim();
        
        System.out.print("Enter target amount: ");
        double targetAmount = readDoubleInput();
        
        System.out.print("Enter target date (MM/DD/YYYY): ");
        String dateStr = scanner.nextLine().trim();
        Date targetDate = DateUtils.parseDate(dateStr);
        
        Goal goal = new Goal(goalName, targetAmount, targetDate);
        currentUser.addGoal(goal);
        
        try {
            persistenceManager.saveDataToFile();
            System.out.println("Financial goal set successfully!");
        } catch (PersistenceException e) {
            System.out.println("Failed to save goal: " + e.getMessage());
        }
    }

    private static void viewGoals() {
        System.out.println("\n=== Financial Goals Progress ===");
        List<Goal> goals = currentUser.getGoals();
        
        if (goals.isEmpty()) {
            System.out.println("No financial goals set yet.");
            return;
        }
        
        System.out.println("----------------------------------------------------------");
        System.out.printf("%-20s %-15s %-15s %-15s\n", "Goal", "Target", "Current", "Target Date");
        System.out.println("----------------------------------------------------------");
        
        for (Goal goal : goals) {
            // Calculate current amount based on savings or investment transactions
            double currentAmount = currentUser.calculateCurrentGoalAmount(goal);
            double progressPercentage = (currentAmount / goal.getTargetAmount()) * 100;
            
            System.out.printf("%-20s $%-14.2f $%-14.2f %-15s\n", 
                goal.getGoalName(), 
                goal.getTargetAmount(),
                currentAmount,
                DateUtils.formatDate(goal.getTargetDate()));
            
            // Simple progress bar
            System.out.print("Progress: [");
            int progressBars = (int)(progressPercentage / 5);
            for (int i = 0; i < 20; i++) {
                if (i < progressBars) {
                    System.out.print("=");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.printf("] %.1f%%\n\n", progressPercentage);
        }
    }

    private static void managePaymentMethods() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n=== Payment Methods ===");
            System.out.println("1. Add Payment Method");
            System.out.println("2. View Payment Methods");
            System.out.println("3. Back to Dashboard");
            System.out.print("Choose an option: ");
            
            int choice = readIntInput();
            switch (choice) {
                case 1:
                    addPaymentMethod();
                    break;
                case 2:
                    viewPaymentMethods();
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addPaymentMethod() {
        System.out.println("\n=== Add Payment Method ===");
        
        System.out.print("Enter card number: ");
        String cardNumber = scanner.nextLine().trim();
        
        if (!InputValidator.isValidCardNumber(cardNumber)) {
            System.out.println("Invalid card number format. Please try again.");
            return;
        }
        
        System.out.print("Enter type (credit/debit/etc.): ");
        String type = scanner.nextLine().trim();
        
        System.out.print("Enter initial balance: ");
        double balance = readDoubleInput();
        
        PaymentMethod paymentMethod = new PaymentMethod(cardNumber, balance, type);
        currentUser.addPaymentMethod(paymentMethod);
        
        try {
            persistenceManager.saveDataToFile();
            System.out.println("Payment method added successfully!");
        } catch (PersistenceException e) {
            System.out.println("Failed to save payment method: " + e.getMessage());
        }
    }

    private static void viewPaymentMethods() {
        System.out.println("\n=== Payment Methods ===");
        List<PaymentMethod> paymentMethods = currentUser.getPaymentMethods();
        
        if (paymentMethods.isEmpty()) {
            System.out.println("No payment methods added yet.");
            return;
        }
        
        System.out.println("----------------------------------------------");
        System.out.printf("%-6s %-15s %-15s\n", "Type", "Card Number", "Balance");
        System.out.println("----------------------------------------------");
        
        for (PaymentMethod pm : paymentMethods) {
            String maskedNumber = "****-****-****-" +
                    pm.getCardNumber().substring(Math.max(0, pm.getCardNumber().length() - 4));
            
            System.out.printf("%-6s %-15s $%-14.2f\n", 
                pm.getType(), 
                maskedNumber,
                pm.getBalance());
        }
        System.out.println("----------------------------------------------");
    }

    private static void viewFinancialReports() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n=== Financial Reports ===");
            System.out.println("1. Monthly Report");
            System.out.println("2. Category Summary");
            System.out.println("3. Back to Dashboard");
            System.out.print("Choose an option: ");
            
            int choice = readIntInput();
            switch (choice) {
                case 1:
                    generateMonthlyReport();
                    break;
                case 2:
                    generateCategorySummary();
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void generateMonthlyReport() {
        System.out.println("\n=== Monthly Financial Report ===");
        
        System.out.print("Enter month (MM/YYYY): ");
        String monthYear = scanner.nextLine().trim();
        
        try {
            Calendar calendar = Calendar.getInstance();
            String[] parts = monthYear.split("/");
            int month = Integer.parseInt(parts[0]) - 1; // Calendar months are 0-based
            int year = Integer.parseInt(parts[1]);
            
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            
            FinancialReport report = new FinancialReport();
            report.generateMonthlyReport(currentUser, calendar.getTime());
        } catch (Exception e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }

    private static void generateCategorySummary() {
        System.out.println("\n=== Category Spending Summary ===");
        
        System.out.print("Enter category: ");
        String category = scanner.nextLine().trim();
        
        FinancialReport report = new FinancialReport();
        report.generateCategorySummary(currentUser, category);
    }

    private static void manageReminders() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n=== Reminders ===");
            System.out.println("1. Set New Reminder");
            System.out.println("2. View All Reminders");
            System.out.println("3. Back to Dashboard");
            System.out.print("Choose an option: ");
            
            int choice = readIntInput();
            switch (choice) {
                case 1:
                    setReminder();
                    break;
                case 2:
                    viewReminders();
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void setReminder() {
        System.out.println("\n=== Set New Reminder ===");
        
        System.out.print("Enter reminder title: ");
        String title = scanner.nextLine().trim();
        
        System.out.print("Enter reminder description: ");
        String description = scanner.nextLine().trim();
        
        System.out.print("Enter reminder date (MM/DD/YYYY): ");
        String dateStr = scanner.nextLine().trim();
        Date date = DateUtils.parseDate(dateStr);
        
        reminderService.setReminder(currentUser, title, description, date);
        
        try {
            persistenceManager.saveDataToFile();
            System.out.println("Reminder set successfully for " + DateUtils.formatDate(date) + "!");
        } catch (PersistenceException e) {
            System.out.println("Failed to save reminder: " + e.getMessage());
        }
    }

    private static void viewReminders() {
        System.out.println("\n=== Reminders ===");
        
        reminderService.checkReminders(currentUser);
        Map<String, Date> reminders = currentUser.getReminders();
        
        if (reminders.isEmpty()) {
            System.out.println("No reminders set yet.");
            return;
        }
        
        System.out.println("----------------------------------------------------------");
        System.out.printf("%-30s %-20s\n", "Title", "Due Date");
        System.out.println("----------------------------------------------------------");
        
        for (Map.Entry<String, Date> entry : reminders.entrySet()) {
            System.out.printf("%-30s %-20s\n", 
                entry.getKey(), 
                DateUtils.formatDate(entry.getValue()));
        }
        System.out.println("----------------------------------------------------------");
    }

    private static int readIntInput() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double readDoubleInput() {
        try {
            String input = scanner.nextLine().trim();
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
