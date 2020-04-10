package com.github.bananaj.model.list.interests;

public enum InterestCategoryType {

	CHECKBOXES("checkboxes"),
	DROPDOWN("dropdown"),
	RADIO("radio"),
	HIDDEN("hidden");

	private String stringRepresentation;

	InterestCategoryType(String stringRepresentation ) {
		setStringRepresentation(stringRepresentation);
	}

	@Override
	public String toString() {
		return stringRepresentation;
	}

	/**
	 * @param stringRepresentation Set the stringRepresentation for the enum constant.
	 */
	private void setStringRepresentation(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}

}
