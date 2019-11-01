package com.github.alexanderwe.bananaj.model.automation;

import org.json.JSONObject;

public class AutomationDelay {
	Integer amount;
	DelayType type;
	DelayDirection direction;
	DelayAction action;
	String actionDescription;
	String fullDescription;

	public AutomationDelay() {
		amount = 1;
		type = DelayType.DAY;
		direction = DelayDirection.AFTER;
		action = DelayAction.SIGNUP;
	}

	public AutomationDelay(Integer amount, DelayType type, DelayDirection direction, DelayAction action) {
		this.amount = amount;
		this.type = type;
		this.direction = direction;
		this.action = action;
	}

	public AutomationDelay(JSONObject jsonObj) {
		amount = jsonObj.has("amount") ? jsonObj.getInt("amount") : null;
		type = DelayType.valueOf(jsonObj.getString("type").toUpperCase());
		direction = jsonObj.has("direction") ? DelayDirection.valueOf(jsonObj.getString("direction").toUpperCase()) : null;
		action = DelayAction.valueOf(jsonObj.getString("action").toUpperCase());
		actionDescription = jsonObj.has("action_description") ? jsonObj.getString("action_description") : null;
		fullDescription = jsonObj.has("full_description") ? fullDescription = jsonObj.getString("full_description") : null;
	}
	
	/**
	 * The delay amount for an automation email
	 */
	public Integer getAmount() {
		return amount;
	}

	/**
	 * he delay amount for an automation email
	 * @param amount
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * The type of delay for an automation email
	 */
	public DelayType getType() {
		return type;
	}

	/**
	 * The type of delay for an automation email
	 * @param type
	 */
	public void setType(DelayType type) {
		this.type = type;
	}

	/**
	 * Whether the delay settings describe before or after the delay action of an automation email
	 */
	public DelayDirection getDirection() {
		return direction;
	}

	/**
	 * Whether the delay settings describe before or after the delay action of an automation email
	 * @param direction
	 */
	public void setDirection(DelayDirection direction) {
		this.direction = direction;
	}

	/**
	 * The action that triggers the delay of an automation emails
	 */
	public DelayAction getAction() {
		return action;
	}

	/**
	 * The action that triggers the delay of an automation emails
	 * @param action
	 */
	public void setAction(DelayAction action) {
		this.action = action;
	}

	/**
	 * The user-friendly description of the action that triggers an Automation email
	 */
	public String getActionDescription() {
		return actionDescription;
	}

	/**
	 * The user-friendly description of the delay and trigger action settings for an Automation email
	 */
	public String getFullDescription() {
		return fullDescription;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
	public JSONObject getJsonRepresentation() throws Exception {
		JSONObject json = new JSONObject();

		json.put("amount", amount);
		json.put("type", type.toString());
		json.put("direction", direction.toString());
		json.put("action", action.toString());

		return json;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return
		"Delay:" + System.lineSeparator() +
		"    Type: " + getType().toString() + System.lineSeparator() +
		"    Action: " + getAction().toString() + System.lineSeparator() +
		(getAmount() != null ? "    amount: " + getAmount() + System.lineSeparator() : "") +
		(getDirection() != null ? "    Direction: " + getDirection().toString() + System.lineSeparator() : "") +
		(getActionDescription() != null ? "    Action Description: " + getActionDescription() + System.lineSeparator() : "") +
		(getFullDescription() != null ? "    Full Description: " + getFullDescription() : ""); 
	}
	
	
}
