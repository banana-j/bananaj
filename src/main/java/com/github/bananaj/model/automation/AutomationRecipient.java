package com.github.bananaj.model.automation;

import org.json.JSONObject;

import com.github.bananaj.utils.JSONObjectCheck;

public class AutomationRecipient {

	private String listId;
	private Boolean listIsActive;
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
		JSONObjectCheck jObj = new JSONObjectCheck(recipients);
		this.listId = jObj.getString("list_id");
		this.listIsActive = jObj.getBoolean("list_is_active");
		this.listName = jObj.getString("list_name");
		this.storeId = jObj.getString("store_id");
	}

	public AutomationRecipient() {

	}

	/**
	 * The unique list id
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
	 */
	public Boolean isListIsActive() {
		return listIsActive;
	}

	/**
	 * List Name
	 */
	public String getListName() {
		return listName;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return 
				"Recipients:" + System.lineSeparator() +
				"    List Id: " + getListId() + System.lineSeparator() +
				"    List Name: " + getListName() + System.lineSeparator() +
				"    List is Active: " + isListIsActive() + System.lineSeparator() +
				"    Store Id: " + getStoreId();
	}
	
}
