package com.github.bananaj.model.list.member;

import java.time.ZonedDateTime;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.utils.DateConverter;
import com.github.bananaj.utils.JSONObjectCheck;

/**
 * A tag that has been assigned to a contact.
 */
public class MemberTag implements JSONParser {
	private String name;
	private Integer id;
	private ZonedDateTime dateAdded;

	public MemberTag() {
		
	}
	
	public MemberTag(JSONObject jsonObj) {
		parse(null, jsonObj);
	}

	public MemberTag(String tagName) {
		name = tagName;
		id = null;
		dateAdded = null;
	}

	/**
	 * Parse a JSON representation of member tag into this.
	 * @param connection Not used
	 * @param jsonObj
	 */
	public void parse(MailChimpConnection connection, JSONObject jsonObj) {
		JSONObjectCheck jObj = new JSONObjectCheck(jsonObj);
		name = jObj.getString("name");
		id = jObj.getInt("id");
		dateAdded = jObj.getISO8601Date("date_added");
	}
	
	/**
	 * @return The name of the tag.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The unique id for the tag.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return The date and time the tag was added to the list member.
	 */
	public ZonedDateTime getDateAdded() {
		return dateAdded;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Tag: " + getId() + " " + getName();
	}

//	/**
//	 * Helper method to convert JSON for mailchimp PATCH/POST operations
//	 */
//	protected JSONObject getJsonRepresentation() {
//		JSONObject jsonObj = new JSONObject();
//
//		jsonObj.put("name", name);
//
//		if (id != null) {
//			jsonObj.put("id", id.intValue());
//		}
//
//		return jsonObj;
//	}
}
