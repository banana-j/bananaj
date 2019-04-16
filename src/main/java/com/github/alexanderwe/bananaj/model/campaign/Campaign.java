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
import com.github.alexanderwe.bananaj.model.MailchimpObject;
import com.github.alexanderwe.bananaj.model.ReportSummary;
import com.github.alexanderwe.bananaj.model.Tracking;
import com.github.alexanderwe.bananaj.model.report.Click;
import com.github.alexanderwe.bananaj.model.report.FacebookLikes;
import com.github.alexanderwe.bananaj.model.report.Forward;
import com.github.alexanderwe.bananaj.model.report.IndustryStats;
import com.github.alexanderwe.bananaj.model.report.Open;
import com.github.alexanderwe.bananaj.model.report.Report;
import com.github.alexanderwe.bananaj.model.report.ReportListStats;
import com.github.alexanderwe.bananaj.utils.DateConverter;

/**
 * Class for representing a mailchimp campaign
 * @author alexanderweiss
 *
 */
public class Campaign extends MailchimpObject {

	private MailChimpConnection connection;
	
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
		super(jsonObj.getString("id"), jsonObj);
		parse(connection, jsonObj);
	}

	public void parse(MailChimpConnection connection, JSONObject jsonObj) {
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
	 * @return
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
	 * @return
	 * @throws Exception
	 */
	public Campaign replicate() throws Exception {
		String results = getConnection().do_Post(new URL(getConnection().getCampaignendpoint()+"/"+getId()+"/actions/replicate"), getConnection().getApikey());
		return new Campaign(getConnection(), new JSONObject(results));
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
	 * @return
	 * @throws Exception
	 */
	public Report getReport() throws Exception {
		final JSONObject report = new JSONObject(getConnection().do_Get(new URL(connection.getReportsendpoint()+"/"+getId()), getConnection().getApikey()));
		final JSONObject forwards = report.getJSONObject("forwards");
		final JSONObject opens = report.getJSONObject("opens");
		final JSONObject clicks = report.getJSONObject("clicks");
		final JSONObject facebook_likes = report.getJSONObject("facebook_likes");
		final JSONObject industry_stats = report.getJSONObject("industry_stats");
		final JSONObject report_list_stats = report.getJSONObject("list_stats");

		Bounce bouncesObject = new Bounce(report.getJSONObject("bounces"));
		Forward forwardsObject = new Forward(forwards.getInt("forwards_count"), forwards.getInt("forwards_opens"));
		Click clicksObject = new Click(clicks.getInt("clicks_total"),clicks.getInt("unique_clicks"),clicks.getInt("unique_subscriber_clicks"),clicks.getDouble("click_rate"), DateConverter.getInstance().createDateFromISO8601(clicks.getString("last_click")));
		Open opensObject = new Open(opens.getInt("opens_total"),opens.getInt("unique_opens"), opens.getDouble("open_rate"), opens.getString("last_open"));
		FacebookLikes facebookObject = new FacebookLikes(facebook_likes.getInt("recipient_likes"),facebook_likes.getInt("unique_likes"),facebook_likes.getInt("facebook_likes"));
		IndustryStats industryStatsObject = new IndustryStats(industry_stats.getString("type"), industry_stats.getDouble("open_rate"),industry_stats.getDouble("click_rate"),industry_stats.getDouble("bounce_rate"),industry_stats.getDouble("unopen_rate"),industry_stats.getDouble("unsub_rate"), industry_stats.getDouble("abuse_rate"));
		ReportListStats reportListStatsObject = new ReportListStats(report_list_stats.getDouble("sub_rate"), report_list_stats.getDouble("unsub_rate"), report_list_stats.getDouble("open_rate"), report_list_stats.getDouble("click_rate"));

		return new Report(report.getString("id"), report.getString("campaign_title"),report.getInt("emails_sent"),report.getInt("abuse_reports"), report.getInt("unsubscribed"),DateConverter.getInstance().createDateFromISO8601(report.getString("send_time")),bouncesObject,forwardsObject,clicksObject,opensObject,facebookObject,industryStatsObject,reportListStatsObject,report);
	}

	/**
	 * @return the com.github.alexanderwe.bananaj.connection
	 */
	public MailChimpConnection getConnection() {
		return connection;
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
		this.content = new CampaignContent(
				content.has("plain_text") ? content.getString("plain_text") : null, 
				content.has("html") ? content.getString("html") : null, 
				this) ;
	}

	/**
	 * The ID used in the Mailchimp web application. View this campaign in your Mailchimp account at https://{dc}.admin.mailchimp.com/campaigns/show/?id={web_id}.
	 * @return
	 */
	public int getWebId() {
		return webId;
	}

	/**
	 * If this campaign is the child of another campaign, this identifies the parent campaign. For Example, for RSS or Automation children.
	 * @return
	 */
	public String getParentCampaignId() {
		return parentCampaignId;
	}

	/**
	 * There are four types of campaigns you can create in Mailchimp. A/B Split campaigns have been deprecated and variate campaigns should be used instead.
	 * @return
	 */
	public CampaignType getType() {
		return type;
	}

	/**
	 * The date and time the campaign was created
	 * @return
	 */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * The link to the campaign’s archive version in ISO 8601 format
	 * @return
	 */
	public String getArchiveUrl() {
		return archiveUrl;
	}

	/**
	 * The original link to the campaign’s archive version
	 * @return
	 */
	public String getLongArchiveUrl() {
		return longArchiveUrl;
	}

	/**
	 * The current status of the campaign
	 * @return
	 */
	public CampaignStatus getStatus() {
		return status;
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
	 * How the campaign’s content is put together 
	 * @return
	 */
	public CampaignContentType getContentType() {
		return contentType;
	}

	/**
	 * Determines if the campaign needs its blocks refreshed by opening the web-based campaign editor.
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
	 * Determines if the campaign qualifies to be resent to non-openers
	 * @return
	 */
	public boolean isResendable() {
		return resendable;
	}

	/**
	 * List settings for the campaign
	 * @return
	 */
	public CampaignRecipients getRecipients() {
		return recipients;
	}

	/**
	 * The settings for your campaign, including subject, from name, reply-to address, and more
	 * @return
	 */
	public CampaignSettings getSettings() {
		return settings;
	}

	/**
	 * The tracking options for a campaign
	 * @return
	 */
	public Tracking getTracking() {
		return tracking;
	}

	/**
	 * For sent campaigns, a summary of opens, clicks, and e-commerce data
	 * @return
	 */
	public ReportSummary getReportSummary() {
		return reportSummary;
	}

	@Override
	public String toString(){
		return "ID: " + getId() + System.lineSeparator() +
				getSettings().toString() + System.lineSeparator() +
				"Type: " + getType().getStringRepresentation() + System.lineSeparator() +
				"Status: " + getStatus().getStringRepresentation() + System.lineSeparator() +
				//"Tracking: " + System.lineSeparator() +
				getTracking().toString() + System.lineSeparator() +
				//"Report Summary: " + System.lineSeparator() +
				getReportSummary().toString();
	}

}
