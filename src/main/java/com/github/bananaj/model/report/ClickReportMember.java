package com.github.bananaj.model.report;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.model.JSONParser;

public class ClickReportMember implements JSONParser {
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

	public ClickReportMember() {

	}

	public ClickReportMember(JSONObject jsonObj) {
		parse(null, jsonObj);
	}

	@Override
	public void parse(MailChimpConnection connection, JSONObject entity) {
		campaignId = entity.getString("campaign_id");
		listId = entity.getString("list_id");
		listIsActive = entity.getBoolean("list_is_active");
		contactStatus = entity.getString("contact_status");
		emailId = entity.getString("email_id");
		emailAddress = entity.getString("email_address");
		
		mergeFields = new HashMap<String, Object>();
		if (entity.has("merge_fields")) {
			final JSONObject mergeFieldsObj = entity.getJSONObject("merge_fields");
			for(String key : mergeFieldsObj.keySet()) {
				mergeFields.put(key, mergeFieldsObj.get(key));
			}
		}
		
		vip = entity.getBoolean("vip");
		clicks = entity.getInt("clicks");
		urlId = entity.getString("url_id");
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
		StringBuilder sb = new StringBuilder(500);
		sb.append("Click Report Member: " + getEmailId() + " " + getEmailAddress() + System.lineSeparator());
		sb.append("    Url Id: " + getUrlId() + System.lineSeparator());
		sb.append("    Clicks: " + getClicks() + System.lineSeparator());
		sb.append("    Status: " + getContactStatus() + System.lineSeparator());
		sb.append("    VIP: " + isVip() + System.lineSeparator());
		sb.append("    List Id: " + getListId() + System.lineSeparator());
		sb.append("    List Active: " + isListIsActive());

		if (mergeFields.size() > 0) {
			sb.append(System.lineSeparator() + "    Merge fields: " );
			for (String key : mergeFields.keySet()) {
				sb.append(System.lineSeparator() + "         " + key + ": " + mergeFields.get(key));
			}
		}

		return sb.toString();
	}

}
