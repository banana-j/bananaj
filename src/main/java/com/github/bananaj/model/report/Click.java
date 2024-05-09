/**
 * @author alexanderweiss
 * @date 20.11.2015
 */
package com.github.bananaj.model.report;

import java.time.ZonedDateTime;

import org.json.JSONObject;

import com.github.bananaj.utils.DateConverter;
import com.github.bananaj.utils.JSONObjectCheck;

/**
 * Class for representing clicks for a campaign
 * @author alexanderweiss
 *
 */
public class Click {
	
	private Integer clicksTotal;
	private Integer uniqueClicks;
	private Integer uniqueSubscriberClicks;
	private Double clickRate;
	private ZonedDateTime lastClick;

	public Click(JSONObject click) {
		JSONObjectCheck jObj = new JSONObjectCheck(click);
		clicksTotal = jObj.getInt("clicks_total");
		uniqueClicks = jObj.getInt("unique_clicks");
		uniqueSubscriberClicks = jObj.getInt("unique_subscriber_clicks");
		clickRate = jObj.getDouble("click_rate");
		lastClick = jObj.getISO8601Date("last_click");
	}

	/**
	 * @return The total number of clicks for the campaign.
	 */
	public Integer getClicksTotal() {
		return clicksTotal;
	}

	/**
	 * @return The total number of unique clicks for links across a campaign.
	 */
	public Integer getUniqueClicks() {
		return uniqueClicks;
	}

	/**
	 * @return The total number of subscribers who clicked on a campaign.
	 */
	public Integer getUniqueSubscriberClicks() {
		return uniqueSubscriberClicks;
	}

	/**
	 * @return The number of unique clicks divided by the total number of successful deliveries.
	 */
	public Double getClickRate() {
		return clickRate;
	}

	/**
	 * @return The date and time of the last recorded click for the campaign.
	 */
	public ZonedDateTime getLastClick() {
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
				"    Last click: " + (getLastClick()!=null ? DateConverter.toLocalString(getLastClick()) : "");
	}

}
