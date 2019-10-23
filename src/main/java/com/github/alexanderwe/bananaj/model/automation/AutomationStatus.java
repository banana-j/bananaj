/**
 * @author alexanderweiss
 * @date 30.11.2015
 */
package com.github.alexanderwe.bananaj.model.automation;

public enum AutomationStatus {

	SAVE("save"),
	PAUSED("paused"),
	SENDING("sending");


	private String stringRepresentation;

	AutomationStatus(String stringRepresentation) {
		setStringRepresentation(stringRepresentation);
	}

	@Override
	public String toString() {
		return stringRepresentation;
	}

	/**
	 * @param Set the stringRepresentation for the enum constant.
	 */
	private void setStringRepresentation(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}

}


