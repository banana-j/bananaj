package com.github.alexanderwe.bananaj.connection;

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

import com.github.alexanderwe.bananaj.exceptions.TransportException;
import com.github.alexanderwe.bananaj.model.automation.Automation;
import com.github.alexanderwe.bananaj.model.automation.AutomationRecipient;
import com.github.alexanderwe.bananaj.model.automation.AutomationSettings;
import com.github.alexanderwe.bananaj.model.automation.emails.AutomationEmail;
import com.github.alexanderwe.bananaj.model.campaign.Campaign;
import com.github.alexanderwe.bananaj.model.campaign.CampaignFolder;
import com.github.alexanderwe.bananaj.model.campaign.CampaignRecipients;
import com.github.alexanderwe.bananaj.model.campaign.CampaignSettings;
import com.github.alexanderwe.bananaj.model.campaign.CampaignType;
import com.github.alexanderwe.bananaj.model.filemanager.FileManager;
import com.github.alexanderwe.bananaj.model.list.MailChimpList;
import com.github.alexanderwe.bananaj.model.report.AbuseReport;
import com.github.alexanderwe.bananaj.model.report.OpenReport;
import com.github.alexanderwe.bananaj.model.report.Report;
import com.github.alexanderwe.bananaj.model.template.Template;
import com.github.alexanderwe.bananaj.model.template.TemplateFolder;
import com.github.alexanderwe.bananaj.utils.DateConverter;

