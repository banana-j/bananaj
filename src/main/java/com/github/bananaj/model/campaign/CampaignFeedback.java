package com.github.bananaj.model.campaign;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.ZonedDateTime;

import org.json.JSONException;
import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.utils.DateConverter;

/**
 * Feedback message from a campaign. Post comments, reply to team feedback, and send test emails while you're working together on a Mailchimp campaign.
 *
 */
public class CampaignFeedback {
	
	private Integer feedbackId;
	private Integer parentId;
	private Integer blockId;
	private String message;
	private Boolean isComplete;
	private String createdBy;
	private ZonedDateTime createdAt;
	private ZonedDateTime updatedAt;
	private CampaignFeedbackSourceType source;
	private String campaignId;
	private MailChimpConnection connection;

	public CampaignFeedback(Builder b) {
		this.blockId = b.blockId;
		this.isComplete = b.isComplete;
		this.message = b.message;
		this.campaignId = b.campaignId;
		this.connection = b.connection;
	}

	public CampaignFeedback(MailChimpConnection connection, JSONObject jsonObj) {
		parse(connection, jsonObj);
	}

	public CampaignFeedback() {
		
	}

	/**
	 * Parse a JSON representation of a member into this.
	 * @param connection
	 * @param jsonObj
	 */
	public void parse(MailChimpConnection connection, JSONObject jsonObj) {
		this.feedbackId = jsonObj.getInt("feedback_id");
		this.parentId = jsonObj.getInt("parent_id");
		this.blockId = jsonObj.getInt("block_id");
		this.message = jsonObj.getString("message");
		this.isComplete = jsonObj.getBoolean("is_complete");
		this.createdBy = jsonObj.getString("created_by");
		this.createdAt = DateConverter.fromISO8601(jsonObj.getString("created_at"));
		this.updatedAt = DateConverter.fromISO8601(jsonObj.getString("updated_at"));
		this.source = CampaignFeedbackSourceType.valueOf(jsonObj.getString("source").toUpperCase());
		this.campaignId = jsonObj.getString("campaign_id");
		this.connection = connection;
	}
	
	/**
	 * Add campaign feedback
	 * @throws IOException
	 * @throws Exception 
	 */
	public void create() throws IOException, Exception {
		JSONObject jsonFeedback = getJsonRepresentation();
		JSONObject jsonObj = new JSONObject(connection.do_Post(new URL(connection.getCampaignendpoint()+"/"+getCampaignId()+"/feedback"), jsonFeedback.toString(), connection.getApikey()));
		parse(connection, jsonObj);
	}
	
	/**
	 * Update a campaign feedback message
	 * @throws IOException
	 * @throws Exception 
	 */
	public void update() throws IOException, Exception {
		JSONObject jsonFeedback = getJsonRepresentation();
		JSONObject jsonObj = new JSONObject(connection.do_Patch(new URL(connection.getCampaignendpoint()+"/"+getCampaignId()+"/feedback/"+getFeedbackId()), jsonFeedback.toString(), connection.getApikey()));
		parse(connection, jsonObj);
	}
	
	/**
	 * Delete a campaign feedback message
	 * @throws IOException
	 * @throws Exception 
	 */
	public void delete() throws IOException, Exception {
		connection.do_Delete(new URL(connection.getCampaignendpoint()+"/"+getCampaignId()+"/feedback/"+getFeedbackId()),connection.getApikey());
	}
	
	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
	public JSONObject getJsonRepresentation() {
		JSONObject jsonObj = new JSONObject();
		if (getBlockId() != null) {
			jsonObj.put("block_id", getBlockId());
		}
		jsonObj.put("message", getMessage());
		if (getIsComplete() != null) {
			jsonObj.put("is_complete", getIsComplete());
		}
		return jsonObj;
	}
	
	/**
	 * The block id for the editable block that the feedback addresses
	 */
	public Integer getBlockId() {
		return blockId;
	}

	/**
	 * set/change the block id for the editable block that the feedback addresses.
	 * You must call {@link #update()} for changes to take effect.
	 * 
	 * @param blockId the blockId to set
	 */
	public void setBlockId(Integer blockId) {
		this.blockId = blockId;
	}

	/**
	 * The content of the feedback
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * set/change the content of the feedback.
	 * You must call {@link #update()} for changes to take effect.
	 * 
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * The status of feedback
	 */
	public Boolean getIsComplete() {
		return isComplete;
	}

	/**
	 * set/change the status of feedback.
	 * You must call {@link #update()} for changes to take effect.
	 * 
	 * @param isComplete the isComplete to set
	 */
	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}

	/**
	 * The individual id for the feedback item
	 */
	public Integer getFeedbackId() {
		return feedbackId;
	}

	/**
	 * If a reply, the id of the parent feedback item
	 */
	public Integer getParentId() {
		return parentId;
	}

	/**
	 * The login name of the user who created the feedback
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * The date and time the feedback item was created
	 */
	public ZonedDateTime getCreatedAt() {
		return createdAt;
	}

	/**
	 * The date and time the feedback was last updated
	 */
	public ZonedDateTime getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * The source of the feedback
	 */
	public CampaignFeedbackSourceType getSource() {
		return source;
	}

	/**
	 * The unique id for the campaign.
	 */
	public String getCampaignId() {
		return campaignId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return 
				"Feedback: " + getFeedbackId() + " " + getCreatedBy() + System.lineSeparator() +
				"    Campaign Id: " + getCampaignId() + System.lineSeparator() +
				"    Created: " +  DateConverter.toLocalString(getCreatedAt()) + System.lineSeparator() +
				"    Updated: " +  DateConverter.toLocalString(getUpdatedAt()) + System.lineSeparator() +
				"    Message: " + getMessage();
	}

	/**
     * Builder for {@link CampaignFeedback}
     *
     */
    public static class Builder {
    	private Integer blockId;
    	private String message;
    	private Boolean isComplete;
    	private String campaignId;
		private MailChimpConnection connection;
   	
		/**
		 * @param connection the connection to set
		 */
		public Builder connection(MailChimpConnection connection) {
			this.connection = connection;
			return this;
		}

		public Builder blockId(Integer blockId) {
            this.blockId = blockId;
            return this;
        }
        
        public Builder message(String message) {
            this.message = message;
            return this;
        }
        
        public Builder isComplete(Boolean isComplete) {
            this.isComplete = isComplete;
            return this;
        }
        
        public Builder campaignId(String campaignId) {
            this.campaignId = campaignId;
            return this;
        }
        
        public CampaignFeedback build() {
        	return new CampaignFeedback(this);
        }
    }
}
