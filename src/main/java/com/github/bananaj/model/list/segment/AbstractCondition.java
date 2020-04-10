package com.github.bananaj.model.list.segment;

import org.json.JSONObject;

/**
 * 
 */
public interface AbstractCondition {

	/**
	 * @return The type of segment, for example: date, language, Mandrill, static, and more.
	 */
    public ConditionType getConditionType();

    /**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
    public JSONObject getJsonRepresentation();
    
}
