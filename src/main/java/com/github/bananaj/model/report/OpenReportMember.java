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
import com.github.bananaj.utils.JSONObjectCheck;

/**
 * a list member who opened a campaign email. Each members object will contain
 * information about the number of total opens by a single member, as well as
 * timestamps for each open event.
 *
 */
public class OpenReportMember implements JSONParser {
	
	private String campaignId;
	private String listId;
	private Boolean listIsActive;
	private String contactStatus;
	private String emailId;
	private String emailAddress;
	private Map<String, Object> mergeFields;
	private Boolean vip;
	private Integer opensCount;
	private List<ZonedDateTime> opens;

	public OpenReportMember() {
		
	}
	
	public OpenReportMember(JSONObject jsonObj) {
		parse(null, jsonObj);
	}

	@Override
	public void parse(MailChimpConnection connection, JSONObject entity) {
		JSONObjectCheck jObj = new JSONObjectCheck(entity);
		campaignId = jObj.getString("campaign_id");
		listId = jObj.getString("list_id");
		listIsActive = jObj.getBoolean("list_is_active");
		contactStatus = jObj.getString("contact_status");
		emailId = jObj.getString("email_id");
		emailAddress = jObj.getString("email_address");

		mergeFields = new HashMap<String, Object>();
		if (jObj.has("merge_fields")) {
			final JSONObject mergeFieldsObj = jObj.getJSONObject("merge_fields");
			for(String key : mergeFieldsObj.keySet()) {
				mergeFields.put(key, mergeFieldsObj.get(key));
			}
		}

		vip = jObj.getBoolean("vip");
		opensCount = jObj.getInt("opens_count");

		final JSONArray openArray = jObj.getJSONArray("opens");
		opens = new ArrayList<ZonedDateTime>(openArray != null ? openArray.length() : 0);
		if (openArray != null) {
			for(int i=0; i<openArray.length(); i++) {
				JSONObjectCheck ts = new JSONObjectCheck(openArray.getJSONObject(i));
				opens.add(ts.getISO8601Date("timestamp"));
			}
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
	public Boolean isListIsActive() {
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
	public Boolean isVip() {
		return vip;
	}

	/**
	 * @return The total number of times the this campaign was opened by the list member.
	 */
	public Integer getOpensCount() {
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
