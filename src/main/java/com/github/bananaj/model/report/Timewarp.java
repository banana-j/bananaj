package com.github.bananaj.model.report;

import java.time.ZonedDateTime;

import org.json.JSONObject;

import com.github.bananaj.utils.DateConverter;
import com.github.bananaj.utils.JSONObjectCheck;

/**
 * An hourly breakdown of sends, opens, and clicks if a campaign is sent using timewarp.
 *
 */
public class Timewarp {

	private Integer gmtOffset;
	private Integer opens;
	private ZonedDateTime lastOpen;
	private Integer uniqueOpens;
	private Integer clicks;
	private ZonedDateTime lastClick;
	private Integer uniqueClicks;
	private Integer bounces;

	public Timewarp(JSONObject entity) {
		JSONObjectCheck jObj = new JSONObjectCheck(entity);
		gmtOffset = jObj.getInt("gmt_offset");
		opens = jObj.getInt("opens");
		lastOpen = jObj.getISO8601Date("last_open");
		uniqueOpens = jObj.getInt("unique_opens");
		clicks = jObj.getInt("clicks");
		lastClick = jObj.getISO8601Date("last_click");
		uniqueClicks = jObj.getInt("unique_clicks");
		bounces = jObj.getInt("bounces");
	}

	/**
	 * @return For campaigns sent with timewarp, the time zone group the member is apart of.
	 */
	public Integer getGmtOffset() {
		return gmtOffset;
	}

	/**
	 * @return The number of opens.
	 */
	public Integer getOpens() {
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
	public Integer getUniqueOpens() {
		return uniqueOpens;
	}

	/**
	 * @return The number of clicks.
	 */
	public Integer getClicks() {
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
	public Integer getUniqueClicks() {
		return uniqueClicks;
	}

	/**
	 * @return The number of bounces.
	 */
	public Integer getBounces() {
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
