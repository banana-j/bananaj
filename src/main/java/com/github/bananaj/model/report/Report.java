/**
 * @author alexanderweiss
 * @date 19.11.2015
 */
package com.github.bananaj.model.report;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.exceptions.TransportException;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.model.campaign.Bounce;
import com.github.bananaj.model.campaign.CampaignType;
import com.github.bananaj.model.list.member.Member;
import com.github.bananaj.utils.DateConverter;
import com.github.bananaj.utils.ModelIterator;
import com.github.bananaj.utils.URLHelper;

/**
 * Mailchimp's campaign and Automation reports analyze clicks, opens, subscribers' social activity, e-commerce data, and more.
 * 
 * @author alexanderweiss
 *
 */
public class Report implements JSONParser {

	private MailChimpConnection connection;

	private String id;
	private String campaignTitle;
	private CampaignType type;
	private String listId;
	private boolean listIsActive;
	private String listName;
	private String subjectLine;
	private String previewText;
	private int emailsSent;
	private int abuseReport;
	private int unsubscribed;
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
		id = entity.getString("id");
		this.connection = connection;
		campaignTitle = entity.getString("campaign_title");
		type = CampaignType.valueOf(entity.getString("type").toUpperCase());
		listId = entity.getString("list_id");
		listIsActive = entity.getBoolean("list_is_active");
		listName = entity.getString("list_name");
		subjectLine = entity.getString("subject_line");
		previewText = entity.getString("preview_text");
		emailsSent = entity.getInt("emails_sent");
		abuseReport = entity.getInt("abuse_reports");
		unsubscribed = entity.getInt("unsubscribed");
		sendtime = DateConverter.fromISO8601(entity.getString("send_time"));
		rssLastSend = entity.has("rss_last_send") ? DateConverter.fromISO8601(entity.getString("rss_last_send")) : null;
		bounces = new Bounce(entity.getJSONObject("bounces"));
		forwards = new Forward(entity.getJSONObject("forwards"));
		clicks = new Click(entity.getJSONObject("clicks"));
		opens = new Open(entity.getJSONObject("opens"));
		facebookLikes = entity.has("facebook_likes") ? new FacebookLikes(entity.getJSONObject("facebook_likes")) : null;
		industryStats = entity.has("industry_stats") ? new IndustryStats(entity.getJSONObject("industry_stats")) : null;
		listStats = entity.has("list_stats") ? new ReportListStats(entity.getJSONObject("list_stats")) : null;
		abSplit = entity.has("ab_split") ? new ABSplit(entity.getJSONObject("ab_split")) : null;
		
		if (entity.has("timewarp")) {
			final JSONArray series = entity.getJSONArray("timewarp");
			timewarp = new ArrayList<Timewarp>(series.length());
			for(int i=0; i<series.length(); i++) {
				timewarp.add(new Timewarp(series.getJSONObject(i)));
			}
		}
		
		if (entity.has("timeseries")) {
			final JSONArray series = entity.getJSONArray("timeseries");
			timeseries = new ArrayList<TimeSeries>(series.length());
			for(int i=0; i<series.length(); i++) {
				timeseries.add(new TimeSeries(series.getJSONObject(i)));
			}
		}
		
