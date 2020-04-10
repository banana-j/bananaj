package com.github.bananaj.model.list;

/**
 * Whether a list is public or private.
 */
public enum ListVisibility {

	PUB("pub"),
	PRV("prv");
	
	private String stringRepresentation;
	
	ListVisibility(String stringRepresentation ) {
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
