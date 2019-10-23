/**
 * @author alexanderweiss
 * @date 19.11.2015
 */
package com.github.alexanderwe.bananaj.model.campaign;

import java.net.URL;
import java.time.LocalDateTime;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.alexanderwe.bananaj.connection.MailChimpConnection;
import com.github.alexanderwe.bananaj.exceptions.CampaignSettingsException;
import com.github.alexanderwe.bananaj.model.ReportSummary;
import com.github.alexanderwe.bananaj.model.Tracking;
import com.github.alexanderwe.bananaj.model.report.Report;
import com.github.alexanderwe.bananaj.utils.DateConverter;

/**
 * Class for representing a mailchimp campaign
 * @author alexanderweiss
 *
 */
public class Campaign {

	private MailChimpConnection connection;
	
	private String id;
	private int webId;
	private String parentCampaignId;
	private CampaignType type;
	private LocalDateTime createTime;
	private String archiveUrl;
	private String longArchiveUrl;
	private CampaignStatus status;
	private int emailsSent;
	private LocalDateTime sendTime;
	private CampaignContentType contentType;
	private boolean needsBlockRefresh;
	private boolean hasLogoMergeTag;
	private boolean resendable;
	private CampaignRecipients recipients;
	private CampaignSettings settings;
	//private VariateSettings variate_settings;
	private Tracking tracking;
	//private RssOpts rss_opts;
	//private AbSplitOpts ab_split_opts;
	//private SocialCard social_card;
	private ReportSummary reportSummary;
	//private DeliveryStatus delivery_status;
	
	private CampaignContent content;

	public Campaign(MailChimpConnection connection, JSONObject jsonObj) throws Exception {
		parse(connection, jsonObj);
	}

	public void parse(MailChimpConnection connection, JSONObject jsonObj) {
		id = jsonObj.getString("id");
		this.connection = connection;
		this.webId = jsonObj.getInt("web_id");
		if (jsonObj.has("parent_campaign_id")) {
			this.parentCampaignId = jsonObj.getString("parent_campaign_id");
		}
		this.type = CampaignType.valueOf(jsonObj.getString("type").toUpperCase());
		this.createTime = DateConverter.getInstance().createDateFromISO8601(jsonObj.getString("create_time"));
		this.archiveUrl = jsonObj.getString("archive_url");
		this.longArchiveUrl = jsonObj.getString("long_archive_url");
		this.status = CampaignStatus.valueOf(jsonObj.getString("status").toUpperCase());
		this.emailsSent = jsonObj.getInt("emails_sent");
		if (jsonObj.has("send_time")) {
			this.sendTime = DateConverter.getInstance().createDateFromISO8601(jsonObj.getString("send_time"));
		}
		this.contentType = CampaignContentType.valueOf(jsonObj.getString("content_type").toUpperCase());
		this.needsBlockRefresh = jsonObj.getBoolean("needs_block_refresh");
		this.hasLogoMergeTag = jsonObj.getBoolean("has_logo_merge_tag");
		this.resendable = jsonObj.getBoolean("resendable");
		
		if (jsonObj.has("recipients")) {
			this.recipients = new CampaignRecipients(jsonObj.getJSONObject("recipients"));
//			if (recipients.getListId() != null) {
//				this.mailChimpList = connection.getList(recipients.getString("list_id"));
//			}
		}
		this.settings = new CampaignSettings(jsonObj.getJSONObject("settings"));
		this.tracking = new Tracking(jsonObj.getJSONObject("tracking"));
		if (jsonObj.has("report_summary")) {
			this.reportSummary = new ReportSummary(jsonObj.getJSONObject("report_summary"));
		}
	}
	
	/**
	 * Update the campaign settings given specified CampaignSettings.
	 * @param campaignSettings
	 */
	public void updateSettings(CampaignSettings campaignSettings) throws CampaignSettingsException, Exception {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("settings", campaignSettings.getJsonRepresentation());
		String response = getConnection().do_Patch(new URL(getConnection().getCampaignendpoint()+"/"+getId()), jsonObj.toString(), getConnection().getApikey());
		parse(connection, new JSONObject(response));
	}

