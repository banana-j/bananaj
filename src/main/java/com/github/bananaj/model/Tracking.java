package com.github.bananaj.model;

import org.json.JSONObject;

import com.github.bananaj.utils.JSONObjectCheck;

/**
 * Common tracking configuration for campaigns, automation and automation emails 
 *
 */
public class Tracking {

	private Boolean opens = true;
	private Boolean htmlClicks = true;
	private Boolean textClicks = true;
	private Boolean goalTracking = false;  // Deprecated
	private Boolean ecomm360 = false;
	private String googleAnalytics;
	private String clicktale;
	
	private Salesforce salesforce;
	private Capsule capsule;

	public Tracking() {

	}

	public Tracking(JSONObject tracking) {
		JSONObjectCheck jObj = new JSONObjectCheck(tracking);
		this.opens = jObj.getBoolean("opens");
		this.htmlClicks = jObj.getBoolean("html_clicks");
		this.textClicks = jObj.getBoolean("text_clicks");
		this.goalTracking = jObj.getBoolean("goal_tracking");
		this.ecomm360 = jObj.getBoolean("ecomm360");
		this.googleAnalytics = jObj.getString("google_analytics");
		this.clicktale = jObj.getString("clicktale");
		if (tracking.has("salesforce")) {
			this.salesforce = new Salesforce(tracking.getJSONObject("salesforce"));
		}
		if (tracking.has("capsule")) {
			this.capsule = new Capsule(tracking.getJSONObject("capsule"));
		}
	}

	/**
	 * Whether to track opens. Defaults to true. Cannot be set to false for variate campaigns.
	 */
	public Boolean isOpens() {
		return opens;
	}

	/**
	 * Whether to track clicks in the HTML version of the campaign. Defaults to true. Cannot be set to false for variate campaigns.
	 */
	public Boolean isHtmlClicks() {
		return htmlClicks;
	}

	/**
	 * Whether to track clicks in the plain-text version of the campaign. Defaults to true. Cannot be set to false for variate campaigns.
	 */
	public Boolean isTextClicks() {
		return textClicks;
	}

	/**
	 * Whether to enable Goal tracking
	 * @deprecated Deprecated by Mailchimp API
	 */
	public Boolean isGoalTracking() {
		return goalTracking;
	}

	/**
	 * Whether to enable e-commerce tracking.
	 */
	public Boolean isEcomm360() {
		return ecomm360;
	}

	/**
	 * The custom slug for Google Analytics tracking (max of 50 bytes).
	 */
	public String getGoogleAnalytics() {
		return googleAnalytics;
	}

	/**
	 * The custom slug for ClickTale tracking (max of 50 bytes)
	 */
	public String getClicktale() {
		return clicktale;
	}

	/**
	 * Whether to track opens. Defaults to true. Cannot be set to false for variate campaigns.
	 * @param opens Whether to track opens. Cannot be set to false for variate campaigns.
	 */
	public void setOpens(Boolean opens) {
		this.opens = opens;
	}

	/**
	 * Whether to track clicks in the HTML version of the campaign. Defaults to true. Cannot be set to false for variate campaigns.
	 * @param htmlClicks Whether to track clicks in the HTML version of the campaign. Cannot be set to false for variate campaigns.
	 */
	public void setHtmlClicks(Boolean htmlClicks) {
		this.htmlClicks = htmlClicks;
	}

	/**
	 * Whether to track clicks in the plain-text version of the campaign. Defaults to true. Cannot be set to false for variate campaigns.
	 * @param textClicks Whether to track clicks in the plain-text version of the campaign. Cannot be set to false for variate campaigns.
	 */
	public void setTextClicks(Boolean textClicks) {
		this.textClicks = textClicks;
	}

	/**
	 * @param goalTracking Enable/disable goalTracking
	 * @deprecated Deprecated by Mailchimp API
	 */
	public void setGoalTracking(Boolean goalTracking) {
		this.goalTracking = goalTracking;
	}

	/**
	 * Whether to enable e-commerce tracking.
	 * @param ecomm360 Whether to enable e-commerce tracking.
	 */
	public void setEcomm360(Boolean ecomm360) {
		this.ecomm360 = ecomm360;
	}

	/**
	 * The custom slug for Google Analytics tracking (max of 50 bytes).
	 * @param googleAnalytics The custom slug for Google Analytics tracking (max of 50 bytes).
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
	 */
	public JSONObject getJsonRepresentation() {
		JSONObjectCheck jsonObj = new JSONObjectCheck();
		jsonObj.put("opens", isOpens().booleanValue());
		jsonObj.put("html_clicks", isHtmlClicks().booleanValue());
		jsonObj.put("text_clicks", isTextClicks().booleanValue());
		jsonObj.put("goal_tracking", isGoalTracking().booleanValue());
		jsonObj.put("ecomm360", isEcomm360().booleanValue());
		jsonObj.put("google_analytics", getGoogleAnalytics());
		jsonObj.put("clicktale", getClicktale());
		//jsonObj.put("salesforce", salesforce.getJsonRepresentation());
		//jsonObj.put("capsule", capsule.getJsonRepresentation());

		return jsonObj.getJsonObject();
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
	
	/**
	 * 
	 * @deprecated
	 */
	public class Salesforce {
		Boolean campaign;
		Boolean notes;
		
		public Salesforce() {

		}
		
		public Salesforce(JSONObject salesforce) {
			JSONObjectCheck jObj = new JSONObjectCheck(salesforce);
			this.campaign = jObj.getBoolean("campaign");
			this.notes = jObj.getBoolean("campaign");
		}

		/**
		 * Campaign is a connected Salesforce account.
		 */
		public Boolean getCampaign() {
			return campaign;
		}

		/**
		 * Contact notes for a campaign based on a subscriber's email address.
		 */
		public Boolean getNotes() {
			return notes;
		}
		
	}

	/**
	 * 
	 * @deprecated
	 */
	public class Capsule {
		Boolean  notes;

		public Capsule() {

		}

		public Capsule(JSONObject capsule) {
			JSONObjectCheck jObj = new JSONObjectCheck(capsule);
			this.notes = jObj.getBoolean("campaign");
		}

		/**
		 * Contact notes for a campaign based on a subscriber's email addresses.
		 */
		public Boolean getNotes() {
			return notes;
		}
		
	}
}
