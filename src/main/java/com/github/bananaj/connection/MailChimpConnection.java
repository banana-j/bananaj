package com.github.bananaj.connection;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidParameterException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.bananaj.exceptions.TransportException;
import com.github.bananaj.model.automation.Automation;
import com.github.bananaj.model.automation.AutomationRecipient;
import com.github.bananaj.model.automation.AutomationSettings;
import com.github.bananaj.model.automation.emails.AutomationEmail;
import com.github.bananaj.model.campaign.Campaign;
import com.github.bananaj.model.campaign.CampaignFeedback;
import com.github.bananaj.model.campaign.CampaignFolder;
import com.github.bananaj.model.campaign.CampaignRecipients;
import com.github.bananaj.model.campaign.CampaignSendChecklist;
import com.github.bananaj.model.campaign.CampaignSettings;
import com.github.bananaj.model.campaign.CampaignType;
import com.github.bananaj.model.filemanager.FileManager;
import com.github.bananaj.model.list.MailChimpList;
import com.github.bananaj.model.list.member.Member;
import com.github.bananaj.model.report.AbuseReport;
import com.github.bananaj.model.report.AdviceReport;
import com.github.bananaj.model.report.ClickReport;
import com.github.bananaj.model.report.ClickReportMember;
import com.github.bananaj.model.report.DomainPerformance;
import com.github.bananaj.model.report.EcommerceProductActivity;
import com.github.bananaj.model.report.EcommerceSortField;
import com.github.bananaj.model.report.OpenReport;
import com.github.bananaj.model.report.Report;
import com.github.bananaj.model.template.Template;
import com.github.bananaj.model.template.TemplateFolder;
import com.github.bananaj.utils.DateConverter;
import com.github.bananaj.utils.ModelIterator;
import com.github.bananaj.utils.URLHelper;

/**
 * Class for the com.github.bananaj.connection to mailchimp servers. Used to get lists from mailchimp account.
 * @author alexanderweiss
 *
 */
public class MailChimpConnection extends Connection {

	private String server;
	private String authorization;
	private final String apiendpoint;
	private final String listendpoint;
	private final String campaignfolderendpoint;
	private final String campaignendpoint;
	private final String templatefolderendpoint;
	private final String templateendpoint;
	private final String automationendpoint;
	private final String filemanagerfolderendpoint;
	private final String filesendpoint;
	private final String reportsendpoint;
	private Account account;
	private FileManager fileManager;

	/**
	 * Create a api key based mailchimp connection.
	 *
	 * @param apikey The api key to use, with the server identifier included in the end of the key
	 */
	public MailChimpConnection(String apikey){
		this(apikey.split("-")[1], "apikey", apikey);
	}

	public MailChimpConnection(final String server, final String tokenType, final String token){
		this.server = server;
		this.authorization = tokenType + " " + token;
		this.apiendpoint = "https://"+server+".api.mailchimp.com/3.0/";
		this.listendpoint = "https://"+server+".api.mailchimp.com/3.0/lists";
		this.campaignfolderendpoint =  "https://"+server+".api.mailchimp.com/3.0/campaign-folders";
		this.campaignendpoint ="https://"+server+".api.mailchimp.com/3.0/campaigns";
		this.templatefolderendpoint = "https://"+server+".api.mailchimp.com/3.0/template-folders";
		this.templateendpoint = "https://"+server+".api.mailchimp.com/3.0/templates";
		this.automationendpoint = "https://"+server+".api.mailchimp.com/3.0/automations";
		this.filemanagerfolderendpoint = "https://"+server+".api.mailchimp.com/3.0/file-manager/folders";
		this.filesendpoint = "https://"+server+".api.mailchimp.com/3.0/file-manager/files";
		this.reportsendpoint = "https://"+server+".api.mailchimp.com/3.0/reports";
	}

	/**
	 * Get the List/Audience iterator for in your account
	 * 
	 * Checked exceptions, including TransportException and JSONException, are
	 * warped in a RuntimeException to reduce the need for boilerplate code inside
	 * of lambdas.
	 * 
	 * @return List/audience iterator
	 */
	public Iterable<MailChimpList> getLists() {
		return new ModelIterator<MailChimpList>(MailChimpList.class, listendpoint, this, 500);
	}

	/**
	 * Get List/Audience in your account with pagination
	 * @param count Number of lists to return. Maximum value is 1000.
	 * @param offset Zero based offset
	 * @return List containing Mailchimp lists
	 * @throws Exception
	 */
	public List<MailChimpList> getLists(int count, int offset) throws Exception{
		List<MailChimpList> mailChimpLists = new ArrayList<MailChimpList>();
		// parse response
		JSONObject jsonLists = new JSONObject(do_Get(URLHelper.url(listendpoint, 
				"?offset=", Integer.toString(offset), "&count=", Integer.toString(count)),getApikey()));
		JSONArray listsArray = jsonLists.getJSONArray("lists");
		for( int i = 0; i< listsArray.length();i++)
		{
			JSONObject jsonList = listsArray.getJSONObject(i);
			MailChimpList mailChimpList = new MailChimpList(this,jsonList);
			mailChimpLists.add(mailChimpList);
		}
		return mailChimpLists;
	}
	
	/**
	 * Get a specific mailchimp List/Audience
	 * @return a Mailchimp list object
	 * @throws URISyntaxException 
	 * @throws TransportException 
	 * @throws MalformedURLException 
	 * @throws JSONException 
	 * @throws Exception
	 */
	public MailChimpList getList(String listID) throws JSONException, MalformedURLException, TransportException, URISyntaxException {
		JSONObject jsonList = new JSONObject(do_Get(URLHelper.url(listendpoint,"/",listID),getApikey()));
		return new MailChimpList(this, jsonList);
	}

