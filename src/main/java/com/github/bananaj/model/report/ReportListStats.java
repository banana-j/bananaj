/**
 * @author alexanderweiss
 * @date 13.12.2015
 */
package com.github.bananaj.model.report;

import org.json.JSONObject;

/**
 * Class for representing list stats in a report object
 * @author alexanderweiss
 *
 */
public class ReportListStats {

	private double subRate;
	private double unsubRate;
	private double openRate;
	private double clickRate;

	public ReportListStats(JSONObject jsonObj) {
		this.subRate = jsonObj.getDouble("sub_rate");
		this.unsubRate = jsonObj.getDouble("unsub_rate");
		this.openRate = jsonObj.getDouble("open_rate");
		this.clickRate = jsonObj.getDouble("click_rate");
	}

	/**
	 * @return The average number of subscriptions per month for the list.
	 */
	public double getSubRate() {
		return subRate;
	}

	/**
	 * @return The average number of unsubscriptions per month for the list.
	 */
	public double getUnsubRate() {
		return unsubRate;
	}

	/**
	 * @return The average open rate (a percentage represented as a number between 0 and 100) per campaign for the list.
	 */
	public double getOpenRate() {
		return openRate;
	}

	/**
	 * @return The average click rate (a percentage represented as a number between 0 and 100) per campaign for the list.
	 */
	public double getClickRate() {
		return clickRate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return
				"Audience Statistics:" + System.lineSeparator() +
				"    Subscrib Rate: " + getSubRate() + System.lineSeparator() +
				"    Unsubscrib Rate: " + getUnsubRate() + System.lineSeparator() +
				"    Open Rate: " + getOpenRate() + System.lineSeparator() +
				"    Click Rate: " + getClickRate();
	}

}
