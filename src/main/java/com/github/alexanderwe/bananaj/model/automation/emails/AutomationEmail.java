package com.github.alexanderwe.bananaj.model.automation.emails;

import java.net.URL;
import java.time.LocalDateTime;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.connection.MailChimpConnection;
import com.github.alexanderwe.bananaj.model.ReportSummary;
import com.github.alexanderwe.bananaj.model.Tracking;
import com.github.alexanderwe.bananaj.model.automation.AutomationDelay;
import com.github.alexanderwe.bananaj.model.automation.AutomationStatus;
import com.github.alexanderwe.bananaj.model.campaign.CampaignRecipients;
import com.github.alexanderwe.bananaj.utils.DateConverter;

public class AutomationEmail {

	private String id;
	private int webId;
	private String workflowId;
	private int position;
	private AutomationDelay delay;
	private LocalDateTime createTime;
	private LocalDateTime startTime;
	private String archiveUrl;
	private AutomationStatus status;
	private int emailsSent;
	private LocalDateTime sendTime;
	private String contentType;
	private boolean needsBlockRefresh;
	private boolean hasLogoMergeTag;
	private CampaignRecipients recipients;
	private AutomationEmailSettings settings;
	private Tracking tracking;
	//private Object social_card;
	//private Object trigger_settings;
	private ReportSummary reportSummary;
	private MailChimpConnection connection;
	
	
	public AutomationEmail(MailChimpConnection connection, JSONObject jsonObj) {
		parse(connection, jsonObj);
	}

	public AutomationEmail() {

	}

	private void parse(MailChimpConnection connection, JSONObject jsonObj) {
        id = jsonObj.getString("id");
		webId = jsonObj.getInt("web_id");
		workflowId = jsonObj.getString("workflow_id");
		position = jsonObj.getInt("position");
		delay = new AutomationDelay(jsonObj.getJSONObject("delay"));
        createTime = DateConverter.createDateFromISO8601(jsonObj.getString("create_time"));
        startTime = DateConverter.createDateFromISO8601(jsonObj.getString("start_time"));
        archiveUrl = jsonObj.getString("archive_url");
		status = AutomationStatus.valueOf(jsonObj.getString("status").toUpperCase());
		emailsSent = jsonObj.getInt("emails_sent");
        sendTime = DateConverter.createDateFromISO8601(jsonObj.getString("send_time"));
        contentType = jsonObj.getString("content_type");
        needsBlockRefresh = jsonObj.getBoolean("needs_block_refresh");
        hasLogoMergeTag = jsonObj.getBoolean("has_logo_merge_tag");
        recipients = new CampaignRecipients(jsonObj.getJSONObject("recipients"));
        settings = new AutomationEmailSettings(jsonObj.getJSONObject("settings"));
        tracking = new Tracking(jsonObj.getJSONObject("tracking"));
        reportSummary = jsonObj.has("report_summary") ? new ReportSummary(jsonObj.getJSONObject("report_summary")) : null;
        this.connection = connection;
	}

	public AutomationSubscriberQueue getSubscriberQueue() throws Exception {
		return getSubscriberQueue(100, 0);
	}
	
	public AutomationSubscriberQueue getSubscriberQueue(int count, int offset) throws Exception {
		JSONObject jsonObj = new JSONObject(connection.do_Get(new URL(connection.getAutomationendpoint() + "/" + workflowId + "/emails/" + getId() + "/queue" + "?offset=" + offset + "&count=" + count), connection.getApikey()));
		return new AutomationSubscriberQueue(connection, jsonObj);
	}
	
	public AutomationSubscriber getSubscriber(String subscriberHash) throws Exception {
		JSONObject jsonObj = new JSONObject(connection.do_Get(new URL(connection.getAutomationendpoint() + "/" + workflowId + "/emails/" + getId() + "/queue/" + subscriberHash), connection.getApikey()));
		return new AutomationSubscriber(jsonObj);
	}
	
	/**
	 * Manually add a subscriber to a workflow, bypassing the default trigger
	 * settings. You can also use this endpoint to trigger a series of automated
	 * emails in an API 3.0 workflow type or add subscribers to an automated email
	 * queue that uses the API request delay type.
	 * 
	 * @param emailAddress The list member’s email address
	 * @throws Exception
	 */
	public void addSubscriber(String emailAddress) throws Exception {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("email_address", emailAddress);
		connection.do_Post(new URL(connection.getAutomationendpoint() + "/" + workflowId + "/emails/" + getId() + "/queue"), jsonObj.toString(), connection.getApikey());
		// Note: MailChimp documents this as returning an AutomationSubscriber but in practice it returns nothing
	}
	
