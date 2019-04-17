package com.github.alexanderwe.bananaj.model.campaign;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.model.MailchimpObject;

/**
 * A feedback item to review before sending a campaign
 *
 */
public class CampaignSendCheck {

	private int id;
	private CampaignCheckType type;
	private String heading;
	private String details;
	
	public CampaignSendCheck(JSONObject jsonObj) {
		id = jsonObj.getInt("id");
		type = CampaignCheckType.valueOf(jsonObj.getString("type").toUpperCase());
		heading = jsonObj.getString("heading");
		details = jsonObj.getString("details");
	}

	public CampaignSendCheck() {

	}

	/**
	 * @return The ID for the item
	 */
	public int getId() {
		return id;
	}

	/**
	 * The item type
	 * @return the item type
	 */
	public CampaignCheckType getType() {
		return type;
	}

	/**
	 * The heading for the item
	 * @return the heading
	 */
	public String getHeading() {
		return heading;
	}

	/**
	 * Details about the item feedback
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return 
				"id: " + getId() + System.lineSeparator() +
				"heading: " + getHeading() + System.lineSeparator() +
				"details: ";
	}
	
	
}
