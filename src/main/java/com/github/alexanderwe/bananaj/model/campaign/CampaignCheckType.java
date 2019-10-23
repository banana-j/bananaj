package com.github.alexanderwe.bananaj.model.campaign;

public enum CampaignCheckType {

	SUCCESS("success"), 
	WARNING("warning"), 
	ERROR("error");

	private String stringRepresentation;

	CampaignCheckType(String stringRepresentation ) {
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
