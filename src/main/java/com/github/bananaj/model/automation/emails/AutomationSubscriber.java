package com.github.bananaj.model.automation.emails;

import java.time.ZonedDateTime;

import org.json.JSONObject;

import com.github.bananaj.model.automation.Subscriber;
import com.github.bananaj.utils.DateConverter;
import com.github.bananaj.utils.JSONObjectCheck;

public class AutomationSubscriber extends Subscriber {

	private String emailId;
	private Boolean listIsActive;
	private ZonedDateTime nextSend;

	public AutomationSubscriber(JSONObject jsonObj) {
		super(jsonObj);
		JSONObjectCheck jObj = new JSONObjectCheck(jsonObj);
		this.emailId = jObj.getString("email_id");
		this.listIsActive = jObj.getBoolean("list_is_active");
		this.nextSend = jObj.getISO8601Date("next_send");
	}

	public AutomationSubscriber() {

	}

	/**
	 * A string that uniquely identifies an email in an Automation workflow
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * The status of the list used, namely if it’s deleted or disabled
	 * @return This value is populated only when a specific subscriber in email was queried. Otherwise null. 
	 */
	public Boolean isListIsActive() {
		return listIsActive;
	}

	/**
	 * The date and time of the next send for the workflow email
	 */
	public ZonedDateTime getNextSend() {
		return nextSend;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return 
				"Subscriber:" + System.lineSeparator() +
				"    Email Id: " + getEmailId() + System.lineSeparator() +
				"    Is List Active: " + isListIsActive() + System.lineSeparator() +
				"    Next Send: " + (getNextSend()!=null ? DateConverter.toLocalString(getNextSend()) : "");
	}

}
