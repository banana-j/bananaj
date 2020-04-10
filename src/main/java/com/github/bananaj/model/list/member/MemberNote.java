package com.github.bananaj.model.list.member;

import java.time.ZonedDateTime;

import org.json.JSONObject;

import com.github.bananaj.utils.DateConverter;

/**
 * Member Notes
 */
public class MemberNote {

	private int id;
	private ZonedDateTime createdAt;
	private String createdBy;
	private ZonedDateTime updatedAt;
	private String note;
	private String listId;
	private String emailId;

	public MemberNote(JSONObject jsonObj) {
		id = jsonObj.getInt("id");
		createdAt = DateConverter.fromISO8601(jsonObj.getString("created_at"));
		createdBy = jsonObj.getString("created_by");
		updatedAt = DateConverter.fromISO8601(jsonObj.getString("updated_at"));
		note = jsonObj.getString("note");
		listId = jsonObj.getString("list_id");
		emailId = jsonObj.getString("email_id");
	}

	/**
	 * Member note.
	 * @param note The content of the note. Note length is limited to 1,000 characters.
	 */
	public MemberNote(String note) {
		this.note = note;
	}
	
	/**
	 * @return The content of the note.
	 */
	public String getNote() {
		return note;
	}

	/**
	 * The content of the note. Note length is limited to 1,000 characters.
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return The note id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return The date and time the note was created.
	 */
	public ZonedDateTime getCreatedAt() {
		return createdAt;
	}

	/**
	 * @return The author of the note.
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @return The date and time the note was last updated.
	 */
	public ZonedDateTime getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @return The unique id for the list.
	 */
	public String getListId() {
		return listId;
	}

	/**
	 * @return The MD5 hash of the lowercase version of the list member’s email address.
	 */
	public String getEmailId() {
		return emailId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return 
				"Note: " + getId() + " " + DateConverter.toLocalString(getCreatedAt()) + " " + getCreatedBy() + System.lineSeparator() +
				"    Updated: " + DateConverter.toLocalString(getUpdatedAt()) + System.lineSeparator() +
				"    List Id: " + getListId() + System.lineSeparator() +
				"    Email Id: " + getEmailId() + System.lineSeparator() +
				"    Message: " + getNote(); 
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
	protected JSONObject getJsonRepresentation() throws Exception {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("note", note);
		return jsonObj;
	}
	
}
