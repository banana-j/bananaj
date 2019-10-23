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
public class Automation {

	private String id;
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
		parse(connection, automation);
	}

	public Automation() {

	}

	private void parse(MailChimpConnection connection, JSONObject jsonObj) {
		id = jsonObj.getString("id");
		this.connection = connection;
		createTime = DateConverter.getInstance().createDateFromISO8601(jsonObj.getString("create_time"));
		startTime = DateConverter.getInstance().createDateFromISO8601(jsonObj.getString("start_time"));
		status = AutomationStatus.valueOf(jsonObj.getString("status").toUpperCase());
		emailsSent = jsonObj.getInt("emails_sent");
		if (jsonObj.has("recipients")) {
			recipients = new AutomationRecipient(jsonObj.getJSONObject("recipients"));
		}
		if (jsonObj.has("settings")) {
			settings = new AutomationSettings(jsonObj.getJSONObject("settings"));
		}
		if (jsonObj.has("tracking")) {
			tracking = new Tracking(jsonObj.getJSONObject("tracking"));
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
		String results = getConnection().do_Patch(new URL(connection.getAutomationendpoint()+"/"+getId()), json.toString(), connection.getApikey());
		parse(connection, new JSONObject(results));  // update member automation with current data
	}

	/**
	 * A string that identifies the Automation.
	 */
	public String getId() {
		return id;
	}

	/**
	 * The date and time the Automation was created
	 */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * The date and time the Automation was started
	 */
	public LocalDateTime getStartTime() {
		return startTime;
	}

	/**
	 * The current status of the Automation
	 */
	public AutomationStatus getStatus() {
		return status;
	}

	/**
	 * The total number of emails sent for the Automation
	 */
	public int getEmailsSent() {
		return emailsSent;
	}

	/**
	 * List settings for the Automation
	 */
	public AutomationRecipient getRecipients() {
		return recipients;
	}

	/**
	 * The settings for the Automation workflow
	 */
	public AutomationSettings getSettings() {
		return settings;
	}

	/**
	 * The tracking options for the Automation
	 */
	public Tracking getTracking() {
		return tracking;
	}

	/**
	 * The MailChimp connection {@link #connection}
	 */
	public MailChimpConnection getConnection() {
		return connection;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
	private JSONObject getJsonRepresentation() throws Exception {
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return
				"Automation Email:" + System.lineSeparator() +
				"    Id: " + getId() + System.lineSeparator() +
				"    Created: " + getCreateTime() + System.lineSeparator() +
				"    Started: " + getStartTime() + System.lineSeparator() +
				"    Status: " + getStatus().toString() + System.lineSeparator() +
				"    Emails Sent: " + getEmailsSent() + System.lineSeparator() +
				getRecipients().toString() + System.lineSeparator() +
				getSettings().toString() + System.lineSeparator() +
				getTracking().toString(); 
	}

	// TODO: add builder pattern
}
