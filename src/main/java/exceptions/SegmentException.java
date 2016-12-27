package exceptions;

/**
 * Created by alexanderweiss on 27.12.16.
 */
public class SegmentException extends Exception {

    public SegmentException() {
        super("A member can only be added to a static segment.");
    }

    public SegmentException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public SegmentException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    public SegmentException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public SegmentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }
}
