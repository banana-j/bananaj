/**
 * @author alexanderweiss
 * @date 06.12.2015
 */
package model.campaign;

/**
 * Class for representing settings for a campaign
 * @author alexanderweiss
 *
 */
public class CampaignSettings {
	
	private String subject_line;
	private String title;
	private String from_name;
	private String reply_to;
	
	public CampaignSettings(String subject_line, String title, String from_name, String reply_to) {
		this.subject_line = subject_line;
		this.title = title;
		this.from_name = from_name;
		this.reply_to = reply_to;
	}

	/**
	 * @return the subject_line
	 */
	public String getSubject_line() {
		return subject_line;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the from_name
	 */
	public String getFrom_name() {
		return from_name;
	}

	/**
	 * @return the reply_to
	 */
	public String getReply_to() {
		return reply_to;
	}

}
