/**
 * @author alexanderweiss
 * @date 05.12.2015
 */
package model.campaign;

public class CampaignDefaults {

	
	private String from_name;
	private String from_email;
	private String subject;
	private String language;
	
	public CampaignDefaults(String from_name, String from_email, String subject, String language) {
		setFrom_name(from_name);
		setFrom_email(from_email);
		setSubject(subject);
		setLanguage(language);
	}

	/**
	 * @return the from_name
	 */
	public String getFrom_name() {
		return from_name;
	}

	/**
	 * @param from_name the from_name to set
	 */
	public void setFrom_name(String from_name) {
		this.from_name = from_name;
	}

	/**
	 * @return the from_email
	 */
	public String getFrom_email() {
		return from_email;
	}

	/**
	 * @param from_email the from_email to set
	 */
	public void setFrom_email(String from_email) {
		this.from_email = from_email;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

}
