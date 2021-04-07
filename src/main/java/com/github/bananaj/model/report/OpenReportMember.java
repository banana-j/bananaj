package com.github.bananaj.model.report;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.utils.DateConverter;

/**
 * a list member who opened a campaign email. Each members object will contain
 * information about the number of total opens by a single member, as well as
 * timestamps for each open event.
 *
 */
public class OpenReportMember implements JSONParser {
	
	private String campaignId;
	private String listId;
	private boolean listIsActive;
	private String contactStatus;
	private String emailId;
	private String emailAddress;
	private Map<String, Object> mergeFields;
	private boolean vip;
	private int opensCount;
	private List<ZonedDateTime> opens;

	public OpenReportMember() {
		
	}
	
	public OpenReportMember(JSONObject jsonObj) {
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
		opensCount = entity.getInt("opens_count");

		final JSONArray openArray = entity.getJSONArray("opens");
		opens = new ArrayList<ZonedDateTime>(openArray.length());
		for(int i=0; i<openArray.length(); i++) {
			JSONObject ts = openArray.getJSONObject(i);
			opens.add(DateConverter.fromISO8601(ts.getString("timestamp")));
		}
	}
	
	/**
	 * @return The unique id for the campaign.
	 */
	public String getCampaignId() {
		return campaignId;
	}

	/**
	 * @return The unique id for the list.
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
	 * @return The status of the member, namely if they are subscribed,
	 *         unsubscribed, deleted, non-subscribed, transactional, pending, or
	 *         need reconfirmation.
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
	 * @return a Map of all merge field name value pairs
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
	 * @return The total number of times the this campaign was opened by the list member.
	 */
	public int getOpensCount() {
		return opensCount;
	}

	/**
	 * @return An array of timestamps for each time a list member opened the
	 *         campaign. If a list member opens an email multiple times, this will
	 *         return a separate timestamp for each open event.
	 */
	public List<ZonedDateTime> getOpens() {
		return opens;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getEmailId() + " " + getEmailAddress() + " Status: " + getContactStatus() + " VIP: " + isVip() + " Opens: " + getOpensCount();
	}


}
