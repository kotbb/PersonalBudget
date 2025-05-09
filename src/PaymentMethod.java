package com.investwise;
public class PaymentMethod {
    private String type;  
    private String name;  
    private String cardNumber;  
    private String expiryDate;  
    private String accountNumber;  
    private boolean isDefault;  
    public PaymentMethod(String type, String name, String cardNumber, String expiryDate, boolean isDefault) {
        this.type = type;
        this.name = name;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.isDefault = isDefault;
    }
    public PaymentMethod(String type, String name, String accountNumber, boolean isDefault) {
        this.type = type;
        this.name = name;
        this.accountNumber = accountNumber;
        this.isDefault = isDefault;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCardNumber() {
        return cardNumber;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    public String getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public boolean isDefault() {
        return isDefault;
    }
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        String lastFourDigits = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + lastFourDigits;
    }
    @Override
    public String toString() {
        if (type.equals("bank_card")) {
            return "PaymentMethod [type=" + type + ", name=" + name + 
                    ", cardNumber=" + getMaskedCardNumber() + ", expiryDate=" + expiryDate + 
                    ", isDefault=" + isDefault + "]";
        } else {
            return "PaymentMethod [type=" + type + ", name=" + name + 
                    ", accountNumber=****" + (accountNumber != null ? accountNumber.substring(Math.max(0, accountNumber.length() - 4)) : "") + 
                    ", isDefault=" + isDefault + "]";
        }
    }
}
