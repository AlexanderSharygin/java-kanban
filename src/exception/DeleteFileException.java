package exception;

public class DeleteFileException extends RuntimeException {
    public DeleteFileException(String message) {
        super(message);
    }
}