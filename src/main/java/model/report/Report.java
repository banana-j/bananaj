/**
 * @author alexanderweiss
 * @date 19.11.2015
 */
package model.report;

import model.MailchimpObject;
import model.campaign.Bounce;
import org.json.JSONObject;

import java.time.LocalDateTime;

/**
 * Object for representing a report of a campaign
 * @author alexanderweiss
 *
 */
public class Report extends MailchimpObject{

	private String campaign_title;
	private int emails_sent_total;
	private int abuse_report;
	private int unsubscribe_total;
	private LocalDateTime time_sent;
	private Bounce bounces;
	private Forward forwards;
	private Open opens;
	private Click clicks;
	private FacebookLikes facebook_likes;
	private IndustryStats industry_stats;
	private ReportListStats report_list_stats;
	
	
	public Report(String camapignID,String campaign_title, int emails_sent_total, int abuse_report, int unsubscribe_total, LocalDateTime time_sent, Bounce bounces, Forward forwards,Click clicks,Open opens,FacebookLikes facebook_likes,IndustryStats industry_stats,ReportListStats report_list_stats,JSONObject jsonRepresentation) {
		super(camapignID,jsonRepresentation);
		this.campaign_title = campaign_title;
		this.emails_sent_total = emails_sent_total;
		this.abuse_report = abuse_report;
		this.unsubscribe_total = unsubscribe_total;
		this.time_sent = time_sent;
		this.bounces = bounces;
		this.forwards = forwards;
		this.clicks = clicks;
		this.opens = opens;
		this.facebook_likes = facebook_likes;
		this.industry_stats = industry_stats;
		this.report_list_stats = report_list_stats;
	}


	/**
	 * @return the emails_send
	 */
	public int getEmails_send_total() {
		return emails_sent_total;
	}

	/**
	 * @return the campaign_title
	 */
	public String getCampaign_title() {
		return campaign_title;
	}

	/**
	 * @return the abuse_report
	 */
	public int getAbuse_report() {
		return abuse_report;
	}

	/**
	 * @return the unsubscribe_total
	 */
	public int getUnsubscribe_total() {
		return unsubscribe_total;
	}

	/**
	 * @return the time_sent
	 */
	public LocalDateTime getTime_sent() {
		return time_sent;
	}

	/**
	 * @return the bounces
	 */
	public Bounce getBounces() {
		return bounces;
	}

	/**
	 * @return the forwards
	 */
	public Forward getForwards() {
		return forwards;
	}

	/**
	 * @return the clicks
	 */
	public Click getClicks() {
		return clicks;
	}

	/**
	 * @return the opens
	 */
	public Open getOpens() {
		return opens;
	}

	/**
	 * @return the facebook_likes
	 */
	public FacebookLikes getFacebook_likes() {
		return facebook_likes;
	}

	/**
	 * @return the industry_stats
	 */
	public IndustryStats getIndustry_stats() {
		return industry_stats;
	}

	/**
	 * @return the report_list_stats
	 */
	public ReportListStats getReport_list_stats() {
		return report_list_stats;
	}

	@Override
	public String toString(){
		return "Report of campaign: " + this.getId() +" " +this.getCampaign_title() + System.lineSeparator() +
				"Total emails sent: " + this.getEmails_send_total() + System.lineSeparator() +
				"Total abuse reports: " + this.getAbuse_report() +  System.lineSeparator() +
				"Total unsubscribed: " + this.getUnsubscribe_total() + System.lineSeparator() +
				"Time sent: " + this.getTime_sent() + System.lineSeparator() +
				"Bounces: " + System.lineSeparator() +
				"    Soft bounces: " + this.getBounces().getSoft_bounces() + System.lineSeparator() +
				"    Hard bounces: " +  this.getBounces().getHard_bounces() + System.lineSeparator() +
				"    Syntax error bounces: " + this.getBounces().getSyntax_error_bounces() + System.lineSeparator() +
				"Forwards: " + System.lineSeparator() +
				"    Forward count: " + this.getForwards().getCount() + System.lineSeparator() +
				"    Forward open: " + this.getForwards().getFowards_open() + System.lineSeparator() +
				"Clicks: " + System.lineSeparator() +
				"    Clicks total: " + this.getClicks().getClicks_total() + System.lineSeparator() +
				"    Unique clicks: " + this.getClicks().getUnique_clicks() + System.lineSeparator() +
				"    Unique subscriber links: " + this.getClicks().getUnique_subscriber_clicks() + System.lineSeparator() +
				"    Click rate: " + this.getClicks().getClick_rate() + System.lineSeparator() +
				"    Last click: " + this.getClicks().getLast_click();
	}

}
