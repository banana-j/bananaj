/**
 * @author alexanderweiss
 * @date 19.11.2015
 */
package com.github.bananaj.model.report;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.connection.MailChimpQueryParameters;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.model.campaign.Bounce;
import com.github.bananaj.model.campaign.CampaignType;
import com.github.bananaj.utils.DateConverter;
import com.github.bananaj.utils.JSONObjectCheck;

/**
 * Mailchimp's campaign and Automation reports analyze clicks, opens, subscribers' social activity, e-commerce data, and more.
 * 
 */
public class Report implements JSONParser {

	private MailChimpConnection connection;

	private String id;
	private String campaignTitle;
	private CampaignType type;
	private String listId;
	private Boolean listIsActive;
	private String listName;
	private String subjectLine;
	private String previewText;
	private Integer emailsSent;
	private Integer abuseReport;
	private Integer unsubscribed;
	private ZonedDateTime sendtime;
	private ZonedDateTime rssLastSend;
	private Bounce bounces;
	private Forward forwards;
	private Open opens;
	private Click clicks;
	private FacebookLikes facebookLikes;
	private IndustryStats industryStats;
	private ReportListStats listStats;
	private ABSplit abSplit;
	private List<Timewarp> timewarp;
	private List<TimeSeries> timeseries;
	private ShareReport shareReport;
	private Ecommerce ecommerce;
	private DeliveryStatus deliveryStatus;

	public Report() {

	}

	public Report(MailChimpConnection connection, JSONObject jsonObj) {
		parse(connection, jsonObj);
	}

	@Override
	public void parse(MailChimpConnection connection, JSONObject entity) {
		JSONObjectCheck jObj = new JSONObjectCheck(entity);
		id = jObj.getString("id");
		this.connection = connection;
		campaignTitle = jObj.getString("campaign_title");
		type = jObj.getEnum(CampaignType.class, "type");
		listId = jObj.getString("list_id");
		listIsActive = jObj.getBoolean("list_is_active");
		listName = jObj.getString("list_name");
		subjectLine = jObj.getString("subject_line");
		previewText = jObj.getString("preview_text");
		emailsSent = jObj.getInt("emails_sent");
		abuseReport = jObj.getInt("abuse_reports");
		unsubscribed = jObj.getInt("unsubscribed");
		sendtime = jObj.getISO8601Date("send_time");
		rssLastSend = jObj.getISO8601Date("rss_last_send");
		bounces = jObj.has("bounces") ? new Bounce(jObj.getJSONObject("bounces")) : null;
		forwards = jObj.has("forwards") ? new Forward(jObj.getJSONObject("forwards")) : null;
		clicks = jObj.has("clicks") ? new Click(jObj.getJSONObject("clicks")) : null;
		opens = jObj.has("opens") ? new Open(jObj.getJSONObject("opens")) : null;
		facebookLikes = jObj.has("facebook_likes") ? new FacebookLikes(jObj.getJSONObject("facebook_likes")) : null;
		industryStats = jObj.has("industry_stats") ? new IndustryStats(jObj.getJSONObject("industry_stats")) : null;
		listStats = jObj.has("list_stats") ? new ReportListStats(jObj.getJSONObject("list_stats")) : null;
		abSplit = jObj.has("ab_split") ? new ABSplit(jObj.getJSONObject("ab_split")) : null;
		
		if (jObj.has("timewarp")) {
			final JSONArray series = jObj.getJSONArray("timewarp");
			timewarp = new ArrayList<Timewarp>(series.length());
			for(int i=0; i<series.length(); i++) {
				timewarp.add(new Timewarp(series.getJSONObject(i)));
			}
		}
		
		if (jObj.has("timeseries")) {
			final JSONArray series = jObj.getJSONArray("timeseries");
			timeseries = new ArrayList<TimeSeries>(series.length());
			for(int i=0; i<series.length(); i++) {
				timeseries.add(new TimeSeries(series.getJSONObject(i)));
			}
		}
		
		shareReport = jObj.has("share_report") ? new ShareReport(jObj.getJSONObject("share_report")) : null;
		ecommerce = jObj.has("ecommerce") ? new Ecommerce(jObj.getJSONObject("ecommerce")) : null;
		deliveryStatus = jObj.has("delivery_status") ? new DeliveryStatus(jObj.getJSONObject("delivery_status")) : null;
	}

