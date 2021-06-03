package exceptions;

public class EventNotExistException extends RuntimeException {
    public EventNotExistException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

    public EventNotExistException(String errorMessage) {
        super(errorMessage);
    }
}
