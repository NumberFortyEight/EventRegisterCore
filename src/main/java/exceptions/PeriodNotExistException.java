package exceptions;

public class PeriodNotExistException extends RuntimeException {
    public PeriodNotExistException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

    public PeriodNotExistException(String errorMessage) {
        super(errorMessage);
    }
}
