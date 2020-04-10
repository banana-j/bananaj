/**
 * @author alexanderweiss
 * @date 19.11.2015
 */
package com.github.bananaj.model.report;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.bananaj.model.campaign.Bounce;
import com.github.bananaj.model.campaign.CampaignType;
import com.github.bananaj.utils.DateConverter;

/**
 * Mailchimp's campaign and Automation reports analyze clicks, opens, subscribers' social activity, e-commerce data, and more.
 * 
 * @author alexanderweiss
 *
 */
public class Report {

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

	public Report(JSONObject jsonObj) {
		id = jsonObj.getString("id");
		campaignTitle = jsonObj.getString("campaign_title");
		type = CampaignType.valueOf(jsonObj.getString("type").toUpperCase());
		listId = jsonObj.getString("list_id");
		listIsActive = jsonObj.getBoolean("list_is_active");
		listName = jsonObj.getString("list_name");
		subjectLine = jsonObj.getString("subject_line");
		previewText = jsonObj.getString("preview_text");
		emailsSent = jsonObj.getInt("emails_sent");
		abuseReport = jsonObj.getInt("abuse_reports");
		unsubscribed = jsonObj.getInt("unsubscribed");
		sendtime = DateConverter.fromISO8601(jsonObj.getString("send_time"));
		rssLastSend = jsonObj.has("rss_last_send") ? DateConverter.fromISO8601(jsonObj.getString("rss_last_send")) : null;
		bounces = new Bounce(jsonObj.getJSONObject("bounces"));
		forwards = new Forward(jsonObj.getJSONObject("forwards"));
		clicks = new Click(jsonObj.getJSONObject("clicks"));
		opens = new Open(jsonObj.getJSONObject("opens"));
		facebookLikes = jsonObj.has("facebook_likes") ? new FacebookLikes(jsonObj.getJSONObject("facebook_likes")) : null;
		industryStats = jsonObj.has("industry_stats") ? new IndustryStats(jsonObj.getJSONObject("industry_stats")) : null;
		listStats = jsonObj.has("list_stats") ? new ReportListStats(jsonObj.getJSONObject("list_stats")) : null;
		abSplit = jsonObj.has("ab_split") ? new ABSplit(jsonObj.getJSONObject("ab_split")) : null;
		
		if (jsonObj.has("timewarp")) {
			final JSONArray series = jsonObj.getJSONArray("timewarp");
			timewarp = new ArrayList<Timewarp>(series.length());
			for(int i=0; i<series.length(); i++) {
				timewarp.add(new Timewarp(series.getJSONObject(i)));
			}
		}
		
		if (jsonObj.has("timeseries")) {
			final JSONArray series = jsonObj.getJSONArray("timeseries");
			timeseries = new ArrayList<TimeSeries>(series.length());
			for(int i=0; i<series.length(); i++) {
				timeseries.add(new TimeSeries(series.getJSONObject(i)));
			}
		}
		
		shareReport = jsonObj.has("share_report") ? new ShareReport(jsonObj.getJSONObject("share_report")) : null;
		ecommerce = jsonObj.has("ecommerce") ? new Ecommerce(jsonObj.getJSONObject("ecommerce")) : null;
		deliveryStatus = jsonObj.has("delivery_status") ? new DeliveryStatus(jsonObj.getJSONObject("delivery_status")) : null;
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
