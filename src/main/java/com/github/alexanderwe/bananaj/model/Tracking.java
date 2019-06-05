package com.github.alexanderwe.bananaj.model;

import org.json.JSONObject;

public class Tracking {

	private boolean opens = true;
	private boolean htmlClicks = true;
	private boolean textClicks = true;
	private boolean goalTracking = false;
	private boolean ecomm360 = false;
	private String googleAnalytics;
	private String clicktale;
	//private Salesforce salesforce;
	//private Capsule capsule;

	public Tracking() {

	}

	public Tracking(JSONObject tracking) {
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

	/**
	 * @param opens the opens to set
	 */
	public void setOpens(boolean opens) {
		this.opens = opens;
	}

	/**
	 * @param htmlClicks the htmlClicks to set
	 */
	public void setHtmlClicks(boolean htmlClicks) {
		this.htmlClicks = htmlClicks;
	}

	/**
	 * @param textClicks the textClicks to set
	 */
	public void setTextClicks(boolean textClicks) {
		this.textClicks = textClicks;
	}

	/**
	 * @param goalTracking the goalTracking to set
	 */
	public void setGoalTracking(boolean goalTracking) {
		this.goalTracking = goalTracking;
	}

	/**
	 * @param ecomm360 the ecomm360 to set
	 */
	public void setEcomm360(boolean ecomm360) {
		this.ecomm360 = ecomm360;
	}

	/**
	 * @param googleAnalytics the googleAnalytics to set
	 */
	public void setGoogleAnalytics(String googleAnalytics) {
		this.googleAnalytics = googleAnalytics;
	}

	/**
	 * @param clicktale the clicktale to set
	 */
	public void setClicktale(String clicktale) {
		this.clicktale = clicktale;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 * @param settings
	 * @return
	 */
	public JSONObject getJsonRepresentation() {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opens", isOpens());
		jsonObj.put("html_clicks", isHtmlClicks());
		jsonObj.put("text_clicks", isTextClicks());
		jsonObj.put("goal_tracking", isGoalTracking());
		jsonObj.put("ecomm360", isEcomm360());
		put(jsonObj, "google_analytics", getGoogleAnalytics());
		put(jsonObj, "clicktale", getClicktale());
		//jsonObj.put("salesforce", salesforce.getJsonRepresentation());
		//jsonObj.put("capsule", capsule.getJsonRepresentation());

		return jsonObj;
	}
	
	@Override
	public String toString() {
		return
				"Tracking:" + System.lineSeparator() +
				"    Opens: " + opens + System.lineSeparator() +
				"    Html Clicks: " + htmlClicks + System.lineSeparator() +
				"    Text Clicks: " + textClicks + System.lineSeparator() +
				"    Goal Tracking: " + goalTracking + System.lineSeparator() +
				"    eCommerce360: " + ecomm360 + System.lineSeparator() +
				"    GoogleAnalytics: " + googleAnalytics + System.lineSeparator() +
				"    ClickTale: " + clicktale;
	}

	private JSONObject put(JSONObject settings, String key, String value) {
		if (value != null) {
			return settings.put(key, value);
		}
		return settings;
	}
	
}
