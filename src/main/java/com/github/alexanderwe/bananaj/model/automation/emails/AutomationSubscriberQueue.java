package com.github.alexanderwe.bananaj.model.automation.emails;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.alexanderwe.bananaj.connection.MailChimpConnection;

public class AutomationSubscriberQueue {
	private String workflowId;
	private String emailId;
	private List<AutomationSubscriber> queue;
	private int totalItems;
	private MailChimpConnection connection;

	public AutomationSubscriberQueue(MailChimpConnection connection, JSONObject jsonObj) {
		this.workflowId = jsonObj.getString("workflow_id");
		this.emailId = jsonObj.getString("email_id");
		this.queue = new ArrayList<AutomationSubscriber>();
		this.totalItems = jsonObj.getInt("total_items");
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

	public void addSubscriber() {
		// TODO: POST /automations/{workflow_id}/emails/{workflow_email_id}/queue
	}
	
	public String getWorkflowId() {
		return workflowId;
	}

	public String getEmailId() {
		return emailId;
	}

	public List<AutomationSubscriber> getQueue() {
		return queue;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public MailChimpConnection getConnection() {
		return connection;
	}

}
