package com.github.bananaj.model.report;

import java.time.ZonedDateTime;

import org.json.JSONObject;

import com.github.bananaj.utils.DateConverter;
import com.github.bananaj.utils.JSONObjectCheck;

public class SplitInfo {

	private Integer bounces;
	private Integer abuseReports;
	private Integer unsubs;
	private Integer recipientClicks;
	private Integer forwards;
	private Integer forwardsOpens;
	private Integer opens;
	private ZonedDateTime lastOpen;
	private Integer uniqueOpens;

	public SplitInfo(JSONObject jsonObj) {
		JSONObjectCheck jObj = new JSONObjectCheck(jsonObj);
		bounces = jObj.getInt("bounces");
		abuseReports = jObj.getInt("abuse_reports");
		unsubs = jObj.getInt("unsubs");
		recipientClicks = jObj.getInt("recipient_clicks");
		forwards = jObj.getInt("forwards");
		forwardsOpens = jObj.getInt("forwards_opens");
		opens = jObj.getInt("opens");
		lastOpen = jObj.getISO8601Date("last_open");
		uniqueOpens = jObj.getInt("unique_opens");
	}

	/**
	 * @return Bounces for Campaign.
	 */
	public Integer getBounces() {
		return bounces;
	}

	/**
	 * @return Abuse reports for Campaign.
	 */
	public Integer getAbuseReports() {
		return abuseReports;
	}

	/**
	 * @return Unsubscribes for Campaign.
	 */
	public Integer getUnsubs() {
		return unsubs;
	}

	/**
	 * @return Recipient Clicks for Campaign.
	 */
	public Integer getRecipientClicks() {
		return recipientClicks;
	}

	/**
	 * @return Forwards for Campaign.
	 */
	public Integer getForwards() {
		return forwards;
	}

	/**
	 * @return Opens from forwards for Campaign.
	 */
	public Integer getForwardsOpens() {
		return forwardsOpens;
	}

	/**
	 * @return Opens for Campaign.
	 */
	public Integer getOpens() {
		return opens;
	}

	/**
	 * @return The last open for Campaign.
	 */
	public ZonedDateTime getLastOpen() {
		return lastOpen;
	}

	/**
	 * @return Unique opens for Campaign.
	 */
	public Integer getUniqueOpens() {
		return uniqueOpens;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return
				"    Bounces: " + getBounces() + System.lineSeparator() +
				"    Abuse Reports: " + getAbuseReports() + System.lineSeparator() +
				"    Unsubs: " + getUnsubs() + System.lineSeparator() +
				"    Recipient Clicks: " + getRecipientClicks() + System.lineSeparator() +
				"    Forwards: " + getForwards() + System.lineSeparator() +
				"    Forwards Opens: " + getForwardsOpens() + System.lineSeparator() +
				"    Opens: " + getOpens() + System.lineSeparator() +
				"    Last Open: " + DateConverter.toLocalString(getLastOpen()) + System.lineSeparator() +
				"    Unique Opens: " + getUniqueOpens();
	}

}
