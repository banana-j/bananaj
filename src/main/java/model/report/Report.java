/**
 * @author alexanderweiss
 * @date 19.11.2015
 */
package model.report;

import java.util.Date;

import org.json.JSONObject;

import model.MailchimpObject;
import model.campaign.Bounce;

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
	private Date time_sent;
	private Bounce bounces;
	private Forward forwards;
	private Open opens;
	private Click clicks;
	private FacebookLikes facebook_likes;
	private IndustryStats industry_stats;
	private ReportListStats report_list_stats;
	
	
	public Report(String camapignID,String campaign_title, int emails_sent_total, int abuse_report, int unsubscribe_total, Date time_sent, Bounce bounces, Forward forwards,Click clicks,Open opens,FacebookLikes facebook_likes,IndustryStats industry_stats,ReportListStats report_list_stats,JSONObject jsonRepresentation) {
		super(camapignID,jsonRepresentation);
		setCampaign_title(campaign_title);
		setEmails_sent_total(emails_sent_total);
		setAbuse_report(abuse_report);
		setUnsubscribe_total(unsubscribe_total);
		setTime_sent(time_sent);
		setBounces(bounces);
		setForwards(forwards);
		setOpens(opens);
		setClicks(clicks);
		setFacebook_likes(facebook_likes);
		setIndustry_stats(industry_stats);
		setReport_list_stats(report_list_stats);
	}


	/**
	 * @return the emails_send
	 */
	public int getEmails_send_total() {
		return emails_sent_total;
	}


	/**
	 * @param emails_sent_total the emails_send to set
	 */
	private void setEmails_sent_total(int emails_sent_total) {
		this.emails_sent_total = emails_sent_total;
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


	/**
	 * @return the campaign_title
	 */
	public String getCampaign_title() {
		return campaign_title;
	}

	/**
	 * @param campaign_title the campaign_title to set
	 */
	private void setCampaign_title(String campaign_title) {
		this.campaign_title = campaign_title;
	}


	/**
	 * @return the abuse_report
	 */
	public int getAbuse_report() {
		return abuse_report;
	}


	/**
	 * @param abuse_report the abuse_report to set
	 */
	private void setAbuse_report(int abuse_report) {
		this.abuse_report = abuse_report;
	}


	/**
	 * @return the unsubscribe_total
	 */
	public int getUnsubscribe_total() {
		return unsubscribe_total;
	}


	/**
	 * @param unsubscribe_total the unsubscribe_total to set
	 */
	private void setUnsubscribe_total(int unsubscribe_total) {
		this.unsubscribe_total = unsubscribe_total;
	}


	/**
	 * @return the time_sent
	 */
	public Date getTime_sent() {
		return time_sent;
	}


	/**
	 * @param time_sent the time_send to set
	 */
	private void setTime_sent(Date time_sent) {
		this.time_sent = time_sent;
	}


	/**
	 * @return the bounces
	 */
	public Bounce getBounces() {
		return bounces;
	}


	/**
	 * @param bounces the bounces to set
	 */
	private void setBounces(Bounce bounces) {
		this.bounces = bounces;
	}


	/**
	 * @return the forwards
	 */
	public Forward getForwards() {
		return forwards;
	}


	/**
	 * @param forwards the forwards to set
	 */
	private void setForwards(Forward forwards) {
		this.forwards = forwards;
	}

	/**
	 * @return the clicks
	 */
	public Click getClicks() {
		return clicks;
	}

	/**
	 * @param clicks the clicks to set
	 */
	private void setClicks(Click clicks) {
		this.clicks = clicks;
	}


	/**
	 * @return the opens
	 */
	public Open getOpens() {
		return opens;
	}


	/**
	 * @param opens the opens to set
	 */
	public void setOpens(Open opens) {
		this.opens = opens;
	}


	/**
	 * @return the facebook_likes
	 */
	public FacebookLikes getFacebook_likes() {
		return facebook_likes;
	}


	/**
	 * @param facebook_likes the facebook_likes to set
	 */
	public void setFacebook_likes(FacebookLikes facebook_likes) {
		this.facebook_likes = facebook_likes;
	}


	/**
	 * @return the industry_stats
	 */
	public IndustryStats getIndustry_stats() {
		return industry_stats;
	}


	/**
	 * @param industry_stats the industry_stats to set
	 */
	public void setIndustry_stats(IndustryStats industry_stats) {
		this.industry_stats = industry_stats;
	}


	/**
	 * @return the report_list_stats
	 */
	public ReportListStats getReport_list_stats() {
		return report_list_stats;
	}


	/**
	 * @param report_list_stats the report_list_stats to set
	 */
	public void setReport_list_stats(ReportListStats report_list_stats) {
		this.report_list_stats = report_list_stats;
	}

}
