package com.github.bananaj.model;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;

/**
 * Implementing this interface allows an object to be the target of
 * the "ModelIterator." See {@link com.github.bananaj.utils.ModelIterator} 
 *
 */
public interface JSONParser {
	/**
	 * Parse a JSON representation of a member into this.
	 * @param connection
	 * @param entity
	 */
	public void parse(MailChimpConnection connection, JSONObject entity);

}
