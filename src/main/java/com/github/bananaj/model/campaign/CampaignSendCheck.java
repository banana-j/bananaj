package com.github.bananaj.model.campaign;

import org.json.JSONObject;

import com.github.bananaj.utils.JSONObjectCheck;

/**
 * A feedback item to review before sending a campaign
 *
 */
public class CampaignSendCheck {

	private Integer id;
	private CampaignCheckType type;
	private String heading;
	private String details;
	
	public CampaignSendCheck(JSONObject jsonObj) {
		JSONObjectCheck jObj = new JSONObjectCheck(jsonObj);
		id = jObj.getInt("id");
		type = jObj.getEnum(CampaignCheckType.class, "type");
		heading = jObj.getString("heading");
		details = jObj.getString("details");
	}

	public CampaignSendCheck() {

	}

	/**
	 * @return The ID for the item
	 */
	public Integer getId() {
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
				"type: " + getType().toString() + System.lineSeparator() +
				"heading: " + getHeading() + System.lineSeparator() +
				"details: ";
	}

}
