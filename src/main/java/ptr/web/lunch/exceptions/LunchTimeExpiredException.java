package ptr.web.lunch.exceptions;

public class LunchTimeExpiredException extends RuntimeException {

    private static final long serialVersionUID = 1;

    public LunchTimeExpiredException(String msg) {
        super(msg);
    }

    public LunchTimeExpiredException(Throwable cause) {
        super(cause);
    }

    public LunchTimeExpiredException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
