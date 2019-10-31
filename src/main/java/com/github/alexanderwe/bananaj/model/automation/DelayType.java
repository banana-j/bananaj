package com.github.alexanderwe.bananaj.model.automation;

public enum DelayType {

	NOW("now"), 
	DAY("day"), 
	HOUR("hour"), 
	WEEK("week");

	private String stringRepresentation;

	DelayType(String stringRepresentation ) {
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
