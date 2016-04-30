package pr;

public class InvertException extends RuntimeException {

    public InvertException() {
    }

    public InvertException(String message) {
        super(message);
    }

    public InvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvertException(Throwable cause) {
        super(cause);
    }
}
