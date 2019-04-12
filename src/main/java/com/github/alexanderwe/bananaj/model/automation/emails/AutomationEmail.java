package com.github.alexanderwe.bananaj.model.automation.emails;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;

import org.json.JSONException;
import org.json.JSONObject;

import com.github.alexanderwe.bananaj.connection.MailChimpConnection;
import com.github.alexanderwe.bananaj.exceptions.TransportException;
import com.github.alexanderwe.bananaj.model.MailchimpObject;
import com.github.alexanderwe.bananaj.model.campaign.CampaignRecipients;
import com.github.alexanderwe.bananaj.utils.DateConverter;

public class AutomationEmail extends MailchimpObject {

	private int webId;
	private String workflowId;
	private int position;
	//private Object delay;
	private LocalDateTime createTime;
	private LocalDateTime startTime;
	private String archive_url;
	//private Object status;
	private int emailsSent;
	private LocalDateTime sendTime;
	private String contentType;
	private boolean needsBlockRefresh;
	private boolean hasLogoMergeTag;
	private CampaignRecipients recipients;
	private AutomationEmailSettings settings;
	//private Object tracking;
	//private Object social_card;
	//private Object trigger_settings;
	//private Object report_summary;
	private MailChimpConnection connection;
	
	
	public AutomationEmail(MailChimpConnection connection, JSONObject jsonObj) {
		super(jsonObj.getString("id"), jsonObj);
		this.webId = jsonObj.getInt("web_id");
		this.workflowId = jsonObj.getString("workflow_id");
		this.position = jsonObj.getInt("position");
        this.createTime = DateConverter.getInstance().createDateFromISO8601(jsonObj.getString("create_time"));
        this.startTime = DateConverter.getInstance().createDateFromISO8601(jsonObj.getString("start_time"));
        this.archive_url = jsonObj.getString("archive_url");
		this.emailsSent = jsonObj.getInt("emails_sent");
        this.sendTime = DateConverter.getInstance().createDateFromISO8601(jsonObj.getString("send_time"));
        this.contentType = jsonObj.getString("content_type");
        this.needsBlockRefresh = jsonObj.getBoolean("needs_block_refresh");
        this.hasLogoMergeTag = jsonObj.getBoolean("has_logo_merge_tag");
        this.recipients = new CampaignRecipients(jsonObj.getJSONObject("recipients"));
        this.settings = new AutomationEmailSettings(jsonObj.getJSONObject("settings"));
        this.connection = connection;
	}

	public AutomationEmail() {

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
	 * The ID used in the Mailchimp web application. View this automation in your
	 * Mailchimp account at
	 * https://{dc}.admin.mailchimp.com/campaigns/show/?id={web_id}.
	 * 
	 * @return
	 */
	public int getWebId() {
		return webId;
	}

	/**
	 * A string that uniquely identifies an Automation workflow
	 * @return
	 */
	public String getWorkflowId() {
		return workflowId;
	}

	/**
	 * the position of an Automation email in a workflow
	 * @return
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * The date and time the campaign was created
	 * @return
	 */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * The date and time the campaign was started
	 * @return
	 */
	public LocalDateTime getStartTime() {
		return startTime;
	}

	/**
	 * The link to the campaign’s archive version in ISO 8601 format
	 * @return
	 */
	public String getArchive_url() {
		return archive_url;
	}

	/**
	 * The total number of emails sent for this campaign
	 * @return
	 */
	public int getEmailsSent() {
		return emailsSent;
	}

	/**
	 * The date and time a campaign was sent
	 * @return
	 */
	public LocalDateTime getSendTime() {
		return sendTime;
	}

	/**
	 * How the campaign’s content is put together (‘template’, ‘drag_and_drop’, ‘html’, ‘url’)
	 * @return
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Determines if the automation email needs its blocks refreshed by opening the web-based campaign editor
	 * @return
	 */
	public boolean isNeedsBlockRefresh() {
		return needsBlockRefresh;
	}

	/**
	 * Determines if the campaign contains the |BRAND:LOGO| merge tag
	 * @return
	 */
	public boolean isHasLogoMergeTag() {
		return hasLogoMergeTag;
	}

	/**
	 * List settings for the campaign
	 * @return
	 */
	public CampaignRecipients getRecipients() {
		return recipients;
	}

	/**
	 * Settings for the campaign including the email subject, from name, and from email address
	 * @return
	 */
	public AutomationEmailSettings getSettings() {
		return settings;
	}

}
