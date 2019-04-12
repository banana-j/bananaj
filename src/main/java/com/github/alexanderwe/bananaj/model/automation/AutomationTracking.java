package com.github.alexanderwe.bananaj.model.automation;

import org.json.JSONObject;

public class AutomationTracking {
	
	private boolean opens;
	private boolean htmlClicks;
	private boolean textClicks;
	private boolean goalTracking;
	private boolean ecomm360;
	private String googleAnalytics;
	private String clicktale;
	//private Salesforce salesforce;
	//private Capsule capsule;

	public AutomationTracking() {

	}

	public AutomationTracking(JSONObject tracking) {
		this.opens = tracking.getBoolean("opens");
		this.htmlClicks = tracking.getBoolean("html_clicks");
		this.textClicks = tracking.getBoolean("text_clicks");
		this.goalTracking = tracking.getBoolean("goal_tracking");
		this.ecomm360 = tracking.getBoolean("ecomm360");
		this.googleAnalytics = tracking.getString("google_analytics");
		this.clicktale = tracking.getString("clicktale");
	}

	/**
	 * Whether to track opens. Defaults to true
	 * @return
	 */
	public boolean isOpens() {
		return opens;
	}

	/**
	 * Whether to track clicks in the HTML version of the Automation. Defaults to true
	 * @return
	 */
	public boolean isHtmlClicks() {
		return htmlClicks;
	}

	/**
	 * Whether to track clicks in the plain-text version of the Automation. Defaults to true
	 * @return
	 */
	public boolean isTextClicks() {
		return textClicks;
	}

	/**
	 * Whether to enable Goal tracking
	 * @return
	 */
	public boolean isGoalTracking() {
		return goalTracking;
	}

	/**
	 * Whether to enable eCommerce360 tracking
	 * @return
	 */
	public boolean isEcomm360() {
		return ecomm360;
	}

	/**
	 * The custom slug for Google Analytics tracking (max of 50 bytes)
	 * @return
	 */
	public String getGoogleAnalytics() {
		return googleAnalytics;
	}

	/**
	 * The custom slug for ClickTale tracking (max of 50 bytes)
	 * @return
	 */
	public String getClicktale() {
		return clicktale;
	}

}
