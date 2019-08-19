package com.github.alexanderwe.bananaj.model.automation.emails;

import java.time.LocalDateTime;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.model.automation.Subscriber;
import com.github.alexanderwe.bananaj.utils.DateConverter;

public class AutomationSubscriber extends Subscriber {

	private String emailId;
	private Boolean listIsActive;
	private LocalDateTime nextSend;

	public AutomationSubscriber(JSONObject jsonObj) {
		super(jsonObj);
		this.emailId = jsonObj.getString("email_id");
		if (jsonObj.has("list_is_active")) {
			this.listIsActive = jsonObj.getBoolean("list_is_active");
		}
        this.nextSend = DateConverter.getInstance().createDateFromISO8601(jsonObj.getString("next_send"));
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
	public LocalDateTime getNextSend() {
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
				"    Next Send: " + getNextSend() + System.lineSeparator() +
				super.toString();
	}

}
