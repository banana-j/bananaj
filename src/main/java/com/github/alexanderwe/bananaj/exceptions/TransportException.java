/**
 * 
 */
package com.github.alexanderwe.bananaj.exceptions;

/**
 * @author USCRIGA
 *
 */
public class TransportException extends Exception {

	/**
	 * 
	 */
	public TransportException() {
		super("Mailchimp transport failure");
	}

	/**
	 * @param message the detail message 
	 */
	public TransportException(String message) {
		super(message);
	}

	/**
	 * @param cause the cause. (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public TransportException(Throwable cause) {
		super("Mailchimp transport failure", cause);
	}

	/**
	 * @param message the detail message 
	 * @param cause cause the cause. (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public TransportException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message the detail message 
	 * @param cause cause the cause. (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 * @param enableSuppression whether or not suppression is enabled or disabled
	 * @param writableStackTrace whether or not the stack trace should be writable
	 */
	public TransportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
