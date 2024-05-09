package com.github.bananaj.model.list.member;

import java.time.ZonedDateTime;

import org.json.JSONObject;

import com.github.bananaj.utils.DateConverter;
import com.github.bananaj.utils.JSONObjectCheck;

/**
 * The most recent Note for a member.
 */
public class LastNote {
	
	private Integer id;
	private ZonedDateTime createdAt;
	private String createdBy;
	private String note;

	public LastNote(JSONObject jsonObj) {
		JSONObjectCheck jObj = new JSONObjectCheck(jsonObj);
		id = jObj.getInt("note_id");
		createdAt = jObj.getISO8601Date("created_at");
		createdBy = jObj.getString("created_by");
		note = jObj.getString("note");
	}

	/**
	 * @return The content of the note.
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return The note id.
	 */
	public Integer getId() {
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return 
				"    Note: " + getId() + " " + DateConverter.toLocalString(getCreatedAt()) + " " + getCreatedBy() + System.lineSeparator() +
				"        " + getNote(); 
	}

}