	public void update() throws Exception {
		JSONObject jsonObj = new JSONObject();
		//jsonObj.put("recipients", recipients.getJsonRepresentation());
		jsonObj.put("settings", settings.getJsonRepresentation());
		//jsonObj.put("variate_settings", settings.getJsonRepresentation());
		jsonObj.put("tracking", settings.getJsonRepresentation());
		//jsonObj.put("rss_opts", settings.getJsonRepresentation());
		//jsonObj.put("social_card", settings.getJsonRepresentation());
		String response = getConnection().do_Patch(new URL(getConnection().getCampaignendpoint()+"/"+getId()), jsonObj.toString(), getConnection().getApikey());
		parse(connection, new JSONObject(response));
	}
	
	public void delete() throws Exception {
		getConnection().do_Delete(new URL(getConnection().getCampaignendpoint() +"/"+getId()), getConnection().getApikey());
	}
	
	/**
	 * Send the campaign to the mailChimpList members
	 */
	public void send() throws Exception{
		getConnection().do_Post(new URL(connection.getCampaignendpoint()+"/"+this.getId()+"/actions/send"),connection.getApikey());
	}
	
	/**
	 * Send the campaign to the mailChimpList members
	 */
	public void sendTestEmail(String[] emails, CampaignSendType type) throws Exception {
		JSONObject data = new JSONObject();
		JSONArray testEmails = new JSONArray();
		for (String email : emails) {
			testEmails.put(email);
		}
		data.put("test_emails", testEmails);
		data.put("send_type", type.toString());
		getConnection().do_Post(new URL(getConnection().getCampaignendpoint()+"/"+getId()+"/actions/test"), data.toString(), getConnection().getApikey());
	}
	
	/**
	 * Cancel a Regular or Plain-Text Campaign after you send, before all of your
	 * recipients receive it. This feature is included with Mailchimp Pro.
	 * 
	 * @throws Exception
	 */
	public void cancel() throws Exception {
		getConnection().do_Post(new URL(getConnection().getCampaignendpoint()+"/"+getId()+"/actions/cancel-send"), getConnection().getApikey());
	}
	
	/**
	 * Creates a Resend to Non-Openers version of this campaign. We will also check
	 * if this campaign meets the criteria for Resend to Non-Openers campaigns.
	 * 
	 * @throws Exception
	 */
	public Campaign resend() throws Exception {
		String results = getConnection().do_Post(new URL(getConnection().getCampaignendpoint()+"/"+getId()+"/actions/create-resend"), getConnection().getApikey());
		return new Campaign(getConnection(), new JSONObject(results));
	}
	
	/**
	 * Pause an RSS-Driven campaign
	 * @throws Exception
	 */
	public void pause() throws Exception {
		getConnection().do_Post(new URL(getConnection().getCampaignendpoint()+"/"+getId()+"/actions/pause"), getConnection().getApikey());
	}

	/**
	 * Resume an RSS-Driven campaign.
	 * @throws Exception
	 */
	public void resume() throws Exception {
		getConnection().do_Post(new URL(getConnection().getCampaignendpoint()+"/"+getId()+"/actions/resume"), getConnection().getApikey());
	}

	/**
	 * Replicate a campaign in saved or send status
	 * @throws Exception
	 */
	public Campaign replicate() throws Exception {
		String results = getConnection().do_Post(new URL(getConnection().getCampaignendpoint()+"/"+getId()+"/actions/replicate"), getConnection().getApikey());
		return new Campaign(getConnection(), new JSONObject(results));
	}
	
	public CampaignSendChecklist getSendChecklist() throws Exception {
		String results = getConnection().do_Get(new URL(getConnection().getCampaignendpoint()+"/"+getId()+"/send-checklist"), getConnection().getApikey());
		return new CampaignSendChecklist(new JSONObject(results));
	}
	
	// TODO: additional actions (schedule, unschedule)
//	public void schedule(LocalDateTime schedule_time, boolean timewarp, BatchDelivery batch_delivery) throws Exception {
//		getConnection().do_Post(new URL(getConnection().getCampaignendpoint()+"/"+getId()+"/actions/schedule"), getConnection().getApikey());
//	}
//	
//	/**
//	 * Unschedule a scheduled campaign that hasn’t started sending.
//	 * @throws Exception
//	 */
//	public void unschedule() throws Exception {
//		getConnection().do_Post(new URL(getConnection().getCampaignendpoint()+"/"+getId()+"/actions/unschedule"), getConnection().getApikey());
//	}
	
