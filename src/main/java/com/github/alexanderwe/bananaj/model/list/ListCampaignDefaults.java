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

	public ListCampaignDefaults(Builder b) {
		this.fromName = b.fromName;
		this.fromEmail = b.fromEmail;
		this.subject = b.subject;
		this.language = b.language;
	}

	/**
	 * The default from name for campaigns sent to this list
	 */
	public String getFromName() {
		return fromName;
	}

	/**
	 * The default from email for campaigns sent to this list
	 */
	public String getFromEmail() {
		return fromEmail;
	}

	/**
	 * The default subject line for campaigns sent to this list
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * The default language for this lists’s forms
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @return the jsonRepresentation
	 */
	protected JSONObject getJSONRepresentation() {
		JSONObject json = new JSONObject();

		json.put("from_name", fromName);
		json.put("from_email", fromEmail);
		json.put("subject", subject);
		json.put("language", language);
		
		return json;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return 
				"Defaults:" + System.lineSeparator() +
				"    From Name: " + getFromName() + System.lineSeparator() +
				"    From Email: " + getFromEmail() + System.lineSeparator() +
				"    Subject: " + getSubject() + System.lineSeparator() +
				"    Language: " + getLanguage(); 
	}

    /**
     * Builder for {@link ListCampaignDefaults}
     *
     */
    public static class Builder {
    	private String fromName;	// The default from name for campaigns sent to this list
    	private String fromEmail;	// The default from email for campaigns sent to this list
    	private String subject;		// The default subject line for campaigns sent to this list
    	private String language;	// The default language for this lists’s forms
		/**
		 * @param fromName The default from name for campaigns sent to this list.
		 */
		public void setFromName(String fromName) {
			this.fromName = fromName;
		}
		/**
		 * @param fromEmail The default from email for campaigns sent to this list.
		 */
		public void setFromEmail(String fromEmail) {
			this.fromEmail = fromEmail;
		}
		/**
		 * @param subject The default subject line for campaigns sent to this list.
		 */
		public void setSubject(String subject) {
			this.subject = subject;
		}
		/**
		 * @param language The default language for this lists's forms.
		 */
		public void setLanguage(String language) {
			this.language = language;
		}

    	public ListCampaignDefaults build() {
    		return new ListCampaignDefaults(this);
    	}
    }
}
