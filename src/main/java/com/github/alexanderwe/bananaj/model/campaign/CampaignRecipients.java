package com.github.alexanderwe.bananaj.model.campaign;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.exceptions.ConditionException;

/**
 * 
 */
public class CampaignRecipients {

    private String listId;
    private boolean listIsActive;
    private String listName;
    private String segmentText;
    private int recipientCount;
    private CampaignSegmentOpts segmentOpts;

    /**
     * Used when created a Condition locally with the Builder class
     * @see Builder
     * @param b
     */

    public CampaignRecipients(Builder b) throws ConditionException{
    	this.segmentOpts = b.segmentOpts;
    	this.listId = b.listId;
    }

    public CampaignRecipients(JSONObject jsonObj) {
    	this.listId = jsonObj.getString("list_id");
    	this.listIsActive = jsonObj.getBoolean("list_is_active");
    	this.listName = jsonObj.getString("list_name");
    	this.segmentText = jsonObj.getString("segment_text");
    	this.recipientCount = jsonObj.getInt("recipient_count");
    	if (jsonObj.has("segment_opts")) {
    		this.segmentOpts = new CampaignSegmentOpts(jsonObj.getJSONObject("segment_opts"));
    	}
    }
    
    public CampaignRecipients() {
    	
    }
    
    /**
     * The unique list id
     * @return
     */
    public String getListId() {
        return listId;
    }

    /**
	 * An object representing all segmentation options. This object should contain a
	 * saved_segment_id to use an existing segment, or you can create a new segment
	 * by including both match and conditions options.
	 * 
	 * @return
	 */
    public CampaignSegmentOpts getSegmentOpts() {
        return segmentOpts;
    }

    /**
     * The status of the list used, namely if it’s deleted or disabled
     * @return
     */
    public boolean isListIsActive() {
		return listIsActive;
	}

    /**
     * The name of the list
     * @return
     */
	public String getListName() {
		return listName;
	}

	/**
	 * A description of the segment used for the campaign. Formatted as a string marked up with HTML.
	 * @return
	 */
	public String getSegmentText() {
		return segmentText;
	}

	/**
	 * Count of the recipients on the associated list
	 * @return
	 */
	public int getRecipientCount() {
		return recipientCount;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 * @return
	 */
	public JSONObject getJsonRepresentation() {
        JSONObject recipents = new JSONObject();
        
        recipents.put("list_id", getListId());
        if (getSegmentOpts() != null) {
        	recipents.put("segment_opts", getSegmentOpts().getJsonRepresentation());
        }

        return recipents;
    }

    @Override
    public String toString() {
        return 
        		"List ID: " + getListId() + System.lineSeparator() +
        		"Name: " + getListName() + System.lineSeparator() +
                "Segment opts: " + getSegmentOpts().toString();
    }

    public static class Builder {
        private String listId;
        private CampaignSegmentOpts segmentOpts;

        public Builder listId(String listId) {
            this.listId = listId;
            return this;
        }

        public Builder segmentOpts(CampaignSegmentOpts op) {
            this.segmentOpts = op;
            return this;
        }

        public CampaignRecipients build() {
            try {
                return new CampaignRecipients(this);
            } catch (ConditionException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
