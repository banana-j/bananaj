package com.github.alexanderwe.bananaj.model.list.member;

import java.time.LocalDateTime;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.utils.DateConverter;

/**
 * The most recent Note for a member.
 */
public class LastNote {
	
	private int id;
	private LocalDateTime createdAt;
	private String createdBy;
	private String note;

	public LastNote(JSONObject jsonObj) {
		id = jsonObj.getInt("note_id");
		createdAt = DateConverter.getInstance().createDateFromISO8601(jsonObj.getString("created_at"));
		createdBy = jsonObj.getString("created_by");
		note = jsonObj.getString("note");
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
	public int getId() {
		return id;
	}

	/**
	 * @return The date and time the note was created.
	 */
	public LocalDateTime getCreatedAt() {
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
				"    Note: " + getId() + " " + getCreatedAt() + " " + getCreatedBy() + System.lineSeparator() +
				"        " + getNote(); 
	}

}
