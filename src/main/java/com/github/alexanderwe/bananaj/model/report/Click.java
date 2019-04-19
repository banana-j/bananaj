/**
 * @author alexanderweiss
 * @date 20.11.2015
 */
package com.github.alexanderwe.bananaj.model.report;

import java.time.LocalDateTime;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.utils.DateConverter;

/**
 * Class for representing clicks for a campaign
 * @author alexanderweiss
 *
 */
public class Click {
	
	private int clicksTotal;
	private int uniqueClicks;
	private int uniqueSubscriberClicks;
	private double clickRate;
	private LocalDateTime lastClick;

	public Click(JSONObject jsonObj) {
		clicksTotal = jsonObj.getInt("clicks_total");
		uniqueClicks = jsonObj.getInt("unique_clicks");
		uniqueSubscriberClicks = jsonObj.getInt("unique_subscriber_clicks");
		clickRate = jsonObj.getDouble("click_rate");
		lastClick = DateConverter.getInstance().createDateFromISO8601(jsonObj.getString("last_click"));
	}

	/**
	 * @return The total number of clicks for the campaign.
	 */
	public int getClicksTotal() {
		return clicksTotal;
	}

	/**
	 * @return The total number of unique clicks for links across a campaign.
	 */
	public int getUniqueClicks() {
		return uniqueClicks;
	}

	/**
	 * @return The total number of subscribers who clicked on a campaign.
	 */
	public int getUniqueSubscriberClicks() {
		return uniqueSubscriberClicks;
	}

	/**
	 * @return The number of unique clicks divided by the total number of successful deliveries.
	 */
	public double getClickRate() {
		return clickRate;
	}

	/**
	 * @return The date and time of the last recorded click for the campaign.
	 */
	public LocalDateTime getLastClick() {
		return lastClick;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return
				"Clicks: " + System.lineSeparator() +
				"    Clicks total: " + getClicksTotal() + System.lineSeparator() +
				"    Unique clicks: " + getUniqueClicks() + System.lineSeparator() +
				"    Unique subscriber links: " + getUniqueSubscriberClicks() + System.lineSeparator() +
				"    Click rate: " + getClickRate() + System.lineSeparator() +
				"    Last click: " + getLastClick();
	}

}
