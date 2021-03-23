package com.github.bananaj.model.report;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.utils.DateConverter;

/**
 * An abuse complaint occurs when your recipient reports an email as spam in their mail program.
 *
 */
public class AbuseReport implements JSONParser {
	private int id;
	private String campaignId;
	private String listId;
	private Boolean listIsActive;
	private String emailId;
	private String emailAddress;
	private Map<String, Object> mergeFields;
	private boolean vip;
	private ZonedDateTime date;

	public AbuseReport() {
		
	}

	public AbuseReport(JSONObject abuse) {
		parse(null, abuse);
	}

	/**
	 * Parse a JSON representation of Abuse Report into this.
	 * @param connection Not used
	 * @param abuse
	 */
	public void parse(MailChimpConnection connection, JSONObject abuse) {
		id = abuse.getInt("id");
		campaignId = abuse.getString("campaign_id");
		listId = abuse.getString("list_id");
		listIsActive = abuse.has("list_is_active") ? abuse.getBoolean("list_is_active") : null;
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
	 * @return The status of the list used, namely if it's deleted or disabled.
	 */
	public Boolean getListIsActive() {
		return listIsActive;
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
				(listIsActive != null ? "    Active: " + listIsActive + System.lineSeparator() : "") + 
				"    Email: " + emailAddress + " Email ID:" + emailId + " VIP: " + vip;
	}
	
}
