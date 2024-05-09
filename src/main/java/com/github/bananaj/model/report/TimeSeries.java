package com.github.bananaj.model.report;

import java.time.ZonedDateTime;

import org.json.JSONObject;

import com.github.bananaj.utils.DateConverter;
import com.github.bananaj.utils.JSONObjectCheck;

/**
 * An hourly breakdown of the performance of the campaign over the first 24 hours.
 *
 */
public class TimeSeries {

	private ZonedDateTime timestamp;
	private Integer emailsSent;
	private Integer uniqueOpens;
	private Integer recipientsClicks;

	public TimeSeries(JSONObject jsonObj) {
		JSONObjectCheck jObj = new JSONObjectCheck(jsonObj);
		timestamp = jObj.getISO8601Date("timestamp");
		emailsSent = jObj.getInt("emails_sent");
		uniqueOpens = jObj.getInt("unique_opens");
		recipientsClicks = jObj.getInt("recipients_clicks");
	}

	/**
	 * @return The date and time for the series
	 */
	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	/**
	 * @return The number of emails sent in the timeseries.
	 */
	public Integer getEmailsSent() {
		return emailsSent;
	}

	/**
	 * @return The number of unique opens in the timeseries
	 */
	public Integer getUniqueOpens() {
		return uniqueOpens;
	}

	/**
	 * @return The number of clicks in the timeseries.
	 */
	public Integer getRecipientsClicks() {
		return recipientsClicks;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return
				"Time Seriese: " + DateConverter.toLocalString(getTimestamp()) + System.lineSeparator() +
				"    Emails Sent: " + getEmailsSent() + System.lineSeparator() +
				"    Unique Opens: " + getUniqueOpens() + System.lineSeparator() +
				"    Recipients Clicks: " + getRecipientsClicks();
	}

}
