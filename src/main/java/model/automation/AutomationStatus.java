/**
 * @author alexanderweiss
 * @date 30.11.2015
 */
package model.automation;

public enum AutomationStatus {

	SAVE("save"),PAUSED("paused"),SENDING("sending");

	
	private String stringRepresentation;
	
	AutomationStatus(String stringRepresentation){
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


