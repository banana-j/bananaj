package com.github.bananaj.model.campaign;

/**
 * 
 */
public enum CampaignFeedbackSourceType {

	API("api"),
	EMAIL("email"),
	SMS("sms"),
	WEB("web"),
	IOS("ios"),
	ANDROID("android");

	private String stringRepresentation;

	CampaignFeedbackSourceType(String stringRepresentation) {
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
