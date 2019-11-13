package com.github.alexanderwe.bananaj.model.report;

import java.time.ZonedDateTime;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.utils.DateConverter;

public class SplitInfo {

	private int bounces;
	private int abuseReports;
	private int unsubs;
	private int recipientClicks;
	private int forwards;
	private int forwardsOpens;
	private int opens;
	private ZonedDateTime lastOpen;
	private int uniqueOpens;

	public SplitInfo(JSONObject jsonObj) {
		bounces = jsonObj.getInt("bounces");
		abuseReports = jsonObj.getInt("abuse_reports");
		unsubs = jsonObj.getInt("unsubs");
		recipientClicks = jsonObj.getInt("recipient_clicks");
		forwards = jsonObj.getInt("forwards");
		forwardsOpens = jsonObj.getInt("forwards_opens");
		opens = jsonObj.getInt("opens");
		lastOpen = DateConverter.fromISO8601(jsonObj.getString("last_open"));
		uniqueOpens = jsonObj.getInt("unique_opens");
	}

	/**
	 * @return Bounces for Campaign.
	 */
	public int getBounces() {
		return bounces;
	}

	/**
	 * @return Abuse reports for Campaign.
	 */
	public int getAbuseReports() {
		return abuseReports;
	}

	/**
	 * @return Unsubscribes for Campaign.
	 */
	public int getUnsubs() {
		return unsubs;
	}

	/**
	 * @return Recipient Clicks for Campaign.
	 */
	public int getRecipientClicks() {
		return recipientClicks;
	}

	/**
	 * @return Forwards for Campaign.
	 */
	public int getForwards() {
		return forwards;
	}

	/**
	 * @return Opens from forwards for Campaign.
	 */
	public int getForwardsOpens() {
		return forwardsOpens;
	}

	/**
	 * @return Opens for Campaign.
	 */
	public int getOpens() {
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
	public int getUniqueOpens() {
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
