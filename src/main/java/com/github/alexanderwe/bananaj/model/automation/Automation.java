/**
 * @author alexanderweiss
 * @date 30.11.2015
 */
package com.github.alexanderwe.bananaj.model.automation;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.alexanderwe.bananaj.connection.MailChimpConnection;
import com.github.alexanderwe.bananaj.model.Tracking;
import com.github.alexanderwe.bananaj.model.MailchimpObject;
import com.github.alexanderwe.bananaj.model.automation.emails.AutomationEmail;
import com.github.alexanderwe.bananaj.utils.DateConverter;

/**
 * Mailchimp’s Automation feature lets you build a series of emails that
 * send to subscribers when triggered by a specific date, activity, or event.
 * Use the API to manage Automation workflows, emails, and queues.
 * 
 * @author alexanderweiss
 *
 */
public class Automation extends MailchimpObject {

	private LocalDateTime createTime;
	private LocalDateTime startTime;
	private AutomationStatus status;
	private int emailsSent;
	private AutomationRecipient recipients;
	private AutomationSettings settings;
	private Tracking tracking;
	//private AutomationTriggerSettings trigger_settings;
	//private AutomationReportSummary report_summary;
	private MailChimpConnection connection;
		
	public Automation(MailChimpConnection connection, JSONObject automation) {
		super(automation.getString("id"), automation);
		parse(connection, automation);
	}
	
	public Automation() {
		
	}

	public void parse(MailChimpConnection connection, JSONObject automation) {
		this.connection = connection;
		this.createTime = DateConverter.getInstance().createDateFromISO8601(automation.getString("create_time"));
		this.startTime = DateConverter.getInstance().createDateFromISO8601(automation.getString("start_time"));
		this.status = AutomationStatus.valueOf(automation.getString("status").toUpperCase());
		this.emailsSent = automation.getInt("emails_sent");
		if (automation.has("recipients")) {
			this.recipients = new AutomationRecipient(automation.getJSONObject("recipients"));
		}
		if (automation.has("settings")) {
			this.settings = new AutomationSettings(automation.getJSONObject("settings"));
		}
		if (automation.has("tracking")) {
			this.tracking = new Tracking(automation.getJSONObject("tracking"));
		}
	}
	
	/**
	 * 	Pause all emails in an Automation workflow
	 * @throws Exception
	 */
	public void pauseAllEmails() throws Exception {
		getConnection().do_Post(new URL(connection.getAutomationendpoint() +"/"+getId()+"/actions/pause-all-emails"), connection.getApikey());
	}
	
	/**
	 * Start all emails in an Automation workflow
	 * @throws Exception
	 */
	public void startAllEmails() throws Exception {
		getConnection().do_Post(new URL(connection.getAutomationendpoint() +"/"+getId()+"/actions/start-all-emails"), connection.getApikey());
	}

	/**
	 * Get a list of automated emails in a workflow
	 * @return List containing the first 100 emails
	 * @throws Exception 
	 */
	public List<AutomationEmail> getEmails() throws Exception {
		return getEmails(100, 0);
	}
	
	/**
	 * Get a list of automated emails in a workflow with pagination
	 * @param count Number of emails to return
	 * @param offset Zero based offset
	 * @return List containing automation emails
	 * @throws Exception
	 */
	public List<AutomationEmail> getEmails(int count, int offset) throws Exception {
		List<AutomationEmail> emails = new ArrayList<AutomationEmail>();
		JSONObject jsonObj = new JSONObject(connection.do_Get(new URL(connection.getAutomationendpoint() + "/" + getId() + "/emails" + "?offset=" + offset + "&count=" + count), connection.getApikey()));
		//int total_items = jsonAutomations.getInt("total_items"); 	// The total number of items matching the query regardless of pagination
		JSONArray emailsArray = jsonObj.getJSONArray("emails");
		for( int i = 0; i< emailsArray.length();i++)
		{
			JSONObject emailDetail = emailsArray.getJSONObject(i);
			AutomationEmail autoEmail = new AutomationEmail(connection, emailDetail);
			emails.add(autoEmail);
		}
		return emails;
	}
	
	/**
	 * Get information about a specific workflow email
	 * @param workflowEmailId
	 * @return
	 * @throws Exception
	 */
	public AutomationEmail getEmail(String workflowEmailId) throws Exception {
		JSONObject jsonObj = new JSONObject(connection.do_Get(new URL(connection.getAutomationendpoint() + "/" + getId() + "/emails/" + workflowEmailId), connection.getApikey()));
		return new AutomationEmail(connection, jsonObj);
	}
	
	/**
	 * Update Automation
	 * @throws Exception
	 */
	public void update(AutomationDelay delay) throws Exception {
		JSONObject json = getJsonRepresentation();
		if (delay != null) {
			json.put("delay", delay.getJsonRepresentation());
		}
		String results = getConnection().do_Patch(new URL(connection.getAutomationendpoint()+"/"+this.getId()), json.toString(), connection.getApikey());
		parse(this.connection, new JSONObject(results));  // update member automation with current data
	}
	
	/**
	 * The date and time the Automation was created
	 * @return
	 */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * The date and time the Automation was started
	 * @return
	 */
	public LocalDateTime getStartTime() {
		return startTime;
	}

	/**
	 * The current status of the Automation
	 * @return
	 */
	public AutomationStatus getStatus() {
		return status;
	}

	/**
	 * The total number of emails sent for the Automation
	 * @return
	 */
	public int getEmailsSent() {
		return emailsSent;
	}

	/**
	 * List settings for the Automation
	 * @return
	 */
	public AutomationRecipient getRecipients() {
		return recipients;
	}

	/**
	 * The settings for the Automation workflow
	 * @return
	 */
	public AutomationSettings getSettings() {
		return settings;
	}

	/**
	 * The tracking options for the Automation
	 * @return
	 */
	public Tracking getTracking() {
		return tracking;
	}
	
	/**
	 * @return the MailChimp {@link #connection}
	 */
	public MailChimpConnection getConnection() {
		return connection;
	}
	
	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 * @return
	 */
	public JSONObject getJsonRepresentation() throws Exception {
		JSONObject json = new JSONObject();
		
		if (recipients != null) {
			JSONObject recipientsObj = recipients.getJsonRepresentation();
			json.put("recipients", recipientsObj);
		}
		
		if (settings != null) {
			JSONObject settingsObj = settings.getJsonRepresentation();
			json.put("settings", settingsObj);
		}
		
		// TODO: add delay object
		// TODO: add trigger_settings object
		return json;
	}
	
	// TODO: add builder pattern
}
