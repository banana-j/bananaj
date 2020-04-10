package com.github.bananaj.model.report;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class ClickReportMember {
	private String campaignId;
	private String listId;
	private boolean listIsActive;
	private String contactStatus;
	private String emailId;
	private String emailAddress;
	private Map<String, Object> mergeFields;
	private boolean vip;
	private int clicks;
	private String urlId;

	public ClickReportMember(JSONObject jsonObj) {
		campaignId = jsonObj.getString("campaign_id");
		listId = jsonObj.getString("list_id");
		listIsActive = jsonObj.getBoolean("list_is_active");
		contactStatus = jsonObj.getString("contact_status");
		emailId = jsonObj.getString("email_id");
		emailAddress = jsonObj.getString("email_address");
		
		mergeFields = new HashMap<String, Object>();
		if (jsonObj.has("merge_fields")) {
			final JSONObject mergeFieldsObj = jsonObj.getJSONObject("merge_fields");
			for(String key : mergeFieldsObj.keySet()) {
				mergeFields.put(key, mergeFieldsObj.get(key));
			}
		}
		
		vip = jsonObj.getBoolean("vip");
		clicks = jsonObj.getInt("clicks");
		urlId = jsonObj.getString("url_id");
	}

	/**
	 * @return The campaign id.
	 */
	public String getCampaignId() {
		return campaignId;
	}

	/**
	 * @return The list id.
	 */
	public String getListId() {
		return listId;
	}

	/**
	 * @return The status of the list used, namely if it's deleted or disabled.
	 */
	public boolean isListIsActive() {
		return listIsActive;
	}

	/**
	 * @return The status of the member, namely if they are subscribed, unsubscribed, deleted, non-subscribed, transactional, pending, or need reconfirmation.
	 */
	public String getContactStatus() {
		return contactStatus;
	}

	/**
	 * @return The MD5 hash of the lowercase version of the list member's email address.
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @return Email address for a subscriber.
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @return An individual merge var and value for a member.
	 */
	public Map<String, Object> getMergeFields() {
		return mergeFields;
	}

	/**
	 * @return VIP status for subscriber.
	 */
	public boolean isVip() {
		return vip;
	}

	/**
	 * @return The total number of times the subscriber clicked on the link.
	 */
	public int getClicks() {
		return clicks;
	}

	/**
	 * @return The id for the tracked URL in the campaign.
	 */
	public String getUrlId() {
		return urlId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getEmailId() + " " + getEmailAddress() + " Status: " + getContactStatus() + " VIP: " + isVip() + " Clicks: " + getClicks();
	}

}
