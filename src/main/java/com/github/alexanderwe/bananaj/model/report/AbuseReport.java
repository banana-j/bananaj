package com.github.alexanderwe.bananaj.model.report;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.utils.DateConverter;

/**
 * An abuse complaint occurs when your recipient reports an email as spam in their mail program.
 *
 */
public class AbuseReport {
	private int id;
	private String campaignId;
	private String listId;
	private String emailId;
	private String emailAddress;
	private Map<String, Object> mergeFields;
	private boolean vip;
	private ZonedDateTime date;

	public AbuseReport(JSONObject abuse) {
		id = abuse.getInt("id");
		campaignId = abuse.getString("campaign_id");
		listId = abuse.getString("list_id");
		emailId = abuse.getString("email_id");
		emailAddress = abuse.getString("email_address");
		mergeFields = new HashMap<String, Object>();
		if (abuse.has("merge_fields")) {
			final JSONObject mergeFieldsObj = abuse.getJSONObject("merge_fields");
			for(String key : mergeFieldsObj.keySet()) {
				mergeFields.put(key, mergeFieldsObj.get(key));
			}
		}
		vip = abuse.getBoolean("vip");
		date = DateConverter.fromISO8601(abuse.getString("date"));
	}

	/**
	 * @return The id for the abuse report.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return The campaign id for the abuse report.
	 */
	public String getCampaignId() {
		return campaignId;
	}

	/**
	 * @return The list id for the abuse report.
	 */
	public String getListId() {
		return listId;
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
	 * @return Date for the abuse report
	 */
	public ZonedDateTime getDate() {
		return date;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return
				"Abuse Report: " + DateConverter.toLocalString(date) + " ID: " + id + "Campaign ID: " + campaignId + " List ID: " + listId + System.lineSeparator() +
				"    Email: " + emailAddress + " Email ID:" + emailId + " VIP: " + vip;
	}
	
}
