/**
 * @author alexanderweiss
 * @date 12.11.2015
 */
package com.github.bananaj.model.batch;

/**
 * The status of the batch call. <a href="https://mailchimp.com/developer/marketing/api/batch-operations/get-batch-operation-status/" target="MailchimpAPIDoc">Learn more</a> about the batch operation status.
 * Possible values: "pending", "preprocessing", "started", "finalizing", or
 * "finished".
 *
 */
public enum BatchStatus {

	PENDING("pending"),
	PREPROCESSING("preprocessing"),
	STARTED("started"),
	FINALIZING("finalizing"),
	FINISHED("finished");
	
	
	private String stringRepresentation;
	
	BatchStatus(String stringRepresentation ) {
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
