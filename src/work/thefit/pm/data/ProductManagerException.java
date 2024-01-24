package work.thefit.pm.data;

public class ProductManagerException extends Exception {
    public ProductManagerException() {
    }

    public ProductManagerException(String message) {
        super(message);
    }

    public ProductManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductManagerException(Throwable cause) {
        super(cause);
    }
}
