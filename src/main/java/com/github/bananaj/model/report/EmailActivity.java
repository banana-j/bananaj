/**
 * @author alexanderweiss
 * @date 19.11.2015
 */
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
import com.github.bananaj.model.automation.emails.AutomationEmail;
import com.github.bananaj.model.list.member.MemberTag;
import com.github.bananaj.utils.DateConverter;

/**
 * Mailchimp's campaign and Automation reports analyze clicks, opens, subscribers' social activity, e-commerce data, and more.
 * 
 *
 */
public class EmailActivity implements JSONParser {

    private String campaignId;
    private String listId;
    private boolean listIsActive;
	private String emailId;
	private String emailAddress;
	private List<Activity> activity;
	
	public EmailActivity() {

	}

	public EmailActivity(JSONObject jsonObj) {
		parse(null, jsonObj);
	}

	@Override
	public void parse(MailChimpConnection connection, JSONObject entity) {
		campaignId = entity.getString("campaign_id");
		listId = entity.getString("list_id");
		listIsActive = entity.getBoolean("list_is_active");
		emailId = entity.getString("email_id");
		emailAddress = entity.getString("email_address");

		JSONArray array = entity.getJSONArray("activity");
		activity = new ArrayList<Activity>(array.length());
		for( int i = 0; i< array.length(); i++)
		{
			activity.add(new Activity(array.getJSONObject(i)));
		}
	}

	public String getEmailId() {
		return emailId;
	}

	public String getEmailAddress() {
		return emailAddress;
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

	public List<Activity> getActivity() {
		return activity;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(1000);
		sb.append("Email Activity Report: " + System.lineSeparator());
		sb.append("    Campaign: " + getCampaignId() + System.lineSeparator());
		sb.append("    Email: " + getEmailAddress() + System.lineSeparator());
		sb.append("    Email Id: " + getEmailId() + System.lineSeparator());
		sb.append("    List Id: " + getListId() + System.lineSeparator());
		sb.append("    List active: " + isListIsActive() + System.lineSeparator());

		if (getActivity().size() > 0) {
			for(Activity activity : getActivity()) {
				sb.append(activity.toString());
			}
		} else {
			sb.append("    Activity: none" + System.lineSeparator());
		}

		return sb.toString();
	}

	public class Activity implements JSONParser {
	    private String action;
	    private String type;
	    private ZonedDateTime timestamp;
		private String url;
		private String ip;

		// TODO: private list<> activity;
		
		public Activity() {

		}

		public Activity(JSONObject jsonObj) {
			parse(null, jsonObj);
		}

		@Override
		public void parse(MailChimpConnection connection, JSONObject entity) {
			if (entity.has("action")) {
				action = entity.getString("action");
			}
			if (entity.has("type")) {
				type = entity.getString("type");
			}
			if (entity.has("url")) {
				url = entity.getString("url");
			}
			if (entity.has("ip")) {
				ip = entity.getString("ip");
			}
			if (entity.has("timestamp")) {
				timestamp = DateConverter.fromISO8601(entity.getString("timestamp"));
			}
		}

		public String getAction() {
			return action;
		}

		public String getType() {
			return type;
		}

		public ZonedDateTime getTimestamp() {
			return timestamp;
		}

		public String getUrl() {
			return url;
		}

		public String getIp() {
			return ip;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(1000);
			sb.append("    Activity: " + System.lineSeparator());
			sb.append("         action: " + getAction() + System.lineSeparator());
			if (getType() != null) {
				sb.append("         type: " + getType() + System.lineSeparator());
			}
			sb.append("         timestamp: " + DateConverter.toLocalString(getTimestamp()) +  System.lineSeparator());
			if (getUrl() != null) {
				sb.append("         url: " + getUrl() + System.lineSeparator());
			}
			sb.append("         ip: " + getIp() + System.lineSeparator());
			return sb.toString();
		}
	}

}
