package com.investwise;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public class PersistenceLayer {
    private static final Map<String, User> users = new HashMap<>();
    private static final Map<String, List<Asset>> userAssets = new HashMap<>();
    private static final Map<String, List<Budget>> userBudgets = new HashMap<>();
    private static final Map<String, List<Income>> userIncomes = new HashMap<>();
    private static final Map<String, List<Expense>> userExpenses = new HashMap<>();
    private static final Map<String, List<PaymentMethod>> userPaymentMethods = new HashMap<>();
    private static final Map<String, List<Goal>> userGoals = new HashMap<>();
    private static final Map<String, List<Transaction>> userTransactions = new HashMap<>();
    private static final Map<String, List<Notification>> userNotifications = new HashMap<>();
    public PersistenceLayer() {
    }
    public void saveUser(User user) {
        users.put(user.getUsername(), user);
        userAssets.putIfAbsent(user.getUsername(), new ArrayList<>());
        userBudgets.putIfAbsent(user.getUsername(), new ArrayList<>());
        userIncomes.putIfAbsent(user.getUsername(), new ArrayList<>());
        userExpenses.putIfAbsent(user.getUsername(), new ArrayList<>());
        userPaymentMethods.putIfAbsent(user.getUsername(), new ArrayList<>());
        userGoals.putIfAbsent(user.getUsername(), new ArrayList<>());
        userTransactions.putIfAbsent(user.getUsername(), new ArrayList<>());
        userNotifications.putIfAbsent(user.getUsername(), new ArrayList<>());
    }
    public void updateUser(User user) {
        if (users.containsKey(user.getUsername())) {
            users.put(user.getUsername(), user);
        }
    }
    public User getUser(String username) {
        return users.get(username);
    }
    public Map<String, User> getAllUsers() {
        return new HashMap<>(users);
    }
    public void deleteUser(String username) {
        users.remove(username);
        userAssets.remove(username);
        userBudgets.remove(username);
        userIncomes.remove(username);
        userExpenses.remove(username);
        userPaymentMethods.remove(username);
        userGoals.remove(username);
        userTransactions.remove(username);
        userNotifications.remove(username);
    }
    public void saveAsset(String username, Asset asset) {
        if (!userAssets.containsKey(username)) {
            userAssets.put(username, new ArrayList<>());
        }
        userAssets.get(username).add(asset);
    }
    public List<Asset> getUserAssets(String username) {
        return userAssets.getOrDefault(username, new ArrayList<>());
    }
    public void updateAsset(String username, int index, Asset asset) {
        if (userAssets.containsKey(username) && index >= 0 && index < userAssets.get(username).size()) {
            userAssets.get(username).set(index, asset);
        }
    }
    public void removeAsset(String username, int index) {
        if (userAssets.containsKey(username) && index >= 0 && index < userAssets.get(username).size()) {
            userAssets.get(username).remove(index);
        }
    }
    public List<Asset> getZakatEligibleAssets(String username) {
        if (!userAssets.containsKey(username)) {
            return new ArrayList<>();
        }
        return userAssets.get(username).stream()
                .filter(Asset::isZakatEligible)
                .collect(Collectors.toList());
    }
    public void saveBudget(String username, Budget budget) {
        if (!userBudgets.containsKey(username)) {
            userBudgets.put(username, new ArrayList<>());
        }
        userBudgets.get(username).add(budget);
    }
    public List<Budget> getUserBudgets(String username) {
        return userBudgets.getOrDefault(username, new ArrayList<>());
    }
    public void updateBudget(String username, int index, Budget budget) {
        if (userBudgets.containsKey(username) && index >= 0 && index < userBudgets.get(username).size()) {
            userBudgets.get(username).set(index, budget);
        }
    }
    public void removeBudget(String username, int index) {
        if (userBudgets.containsKey(username) && index >= 0 && index < userBudgets.get(username).size()) {
            userBudgets.get(username).remove(index);
        }
    }
    public double calculateSpentAmount(String username, String category) {
        if (!userExpenses.containsKey(username)) {
            return 0;
        }
        return userExpenses.get(username).stream()
                .filter(expense -> expense.getCategory().equalsIgnoreCase(category))
                .mapToDouble(Expense::getAmount)
                .sum();
    }
    public void saveIncome(String username, Income income) {
        if (!userIncomes.containsKey(username)) {
            userIncomes.put(username, new ArrayList<>());
        }
        userIncomes.get(username).add(income);
    }
    public List<Income> getUserIncomes(String username) {
        return userIncomes.getOrDefault(username, new ArrayList<>());
    }
    public void updateIncome(String username, int index, Income income) {
        if (userIncomes.containsKey(username) && index >= 0 && index < userIncomes.get(username).size()) {
            userIncomes.get(username).set(index, income);
        }
    }
    public void removeIncome(String username, int index) {
        if (userIncomes.containsKey(username) && index >= 0 && index < userIncomes.get(username).size()) {
            userIncomes.get(username).remove(index);
        }
    }
    public void saveExpense(String username, Expense expense) {
        if (!userExpenses.containsKey(username)) {
            userExpenses.put(username, new ArrayList<>());
        }
        userExpenses.get(username).add(expense);
    }
    public List<Expense> getUserExpenses(String username) {
        return userExpenses.getOrDefault(username, new ArrayList<>());
    }
    public void updateExpense(String username, int index, Expense expense) {
        if (userExpenses.containsKey(username) && index >= 0 && index < userExpenses.get(username).size()) {
            userExpenses.get(username).set(index, expense);
        }
    }
    public void removeExpense(String username, int index) {
        if (userExpenses.containsKey(username) && index >= 0 && index < userExpenses.get(username).size()) {
            userExpenses.get(username).remove(index);
        }
    }
    public void savePaymentMethod(String username, PaymentMethod paymentMethod) {
        if (!userPaymentMethods.containsKey(username)) {
            userPaymentMethods.put(username, new ArrayList<>());
        }
        userPaymentMethods.get(username).add(paymentMethod);
    }
    public List<PaymentMethod> getUserPaymentMethods(String username) {
        return userPaymentMethods.getOrDefault(username, new ArrayList<>());
    }
    public void updatePaymentMethod(String username, int index, PaymentMethod paymentMethod) {
        if (userPaymentMethods.containsKey(username) && index >= 0 && index < userPaymentMethods.get(username).size()) {
            userPaymentMethods.get(username).set(index, paymentMethod);
        }
    }
    public void removePaymentMethod(String username, int index) {
        if (userPaymentMethods.containsKey(username) && index >= 0 && index < userPaymentMethods.get(username).size()) {
            userPaymentMethods.get(username).remove(index);
        }
    }
    public void clearDefaultPaymentMethods(String username) {
        if (userPaymentMethods.containsKey(username)) {
            for (PaymentMethod method : userPaymentMethods.get(username)) {
                method.setDefault(false);
            }
        }
    }
    public void saveGoal(String username, Goal goal) {
        if (!userGoals.containsKey(username)) {
            userGoals.put(username, new ArrayList<>());
        }
        userGoals.get(username).add(goal);
    }
    public List<Goal> getUserGoals(String username) {
        return userGoals.getOrDefault(username, new ArrayList<>());
    }
    public void updateGoal(String username, int index, Goal goal) {
        if (userGoals.containsKey(username) && index >= 0 && index < userGoals.get(username).size()) {
            userGoals.get(username).set(index, goal);
        }
    }
    public void removeGoal(String username, int index) {
        if (userGoals.containsKey(username) && index >= 0 && index < userGoals.get(username).size()) {
            userGoals.get(username).remove(index);
        }
    }
    public void saveTransaction(String username, Transaction transaction) {
        if (!userTransactions.containsKey(username)) {
            userTransactions.put(username, new ArrayList<>());
        }
        userTransactions.get(username).add(transaction);
    }
    public List<Transaction> getUserTransactions(String username) {
        return userTransactions.getOrDefault(username, new ArrayList<>());
    }
    public void saveNotification(String username, Notification notification) {
        if (!userNotifications.containsKey(username)) {
            userNotifications.put(username, new ArrayList<>());
        }
        userNotifications.get(username).add(notification);
    }
    public List<Notification> getUserNotifications(String username) {
        return userNotifications.getOrDefault(username, new ArrayList<>());
    }
    public void markNotificationAsRead(String username, int index) {
        if (userNotifications.containsKey(username) && index >= 0 && index < userNotifications.get(username).size()) {
            userNotifications.get(username).get(index).markAsRead();
        }
    }
    public List<Notification> getUnreadNotifications(String username) {
        if (!userNotifications.containsKey(username)) {
            return new ArrayList<>();
        }
        return userNotifications.get(username).stream()
                .filter(notification -> !notification.isRead())
                .collect(Collectors.toList());
    }
    public void clearAllData() {
        users.clear();
        userAssets.clear();
        userBudgets.clear();
        userIncomes.clear();
        userExpenses.clear();
        userPaymentMethods.clear();
        userGoals.clear();
        userTransactions.clear();
        userNotifications.clear();
    }
}
