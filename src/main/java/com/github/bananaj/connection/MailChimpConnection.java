package com.github.bananaj.connection;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.bananaj.model.ModelIterator;
import com.github.bananaj.model.automation.Automation;
import com.github.bananaj.model.automation.AutomationRecipient;
import com.github.bananaj.model.automation.AutomationSettings;
import com.github.bananaj.model.automation.emails.AutomationEmail;
import com.github.bananaj.model.batch.BatchInfo;
import com.github.bananaj.model.batch.BatchOperation;
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
import com.github.bananaj.model.report.EmailActivity;
import com.github.bananaj.model.report.OpenReport;
import com.github.bananaj.model.report.OpenReportMember;
import com.github.bananaj.model.report.Report;
import com.github.bananaj.model.report.ReportLocation;
import com.github.bananaj.model.report.ReportSentTo;
import com.github.bananaj.model.template.Template;
import com.github.bananaj.model.template.TemplateFolder;
import com.github.bananaj.utils.URLHelper;

/**
 * Primary class in the bananaj library that provides access to the Mailchimp
 * Marketing API. Used to establish a connection with the Mailchimp Marketing
 * API and provides methods to call most of the exposed Mailchimp API.
 * 
 * 
 * @see <a href="https://mailchimp.com/developer/marketing/api/" target="MailchimpAPIDoc">MAILCHIMP MARKETING API</a>
 *
 */
public class MailChimpConnection extends Connection {

	private String server;
	private String authorization;
	private final String apiendpoint;
	private final String batchendpoint;
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
		this.batchendpoint = "https://"+server+".api.mailchimp.com/3.0/batches";
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
	 * Get information about all lists/audiences in the account.
	 * 
	 * @return Iterable<MailChimpList> List/audience iterator
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<MailChimpList> getLists() throws IOException, Exception {
		return new ModelIterator<MailChimpList>(MailChimpList.class, listendpoint, this);
	}

	/**
	 * Get information about all lists/audiences in the account.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/lists/get-lists-info/" target="MailchimpAPIDoc">Lists/Audiences -- GET /lists</a>
	 * @return Iterable<MailChimpList> List/audience iterator
	 * @throws IOException
	 * @throws Exception
	 */
	public Iterable<MailChimpList> getLists(final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return new ModelIterator<MailChimpList>(MailChimpList.class, listendpoint, this, queryParameters);
	}

	/**
	 * Get information about a specific list in your Mailchimp account. Results include list 
	 * members who have signed up but haven't confirmed their subscription yet and unsubscribed 
	 * or cleaned.
	 * 
	 * @param listID
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/lists/get-list-info/" target="MailchimpAPIDoc">Lists/Audiences -- GET /lists/{list_id}</a>
	 * @return a Mailchimp list/audience object
	 * @throws IOException
	 * @throws Exception
	 */
	public MailChimpList getList(String listID, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		MailChimpQueryParameters query = queryParameters != null ? (MailChimpQueryParameters) queryParameters.clone() : new MailChimpQueryParameters();
		query.baseUrl(URLHelper.join(listendpoint,"/",listID));
		JSONObject jsonList = new JSONObject(do_Get(query.getURL(),getApikey()));
		return new MailChimpList(this, jsonList);
	}

	/**
	 * Create a new List/Audience in your mailchimp account
	 * @param audience
	 * @return a Mailchimp list/audience object
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
     * Get campaign folders
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/campaign-folders/list-campaign-folders/" target="MailchimpAPIDoc">Campaign Folders -- GET /campaign-folders</a>
     * @return campaign folder iterator
     * @throws IOException
     * @throws Exception
     */
    public Iterable<CampaignFolder> getCampaignFolders(final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return new ModelIterator<CampaignFolder>(CampaignFolder.class, campaignfolderendpoint, this, queryParameters);
    }