	/**
	 * Create a new List/Audience in your mailchimp account
	 * @param audience
	 */
	public MailChimpList createList(MailChimpList audience) throws Exception {
		cacheAccountInfo();
		JSONObject jsonList = audience.getJSONRepresentation();
		JSONObject jsonNewList = new JSONObject(do_Post(new URL(listendpoint), jsonList.toString(),getApikey()));
		return new MailChimpList(this, jsonNewList);
	}

	/**
	 * Delete a List/Audience from your account
	 * @param listID
	 * @throws Exception
	 */
	public void deleteList(String listID) throws Exception {
		do_Delete(URLHelper.url(listendpoint,"/",listID), getApikey());
	}

	/**
	 * A health check for the API.
	 * @return true on successful API ping otherwise false
	 */
	public boolean ping() {
		try {
			JSONObject jsonObj = new JSONObject(do_Get(URLHelper.url(apiendpoint, "ping"), getApikey()));
			if (jsonObj.has("health_status") && "Everything's Chimpy!".equals(jsonObj.getString("health_status"))) {
				return true;
			}
		} catch (Exception ignored) { }
		return false;
	}
	
    /**
     * Get campaign folders iterator from MailChimp
     * @return campaign folder iterator
     */
    public Iterable<CampaignFolder> getCampaignFolders() throws Exception{
		return new ModelIterator<CampaignFolder>(CampaignFolder.class, campaignfolderendpoint, this);
    }

    /**
     * Get campaign folders from MailChimp with pagination
     * @param count Number of campaign folders to return. Maximum value is 1000.
     * @param offset Zero based offset
     * @return List containing the campaign folders
     */
    public List<CampaignFolder> getCampaignFolders(int count, int offset) throws Exception{
    	List<CampaignFolder> campaignFolders = new ArrayList<>();
    	JSONObject campaignFoldersResponse = new JSONObject(do_Get(URLHelper.url(campaignfolderendpoint, 
    			"?offset=", Integer.toString(offset), "&count=", Integer.toString(count)), getApikey()));

    	JSONArray campaignFoldersJSON = campaignFoldersResponse.getJSONArray("folders");

    	for(int i = 0 ; i < campaignFoldersJSON.length(); i++){
    		JSONObject campaignFolderJSON = campaignFoldersJSON.getJSONObject(i);
    		CampaignFolder campaignFolder = new CampaignFolder(this, campaignFolderJSON);
    		campaignFolders.add(campaignFolder);
    	}
    	return campaignFolders;
    }

    /**
     * Get a specific template folder
     * @param folder_id
     */
    public CampaignFolder getCampaignFolder(String folder_id) throws Exception {

    	JSONObject jsonCampaignFolder = new JSONObject(do_Get(URLHelper.url(campaignfolderendpoint,"/",folder_id), getApikey()));
    	return new CampaignFolder(this, jsonCampaignFolder);
    }

    /**
     * Add a template folder with a specific name
     * @param name Name to associate with the folder
     */
    public CampaignFolder addCampaignFolder(String name) throws Exception {
    	JSONObject campaignFolder = new JSONObject();
    	campaignFolder.put("name", name);
    	JSONObject jsonCampaignFolder = new JSONObject(do_Post(new URL(campaignfolderendpoint), campaignFolder.toString(), getApikey()));
    	return new CampaignFolder(this, jsonCampaignFolder);
    }

    /**
     * Delete a specific template folder
     * @param folder_id
     */
    public void deleteCampaignFolder(String folder_id) throws Exception {
    	do_Delete(URLHelper.url(campaignfolderendpoint,"/",folder_id), getApikey());
    }

    /**
     * Get campaigns iterator from mailchimp account
	 * 
	 * Checked exceptions, including TransportException and JSONException, are
	 * warped in a RuntimeException to reduce the need for boilerplate code inside
	 * of lambdas.
	 * 
     * @return Campaign iterator
     */
    public Iterable<Campaign> getCampaigns() {
		return new ModelIterator<Campaign>(Campaign.class, campaignendpoint, this, 500);
    }

    /**
     * Get campaigns from mailchimp account with pagination
     * @param count Number of campaigns to return. Maximum value is 1000.
     * @param offset Zero based offset
     * @return List containing campaigns
     * @throws Exception
     */
    public List<Campaign> getCampaigns(int count, int offset) throws Exception {
		if (count < 1 || count > 1000) {
			throw new InvalidParameterException("Page size must be 1-1000");
		}
    	// parse response
    	JSONObject jsonCampaigns = new JSONObject(do_Get(URLHelper.url(campaignendpoint, 
    			"?offset=", Integer.toString(offset), "&count=", Integer.toString(count)),getApikey()));
    	JSONArray campaignsArray = jsonCampaigns.getJSONArray("campaigns");
    	List<Campaign> campaigns = new ArrayList<Campaign>(campaignsArray.length());
    	for( int i = 0; i< campaignsArray.length();i++)
    	{
    		JSONObject campaignDetail = campaignsArray.getJSONObject(i);
    		Campaign campaign = new Campaign(this, campaignDetail);
    		campaigns.add(campaign);
    	}
    	return campaigns;
    }

	/**
	 * Get a campaign from mailchimp account
	 * @param campaignID
	 * @return a campaign object
	 * @throws Exception
	 */
	public Campaign getCampaign(String campaignID) throws Exception {
		JSONObject campaign = new JSONObject(do_Get(URLHelper.url(campaignendpoint,"/",campaignID),getApikey()));
		return new Campaign(this, campaign);
	}

	/**
	 * Create a new campaign in your mailchimp account
	 * @param type
	 * @param mailChimpList
	 * @param settings
	 */
	public Campaign createCampaign(CampaignType type, MailChimpList mailChimpList, CampaignSettings settings) throws Exception{
		
		JSONObject campaign = new JSONObject();
		
		JSONObject recipients = new JSONObject();
		recipients.put("list_id", mailChimpList.getId());
		
		JSONObject jsonSettings = settings.getJsonRepresentation();
		
		campaign.put("type", type.toString());
		campaign.put("recipients", recipients);
		campaign.put("settings", jsonSettings);
		
		campaign = new JSONObject(do_Post(new URL(campaignendpoint), campaign.toString(), getApikey()));
		return new Campaign(this, campaign);
	}