	public void update() throws Exception {
		JSONObject json = getJsonRepresentation();
		String results = connection.do_Patch(new URL(connection.getAutomationendpoint() + "/" + workflowId + "/emails/" + getId()), json.toString(), connection.getApikey());
		parse(connection, new JSONObject(results));
	}
	
	public void delete() throws Exception {
		connection.do_Delete(new URL(connection.getAutomationendpoint() + "/" + workflowId + "/emails/" + getId()), connection.getApikey());
	}
	
    /**
	 * @return A string that uniquely identifies the Automation email.
	 */
	public String getId() {
		return id;
	}

	/**
	 * The ID used in the Mailchimp web application. View this automation in your
	 * Mailchimp account at
	 * https://{dc}.admin.mailchimp.com/campaigns/show/?id={web_id}.
	 */
	public int getWebId() {
		return webId;
	}

	/**
	 * A string that uniquely identifies an Automation workflow
	 */
	public String getWorkflowId() {
		return workflowId;
	}

	/**
	 * the position of an Automation email in a workflow
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * The delay settings for an automation email
	 */
	public AutomationDelay getDelay() {
		return delay;
	}

	/**
	 * The date and time the campaign was created
	 */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * The date and time the campaign was started
	 */
	public LocalDateTime getStartTime() {
		return startTime;
	}

	/**
	 * The link to the campaign’s archive version in ISO 8601 format
	 */
	public String getArchiveUrl() {
		return archiveUrl;
	}

	/**
	 * The current status of the campaign
	 */
	public AutomationStatus getStatus() {
		return status;
	}

	/**
	 * The total number of emails sent for this campaign
	 */
	public int getEmailsSent() {
		return emailsSent;
	}

	/**
	 * The date and time a campaign was sent
	 */
	public LocalDateTime getSendTime() {
		return sendTime;
	}

	/**
	 * How the campaign’s content is put together (‘template’, ‘drag_and_drop’, ‘html’, ‘url’)
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Determines if the automation email needs its blocks refreshed by opening the web-based campaign editor
	 */
	public boolean isNeedsBlockRefresh() {
		return needsBlockRefresh;
	}

	/**
	 * Determines if the campaign contains the |BRAND:LOGO| merge tag
	 */
	public boolean isHasLogoMergeTag() {
		return hasLogoMergeTag;
	}

	/**
	 * List settings for the campaign
	 */
	public CampaignRecipients getRecipients() {
		return recipients;
	}

	/**
	 * Settings for the campaign including the email subject, from name, and from email address
	 */
	public AutomationEmailSettings getSettings() {
		return settings;
	}

	/**
	 * The tracking options for a campaign
	 */
	public Tracking getTracking() {
		return tracking;
	}

	/**
	 * For sent campaigns, a summary of opens, clicks, and unsubscribes
	 */
	public ReportSummary getReportSummary() {
		return reportSummary;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
	protected JSONObject getJsonRepresentation() throws Exception {
		JSONObject json = new JSONObject();
		
		if (settings != null) {
			JSONObject settingsObj = settings.getJsonRepresentation();
			json.put("settings", settingsObj);
		}
		
		if (delay != null) {
			JSONObject delayObj = delay.getJsonRepresentation();
			json.put("delay", delayObj);
		}
		
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
				"    Web Id: " + getWebId() + System.lineSeparator() +
				"    Position: " + getPosition() + System.lineSeparator() +
				"    Created: " + getCreateTime() + System.lineSeparator() +
				"    Started: " + getStartTime() + System.lineSeparator() +
				"    Archive URL: " + getArchiveUrl() + System.lineSeparator() +
				"    Status: " + getStatus().toString() + System.lineSeparator() +
				"    Emails Sent: " + getEmailsSent() + System.lineSeparator() +
				"    Send Time: " + getSendTime() + System.lineSeparator() +
				"    Content Type: " + getContentType() + System.lineSeparator() +
				"    Needs Block Refresh: " + isNeedsBlockRefresh() + System.lineSeparator() +
				"    Has Logo Merge Tag: " + isHasLogoMergeTag() + System.lineSeparator() +
				getDelay().toString() + System.lineSeparator() +
				getRecipients().toString() + System.lineSeparator() +
				getSettings().toString() + System.lineSeparator() +
				getTracking().toString() + 
				(reportSummary != null ? System.lineSeparator() + reportSummary.toString() : "");
	}
	
}
