/**
 * @author alexanderweiss
 * @date 13.12.2015
 */
package com.github.bananaj.model.report;

import org.json.JSONObject;

/**
 * Class for representing the average campaign statistics for your industry
 * @author alexanderweiss
 *
 */
public class IndustryStats {

	private String type;
	private double openRate;
	private double clickRate;
	private double bounceRate;
	private double unopenRate;
	private double unsubscribeRate;
	private double abuseRate;

	public IndustryStats(JSONObject jsonObj) {
		type = jsonObj.getString("type");
		openRate = jsonObj.getDouble("open_rate");
		clickRate = jsonObj.getDouble("click_rate");
		bounceRate = jsonObj.getDouble("bounce_rate");
		unopenRate = jsonObj.getDouble("unopen_rate");
		unsubscribeRate = jsonObj.getDouble("unsub_rate");
		abuseRate = jsonObj.getDouble("abuse_rate");
	}

	/**
	 * @return The type of business industry associated with your account. For example: retail, education, etc.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return The industry open rate.
	 */
	public double getOpenRate() {
		return openRate;
	}

	/**
	 * @return The industry click rate.
	 */
	public double getClickRate() {
		return clickRate;
	}

	/**
	 * @return The industry bounce rate.
	 */
	public double getBounceRate() {
		return bounceRate;
	}

	/**
	 * @return The industry unopened rate.
	 */
	public double getUnopenRate() {
		return unopenRate;
	}

	/**
	 * @return The industry unsubscribe rate.
	 */
	public double getUnsubscribeRate() {
		return unsubscribeRate;
	}

	/**
	 * @return The industry abuse rate.
	 */
	public double getAbuseRate() {
		return abuseRate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return
		"Industry Stats:" + System.lineSeparator() +
		"    Type: " + getType() + System.lineSeparator() +
		"    Open Rate: " + getOpenRate() + System.lineSeparator() +
		"    Click Rate: " + getClickRate() + System.lineSeparator() +
		"    Bounce Rate: " + getBounceRate() + System.lineSeparator() +
		"    Unopen Rate: " + getUnopenRate() + System.lineSeparator() +
		"    Unsubscribe Rate: " + getUnsubscribeRate() + System.lineSeparator() +
		"    Abuse Rate: " + getAbuseRate();
	}
	
}
