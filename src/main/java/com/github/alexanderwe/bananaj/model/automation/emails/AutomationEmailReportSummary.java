package com.github.alexanderwe.bananaj.model.automation.emails;

import org.json.JSONObject;

public class AutomationEmailReportSummary {

	private int opens;
	private int uniqueOpens;
	private double openRate;
	private int clicks;
	private int subscriberClicks;
	private double clickRate;

	public AutomationEmailReportSummary(JSONObject jsonObj) {
		opens = jsonObj.getInt("opens");
		uniqueOpens = jsonObj.getInt("unique_opens");
		openRate = jsonObj.getDouble("open_rate");
		clicks = jsonObj.getInt("clicks");
		subscriberClicks = jsonObj.getInt("subscriber_clicks");
		clickRate = jsonObj.getDouble("click_rate");
	}

	public AutomationEmailReportSummary() {

	}

	/**
	 * The total number of opens for a campaign
	 * @return
	 */
	public int getOpens() {
		return opens;
	}

	/**
	 * The number of unique opens
	 * @return
	 */
	public int getUniqueOpens() {
		return uniqueOpens;
	}

	/**
	 * The number of unique opens divided by the total number of successful deliveries
	 * @return
	 */
	public double getOpenRate() {
		return openRate;
	}

	/**
	 * The total number of clicks for an campaign
	 * @return
	 */
	public int getClicks() {
		return clicks;
	}

	/**
	 * The number of unique clicks
	 * @return
	 */
	public int getSubscriberClicks() {
		return subscriberClicks;
	}

	/**
	 * The number of unique clicks divided by the total number of successful deliveries
	 * @return
	 */
	public double getClickRate() {
		return clickRate;
	}

}
