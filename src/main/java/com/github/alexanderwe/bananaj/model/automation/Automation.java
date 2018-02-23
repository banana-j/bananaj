/**
 * @author alexanderweiss
 * @date 30.11.2015
 */
package com.github.alexanderwe.bananaj.model.automation;

import com.github.alexanderwe.bananaj.model.MailchimpObject;
import com.github.alexanderwe.bananaj.model.list.MailChimpList;
import org.json.JSONObject;

import java.time.LocalDateTime;

/**
 * Class for representing an automation
 * @author alexanderweiss
 *
 */
public class Automation extends MailchimpObject {

	private LocalDateTime create_time;
	private LocalDateTime start_time;
	private AutomationStatus status;
	private int emails_sent;
	private MailChimpList mailChimpList;
		
	public Automation(String id, LocalDateTime create_time, LocalDateTime start_time, AutomationStatus status, int emails_sent, MailChimpList mailChimpList, JSONObject jsonRepresentation) {
		super(id,jsonRepresentation);
		this.create_time = create_time;
		this.start_time = start_time;
		this.status = status;
		this.emails_sent = emails_sent;
		this.mailChimpList = mailChimpList;
		
	}

	/**
	 * @return the create_time
	 */
	public LocalDateTime getCreate_time() {
		return create_time;
	}

	/**
	 * @return the start_time
	 */
	public LocalDateTime getStart_time() {
		return start_time;
	}

	/**
	 * @return the status
	 */
	public AutomationStatus getStatus() {
		return status;
	}

	/**
	 * @return the emails_sent
	 */
	public int getEmails_sent() {
		return emails_sent;
	}

	/**
	 * @return the mailChimpList
	 */
	public MailChimpList getMailChimpList() {
		return mailChimpList;
	}

}
