/**
 * @author alexanderweiss
 * @date 19.11.2015
 */
package model.campaign;

import java.net.URL;
import java.util.Date;

import model.list.MailChimpList;
import org.json.JSONObject;

import connection.MailchimpConnection;
import model.MailchimpObject;
import model.report.Click;
import model.report.FacebookLikes;
import model.report.Forward;
import model.report.IndustryStats;
import model.report.Open;
import model.report.Report;
import model.report.ReportListStats;

/**
 * Class for representing a mailchimp campaign
 * @author alexanderweiss
 *
 */
public class Campaign extends MailchimpObject {

	private MailchimpConnection connection;
	private MailChimpList mailChimpList;
	private String title;
	private CampaignContent content;
	private String REPORTENDPOINT;
	private CampaignType campaign_type;
	private CampaignStatus campaign_status;
	
	
	public Campaign(String id, String title, MailChimpList mailChimpList, CampaignType campaign_type, CampaignStatus campaign_status, MailchimpConnection connection, JSONObject jsonRepresentation) {
		super(id,jsonRepresentation);
		setConnection(connection);
		setMailChimpList(mailChimpList);
		setTitle(title);
		try {
			setContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setREPORTENDPOINT("https://"+this.connection.getServer()+".api.mailchimp.com/3.0/reports/"+this.getId());
		setCampaign_type(campaign_type);
		setCampaign_status(campaign_status);
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
		Date last_click;
		try{
			last_click = javax.xml.bind.DatatypeConverter.parseDateTime(clicks.getString("last_click")).getTime();
		}catch(IllegalArgumentException iae){
			last_click = null;
		}
    	
		Click clicksObject = new Click(clicks.getInt("clicks_total"),clicks.getInt("unique_clicks"),clicks.getInt("unique_subscriber_clicks"),clicks.getDouble("click_rate"),last_click);
    	Date send_time = javax.xml.bind.DatatypeConverter.parseDateTime(report.getString("send_time")).getTime();
    	Open opensObject = new Open(opens.getInt("opens_total"),opens.getInt("unique_opens"), opens.getDouble("open_rate"), opens.getString("last_open"));
    	FacebookLikes facebookObject = new FacebookLikes(facebook_likes.getInt("recipient_likes"),facebook_likes.getInt("unique_likes"),facebook_likes.getInt("facebook_likes"));
    	IndustryStats industryStatsObject = new IndustryStats(industry_stats.getString("type"), industry_stats.getDouble("open_rate"),industry_stats.getDouble("click_rate"),industry_stats.getDouble("bounce_rate"),industry_stats.getDouble("unopen_rate"),industry_stats.getDouble("unsub_rate"), industry_stats.getDouble("abuse_rate"));
    	ReportListStats reportListStatsObject = new ReportListStats(report_list_stats.getDouble("sub_rate"), report_list_stats.getDouble("unsub_rate"), report_list_stats.getDouble("open_rate"), report_list_stats.getDouble("click_rate"));
    	
    	
		return new Report(report.getString("id"), report.getString("campaign_title"),report.getInt("emails_sent"),report.getInt("abuse_reports"), report.getInt("unsubscribed"),send_time,bouncesObject,forwardsObject,clicksObject,opensObject,facebookObject,industryStatsObject,reportListStatsObject,report);	
	}
	
	/**
	 * Send the campaign to the mailChimpList members
	 */
	public void send() throws Exception{
		getConnection().do_Post(new URL(connection.getCAMPAIGNENDPOINT()+"/"+this.getId()+"/actions/send"),connection.getApikey());
	}
	
	/**
	 * Stops the sending of your campaign
	 * (!Only included in mailchimp pro)
	 */
	public void cancelSend() throws Exception{
		getConnection().do_Post(new URL(connection.getCAMPAIGNENDPOINT()+"/"+this.getId()+"/actions/cancel-send"),connection.getApikey());
	}
	
	/**
	 * @return the connection
	 */
	public MailchimpConnection getConnection() {
		return connection;
	}

	/**
	 * @param connection the connection to set
	 */
	public void setConnection(MailchimpConnection connection) {
		this.connection = connection;
	}

	
	@Override
	public String toString(){
		return "ID: " + this.getId() + System.lineSeparator() +
				"Title: " +this.getTitle() + System.lineSeparator() + 
				"Type of campaign: " + this.getCampaign_type().getStringRepresentation() +  System.lineSeparator() + 
				"Status of campaign: " + this.getCampaign_status().getStringRepresentation() + System.lineSeparator();
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the name to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the REPORTSENDPOINT
	 */
	public String getREPORTENDPOINT() {
		return REPORTENDPOINT;
	}

	/**
	 * @param rEPORTSENDPOINT the rEPORTSENDPOINT to set
	 */
	public void setREPORTENDPOINT(String rEPORTSENDPOINT) {
		REPORTENDPOINT = rEPORTSENDPOINT;
	}

	/**
	 * @return the campaign_type
	 */
	public CampaignType getCampaign_type() {
		return campaign_type;
	}

	/**
	 * @param campaign_type the campaign_type to set
	 */
	public void setCampaign_type(CampaignType campaign_type) {
		this.campaign_type = campaign_type;
	}

	/**
	 * @return the campaign_status
	 */
	public CampaignStatus getCampaign_status() {
		return campaign_status;
	}

	/**
	 * @param campaign_status the campaign_status to set
	 */
	public void setCampaign_status(CampaignStatus campaign_status) {
		this.campaign_status = campaign_status;
	}

	/**
	 * @return the mailChimpList
	 */
	public MailChimpList getMailChimpList() {
		return mailChimpList;
	}

	/**
	 * @param mailChimpList the mailChimpList to set
	 */
	public void setMailChimpList(MailChimpList mailChimpList) {
		this.mailChimpList = mailChimpList;
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
		JSONObject content = new JSONObject(getConnection().do_Get(new URL(connection.getCAMPAIGNENDPOINT()+"/"+this.getId()+"/content"),connection.getApikey()));
		this.content = new CampaignContent(content.getString("plain_text"), content.getString("html"), this) ;
	}
}
