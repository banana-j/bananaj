package com.github.alexanderwe.bananaj.model.campaign;

public enum CampaignContentType {

	TEMPLATE("template"), DRAG_AND_DROP("drag_and_drop"), HTML("html"), URL("url");
	
	private String stringRepresentation;
	
	CampaignContentType(String stringRepresentation ) {
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
