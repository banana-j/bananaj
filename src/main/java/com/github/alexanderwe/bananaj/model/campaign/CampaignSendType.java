package com.github.alexanderwe.bananaj.model.campaign;

/**
 * Enum representation of different campaign send types
 */
public enum CampaignSendType {

	HTML("html"),
	PLAINTEXT("plaintext");

	private String stringRepresentation;
	
	CampaignSendType(String stringRepresentation ) {
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
