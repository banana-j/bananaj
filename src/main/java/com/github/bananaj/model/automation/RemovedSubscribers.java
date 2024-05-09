package com.github.bananaj.model.automation;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.bananaj.utils.JSONObjectCheck;

/**
 * 
 *
 */
public class RemovedSubscribers extends Subscriber {

	private String workflowId;
	private List<Subscriber> subscribers = new ArrayList<Subscriber>();
	private Integer totalItems;
	
	public RemovedSubscribers(JSONObject jsonObj) {
		super(jsonObj);
		JSONObjectCheck jObj = new JSONObjectCheck(jsonObj);
		this.workflowId = jObj.getString("workflow_id"); 
		this.totalItems = jObj.getInt("workflow_id");
		if(jsonObj.has("subscribers")) {
			JSONArray list = jsonObj.getJSONArray("subscribers");
			for(int i=0; i<list.length(); i++) {
				JSONObject subscriberJson = list.getJSONObject(i);
				subscribers.add(new Subscriber(subscriberJson));
			}
		}
	}

	public RemovedSubscribers() {

	}

	/**
	 * A string that uniquely identifies an Automation workflow
	 */
	public String getWorkflowId() {
		return workflowId;
	}

	/**
	 * An array of objects, each representing a subscriber who was removed from an Automation workflow
	 */
	public List<Subscriber> getSubscribers() {
		return subscribers;
	}

	/**
	 * The total number of items matching the query regardless of pagination
	 */
	public Integer getTotalItems() {
		return totalItems;
	}

	/* (non-Javadoc)
	 * @see com.github.bananaj.model.automation.Subscriber#toString()
	 */
	@Override
	public String toString() {
		return 
				"Removed Subscribers:" + System.lineSeparator() +
				"    Workflow Id: " + getWorkflowId() + System.lineSeparator() +
				"    Total Items: " + getTotalItems() + System.lineSeparator() +
				super.toString();
	}
	
}
