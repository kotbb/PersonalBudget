
import java.util.Date;
public class Budget {
    private String category;
    private double amount;
    private String currency;
    private String period;  
    private Date startDate;
    private Date endDate;
    public Budget(String category, double amount, String currency, String period) {
        this.category = category;
        this.amount = amount;
        this.currency = currency;
        this.period = period;
        this.startDate = new Date();  
        this.endDate = calculateEndDate();
    }
    public Budget(String category, double amount, String currency, String period, Date startDate, Date endDate) {
        this.category = category;
        this.amount = amount;
        this.currency = currency;
        this.period = period;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public String getPeriod() {
        return period;
    }
    public void setPeriod(String period) {
        this.period = period;
        this.endDate = calculateEndDate();
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    private Date calculateEndDate() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(startDate);
        if (period.equalsIgnoreCase("weekly")) {
            cal.add(java.util.Calendar.DAY_OF_YEAR, 7);
        } else if (period.equalsIgnoreCase("monthly")) {
            cal.add(java.util.Calendar.MONTH, 1);
        } else if (period.equalsIgnoreCase("yearly")) {
            cal.add(java.util.Calendar.YEAR, 1);
        }
        return cal.getTime();
    }
    public boolean isActive() {
        Date now = new Date();
        return now.after(startDate) && now.before(endDate);
    }
    @Override
    public String toString() {
        return "Budget [category=" + category + ", amount=" + amount + " " + currency + 
                ", period=" + period + ", active=" + isActive() + "]";
    }
}