/**
 * Class for the com.github.alexanderwe.bananaj.connection to mailchimp servers. Used to get lists from mailchimp account.
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
	 * Get the List/Audience in your account
	 * @return List containing the first 100 lists
	 * @throws Exception
	 * @deprecated
	 */
	public List<MailChimpList> getLists() throws Exception {
		return getLists(100,0);
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
		JSONObject jsonLists = new JSONObject(do_Get(new URL(listendpoint + "?offset=" + offset + "&count=" + count),getApikey()));
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
		JSONObject jsonList = new JSONObject(do_Get(new URL(listendpoint +"/"+listID),getApikey()));
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
		do_Delete(new URL(listendpoint +"/"+listID), getApikey());
	}

	/**
	 * A health check for the API.
	 * @return true on successful API ping otherwise false
	 */
	public boolean ping() {
		try {
			JSONObject jsonObj = new JSONObject(do_Get(new URL(apiendpoint + "ping"), getApikey()));
			if (jsonObj.has("health_status") && "Everything's Chimpy!".equals(jsonObj.getString("health_status"))) {
				return true;
			}
		} catch (Exception ignored) { }
		return false;
	}
	
    /**
     * Get campaign folders from MailChimp
     * @return List containing the first 100 campaign folders
	 * @deprecated
     */
    public List<CampaignFolder> getCampaignFolders() throws Exception{
        return getCampaignFolders(100,0);
    }

    /**
     * Get campaign folders from MailChimp with pagination
     * @param count Number of campaign folders to return. Maximum value is 1000.
     * @param offset Zero based offset
     * @return List containing the campaign folders
     */
    public List<CampaignFolder> getCampaignFolders(int count, int offset) throws Exception{
    	List<CampaignFolder> campaignFolders = new ArrayList<>();
    	JSONObject campaignFoldersResponse = new JSONObject(do_Get(new URL(campaignfolderendpoint + "?offset=" + offset + "&count=" + count), getApikey()));

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

    	JSONObject jsonCampaignFolder = new JSONObject(do_Get(new URL(campaignfolderendpoint +"/"+folder_id), getApikey()));
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
    	do_Delete(new URL(campaignfolderendpoint +"/"+folder_id), getApikey());
    }

    /**
     * Get campaigns from mailchimp account
     * @return List containing the first 100 campaigns
     * @throws Exception
     * @deprecated
     */
    public List<Campaign> getCampaigns() throws Exception {
    	return getCampaigns(100,0);
    }

    /**
     * Get campaigns from mailchimp account with pagination
     * @param count Number of campaigns to return. Maximum value is 1000.
     * @param offset Zero based offset
     * @return List containing campaigns
     * @throws Exception
     */
    public List<Campaign> getCampaigns(int count, int offset) throws Exception {
    	// parse response
    	JSONObject jsonCampaigns = new JSONObject(do_Get(new URL(campaignendpoint+ "?offset=" + offset + "&count=" + count),getApikey()));
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
		JSONObject campaign = new JSONObject(do_Get(new URL(campaignendpoint +"/"+campaignID),getApikey()));
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

	public Campaign createCampaign(CampaignType type, CampaignRecipients mailRecipients, CampaignSettings settings) throws Exception{
		
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
		do_Delete(new URL(campaignendpoint +"/"+campaignID),getApikey());
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
		URL url = new URL(reportsendpoint + "/" + campaignId);
		JSONObject jsonReport = new JSONObject(do_Get(url, getApikey()));
    	return new Report(jsonReport);
	}
	
	/**
	 * Mailchimp's campaign and Automation reports analyze clicks, opens, subscribers' social activity, e-commerce data, and more.
	 * @param count Number of reports to return. Maximum value is 1000.
	 * @param offset Zero based offset
	 * @param campaignType Optional, restrict the response by campaign type
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
		URL url = new URL(reportsendpoint + "?offset=" + offset + "&count=" + count +
				(campaignType!=null ? "&type" + campaignType.toString() : "") +
				(beforeSendTime!=null ? "&before_send_time=" + URLEncoder.encode(DateConverter.toISO8601UTC(beforeSendTime), "UTF-8") : "") +
				(sinceSendTime!=null ? "&since_send_time=" + URLEncoder.encode(DateConverter.toISO8601UTC(sinceSendTime), "UTF-8") : "") );
		JSONObject jsonReports = new JSONObject(do_Get(url, getApikey()));
		//int total_items = jsonReports.getInt("total_items"); 	// The total number of items matching the query regardless of pagination
    	JSONArray reportsArray = jsonReports.getJSONArray("reports");
    	List<Report> reports = new ArrayList<Report>(reportsArray.length());
    	for( int i = 0; i< reportsArray.length();i++)
    	{
    		JSONObject reportDetail = reportsArray.getJSONObject(i);
    		Report report = new Report(reportDetail);
    		reports.add(report);
    	}
    	return reports;
	}
	
	/**
	 * Get a detailed report about any emails in a specific campaign that were opened by recipients.
	 * @param count Number of reports to return. Maximum value is 1000.
	 * @param offset Zero based offset
	 * @param campaignId
	 * @param since Optional, restrict results to campaign open events that occur after a specific time.
	 * @return Detailed information about the campaigns emails that were opened by list members.
	 * @throws JSONException
	 * @throws TransportException
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException
	 */
	public OpenReport getCampaignOpenReports(int count, int offset, String campaignId, ZonedDateTime since) throws JSONException, TransportException, URISyntaxException, MalformedURLException, UnsupportedEncodingException {
		URL url = new URL(reportsendpoint + "/" + campaignId + "/open-details?offset=" + offset + "&count=" + count +
				(since!=null ? "&since=" + URLEncoder.encode(DateConverter.toISO8601UTC(since), "UTF-8") : "") );
		JSONObject jsonReports = new JSONObject(do_Get(url, getApikey()));
		OpenReport report = new OpenReport(jsonReports);
		return report;
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
		URL url = new URL(reportsendpoint + "/" + campaignId + "/abuse-reports?offset=" + offset + "&count=" + count);
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
		URL url = new URL(reportsendpoint + "/" + campaignId + "/abuse-reports/" + reportId);
		JSONObject jsonReport = new JSONObject(do_Get(url, getApikey()));
		AbuseReport report = new AbuseReport(jsonReport);
		return report;
	}
	
	// TODO: Report - Campaign Abuse - Get information about a specific abuse report
	// TODO: Report - Campaign Advice - Get recent feedback based on a campaign's statistics.
	// TODO: Report - Click Reports - Get detailed information about links clicked in campaigns.
	// TODO: Report - Click Reports - Get detailed information about links clicked in campaigns for a specific link.
	// TODO: Report - Click Reports Members - Get information about subscribers who clicked a link.
	// TODO: Report - Click Reports Members - Get information about a specific subscriber who clicked a link
	// TODO: Report - Domain Performance - Get statistics for the top-performing domains from a campaign.
	// TODO: Report - Ecommerce Product Activity - Ecommerce product activity report for Campaign.
	// TODO: Report - EepURL Reports - Get a summary of social activity for the campaign, tracked by EepURL.
	// TODO: Report - Email Activity - Get list member activity for a specific campaign.
	// TODO: Report - Email Activity - Get list member activity for a specific campaign and subscriber.
	// TODO: Report - Location - Get top open locations for a specific campaign.
	// TODO: Report - Sent To - Get information about campaign recipients.
	// TODO: Report - Sent To - Get information about a specific campaign recipient.
	// TODO: Report - Sub-Reports- A list of reports for child campaigns of a specific parent campaign. For example, use this endpoint to view Multivariate, RSS, and A/B Testing Campaign reports.
	// TODO: Report - Unsubscribes - Get information about list members who unsubscribed from a specific campaign.
	
    /**
     * Get template folders from MailChimp
     * @return List containing the first 100 template folders
	 * @deprecated
     */
	public List<TemplateFolder> getTemplateFolders() throws Exception{
        return getTemplateFolders(100,0);
	}

    /**
     * Get template folders from MailChimp with pagination
	 * @param count Number of templates to return. Maximum value is 1000.
	 * @param offset Zero based offset
     * @return List of template folders
     */
	public List<TemplateFolder> getTemplateFolders(int count, int offset) throws Exception{
        List<TemplateFolder> templateFolders = new ArrayList<>();
        JSONObject templateFoldersResponse = new JSONObject(do_Get(new URL(templatefolderendpoint + "?offset=" + offset + "&count=" + count), getApikey()));
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

        JSONObject jsonTemplateFolder = new JSONObject(do_Get(new URL(templatefolderendpoint +"/"+folder_id), getApikey()));
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
        do_Delete(new URL(templatefolderendpoint +"/"+folder_id), getApikey());
    }

	/**
	 * Get templates from mailchimp account
	 * @return List containing the first 100 templates
	 * @throws Exception
	 * @deprecated
	 */
	public List<Template> getTemplates() throws Exception{
		return getTemplates(100,0);
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

		JSONObject jsonTemplates = new JSONObject(do_Get(new URL(templateendpoint + "?offset=" + offset + "&count=" + count),getApikey()));
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
		JSONObject jsonTemplate = new JSONObject(do_Get(new URL(templateendpoint +"/" +id),getApikey()));
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
		String results = do_Post(new URL(templateendpoint +"/"), jsonObj.toString(),getApikey());
		template.parse(this, new JSONObject(results));
		return template;
	}
	
	/**
	 * Delete a specific template
	 * @param id
	 * @throws Exception
	 */
	public void deleteTemplate(String id) throws Exception {
		do_Delete(new URL(templateendpoint +"/" +id),getApikey());
	}

	/**
	 * Get a list of Automations
	 * @return List containing the first 100 automations
	 * @throws Exception
	 * @deprecated
	 */
	public List<Automation> getAutomations() throws Exception{
		return getAutomations(100,0);
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

		JSONObject jsonAutomations = new JSONObject(do_Get(new URL(automationendpoint + "?offset=" + offset + "&count=" + count),getApikey()));
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
		JSONObject jsonAutomation = new JSONObject(do_Get(new URL(automationendpoint +"/"+workflowId), getApikey()));
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
		do_Post(new URL(getAutomationendpoint() +"/"+workflowId+"/actions/pause-all-emails"), getApikey());
	}
	
	/**
	 * Start all emails in an Automation workflow
	 * @param workflowId The unique id for the Automation workflow
	 * @throws Exception
	 */
	public void startAutomationEmails(String workflowId) throws Exception {
		do_Post(new URL(getAutomationendpoint() +"/"+workflowId+"/actions/start-all-emails"), getApikey());
	}
	
	/**
	 * Get a list of automated emails in a workflow
	 * @param workflowId The unique id for the Automation workflow
	 * @return List containing the first 100 emails
	 * @throws Exception 
	 */
	public List<AutomationEmail> getAutomationEmails(String workflowId) throws Exception {
		return getAutomationEmails(workflowId, 100, 0);
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
		JSONObject jsonObj = new JSONObject(do_Get(new URL(automationendpoint + "/" + workflowId + "/emails" + "?offset=" + offset + "&count=" + count), getApikey()));
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
		JSONObject jsonObj = new JSONObject(do_Get(new URL(automationendpoint + "/" + workflowId + "/emails/" + workflowEmailId), getApikey()));
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
		do_Post(new URL(getAutomationendpoint() + "/" + workflowId + "/emails/" + workflowEmailId + "/queue"), jsonObj.toString(), getApikey());
		// Note: MailChimp documents this as returning an AutomationSubscriber but in practice it returns nothing
	}
	/**
	 * Delete a workflow email
	 * @param workflowId The unique id for the Automation workflow
	 * @param workflowEmailId The unique id for the Automation workflow email
	 * @throws Exception
	 */
	public void deleteAutomationEmail(String workflowId, String workflowEmailId) throws Exception {
		do_Delete(new URL(automationendpoint + "/" + workflowId + "/emails/" + workflowEmailId), getApikey());
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
