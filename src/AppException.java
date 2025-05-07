
public class AppException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    private int errorCode;
    
    public AppException(String message) {
        super(message);
        this.errorCode = 0;
    }
    
    public AppException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public AppException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = 0;
    }
    
    public AppException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public int getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    
    public static AppException createAuthenticationException(String message) {
        return new AppException(message, 1001);
    }
    
    public static AppException createValidationException(String message) {
        return new AppException(message, 2001);
    }
    
    public static AppException createDataException(String message) {
        return new AppException(message, 3001);
    }
    
    public static AppException createBusinessRuleException(String message) {
        return new AppException(message, 4001);
    }
    
    @Override
    public String toString() {
        if (errorCode != 0) {
            return "AppException [errorCode=" + errorCode + ", message=" + getMessage() + "]";
        } else {
            return "AppException [message=" + getMessage() + "]";
        }
    }
}