	/**
	 * @return A string that uniquely identifies this campaign.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @return The total number of emails sent for the campaign.
	 */
	public Integer getEmailsSent() {
		return emailsSent;
	}

	/**
	 * @return The title of the campaign.
	 */
	public String getCampaignTitle() {
		return campaignTitle;
	}

	/**
	 * @return The number of abuse reports generated for this campaign.
	 */
	public Integer getAbuseReport() {
		return abuseReport;
	}

	/**
	 * @return The total number of unsubscribed members for this campaign.
	 */
	public Integer getUnsubscribed() {
		return unsubscribed;
	}

	/**
	 * @return The date and time a campaign was sent.
	 */
	public ZonedDateTime getSendTime() {
		return sendtime;
	}

	/**
	 * @return For RSS campaigns, the date and time of the last send.
	 */
	public ZonedDateTime getRssLastSend() {
		return rssLastSend;
	}

	/**
	 * @return The bounce summary for the campaign.
	 */
	public Bounce getBounces() {
		return bounces;
	}

	/**
	 * @return The forwards and forward activity for the campaign.
	 */
	public Forward getForwards() {
		return forwards;
	}

	/**
	 * @return The click activity for the campaign.
	 */
	public Click getClicks() {
		return clicks;
	}

	/**
	 * @return The open activity for the campaign.
	 */
	public Open getOpens() {
		return opens;
	}

	/**
	 * @return Campaign engagement on Facebook.
	 */
	public FacebookLikes getFacebookLikes() {
		return facebookLikes;
	}

	/**
	 * @return The average campaign statistics for your industry.
	 */
	public IndustryStats getIndustryStats() {
		return industryStats;
	}

	/**
	 * @return The average campaign statistics for your list. Null if it hasn't been calculated it yet for the list.
	 */
	public ReportListStats getListStats() {
		return listStats;
	}

	/**
	 * @return General stats about different groups of an A/B Split campaign. Does not return 
	 * information about Mailchimp Pro's Multivariate Campaigns.
	 */
	public ABSplit getAbSplit() {
		return abSplit;
	}

	/**
	 * @return An hourly breakdown of sends, opens, and clicks if a campaign is sent using timewarp.
	 */
	public List<Timewarp> getTimewarp() {
		return timewarp;
	}

	/**
	 * @return An hourly breakdown of the performance of the campaign over the first 24 hours.
	 */
	public List<TimeSeries> getTimeseries() {
		return timeseries;
	}

	/**
	 * @return The url and password for the VIP report.
	 */
	public ShareReport getShareReport() {
		return shareReport;
	}

	/**
	 * @return E-Commerce stats for a campaign.
	 */
	public Ecommerce getEcommerce() {
		return ecommerce;
	}

	/**
	 * @return Updates on campaigns in the process of sending.
	 */
	public DeliveryStatus getDeliveryStatus() {
		return deliveryStatus;
	}

	/**
	 * @return The type of campaign (regular, plain-text, ab_split, rss, automation, variate, or auto).
	 */
	public CampaignType getType() {
		return type;
	}

	/**
	 * @return The unique list id.
	 */
	public String getListId() {
		return listId;
	}

	/**
	 * @return The status of the list used, namely if it's deleted or disabled.
	 */
	public Boolean isListIsActive() {
		return listIsActive;
	}

	/**
	 * @return The name of the list.
	 */
	public String getListName() {
		return listName;
	}

	/**
	 * @return The subject line for the campaign.
	 */
	public String getSubjectLine() {
		return subjectLine;
	}

	/**
	 * @return The preview text for the campaign.
	 */
	public String getPreviewText() {
		return previewText;
	}

	/**
	 * @return The date and time a campaign was sent
	 */
	public ZonedDateTime getSendtime() {
		return sendtime;
	}

	/**
	 * Get Abuse sub-reports
	 * @throws IOException 
	 * @throws Exception 
	 */
	public Iterable<AbuseReport>  getAbuseReports() throws IOException, Exception {
		return connection.getCampaignAbuseReports(getId());
	}
	
	/**
	 * Get feedback based on a campaign's statistics. Advice feedback is based on 
	 * campaign stats like opens, clicks, unsubscribes, bounces, and more.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/campaign-advice/list-campaign-feedback/" target="MailchimpAPIDoc">Campaign Advice -- GET /reports/{campaign_id}/advice</a>
	 * @return Recent feedback based on a campaign's statistics.
	 * @throws IOException 
	 * @throws Exception 
	 */
	public Iterable<AdviceReport> getAdviceReports(final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return connection.getCampaignAdviceReports(getId(), queryParameters);
	}
	