	/**
	 * Stops the sending of your campaign
	 * (!Only included in mailchimp pro)
	 */
	public void cancelSend() throws Exception {
		getConnection().do_Post(new URL(getConnection().getCampaignendpoint()+"/"+getId()+"/actions/cancel-send"), getConnection().getApikey());
	}

	/**
	 * Get the report of this campaign
	 * @throws Exception
	 */
	public Report getReport() throws Exception {
		final JSONObject report = new JSONObject(getConnection().do_Get(new URL(connection.getReportsendpoint()+"/"+getId()), getConnection().getApikey()));
		return new Report(report);
	}

	/**
	 * @return the com.github.alexanderwe.bananaj.connection
	 */
	public MailChimpConnection getConnection() {
		return connection;
	}

    /**
	 * @return A string that uniquely identifies this campaign.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the content
	 * @throws Exception 
	 */
	public CampaignContent getContent() throws Exception {
		if (content == null) {
			getCampaignContent();
		}
		return content;
	}

	/**
	 * Get the content of this campaign from mailchimp API
	 */
	private void getCampaignContent() throws Exception{
		JSONObject content = new JSONObject(getConnection().do_Get(new URL(connection.getCampaignendpoint()+"/"+this.getId()+"/content"),connection.getApikey()));
		this.content = new CampaignContent(this, content);
	}

	// TODO:
//	public CampaignFeedback getFeedback() throws Exception {
//		
//	}
	
	/**
	 * The ID used in the Mailchimp web application. View this campaign in your Mailchimp account at https://{dc}.admin.mailchimp.com/campaigns/show/?id={web_id}.
	 */
	public int getWebId() {
		return webId;
	}

	/**
	 * If this campaign is the child of another campaign, this identifies the parent campaign. For Example, for RSS or Automation children.
	 */
	public String getParentCampaignId() {
		return parentCampaignId;
	}

	/**
	 * There are four types of campaigns you can create in Mailchimp. A/B Split campaigns have been deprecated and variate campaigns should be used instead.
	 */
	public CampaignType getType() {
		return type;
	}

	/**
	 * The date and time the campaign was created
	 */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * The link to the campaign’s archive version in ISO 8601 format
	 */
	public String getArchiveUrl() {
		return archiveUrl;
	}

	/**
	 * The original link to the campaign’s archive version
	 */
	public String getLongArchiveUrl() {
		return longArchiveUrl;
	}

	/**
	 * The current status of the campaign
	 */
	public CampaignStatus getStatus() {
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
	 * How the campaign’s content is put together 
	 */
	public CampaignContentType getContentType() {
		return contentType;
	}

	/**
	 * Determines if the campaign needs its blocks refreshed by opening the web-based campaign editor.
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
	 * Determines if the campaign qualifies to be resent to non-openers
	 */
	public boolean isResendable() {
		return resendable;
	}

	/**
	 * List settings for the campaign
	 */
	public CampaignRecipients getRecipients() {
		return recipients;
	}

	/**
	 * The settings for your campaign, including subject, from name, reply-to address, and more
	 */
	public CampaignSettings getSettings() {
		return settings;
	}

	/**
	 * The tracking options for a campaign
	 */
	public Tracking getTracking() {
		return tracking;
	}

	/**
	 * For sent campaigns, a summary of opens, clicks, and e-commerce data
	 */
	public ReportSummary getReportSummary() {
		return reportSummary;
	}

	@Override
	public String toString() {
		return "ID: " + getId() + System.lineSeparator() +
				getSettings().toString() + System.lineSeparator() +
				"Type: " + getType().toString() + System.lineSeparator() +
				"Status: " + getStatus().toString() + System.lineSeparator() +
				(getRecipients() != null ? getRecipients().toString() + System.lineSeparator() : "") +
				getTracking().toString() + 
				(getReportSummary() != null ? System.lineSeparator() + getReportSummary().toString() : "");
	}

}
