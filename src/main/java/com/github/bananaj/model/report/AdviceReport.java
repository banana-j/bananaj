package com.github.bananaj.model.report;

import org.json.JSONObject;

/**
 * Get recent feedback based on a campaign's statistics.
 *
 */
public class AdviceReport {
	private String type;
	private String message;

	public AdviceReport(JSONObject abuse) {
		type = abuse.getString("type");
		message = abuse.getString("message");
	}

	/**
	 * @return The sentiment type for a feedback message.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return The advice message.
	 */
	public String getMessage() {
		return message;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Campaign Advice: (" + getType() + ") " + getMessage();
	}

}
