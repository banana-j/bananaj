/**
 * @author alexanderweiss
 * @date 12.12.2015
 */
package com.github.bananaj.model.report;

import java.time.ZonedDateTime;

import org.json.JSONObject;

import com.github.bananaj.utils.DateConverter;

/**
 * Class for representing the opens of a campaign
 * @author alexanderweiss
 *
 */
public class Open {

	private int opensTotal;
	private int uniqueOpens;
	private double openRate;
	private ZonedDateTime lastOpen;
	
	
	public Open(JSONObject jsonObj) {
		opensTotal = jsonObj.getInt("opens_total");
		uniqueOpens = jsonObj.getInt("unique_opens");
		openRate = jsonObj.getDouble("open_rate");
		lastOpen = DateConverter.fromISO8601(jsonObj.getString("last_open"));
	}

	/**
	 * @return The total number of opens for a campaign.
	 */
	public int getOpensTotal() {
		return opensTotal;
	}

	/**
	 * @return The total number of unique opens.
	 */
	public int getUniqueOpens() {
		return uniqueOpens;
	}

	/**
	 * @return The number of unique opens divided by the total number of successful deliveries.
	 */
	public double getOpenRate() {
		return openRate;
	}

	/**
	 * @return The date and time of the last recorded open.
	 */
	public ZonedDateTime getLastOpen() {
		return lastOpen;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return
				"Opens:" + System.lineSeparator() +
				"    Total: " + getOpensTotal() + System.lineSeparator() +
				"    Unique: " +  getUniqueOpens() + System.lineSeparator() +
				"    Open Rate: " +  getOpenRate() + System.lineSeparator() +
				"    Last Open: " + DateConverter.toLocalString(getLastOpen());
	}

}
