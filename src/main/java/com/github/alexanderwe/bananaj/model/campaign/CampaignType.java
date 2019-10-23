/**
 * @author alexanderweiss
 * @date 20.11.2015
 */
package com.github.alexanderwe.bananaj.model.campaign;

/**
 * Enum representation of different campaign types
 */
public enum CampaignType {

	REGULAR("regular"), 
	PLAINTEXT("plaintext"), 
	ABSPLIT("absplit"), 
	RSS("rss"), 
	AUTOMATION("automation"), 
	VARIATE("variate"), 
	AUTO("auto");
	
	private String stringRepresentation;
	
	CampaignType(String stringRepresentation ) {
		setStringRepresentation(stringRepresentation);
	}

	@Override
	public String toString() {
		return stringRepresentation;
	}

	/**
	 * @param Set the stringRepresentation for the enum constant.
	 */
	public void setStringRepresentation(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}
	
}
