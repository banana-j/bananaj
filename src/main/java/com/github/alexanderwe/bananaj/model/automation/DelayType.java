package com.github.alexanderwe.bananaj.model.automation;

public enum DelayType {

	NOW("now"), DAY("day"), HOUR("hour"), WEEK("week");
	
	private String stringRepresentation;
	
	DelayType(String stringRepresentation ) {
		setStringRepresentation(stringRepresentation);
	}

	/**
	 * @return the stringRepresentation
	 */
	public String getStringRepresentation() {
		return stringRepresentation;
	}

	/**
	 * @param stringRepresentation the stringRepresentation to set
	 */
	private void setStringRepresentation(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}
}
