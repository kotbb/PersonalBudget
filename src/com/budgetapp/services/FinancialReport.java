package com.budgetapp.services;

import com.budgetapp.models.Expense;
import com.budgetapp.models.Income;
import com.budgetapp.models.User;
import com.budgetapp.utils.DateUtils;

import java.io.Serializable;
import java.util.*;

public class FinancialReport implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public void generateMonthlyReport(User user, Date month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(month);
        
        int targetMonth = cal.get(Calendar.MONTH);
        int targetYear = cal.get(Calendar.YEAR);
        
        // Filter incomes and expenses for the specified month
        List<Income> monthlyIncomes = new ArrayList<>();
        for (Income income : user.getIncomes()) {
            cal.setTime(income.getDate());
            if (cal.get(Calendar.MONTH) == targetMonth && cal.get(Calendar.YEAR) == targetYear) {
                monthlyIncomes.add(income);
            }
        }
        
        List<Expense> monthlyExpenses = new ArrayList<>();
        for (Expense expense : user.getExpenses()) {
            cal.setTime(expense.getDate());
            if (cal.get(Calendar.MONTH) == targetMonth && cal.get(Calendar.YEAR) == targetYear) {
                monthlyExpenses.add(expense);
            }
        }
        
        // Calculate total income and expenses
        double totalIncome = monthlyIncomes.stream()
                .mapToDouble(Income::getAmount)
                .sum();
        
        double totalExpense = monthlyExpenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
        
        // Group expenses by category
        Map<String, Double> expensesByCategory = new HashMap<>();
        for (Expense expense : monthlyExpenses) {
            String category = expense.getCategory();
            expensesByCategory.put(category, 
                    expensesByCategory.getOrDefault(category, 0.0) + expense.getAmount());
        }
        
        // Print the report
        cal.setTime(month);
        String monthName = new String[] {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        }[cal.get(Calendar.MONTH)];
        
        System.out.println("\n=== Monthly Financial Report for " + monthName + " " + targetYear + " ===");
        
        System.out.println("\n--- Income Summary ---");
        System.out.println("Total Income: $" + String.format("%.2f", totalIncome));
        
        if (!monthlyIncomes.isEmpty()) {
            System.out.println("\nIncome Breakdown:");
            System.out.println("----------------------------------------------------------");
            System.out.printf("%-20s %-15s %-15s\n", "Source", "Amount", "Date");
            System.out.println("----------------------------------------------------------");
            
            for (Income income : monthlyIncomes) {
                System.out.printf("%-20s $%-14.2f %-15s\n", 
                    income.getSource(), 
                    income.getAmount(),
                    DateUtils.formatDate(income.getDate()));
            }
            System.out.println("----------------------------------------------------------");
        }
        
        System.out.println("\n--- Expense Summary ---");
        System.out.println("Total Expenses: $" + String.format("%.2f", totalExpense));
        
        if (!monthlyExpenses.isEmpty()) {
            System.out.println("\nExpense Breakdown by Category:");
            System.out.println("----------------------------------------------------------");
            System.out.printf("%-20s %-15s %-15s\n", "Category", "Amount", "% of Total");
            System.out.println("----------------------------------------------------------");
            
            for (Map.Entry<String, Double> entry : expensesByCategory.entrySet()) {
                double percentage = (entry.getValue() / totalExpense) * 100;
                System.out.printf("%-20s $%-14.2f %.1f%%\n", 
                    entry.getKey(), 
                    entry.getValue(),
                    percentage);
            }
            System.out.println("----------------------------------------------------------");
            
            System.out.println("\nExpense Details:");
            System.out.println("----------------------------------------------------------");
            System.out.printf("%-20s %-15s %-15s\n", "Category", "Amount", "Date");
            System.out.println("----------------------------------------------------------");
            
            for (Expense expense : monthlyExpenses) {
                System.out.printf("%-20s $%-14.2f %-15s\n", 
                    expense.getCategory(), 
                    expense.getAmount(),
                    DateUtils.formatDate(expense.getDate()));
            }
            System.out.println("----------------------------------------------------------");
        }
        
        System.out.println("\n--- Summary ---");
        double netCashFlow = totalIncome - totalExpense;
        System.out.println("Net Cash Flow: $" + String.format("%.2f", netCashFlow));
        System.out.println("Savings Rate: " + String.format("%.1f", 
                totalIncome > 0 ? (netCashFlow / totalIncome) * 100 : 0) + "%");
    }
    
    public void generateCategorySummary(User user, String category) {
        // Filter expenses for the specified category
        List<Expense> categoryExpenses = new ArrayList<>();
        for (Expense expense : user.getExpenses()) {
            if (expense.getCategory().equalsIgnoreCase(category)) {
                categoryExpenses.add(expense);
            }
        }
        
        if (categoryExpenses.isEmpty()) {
            System.out.println("No expenses found for category: " + category);
            return;
        }
        
        // Calculate total expense for the category
        double totalCategoryExpense = categoryExpenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
        
        // Calculate total of all expenses
        double totalAllExpenses = user.getExpenses().stream()
                .mapToDouble(Expense::getAmount)
                .sum();
        
        // Calculate percentage of total
        double percentageOfTotal = (totalCategoryExpense / totalAllExpenses) * 100;
        
        // Group expenses by month
        Map<String, Double> expensesByMonth = new TreeMap<>();
        Calendar cal = Calendar.getInstance();
        
        for (Expense expense : categoryExpenses) {
            cal.setTime(expense.getDate());
            String monthYear = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
            expensesByMonth.put(monthYear, 
                    expensesByMonth.getOrDefault(monthYear, 0.0) + expense.getAmount());
        }
        
        // Print the report
        System.out.println("\n=== Category Summary for '" + category + "' ===");
        System.out.println("Total Spent: $" + String.format("%.2f", totalCategoryExpense));
        System.out.println("Percentage of All Expenses: " + String.format("%.1f", percentageOfTotal) + "%");
        
        System.out.println("\nMonthly Breakdown:");
        System.out.println("----------------------------------------------------------");
        System.out.printf("%-15s %-15s\n", "Month", "Amount");
        System.out.println("----------------------------------------------------------");
        
        for (Map.Entry<String, Double> entry : expensesByMonth.entrySet()) {
            System.out.printf("%-15s $%-14.2f\n", 
                entry.getKey(), 
                entry.getValue());
        }
        System.out.println("----------------------------------------------------------");
        
        System.out.println("\nExpense Details:");
        System.out.println("----------------------------------------------------------");
        System.out.printf("%-15s %-15s\n", "Amount", "Date");
        System.out.println("----------------------------------------------------------");
        
        for (Expense expense : categoryExpenses) {
            System.out.printf("$%-14.2f %-15s\n", 
                expense.getAmount(),
                DateUtils.formatDate(expense.getDate()));
        }
        System.out.println("----------------------------------------------------------");
    }
}
