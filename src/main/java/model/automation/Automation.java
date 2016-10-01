/**
 * @author alexanderweiss
 * @date 30.11.2015
 */
package model.automation;

import java.util.Date;

import model.list.MailChimpList;
import org.json.JSONObject;

import model.MailchimpObject;

/**
 * Class for representing an automation
 * @author alexanderweiss
 *
 */
public class Automation extends MailchimpObject {

	private Date create_time;
	private Date start_time;
	private AutomationStatus status;
	private int emails_sent;
	private MailChimpList mailChimpList;
		
	public Automation(String id, Date create_time, Date start_time, AutomationStatus status, int emails_send, MailChimpList mailChimpList, JSONObject jsonRepresentation) {
		super(id,jsonRepresentation);
		setCreate_time(create_time);
		setStart_time(start_time);
		setStatus(status);
		setEmails_sent(emails_sent);
		setMailChimpList(mailChimpList);
		
	}

	/**
	 * @return the create_time
	 */
	public Date getCreate_time() {
		return create_time;
	}
	
	/**
	 * @param create_time the create_time to set
	 */
	private void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the start_time
	 */
	public Date getStart_time() {
		return start_time;
	}

	/**
	 * @param start_time the start_time to set
	 */
	private void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	/**
	 * @return the status
	 */
	public AutomationStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	private void setStatus(AutomationStatus status) {
		this.status = status;
	}
	
	/**
	 * @return the emails_sent
	 */
	public int getEmails_sent() {
		return emails_sent;
	}

	/**
	 * @param emails_sent the emails_sent to set
	 */
	private void setEmails_sent(int emails_sent) {
		this.emails_sent = emails_sent;
	}

	/**
	 * @return the mailChimpList
	 */
	public MailChimpList getMailChimpList() {
		return mailChimpList;
	}
	
	/**
	 * @param mailChimpList the mailChimpList to set
	 */
	private void setMailChimpList(MailChimpList mailChimpList) {
		this.mailChimpList = mailChimpList;
	}
}
