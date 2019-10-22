/**
 * @author alexanderweiss
 * @date 19.11.2015
 */
package com.github.alexanderwe.bananaj.model.report;

import java.time.LocalDateTime;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.model.MailchimpObject;
import com.github.alexanderwe.bananaj.model.campaign.Bounce;
import com.github.alexanderwe.bananaj.model.campaign.CampaignType;
import com.github.alexanderwe.bananaj.utils.DateConverter;

/**
 * Object for representing a report of a campaign
 * @author alexanderweiss
 *
 */
public class Report extends MailchimpObject {

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
	private LocalDateTime sendtime;
	//private String rss_last_send;
	private Bounce bounces;
	private Forward forwards;
	private Open opens;
	private Click clicks;
	private FacebookLikes facebookLikes;
	private IndustryStats industryStats;
	private ReportListStats listStats;
	//private Object ab_split;
	//private List<Object> timewarp;
	//private List<Object> timeseries;
	//private Object share_report;
	private Ecommerce ecommerce;
	//private Object delivery_status;

	public Report(JSONObject jsonObj) {
		super(jsonObj.getString("id"), null);
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
		sendtime = DateConverter.getInstance().createDateFromISO8601(jsonObj.getString("send_time"));
		bounces = new Bounce(jsonObj.getJSONObject("bounces"));
		forwards = new Forward(jsonObj.getJSONObject("forwards"));
		clicks = new Click(jsonObj.getJSONObject("clicks"));
		opens = new Open(jsonObj.getJSONObject("opens"));
		if (jsonObj.has("facebook_likes")) {
			facebookLikes = new FacebookLikes(jsonObj.getJSONObject("facebook_likes"));
		}
		if (jsonObj.has("industry_stats")) {
			industryStats = new IndustryStats(jsonObj.getJSONObject("industry_stats"));
		}
		if (jsonObj.has("list_stats")) {
			this.listStats = new ReportListStats(jsonObj.getJSONObject("list_stats"));
		}
		if (jsonObj.has("ecommerce")) {
			ecommerce = new Ecommerce(jsonObj.getJSONObject("ecommerce"));
		}
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
	public LocalDateTime getSendTime() {
		return sendtime;
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
	 * @return E-Commerce stats for a campaign.
	 */
	public Ecommerce getEcommerce() {
		return ecommerce;
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
	public LocalDateTime getSendtime() {
		return sendtime;
	}

	@Override
	public String toString(){
		return "Report of campaign: " + getId() + " " + this.getCampaignTitle() + System.lineSeparator() +
				"Total emails sent: " + getEmailsSent() + System.lineSeparator() +
				"Total abuse reports: " + getAbuseReport() +  System.lineSeparator() +
				"Total unsubscribed: " + getUnsubscribed() + System.lineSeparator() +
				"Time sent: " + getSendTime() + System.lineSeparator() +
				getForwards().toString() + System.lineSeparator() +
				getOpens().toString() + System.lineSeparator() +
				getBounces().toString() + System.lineSeparator() +
				getClicks().toString() + System.lineSeparator() +
				(getFacebookLikes() != null ? getFacebookLikes().toString() + System.lineSeparator() : "") +
				(getIndustryStats() != null ? getIndustryStats().toString() + System.lineSeparator() : "") +
				(getListStats() != null ? getListStats().toString() + System.lineSeparator() : "") +
				(getEcommerce() != null ? getEcommerce().toString() : "");
	}

}
