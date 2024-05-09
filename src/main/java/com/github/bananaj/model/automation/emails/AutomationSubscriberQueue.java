package com.github.bananaj.model.automation.emails;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.utils.JSONObjectCheck;

public class AutomationSubscriberQueue {
	private String workflowId;
	private String emailId;
	private List<AutomationSubscriber> queue;
	private Integer totalItems;
	private MailChimpConnection connection;

	public AutomationSubscriberQueue(MailChimpConnection connection, JSONObject jsonObj) {
		JSONObjectCheck jObj = new JSONObjectCheck(jsonObj);
		this.workflowId = jObj.getString("workflow_id");
		this.emailId = jObj.getString("email_id");
		this.queue = new ArrayList<AutomationSubscriber>();
		this.totalItems = jObj.getInt("total_items");
		if (jsonObj.has("queue")) {
			JSONArray subscriberQueue = jsonObj.getJSONArray("queue");
			for(int i=0; i<subscriberQueue.length(); i++) {
				AutomationSubscriber subscriver = new AutomationSubscriber(subscriberQueue.getJSONObject(i));
				queue.add(subscriver);
			}
		}
        this.connection = connection;
	}

	public AutomationSubscriberQueue() {

	}

	public AutomationSubscriber addSubscriber(String emailAddress) throws Exception {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("email_address", emailAddress);
		String results = connection.do_Post(new URL(connection.getAutomationendpoint() + "/" + workflowId + "/emails/" + emailId + "/queue"), jsonObj.toString(), connection.getApikey());
		return new AutomationSubscriber(new JSONObject(results));
	}
	
	/**
	 * A string that uniquely identifies an Automation workflow
	 */
	public String getWorkflowId() {
		return workflowId;
	}

	/**
	 * A string that uniquely identifies an email in an Automation workflow
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * An array of objects, each representing a subscriber queue for an email in an Automation workflow
	 */
	public List<AutomationSubscriber> getQueue() {
		return queue;
	}

	/**
	 * The total number of items matching the query regardless of pagination
	 */
	public Integer getTotalItems() {
		return totalItems;
	}

	public MailChimpConnection getConnection() {
		return connection;
	}

}
