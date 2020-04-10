package com.github.bananaj.model.campaign;

public enum CampaignContentType {

	TEMPLATE("template"), 
	DRAG_AND_DROP("drag_and_drop"), 
	HTML("html"), 
	URL("url");
	
	private String stringRepresentation;
	
	CampaignContentType(String stringRepresentation ) {
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
