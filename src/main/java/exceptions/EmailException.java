/**
 * @author alexanderweiss
 * @date 05.12.2015
 */
package exceptions;

public class EmailException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8513348757471330966L;

	public EmailException() {
		// TODO Auto-generated constructor stub
	}

	public EmailException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public EmailException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public EmailException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public EmailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
