package com.github.alexanderwe.bananaj.model.list.member;

import org.json.JSONObject;

/**
 * A tag that has been assigned to a contact.
 */
public class MemberTag {
	private String name;
	private Integer id;

	public MemberTag(JSONObject jsonObj) {
		name = jsonObj.getString("name");
		id = jsonObj.getInt("id");
	}

	public MemberTag(String tagName) {
		name = tagName;
		id = null;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Tag: " + getId() + " " + getName();
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
	public JSONObject getJsonRepresentation() {
		JSONObject jsonObj = new JSONObject();

		jsonObj.put("name", name);

		if (id != null) {
			jsonObj.put("id", id.intValue());
		}

		return jsonObj;
	}
}
