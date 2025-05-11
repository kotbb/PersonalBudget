# 💸 Personal Budget Management Application

A Java-based console application for managing personal finances—track expenses, set budgets, manage incomes, and analyze spending patterns effortlessly.

---

## 🚀 Features

- 👤 **User Management**: Secure signup and login with OTP Verification  
- 💰 **Budget Tracking**: Set spending limits with alerts when exceeding them  
- 📊 **Expense Management**: Record expenses organized by categories  
- 💵 **Income Tracking**: Manage income from multiple sources  
- 📈 **Financial Analysis**: Visual breakdown of spending patterns  
- 💳 **Payment Methods**: Track balances across multiple payment methods  

---

## 🛠 Tools Used

| Tool         | Purpose                                         |
|--------------|--------------------------------------------------|
| [draw.io](https://draw.io)     | System architecture and flow diagrams        |
| [Structurizr](https://structurizr.com) | C4-based software architecture modeling      |
| [IntelliJ IDEA](https://www.jetbrains.com/idea/) | Java development environment                   |
| [Canva](https://canva.com)     | UI mockups and project presentation assets  |
| [ChatGPT](https://chat.openai.com) | Planning, code generation, and debugging help |

---

## 📦 Getting Started

### ✅ Prerequisites

- Java installed (JDK 8 or higher)
- Basic familiarity with command line interfaces

---

## 🧭 Usage Guide

### 🔐 First-Time Setup

1. Run the application
2. Choose `Sign Up` from the main menu
3. Enter user details and verify with displayed OTP
4. On successful login, access the main dashboard

### 📌 Setting Up a Budget

1. Navigate to `Budget Management`
2. Select `Set Budget for Category`
3. Enter category name and spending limit

### 🧾 Tracking Expenses

1. Go to `Expense Management`
2. Select `Add Expense`
3. Provide category, amount, date, and payment method

### 📊 Analyzing Spending

1. Open `Budget Management`
2. Choose `Analyze Spending`
3. View a percentage-based breakdown of your expenses

---

## 🧱 Project Structure
com.budgetapp/
├── Main.java # Application entry point
├── models/ # User, Budget, Expense, etc.
├── services/ # Auth, persistence, business logic
├── exceptions/ # Custom exception handling
└── utils/ # Date parsing, validations, etc.

---

## 💾 Data Persistence

All financial data is stored locally using Java serialization in:
budget_app_data.ser

---

## 📃 License

This project is for educational purposes as part of a Software Engineering course. Feel free to fork and build on top of it!

---