	/**
	 * Get a detailed report about any emails in a specific campaign that were opened by recipients.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/open-reports/list-campaign-open-details/" target="MailchimpAPIDoc">Campaign Open Reports -- GET /reports/{campaign_id}/open-details</a>
	 * @return Detailed information about the campaigns emails that were opened by list members.
	 * @throws IOException 
	 * @throws Exception 
	 */
	public OpenReport getOpenReports(final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return connection.getCampaignOpenReports(getId(), queryParameters);
	}
	
	/**
	 * Get information about a specific subscriber who opened a campaign.
	 * @param subscriber The member's email address or subscriber hash
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/open-reports/get-opened-campaign-subscriber/" target="MailchimpAPIDoc">Campaign Subscriber Open Reports -- GET /reports/{campaign_id}/open-details/{subscriber_hash}</a>
	 * @return Detailed information about the campaigns emails that were opened by list members.
	 * @throws IOException
	 * @throws Exception 
	 */
	public OpenReportMember getOpenReport(String subscriber, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return connection.getCampaignOpenReport(getId(), subscriber, queryParameters);
	}

	/**
	 * Get detailed information about links clicked in campaigns.
	 * @return Campaign click details
	 * @throws IOException 
	 * @throws Exception 
	 */
	public Iterable<ClickReport> getClickReports() throws IOException, Exception {
		return connection.getCampaignClickReports(getId());
	}
	
	/**
	 * Get detailed information about links clicked in campaigns for a specific link.
	 * @param linkId The id for the link.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/click-reports/get-campaign-link-details/" target="MailchimpAPIDoc">Click Reports -- GET /reports/{campaign_id}/click-details/{link_id}</a>
	 * @return Click details for a specific link.
	 * @throws IOException 
	 * @throws Exception 
	 */
	public ClickReport getClickReport(String linkId, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return connection.getCampaignClickReport(getId(), linkId, queryParameters);
	}
	
	/**
	 * Get information about subscribers who clicked a link.
	 * @param linkId The id for the link.
	 * @return Information about subscribers who clicked a link
	 * @throws IOException 
	 * @throws Exception 
	 */
	public Iterable<ClickReportMember> getMembersClickReports(String linkId) throws IOException, Exception {
		return connection.getCampaignMembersClickReports(getId(), linkId);
	}
	
	/**
	 * Get information about a specific subscriber who clicked a link.
	 * @param linkId The id for the link.
	 * @param subscriber The member's email address or subscriber hash
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/link-clickers/get-clicked-link-subscriber/" target="MailchimpAPIDoc">Click Reports Members -- GET /reports/{campaign_id}/click-details/{link_id}/members/{subscriber_hash}</a>
	 * @return Information about a specific subscriber who clicked a link
	 * @throws IOException 
	 * @throws Exception 
	 */
	public ClickReportMember getMembersClickReport(String linkId, String subscriber, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return connection.getCampaignMembersClickReport(getId(), linkId, subscriber, queryParameters);
	}
	
	/**
	 * Get statistics for the top-performing domains from a campaign.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/domain-performance-reports/list-domain-performance-stats/" target="MailchimpAPIDoc">Reports Domain Performance -- GET /reports/{campaign_id}/domain-performance</a>
	 * @return Statistics for the top-performing domains from a campaign.
	 * @throws IOException 
	 * @throws Exception 
	 */
	public DomainPerformance getDomainPerformanceReport(final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return connection.getDomainPerformanceReport(getId(), queryParameters);
	}
	
	/**
	 * Ecommerce product activity report for Campaign
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/campaign-ecommerce-product-activity/list-campaign-product-activity/" target="MailchimpAPIDoc">Ecommerce Product Activity -- GET /reports/{campaign_id}/ecommerce-product-activity</a>
	 * @return Breakdown of product activity for a campaign.
	 * @throws IOException 
	 * @throws Exception 
	 */
	public Iterable<EcommerceProductActivity> getEcommerceProductActivityReports(final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return connection.getEcommerceProductActivityReports(getId(), queryParameters);
	}

