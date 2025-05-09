
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class FinancialReport {
    private PersistenceLayer persistenceLayer;
    public FinancialReport() {
        this.persistenceLayer = new PersistenceLayer();
    }
    public double calculateZakat(User user) {
        double totalZakatEligibleAssets = getTotalZakatEligibleAssets(user);
        return totalZakatEligibleAssets * 0.025;  
    }
    public double getTotalZakatEligibleAssets(User user) {
        List<Asset> assets = persistenceLayer.getUserAssets(user.getUsername());
        double total = 0;
        for (Asset asset : assets) {
            if (asset.isZakatEligible()) {
                total += asset.getValue();
            }
        }
        return total;
    }
    public void generateMonthlySummary(User user) {
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        List<Income> incomes = persistenceLayer.getUserIncomes(user.getUsername());
        List<Expense> expenses = persistenceLayer.getUserExpenses(user.getUsername());
        double totalIncome = 0;
        double totalExpense = 0;
        Map<String, Double> incomeBySource = new HashMap<>();
        Map<String, Double> expenseByCategory = new HashMap<>();
        for (Income income : incomes) {
            Calendar incomeCal = Calendar.getInstance();
            incomeCal.setTime(income.getDate());
            if (incomeCal.get(Calendar.MONTH) == currentMonth && 
                incomeCal.get(Calendar.YEAR) == currentYear) {
                totalIncome += income.getAmount();
                String source = income.getSource();
                if (incomeBySource.containsKey(source)) {
                    incomeBySource.put(source, incomeBySource.get(source) + income.getAmount());
                } else {
                    incomeBySource.put(source, income.getAmount());
                }
            }
        }
        for (Expense expense : expenses) {
            Calendar expenseCal = Calendar.getInstance();
            expenseCal.setTime(expense.getDate());
            if (expenseCal.get(Calendar.MONTH) == currentMonth && 
                expenseCal.get(Calendar.YEAR) == currentYear) {
                totalExpense += expense.getAmount();
                String category = expense.getCategory();
                if (expenseByCategory.containsKey(category)) {
                    expenseByCategory.put(category, expenseByCategory.get(category) + expense.getAmount());
                } else {
                    expenseByCategory.put(category, expense.getAmount());
                }
            }
        }
        String[] months = {"January", "February", "March", "April", "May", "June", 
                          "July", "August", "September", "October", "November", "December"};
        System.out.println("\n----- MONTHLY FINANCIAL SUMMARY -----");
        System.out.println("Month: " + months[currentMonth] + " " + currentYear);
        System.out.println("\nINCOME SUMMARY:");
        System.out.println("Total Income: " + totalIncome + " " + (incomes.isEmpty() ? "USD" : incomes.get(0).getCurrency()));
        if (!incomeBySource.isEmpty()) {
            System.out.println("\nIncome by Source:");
            for (Map.Entry<String, Double> entry : incomeBySource.entrySet()) {
                System.out.printf("%-20s: %.2f\n", entry.getKey(), entry.getValue());
            }
        }
        System.out.println("\nEXPENSE SUMMARY:");
        System.out.println("Total Expenses: " + totalExpense + " " + (expenses.isEmpty() ? "USD" : expenses.get(0).getCurrency()));
        if (!expenseByCategory.isEmpty()) {
            System.out.println("\nExpenses by Category:");
            for (Map.Entry<String, Double> entry : expenseByCategory.entrySet()) {
                System.out.printf("%-20s: %.2f\n", entry.getKey(), entry.getValue());
            }
        }
        System.out.println("\nNET INCOME: " + (totalIncome - totalExpense) + 
                          " " + (incomes.isEmpty() ? "USD" : incomes.get(0).getCurrency()));
        if (totalIncome > totalExpense) {
            double savingsRate = ((totalIncome - totalExpense) / totalIncome) * 100;
            System.out.printf("\nSavings Rate: %.1f%%\n", savingsRate);
            if (savingsRate >= 20) {
                System.out.println("Great job! You're saving more than 20% of your income.");
            } else if (savingsRate >= 10) {
                System.out.println("You're doing well with saving. Try to increase your savings rate to 20%.");
            } else {
                System.out.println("Consider increasing your savings rate to at least 10-20% of your income.");
            }
        } else if (totalIncome < totalExpense) {
            System.out.println("\nWARNING: You're spending more than you earn this month!");
            System.out.println("Consider reducing expenses in the following categories:");
            expenseByCategory.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(3)
                .forEach(entry -> System.out.println("- " + entry.getKey() + ": " + entry.getValue()));
        }
    }
    public void generateAssetDistributionReport(User user) {
        List<Asset> assets = persistenceLayer.getUserAssets(user.getUsername());
        if (assets.isEmpty()) {
            System.out.println("You don't have any assets to generate a distribution report.");
            return;
        }
        double totalAssetValue = 0;
        Map<String, Double> assetsByType = new HashMap<>();
        for (Asset asset : assets) {
            totalAssetValue += asset.getValue();
            String type = asset.getType();
            if (assetsByType.containsKey(type)) {
                assetsByType.put(type, assetsByType.get(type) + asset.getValue());
            } else {
                assetsByType.put(type, asset.getValue());
            }
        }
        System.out.println("\n----- ASSET DISTRIBUTION REPORT -----");
        System.out.println("Total Asset Value: " + totalAssetValue + " " + assets.get(0).getCurrency());
        System.out.println("\nAsset Distribution by Type:");
        System.out.printf("%-15s %-15s %-10s %-15s\n", "Asset Type", "Value", "Currency", "Percentage");
        System.out.println("----------------------------------------------------");
        for (Map.Entry<String, Double> entry : assetsByType.entrySet()) {
            double percentage = (entry.getValue() / totalAssetValue) * 100;
            System.out.printf("%-15s %-15.2f %-10s %-15.2f%%\n", 
                    entry.getKey(), 
                    entry.getValue(), 
                    assets.get(0).getCurrency(),
                    percentage);
        }
        System.out.println("\nINSIGHTS:");
        if (assetsByType.size() <= 2) {
            System.out.println("- Consider diversifying your assets more. Having assets in different categories " +
                              "can reduce risk.");
        }
        for (Map.Entry<String, Double> entry : assetsByType.entrySet()) {
            double percentage = (entry.getValue() / totalAssetValue) * 100;
            if (percentage > 50) {
                System.out.println("- " + entry.getKey() + " makes up " + String.format("%.1f", percentage) + 
                                  "% of your portfolio. Consider diversifying to reduce concentration risk.");
                break;
            }
        }
        double zakatEligibleValue = getTotalZakatEligibleAssets(user);
        double zakatPercentage = (zakatEligibleValue / totalAssetValue) * 100;
        System.out.println("- " + String.format("%.1f", zakatPercentage) + 
                          "% of your assets are eligible for zakat calculation.");
    }
    public void generateBudgetPerformanceReport(User user) {
        List<Budget> budgets = persistenceLayer.getUserBudgets(user.getUsername());
        if (budgets.isEmpty()) {
            System.out.println("You don't have any budgets to generate a performance report.");
            return;
        }
        System.out.println("\n----- BUDGET PERFORMANCE REPORT -----");
        System.out.printf("%-15s %-15s %-15s %-15s %-15s\n", 
                "Category", "Budgeted", "Spent", "Remaining", "Usage %");
        System.out.println("-------------------------------------------------------------------------");
        int exceedCount = 0;
        int nearLimitCount = 0;
        for (Budget budget : budgets) {
            double spent = persistenceLayer.calculateSpentAmount(user.getUsername(), budget.getCategory());
            double remaining = budget.getAmount() - spent;
            double usagePercent = (spent / budget.getAmount()) * 100;
            System.out.printf("%-15s %-15.2f %-15.2f %-15.2f %-15.1f%%\n", 
                    budget.getCategory(), 
                    budget.getAmount(), 
                    spent, 
                    remaining, 
                    usagePercent);
            if (usagePercent > 100) {
                exceedCount++;
            } else if (usagePercent > 80) {
                nearLimitCount++;
            }
        }
        System.out.println("\nINSIGHTS:");
        if (exceedCount > 0) {
            System.out.println("- You have exceeded " + exceedCount + " budget(s). Consider adjusting your spending or " +
                              "revising these budget amounts if they're consistently exceeded.");
        }
        if (nearLimitCount > 0) {
            System.out.println("- You are close to the limit (>80%) on " + nearLimitCount + " budget(s). " +
                              "Monitor your spending in these categories closely.");
        }
        if (exceedCount == 0 && nearLimitCount == 0) {
            System.out.println("- Great job! You're staying within all your budget limits.");
        }
    }
    public void generateZakatReport(User user) {
        List<Asset> assets = persistenceLayer.getUserAssets(user.getUsername());
        if (assets.isEmpty()) {
            System.out.println("You don't have any assets to calculate zakat.");
            return;
        }
        List<Asset> eligibleAssets = persistenceLayer.getZakatEligibleAssets(user.getUsername());
        if (eligibleAssets.isEmpty()) {
            System.out.println("You don't have any zakat-eligible assets.");
            return;
        }
        double totalEligibleValue = getTotalZakatEligibleAssets(user);
        double zakatAmount = calculateZakat(user);
        System.out.println("\n----- ZAKAT REPORT -----");
        System.out.println("Total Zakat-Eligible Assets: " + totalEligibleValue + " " + eligibleAssets.get(0).getCurrency());
        System.out.println("Zakat Amount (2.5%): " + zakatAmount + " " + eligibleAssets.get(0).getCurrency());
        System.out.println("\nEligible Assets Breakdown:");
        System.out.printf("%-15s %-15s %-10s %-15s\n", 
                "Asset Type", "Value", "Currency", "Zakat Amount");
        System.out.println("-------------------------------------------------------");
        for (Asset asset : eligibleAssets) {
            double assetZakat = asset.calculateZakat();
            System.out.printf("%-15s %-15.2f %-10s %-15.2f\n", 
                    asset.getType(), 
                    asset.getValue(), 
                    asset.getCurrency(),
                    assetZakat);
        }
        System.out.println("\nINFORMATION:");
        System.out.println("- Zakat is calculated at 2.5% of eligible assets.");
        System.out.println("- Eligible assets typically include gold, silver, cash, stocks, and other liquid investments.");
        System.out.println("- Zakat is typically due once per year when your wealth reaches and maintains the nisab threshold.");
        System.out.println("- Consult with a religious authority for specific guidance on your zakat obligations.");
    }
}
