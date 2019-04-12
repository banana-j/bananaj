package com.github.alexanderwe.bananaj.model.list.segment;

import org.json.JSONObject;

/**
 * 
 */
public interface AbstractCondition {

	/**
	 * Helper method to convert JSON for mailchimp PUT/PATCH/POST operations
	 * @return
	 */
    public JSONObject getJsonRepresentation();
}
