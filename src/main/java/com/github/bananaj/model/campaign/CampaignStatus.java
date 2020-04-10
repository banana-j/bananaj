/**
 * @author alexanderweiss
 * @date 20.11.2015
 */
package com.github.bananaj.model.campaign;

public enum CampaignStatus {

	SAVE("save"), 
	PAUSED("paused"), 
	SCHEDULE("schedule"), 
	SENDING("sending"), 
	SENT("sent");
	
	private String stringRepresentation;
	
	CampaignStatus(String stringRepresentation ) {
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
