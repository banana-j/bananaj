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
