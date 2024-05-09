/**
 * @author alexanderweiss
 * @date 13.12.2015
 */
package com.github.bananaj.model.report;

import org.json.JSONObject;

import com.github.bananaj.utils.JSONObjectCheck;

/**
 * Class for representing the average campaign statistics for your industry
 * @author alexanderweiss
 *
 */
public class IndustryStats {

	private String type;
	private Double openRate;
	private Double clickRate;
	private Double bounceRate;
	private Double unopenRate;
	private Double unsubscribeRate;
	private Double abuseRate;

	public IndustryStats(JSONObject jsonObj) {
		JSONObjectCheck jObj = new JSONObjectCheck(jsonObj);
		type = jObj.getString("type");
		openRate = jObj.getDouble("open_rate");
		clickRate = jObj.getDouble("click_rate");
		bounceRate = jObj.getDouble("bounce_rate");
		unopenRate = jObj.getDouble("unopen_rate");
		unsubscribeRate = jObj.getDouble("unsub_rate");
		abuseRate = jObj.getDouble("abuse_rate");
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
	public Double getOpenRate() {
		return openRate;
	}

	/**
	 * @return The industry click rate.
	 */
	public Double getClickRate() {
		return clickRate;
	}

	/**
	 * @return The industry bounce rate.
	 */
	public Double getBounceRate() {
		return bounceRate;
	}

	/**
	 * @return The industry unopened rate.
	 */
	public Double getUnopenRate() {
		return unopenRate;
	}

	/**
	 * @return The industry unsubscribe rate.
	 */
	public Double getUnsubscribeRate() {
		return unsubscribeRate;
	}

	/**
	 * @return The industry abuse rate.
	 */
	public Double getAbuseRate() {
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