    /**
     * Get a specific template folder
     * @param folder_id The unique id for the campaign folder.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/campaign-folders/get-campaign-folder/" target="MailchimpAPIDoc">Campaign Folders -- GET /campaign-folders/{folder_id}</a>
     * @return campaign folder object
	 * @throws IOException
     * @throws Exception 
     */
    public CampaignFolder getCampaignFolder(String folder_id, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		MailChimpQueryParameters query = queryParameters != null ? (MailChimpQueryParameters) queryParameters.clone() : new MailChimpQueryParameters();
		query.baseUrl(URLHelper.join(campaignfolderendpoint,"/",folder_id));
    	JSONObject jsonCampaignFolder = new JSONObject(do_Get(query.getURL(), getApikey()));
    	return new CampaignFolder(this, jsonCampaignFolder);
    }

    /**
     * Add a template folder with a specific name
     * @param name Name to associate with the folder
     * @return campaign folder object
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
     * Get all campaigns in an account.<br>
     * Campaigns are how you send emails to your Mailchimp list. Use the Campaigns API calls 
     * to manage campaigns in your Mailchimp account.
     * @param queryParameters Optional query parameters to send to the MailChimp API. 
     *   @see <a href="https://mailchimp.com/developer/marketing/api/campaigns/list-campaigns/" target="MailchimpAPIDoc">Campaigns -- GET /campaigns</a>
     * @return campaign iterator
     * @throws IOException
     * @throws Exception
     */
    public Iterable<Campaign> getCampaigns(final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return new ModelIterator<Campaign>(Campaign.class, campaignendpoint, this, queryParameters);
    }
    
    /**
     * Get information about a specific campaign.
     * @param campaignID The unique id for the campaign.
     * @param queryParameters Optional query parameters to send to the MailChimp API. 
     *   @see <a href="https://mailchimp.com/developer/marketing/api/campaigns/get-campaign-info/" target="MailchimpAPIDoc">Campaigns -- GET /campaigns/{campaign_id}</a>
     * @return a campaign object
     * @throws IOException
     * @throws Exception 
     */
    public Campaign getCampaign(String campaignID, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		MailChimpQueryParameters query = queryParameters != null ? (MailChimpQueryParameters) queryParameters.clone() : new MailChimpQueryParameters();
		query.baseUrl(URLHelper.join(campaignendpoint,"/",campaignID));
    	JSONObject campaign = new JSONObject(do_Get(query.getURL(),getApikey()));
    	return new Campaign(this, campaign);
    }

	/**
	 * Create a new campaign in your mailchimp account
	 * @param type
	 * @param mailChimpList
	 * @param settings
     * @return newly created campaign object
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
     * @return newly created campaign object
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
	 * Remove a campaign from your Mailchimp account.
	 * @param campaignID The unique id for the campaign.
	 * @throws IOException
	 * @throws Exception 
	 */
	public void deleteCampaign(String campaignID) throws IOException, Exception {
		do_Delete(URLHelper.url(campaignendpoint,"/",campaignID),getApikey());
	}

