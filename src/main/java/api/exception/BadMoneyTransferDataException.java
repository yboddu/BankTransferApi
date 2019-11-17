package api.exception;

public class BadMoneyTransferDataException extends RuntimeException{
    public BadMoneyTransferDataException() {
        super();
    }

    public BadMoneyTransferDataException(String message) {
        super(message);
    }

    public BadMoneyTransferDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