	/**
	 * Sub report - Sent To - Get information about campaign recipients.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/sent-to-reports/list-campaign-recipients/" target="MailchimpAPIDoc">Sent To -- GET /reports/{campaign_id}/sent-to</a>
	 * @return Information about campaign recipients.
	 * @throws IOException 
	 * @throws Exception 
	 */
	public Iterable<ReportSentTo> getSentToReports(final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return connection.getCampaignSentToReports(getId(), queryParameters);
	}

	/**
	 * Sent To Recipient report - Get information about a specific campaign recipient.
	 * @param subscriber The member's email address or subscriber hash
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/sent-to-reports/list-campaign-recipients/" target="MailchimpAPIDoc">Sent To -- GET /reports/{campaign_id}/sent-to</a>
	 * @return Information about a specific campaign recipients.
	 * @throws IOException 
	 * @throws Exception 
	 */
	public ReportSentTo getSentToReport(String subscriberHash, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return connection.getCampaignSentToReport(getId(), subscriberHash, queryParameters);
	}
	
	/**
	 * Email Activity report - Get list member activity.
	 * @return Member activity for a campaign.
	 * @throws IOException 
	 * @throws Exception 
	 */
	public Iterable<EmailActivity> getEmailActivityReports() throws IOException, Exception {
		return connection.getCampaignEmailActivityReports(getId());
	}
	
	/**
	 * Email Activity report - Get a specific list member's activity in a campaign including opens, clicks, and bounces.
	 * @param subscriber The member's email address or subscriber hash
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/email-activity-reports/get-subscriber-email-activity/" target="MailchimpAPIDoc">Email Activity -- GET /reports/{campaign_id}/email-activity/{subscriber_hash}</a>
	 * @return Member activity for a campaign.
	 * @throws IOException 
	 * @throws Exception 
	 */
	public EmailActivity getEmailActivityReport(String subscriber, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return connection.getCampaignEmailActivityReport(getId(), subscriber, queryParameters);
	}
	
	/**
	 * Top open locations for a specific campaign.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/location-reports/list-top-open-activities/" target="MailchimpAPIDoc">Reports Location -- GET /reports/{campaign_id}/locations</a>
	 * @throws IOException 
	 * @throws Exception 
	 */
	public Iterable<ReportLocation> getLocationsReports(final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return connection.getCampaignLocationsReports(getId(), queryParameters);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(5000);
		sb.append("Report of campaign: " + getId() + " '" + this.getCampaignTitle() + "'" + System.lineSeparator());
		sb.append("    Total emails sent: " + getEmailsSent() + System.lineSeparator());
		sb.append("    Total abuse reports: " + getAbuseReport() +  System.lineSeparator());
		sb.append("    Total unsubscribed: " + getUnsubscribed() + System.lineSeparator());
		sb.append("    Send Time: " + (getSendTime()!=null ? DateConverter.toLocalString(getSendTime()) : "") + System.lineSeparator());
		sb.append((getRssLastSend() != null ? DateConverter.toLocalString(getRssLastSend()) + System.lineSeparator() : ""));
		sb.append(getForwards().toString() + System.lineSeparator());
		sb.append(getOpens().toString() + System.lineSeparator());
		sb.append(getBounces().toString() + System.lineSeparator());
		sb.append(getClicks().toString() + System.lineSeparator());
		sb.append((getFacebookLikes() != null ? getFacebookLikes().toString() + System.lineSeparator() : ""));
		sb.append((getIndustryStats() != null ? getIndustryStats().toString() + System.lineSeparator() : ""));
		sb.append((getListStats() != null ? getListStats().toString() + System.lineSeparator() : ""));
		sb.append((getShareReport() != null ? getShareReport().toString() : ""));
		sb.append((getEcommerce() != null ? getEcommerce().toString() : ""));
		sb.append((getDeliveryStatus() != null ? getDeliveryStatus().toString() : ""));
		if (getTimeseries() != null) {
			sb.append(System.lineSeparator());
			sb.append("Time Series (first 24 hours):");
			for (TimeSeries ts : getTimeseries()) {
				String s = ts.toString();
				sb.append(System.lineSeparator());
				sb.append(s.replaceAll("(?m)^", "    "));
			}
		}
		if (getTimewarp() != null) {
			sb.append(System.lineSeparator());
			sb.append("Timewarp Series hourly breakdown:");
			for (Timewarp tw : getTimewarp()) {
				String s = tw.toString();
				sb.append(System.lineSeparator());
				sb.append(s.replaceAll("(?m)^", "    "));
			}
		}
		return sb.toString();
	}

}
