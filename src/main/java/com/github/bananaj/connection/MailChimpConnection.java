package com.github.bananaj.connection;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
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
import com.github.bananaj.model.report.EmailActivity;
import com.github.bananaj.model.report.OpenReport;
import com.github.bananaj.model.report.Report;
import com.github.bananaj.model.report.ReportSentTo;
import com.github.bananaj.model.template.Template;
import com.github.bananaj.model.template.TemplateFolder;
import com.github.bananaj.utils.DateConverter;
import com.github.bananaj.utils.ModelIterator;
import com.github.bananaj.utils.URLHelper;

/**
 * Class for the com.github.bananaj.connection to mailchimp servers. Used to get lists from mailchimp account.
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
		return new ModelIterator<MailChimpList>(MailChimpList.class, listendpoint, this);
	}

	/**
	 * Get List/Audience in your account with pagination
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @return List containing Mailchimp lists
	 * @throws TransportException
	 * @throws Exception
	 */
	public Iterable<MailChimpList> getLists(int pageSize, int pageNumber) throws TransportException, Exception {
		return new ModelIterator<MailChimpList>(MailChimpList.class, listendpoint, this, pageSize, pageNumber);
	}
	
	/**
	 * Get a specific mailchimp List/Audience
	 * @return a Mailchimp list object
	 * @throws Exception
	 */
	public MailChimpList getList(String listID) throws Exception {
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
    public Iterable<CampaignFolder> getCampaignFolders() throws Exception {
		return new ModelIterator<CampaignFolder>(CampaignFolder.class, campaignfolderendpoint, this);
    }

    /**
     * Get campaign folders from MailChimp with pagination
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
     * @return List containing the campaign folders
     */
    public Iterable<CampaignFolder> getCampaignFolders(int pageSize, int pageNumber) throws Exception {
		return new ModelIterator<CampaignFolder>(CampaignFolder.class, campaignfolderendpoint, this, pageSize, pageNumber);
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
     * @param pageSize Number of records to fetch per query. Maximum value is 1000.
     * @param pageNumber First page number to fetch starting from 0.
     * @return List containing campaigns
     * @throws Exception
     */
    public Iterable<Campaign> getCampaigns(int pageSize, int pageNumber) throws Exception {
		return new ModelIterator<Campaign>(Campaign.class, campaignendpoint, this, pageSize, pageNumber);
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
	public Campaign createCampaign(CampaignType type, MailChimpList mailChimpList, CampaignSettings settings) throws Exception {
		
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
	public void deleteCampaign(String campaignID) throws Exception {
		do_Delete(URLHelper.url(campaignendpoint,"/",campaignID),getApikey());
	}

	/**
	 * Get Post comments, reply to team feedback, and send test emails while you're working together on a Mailchimp campaign.
	 * @param campaignID
	 * @return
	 * @throws Exception
	 */
	public List<CampaignFeedback> getCampaignFeedback(String campaignID) throws Exception {
		JSONObject campaignFeedback = new JSONObject(do_Get(URLHelper.url(getCampaignendpoint(),"/",campaignID,"/feedback"),getApikey()));
		JSONArray feedbackArray = campaignFeedback.getJSONArray("feedback");
		List<CampaignFeedback> feedback = new ArrayList<CampaignFeedback>(feedbackArray.length());
		
		for( int i = 0; i< feedbackArray.length(); i++)
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
	 * @throws Exception 
	 */
	public CampaignFeedback getCampaignFeedback(String campaignID, String feedbackId) throws Exception {
		JSONObject jsonObj = new JSONObject(do_Get(URLHelper.url(getCampaignendpoint(),"/",campaignID,"/feedback/",feedbackId),getApikey()));
		CampaignFeedback feedback = new CampaignFeedback(this, jsonObj);
		return feedback;
	}
	
	/**
	 * Add campaign feedback
	 * @param campaignID
	 * @param message
	 * @throws Exception 
	 */
	CampaignFeedback createCampaignFeedback(String campaignID, String message) throws Exception {
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
	 * @throws Exception 
	 */
	CampaignSendChecklist getCampaignSendChecklist(String campaignID) throws Exception {
		String results = do_Get(URLHelper.url(getCampaignendpoint(),"/",campaignID,"/send-checklist"), getApikey());
		return new CampaignSendChecklist(new JSONObject(results));
	}
	
	/**
	 * Mailchimp's campaign and Automation reports analyze clicks, opens, subscribers' social activity, e-commerce data, and more.
	 * 
	 * @param campaignId
	 * @return Report for the specified campaign. 
	 * @throws Exception
	 */
	public Report getCampaignReport(String campaignId) throws Exception {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId);
		JSONObject jsonReport = new JSONObject(do_Get(url, getApikey()));
    	return new Report(this, jsonReport);
	}
	
	/**
	 * Mailchimp's campaign and Automation reports analyze clicks, opens, subscribers' social activity, e-commerce data, and more.
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @param campaignType Optional, restrict the response by campaign type. Possible values: regular, plaintext, absplit, rss, or variate.
	 * @param beforeSendTime Optional, restrict the response to campaigns sent before the set time.
	 * @param sinceSendTime Optional, restrict the response to campaigns sent after the set time.
	 * @return Campaign reports meeting the specified criteria.
	 * @throws Exception
	 */
	public Iterable<Report> getCampaignReports(int pageSize, int pageNumber, CampaignType campaignType, ZonedDateTime beforeSendTime, ZonedDateTime sinceSendTime) throws Exception {
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
		return new ModelIterator<Report>(Report.class, baseURL.toString(), this, pageSize, pageNumber);
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
	 * Get information about campaign abuse complaints.
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @param campaignId The unique id for the campaign.
	 * @return Abuse complaints for a campaign
	 * @throws Exception 
	 */
	public Iterable<AbuseReport>  getCampaignAbuseReports(int pageSize, int pageNumber, String campaignId) throws Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/abuse-reports");
		return new ModelIterator<AbuseReport>(AbuseReport.class, baseURL, this, pageSize, pageNumber);
	}

	/**
	 * Get information about campaign abuse complaints.
	 * @return Abuse complaints for a campaign
	 * @throws Exception 
	 */
	public Iterable<AbuseReport>  getCampaignAbuseReports(String campaignId) throws Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/abuse-reports");
		return new ModelIterator<AbuseReport>(AbuseReport.class, baseURL, this);
	}

	/**
	 * Get information about a specific abuse report for a campaign.
	 * @param campaignId The unique id for the campaign.
	 * @param reportId The id for the abuse report.
	 * @return Information about a specific abuse report
	 * @throws Exception 
	 */
	public AbuseReport  getCampaignAbuseReport(String campaignId, int reportId) throws Exception {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId, "/abuse-reports/", Integer.toString(reportId));
		JSONObject jsonReport = new JSONObject(do_Get(url, getApikey()));
		AbuseReport report = new AbuseReport(jsonReport);
		return report;
	}
	
	/**
	 * Get recent feedback based on a campaign's statistics.
	 * @param campaignId The unique id for the campaign.
	 * @return Recent feedback based on a campaign's statistics.
	 * @throws Exception
	 */
	public List<AdviceReport> getCampaignAdviceReports(String campaignId) throws Exception {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId, "/advice");
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
	 * Get detailed information about links clicked in campaigns.
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @param campaignId The unique id for the campaign.
	 * @return Campaign click details
	 * @throws Exception
	 */
	public Iterable<ClickReport> getCampaignClickReports(int pageSize, int pageNumber, String campaignId) throws Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/click-details");
		return new ModelIterator<ClickReport>(ClickReport.class, baseURL, this, pageSize, pageNumber);
	}
	
	/**
	 * Get detailed information about links clicked in campaigns.
	 * @param campaignId The unique id for the campaign.
	 * @return Campaign click details
	 * @throws Exception
	 */
	public Iterable<ClickReport> getCampaignClickReports(String campaignId) throws Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/click-details");
		return new ModelIterator<ClickReport>(ClickReport.class, baseURL, this,500);
	}
	
	/**
	 * Get detailed information about links clicked in campaigns for a specific link.
	 * @param campaignId The unique id for the campaign.
	 * @param linkId The id for the link.
	 * @return Click details for a specific link.
	 * @throws Exception
	 */
	public ClickReport getCampaignClickReport(String campaignId, String linkId) throws Exception {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId, "/click-details/", linkId);
		JSONObject jsonReport = new JSONObject(do_Get(url, getApikey()));
		ClickReport report = new ClickReport(jsonReport);
		return report;
	}
	
	/**
	 * Get information about subscribers who clicked a link.
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @param campaignId The unique id for the campaign.
	 * @param linkId The id for the link.
	 * @return Information about subscribers who clicked a link
	 * @throws Exception
	 */
	public Iterable<ClickReportMember> getCampaignMembersClickReports(int pageSize, int pageNumber, String campaignId, String linkId) throws Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/click-details/", linkId, "/members");
		return new ModelIterator<ClickReportMember>(ClickReportMember.class, baseURL, this, pageSize, pageNumber);
	}
	
	/**
	 * Get information about subscribers who clicked a link.
	 * @param campaignId The unique id for the campaign.
	 * @param linkId The id for the link.
	 * @return Information about subscribers who clicked a link
	 * @throws Exception
	 */
	public Iterable<ClickReportMember> getCampaignMembersClickReports(String campaignId, String linkId) throws Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/click-details/", linkId, "/members");
		return new ModelIterator<ClickReportMember>(ClickReportMember.class, baseURL, this,500);
	}
	
	/**
	 * Get information about a specific subscriber who clicked a link.
	 * @param campaignId The unique id for the campaign.
	 * @param linkId The id for the link.
	 * @param subscriber The member's email address or subscriber hash
	 * @return Information about a specific subscriber who clicked a link
	 * @throws Exception
	 */
	public ClickReportMember getCampaignMembersClickReport(String campaignId, String linkId, String subscriber) throws Exception {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId, "/click-details/", linkId, "/members/", Member.subscriberHash(subscriber));
		JSONObject jsonReport = new JSONObject(do_Get(url, getApikey()));
		ClickReportMember report = new ClickReportMember(jsonReport);
		return report;
	}
	
	/**
	 * Get statistics for the top-performing domains from a campaign.
	 * @param campaignId The unique id for the campaign.
	 * @return Statistics for the top-performing domains from a campaign.
	 * @throws Exception
	 */
	public DomainPerformance getDomainPerformanceReport(String campaignId) throws Exception {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId, "/domain-performance");
		JSONObject jsonReport = new JSONObject(do_Get(url, getApikey()));
		DomainPerformance report = new DomainPerformance(jsonReport);
		return report;
	}
	
	/**
	 * Ecommerce product activity report for Campaign
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @param campaignId The unique id for the campaign.
	 * @param sortField Optional, sort products by this field.
	 * @return Breakdown of product activity for a campaign.
	 * @throws Exception
	 */
	public Iterable<EcommerceProductActivity> getEcommerceProductActivityReports(int pageSize, int pageNumber, String campaignId, EcommerceSortField sortField) throws Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/ecommerce-product-activity",
				"?sort_field=", (sortField != null ? sortField.toString() : EcommerceSortField.TITLE.toString()));
		return new ModelIterator<EcommerceProductActivity>(EcommerceProductActivity.class, baseURL, this, pageSize, pageNumber);
	}
	
	/**
	 * Ecommerce product activity report for Campaign
	 * @param campaignId The unique id for the campaign.
	 * @param sortField Optional, sort products by this field.
	 * @return Breakdown of product activity for a campaign.
	 * @throws Exception
	 */
	public Iterable<EcommerceProductActivity> getEcommerceProductActivityReports(String campaignId, EcommerceSortField sortField) throws Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/ecommerce-product-activity",
				"?sort_field=", (sortField != null ? sortField.toString() : EcommerceSortField.TITLE.toString()));
		return new ModelIterator<EcommerceProductActivity>(EcommerceProductActivity.class, baseURL, this);
	}

	/**
	 * Sent To report - Get information about campaign recipients.
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @param campaignId The unique id for the campaign.
	 * @return Information about campaign recipients.
	 */
	public Iterable<ReportSentTo> getCampaignSentToReports(int pageSize, int pageNumber, String campaignId) {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/sent-to");
		return new ModelIterator<ReportSentTo>(ReportSentTo.class, baseURL, this, pageSize, pageNumber);
	}
	
	/**
	 * Sent To report - Get information about campaign recipients.
	 * @param campaignId The unique id for the campaign.
	 * @return Information about campaign recipients.
	 */
	public Iterable<ReportSentTo> getCampaignSentToReports(String campaignId) {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/sent-to");
		return new ModelIterator<ReportSentTo>(ReportSentTo.class, baseURL, this);
	}
	
	/**
	 * Sent To Recipient report - Get information about a specific campaign recipient.
	 * @param campaignId The unique id for the campaign.
	 * @return Information about a specific campaign recipients.
	 * @throws Exception 
	 */
	public ReportSentTo getCampaignSentToRecipientReport(String campaignId, String subscriber) throws Exception {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId, "/sent-to/", Member.subscriberHash(subscriber));
		JSONObject jsonRpt = new JSONObject(do_Get(url, getApikey()));
		ReportSentTo rpt = new ReportSentTo(jsonRpt);
		return rpt;
	}

	/**
	 * Email Activity report - Get list member activity for a campaign.
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @param campaignId The unique id for the campaign.
	 * @return Member activity for a campaign.
	 */
	public Iterable<EmailActivity> getCampaignEmailActivityReports(int pageSize, int pageNumber, String campaignId) {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/email-activity");
		return new ModelIterator<EmailActivity>(EmailActivity.class, baseURL, this, pageSize, pageNumber);
	}
	
	/**
	 * Email Activity report - Get list member activity for a campaign.
	 * @param campaignId The unique id for the campaign.
	 * @return Member activity for a campaign.
	 */
	public Iterable<EmailActivity> getCampaignEmailActivityReports(String campaignId) {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/email-activity");
		return new ModelIterator<EmailActivity>(EmailActivity.class, baseURL, this);
	}
	
	/**
	 * Email Activity report - Get a specific list member's activity in a campaign including opens, clicks, and bounces.
	 * @param campaignId The unique id for the campaign.
	 * @return Member activity for a campaign.
	 * @throws Exception 
	 */
	public EmailActivity getCampaignEmailActivityReport(String campaignId, String subscriber) throws Exception {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId, "/email-activity/", Member.subscriberHash(subscriber));
		JSONObject jsonRpt = new JSONObject(do_Get(url, getApikey()));
		EmailActivity rpt = new EmailActivity(jsonRpt);
		return rpt;
	}
	
	// TODO: Report - Location - Get top open locations for a specific campaign.
	// TODO: Report - Sub-Reports- A list of reports for child campaigns of a specific parent campaign. For example, use this endpoint to view Multivariate, RSS, and A/B Testing Campaign reports.
	// TODO: Report - Unsubscribes - Get information about list members who unsubscribed from a specific campaign.
	// TODO: Report - EepURL Reports - Get a summary of social activity for the campaign, tracked by EepURL.
	
    /**
     * Get template folders iterator
     * @return template folder iterator
     */
	public Iterable<TemplateFolder> getTemplateFolders() throws Exception {
		return new ModelIterator<TemplateFolder>(TemplateFolder.class, templatefolderendpoint, this);
	}

    /**
     * Get template folders from MailChimp with pagination
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
     * @return List of template folders
     */
	public Iterable<TemplateFolder> getTemplateFolders(int pageSize, int pageNumber) throws Exception {
		return new ModelIterator<TemplateFolder>(TemplateFolder.class, templatefolderendpoint, this, pageSize, pageNumber);
	}

    /**
     * Get a specific template folder
     * @param folder_id
     */
    public TemplateFolder getTemplateFolder(String folder_id) throws Exception {

        JSONObject jsonTemplateFolder = new JSONObject(do_Get(URLHelper.url(templatefolderendpoint,"/",folder_id), getApikey()));
        return new TemplateFolder(this, jsonTemplateFolder);
    }

    /**
     * Add a template folder with a specific name
     * @param name
     */
    public TemplateFolder createTemplateFolder(String name) throws Exception {
        JSONObject templateFolder = new JSONObject();
        templateFolder.put("name", name);
        JSONObject jsonTemplateFolder = new JSONObject(do_Post(new URL(templatefolderendpoint), templateFolder.toString(), getApikey()));
        return new TemplateFolder(this, jsonTemplateFolder);
    }

    /**
     * Delete a specific template folder
     * @param folder_id
     */
    public void deleteTemplateFolder(String folder_id) throws Exception {
        do_Delete(URLHelper.url(templatefolderendpoint,"/",folder_id), getApikey());
    }

	/**
	 * Get templates iterator from mailchimp account
	 * @return templates iterator
	 * @throws Exception
	 */
	public Iterable<Template> getTemplates() throws Exception {
		return new ModelIterator<Template>(Template.class, templateendpoint, this, 500);
	}

	/**
	 * Get templates from mailchimp account with pagination
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @return list of templates
	 * @throws Exception
	 */
	public Iterable<Template> getTemplates(int pageSize, int pageNumber) throws Exception {
		return new ModelIterator<Template>(Template.class, templateendpoint, this, pageSize, pageNumber);
	}

	/**
	 * Get a template from mailchimp account
	 * @param id
	 * @return a template object
	 * @throws Exception
	 */
	public Template getTemplate(String id) throws Exception {
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
	 * Get a summary of an account's classic automations.
	 * Mailchimp's classic automations feature lets you build a series of emails that 
	 * send to subscribers when triggered by a specific date, activity, or event. Use 
	 * the API to manage Automation workflows, emails, and queues. Does not include 
	 * Customer Journeys.
	 * 
	 * @return automations iterator
	 * @throws Exception
	 */
	public Iterable<Automation> getAutomations() throws Exception {
		return new ModelIterator<Automation>(Automation.class, automationendpoint, this);
	}
	
	/**
	 * Get a summary of an account's classic automations with pagination
	 * Mailchimp's classic automations feature lets you build a series of emails that 
	 * send to subscribers when triggered by a specific date, activity, or event. Use 
	 * the API to manage Automation workflows, emails, and queues. Does not include 
	 * Customer Journeys.
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @return automations iterator starting at the given page number
	 * @throws Exception
	 */
	public Iterable<Automation> getAutomations(int pageSize, int pageNumber) throws Exception {
		return new ModelIterator<Automation>(Automation.class, automationendpoint, this, pageSize, pageNumber);
	}
	
	/**
	 * Get information about a specific Automation workflow.
	 * Mailchimp's classic automations feature lets you build a series of emails that 
	 * send to subscribers when triggered by a specific date, activity, or event. Use 
	 * the API to manage Automation workflows, emails, and queues. Does not include 
	 * Customer Journeys.
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
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @param workflowId The unique id for the Automation workflow
	 * @return List containing automation emails
	 * @throws Exception
	 */
	public Iterable<AutomationEmail> getAutomationEmails(int pageSize, int pageNumber, String workflowId) throws Exception {
		final String baseURL = URLHelper.join(automationendpoint, "/", workflowId, "/emails");
		return new ModelIterator<AutomationEmail>(AutomationEmail.class, baseURL, this, pageSize, pageNumber);
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