		shareReport = entity.has("share_report") ? new ShareReport(entity.getJSONObject("share_report")) : null;
		ecommerce = entity.has("ecommerce") ? new Ecommerce(entity.getJSONObject("ecommerce")) : null;
		deliveryStatus = entity.has("delivery_status") ? new DeliveryStatus(entity.getJSONObject("delivery_status")) : null;
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
	public int getEmailsSent() {
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
	public int getAbuseReport() {
		return abuseReport;
	}

	/**
	 * @return The total number of unsubscribed members for this campaign.
	 */
	public int getUnsubscribed() {
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
	public boolean isListIsActive() {
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
	 */
	public Iterable<AbuseReport>  getAbuseReports() throws Exception {
		return connection.getCampaignAbuseReports(getId());
	}
	
	/**
	 * Get recent feedback based on a campaign's statistics.
	 * @return Recent feedback based on a campaign's statistics.
	 */
	public Iterable<AdviceReport> getAdviceReports(String campaignId) throws Exception {
		return connection.getCampaignAdviceReports(getId());
	}
	
	/**
	 * Get a detailed report about any emails in a specific campaign that were opened by recipients.
	 * @param since Optional, restrict results to campaign open events that occur after a specific time.
	 * @return Detailed information about the campaigns emails that were opened by list members.
	 * @throws Exception
	 */
	public OpenReport getOpenReports(ZonedDateTime since) throws Exception {
		return connection.getCampaignOpenReports(getId(), since);
	}
	
	/**
	 * Get detailed information about links clicked in campaigns.
	 * @return Campaign click details
	 * @throws Exception
	 */
	public Iterable<ClickReport> getClickReports() throws Exception {
		return connection.getCampaignClickReports(getId());
	}
	
	/**
	 * Get detailed information about links clicked in campaigns for a specific link.
	 * @param linkId The id for the link.
	 * @return Click details for a specific link.
	 * @throws Exception
	 */
	public ClickReport getClickReport(String linkId) throws Exception {
		return connection.getCampaignClickReport(getId(), linkId);
	}
	
	/**
	 * Get information about subscribers who clicked a link.
	 * @param linkId The id for the link.
	 * @return Information about subscribers who clicked a link
	 * @throws Exception
	 */
	public Iterable<ClickReportMember> getClickDetailsReports(String linkId) throws Exception {
		return connection.getCampaignClickDetailsReports(getId(), linkId);
	}
	
	/**
	 * Get information about a specific subscriber who clicked a link.
	 * @param linkId The id for the link.
	 * @param subscriber The member's email address or subscriber hash
	 * @return Information about a specific subscriber who clicked a link
	 * @throws Exception
	 */
	public ClickReportMember getClickDetailsMemberReport(String linkId, String subscriber) throws Exception {
		return connection.getCampaignClickDetailsMemberReport(getId(), linkId, subscriber);
	}
	
	/**
	 * Get statistics for the top-performing domains from a campaign.
	 * @return Statistics for the top-performing domains from a campaign.
	 * @throws Exception
	 */
	public DomainPerformance getDomainPerformanceReport() throws Exception {
		return connection.getDomainPerformanceReport(getId());
	}
	
	/**
	 * Ecommerce product activity report for Campaign
	 * @param sortField Optional, sort products by this field.
	 * @return Breakdown of product activity for a campaign.
	 * @throws Exception
	 */
	public Iterable<EcommerceProductActivity> getEcommerceProductActivityReports(EcommerceSortField sortField) throws Exception {
		return connection.getEcommerceProductActivityReports(getId(), sortField);
	}

	/**
	 * Sub report - Sent To - Get information about campaign recipients.
	 * @return Information about campaign recipients.
	 * @throws Exception
	 */
	public Iterable<ReportSentTo> getSentToReports() throws Exception {
		return connection.getCampaignSentToReports(getId());
	}

	/**
	 * Sent To Recipient report - Get information about a specific campaign recipient.
	 * @return Information about a specific campaign recipients.
	 * @throws Exception 
	 */
	public ReportSentTo getSentToRecipientReport(String subscriberHash) throws Exception {
		return connection.getCampaignSentToRecipientReport(getId(), subscriberHash);
	}
	
	/**
	 * Email Activity report - Get list member activity.
	 * @return Member activity for a campaign.
	 */
	public Iterable<EmailActivity> getEmailActivityReports() {
		return connection.getCampaignEmailActivityReports(getId());
	}
	
	/**
	 * Email Activity report - Get a specific list member's activity in a campaign including opens, clicks, and bounces.
	 * @param campaignId The unique id for the campaign.
	 * @return Member activity for a campaign.
	 * @throws Exception 
	 */
	public EmailActivity getEmailActivityReport(String subscriber) throws Exception {
		return connection.getCampaignEmailActivityReport(getId(), subscriber);
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
