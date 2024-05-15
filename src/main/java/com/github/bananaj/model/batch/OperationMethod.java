package com.github.bananaj.model.batch;

/**
 * The HTTP method to use for an operation. Possible values: "GET", "POST", "PUT", "PATCH", or "DELETE".
 */
public enum OperationMethod {

	GET("GET"), 
	POST("POST"),
	PUT("PUT"),
	PATCH("PATCH"),
	DELETE("DELETE");

	private String stringRepresentation;
	
	OperationMethod(String stringRepresentation ) {
		setStringRepresentation(stringRepresentation);
	}

	@Override
	public String toString() {
		return stringRepresentation;
	}

	/**
	 * @param stringRepresentation Set the stringRepresentation for the enum constant.
	 */
	private void setStringRepresentation(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}
	
}
