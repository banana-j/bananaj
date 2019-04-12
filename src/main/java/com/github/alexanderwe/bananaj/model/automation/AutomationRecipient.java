package com.github.alexanderwe.bananaj.model.automation;

import org.json.JSONObject;

public class AutomationRecipient {

	private String listId;
	private boolean listIsActive;
	private String listName;
	//private SegmentOpts segment_opts;
	private String storeId;

	/**
	 * Construct AutomationRecipient for automation creation operation. 
	 * @param listId
	 * @param storeId
	 */
	public AutomationRecipient(String listId, String storeId) {
		super();
		this.listId = listId;
		this.storeId = storeId;
	}

	public AutomationRecipient(JSONObject recipients) {
		this.listId = recipients.getString("list_id");
		this.listIsActive = recipients.getBoolean("list_is_active");
		this.listName = recipients.getString("list_name");
		if (recipients.has("store_id")) {
			this.storeId = recipients.getString("store_id");
		}
	}

	public AutomationRecipient() {

	}

	/**
	 * The unique list id
	 * @return
	 */
	public String getListId() {
		return listId;
	}

	/**
	 * The unique list id
	 * @param listId
	 */
	public void setListId(String listId) {
		this.listId = listId;
	}

	/**
	 * The id of the store
	 * @return
	 */
	public String getStoreId() {
		return storeId;
	}

	/**
	 * The id of the store
	 * @param storeId
	 */
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	/**
	 * The status of the list used, namely if it’s deleted or disabled
	 * @return
	 */
	public boolean isListIsActive() {
		return listIsActive;
	}

	/**
	 * List Name
	 * @return
	 */
	public String getListName() {
		return listName;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 * @return
	 */
	public JSONObject getJsonRepresentation() throws Exception {
		JSONObject json = new JSONObject();

		if (listId != null) {
			json.put("list_id", listId);
		}
		if (storeId != null) {
			json.put("store_id", storeId);
		}

		return json;
	}
}
