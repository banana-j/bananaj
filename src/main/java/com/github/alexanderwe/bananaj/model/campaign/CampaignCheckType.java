package com.github.alexanderwe.bananaj.model.campaign;

public enum CampaignCheckType {

	SUCCESS("success"), WARNING("warning"), ERROR("error");
	
	private String stringRepresentation;
	
	CampaignCheckType(String stringRepresentation ) {
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
