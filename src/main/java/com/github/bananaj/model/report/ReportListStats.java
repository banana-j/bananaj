/**
 * @author alexanderweiss
 * @date 13.12.2015
 */
package com.github.bananaj.model.report;

import org.json.JSONObject;

import com.github.bananaj.utils.JSONObjectCheck;

/**
 * Class for representing list stats in a report object
 * @author alexanderweiss
 *
 */
public class ReportListStats {

	private Double subRate;
	private Double unsubRate;
	private Double openRate;
	private Double clickRate;

	public ReportListStats(JSONObject entity) {
		JSONObjectCheck jObj = new JSONObjectCheck(entity);
		this.subRate = jObj.getDouble("sub_rate");
		this.unsubRate = jObj.getDouble("unsub_rate");
		this.openRate = jObj.getDouble("open_rate");
		this.clickRate = jObj.getDouble("click_rate");
	}

	/**
	 * @return The average number of subscriptions per month for the list.
	 */
	public Double getSubRate() {
		return subRate;
	}

	/**
	 * @return The average number of unsubscriptions per month for the list.
	 */
	public Double getUnsubRate() {
		return unsubRate;
	}

	/**
	 * @return The average open rate (a percentage represented as a number between 0 and 100) per campaign for the list.
	 */
	public Double getOpenRate() {
		return openRate;
	}

	/**
	 * @return The average click rate (a percentage represented as a number between 0 and 100) per campaign for the list.
	 */
	public Double getClickRate() {
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
