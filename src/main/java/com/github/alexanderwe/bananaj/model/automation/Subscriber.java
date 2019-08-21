package com.github.alexanderwe.bananaj.model.automation;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.model.MailchimpObject;

public class Subscriber extends MailchimpObject {
	private String workflowId;
	private String listId;
	private String emailAddress;

	public Subscriber(JSONObject jsonObj) {
		super(jsonObj.getString("id"), jsonObj);
		this.workflowId = jsonObj.getString("workflow_id");
		this.listId = jsonObj.getString("list_id");
		this.emailAddress = jsonObj.getString("email_address");
	}

	public Subscriber() {

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
