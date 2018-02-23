/**
 * @author alexanderweiss
 * @date 20.11.2015
 */
package model.campaign;

public enum CampaignStatus {

	
	SAVE("save"), PAUSED("paused"), SCHEDULE("schedule"), SENDING("sending"), SENT("sent");
	
	private String stringRepresentation;
	
	CampaignStatus(String stringRepresentation ){
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
