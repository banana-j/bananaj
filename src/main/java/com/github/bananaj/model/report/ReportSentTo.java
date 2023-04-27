/**
 * @author alexanderweiss
 * @date 19.11.2015
 */
package com.github.bananaj.model.report;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.utils.DateConverter;

/**
 * Mailchimp's campaign and Automation reports analyze clicks, opens, subscribers' social activity, e-commerce data, and more.
 * 
 *
 */
public class ReportSentTo implements JSONParser {

	private String emailId;
	private String emailAddress;
	private Map<String, Object> mergeFields;
	private boolean vip;
    // private MemberStatus status;
    private String status;
    private int openCount;
    private ZonedDateTime lastOpen;
    private String abSplitGroup;
    private int gmtOffset;
    private String campaignId;
    private String listId;
    private boolean listIsActive;
	
	public ReportSentTo() {

	}

	public ReportSentTo(JSONObject jsonObj) {
		parse(null, jsonObj);
	}

	@Override
	public void parse(MailChimpConnection connection, JSONObject entity) {
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
		//status = MemberStatus.valueOf(entity.getString("status").toUpperCase());
		status = entity.getString("status");
		openCount = entity.getInt("open_count");
		if (entity.has("last_open")) {
			lastOpen = DateConverter.fromISO8601(entity.getString("last_open"));
		}
		abSplitGroup = entity.getString("absplit_group");
		gmtOffset = entity.getInt("gmt_offset");
		campaignId = entity.getString("campaign_id");
		listId = entity.getString("list_id");
		listIsActive = entity.getBoolean("list_is_active");
	}

	public String getEmailId() {
		return emailId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public Map<String, Object> getMergeFields() {
		return mergeFields;
	}

	public boolean isVip() {
		return vip;
	}

	public String getStatus() {
		return status;
	}

	public int getOpenCount() {
		return openCount;
	}

	public ZonedDateTime getLastOpen() {
		return lastOpen;
	}

	public String getAbSplitGroup() {
		return abSplitGroup;
	}

	public int getGmtOffset() {
		return gmtOffset;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public String getListId() {
		return listId;
	}

	public boolean isListIsActive() {
		return listIsActive;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(1000);
		sb.append("SendTo Report: " + System.lineSeparator());
		sb.append("    Campaign: " + getCampaignId() + System.lineSeparator());
		sb.append("    Email: " + getEmailAddress() + System.lineSeparator());
		sb.append("    Email Id: " + getEmailId() + System.lineSeparator());
		sb.append("    Status: " + getStatus() + System.lineSeparator());
		sb.append("    Open count: " + getOpenCount() + System.lineSeparator());
		sb.append("    Last open: " + DateConverter.toLocalString(getLastOpen()) +  System.lineSeparator());
		sb.append("    List Id: " + getListId() + System.lineSeparator());
		sb.append("    A/B split group: " + getAbSplitGroup() + System.lineSeparator());
		sb.append("    GMT offset: " + getGmtOffset() + System.lineSeparator());
		sb.append("    List active: " + isListIsActive() + System.lineSeparator());
		sb.append("    Vip: " + isVip() + System.lineSeparator());

		if (mergeFields.size() > 0) {
			sb.append("    Merge fields: " + System.lineSeparator());
			for (String key : mergeFields.keySet()) {
				sb.append("         " + key + ": " + mergeFields.get(key) + System.lineSeparator());
			}
		}

		return sb.toString();
	}

}
