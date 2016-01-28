/**
 * @author alexanderweiss
 * @date 20.11.2015
 */
package model.campaign;

public enum CampaignType {

	
	REGULAR("regular"), PLAINTEXT("plaintext"), ABSPLIT("absplit"), RSS("rss"), AUTOMATION("automation"), VARIATE("variate"), AUTO("auto");
	
	private String stringRepresentation;
	
	CampaignType(String stringRepresentation ){
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
	public void setStringRepresentation(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}
	



	
	
}
