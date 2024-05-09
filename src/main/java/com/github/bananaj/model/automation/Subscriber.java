package com.github.bananaj.model.automation;

import org.json.JSONObject;

import com.github.bananaj.utils.JSONObjectCheck;

/**
 * Mailchimp's Automation feature lets you build a series of emails that send to
 * subscribers when triggered by a specific date, activity, or event. Use the
 * API to manage Automation workflows, emails, and queues.
 *
 */
public class Subscriber {

	private String id;
	private String workflowId;
	private String listId;
	private String emailAddress;

	public Subscriber(JSONObject jsonObj) {
		JSONObjectCheck jObj = new JSONObjectCheck(jsonObj);
		id = jObj.getString("id");
		this.workflowId = jObj.getString("workflow_id");
		this.listId = jObj.getString("list_id");
		this.emailAddress = jObj.getString("email_address");
	}

	public Subscriber() {

	}

	/**
	 * @return The MD5 hash of the lowercase version of the list member's email address.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * A string that uniquely identifies an Automation workflow
	 */
	public String getWorkflowId() {
		return workflowId;
	}

	/**
	 * A string that uniquely identifies a list
	 */
	public String getListId() {
		return listId;
	}

	/**
	 * The list member’s email address
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return 
				"    Subscriber Id: " + getId() + System.lineSeparator() +
				"    Workflow Id: " + getWorkflowId() + System.lineSeparator() +
				"    List Id: " + getListId() + System.lineSeparator() +
				"    Email Address: " + getEmailAddress();
	}
}
