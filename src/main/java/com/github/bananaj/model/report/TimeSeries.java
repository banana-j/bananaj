package com.github.bananaj.model.report;

import java.time.ZonedDateTime;

import org.json.JSONObject;

import com.github.bananaj.utils.DateConverter;

/**
 * An hourly breakdown of the performance of the campaign over the first 24 hours.
 *
 */
public class TimeSeries {

	private ZonedDateTime timestamp;
	private int emailsSent;
	private int uniqueOpens;
	private int recipientsClicks;

	public TimeSeries(JSONObject jsonObj) {
		timestamp = DateConverter.fromISO8601(jsonObj.getString("timestamp"));
		emailsSent = jsonObj.getInt("emails_sent");
		uniqueOpens = jsonObj.getInt("unique_opens");
		recipientsClicks = jsonObj.getInt("recipients_clicks");
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
	public int getEmailsSent() {
		return emailsSent;
	}

	/**
	 * @return The number of unique opens in the timeseries
	 */
	public int getUniqueOpens() {
		return uniqueOpens;
	}

	/**
	 * @return The number of clicks in the timeseries.
	 */
	public int getRecipientsClicks() {
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