	public Campaign createCampaign(CampaignType type, CampaignRecipients mailRecipients, CampaignSettings settings) throws Exception {
		
		JSONObject campaign = new JSONObject();
		JSONObject recipients = mailRecipients.getJsonRepresentation();
		
		JSONObject jsonSettings = settings.getJsonRepresentation();
		
		campaign.put("type", type.toString());
		campaign.put("recipients", recipients);
		campaign.put("settings", jsonSettings);
		
		campaign = new JSONObject(do_Post(new URL(campaignendpoint), campaign.toString(), getApikey()));
		return new Campaign(this, campaign);
	}
	
	/**
	 * Delete a campaign from mailchimp account
	 * @param campaignID
	 * @throws Exception
	 */
	public void deleteCampaign(String campaignID) throws Exception{
		do_Delete(URLHelper.url(campaignendpoint,"/",campaignID),getApikey());
	}

	/**
	 * Get feedback about a campaign
	 * @param campaignID
	 * @return
	 * @throws URISyntaxException 
	 * @throws TransportException 
	 * @throws MalformedURLException 
	 * @throws JSONException 
	 * @throws Exception
	 */
	public List<CampaignFeedback> getCampaignFeedback(String campaignID) throws JSONException, MalformedURLException, TransportException, URISyntaxException {
		List<CampaignFeedback> feedback = new ArrayList<CampaignFeedback>();
		JSONObject campaignFeedback = new JSONObject(do_Get(URLHelper.url(getCampaignendpoint(),"/",campaignID,"/feedback"),getApikey()));
		
		JSONArray feedbackArray = campaignFeedback.getJSONArray("feedback");
		for( int i = 0; i< feedbackArray.length();i++)
		{
			JSONObject jsonObj = feedbackArray.getJSONObject(i);
			CampaignFeedback f = new CampaignFeedback(this, jsonObj);
			feedback.add(f);
		}
		
		return feedback;
	}
	
	/**
	 * Get a specific feedback about a campaign
	 * @param campaignID
	 * @param feedbackId
	 * @throws URISyntaxException 
	 * @throws TransportException 
	 * @throws MalformedURLException 
	 * @throws JSONException 
	 */
	public CampaignFeedback getCampaignFeedback(String campaignID, String feedbackId) throws JSONException, MalformedURLException, TransportException, URISyntaxException {
		JSONObject jsonObj = new JSONObject(do_Get(URLHelper.url(getCampaignendpoint(),"/",campaignID,"/feedback/",feedbackId),getApikey()));
		CampaignFeedback feedback = new CampaignFeedback(this, jsonObj);
		return feedback;
	}
	
	/**
	 * Add campaign feedback
	 * @param campaignID
	 * @param message
	 * @throws URISyntaxException 
	 * @throws TransportException 
	 * @throws MalformedURLException 
	 * @throws JSONException 
	 */
	CampaignFeedback createCampaignFeedback(String campaignID, String message) throws JSONException, MalformedURLException, TransportException, URISyntaxException {
		CampaignFeedback feedback = new CampaignFeedback.Builder()
				.connection(this)
				.campaignId(campaignID)
				.blockId(0)
				.message(message)
				.isComplete(true)
				.build();
		feedback.create();
		return feedback;
	}

	/**
	 * Get the send checklist for a campaign. Review the send checklist for your campaign, and resolve any issues before sending.
	 * @param campaignID
	 * @throws URISyntaxException 
	 * @throws TransportException 
	 * @throws MalformedURLException 
	 */
	CampaignSendChecklist getCampaignSendChecklist(String campaignID) throws MalformedURLException, TransportException, URISyntaxException {
		String results = do_Get(URLHelper.url(getCampaignendpoint(),"/",campaignID,"/send-checklist"), getApikey());
		return new CampaignSendChecklist(new JSONObject(results));
	}
	
