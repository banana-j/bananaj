package com.github.bananaj.model.list.member;

import org.json.JSONObject;

import com.github.bananaj.utils.JSONObjectCheck;

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

    public MemberActivity(JSONObject activity) {
		JSONObjectCheck jObj = new JSONObjectCheck(activity);
    	action = jObj.getString("action");
    	timestamp = jObj.getString("timestamp");
    	url = jObj.getString("url");
    	type = jObj.getString("type");
    	campaignId = jObj.getString("campaign_id");
    	title = jObj.getString("title");
    	parentCampaign = jObj.getString("parent_campaign");
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
