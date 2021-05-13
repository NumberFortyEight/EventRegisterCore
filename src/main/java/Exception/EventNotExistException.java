package Exception;

public class EventNotExistException extends RuntimeException{
    public EventNotExistException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
