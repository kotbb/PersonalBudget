package com.investwise;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;
    private static final PersistenceLayer persistenceLayer = new PersistenceLayer();
    private static final Authentication authentication = new Authentication();
    public Main() {
    }
    public static void main(String[] args) {
        System.out.println("Welcome to Invest Wise - Your Budget Management App");
        showMainMenu();
        scanner.close();
    }
    private static void showMainMenu() {
        while (true) {
            System.out.println("\n----- MAIN MENU -----");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }
            switch (choice) {
                case 1:
                    signUp();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.out.println("Thank you for using Invest Wise. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private static void signUp() {
        System.out.println("\n----- SIGN UP -----");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        try {
            User newUser = authentication.registerUser(username, password, email);
            persistenceLayer.saveUser(newUser);
            System.out.println("Registration successful! Please login.");
        } catch (AppException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }
    private static void login() {
        System.out.println("\n----- LOGIN -----");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        try {
            currentUser = authentication.loginUser(username, password);
            System.out.println("Login successful! Welcome, " + currentUser.getUsername() + "!");
            showUserMenu();
        } catch (AppException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }
    private static void showUserMenu() {
        while (true) {
            System.out.println("\n----- USER MENU -----");
            System.out.println("1. Asset Management");
            System.out.println("2. Budget Management");
            System.out.println("3. Income & Expense Tracking");
            System.out.println("4. Banking & Payment Methods");
            System.out.println("5. Financial Reports");
            System.out.println("6. Log Out");
            System.out.print("Select an option: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }
            switch (choice) {
                case 1:
                    assetManagementMenu();
                    break;
                case 2:
                    budgetManagementMenu();
                    break;
                case 3:
                    incomeExpenseMenu();
                    break;
                case 4:
                    bankingMenu();
                    break;
                case 5:
                    reportingMenu();
                    break;
                case 6:
                    System.out.println("Logging out...");
                    currentUser = null;
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private static void assetManagementMenu() {
        while (true) {
            System.out.println("\n----- ASSET MANAGEMENT -----");
            System.out.println("1. View All Assets");
            System.out.println("2. Add New Asset");
            System.out.println("3. Update Asset");
            System.out.println("4. Remove Asset");
            System.out.println("5. Calculate Zakat");
            System.out.println("6. Back to Main Menu");
            System.out.print("Select an option: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }
            switch (choice) {
                case 1:
                    viewAllAssets();
                    break;
                case 2:
                    addNewAsset();
                    break;
                case 3:
                    updateAsset();
                    break;
                case 4:
                    removeAsset();
                    break;
                case 5:
                    calculateZakat();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private static void viewAllAssets() {
        List<Asset> assets = persistenceLayer.getUserAssets(currentUser.getUsername());
        if (assets.isEmpty()) {
            System.out.println("You don't have any assets yet.");
            return;
        }
        System.out.println("\n----- YOUR ASSETS -----");
        System.out.printf("%-5s %-15s %-10s %-10s %-15s %-10s\n", 
                "ID", "Type", "Value", "Currency", "Zakat Eligible", "Date Added");
        System.out.println("-------------------------------------------------------------------------");
        for (int i = 0; i < assets.size(); i++) {
            Asset asset = assets.get(i);
            System.out.printf("%-5d %-15s %-10.2f %-10s %-15s %-10s\n", 
                    i+1, 
                    asset.getType(), 
                    asset.getValue(), 
                    asset.getCurrency(), 
                    asset.isZakatEligible() ? "Yes" : "No",
                    asset.getDateAdded());
        }
    }
    private static void addNewAsset() {
        System.out.println("\n----- ADD NEW ASSET -----");
        System.out.print("Enter asset type (gold, silver, cash, stock, crypto, property, other): ");
        String type = scanner.nextLine();
        double value;
        while (true) {
            System.out.print("Enter asset value: ");
            try {
                value = Double.parseDouble(scanner.nextLine());
                if (value <= 0) {
                    System.out.println("Asset value must be greater than zero.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        System.out.print("Enter currency (USD, EUR, GBP, etc.): ");
        String currency = scanner.nextLine();
        boolean zakatEligible = false;
        if (type.equalsIgnoreCase("gold") || 
            type.equalsIgnoreCase("silver") || 
            type.equalsIgnoreCase("cash") || 
            type.equalsIgnoreCase("stock") || 
            type.equalsIgnoreCase("crypto")) {
            zakatEligible = true;
        } else {
            System.out.print("Is this asset Zakat eligible? (yes/no): ");
            String response = scanner.nextLine();
            zakatEligible = response.equalsIgnoreCase("yes");
        }
        System.out.print("Enter description (optional): ");
        String description = scanner.nextLine();
        Asset newAsset = new Asset(type, value, currency, zakatEligible, description, new Date());
        persistenceLayer.saveAsset(currentUser.getUsername(), newAsset);
        System.out.println("Asset added successfully!");
    }
    private static void updateAsset() {
        List<Asset> assets = persistenceLayer.getUserAssets(currentUser.getUsername());
        if (assets.isEmpty()) {
            System.out.println("You don't have any assets to update.");
            return;
        }
        viewAllAssets();
        int assetId;
        while (true) {
            System.out.print("\nEnter the ID of the asset to update (or 0 to cancel): ");
            try {
                assetId = Integer.parseInt(scanner.nextLine());
                if (assetId == 0) {
                    return;
                }
                if (assetId < 1 || assetId > assets.size()) {
                    System.out.println("Invalid ID. Please enter a valid asset ID.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        Asset selectedAsset = assets.get(assetId - 1);
        System.out.println("\n----- UPDATE ASSET -----");
        System.out.println("Current type: " + selectedAsset.getType());
        System.out.print("Enter new asset type (leave blank to keep current): ");
        String type = scanner.nextLine();
        if (!type.isEmpty()) {
            selectedAsset.setType(type);
        }
        System.out.println("Current value: " + selectedAsset.getValue());
        System.out.print("Enter new asset value (leave blank to keep current): ");
        String valueStr = scanner.nextLine();
        if (!valueStr.isEmpty()) {
            try {
                double value = Double.parseDouble(valueStr);
                if (value > 0) {
                    selectedAsset.setValue(value);
                } else {
                    System.out.println("Value must be greater than zero. Keeping current value.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid value. Keeping current value.");
            }
        }
        System.out.println("Current currency: " + selectedAsset.getCurrency());
        System.out.print("Enter new currency (leave blank to keep current): ");
        String currency = scanner.nextLine();
        if (!currency.isEmpty()) {
            selectedAsset.setCurrency(currency);
        }
        persistenceLayer.updateAsset(currentUser.getUsername(), assetId - 1, selectedAsset);
        System.out.println("Asset updated successfully!");
    }
    private static void removeAsset() {
        List<Asset> assets = persistenceLayer.getUserAssets(currentUser.getUsername());
        if (assets.isEmpty()) {
            System.out.println("You don't have any assets to remove.");
            return;
        }
        viewAllAssets();
        int assetId;
        while (true) {
            System.out.print("\nEnter the ID of the asset to remove (or 0 to cancel): ");
            try {
                assetId = Integer.parseInt(scanner.nextLine());
                if (assetId == 0) {
                    return;
                }
                if (assetId < 1 || assetId > assets.size()) {
                    System.out.println("Invalid ID. Please enter a valid asset ID.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        System.out.print("Are you sure you want to remove this asset? (yes/no): ");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("yes")) {
            persistenceLayer.removeAsset(currentUser.getUsername(), assetId - 1);
            System.out.println("Asset removed successfully!");
        } else {
            System.out.println("Asset removal cancelled.");
        }
    }
    private static void calculateZakat() {
        FinancialReport report = new FinancialReport();
        double zakatAmount = report.calculateZakat(currentUser);
        System.out.println("\n----- ZAKAT CALCULATION -----");
        System.out.println("Your zakat-eligible assets total: " + report.getTotalZakatEligibleAssets(currentUser) + " " + getCurrentUserMainCurrency());
        System.out.println("Zakat amount (2.5%): " + zakatAmount + " " + getCurrentUserMainCurrency());
        List<Asset> eligibleAssets = persistenceLayer.getZakatEligibleAssets(currentUser.getUsername());
        if (!eligibleAssets.isEmpty()) {
            System.out.println("\nZakat-eligible assets:");
            System.out.printf("%-15s %-10s %-10s\n", "Type", "Value", "Currency");
            System.out.println("----------------------------------------");
            for (Asset asset : eligibleAssets) {
                System.out.printf("%-15s %-10.2f %-10s\n", 
                        asset.getType(), 
                        asset.getValue(), 
                        asset.getCurrency());
            }
        }
    }
    private static void budgetManagementMenu() {
        while (true) {
            System.out.println("\n----- BUDGET MANAGEMENT -----");
            System.out.println("1. View All Budgets");
            System.out.println("2. Create New Budget");
            System.out.println("3. Update Budget");
            System.out.println("4. Delete Budget");
            System.out.println("5. View Budget Performance");
            System.out.println("6. Back to Main Menu");
            System.out.print("Select an option: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }
            switch (choice) {
                case 1:
                    viewAllBudgets();
                    break;
                case 2:
                    createNewBudget();
                    break;
                case 3:
                    updateBudget();
                    break;
                case 4:
                    deleteBudget();
                    break;
                case 5:
                    viewBudgetPerformance();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private static void viewAllBudgets() {
        List<Budget> budgets = persistenceLayer.getUserBudgets(currentUser.getUsername());
        if (budgets.isEmpty()) {
            System.out.println("You don't have any budgets yet.");
            return;
        }
        System.out.println("\n----- YOUR BUDGETS -----");
        System.out.printf("%-5s %-15s %-15s %-10s %-10s %-15s\n", 
                "ID", "Category", "Amount", "Currency", "Period", "Status");
        System.out.println("-----------------------------------------------------------------------");
        for (int i = 0; i < budgets.size(); i++) {
            Budget budget = budgets.get(i);
            double spent = persistenceLayer.calculateSpentAmount(currentUser.getUsername(), budget.getCategory());
            String status = spent <= budget.getAmount() ? "On Track" : "Exceeded";
            System.out.printf("%-5d %-15s %-15.2f %-10s %-10s %-15s\n", 
                    i+1, 
                    budget.getCategory(), 
                    budget.getAmount(), 
                    budget.getCurrency(), 
                    budget.getPeriod(),
                    status);
        }
    }
    private static void createNewBudget() {
        System.out.println("\n----- CREATE NEW BUDGET -----");
        System.out.print("Enter budget category (e.g., Food, Transportation, Housing): ");
        String category = scanner.nextLine();
        double amount;
        while (true) {
            System.out.print("Enter budget amount: ");
            try {
                amount = Double.parseDouble(scanner.nextLine());
                if (amount <= 0) {
                    System.out.println("Budget amount must be greater than zero.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        System.out.print("Enter currency (USD, EUR, GBP, etc.): ");
        String currency = scanner.nextLine();
        System.out.print("Enter budget period (monthly, weekly, yearly): ");
        String period = scanner.nextLine();
        Budget newBudget = new Budget(category, amount, currency, period);
        persistenceLayer.saveBudget(currentUser.getUsername(), newBudget);
        System.out.println("Budget created successfully!");
    }
    private static void updateBudget() {
        List<Budget> budgets = persistenceLayer.getUserBudgets(currentUser.getUsername());
        if (budgets.isEmpty()) {
            System.out.println("You don't have any budgets to update.");
            return;
        }
        viewAllBudgets();
        int budgetId;
        while (true) {
            System.out.print("\nEnter the ID of the budget to update (or 0 to cancel): ");
            try {
                budgetId = Integer.parseInt(scanner.nextLine());
                if (budgetId == 0) {
                    return;
                }
                if (budgetId < 1 || budgetId > budgets.size()) {
                    System.out.println("Invalid ID. Please enter a valid budget ID.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        Budget selectedBudget = budgets.get(budgetId - 1);
        System.out.println("\n----- UPDATE BUDGET -----");
        System.out.println("Current category: " + selectedBudget.getCategory());
        System.out.print("Enter new category (leave blank to keep current): ");
        String category = scanner.nextLine();
        if (!category.isEmpty()) {
            selectedBudget.setCategory(category);
        }
        System.out.println("Current amount: " + selectedBudget.getAmount());
        System.out.print("Enter new amount (leave blank to keep current): ");
        String amountStr = scanner.nextLine();
        if (!amountStr.isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount > 0) {
                    selectedBudget.setAmount(amount);
                } else {
                    System.out.println("Amount must be greater than zero. Keeping current amount.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Keeping current amount.");
            }
        }
        System.out.println("Current period: " + selectedBudget.getPeriod());
        System.out.print("Enter new period (leave blank to keep current): ");
        String period = scanner.nextLine();
        if (!period.isEmpty()) {
            selectedBudget.setPeriod(period);
        }
        persistenceLayer.updateBudget(currentUser.getUsername(), budgetId - 1, selectedBudget);
        System.out.println("Budget updated successfully!");
    }
    private static void deleteBudget() {
        List<Budget> budgets = persistenceLayer.getUserBudgets(currentUser.getUsername());
        if (budgets.isEmpty()) {
            System.out.println("You don't have any budgets to delete.");
            return;
        }
        viewAllBudgets();
        int budgetId;
        while (true) {
            System.out.print("\nEnter the ID of the budget to delete (or 0 to cancel): ");
            try {
                budgetId = Integer.parseInt(scanner.nextLine());
                if (budgetId == 0) {
                    return;
                }
                if (budgetId < 1 || budgetId > budgets.size()) {
                    System.out.println("Invalid ID. Please enter a valid budget ID.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        System.out.print("Are you sure you want to delete this budget? (yes/no): ");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("yes")) {
            persistenceLayer.removeBudget(currentUser.getUsername(), budgetId - 1);
            System.out.println("Budget deleted successfully!");
        } else {
            System.out.println("Budget deletion cancelled.");
        }
    }
    private static void viewBudgetPerformance() {
        List<Budget> budgets = persistenceLayer.getUserBudgets(currentUser.getUsername());
        if (budgets.isEmpty()) {
            System.out.println("You don't have any budgets to analyze.");
            return;
        }
        System.out.println("\n----- BUDGET PERFORMANCE -----");
        System.out.printf("%-15s %-10s %-10s %-10s %-15s\n", 
                "Category", "Budgeted", "Spent", "Remaining", "Status");
        System.out.println("----------------------------------------------------------");
        for (Budget budget : budgets) {
            double spent = persistenceLayer.calculateSpentAmount(currentUser.getUsername(), budget.getCategory());
            double remaining = budget.getAmount() - spent;
            String status = remaining >= 0 ? "On Track" : "Exceeded";
            System.out.printf("%-15s %-10.2f %-10.2f %-10.2f %-15s\n", 
                    budget.getCategory(), 
                    budget.getAmount(), 
                    spent, 
                    remaining, 
                    status);
        }
    }
    private static void incomeExpenseMenu() {
        while (true) {
            System.out.println("\n----- INCOME & EXPENSE TRACKING -----");
            System.out.println("1. Record Income");
            System.out.println("2. Record Expense");
            System.out.println("3. View Income Records");
            System.out.println("4. View Expense Records");
            System.out.println("5. View Monthly Summary");
            System.out.println("6. Back to Main Menu");
            System.out.print("Select an option: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }
            switch (choice) {
                case 1:
                    recordIncome();
                    break;
                case 2:
                    recordExpense();
                    break;
                case 3:
                    viewIncomeRecords();
                    break;
                case 4:
                    viewExpenseRecords();
                    break;
                case 5:
                    viewMonthlySummary();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private static void recordIncome() {
        System.out.println("\n----- RECORD INCOME -----");
        System.out.print("Enter income source (e.g., Salary, Freelance, Investments): ");
        String source = scanner.nextLine();
        double amount;
        while (true) {
            System.out.print("Enter amount: ");
            try {
                amount = Double.parseDouble(scanner.nextLine());
                if (amount <= 0) {
                    System.out.println("Amount must be greater than zero.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        System.out.print("Enter currency (USD, EUR, GBP, etc.): ");
        String currency = scanner.nextLine();
        System.out.print("Enter date (MM/DD/YYYY, leave blank for today): ");
        String dateStr = scanner.nextLine();
        Date date = new Date();  
        System.out.print("Enter description (optional): ");
        String description = scanner.nextLine();
        PaymentMethod paymentMethod = selectPaymentMethod();
        if (paymentMethod == null) {
            System.out.println("Income recording cancelled.");
            return;
        }
        Income income = new Income(source, amount, currency, date, description, paymentMethod);
        persistenceLayer.saveIncome(currentUser.getUsername(), income);
        System.out.println("Income recorded successfully!");
    }
    private static void recordExpense() {
        System.out.println("\n----- RECORD EXPENSE -----");
        System.out.print("Enter expense category (e.g., Food, Transportation, Utilities): ");
        String category = scanner.nextLine();
        double amount;
        while (true) {
            System.out.print("Enter amount: ");
            try {
                amount = Double.parseDouble(scanner.nextLine());
                if (amount <= 0) {
                    System.out.println("Amount must be greater than zero.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        System.out.print("Enter currency (USD, EUR, GBP, etc.): ");
        String currency = scanner.nextLine();
        System.out.print("Enter date (MM/DD/YYYY, leave blank for today): ");
        String dateStr = scanner.nextLine();
        Date date = new Date();  
        System.out.print("Enter description (optional): ");
        String description = scanner.nextLine();
        PaymentMethod paymentMethod = selectPaymentMethod();
        if (paymentMethod == null) {
            System.out.println("Expense recording cancelled.");
            return;
        }
        Expense expense = new Expense(category, amount, currency, date, description, paymentMethod);
        persistenceLayer.saveExpense(currentUser.getUsername(), expense);
        checkBudgetStatus(category, amount);
        System.out.println("Expense recorded successfully!");
    }
    private static void checkBudgetStatus(String category, double amount) {
        List<Budget> budgets = persistenceLayer.getUserBudgets(currentUser.getUsername());
        for (Budget budget : budgets) {
            if (budget.getCategory().equalsIgnoreCase(category)) {
                double spent = persistenceLayer.calculateSpentAmount(currentUser.getUsername(), category);
                if (spent > budget.getAmount()) {
                    System.out.println("\nWARNING: This expense has exceeded your budget for " + category + "!");
                    System.out.println("Budget: " + budget.getAmount() + " " + budget.getCurrency());
                    System.out.println("Spent: " + spent + " " + budget.getCurrency());
                    System.out.println("Overspent: " + (spent - budget.getAmount()) + " " + budget.getCurrency());
                } else {
                    double remaining = budget.getAmount() - spent;
                    double percentUsed = (spent / budget.getAmount()) * 100;
                    System.out.println("\nBudget status for " + category + ":");
                    System.out.println("Spent: " + spent + " " + budget.getCurrency() + 
                            " (" + String.format("%.1f", percentUsed) + "% of budget)");
                    System.out.println("Remaining: " + remaining + " " + budget.getCurrency());
                }
                break;
            }
        }
    }
    private static void viewIncomeRecords() {
        List<Income> incomes = persistenceLayer.getUserIncomes(currentUser.getUsername());
        if (incomes.isEmpty()) {
            System.out.println("You don't have any income records yet.");
            return;
        }
        System.out.println("\n----- YOUR INCOME RECORDS -----");
        System.out.printf("%-5s %-15s %-10s %-10s %-15s %-20s\n", 
                "ID", "Source", "Amount", "Currency", "Date", "Payment Method");
        System.out.println("-------------------------------------------------------------------------");
        for (int i = 0; i < incomes.size(); i++) {
            Income income = incomes.get(i);
            System.out.printf("%-5d %-15s %-10.2f %-10s %-15s %-20s\n", 
                    i+1, 
                    income.getSource(), 
                    income.getAmount(), 
                    income.getCurrency(), 
                    income.getDate().toString(),
                    income.getPaymentMethod().getName());
        }
    }
    private static void viewExpenseRecords() {
        List<Expense> expenses = persistenceLayer.getUserExpenses(currentUser.getUsername());
        if (expenses.isEmpty()) {
            System.out.println("You don't have any expense records yet.");
            return;
        }
        System.out.println("\n----- YOUR EXPENSE RECORDS -----");
        System.out.printf("%-5s %-15s %-10s %-10s %-15s %-20s\n", 
                "ID", "Category", "Amount", "Currency", "Date", "Payment Method");
        System.out.println("-------------------------------------------------------------------------");
        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            System.out.printf("%-5d %-15s %-10.2f %-10s %-15s %-20s\n", 
                    i+1, 
                    expense.getCategory(), 
                    expense.getAmount(), 
                    expense.getCurrency(), 
                    expense.getDate().toString(),
                    expense.getPaymentMethod().getName());
        }
    }
    private static void viewMonthlySummary() {
        FinancialReport report = new FinancialReport();
        report.generateMonthlySummary(currentUser);
    }
    private static void bankingMenu() {
        while (true) {
            System.out.println("\n----- BANKING & PAYMENT METHODS -----");
            System.out.println("1. View Connected Accounts");
            System.out.println("2. Connect Bank Account");
            System.out.println("3. Set Default Payment Method");
            System.out.println("4. Make Transfer");
            System.out.println("5. Remove Payment Method");
            System.out.println("6. Back to Main Menu");
            System.out.print("Select an option: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }
            switch (choice) {
                case 1:
                    viewConnectedAccounts();
                    break;
                case 2:
                    connectBankAccount();
                    break;
                case 3:
                    setDefaultPaymentMethod();
                    break;
                case 4:
                    makeTransfer();
                    break;
                case 5:
                    removePaymentMethod();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private static void viewConnectedAccounts() {
        List<PaymentMethod> paymentMethods = persistenceLayer.getUserPaymentMethods(currentUser.getUsername());
        if (paymentMethods.isEmpty()) {
            System.out.println("You don't have any connected payment methods yet.");
            return;
        }
        System.out.println("\n----- YOUR CONNECTED ACCOUNTS -----");
        System.out.printf("%-5s %-15s %-20s %-12s %-10s\n", 
                "ID", "Type", "Name", "Last 4 Digits", "Default");
        System.out.println("----------------------------------------------------------");
        for (int i = 0; i < paymentMethods.size(); i++) {
            PaymentMethod method = paymentMethods.get(i);
            String last4 = "****";
            if (method.getCardNumber() != null && method.getCardNumber().length() >= 4) {
                last4 = method.getCardNumber().substring(method.getCardNumber().length() - 4);
            }
            System.out.printf("%-5d %-15s %-20s %-12s %-10s\n", 
                    i+1, 
                    method.getType(), 
                    method.getName(), 
                    last4,
                    method.isDefault() ? "Yes" : "No");
        }
    }
    private static void connectBankAccount() {
        System.out.println("\n----- CONNECT BANK ACCOUNT -----");
        System.out.print("Enter bank name: ");
        String bankName = scanner.nextLine();
        String cardNumber;
        while (true) {
            System.out.print("Enter card number (16 digits): ");
            cardNumber = scanner.nextLine();
            if (cardNumber.matches("\\d{16}")) {
                break;
            }
            System.out.println("Invalid card number! Must be 16 digits.");
        }
        String expiryDate;
        while (true) {
            System.out.print("Enter expiry date (MM/YY): ");
            expiryDate = scanner.nextLine().trim();
            if (expiryDate.endsWith(".")) {
                expiryDate = expiryDate.substring(0, expiryDate.length() - 1);
            }
            if (expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
                break;
            }
            System.out.println("Invalid expiry date format! Must be MM/YY (example: 10/27).");
        }
        String cvv;
        while (true) {
            System.out.print("Enter CVV (3 digits): ");
            cvv = scanner.nextLine();
            if (cvv.matches("\\d{3}")) {
                break;
            }
            System.out.println("Invalid CVV! Must be 3 digits.");
        }
        System.out.print("Make this your default payment method? (yes/no): ");
        String makeDefaultStr = scanner.nextLine();
        boolean makeDefault = makeDefaultStr.equalsIgnoreCase("yes");
        PaymentMethod paymentMethod = new PaymentMethod("bank_card", bankName, cardNumber, expiryDate, makeDefault);
        if (makeDefault) {
            persistenceLayer.clearDefaultPaymentMethods(currentUser.getUsername());
        }
        persistenceLayer.savePaymentMethod(currentUser.getUsername(), paymentMethod);
        System.out.println("Bank account connected successfully!");
    }
    private static void setDefaultPaymentMethod() {
        List<PaymentMethod> paymentMethods = persistenceLayer.getUserPaymentMethods(currentUser.getUsername());
        if (paymentMethods.isEmpty()) {
            System.out.println("You don't have any payment methods to set as default.");
            return;
        }
        viewConnectedAccounts();
        int methodId;
        while (true) {
            System.out.print("\nEnter the ID of the payment method to set as default (or 0 to cancel): ");
            try {
                methodId = Integer.parseInt(scanner.nextLine());
                if (methodId == 0) {
                    return;
                }
                if (methodId < 1 || methodId > paymentMethods.size()) {
                    System.out.println("Invalid ID. Please enter a valid payment method ID.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        persistenceLayer.clearDefaultPaymentMethods(currentUser.getUsername());
        PaymentMethod selectedMethod = paymentMethods.get(methodId - 1);
        selectedMethod.setDefault(true);
        persistenceLayer.updatePaymentMethod(currentUser.getUsername(), methodId - 1, selectedMethod);
        System.out.println(selectedMethod.getName() + " set as default payment method!");
    }
    private static void makeTransfer() {
        List<PaymentMethod> paymentMethods = persistenceLayer.getUserPaymentMethods(currentUser.getUsername());
        if (paymentMethods.size() < 2) {
            System.out.println("You need at least two payment methods to make a transfer.");
            return;
        }
        viewConnectedAccounts();
        int fromId;
        while (true) {
            System.out.print("\nEnter the ID of the source account (or 0 to cancel): ");
            try {
                fromId = Integer.parseInt(scanner.nextLine());
                if (fromId == 0) {
                    return;
                }
                if (fromId < 1 || fromId > paymentMethods.size()) {
                    System.out.println("Invalid ID. Please enter a valid payment method ID.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        int toId;
        while (true) {
            System.out.print("Enter the ID of the destination account (or 0 to cancel): ");
            try {
                toId = Integer.parseInt(scanner.nextLine());
                if (toId == 0) {
                    return;
                }
                if (toId < 1 || toId > paymentMethods.size() || toId == fromId) {
                    System.out.println("Invalid ID. Please enter a valid payment method ID different from the source.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        double amount;
        while (true) {
            System.out.print("Enter transfer amount: ");
            try {
                amount = Double.parseDouble(scanner.nextLine());
                if (amount <= 0) {
                    System.out.println("Amount must be greater than zero.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        System.out.print("Enter currency (USD, EUR, GBP, etc.): ");
        String currency = scanner.nextLine();
        PaymentMethod fromMethod = paymentMethods.get(fromId - 1);
        PaymentMethod toMethod = paymentMethods.get(toId - 1);
        System.out.println("\nTransfer details:");
        System.out.println("From: " + fromMethod.getName());
        System.out.println("To: " + toMethod.getName());
        System.out.println("Amount: " + amount + " " + currency);
        System.out.print("\nConfirm transfer? (yes/no): ");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("yes")) {
            Transaction transaction = new Transaction(
                    "transfer",
                    amount,
                    currency,
                    new Date(),
                    "Transfer from " + fromMethod.getName() + " to " + toMethod.getName(),
                    fromMethod,
                    toMethod
            );
            persistenceLayer.saveTransaction(currentUser.getUsername(), transaction);
            System.out.println("Transfer completed successfully!");
        } else {
            System.out.println("Transfer cancelled.");
        }
    }
    private static void removePaymentMethod() {
        List<PaymentMethod> paymentMethods = persistenceLayer.getUserPaymentMethods(currentUser.getUsername());
        if (paymentMethods.isEmpty()) {
            System.out.println("You don't have any payment methods to remove.");
            return;
        }
        viewConnectedAccounts();
        int methodId;
        while (true) {
            System.out.print("\nEnter the ID of the payment method to remove (or 0 to cancel): ");
            try {
                methodId = Integer.parseInt(scanner.nextLine());
                if (methodId == 0) {
                    return;
                }
                if (methodId < 1 || methodId > paymentMethods.size()) {
                    System.out.println("Invalid ID. Please enter a valid payment method ID.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        PaymentMethod selectedMethod = paymentMethods.get(methodId - 1);
        if (selectedMethod.isDefault()) {
            System.out.println("Warning: This is your default payment method. Removing it will require setting a new default.");
        }
        System.out.print("Are you sure you want to remove this payment method? (yes/no): ");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("yes")) {
            persistenceLayer.removePaymentMethod(currentUser.getUsername(), methodId - 1);
            System.out.println("Payment method removed successfully!");
            if (selectedMethod.isDefault() && persistenceLayer.getUserPaymentMethods(currentUser.getUsername()).size() > 0) {
                System.out.println("You need to set a new default payment method.");
                setDefaultPaymentMethod();
            }
        } else {
            System.out.println("Removal cancelled.");
        }
    }
    private static void reportingMenu() {
        while (true) {
            System.out.println("\n----- FINANCIAL REPORTS -----");
            System.out.println("1. Monthly Income/Expense Summary");
            System.out.println("2. Asset Distribution Report");
            System.out.println("3. Budget Performance Report");
            System.out.println("4. Zakat Report");
            System.out.println("5. Back to Main Menu");
            System.out.print("Select an option: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }
            switch (choice) {
                case 1:
                    monthlyFinancialSummary();
                    break;
                case 2:
                    assetDistributionReport();
                    break;
                case 3:
                    budgetPerformanceReport();
                    break;
                case 4:
                    zakatReport();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private static void monthlyFinancialSummary() {
        FinancialReport report = new FinancialReport();
        report.generateMonthlySummary(currentUser);
    }
    private static void assetDistributionReport() {
        FinancialReport report = new FinancialReport();
        report.generateAssetDistributionReport(currentUser);
    }
    private static void budgetPerformanceReport() {
        FinancialReport report = new FinancialReport();
        report.generateBudgetPerformanceReport(currentUser);
    }
    private static void zakatReport() {
        FinancialReport report = new FinancialReport();
        report.generateZakatReport(currentUser);
    }
    private static PaymentMethod selectPaymentMethod() {
        List<PaymentMethod> paymentMethods = persistenceLayer.getUserPaymentMethods(currentUser.getUsername());
        if (paymentMethods.isEmpty()) {
            System.out.println("You don't have any payment methods set up yet.");
            System.out.print("Would you like to add one now? (yes/no): ");
            String addNew = scanner.nextLine();
            if (addNew.equalsIgnoreCase("yes")) {
                connectBankAccount();
                paymentMethods = persistenceLayer.getUserPaymentMethods(currentUser.getUsername());
                if (paymentMethods.isEmpty()) {
                    return null;
                }
            } else {
                return null;
            }
        }
        for (PaymentMethod method : paymentMethods) {
            if (method.isDefault()) {
                System.out.println("Using default payment method: " + method.getName());
                return method;
            }
        }
        System.out.println("\n----- SELECT PAYMENT METHOD -----");
        System.out.printf("%-5s %-15s %-20s %-12s\n", 
                "ID", "Type", "Name", "Last 4 Digits");
        System.out.println("-------------------------------------------");
        for (int i = 0; i < paymentMethods.size(); i++) {
            PaymentMethod method = paymentMethods.get(i);
            String last4 = "****";
            if (method.getCardNumber() != null && method.getCardNumber().length() >= 4) {
                last4 = method.getCardNumber().substring(method.getCardNumber().length() - 4);
            }
            System.out.printf("%-5d %-15s %-20s %-12s\n", 
                    i+1, 
                    method.getType(), 
                    method.getName(), 
                    last4);
        }
        int methodId;
        while (true) {
            System.out.print("\nSelect payment method (or 0 to cancel): ");
            try {
                methodId = Integer.parseInt(scanner.nextLine());
                if (methodId == 0) {
                    return null;
                }
                if (methodId < 1 || methodId > paymentMethods.size()) {
                    System.out.println("Invalid ID. Please enter a valid payment method ID.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return paymentMethods.get(methodId - 1);
    }
    private static String getCurrentUserMainCurrency() {
        List<Asset> assets = persistenceLayer.getUserAssets(currentUser.getUsername());
        if (!assets.isEmpty()) {
            return assets.get(0).getCurrency();
        }
        return "USD";  
    }
}
