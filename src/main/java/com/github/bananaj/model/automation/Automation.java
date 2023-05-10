/**
 * @author alexanderweiss
 * @date 30.11.2015
 */
package com.github.bananaj.model.automation;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.model.Tracking;
import com.github.bananaj.model.automation.emails.AutomationEmail;
import com.github.bananaj.utils.DateConverter;
import com.github.bananaj.utils.ModelIterator;
import com.github.bananaj.utils.URLHelper;

/**
 * Mailchimp’s Automation feature lets you build a series of emails that
 * send to subscribers when triggered by a specific date, activity, or event.
 * Use the API to manage Automation workflows, emails, and queues.
 * 
 * @author alexanderweiss
 *
 */
public class Automation implements JSONParser {

	private String id;
	private ZonedDateTime createTime;
	private ZonedDateTime startTime;
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

	public void parse(MailChimpConnection connection, JSONObject jsonObj) {
		id = jsonObj.getString("id");
		this.connection = connection;
		createTime = DateConverter.fromISO8601(jsonObj.getString("create_time"));
		startTime = jsonObj.has("start_time") ? DateConverter.fromISO8601(jsonObj.getString("start_time")) : null;
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
	 * @throws IOException
	 * @throws Exception 
	 */
	public void pauseAllEmails() throws IOException, Exception {
		getConnection().do_Post(new URL(connection.getAutomationendpoint() +"/"+getId()+"/actions/pause-all-emails"), connection.getApikey());
	}

	/**
	 * Start all emails in an Automation workflow
	 * @throws IOException
	 * @throws Exception 
	 */
	public void startAllEmails() throws IOException, Exception {
		getConnection().do_Post(new URL(connection.getAutomationendpoint() +"/"+getId()+"/actions/start-all-emails"), connection.getApikey());
	}

	/**
	 * Get a summary of the emails in a classic automation workflow.
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<AutomationEmail> getEmails() throws IOException, Exception {
		final String baseURL = URLHelper.join(connection.getAutomationendpoint(), "/", getId(), "/emails");
		return new ModelIterator<AutomationEmail>(AutomationEmail.class, baseURL, connection);
	}

	/**
	 * Get a summary of the emails in a classic automation workflow.
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<AutomationEmail> getEmails(int pageSize, int pageNumber) throws IOException, Exception {
		final String baseURL = URLHelper.join(connection.getAutomationendpoint(), "/", getId(), "/emails");
		return new ModelIterator<AutomationEmail>(AutomationEmail.class, baseURL, connection, pageSize, pageNumber);
	}

	/**
	 * Get information about a specific workflow email
	 * @param workflowEmailId
	 * @throws IOException
	 * @throws Exception 
	 */
	public AutomationEmail getEmail(String workflowEmailId) throws IOException, Exception {
		JSONObject jsonObj = new JSONObject(connection.do_Get(new URL(connection.getAutomationendpoint() + "/" + getId() + "/emails/" + workflowEmailId), connection.getApikey()));
		return new AutomationEmail(connection, jsonObj);
	}

	/**
	 * Update Automation
	 * @throws IOException
	 * @throws Exception 
	 */
	public void update(AutomationDelay delay) throws IOException, Exception {
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
	public ZonedDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * The date and time the Automation was started
	 */
	public ZonedDateTime getStartTime() {
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
				"    Created: " + DateConverter.toLocalString(getCreateTime()) + System.lineSeparator() +
				"    Started: " + (getStartTime()!=null ? DateConverter.toLocalString(getStartTime()) : "") + System.lineSeparator() +
				"    Status: " + getStatus().toString() + System.lineSeparator() +
				"    Emails Sent: " + getEmailsSent() + System.lineSeparator() +
				getRecipients().toString() + System.lineSeparator() +
				getSettings().toString() + System.lineSeparator() +
				getTracking().toString(); 
	}

	// TODO: add builder pattern
}
