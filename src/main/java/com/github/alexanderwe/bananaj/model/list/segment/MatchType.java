package com.github.alexanderwe.bananaj.model.list.segment;

/**
 * Created by alexanderweiss on 27.12.16.
 */
public enum MatchType {

    ANY("any"),
    ALL("all");
	
	private String stringRepresentation;
	
	MatchType(String stringRepresentation ) {
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
