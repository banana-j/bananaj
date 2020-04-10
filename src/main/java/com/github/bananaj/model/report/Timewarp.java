package com.github.bananaj.model.report;

import java.time.ZonedDateTime;

import org.json.JSONObject;

import com.github.bananaj.utils.DateConverter;

/**
 * An hourly breakdown of sends, opens, and clicks if a campaign is sent using timewarp.
 *
 */
public class Timewarp {

	private int gmtOffset;
	private int opens;
	private ZonedDateTime lastOpen;
	private int uniqueOpens;
	private int clicks;
	private ZonedDateTime lastClick;
	private int uniqueClicks;
	private int bounces;

	public Timewarp(JSONObject jsonObj) {
		gmtOffset = jsonObj.getInt("gmt_offset");
		opens = jsonObj.getInt("opens");
		lastOpen = DateConverter.fromISO8601(jsonObj.getString("last_open"));
		uniqueOpens = jsonObj.getInt("unique_opens");
		clicks = jsonObj.getInt("clicks");
		lastClick = DateConverter.fromISO8601(jsonObj.getString("last_click"));
		uniqueClicks = jsonObj.getInt("unique_clicks");
		bounces = jsonObj.getInt("bounces");
	}

	/**
	 * @return For campaigns sent with timewarp, the time zone group the member is apart of.
	 */
	public int getGmtOffset() {
		return gmtOffset;
	}

	/**
	 * @return The number of opens.
	 */
	public int getOpens() {
		return opens;
	}

	/**
	 * @return The date and time of the last open
	 */
	public ZonedDateTime getLastOpen() {
		return lastOpen;
	}

	/**
	 * @return The number of unique opens.
	 */
	public int getUniqueOpens() {
		return uniqueOpens;
	}

	/**
	 * @return The number of clicks.
	 */
	public int getClicks() {
		return clicks;
	}

	/**
	 * @return The date and time of the last click
	 */
	public ZonedDateTime getLastClick() {
		return lastClick;
	}

	/**
	 * @return The number of unique clicks.
	 */
	public int getUniqueClicks() {
		return uniqueClicks;
	}

	/**
	 * @return The number of bounces.
	 */
	public int getBounces() {
		return bounces;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return
				"Timewarp: " + getGmtOffset() + System.lineSeparator() +
				"    Opens: " + getOpens() + System.lineSeparator() +
				"    Last Open: " + DateConverter.toLocalString(getLastOpen()) + System.lineSeparator() +
				"    Unique Opens: " + getUniqueOpens() + System.lineSeparator() +
				"    Clicks: " + getClicks() + System.lineSeparator() +
				"    Last Click: " + DateConverter.toLocalString(getLastClick()) + System.lineSeparator() +
				"    Unique Clicks: " + getUniqueClicks() + System.lineSeparator() +
				"    Bounces: " + getBounces();
	}

}
