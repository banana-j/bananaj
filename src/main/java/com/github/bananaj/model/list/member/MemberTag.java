package com.github.bananaj.model.list.member;

import java.time.ZonedDateTime;

import org.json.JSONObject;

import com.github.bananaj.utils.DateConverter;

/**
 * A tag that has been assigned to a contact.
 */
public class MemberTag {
	private String name;
	private Integer id;
	private ZonedDateTime dateAdded;

	public MemberTag(JSONObject jsonObj) {
		name = jsonObj.getString("name");
		id = jsonObj.getInt("id");
		if (jsonObj.has("date_added")) {
			dateAdded = DateConverter.fromISO8601(jsonObj.getString("date_added"));
		}
	}

	public MemberTag(String tagName) {
		name = tagName;
		id = null;
		dateAdded = null;
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
