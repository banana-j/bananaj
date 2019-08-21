package com.github.alexanderwe.bananaj.model.list;

/**
 * Whether a list is public or private.
 */
public enum ListVisibility {

	PUB("pub"),PRV("prv");
	
	private String stringRepresentation;
	
	ListVisibility(String stringRepresentation ) {
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
