package com.investwise;

import java.util.Date;

public class Asset {
    private String type;
    private double value;
    private String currency;
    private boolean zakatEligible;
    private String description;
    private Date dateAdded;
    
    public Asset(String type, double value, String currency, boolean zakatEligible, String description, Date dateAdded) {
        this.type = type;
        this.value = value;
        this.currency = currency;
        this.zakatEligible = zakatEligible;
        this.description = description;
        this.dateAdded = dateAdded;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isZakatEligible() {
        return zakatEligible;
    }

    public void setZakatEligible(boolean zakatEligible) {
        this.zakatEligible = zakatEligible;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
    
    public double calculateZakat() {
        if (zakatEligible) {
            return value * 0.025;
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return "Asset [type=" + type + ", value=" + value + " " + currency + 
                ", zakatEligible=" + zakatEligible + ", dateAdded=" + dateAdded + "]";
    }
}
