package com.github.bananaj.model.list.member;

import org.json.JSONObject;

/**
 * Details about subscribers’ recent activity.
 * 
 * Created by alexanderweiss on 20.01.16.
 */
public class MemberActivity {
    String action;
    String timestamp;
    String url;
    String type;
    String campaignId;
    String title;
    String parentCampaign;

    public MemberActivity(JSONObject jsonObj) {
    	action = jsonObj.getString("action");
    	timestamp = jsonObj.getString("timestamp");
    	url = jsonObj.has("url") ? jsonObj.getString("url") : null;
    	type = jsonObj.has("type") ? jsonObj.getString("type") : null;
    	campaignId = jsonObj.has("campaign_id") ? jsonObj.getString("campaign_id") : null;
    	title = jsonObj.has("title") ? jsonObj.getString("title") : null;
    	parentCampaign = jsonObj.has("parent_campaign") ? jsonObj.getString("parent_campaign") : null;
    }

	/**
	 * @return The type of action recorded for the subscriber.
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @return The date and time recorded for the action.
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @return For clicks, the URL the subscriber clicked on.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return The type of campaign that was sent.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return The web-based ID for the campaign.
	 */
	public String getCampaignId() {
		return campaignId;
	}

	/**
	 * @return If set, the campaign’s title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return The ID of the parent campaign.
	 */
	public String getParentCampaign() {
		return parentCampaign;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString(){
    	return 
    			"Activity: " + timestamp + " " + (campaignId != null ? campaignId: "") + (title != null ? " " + title : "") + (parentCampaign != null ? " " + parentCampaign : "") + System.lineSeparator() +
    			"    Action: " + action + 
    			(type != null ? System.lineSeparator() + "    Campaign Type: " + type : "") +
    			(url != null ? System.lineSeparator() + "    URL: " + url : "");
    }
}
