package pr;

/**
 * Wyjątki związane z:
 * - zaokrąglaniem i zakresem typu double
 * - z nieodwracalnością macierzy.
 */
public class KnownException extends RuntimeException {

    public KnownException(String message) {
        super(message);
    }

    public KnownException(String message, Throwable cause) {
        super(message, cause);
    }

    public KnownException(Throwable cause) {
        super(cause);
    }

}
