package exception;

public class InverseException extends RuntimeException {

    public InverseException() {
    }

    public InverseException(String message) {
        super(message);
    }

    public InverseException(String message, Throwable cause) {
        super(message, cause);
    }

    public InverseException(Throwable cause) {
        super(cause);
    }
}
