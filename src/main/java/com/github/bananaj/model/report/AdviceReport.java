package com.github.bananaj.model.report;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.model.JSONParser;

/**
 * Get recent feedback based on a campaign's statistics.
 *
 */
public class AdviceReport implements JSONParser {
	private String type;
	private String message;

	public AdviceReport() {

	}

	public AdviceReport(JSONObject abuse) {
		parse(null, abuse);
	}


	@Override
	public void parse(MailChimpConnection connection, JSONObject entity) {
		type = entity.getString("type");
		message = entity.getString("message");
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
