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
		setSubject_line(subject_line);
		setTitle(title);
		setFrom_name(from_name);
		setReply_to(reply_to);
	}

	/**
	 * @return the subject_line
	 */
	public String getSubject_line() {
		return subject_line;
	}

	/**
	 * @param subject_line the subject_line to set
	 */
	public void setSubject_line(String subject_line) {
		this.subject_line = subject_line;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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
	 * @return the reply_to
	 */
	public String getReply_to() {
		return reply_to;
	}

	/**
	 * @param reply_to the reply_to to set
	 */
	public void setReply_to(String reply_to) {
		this.reply_to = reply_to;
	}

}
