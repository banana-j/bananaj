package com.github.bananaj.model.list.segment;

/**
 * Created by alexanderweiss on 04.02.16.
 */
public enum SegmentType {

	/**
	 * Saved or group segments
	 */
    SAVED("saved"),
    /**
     * Static segments are now known as tags
     */
    STATIC("static"),
    FUZZY("fuzzy");

	private String stringRepresentation;
	
	SegmentType(String stringRepresentation ) {
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