	/**
	 * Get team feedback while you're working together on a Mailchimp campaign.
	 * @param campaignID
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/campaign-feedback/list-campaign-feedback/" target="MailchimpAPIDoc">Campaign Feedback -- GET /campaigns/{campaign_id}/feedback</a>
	 * @return List of feedback for the campaign
	 * @throws IOException
	 * @throws Exception 
	 */
	public List<CampaignFeedback> getCampaignFeedback(String campaignID, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		MailChimpQueryParameters query = queryParameters != null ? (MailChimpQueryParameters) queryParameters.clone() : new MailChimpQueryParameters();
		query.baseUrl(URLHelper.join(getCampaignendpoint(),"/",campaignID,"/feedback"));
		JSONObject campaignFeedback = new JSONObject(do_Get(query.getURL(),getApikey()));
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
	 * Get a specific feedback message from a campaign.
	 * @param campaignID The unique id for the campaign.
	 * @param feedbackId The unique id for the feedback message.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/campaign-feedback/get-campaign-feedback-message/" target="MailchimpAPIDoc">Campaign Feedback -- GET /campaigns/{campaign_id}/feedback/{feedback_id}</a>
	 * @return Specific feedback for the campaign
	 * @throws IOException 
	 * @throws Exception 
	 */
	public CampaignFeedback getCampaignFeedback(String campaignID, String feedbackId, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		MailChimpQueryParameters query = queryParameters != null ? (MailChimpQueryParameters) queryParameters.clone() : new MailChimpQueryParameters();
		query.baseUrl(URLHelper.join(getCampaignendpoint(),"/",campaignID,"/feedback/",feedbackId));
		JSONObject jsonObj = new JSONObject(do_Get(query.getURL(),getApikey()));
		CampaignFeedback feedback = new CampaignFeedback(this, jsonObj);
		return feedback;
	}
	
	/**
	 * Add campaign feedback
	 * @param campaignID
	 * @param message
	 * @return The newly created feedback
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
	 * @param campaignID The unique id for the campaign.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/campaign-checklist/get-campaign-send-checklist/" target="MailchimpAPIDoc">Campaign Send Checklist -- GET /campaigns/{campaign_id}/send-checklist</a>
	 * @throws IOException 
	 * @throws Exception 
	 */
	CampaignSendChecklist getCampaignSendChecklist(String campaignID, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		MailChimpQueryParameters query = queryParameters != null ? (MailChimpQueryParameters) queryParameters.clone() : new MailChimpQueryParameters();
		query.baseUrl(URLHelper.join(getCampaignendpoint(),"/",campaignID,"/send-checklist"));
		String results = do_Get(query.getURL(), getApikey());
		return new CampaignSendChecklist(new JSONObject(results));
	}
	
	/**
	 * 
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/reports/list-campaign-reports/" target="MailchimpAPIDoc">Reports -- GET /reports</a>
	 * @return Report iterator
	 * @throws IOException
	 * @throws Exception
	 */
	public Iterable<Report> getCampaignReports(final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return new ModelIterator<Report>(Report.class, reportsendpoint, this, queryParameters);
	}

	/**
	 * Mailchimp's campaign and Automation reports analyze clicks, opens, subscribers' social activity, e-commerce data, and more.
	 * 
	 * @param campaignId The unique id for the campaign.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/reports/get-campaign-report/" target="MailchimpAPIDoc">Reports -- GET /reports/{campaign_id}</a>
	 * @return Report for the specified campaign. 
	 * @throws IOException
	 * @throws Exception 
	 */
	public Report getCampaignReport(String campaignId, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		MailChimpQueryParameters query = queryParameters != null ? (MailChimpQueryParameters) queryParameters.clone() : new MailChimpQueryParameters();
		query.baseUrl(URLHelper.join(getReportsendpoint(), "/", campaignId));
		JSONObject jsonReport = new JSONObject(do_Get(query.getURL(), getApikey()));
    	return new Report(this, jsonReport);
	}
	
	/**
	 * Get detailed information about any campaign emails that were opened by a list member.
	 * @param campaignId The unique id for the campaign.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/open-reports/list-campaign-open-details/" target="MailchimpAPIDoc">Campaign Open Reports -- GET /reports/{campaign_id}/open-details</a>
	 * @return OpenReport Iterator
	 * @throws IOException
	 * @throws Exception
	 */
	public OpenReport getCampaignOpenReports(String campaignId, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return OpenReport.getOpenReport(this, campaignId, queryParameters);
	}
	
	/**
	 * Get a detailed report about any emails in a specific campaign that were opened by recipients.
	 * @param campaignId The unique id for the campaign.
	 * @param since Optional, restrict results to campaign open events that occur after a specific time.
	 * @return Detailed information about the campaigns emails that were opened by list members.
	 * @throws IOException
	 * @throws Exception 
	 * @deprecated use {@link MailChimpConnection#getCampaignOpenReports(String, MailChimpQueryParameters)} with "since" query parameter defined.
	 */
	public OpenReport getCampaignOpenReports(String campaignId, ZonedDateTime since) throws IOException, Exception {
		return OpenReport.getOpenReport(this, campaignId, since);
	}
	

	/**
	 * Get information about a specific subscriber who opened a campaign.
	 * @param campaignId The unique id for the campaign.
	 * @param subscriber The member's email address or subscriber hash
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/open-reports/get-opened-campaign-subscriber/" target="MailchimpAPIDoc">Campaign Subscriber Open Reports -- GET /reports/{campaign_id}/open-details/{subscriber_hash}</a>
	 * @return Detailed information about the campaigns emails that were opened by list members.
	 * @throws IOException
	 * @throws Exception 
	 */
	public OpenReportMember getCampaignOpenReport(String campaignId, String subscriber, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		MailChimpQueryParameters query = queryParameters != null ? (MailChimpQueryParameters) queryParameters.clone() : new MailChimpQueryParameters();
		query.baseUrl(URLHelper.join(getReportsendpoint(), "/", campaignId, "/open-details","/", Member.subscriberHash(subscriber)));
		JSONObject jsonReport = new JSONObject(do_Get(query.getURL(), getApikey()));
		return new OpenReportMember(jsonReport);
	}
	

	/**
	 * Get information about campaign abuse complaints.
	 * @param campaignId The unique id for the campaign.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/campaign-abuse/list-abuse-reports/" target="MailchimpAPIDoc">Campaign Abuse -- GET /reports/{campaign_id}/abuse-reports</a>
	 * @return Abuse complaints for a campaign
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<AbuseReport>  getCampaignAbuseReports(String campaignId, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/abuse-reports");
		return new ModelIterator<AbuseReport>(AbuseReport.class, baseURL, this, queryParameters);
	}

	/**
	 * Get information about campaign abuse complaints.
	 * @param campaignId The unique id for the campaign.
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
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/campaign-abuse/get-abuse-report/" target="MailchimpAPIDoc">Campaign Abuse -- GET /reports/{campaign_id}/abuse-reports/{report_id}</a>
	 * @return Information about a specific abuse report
	 * @throws IOException 
	 * @throws Exception 
	 */
	public AbuseReport  getCampaignAbuseReport(String campaignId, int reportId, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		MailChimpQueryParameters query = queryParameters != null ? (MailChimpQueryParameters) queryParameters.clone() : new MailChimpQueryParameters();
		query.baseUrl(URLHelper.join(getReportsendpoint(), "/", campaignId, "/abuse-reports/", Integer.toString(reportId)));
		JSONObject jsonReport = new JSONObject(do_Get(query.getURL(), getApikey()));
		AbuseReport report = new AbuseReport(jsonReport);
		return report;
	}
	
	/**
	 * Get feedback based on a campaign's statistics. Advice feedback is based on 
	 * campaign stats like opens, clicks, unsubscribes, bounces, and more.
	 * @param campaignId The unique id for the campaign.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/campaign-advice/list-campaign-feedback/" target="MailchimpAPIDoc">Campaign Advice -- GET /reports/{campaign_id}/advice</a>
	 * @return Recent feedback based on a campaign's statistics.
	 * @throws IOException
	 * @throws Exception 
	 */
	public List<AdviceReport> getCampaignAdviceReports(String campaignId, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		MailChimpQueryParameters query = queryParameters != null ? (MailChimpQueryParameters) queryParameters.clone() : new MailChimpQueryParameters();
		query.baseUrl(URLHelper.join(getReportsendpoint(), "/", campaignId, "/advice"));
		JSONObject jsonReports = new JSONObject(do_Get(query.getURL(), getApikey()));
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
	 * @param campaignId The unique id for the campaign.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/click-reports/list-campaign-details/" target="MailchimpAPIDoc">Click Reports -- GET /reports/{campaign_id}/click-details</a>
	 * @return Campaign click details
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<ClickReport> getCampaignClickReports(String campaignId, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/click-details");
		return new ModelIterator<ClickReport>(ClickReport.class, baseURL, this, queryParameters);
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
		return new ModelIterator<ClickReport>(ClickReport.class, baseURL, this, 500);
	}
	
	/**
	 * Get detailed information about links clicked in campaigns for a specific link.
	 * @param campaignId The unique id for the campaign.
	 * @param linkId The id for the link.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/click-reports/get-campaign-link-details/" target="MailchimpAPIDoc">Click Reports -- GET /reports/{campaign_id}/click-details/{link_id}</a>
	 * @return Click details for a specific link.
	 * @throws IOException
	 * @throws Exception 
	 */
	public ClickReport getCampaignClickReport(String campaignId, String linkId, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		MailChimpQueryParameters query = queryParameters != null ? (MailChimpQueryParameters) queryParameters.clone() : new MailChimpQueryParameters();
		query.baseUrl(URLHelper.join(getReportsendpoint(), "/", campaignId, "/click-details/", linkId));
		JSONObject jsonReport = new JSONObject(do_Get(query.getURL(), getApikey()));
		ClickReport report = new ClickReport(jsonReport);
		return report;
	}
	
	/**
	 * Get information about subscribers who clicked a link.
	 * @param campaignId The unique id for the campaign.
	 * @param linkId The id for the link.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/link-clickers/list-clicked-link-subscribers/" target="MailchimpAPIDoc">Click Reports Members -- GET /reports/{campaign_id}/click-details/{link_id}/members</a>
	 * @return Information about subscribers who clicked a link
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<ClickReportMember> getCampaignMembersClickReports(String campaignId, String linkId, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/click-details/", linkId, "/members");
		return new ModelIterator<ClickReportMember>(ClickReportMember.class, baseURL, this, queryParameters);
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
		return new ModelIterator<ClickReportMember>(ClickReportMember.class, baseURL, this, 500);
	}
	
	/**
	 * Get information about a specific subscriber who clicked a link.
	 * @param campaignId The unique id for the campaign.
	 * @param linkId The id for the link.
	 * @param subscriber The member's email address or subscriber hash
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/link-clickers/get-clicked-link-subscriber/" target="MailchimpAPIDoc">Click Reports Members -- GET /reports/{campaign_id}/click-details/{link_id}/members/{subscriber_hash}</a>
	 * @return Information about a specific subscriber who clicked a link
	 * @throws IOException
	 * @throws Exception 
	 */
	public ClickReportMember getCampaignMembersClickReport(String campaignId, String linkId, String subscriber, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		MailChimpQueryParameters query = queryParameters != null ? (MailChimpQueryParameters) queryParameters.clone() : new MailChimpQueryParameters();
		query.baseUrl(URLHelper.join(getReportsendpoint(), "/", campaignId, "/click-details/", linkId, "/members/", Member.subscriberHash(subscriber)));
		JSONObject jsonReport = new JSONObject(do_Get(query.getURL(), getApikey()));
		ClickReportMember report = new ClickReportMember(jsonReport);
		return report;
	}
	
	/**
	 * Get statistics for the top-performing domains from a campaign.
	 * @param campaignId The unique id for the campaign.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/domain-performance-reports/list-domain-performance-stats/" target="MailchimpAPIDoc">Reports Domain Performance -- GET /reports/{campaign_id}/domain-performance</a>
	 * @return Statistics for the top-performing domains from a campaign.
	 * @throws IOException
	 * @throws Exception 
	 */
	public DomainPerformance getDomainPerformanceReport(String campaignId, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		MailChimpQueryParameters query = queryParameters != null ? (MailChimpQueryParameters) queryParameters.clone() : new MailChimpQueryParameters();
		query.baseUrl(URLHelper.join(getReportsendpoint(), "/", campaignId, "/domain-performance"));
		JSONObject jsonReport = new JSONObject(do_Get(query.getURL(), getApikey()));
		DomainPerformance report = new DomainPerformance(jsonReport);
		return report;
	}
	
	/**
	 * Ecommerce product activity report for Campaign
	 * @param campaignId The unique id for the campaign.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/campaign-ecommerce-product-activity/list-campaign-product-activity/" target="MailchimpAPIDoc">Ecommerce Product Activity -- GET /reports/{campaign_id}/ecommerce-product-activity</a>
	 * @return Breakdown of product activity for a campaign.
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<EcommerceProductActivity> getEcommerceProductActivityReports(String campaignId, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/ecommerce-product-activity");
		return new ModelIterator<EcommerceProductActivity>(EcommerceProductActivity.class, baseURL, this);
	}

	/**
	 * Sent To report - Get information about campaign recipients.
	 * @param campaignId The unique id for the campaign.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/sent-to-reports/list-campaign-recipients/" target="MailchimpAPIDoc">Sent To -- GET /reports/{campaign_id}/sent-to</a>
	 * @return Information about campaign recipients.
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<ReportSentTo> getCampaignSentToReports(String campaignId, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/sent-to");
		return new ModelIterator<ReportSentTo>(ReportSentTo.class, baseURL, this, queryParameters);
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
	 * @param subscriber The member's email address or subscriber hash
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/sent-to-reports/list-campaign-recipients/" target="MailchimpAPIDoc">Sent To -- GET /reports/{campaign_id}/sent-to</a>
	 * @return Information about a specific campaign recipients.
	 * @throws IOException 
	 * @throws Exception 
	 */
	public ReportSentTo getCampaignSentToReport(String campaignId, String subscriber, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		MailChimpQueryParameters query = queryParameters != null ? (MailChimpQueryParameters) queryParameters.clone() : new MailChimpQueryParameters();
		query.baseUrl(URLHelper.join(getReportsendpoint(), "/", campaignId, "/sent-to/", Member.subscriberHash(subscriber)));
		JSONObject jsonRpt = new JSONObject(do_Get(query.getURL(), getApikey()));
		ReportSentTo rpt = new ReportSentTo(jsonRpt);
		return rpt;
	}

	/**
	 * Email Activity report - Get list member activity for a campaign.
	 * @param campaignId The unique id for the campaign.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/email-activity-reports/list-email-activity/" target="MailchimpAPIDoc">Email Activity -- GET /reports/{campaign_id}/email-activity</a>
	 * @return Member activity for a campaign.
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<EmailActivity> getCampaignEmailActivityReports(String campaignId, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/email-activity");
		return new ModelIterator<EmailActivity>(EmailActivity.class, baseURL, this, queryParameters);
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
	 * @param subscriber The member's email address or subscriber hash
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/email-activity-reports/get-subscriber-email-activity/" target="MailchimpAPIDoc">Email Activity -- GET /reports/{campaign_id}/email-activity/{subscriber_hash}</a>
	 * @return Member activity for a campaign.
	 * @throws IOException 
	 * @throws Exception 
	 */
	public EmailActivity getCampaignEmailActivityReport(String campaignId, String subscriber, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		MailChimpQueryParameters query = queryParameters != null ? (MailChimpQueryParameters) queryParameters.clone() : new MailChimpQueryParameters();
		query.baseUrl(URLHelper.join(getReportsendpoint(), "/", campaignId, "/email-activity/", Member.subscriberHash(subscriber)));
		JSONObject jsonRpt = new JSONObject(do_Get(query.getURL(), getApikey()));
		EmailActivity rpt = new EmailActivity(jsonRpt);
		return rpt;
	}
	
	/**
	 * Top open locations for a specific campaign.
	 * @param campaignId The unique id for the campaign.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/location-reports/list-top-open-activities/" target="MailchimpAPIDoc">Reports Location -- GET /reports/{campaign_id}/locations</a>
	 * @throws IOException 
	 * @throws Exception 
	 */
	public Iterable<ReportLocation> getCampaignLocationsReports(String campaignId, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		final String baseURL = URLHelper.join(getReportsendpoint(), "/", campaignId, "/locations");
		return new ModelIterator<ReportLocation>(ReportLocation.class, baseURL, this, queryParameters);
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
	 * Get template folders iterator
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/template-folders/" target="MailchimpAPIDoc">Template Folders -- GET /template-folders</a>
	 * @return Template folder iterator
	 * @throws IOException
	 * @throws Exception
	 */
	public Iterable<TemplateFolder> getTemplateFolders(final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return new ModelIterator<TemplateFolder>(TemplateFolder.class, templatefolderendpoint, this, queryParameters);
	}
	
    /**
     * Get information about a specific folder used to organize templates.
     * @param folder_id The unique id for the template folder.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/templates/" target="MailchimpAPIDoc">Template Folders -- GET /template-folders/{folder_id}</a>
	 * @throws IOException
     * @throws Exception 
     */
    public TemplateFolder getTemplateFolder(String folder_id, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		MailChimpQueryParameters query = queryParameters != null ? (MailChimpQueryParameters) queryParameters.clone() : new MailChimpQueryParameters();
		query.baseUrl(URLHelper.join(templatefolderendpoint,"/",folder_id));
        JSONObject jsonTemplateFolder = new JSONObject(do_Get(query.getURL(), getApikey()));
        return new TemplateFolder(this, jsonTemplateFolder);
    }

    /**
     * Create a new template folder.
     * @param name The name of the folder.
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
	 * Get a list of an account's available templates.
	 * @return Template iterator
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<Template> getTemplates() throws IOException, Exception {
		return new ModelIterator<Template>(Template.class, templateendpoint, this, 500);
	}

	/**
	 * Get a list of an account's available templates.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/templates/" target="MailchimpAPIDoc">Templates -- GET /templates</a>
	 * @return Template iterator
	 * @throws IOException
	 * @throws Exception
	 */
	public Iterable<Template> getTemplates(final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return new ModelIterator<Template>(Template.class, templateendpoint, this, queryParameters);
	}
	
	/**
	 * Get a template from mailchimp account
	 * @param id The unique id for the template.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/templates/get-template-info/" target="MailchimpAPIDoc">Templates -- GET /templates/{template_id}</a>
	 * @return a template object
	 * @throws IOException
	 * @throws Exception 
	 */
	public Template getTemplate(String id, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		MailChimpQueryParameters query = queryParameters != null ? (MailChimpQueryParameters) queryParameters.clone() : new MailChimpQueryParameters();
		query.baseUrl(URLHelper.join(templateendpoint,"/",id));
		JSONObject jsonTemplate = new JSONObject(do_Get(query.getURL(),getApikey()));
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
	 * Get a summary of an account's classic automations.
	 * Mailchimp's classic automations feature lets you build a series of emails that 
	 * send to subscribers when triggered by a specific date, activity, or event. Use 
	 * the API to manage Automation workflows, emails, and queues. Does not include 
	 * Customer Journeys.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/automation/list-automations/" target="MailchimpAPIDoc">Automations -- GET /automations</a>
	 * @return automations iterator
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<Automation> getAutomations(final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return new ModelIterator<Automation>(Automation.class, automationendpoint, this, queryParameters);
	}
	
	/**
	 * Get a summary of an individual classic automation workflow's settings and
	 * content. The trigger_settings object returns information for the first email
	 * in the workflow.
	 * 
	 * @param workflowId The unique id for the Automation workflow
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/automation/get-automation-info/" target="MailchimpAPIDoc">Automations -- GET /automations/{workflow_id}</a>
	 * @return an Automation object
	 * @throws IOException
	 * @throws Exception
	 */
	public Automation getAutomation(String workflowId, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		MailChimpQueryParameters query = queryParameters != null ? (MailChimpQueryParameters) queryParameters.clone() : new MailChimpQueryParameters();
		query.baseUrl(URLHelper.join(automationendpoint,"/",workflowId));
		JSONObject jsonAutomation = new JSONObject(do_Get(query.getURL(), getApikey()));
		return new Automation(this, jsonAutomation);
	}

	/**
	 * Create a new classic automation in your Mailchimp account.
	 * @param recipients List settings for the Automation.
	 * @param settings The settings for the Automation workflow.
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
	
// Does this endpoint support query parameters? 
//	/**
//	 * Get a list of automated emails in a workflow
//	 * @param workflowId The unique id for the Automation workflow
//	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
//	 *   @see <a href="https://mailchimp.com/developer/marketing/api/automation-email/list-automated-emails/" target="MailchimpAPIDoc">Automation Emails -- GET /automations/{workflow_id}/emails</a>
//	 * @return List containing automation emails
//	 * @throws IOException
//	 * @throws Exception 
//	 */
//	public Iterable<AutomationEmail> getAutomationEmails(String workflowId, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
//		final String baseURL = URLHelper.join(automationendpoint, "/", workflowId, "/emails");
//		return new ModelIterator<AutomationEmail>(AutomationEmail.class, baseURL, this, queryParameters);
//	}
	
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
	 * Get a summary of batch requests that have been made in the last seven days.
     * @param queryParameters Optional query parameters to send to the MailChimp API. 
     *   @see <a href="https://mailchimp.com/developer/marketing/api/batch-operations/list-batch-requests/" target="MailchimpAPIDoc">Batch Operations -- GET /batches</a>
	 * @return BatchInfo iterator for batches created in the last seven days.
	 * @throws IOException
	 * @throws Exception
	 */
	public Iterable<BatchInfo> getBatches(final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return new ModelIterator<BatchInfo>(BatchInfo.class, batchendpoint, this, queryParameters);
	}

	/**
	 * Get the status of a batch request.
	 * @param batch_id The unique id for the batch operation.
     * @param queryParameters Optional query parameters to send to the MailChimp API. 
     *   @see <a href="https://mailchimp.com/developer/marketing/api/batch-operations/get-batch-operation-status/" target="MailchimpAPIDoc">Batch Operations -- /batches/{batch_id}</a>
	 * @return The status of a batch request.
	 * @throws IOException
	 * @throws Exception
	 */
	public BatchInfo getBatch(String batch_id, final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		MailChimpQueryParameters query = queryParameters != null ? (MailChimpQueryParameters) queryParameters.clone() : new MailChimpQueryParameters();
		query.baseUrl(URLHelper.join(batchendpoint,"/",batch_id));
		JSONObject jsonBatch = new JSONObject(do_Get(query.getURL(), getApikey()));
		return new BatchInfo(this, jsonBatch);
	}
	
	/**
	 * Start batch operation
	 * @param operations List of operations to perform in a batch
	 * @return The status of a batch request
	 * @throws IOException
	 * @throws Exception
	 */
	public BatchInfo createBatch(final List<BatchOperation> operations) throws IOException, Exception {
		JSONObject jsonObj = new JSONObject();
		JSONArray a = new JSONArray();
		for (BatchOperation batch : operations) {
			a.put(batch.getJsonRepresentation());
		}
		jsonObj.put("operations", a);
		
		String results = do_Post(URLHelper.url(batchendpoint), jsonObj.toString(),getApikey());
		return new BatchInfo(this, new JSONObject(results));
	}

	/**
	 * Stops a batch request from running. Since only one batch request is run at a
	 * time, this can be used to cancel a long running request. The results of any
	 * completed operations will not be available after this call.
	 * 
	 * @param batch_id The unique id for the batch operation.
	 * @throws IOException
	 * @throws Exception
	 */
	public void deleteBatch(String batch_id) throws IOException, Exception {
		do_Delete(URLHelper.url(batchendpoint,"/",batch_id),getApikey());
	}
	
	// TODO: Batch Webhooks
	
	
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
	 * @return The server
	 */
	public String getServer() {
		return this.server;
	}

	/**
	 * @return The api key
	 */
	public String getApikey() {
		return this.authorization;
	}

	/**
	 * @return The api key
	 */
	public String getAuthorization() {
		return this.authorization;
	}

	/**
	 * @return The api end point
	 */
	public String getApiendpoint() {
		return this.apiendpoint;
	}

	/**
	 * @return The batch end point
	 */
	public String getBatchendpoint() {
		return this.batchendpoint;
	}

	/**
	 * @return The list end point
	 */
	public String getListendpoint() {
		return this.listendpoint;
	}

	/**
	 * @return The campaign end point
	 */
	public String getCampaignendpoint() {
		return this.campaignendpoint;
	}

	/**
	 * @return The template end point
	 */
	public String getTemplateendpoint() {
		return this.templateendpoint;
	}

	/**
	 * @return The automation end point
	 */
	public String getAutomationendpoint(){
		return this.automationendpoint;
	}

	/**
	 * @return The file manager folder end point
	 */
	public String getFilemanagerfolderendpoint() {
		return this.filemanagerfolderendpoint;
	}

	/**
	 * @return The files end point
	 */
	public String getFilesendpoint() {
		return filesendpoint;
	}

	/**
	 * @return The campaign folder end point
	 */
	public String getCampaignfolderendpoint() {
		return this.campaignfolderendpoint;
	}

	/**
	 * @return The template folder end point
	 */
	public String getTemplatefolderendpoint() {
		return this.templatefolderendpoint;
	}

	/**
	 * @return The reports end point
	 */
	public String getReportsendpoint() {
		return reportsendpoint;
	}

	/**
	 * @return the account information
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
