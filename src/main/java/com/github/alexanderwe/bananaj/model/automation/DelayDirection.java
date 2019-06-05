package com.github.alexanderwe.bananaj.model.automation;

public enum DelayDirection {
	
	AFTER("after"), BEFORE("before");

	private String stringRepresentation;
	
	DelayDirection(String stringRepresentation ) {
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
