/**
 * @author alexanderweiss
 * @date 05.12.2015
 */
package com.github.alexanderwe.bananaj.model.campaign;

public class CampaignDefaults {

	
	private String from_name;
	private String from_email;
	private String subject;
	private String language;
	
	public CampaignDefaults(String from_name, String from_email, String subject, String language) {
		this.from_name = from_name;
		this.from_email = from_email;
		this.subject = subject;
		this.language = language;
	}

	/**
	 * @return the from_name
	 */
	public String getFrom_name() {
		return from_name;
	}

	/**
	 * @return the from_email
	 */
	public String getFrom_email() {
		return from_email;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

}
