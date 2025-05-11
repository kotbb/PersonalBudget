# Personal Budget Management Application

A Java-based CLI application for managing personal finances, tracking expenses, setting budgets, and analyzing spending patterns.

## Features

- 👤 **User Management**: Secure signup and login with OTP verification
- 💰 **Budget Tracking**: Set spending limits with alerts when exceeding limits
- 📊 **Expense Management**: Record expenses with category-based organization
- 💵 **Income Tracking**: Track income from multiple sources
- 📈 **Financial Analysis**: Get detailed breakdown of spending patterns
- 💳 **Payment Methods**: Manage multiple payment methods and their balances

## Getting Started

### Prerequisites

- Java
- Basic understanding of command line interfaces

## Usage Guide

### First-time Setup

1. Run the application and select "Sign Up" from the main menu
2. Enter your details and verify with the OTP code shown
3. After logging in, you'll be taken to the dashboard

### Setting Up a Budget

1. From the dashboard, select "Budget Management"
2. Choose "Set Budget for Category"
3. Enter a category name and budget limit

### Tracking Expenses

1. From the dashboard, select "Expense Management"
2. Choose "Add Expense"
3. Enter category, amount, date, and payment method

### Analyzing Spending

1. From the dashboard, select "Budget Management"
2. Choose "Analyze Spending" to see a breakdown of expenses
3. View the percentage distribution across categories

## Project Structure

- `com.budgetapp.Main`: Application entry point and UI
- `com.budgetapp.models`: Data models (User, Budget, Expense, etc.)
- `com.budgetapp.services`: Service classes for authentication, persistence, etc.
- `com.budgetapp.exceptions`: Custom exception classes
- `com.budgetapp.utils`: Utility classes for dates, validation, etc.

## Data Persistence

All data is stored locally using Java serialization in the file "budget_app_data.ser". This ensures your financial data persists between sessions.

## Recent Improvements

- Budget reset now properly clears all related expenses
- Enhanced budget alerts show warnings directly on dashboard
- Improved spending analysis with visual distribution charts

## License

This project is licensed under the MIT License - see the LICENSE file for details.
