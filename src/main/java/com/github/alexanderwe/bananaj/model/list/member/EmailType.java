package com.github.alexanderwe.bananaj.model.list.member;

import java.util.HashMap;
import java.util.Map;

public enum EmailType {

	HTML("html"), TEXT("text");

	private String stringRepresentation;
	
	EmailType(String stringRepresentation ){
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
