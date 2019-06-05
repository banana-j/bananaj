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

	public AutomationDelay(int amount, DelayType type, DelayDirection direction, DelayAction action) {
		super();
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
	 * he delay amount for an automation email
	 * @return
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
	 * @return
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
	 * @return
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
	 * @return
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
	 * @return
	 */
	public String getActionDescription() {
		return actionDescription;
	}

	/**
	 * The user-friendly description of the delay and trigger action settings for an Automation email
	 * @return
	 */
	public String getFullDescription() {
		return fullDescription;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 * @return
	 */
	public JSONObject getJsonRepresentation() throws Exception {
		JSONObject json = new JSONObject();

		json.put("amount", amount);
		json.put("type", type.getStringRepresentation());
		json.put("direction", direction.getStringRepresentation());
		json.put("action", action.getStringRepresentation());

		return json;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return
		"Delay:" + System.lineSeparator() +
		"    amount: " + getAmount() + System.lineSeparator() +
		"    Type: " + getType().getStringRepresentation() + System.lineSeparator() +
		"    Direction: " + getDirection().getStringRepresentation() + System.lineSeparator() +
		"    Action: " + getAction().getStringRepresentation() + System.lineSeparator() +
		"    Action Description: " + getActionDescription() + System.lineSeparator() +
		"    Full Description: " + getFullDescription(); 
	}
	
	
}
