/**
 * @author alexanderweiss
 * @date 19.11.2015
 */
package model.campaign;

import connection.MailChimpConnection;
import model.MailchimpObject;
import model.list.MailChimpList;
import model.report.*;
import org.json.JSONException;
import org.json.JSONObject;
import utils.DateConverter;

import java.net.URL;

/**
 * Class for representing a mailchimp campaign
 * @author alexanderweiss
 *
 */
public class Campaign extends MailchimpObject {

	private MailChimpConnection connection;
	private MailChimpList mailChimpList;
	private String title;
	private CampaignContent content;
	private static String REPORTENDPOINT;
	private CampaignType campaign_type;
	private CampaignStatus campaign_status;
	
	
	public Campaign(String id, String title, MailChimpList mailChimpList, CampaignType campaign_type, CampaignStatus campaign_status, MailChimpConnection connection, JSONObject jsonRepresentation) {
		super(id,jsonRepresentation);
		this.connection = connection;
		this.mailChimpList = mailChimpList;
		this.title = title;
		this.REPORTENDPOINT = "https://"+this.connection.getServer()+".api.mailchimp.com/3.0/reports/"+this.getId();
		this.campaign_type = campaign_type;
		this.campaign_status = campaign_status;
		try {
			setContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the report of this campaign
	 * @return
	 * @throws Exception
     */
	public Report getReport() throws Exception{

		final JSONObject report = new JSONObject(connection.do_Get(new URL(getREPORTENDPOINT()),connection.getApikey()));
		final JSONObject bounces = report.getJSONObject("bounces");
		final JSONObject forwards = report.getJSONObject("forwards");
		final JSONObject opens = report.getJSONObject("opens");
		final JSONObject clicks = report.getJSONObject("clicks");
		final JSONObject facebook_likes = report.getJSONObject("facebook_likes");
		final JSONObject industry_stats = report.getJSONObject("industry_stats");
		final JSONObject report_list_stats = report.getJSONObject("list_stats");
		
		
		Bounce bouncesObject = new Bounce(bounces.getInt("hard_bounces"),bounces.getInt("soft_bounces"),bounces.getInt("syntax_errors"));
		Forward forwardsObject = new Forward(forwards.getInt("forwards_count"), forwards.getInt("forwards_opens"));
		Click clicksObject = new Click(clicks.getInt("clicks_total"),clicks.getInt("unique_clicks"),clicks.getInt("unique_subscriber_clicks"),clicks.getDouble("click_rate"), DateConverter.getInstance().createDateFromISO8601(clicks.getString("last_click")));
    	Open opensObject = new Open(opens.getInt("opens_total"),opens.getInt("unique_opens"), opens.getDouble("open_rate"), opens.getString("last_open"));
    	FacebookLikes facebookObject = new FacebookLikes(facebook_likes.getInt("recipient_likes"),facebook_likes.getInt("unique_likes"),facebook_likes.getInt("facebook_likes"));
    	IndustryStats industryStatsObject = new IndustryStats(industry_stats.getString("type"), industry_stats.getDouble("open_rate"),industry_stats.getDouble("click_rate"),industry_stats.getDouble("bounce_rate"),industry_stats.getDouble("unopen_rate"),industry_stats.getDouble("unsub_rate"), industry_stats.getDouble("abuse_rate"));
    	ReportListStats reportListStatsObject = new ReportListStats(report_list_stats.getDouble("sub_rate"), report_list_stats.getDouble("unsub_rate"), report_list_stats.getDouble("open_rate"), report_list_stats.getDouble("click_rate"));
    	
    	
		return new Report(report.getString("id"), report.getString("campaign_title"),report.getInt("emails_sent"),report.getInt("abuse_reports"), report.getInt("unsubscribed"),DateConverter.getInstance().createDateFromISO8601(report.getString("send_time")),bouncesObject,forwardsObject,clicksObject,opensObject,facebookObject,industryStatsObject,reportListStatsObject,report);
	}
	
	/**
	 * Send the campaign to the mailChimpList members
	 */
	public void send() throws Exception{
		getConnection().do_Post(new URL(connection.getCampaignendpoint()+"/"+this.getId()+"/actions/send"),connection.getApikey());
	}
	
	/**
	 * Stops the sending of your campaign
	 * (!Only included in mailchimp pro)
	 */
	public void cancelSend() throws Exception{
		getConnection().do_Post(new URL(connection.getCampaignendpoint()+"/"+this.getId()+"/actions/cancel-send"),connection.getApikey());
	}

	/**
	 * @return the connection
	 */
	public MailChimpConnection getConnection() {
		return connection;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the REPORTSENDPOINT
	 */
	public String getREPORTENDPOINT() {
		return REPORTENDPOINT;
	}

	/**
	 * @return the campaign_type
	 */
	public CampaignType getCampaign_type() {
		return campaign_type;
	}

	/**
	 * @return the campaign_status
	 */
	public CampaignStatus getCampaign_status() {
		return campaign_status;
	}

	/**
	 * @return the mailChimpList
	 */
	public MailChimpList getMailChimpList() {
		return mailChimpList;
	}

	/**
	 * @return the content
	 */
	public CampaignContent getContent() {
		return content;
	}

	/**
	 * Set the content of this campaign
	 */
	private void setContent() throws Exception{
		JSONObject content = new JSONObject(getConnection().do_Get(new URL(connection.getCampaignendpoint()+"/"+this.getId()+"/content"),connection.getApikey()));
		try{
			this.content = new CampaignContent(content.getString("plain_text"), content.getString("html"), this) ;
		} catch (JSONException jsone) { //no plain_text available
			this.content = new CampaignContent(null, content.getString("html"), this) ;
		}
	}

	@Override
	public String toString(){
		return "ID: " + this.getId() + System.lineSeparator() +
				"Title: " +this.getTitle() + System.lineSeparator() +
				"Type of campaign: " + this.getCampaign_type().getStringRepresentation() +  System.lineSeparator() +
				"Status of campaign: " + this.getCampaign_status().getStringRepresentation() + System.lineSeparator();
	}

}