	/**
	 * Mailchimp's campaign and Automation reports analyze clicks, opens, subscribers' social activity, e-commerce data, and more.
	 * 
	 * @param campaignId
	 * @return Report for the specified campaign. 
	 * @throws JSONException
	 * @throws TransportException
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 */
	public Report getCampaignReport(String campaignId) throws JSONException, TransportException, URISyntaxException, MalformedURLException {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId);
		JSONObject jsonReport = new JSONObject(do_Get(url, getApikey()));
    	return new Report(this, jsonReport);
	}
	
	/**
	 * Mailchimp's campaign and Automation reports analyze clicks, opens, subscribers' social activity, e-commerce data, and more.
	 * @param count Number of reports to return. Maximum value is 1000.
	 * @param offset Zero based offset
	 * @param campaignType Optional, restrict the response by campaign type. Possible values: regular, plaintext, absplit, rss, or variate.
	 * @param beforeSendTime Optional, restrict the response to campaigns sent before the set time.
	 * @param sinceSendTime Optional, restrict the response to campaigns sent after the set time.
	 * @return Campaign reports meeting the specified criteria.
	 * @throws JSONException
	 * @throws TransportException
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException 
	 */
	public List<Report> getCampaignReports(int count, int offset, CampaignType campaignType, ZonedDateTime beforeSendTime, ZonedDateTime sinceSendTime) throws JSONException, TransportException, URISyntaxException, MalformedURLException, UnsupportedEncodingException {
		URL url = URLHelper.url(getReportsendpoint(), 
				"?offset=", Integer.toString(offset), "&count=", Integer.toString(count),
				(campaignType!=null ? "&type=" + campaignType.toString() : ""),
				(beforeSendTime!=null ? "&"+"before_send_time=" + URLEncoder.encode(DateConverter.toISO8601UTC(beforeSendTime), "UTF-8") : ""),
				(sinceSendTime!=null ? "&"+"since_send_time=" + URLEncoder.encode(DateConverter.toISO8601UTC(sinceSendTime), "UTF-8") : "") );
		JSONObject jsonReports = new JSONObject(do_Get(url, getApikey()));
		//int total_items = jsonReports.getInt("total_items"); 	// The total number of items matching the query regardless of pagination
    	JSONArray reportsArray = jsonReports.getJSONArray("reports");
    	List<Report> reports = new ArrayList<Report>(reportsArray.length());
    	for( int i = 0; i< reportsArray.length();i++)
    	{
    		JSONObject reportDetail = reportsArray.getJSONObject(i);
    		Report report = new Report(this, reportDetail);
    		reports.add(report);
    	}
    	return reports;
	}
	
	/**
	 * Mailchimp's campaign and Automation reports analyze clicks, opens, subscribers' social activity, e-commerce data, and more.
	 * @param campaignType Optional, restrict the response by campaign type. Possible values: regular, plaintext, absplit, rss, or variate.
	 * @param beforeSendTime Optional, restrict the response to campaigns sent before the set time.
	 * @param sinceSendTime Optional, restrict the response to campaigns sent after the set time.
	 * @return Campaign reports meeting the specified criteria.
	 * @throws Exception
	 */
	public Iterable<Report> getCampaignReports(CampaignType campaignType, ZonedDateTime beforeSendTime, ZonedDateTime sinceSendTime) throws Exception {
		boolean firstSep = true;
		StringBuilder baseURL = new StringBuilder(getReportsendpoint());
		if (campaignType!=null) {
			baseURL.append("?type=").append(campaignType.toString());
			firstSep = false;
		}
		if (beforeSendTime!=null) {
			baseURL.append(firstSep ? "?" :"&").append("before_send_time=").append(URLEncoder.encode(DateConverter.toISO8601UTC(beforeSendTime), "UTF-8"));
			firstSep = false;
		}
		if (sinceSendTime!=null) { 
			baseURL.append(firstSep ? "?" :"&").append("since_send_time=").append(URLEncoder.encode(DateConverter.toISO8601UTC(sinceSendTime), "UTF-8"));
			firstSep = false;
		}
		return new ModelIterator<Report>(Report.class, baseURL.toString(), this);
	}

	/**
	 * Get a detailed report about any emails in a specific campaign that were opened by recipients.
	 * @param campaignId
	 * @param since Optional, restrict results to campaign open events that occur after a specific time.
	 * @return Detailed information about the campaigns emails that were opened by list members.
	 * @throws Exception
	 */
	public OpenReport getCampaignOpenReports(String campaignId, ZonedDateTime since) throws Exception {
		return OpenReport.getOpenReport(this, campaignId, since);
	}
	

	/**
	 * 
	 * @param count Number of reports to return. Maximum value is 1000.
	 * @param offset Zero based offset
	 * @param campaignId The unique id for the campaign.
	 * @return Abuse complaints for a campaign
	 * @throws MalformedURLException 
	 * @throws URISyntaxException 
	 * @throws TransportException 
	 * @throws JSONException 
	 */
	public List<AbuseReport>  getCampaignAbuseReports(int count, int offset, String campaignId) throws MalformedURLException, JSONException, TransportException, URISyntaxException {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId, "/abuse-reports",
				"?offset=", Integer.toString(offset), "&count=", Integer.toString(count));
		JSONObject jsonReports = new JSONObject(do_Get(url, getApikey()));
		//int total_items = jsonReports.getInt("total_items"); 	// The total number of items matching the query regardless of pagination
    	JSONArray reportsArray = jsonReports.getJSONArray("abuse_reports");
    	List<AbuseReport> reports = new ArrayList<AbuseReport>(reportsArray.length());
    	for( int i = 0; i< reportsArray.length();i++)
    	{
    		JSONObject reportDetail = reportsArray.getJSONObject(i);
    		AbuseReport report = new AbuseReport(reportDetail);
    		reports.add(report);
    	}
    	return reports;
	}
	
	/**
	 * Get information about a specific abuse report for a campaign.
	 * @param campaignId The unique id for the campaign.
	 * @param reportId The id for the abuse report.
	 * @return Information about a specific abuse report
	 * @throws MalformedURLException 
	 * @throws URISyntaxException 
	 * @throws TransportException 
	 * @throws JSONException 
	 */
	public AbuseReport  getCampaignAbuseReport(String campaignId, int reportId) throws MalformedURLException, JSONException, TransportException, URISyntaxException {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId, "/abuse-reports/", Integer.toString(reportId));
		JSONObject jsonReport = new JSONObject(do_Get(url, getApikey()));
		AbuseReport report = new AbuseReport(jsonReport);
		return report;
	}
	
	/**
	 * 
	 * @return Abuse complaints for a campaign
	 */
	public Iterable<AbuseReport>  getCampaignAbuseReports(String campaignId) {
		String query = getReportsendpoint()+"/"+campaignId+"/abuse-reports";
		return new ModelIterator<AbuseReport>(AbuseReport.class, query, this);
	}

	/**
	 * Get recent feedback based on a campaign's statistics.
	 * @param campaignId The unique id for the campaign.
	 * @return Recent feedback based on a campaign's statistics.
	 * @throws JSONException
	 * @throws TransportException
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException
	 */
	public List<AdviceReport> getCampaignAdviceReports(int count, int offset, String campaignId) throws JSONException, TransportException, URISyntaxException, MalformedURLException, UnsupportedEncodingException {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId, "/advice",
				"?offset=", Integer.toString(offset), "&count=", Integer.toString(count));
		JSONObject jsonReports = new JSONObject(do_Get(url, getApikey()));
		//int total_items = jsonReports.getInt("total_items"); 	// The total number of items matching the query regardless of pagination
    	JSONArray reportsArray = jsonReports.getJSONArray("advice");
    	List<AdviceReport> reports = new ArrayList<AdviceReport>(reportsArray.length());
    	for( int i = 0; i< reportsArray.length();i++)
    	{
    		JSONObject reportDetail = reportsArray.getJSONObject(i);
    		AdviceReport report = new AdviceReport(reportDetail);
    		reports.add(report);
    	}
    	return reports;
	}
	
	/**
	 * Get recent feedback based on a campaign's statistics.
	 * @param campaignId The unique id for the campaign.
	 * @return Recent feedback based on a campaign's statistics.
	 */
	public Iterable<AdviceReport> getCampaignAdviceReports(String campaignId) {
		String query = getReportsendpoint()+"/"+campaignId+"/advice";
		return new ModelIterator<AdviceReport>(AdviceReport.class, query, this);
	}
	
	/**
	 * Get detailed information about links clicked in campaigns.
	 * @param count Number of reports to return. Maximum value is 1000.
	 * @param offset Zero based offset
	 * @param campaignId The unique id for the campaign.
	 * @return Campaign click details
	 * @throws MalformedURLException
	 * @throws JSONException
	 * @throws TransportException
	 * @throws URISyntaxException
	 */
	public List<ClickReport> getClickReports(int count, int offset, String campaignId) throws MalformedURLException, JSONException, TransportException, URISyntaxException {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId, "/click-details",
				"?offset=", Integer.toString(offset), "&count=", Integer.toString(count));
		JSONObject jsonReports = new JSONObject(do_Get(url, getApikey()));
		//int total_items = jsonReports.getInt("total_items"); 	// The total number of items matching the query regardless of pagination
    	JSONArray reportsArray = jsonReports.getJSONArray("urls_clicked");
    	List<ClickReport> reports = new ArrayList<ClickReport>(reportsArray.length());
    	for( int i = 0; i< reportsArray.length();i++)
    	{
    		JSONObject reportDetail = reportsArray.getJSONObject(i);
    		ClickReport report = new ClickReport(reportDetail);
    		reports.add(report);
    	}
    	return reports;
	}
	
	/**
	 * Get detailed information about links clicked in campaigns for a specific link.
	 * @param campaignId The unique id for the campaign.
	 * @param linkId The id for the link.
	 * @return Click details for a specific link.
	 * @throws MalformedURLException
	 * @throws JSONException
	 * @throws TransportException
	 * @throws URISyntaxException
	 */
	public ClickReport getClickReport(String campaignId, String linkId) throws MalformedURLException, JSONException, TransportException, URISyntaxException {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId, "/click-details/", linkId);
		JSONObject jsonReport = new JSONObject(do_Get(url, getApikey()));
		ClickReport report = new ClickReport(jsonReport);
		return report;
	}
	
	/**
	 * Get information about subscribers who clicked a link.
	 * @param count Number of reports to return. Maximum value is 1000.
	 * @param offset Zero based offset
	 * @param campaignId The unique id for the campaign.
	 * @param linkId The id for the link.
	 * @return Information about subscribers who clicked a link
	 * @throws MalformedURLException
	 * @throws JSONException
	 * @throws TransportException
	 * @throws URISyntaxException
	 */
	public List<ClickReportMember> getClickReportMembers(int count, int offset, String campaignId, String linkId) throws MalformedURLException, JSONException, TransportException, URISyntaxException {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId, "/click-details/", linkId, "/members",
				"?offset=", Integer.toString(offset), "&count=", Integer.toString(count));
		JSONObject jsonReports = new JSONObject(do_Get(url, getApikey()));
		//int total_items = jsonReports.getInt("total_items"); 	// The total number of items matching the query regardless of pagination
    	JSONArray reportsArray = jsonReports.getJSONArray("members");
    	List<ClickReportMember> reports = new ArrayList<ClickReportMember>(reportsArray.length());
    	for( int i = 0; i< reportsArray.length();i++)
    	{
    		JSONObject reportDetail = reportsArray.getJSONObject(i);
    		ClickReportMember report = new ClickReportMember(reportDetail);
    		reports.add(report);
    	}
    	return reports;
	}
	
	/**
	 * Get information about a specific subscriber who clicked a link.
	 * @param campaignId The unique id for the campaign.
	 * @param linkId The id for the link.
	 * @param subscriber The member's email address or subscriber hash
	 * @return Information about a specific subscriber who clicked a link
	 * @throws MalformedURLException
	 * @throws JSONException
	 * @throws TransportException
	 * @throws URISyntaxException
	 */
	public ClickReportMember getClickReportMember(String campaignId, String linkId, String subscriber) throws MalformedURLException, JSONException, TransportException, URISyntaxException {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId, "/click-details/", linkId, "/members/", Member.subscriberHash(subscriber));
		JSONObject jsonReport = new JSONObject(do_Get(url, getApikey()));
		ClickReportMember report = new ClickReportMember(jsonReport);
		return report;
	}
	
	/**
	 * Get statistics for the top-performing domains from a campaign.
	 * @param campaignId The unique id for the campaign.
	 * @return Statistics for the top-performing domains from a campaign.
	 * @throws MalformedURLException
	 * @throws JSONException
	 * @throws TransportException
	 * @throws URISyntaxException
	 */
	public DomainPerformance getDomainPerformance(String campaignId) throws MalformedURLException, JSONException, TransportException, URISyntaxException {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId, "/domain-performance");
		JSONObject jsonReport = new JSONObject(do_Get(url, getApikey()));
		DomainPerformance report = new DomainPerformance(jsonReport);
		return report;
	}
	
	/**
	 * Ecommerce product activity report for Campaign
	 * @param count Number of reports to return. Maximum value is 1000.
	 * @param offset Zero based offset
	 * @param campaignId The unique id for the campaign.
	 * @param sortField Optional, sort products by this field.
	 * @return Breakdown of product activity for a campaign.
	 * @throws MalformedURLException
	 * @throws JSONException
	 * @throws TransportException
	 * @throws URISyntaxException
	 */
	public List<EcommerceProductActivity> getEcommerceProductActivity(int count, int offset, String campaignId, EcommerceSortField sortField) throws MalformedURLException, JSONException, TransportException, URISyntaxException {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId, "/ecommerce-product-activity",
				"?offset=", Integer.toString(offset), "&count=", Integer.toString(count), 
				"&sort_field=", (sortField != null ? sortField.toString() : EcommerceSortField.TITLE.toString()));
		JSONObject jsonReports = new JSONObject(do_Get(url, getApikey()));
		//int total_items = jsonReports.getInt("total_items"); 	// The total number of items matching the query regardless of pagination
    	JSONArray reportsArray = jsonReports.getJSONArray("products");
    	List<EcommerceProductActivity> reports = new ArrayList<EcommerceProductActivity>(reportsArray.length());
    	for( int i = 0; i< reportsArray.length();i++)
    	{
    		JSONObject reportDetail = reportsArray.getJSONObject(i);
    		EcommerceProductActivity report = new EcommerceProductActivity(reportDetail);
    		reports.add(report);
    	}
    	return reports;
	}
	
	// TODO: Report - Email Activity - Get list member activity for a specific campaign.
	// TODO: Report - Email Activity - Get list member activity for a specific campaign and subscriber.
	// TODO: Report - Location - Get top open locations for a specific campaign.
	// TODO: Report - Sent To - Get information about campaign recipients.
	// TODO: Report - Sent To - Get information about a specific campaign recipient.
	// TODO: Report - Sub-Reports- A list of reports for child campaigns of a specific parent campaign. For example, use this endpoint to view Multivariate, RSS, and A/B Testing Campaign reports.
	// TODO: Report - Unsubscribes - Get information about list members who unsubscribed from a specific campaign.
	// TODO: Report - EepURL Reports - Get a summary of social activity for the campaign, tracked by EepURL.
	
    /**
     * Get template folders iterator
     * @return template folder iterator
     */
	public Iterable<TemplateFolder> getTemplateFolders() throws Exception {
		return new ModelIterator<TemplateFolder>(TemplateFolder.class, templatefolderendpoint, this, 500);
	}

    /**
     * Get template folders from MailChimp with pagination
	 * @param count Number of templates to return. Maximum value is 1000.
	 * @param offset Zero based offset
     * @return List of template folders
     */
	public List<TemplateFolder> getTemplateFolders(int count, int offset) throws Exception{
        List<TemplateFolder> templateFolders = new ArrayList<>();
        JSONObject templateFoldersResponse = new JSONObject(do_Get(URLHelper.url(templatefolderendpoint, 
        		"?offset=", Integer.toString(offset), "&count=", Integer.toString(count)), getApikey()));
		//int total_items = templateFoldersResponse.getInt("total_items"); 	// The total number of items matching the query regardless of pagination
        JSONArray templateFoldersJSON = templateFoldersResponse.getJSONArray("folders");

        for(int i = 0 ; i < templateFoldersJSON.length(); i++){
            TemplateFolder templateFolder = new TemplateFolder(this, templateFoldersJSON.getJSONObject(i));
            templateFolders.add(templateFolder);
        }
        return templateFolders;
	}

    /**
     * Get a specific template folder
     * @param folder_id
     */
    public TemplateFolder getTemplateFolder(String folder_id) throws Exception{

        JSONObject jsonTemplateFolder = new JSONObject(do_Get(URLHelper.url(templatefolderendpoint,"/",folder_id), getApikey()));
        return new TemplateFolder(this, jsonTemplateFolder);
    }

    /**
     * Add a template folder with a specific name
     * @param name
     */
    public TemplateFolder createTemplateFolder(String name) throws Exception{
        JSONObject templateFolder = new JSONObject();
        templateFolder.put("name", name);
        JSONObject jsonTemplateFolder = new JSONObject(do_Post(new URL(templatefolderendpoint), templateFolder.toString(), getApikey()));
        return new TemplateFolder(this, jsonTemplateFolder);
    }

    /**
     * Delete a specific template folder
     * @param folder_id
     */
    public void deleteTemplateFolder(String folder_id) throws Exception{
        do_Delete(URLHelper.url(templatefolderendpoint,"/",folder_id), getApikey());
    }

	/**
	 * Get templates iterator from mailchimp account
	 * @return templates iterator
	 * @throws Exception
	 */
	public Iterable<Template> getTemplates() throws Exception{
		return new ModelIterator<Template>(Template.class, templateendpoint, this, 500);
	}

	/**
	 * Get templates from mailchimp account with pagination
	 * @param count Number of templates to return. Maximum value is 1000.
	 * @param offset Zero based offset
	 * @return list of templates
	 * @throws Exception
	 */
	public List<Template> getTemplates(int count, int offset) throws Exception{
		List<Template> templates = new ArrayList<Template>();

		JSONObject jsonTemplates = new JSONObject(do_Get(URLHelper.url(templateendpoint, 
				"?offset=", Integer.toString(offset), "&count=", Integer.toString(count)),getApikey()));
		JSONArray templatesArray = jsonTemplates.getJSONArray("templates");
		for( int i = 0; i< templatesArray.length();i++)
		{
			Template template = new Template(this, templatesArray.getJSONObject(i));
			templates.add(template);
		}
		return templates;
	}

	/**
	 * Get a template from mailchimp account
	 * @param id
	 * @return a template object
	 * @throws Exception
	 */
	public Template getTemplate(String id) throws Exception{
		JSONObject jsonTemplate = new JSONObject(do_Get(URLHelper.url(templateendpoint,"/",id),getApikey()));
		return new Template(this, jsonTemplate);
	}

	/**
	 * Add a template to your MailChimp account. Only Classic templates are
	 * supported. The Mailchimp Template Language is supported in any HTML code.
	 * 
	 * @param name The name of the template
	 * @param html The raw HTML for the template
	 * @throws Exception
	 */
	public Template createTemplate(String name, String html) throws Exception {
		return createTemplate(name, null, html);
	}

	/**
	 * Add a template to a specific folder to your MailChimp Account. Only Classic
	 * templates are supported. The Mailchimp Template Language is supported in any
	 * HTML code.
	 * 
	 * @param name      The name of the template
	 * @param folder_id The id of the folder the template is currently in
	 * @param html      The raw HTML for the template
	 * @throws Exception
	 */
	public Template createTemplate(String name, String folder_id, String html) throws Exception {
		return createTemplate(new Template.Builder()
				.withName(name)
				.inFolder(folder_id)
				.withHTML(html)
				.build());
	}

	/**
	 * Create a new template. Only Classic templates are supported.
	 * @param template
	 * @throws Exception
	 */
	public Template createTemplate(Template template) throws Exception {
		JSONObject jsonObj = template.getJsonRepresentation();
		String results = do_Post(URLHelper.url(templateendpoint,"/"), jsonObj.toString(),getApikey());
		template.parse(this, new JSONObject(results));
		return template;
	}
	
	/**
	 * Delete a specific template
	 * @param id
	 * @throws Exception
	 */
	public void deleteTemplate(String id) throws Exception {
		do_Delete(URLHelper.url(templateendpoint,"/",id),getApikey());
	}

	/**
	 * Get an Automations iterator
	 * @return automations iterator
	 * @throws Exception
	 */
	public Iterable<Automation> getAutomations() throws Exception{
		return new ModelIterator<Automation>(Automation.class, automationendpoint, this);
	}
	
	/**
	 * Get a list of Automations with pagination
	 * @param count Number of templates to return. Maximum value is 1000.
	 * @param offset Zero based offset
	 * @return List containing automations
	 * @throws Exception
	 */
	public List<Automation> getAutomations(int count, int offset) throws Exception {
		List<Automation> automations = new ArrayList<Automation>();

		JSONObject jsonAutomations = new JSONObject(do_Get(URLHelper.url(automationendpoint, 
				"?offset=", Integer.toString(offset), "&count=", Integer.toString(count)),getApikey()));
		//int total_items = jsonAutomations.getInt("total_items"); 	// The total number of items matching the query regardless of pagination
		JSONArray automationsArray = jsonAutomations.getJSONArray("automations");
		for( int i = 0; i< automationsArray.length();i++)
		{
			JSONObject automationDetail = automationsArray.getJSONObject(i);
			Automation automation = new Automation(this, automationDetail);
			automations.add(automation);
		}
		return automations;
	}
	
	/**
	 * Get information about a specific Automation workflow
	 * @param workflowId The unique id for the Automation workflow
	 * @return an Automation object
	 * @throws Exception
	 */
	public Automation getAutomation(String workflowId) throws Exception {
		JSONObject jsonAutomation = new JSONObject(do_Get(URLHelper.url(automationendpoint,"/",workflowId), getApikey()));
		return new Automation(this, jsonAutomation);
	}

	/**
	 * Create a new Automation
	 * @param recipients
	 * @param settings
	 * @return The newly added automation
	 * @throws Exception
	 */
	public Automation createAutomation(AutomationRecipient recipients, AutomationSettings settings) throws Exception {
		JSONObject json = new JSONObject();
		
		json.put("recipients", recipients.getJsonRepresentation());
		
		if (settings != null) {
			json.put("settings", settings.getJsonRepresentation());
		}
		
		// currently only supports trigger_settings:{workflow_type:'abandonedCart'}
		JSONObject jsonTrigger = new JSONObject();
		jsonTrigger.put("workflow_type", "abandonedCart");
		json.put("trigger_settings", jsonTrigger);
		
		String results = do_Post(new URL(automationendpoint),json.toString(), getApikey());
		Automation newAutomation = new Automation(this, new JSONObject(results)); // update automation object with current data
        return newAutomation;
	}
	
	/**
	 * 	Pause all emails in an Automation workflow
	 * @param workflowId The unique id for the Automation workflow
	 * @throws Exception
	 */
	public void pauseAutomationEmails(String workflowId) throws Exception {
		do_Post(URLHelper.url(getAutomationendpoint(),"/",workflowId,"/actions","/pause-all-emails"), getApikey());
	}
	
	/**
	 * Start all emails in an Automation workflow
	 * @param workflowId The unique id for the Automation workflow
	 * @throws Exception
	 */
	public void startAutomationEmails(String workflowId) throws Exception {
		do_Post(URLHelper.url(getAutomationendpoint(),"/",workflowId,"/actions","/start-all-emails"), getApikey());
	}
	
	/**
	 * Get automated email iterator in a workflow
	 * @param workflowId The unique id for the Automation workflow
	 * @return automated email iterator
	 * @throws Exception 
	 */
	public Iterable<AutomationEmail> getAutomationEmails(String workflowId) throws Exception {
		final String baseURL = URLHelper.join(automationendpoint, "/", workflowId, "/emails");
		return new ModelIterator<AutomationEmail>(AutomationEmail.class, baseURL, this);
	}
	
	/**
	 * Get a list of automated emails in a workflow with pagination
	 * @param workflowId The unique id for the Automation workflow
	 * @param count Number of emails to return. Maximum value is 1000.
	 * @param offset Zero based offset
	 * @return List containing automation emails
	 * @throws Exception
	 */
	public List<AutomationEmail> getAutomationEmails(String workflowId, int count, int offset) throws Exception {
		List<AutomationEmail> emails = new ArrayList<AutomationEmail>();
		JSONObject jsonObj = new JSONObject(do_Get(URLHelper.url(automationendpoint, "/", workflowId, "/emails", 
				"?offset=", Integer.toString(offset), "&count=", Integer.toString(count)), getApikey()));
		//int total_items = jsonAutomations.getInt("total_items"); 	// The total number of items matching the query regardless of pagination
		JSONArray emailsArray = jsonObj.getJSONArray("emails");
		for( int i = 0; i< emailsArray.length();i++)
		{
			JSONObject emailDetail = emailsArray.getJSONObject(i);
			AutomationEmail autoEmail = new AutomationEmail(this, emailDetail);
			emails.add(autoEmail);
		}
		return emails;
	}
	
	/**
	 * Get information about a specific workflow email
	 * @param workflowId The unique id for the Automation workflow
	 * @param workflowEmailId The unique id for the Automation workflow email
	 * @throws Exception
	 */
	public AutomationEmail getAutomationEmail(String workflowId, String workflowEmailId) throws Exception {
		JSONObject jsonObj = new JSONObject(do_Get(URLHelper.url(automationendpoint, "/", workflowId, "/emails","/", workflowEmailId), getApikey()));
		return new AutomationEmail(this, jsonObj);
	}
	
	/**
	 * Manually add a subscriber to a workflow, bypassing the default trigger
	 * settings. You can also use this endpoint to trigger a series of automated
	 * emails in an API 3.0 workflow type or add subscribers to an automated email
	 * queue that uses the API request delay type.
	 * 
	 * @param workflowId The unique id for the Automation workflow
	 * @param workflowEmailId The unique id for the Automation workflow email
	 * @param emailAddress The list member’s email address
	 * @throws Exception
	 */
	public void addAutomationSubscriber(String workflowId, String workflowEmailId, String emailAddress) throws Exception {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("email_address", emailAddress);
		do_Post(URLHelper.url(getAutomationendpoint(), "/", workflowId, "/emails","/", workflowEmailId, "/queue"), jsonObj.toString(), getApikey());
		// Note: MailChimp documents this as returning an AutomationSubscriber but in practice it returns nothing
	}
	/**
	 * Delete a workflow email
	 * @param workflowId The unique id for the Automation workflow
	 * @param workflowEmailId The unique id for the Automation workflow email
	 * @throws Exception
	 */
	public void deleteAutomationEmail(String workflowId, String workflowEmailId) throws Exception {
		do_Delete(URLHelper.url(automationendpoint, "/", workflowId, "/emails","/", workflowEmailId), getApikey());
	}
	
	/**
	 * Get the File/Folder Manager for accessing files and folders in your account.
	 */
	public FileManager getFileManager() {
		if (fileManager == null) {
			fileManager = new FileManager(this);
		}
		return fileManager;
	}

	/**
	 * @return the server
	 */
	public String getServer() {
		return this.server;
	}

	/**
	 * @return the apikey
	 */
	public String getApikey() {
		return this.authorization;
	}

	public String getAuthorization() {
		return this.authorization;
	}

	/**
	 * @return the apiendpoint
	 */
	public String getApiendpoint() {
		return this.apiendpoint;
	}

	/**
	 * @return the lISTENDPOINT
	 */
	public String getListendpoint() {
		return this.listendpoint;
	}

	/**
	 * @return the campaignendpoint
	 */
	public String getCampaignendpoint() {
		return this.campaignendpoint;
	}

	/**
	 * @return the templateendpoint
	 */
	public String getTemplateendpoint() {
		return this.templateendpoint;
	}

	/**
	 * @return the automationendpoint
	 */
	public String getAutomationendpoint(){
		return this.automationendpoint;
	}

	/**
	 * @return the filemanagerfolderendpoint
	 */
	public String getFilemanagerfolderendpoint() {
		return this.filemanagerfolderendpoint;
	}


	public String getFilesendpoint() {
		return filesendpoint;
	}

	public String getCampaignfolderendpoint() {
		return this.campaignfolderendpoint;
	}

	public String getTemplatefolderendpoint() {
		return this.templatefolderendpoint;
	}

	public String getReportsendpoint() {
		return reportsendpoint;
	}

	/**
	 * @return the account
	 * @throws Exception 
	 */
	public Account getAccount() throws Exception {
		cacheAccountInfo();
		return account;
	}

	/**
	 * Read the account information for this Connection
	 */
	private void cacheAccountInfo() throws Exception {
		if (account == null) {
			synchronized(this) {
				if (account == null) {
					JSONObject jsonAPIROOT = new JSONObject(do_Get(new URL(apiendpoint),getApikey()));
					this.account = new Account(this, jsonAPIROOT);
				}
			}
		}
	}

	public enum TokenType {
		BEARER,
		APIKEY
	}

	public static class Builder {
		private String apiKey;
		private String token;
		private TokenType tokenType;
		private String dc;

		public Builder usingApiKey(String apiKey) {
			this.apiKey = apiKey;
			this.tokenType = TokenType.APIKEY;
			return this;
		}

		public Builder usingOAuthToken(String oauthToken) {
			this.tokenType = TokenType.BEARER;
			this.token = oauthToken;
			return this;
		}

		public Builder withDc(String dc) {
			this.dc = dc;
			return this;
		}

		public MailChimpConnection build() {
			if (this.tokenType == null) {
				throw new NullPointerException("No token specified");
			}

			switch (tokenType) {
				case APIKEY:
					return new MailChimpConnection(apiKey.split("-")[1], "apikey", apiKey);
				case BEARER:
					if (this.dc == null) {
						throw new NullPointerException("No datacenter specified");
					}
					return new MailChimpConnection(this.dc, "Bearer", this.token);
				default:
					throw new IllegalArgumentException();
			}
		}
	}
}
