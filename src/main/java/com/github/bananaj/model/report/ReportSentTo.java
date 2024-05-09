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
import com.github.bananaj.utils.JSONObjectCheck;

/**
 * Mailchimp's campaign and Automation reports analyze clicks, opens, subscribers' social activity, e-commerce data, and more.
 * 
 *
 */
public class ReportSentTo implements JSONParser {

    private String campaignId;
    private String listId;
    private Boolean listIsActive;
	private String emailId;
	private String emailAddress;
	private Map<String, Object> mergeFields;
	private Boolean vip;
    private String status;
    private Integer openCount;
    private ZonedDateTime lastOpen;
    private String abSplitGroup;
    private Integer gmtOffset;
	
	public ReportSentTo() {

	}

	public ReportSentTo(JSONObject jsonObj) {
		parse(null, jsonObj);
	}

	@Override
	public void parse(MailChimpConnection connection, JSONObject entity) {
		JSONObjectCheck jObj = new JSONObjectCheck(entity);
		campaignId = jObj.getString("campaign_id");
		listId = jObj.getString("list_id");
		listIsActive = jObj.getBoolean("list_is_active");
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
		status = jObj.getString("status");
		openCount = jObj.getInt("open_count");
		lastOpen = jObj.getISO8601Date("last_open");
		abSplitGroup = jObj.getString("absplit_group");
		gmtOffset = jObj.getInt("gmt_offset");
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

	public Boolean isVip() {
		return vip;
	}

	public String getStatus() {
		return status;
	}

	public Integer getOpenCount() {
		return openCount;
	}

	public ZonedDateTime getLastOpen() {
		return lastOpen;
	}

	public String getAbSplitGroup() {
		return abSplitGroup;
	}

	public Integer getGmtOffset() {
		return gmtOffset;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public String getListId() {
		return listId;
	}

	public Boolean isListIsActive() {
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
