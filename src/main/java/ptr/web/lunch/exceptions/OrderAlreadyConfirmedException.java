package ptr.web.lunch.exceptions;

public class OrderAlreadyConfirmedException extends RuntimeException {

    private static final long serialVersionUID = 1;

    public OrderAlreadyConfirmedException(String msg) {
        super(msg);
    }

    public OrderAlreadyConfirmedException(Throwable cause) {
        super(cause);
    }

    public OrderAlreadyConfirmedException(String msg, Throwable cause) {
        super(msg, cause);
    }


}
