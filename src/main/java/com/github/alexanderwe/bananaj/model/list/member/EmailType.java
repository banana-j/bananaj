package com.github.alexanderwe.bananaj.model.list.member;

public enum EmailType {

	HTML("html"), 
	TEXT("text");

	private String stringRepresentation;
	
	EmailType(String stringRepresentation ) {
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
