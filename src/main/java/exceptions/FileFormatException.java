package exceptions;

/**
 * Created by alexanderweiss on 11.02.16.
 */


public class FileFormatException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -8513348757471330966L;

    public FileFormatException() {
        super("Invalid file format. Only use: .xls, .xlxs, .csv or .txt");
    }

    public FileFormatException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public FileFormatException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    public FileFormatException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public FileFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

}
