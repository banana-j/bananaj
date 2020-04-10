package com.github.bananaj.model.list.mergefield;

public enum MergeFieldType {
	TEXT("text"),
	NUMBER("number"),
	ADDRESS("address"),
	PHONE("phone"),
	DATE("date"),
	URL("url"),
	IMAGEURL("imageurl"),
	RADIO("radio"),
	DROPDOWN("dropdown"),
	BIRTHDAY("birthday"),
	ZIP("zip");

	private String stringRepresentation;

	MergeFieldType(String stringRepresentation ) {
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
