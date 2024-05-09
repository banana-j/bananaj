package com.github.bananaj.model;

import org.json.JSONObject;

import com.github.bananaj.model.report.Ecommerce;
import com.github.bananaj.utils.JSONObjectCheck;

/**
 * For sent campaigns, a summary of opens, clicks, and e-commerce data.
 *
 */
public class ReportSummary {

	private Integer opens;
	private Integer uniqueOpens;
	private Double openRate;
	private Integer clicks;
	private Integer subscriberClicks;
	private Double clickRate;
	private Ecommerce ecommerce;

	public ReportSummary(JSONObject summary) {
		JSONObjectCheck jObj = new JSONObjectCheck(summary);
		opens = jObj.getInt("opens");
		uniqueOpens = jObj.getInt("unique_opens");
		openRate = jObj.getDouble("open_rate");
		clicks = jObj.getInt("clicks");
		subscriberClicks = jObj.getInt("subscriber_clicks");
		clickRate = jObj.getDouble("click_rate");
		
		if (summary.has("ecommerce")) {
			ecommerce = new Ecommerce(summary.getJSONObject("ecommerce"));
		}
	}

	public ReportSummary() {

	}

	/**
	 * The total number of opens for a campaign
	 */
	public Integer getOpens() {
		return opens;
	}

	/**
	 * The number of unique opens
	 */
	public Integer getUniqueOpens() {
		return uniqueOpens;
	}

	/**
	 * The number of unique opens divided by the total number of successful deliveries
	 */
	public Double getOpenRate() {
		return openRate;
	}

	/**
	 * The total number of clicks for a campaign
	 */
	public Integer getClicks() {
		return clicks;
	}

	/**
	 * The number of unique clicks
	 */
	public Integer getSubscriberClicks() {
		return subscriberClicks;
	}

	/**
	 * The number of unique clicks divided by the total number of successful deliveries
	 */
	public Double getClickRate() {
		return clickRate;
	}

	/**
	 * E-Commerce stats for a campaign
	 * @return null or E-Commerce stats
	 */
	public Ecommerce getEcommerce() {
		return ecommerce;
	}

	@Override
	public String toString() {
		return 
				"ReportSummary:" + System.lineSeparator() +
				"    Opens: " + opens + System.lineSeparator() +
				"    Unique Opens: " + uniqueOpens + System.lineSeparator() +
				"    Open Rate: " + openRate + System.lineSeparator() +
				"    Clicks: " + clicks + System.lineSeparator() +
				"    Subscriber Clicks: " + subscriberClicks + System.lineSeparator() +
				"    click Rate: " + clickRate + 
				(ecommerce != null ? System.lineSeparator() + ecommerce.toString() : "");
	}

}
