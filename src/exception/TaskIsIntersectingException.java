package exception;

public class TaskIsIntersectingException extends RuntimeException {
    public TaskIsIntersectingException(String message) {
        super(message);
    }
}
