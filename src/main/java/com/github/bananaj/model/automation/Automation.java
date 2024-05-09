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
import com.github.bananaj.connection.MailChimpQueryParameters;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.model.ModelIterator;
import com.github.bananaj.model.ReportSummary;
import com.github.bananaj.model.Tracking;
import com.github.bananaj.model.automation.emails.AutomationEmail;
import com.github.bananaj.utils.DateConverter;
import com.github.bananaj.utils.JSONObjectCheck;
import com.github.bananaj.utils.URLHelper;

/**
 * Mailchimp's classic automations feature lets you build a series of emails
 * that send to subscribers when triggered by a specific date, activity, or
 * event. Use the API to manage Automation workflows, emails, and queues. Does
 * not include Customer Journeys.
 * 
 */
public class Automation implements JSONParser {

	private String id;
	private ZonedDateTime createTime;
	private ZonedDateTime startTime;
	private AutomationStatus status;
	private Integer emailsSent;
	private AutomationRecipient recipients;
	private AutomationSettings settings;
	private Tracking tracking;
	//private AutomationTriggerSettings trigger_settings;
	private ReportSummary reportSummary;
	private MailChimpConnection connection;

	public Automation(MailChimpConnection connection, JSONObject automation) {
		parse(connection, automation);
	}

	public Automation() {

	}

	public void parse(MailChimpConnection connection, JSONObject automation) {
		JSONObjectCheck jObj = new JSONObjectCheck(automation);
		id = jObj.getString("id");
		this.connection = connection;
		createTime = DateConverter.fromISO8601(jObj.getString("create_time"));
		startTime = jObj.getISO8601Date("start_time");
		status = jObj.getEnum(AutomationStatus.class, "status");
		emailsSent = jObj.getInt("emails_sent");
		if (automation.has("recipients")) {
			recipients = new AutomationRecipient(automation.getJSONObject("recipients"));
		}
		if (automation.has("settings")) {
			settings = new AutomationSettings(automation.getJSONObject("settings"));
		}
		if (automation.has("tracking")) {
			tracking = new Tracking(automation.getJSONObject("tracking"));
		}
		if (automation.has("report_summary")) {
			reportSummary = new ReportSummary(automation.getJSONObject("report_summary"));
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

// Does this endpoint support query parameters? 
//	/**
//	 * Get a summary of the emails in a classic automation workflow.
//	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
//	 *   @see <a href="https://mailchimp.com/developer/marketing/api/automation-email/list-automated-emails/" target="MailchimpAPIDoc">Automation Emails -- GET /automations/{workflow_id}/emails</a>
//	 * @throws IOException
//	 * @throws Exception 
//	 */
//	public Iterable<AutomationEmail> getEmails(MailChimpQueryParameters queryParameters) throws IOException, Exception {
//		final String baseURL = URLHelper.join(connection.getAutomationendpoint(), "/", getId(), "/emails");
//		return new ModelIterator<AutomationEmail>(AutomationEmail.class, baseURL, connection, queryParameters);
//	}

	/**
	 * Get information about an individual classic automation workflow email.
	 * @param workflowEmailId
	 * @throws IOException
	 * @throws Exception 
	 */
	public AutomationEmail getEmail(String workflowEmailId) throws IOException, Exception {
		JSONObject jsonObj = new JSONObject(connection.do_Get(new URL(connection.getAutomationendpoint() + "/" + getId() + "/emails/" + workflowEmailId), connection.getApikey()));
		return new AutomationEmail(connection, jsonObj);
	}

//	/**
//	 * Update Automation
//	 * @throws IOException
//	 * @throws Exception 
//	 */
//	public void update(AutomationDelay delay) throws IOException, Exception {
//		JSONObject json = getJsonRepresentation();
//		if (delay != null) {
//			json.put("delay", delay.getJsonRepresentation());
//		}
//		String results = getConnection().do_Patch(new URL(connection.getAutomationendpoint()+"/"+getId()), json.toString(), connection.getApikey());
//		parse(connection, new JSONObject(results));  // update member automation with current data
//	}

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
	public Integer getEmailsSent() {
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
	 * A summary of opens and clicks for sent campaigns.
	 */
	public ReportSummary getReportSummary() {
		return reportSummary;
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
