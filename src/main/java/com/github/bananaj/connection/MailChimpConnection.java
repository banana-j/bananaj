package com.github.bananaj.connection;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

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
import com.github.bananaj.model.report.OpenReportMember;
import com.github.bananaj.model.report.Report;
import com.github.bananaj.model.report.ReportLocation;
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
	 * @return List/audience iterator
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<MailChimpList> getLists() throws IOException, Exception {
		return new ModelIterator<MailChimpList>(MailChimpList.class, listendpoint, this);
	}

	/**
	 * Get List/Audience in your account with pagination
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @return List containing Mailchimp lists
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<MailChimpList> getLists(int pageSize, int pageNumber) throws IOException, Exception {
		return new ModelIterator<MailChimpList>(MailChimpList.class, listendpoint, this, pageSize, pageNumber);
	}
	
	/**
	 * Get a specific mailchimp List/Audience
	 * @return a Mailchimp list object
	 * @throws IOException
	 * @throws Exception 
	 */
	public MailChimpList getList(String listID) throws IOException, Exception {
		JSONObject jsonList = new JSONObject(do_Get(URLHelper.url(listendpoint,"/",listID),getApikey()));
		return new MailChimpList(this, jsonList);
	}

	/**
	 * Create a new List/Audience in your mailchimp account
	 * @param audience
	 * @throws IOException
	 * @throws Exception 
	 */
	public MailChimpList createList(MailChimpList audience) throws IOException, Exception {
		cacheAccountInfo();
		JSONObject jsonList = audience.getJSONRepresentation();
		JSONObject jsonNewList = new JSONObject(do_Post(new URL(listendpoint), jsonList.toString(),getApikey()));
		return new MailChimpList(this, jsonNewList);
	}

	/**
	 * Delete a List/Audience from your account
	 * @param listID
	 * @throws IOException
	 * @throws Exception 
	 */
	public void deleteList(String listID) throws IOException, Exception {
		do_Delete(URLHelper.url(listendpoint,"/",listID), getApikey());
	}

	/**
	 * A health check for the API.
	 * @return true on successful API ping otherwise false
	 * @throws Exception 
	 * @throws IOException
	 */
	public boolean ping() throws IOException, Exception {
		try {
			JSONObject jsonObj = new JSONObject(do_Get(URLHelper.url(apiendpoint, "ping"), getApikey()));
			if (jsonObj.has("health_status") && "Everything's Chimpy!".equals(jsonObj.getString("health_status"))) {
				return true;
			}
		} catch (IOException ignored) { }
		return false;
	}
	
    /**
     * Get campaign folders iterator from MailChimp
     * @return campaign folder iterator
	 * @throws IOException
	 * @throws Exception 
     */
    public Iterable<CampaignFolder> getCampaignFolders() throws IOException, Exception {
		return new ModelIterator<CampaignFolder>(CampaignFolder.class, campaignfolderendpoint, this);
    }

    /**
     * Get campaign folders from MailChimp with pagination
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
     * @return List containing the campaign folders
	 * @throws IOException
	 * @throws Exception 
     */
    public Iterable<CampaignFolder> getCampaignFolders(int pageSize, int pageNumber) throws IOException, Exception {
		return new ModelIterator<CampaignFolder>(CampaignFolder.class, campaignfolderendpoint, this, pageSize, pageNumber);
    }

    /**
     * Get a specific template folder
     * @param folder_id
	 * @throws IOException
     * @throws Exception 
     */
    public CampaignFolder getCampaignFolder(String folder_id) throws IOException, Exception {

    	JSONObject jsonCampaignFolder = new JSONObject(do_Get(URLHelper.url(campaignfolderendpoint,"/",folder_id), getApikey()));
    	return new CampaignFolder(this, jsonCampaignFolder);
    }

    /**
     * Add a template folder with a specific name
     * @param name Name to associate with the folder
	 * @throws IOException
     * @throws Exception 
     */
    public CampaignFolder addCampaignFolder(String name) throws IOException, Exception {
    	JSONObject campaignFolder = new JSONObject();
    	campaignFolder.put("name", name);
    	JSONObject jsonCampaignFolder = new JSONObject(do_Post(new URL(campaignfolderendpoint), campaignFolder.toString(), getApikey()));
    	return new CampaignFolder(this, jsonCampaignFolder);
    }

    /**
     * Delete a specific template folder
     * @param folder_id
	 * @throws IOException
     * @throws Exception 
     */
    public void deleteCampaignFolder(String folder_id) throws IOException, Exception {
    	do_Delete(URLHelper.url(campaignfolderendpoint,"/",folder_id), getApikey());
    }

    /**
     * Get campaigns iterator from mailchimp account
	 * 
     * @return Campaign iterator
	 * @throws IOException
	 * @throws Exception 
     */
    public Iterable<Campaign> getCampaigns() throws IOException, Exception {
		return new ModelIterator<Campaign>(Campaign.class, campaignendpoint, this, 500);
    }

    /**
     * Get campaigns from mailchimp account with pagination
     * @param pageSize Number of records to fetch per query. Maximum value is 1000.
     * @param pageNumber First page number to fetch starting from 0.
     * @return List containing campaigns
	 * @throws IOException
	 * @throws Exception 
     */
    public Iterable<Campaign> getCampaigns(int pageSize, int pageNumber) throws IOException, Exception {
		return new ModelIterator<Campaign>(Campaign.class, campaignendpoint, this, pageSize, pageNumber);
    }

	/**
	 * Get a campaign from mailchimp account
	 * @param campaignID
	 * @return a campaign object
	 * @throws IOException
	 * @throws Exception 
	 */
	public Campaign getCampaign(String campaignID) throws IOException, Exception {
		JSONObject campaign = new JSONObject(do_Get(URLHelper.url(campaignendpoint,"/",campaignID),getApikey()));
		return new Campaign(this, campaign);
	}

	/**
	 * Create a new campaign in your mailchimp account
	 * @param type
	 * @param mailChimpList
	 * @param settings
	 * @throws IOException
	 * @throws Exception 
	 */
	public Campaign createCampaign(CampaignType type, MailChimpList mailChimpList, CampaignSettings settings) throws IOException, Exception {
		
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

	/**
	 * Create a new campaign in your mailchimp account
	 * @param type
	 * @param mailRecipients
	 * @param settings
	 * @return
	 * @throws IOException
	 * @throws Exception 
	 */
	public Campaign createCampaign(CampaignType type, CampaignRecipients mailRecipients, CampaignSettings settings) throws IOException, Exception {
		
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
	 * @throws IOException
	 * @throws Exception 
	 */
	public void deleteCampaign(String campaignID) throws IOException, Exception {
		do_Delete(URLHelper.url(campaignendpoint,"/",campaignID),getApikey());
	}

	/**
	 * Get Post comments, reply to team feedback, and send test emails while you're working together on a Mailchimp campaign.
	 * @param campaignID
	 * @return
	 * @throws IOException
	 * @throws Exception 
	 */
	public List<CampaignFeedback> getCampaignFeedback(String campaignID) throws IOException, Exception {
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
	 * @throws IOException 
	 * @throws Exception 
	 */
	public CampaignFeedback getCampaignFeedback(String campaignID, String feedbackId) throws IOException, Exception {
		JSONObject jsonObj = new JSONObject(do_Get(URLHelper.url(getCampaignendpoint(),"/",campaignID,"/feedback/",feedbackId),getApikey()));
		CampaignFeedback feedback = new CampaignFeedback(this, jsonObj);
		return feedback;
	}
	
	/**
	 * Add campaign feedback
	 * @param campaignID
	 * @param message
	 * @throws IOException 
	 * @throws Exception 
	 */
	CampaignFeedback createCampaignFeedback(String campaignID, String message) throws IOException, Exception {
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
	 * @throws IOException 
	 * @throws Exception 
	 */
	CampaignSendChecklist getCampaignSendChecklist(String campaignID) throws IOException, Exception {
		String results = do_Get(URLHelper.url(getCampaignendpoint(),"/",campaignID,"/send-checklist"), getApikey());
		return new CampaignSendChecklist(new JSONObject(results));
	}
	
	/**
	 * Mailchimp's campaign and Automation reports analyze clicks, opens, subscribers' social activity, e-commerce data, and more.
	 * 
	 * @param campaignId
	 * @return Report for the specified campaign. 
	 * @throws IOException
	 * @throws Exception 
	 */
	public Report getCampaignReport(String campaignId) throws IOException, Exception {
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
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<Report> getCampaignReports(int pageSize, int pageNumber, CampaignType campaignType, ZonedDateTime beforeSendTime, ZonedDateTime sinceSendTime) throws IOException, Exception {
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
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<Report> getCampaignReports(CampaignType campaignType, ZonedDateTime beforeSendTime, ZonedDateTime sinceSendTime) throws IOException, Exception {
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
	 * @throws IOException
	 * @throws Exception 
	 */
	public OpenReport getCampaignOpenReports(String campaignId, ZonedDateTime since) throws IOException, Exception {
		return OpenReport.getOpenReport(this, campaignId, since);
	}
	

	/**
	 * Get information about a specific subscriber who opened a campaign.
	 * @param campaignId
	 * @param subscriber The member's email address or subscriber hash
	 * @return Detailed information about the campaigns emails that were opened by list members.
	 * @throws IOException
	 * @throws Exception 
	 */
	public OpenReportMember getCampaignOpenReport(String campaignId, String subscriber) throws IOException, Exception {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId, "/open-details","/", Member.subscriberHash(subscriber));
		JSONObject jsonReport = new JSONObject(do_Get(url, getApikey()));
		return new OpenReportMember(jsonReport);
	}
	

	/**
	 * Get information about campaign abuse complaints.
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @param campaignId The unique id for the campaign.
	 * @return Abuse complaints for a campaign
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<AbuseReport>  getCampaignAbuseReports(int pageSize, int pageNumber, String campaignId) throws IOException, Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/abuse-reports");
		return new ModelIterator<AbuseReport>(AbuseReport.class, baseURL, this, pageSize, pageNumber);
	}

	/**
	 * Get information about campaign abuse complaints.
	 * @return Abuse complaints for a campaign
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<AbuseReport>  getCampaignAbuseReports(String campaignId) throws IOException, Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/abuse-reports");
		return new ModelIterator<AbuseReport>(AbuseReport.class, baseURL, this);
	}

	/**
	 * Get information about a specific abuse report for a campaign.
	 * @param campaignId The unique id for the campaign.
	 * @param reportId The id for the abuse report.
	 * @return Information about a specific abuse report
	 * @throws IOException 
	 * @throws Exception 
	 */
	public AbuseReport  getCampaignAbuseReport(String campaignId, int reportId) throws IOException, Exception {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId, "/abuse-reports/", Integer.toString(reportId));
		JSONObject jsonReport = new JSONObject(do_Get(url, getApikey()));
		AbuseReport report = new AbuseReport(jsonReport);
		return report;
	}
	
	/**
	 * Get recent feedback based on a campaign's statistics.
	 * @param campaignId The unique id for the campaign.
	 * @return Recent feedback based on a campaign's statistics.
	 * @throws IOException
	 * @throws Exception 
	 */
	public List<AdviceReport> getCampaignAdviceReports(String campaignId) throws IOException, Exception {
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
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<ClickReport> getCampaignClickReports(int pageSize, int pageNumber, String campaignId) throws IOException, Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/click-details");
		return new ModelIterator<ClickReport>(ClickReport.class, baseURL, this, pageSize, pageNumber);
	}
	
	/**
	 * Get detailed information about links clicked in campaigns.
	 * @param campaignId The unique id for the campaign.
	 * @return Campaign click details
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<ClickReport> getCampaignClickReports(String campaignId) throws IOException, Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/click-details");
		return new ModelIterator<ClickReport>(ClickReport.class, baseURL, this,500);
	}
	
	/**
	 * Get detailed information about links clicked in campaigns for a specific link.
	 * @param campaignId The unique id for the campaign.
	 * @param linkId The id for the link.
	 * @return Click details for a specific link.
	 * @throws IOException
	 * @throws Exception 
	 */
	public ClickReport getCampaignClickReport(String campaignId, String linkId) throws IOException, Exception {
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
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<ClickReportMember> getCampaignMembersClickReports(int pageSize, int pageNumber, String campaignId, String linkId) throws IOException, Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/click-details/", linkId, "/members");
		return new ModelIterator<ClickReportMember>(ClickReportMember.class, baseURL, this, pageSize, pageNumber);
	}
	
	/**
	 * Get information about subscribers who clicked a link.
	 * @param campaignId The unique id for the campaign.
	 * @param linkId The id for the link.
	 * @return Information about subscribers who clicked a link
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<ClickReportMember> getCampaignMembersClickReports(String campaignId, String linkId) throws IOException, Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/click-details/", linkId, "/members");
		return new ModelIterator<ClickReportMember>(ClickReportMember.class, baseURL, this,500);
	}
	
	/**
	 * Get information about a specific subscriber who clicked a link.
	 * @param campaignId The unique id for the campaign.
	 * @param linkId The id for the link.
	 * @param subscriber The member's email address or subscriber hash
	 * @return Information about a specific subscriber who clicked a link
	 * @throws IOException
	 * @throws Exception 
	 */
	public ClickReportMember getCampaignMembersClickReport(String campaignId, String linkId, String subscriber) throws IOException, Exception {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId, "/click-details/", linkId, "/members/", Member.subscriberHash(subscriber));
		JSONObject jsonReport = new JSONObject(do_Get(url, getApikey()));
		ClickReportMember report = new ClickReportMember(jsonReport);
		return report;
	}
	
	/**
	 * Get statistics for the top-performing domains from a campaign.
	 * @param campaignId The unique id for the campaign.
	 * @return Statistics for the top-performing domains from a campaign.
	 * @throws IOException
	 * @throws Exception 
	 */
	public DomainPerformance getDomainPerformanceReport(String campaignId) throws IOException, Exception {
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
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<EcommerceProductActivity> getEcommerceProductActivityReports(int pageSize, int pageNumber, String campaignId, EcommerceSortField sortField) throws IOException, Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/ecommerce-product-activity",
				"?sort_field=", (sortField != null ? sortField.toString() : EcommerceSortField.TITLE.toString()));
		return new ModelIterator<EcommerceProductActivity>(EcommerceProductActivity.class, baseURL, this, pageSize, pageNumber);
	}
	
	/**
	 * Ecommerce product activity report for Campaign
	 * @param campaignId The unique id for the campaign.
	 * @param sortField Optional, sort products by this field.
	 * @return Breakdown of product activity for a campaign.
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<EcommerceProductActivity> getEcommerceProductActivityReports(String campaignId, EcommerceSortField sortField) throws IOException, Exception {
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
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<ReportSentTo> getCampaignSentToReports(int pageSize, int pageNumber, String campaignId) throws IOException, Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/sent-to");
		return new ModelIterator<ReportSentTo>(ReportSentTo.class, baseURL, this, pageSize, pageNumber);
	}
	
	/**
	 * Sent To report - Get information about campaign recipients.
	 * @param campaignId The unique id for the campaign.
	 * @return Information about campaign recipients.
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<ReportSentTo> getCampaignSentToReports(String campaignId) throws IOException, Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/sent-to");
		return new ModelIterator<ReportSentTo>(ReportSentTo.class, baseURL, this);
	}
	
	/**
	 * Sent To Recipient report - Get information about a specific campaign recipient.
	 * @param campaignId The unique id for the campaign.
	 * @return Information about a specific campaign recipients.
	 * @throws IOException 
	 * @throws Exception 
	 */
	public ReportSentTo getCampaignSentToReport(String campaignId, String subscriber) throws IOException, Exception {
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
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<EmailActivity> getCampaignEmailActivityReports(int pageSize, int pageNumber, String campaignId) throws IOException, Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/email-activity");
		return new ModelIterator<EmailActivity>(EmailActivity.class, baseURL, this, pageSize, pageNumber);
	}
	
	/**
	 * Email Activity report - Get list member activity for a campaign.
	 * @param campaignId The unique id for the campaign.
	 * @return Member activity for a campaign.
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<EmailActivity> getCampaignEmailActivityReports(String campaignId) throws IOException, Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/email-activity");
		return new ModelIterator<EmailActivity>(EmailActivity.class, baseURL, this);
	}
	
	/**
	 * Email Activity report - Get a specific list member's activity in a campaign including opens, clicks, and bounces.
	 * @param campaignId The unique id for the campaign.
	 * @return Member activity for a campaign.
	 * @throws IOException 
	 * @throws Exception 
	 */
	public EmailActivity getCampaignEmailActivityReport(String campaignId, String subscriber) throws IOException, Exception {
		URL url = URLHelper.url(getReportsendpoint(), "/", campaignId, "/email-activity/", Member.subscriberHash(subscriber));
		JSONObject jsonRpt = new JSONObject(do_Get(url, getApikey()));
		EmailActivity rpt = new EmailActivity(jsonRpt);
		return rpt;
	}
	
	/**
	 * Top open locations for a specific campaign.
	 * @param campaignId The unique id for the campaign.
	 * @throws IOException 
	 * @throws Exception 
	 */
	public Iterable<ReportLocation> getCampaignLocationsReports(String campaignId) throws IOException, Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/locations");
		return new ModelIterator<ReportLocation>(ReportLocation.class, baseURL, this);
	}
	
	// TODO: Report - Sub-Reports- A list of reports for child campaigns of a specific parent campaign. For example, use this endpoint to view Multivariate, RSS, and A/B Testing Campaign reports.
	// TODO: Report - Unsubscribes - Get information about list members who unsubscribed from a specific campaign.
	// TODO: Report - EepURL Reports - Get a summary of social activity for the campaign, tracked by EepURL.
	
	// TODO: Search Campaigns - GET /search-campaigns - Search all campaigns for the specified query terms.
	
    /**
     * Get template folders iterator
     * @return template folder iterator
	 * @throws IOException
	 * @throws Exception 
     */
	public Iterable<TemplateFolder> getTemplateFolders() throws IOException, Exception {
		return new ModelIterator<TemplateFolder>(TemplateFolder.class, templatefolderendpoint, this);
	}

    /**
     * Get template folders from MailChimp with pagination
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
     * @return List of template folders
	 * @throws IOException
	 * @throws Exception 
     */
	public Iterable<TemplateFolder> getTemplateFolders(int pageSize, int pageNumber) throws IOException, Exception {
		return new ModelIterator<TemplateFolder>(TemplateFolder.class, templatefolderendpoint, this, pageSize, pageNumber);
	}

    /**
     * Get a specific template folder
     * @param folder_id
	 * @throws IOException
     * @throws Exception 
     */
    public TemplateFolder getTemplateFolder(String folder_id) throws IOException, Exception {

        JSONObject jsonTemplateFolder = new JSONObject(do_Get(URLHelper.url(templatefolderendpoint,"/",folder_id), getApikey()));
        return new TemplateFolder(this, jsonTemplateFolder);
    }

    /**
     * Add a template folder with a specific name
     * @param name
	 * @throws IOException
     * @throws Exception 
     */
    public TemplateFolder createTemplateFolder(String name) throws IOException, Exception {
        JSONObject templateFolder = new JSONObject();
        templateFolder.put("name", name);
        JSONObject jsonTemplateFolder = new JSONObject(do_Post(new URL(templatefolderendpoint), templateFolder.toString(), getApikey()));
        return new TemplateFolder(this, jsonTemplateFolder);
    }

    /**
     * Delete a specific template folder
     * @param folder_id
	 * @throws IOException
     * @throws Exception 
     */
    public void deleteTemplateFolder(String folder_id) throws IOException, Exception {
        do_Delete(URLHelper.url(templatefolderendpoint,"/",folder_id), getApikey());
    }

	/**
	 * Get templates iterator from mailchimp account
	 * @return templates iterator
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<Template> getTemplates() throws IOException, Exception {
		return new ModelIterator<Template>(Template.class, templateendpoint, this, 500);
	}

	/**
	 * Get templates from mailchimp account with pagination
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @return list of templates
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<Template> getTemplates(int pageSize, int pageNumber) throws IOException, Exception {
		return new ModelIterator<Template>(Template.class, templateendpoint, this, pageSize, pageNumber);
	}

	/**
	 * Get a template from mailchimp account
	 * @param id
	 * @return a template object
	 * @throws IOException
	 * @throws Exception 
	 */
	public Template getTemplate(String id) throws IOException, Exception {
		JSONObject jsonTemplate = new JSONObject(do_Get(URLHelper.url(templateendpoint,"/",id),getApikey()));
		return new Template(this, jsonTemplate);
	}

	/**
	 * Add a template to your MailChimp account. Only Classic templates are
	 * supported. The Mailchimp Template Language is supported in any HTML code.
	 * 
	 * @param name The name of the template
	 * @param html The raw HTML for the template
	 * @throws IOException
	 * @throws Exception 
	 */
	public Template createTemplate(String name, String html) throws IOException, Exception {
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
	 * @throws IOException 
	 * @throws Exception 
	 */
	public Template createTemplate(String name, String folder_id, String html) throws IOException, Exception {
		return createTemplate(new Template.Builder()
				.withName(name)
				.inFolder(folder_id)
				.withHTML(html)
				.build());
	}

	/**
	 * Create a new template. Only Classic templates are supported.
	 * @param template
	 * @throws IOException 
	 * @throws Exception 
	 */
	public Template createTemplate(Template template) throws IOException, Exception {
		JSONObject jsonObj = template.getJsonRepresentation();
		String results = do_Post(URLHelper.url(templateendpoint,"/"), jsonObj.toString(),getApikey());
		template.parse(this, new JSONObject(results));
		return template;
	}
	
	/**
	 * Delete a specific template
	 * @param id
	 * @throws IOException
	 * @throws Exception 
	 */
	public void deleteTemplate(String id) throws IOException, Exception {
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
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<Automation> getAutomations() throws IOException, Exception {
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
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<Automation> getAutomations(int pageSize, int pageNumber) throws IOException, Exception {
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
	 * @throws IOException
	 * @throws Exception 
	 */
	public Automation getAutomation(String workflowId) throws IOException, Exception {
		JSONObject jsonAutomation = new JSONObject(do_Get(URLHelper.url(automationendpoint,"/",workflowId), getApikey()));
		return new Automation(this, jsonAutomation);
	}

	/**
	 * Create a new Automation
	 * @param recipients
	 * @param settings
	 * @return The newly added automation
	 * @throws IOException
	 * @throws Exception 
	 */
	public Automation createAutomation(AutomationRecipient recipients, AutomationSettings settings) throws IOException, Exception {
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
	 * @throws IOException
	 * @throws Exception 
	 */
	public void pauseAutomationEmails(String workflowId) throws IOException, Exception {
		do_Post(URLHelper.url(getAutomationendpoint(),"/",workflowId,"/actions","/pause-all-emails"), getApikey());
	}
	
	/**
	 * Start all emails in an Automation workflow
	 * @param workflowId The unique id for the Automation workflow
	 * @throws IOException
	 * @throws Exception 
	 */
	public void startAutomationEmails(String workflowId) throws IOException, Exception {
		do_Post(URLHelper.url(getAutomationendpoint(),"/",workflowId,"/actions","/start-all-emails"), getApikey());
	}
	
	/**
	 * Get automated email iterator in a workflow
	 * @param workflowId The unique id for the Automation workflow
	 * @return automated email iterator
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<AutomationEmail> getAutomationEmails(String workflowId) throws IOException, Exception {
		final String baseURL = URLHelper.join(automationendpoint, "/", workflowId, "/emails");
		return new ModelIterator<AutomationEmail>(AutomationEmail.class, baseURL, this);
	}
	
	/**
	 * Get a list of automated emails in a workflow with pagination
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @param workflowId The unique id for the Automation workflow
	 * @return List containing automation emails
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<AutomationEmail> getAutomationEmails(int pageSize, int pageNumber, String workflowId) throws IOException, Exception {
		final String baseURL = URLHelper.join(automationendpoint, "/", workflowId, "/emails");
		return new ModelIterator<AutomationEmail>(AutomationEmail.class, baseURL, this, pageSize, pageNumber);
	}
	
	/**
	 * Get information about a specific workflow email
	 * @param workflowId The unique id for the Automation workflow
	 * @param workflowEmailId The unique id for the Automation workflow email
	 * @throws IOException
	 * @throws Exception 
	 */
	public AutomationEmail getAutomationEmail(String workflowId, String workflowEmailId) throws IOException, Exception {
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
	 * @throws IOException
	 * @throws Exception 
	 */
	public void addAutomationSubscriber(String workflowId, String workflowEmailId, String emailAddress) throws IOException, Exception {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("email_address", emailAddress);
		do_Post(URLHelper.url(getAutomationendpoint(), "/", workflowId, "/emails","/", workflowEmailId, "/queue"), jsonObj.toString(), getApikey());
		// Note: MailChimp documents this as returning an AutomationSubscriber but in practice it returns nothing
	}
	/**
	 * Delete a workflow email
	 * @param workflowId The unique id for the Automation workflow
	 * @param workflowEmailId The unique id for the Automation workflow email
	 * @throws IOException
	 * @throws Exception 
	 */
	public void deleteAutomationEmail(String workflowId, String workflowEmailId) throws IOException, Exception {
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
	 * @throws IOException 
	 * @throws Exception 
	 */
	public Account getAccount() throws IOException, Exception {
		cacheAccountInfo();
		return account;
	}

	/**
	 * Read the account information for this Connection
	 * @throws IOException 
	 * @throws Exception 
	 */
	private void cacheAccountInfo() throws IOException, Exception {
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
