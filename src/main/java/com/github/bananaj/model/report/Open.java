/**
 * @author alexanderweiss
 * @date 12.12.2015
 */
package com.github.bananaj.model.report;

import java.time.ZonedDateTime;

import org.json.JSONObject;

import com.github.bananaj.utils.DateConverter;
import com.github.bananaj.utils.JSONObjectCheck;

/**
 * Class for representing the opens of a campaign
 * @author alexanderweiss
 *
 */
public class Open {

	private Integer opensTotal;
	private Integer uniqueOpens;
	private Double openRate;
	private ZonedDateTime lastOpen;
	
	
	public Open(JSONObject jsonObj) {
		JSONObjectCheck jObj = new JSONObjectCheck(jsonObj);
		opensTotal = jObj.getInt("opens_total");
		uniqueOpens = jObj.getInt("unique_opens");
		openRate = jObj.getDouble("open_rate");
		lastOpen = jObj.getISO8601Date("last_open");
	}

	/**
	 * @return The total number of opens for a campaign.
	 */
	public Integer getOpensTotal() {
		return opensTotal;
	}

	/**
	 * @return The total number of unique opens.
	 */
	public Integer getUniqueOpens() {
		return uniqueOpens;
	}

	/**
	 * @return The number of unique opens divided by the total number of successful deliveries.
	 */
	public Double getOpenRate() {
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
