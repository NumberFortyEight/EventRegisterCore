package exceptions;

public class WrongDateException extends RuntimeException {
    public WrongDateException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

    public WrongDateException(String errorMessage) {
        super(errorMessage);
    }
}
