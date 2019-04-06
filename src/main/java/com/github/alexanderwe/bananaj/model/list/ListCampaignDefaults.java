package com.github.alexanderwe.bananaj.model.list;

import org.json.JSONObject;

public class ListCampaignDefaults {

	private String fromName;	// The default from name for campaigns sent to this list
	private String fromEmail;	// The default from email for campaigns sent to this list
	private String subject;		// The default subject line for campaigns sent to this list
	private String language;	// The default language for this lists’s forms
	
	public ListCampaignDefaults() {

	}

	public ListCampaignDefaults(JSONObject defaults) {
		this.fromName = defaults.getString("from_name");
		this.fromEmail = defaults.getString("from_email");
		this.subject = defaults.getString("subject");
		this.language = defaults.getString("language");
	}

	/**
	 * The default from name for campaigns sent to this list
	 * @return
	 */
	public String getFromName() {
		return fromName;
	}

	/**
	 * The default from email for campaigns sent to this list
	 * @return
	 */
	public String getFromEmail() {
		return fromEmail;
	}

	/**
	 * The default subject line for campaigns sent to this list
	 * @return
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * The default language for this lists’s forms
	 * @return
	 */
	public String getLanguage() {
		return language;
	}

}
