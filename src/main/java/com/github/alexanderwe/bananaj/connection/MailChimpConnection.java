package com.github.alexanderwe.bananaj.connection;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.alexanderwe.bananaj.exceptions.TransportException;
import com.github.alexanderwe.bananaj.model.automation.Automation;
import com.github.alexanderwe.bananaj.model.automation.AutomationRecipient;
import com.github.alexanderwe.bananaj.model.automation.AutomationSettings;
import com.github.alexanderwe.bananaj.model.automation.emails.AutomationEmail;
import com.github.alexanderwe.bananaj.model.campaign.Campaign;
import com.github.alexanderwe.bananaj.model.campaign.CampaignDefaults;
import com.github.alexanderwe.bananaj.model.campaign.CampaignFolder;
import com.github.alexanderwe.bananaj.model.campaign.CampaignRecipients;
import com.github.alexanderwe.bananaj.model.campaign.CampaignSettings;
import com.github.alexanderwe.bananaj.model.campaign.CampaignType;
import com.github.alexanderwe.bananaj.model.filemanager.FileManager;
import com.github.alexanderwe.bananaj.model.list.MailChimpList;
import com.github.alexanderwe.bananaj.model.list.member.Member;
import com.github.alexanderwe.bananaj.model.template.Template;
import com.github.alexanderwe.bananaj.model.template.TemplateFolder;

import jxl.CellView;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Class for the com.github.alexanderwe.bananaj.connection to mailchimp servers. Used to get lists from mailchimp account.
 * @author alexanderweiss
 *
 */
public class MailChimpConnection extends Connection{

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
	 * Get the lists in your account
	 * @return List containing the first 100 lists
	 * @throws Exception
	 */
	public List<MailChimpList> getLists() throws Exception {
		return getLists(100,0);
	}

	/**
	 * Get lists in your account with pagination
	 * @param count Number of lists to return
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
	 * Get a specific mailchimp list
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
	 * Create a new list in your mailchimp account
	 * @param listName
	 */
	public MailChimpList createList(String listName, String permission_reminder, boolean email_type_option, CampaignDefaults campaignDefaults) throws Exception{
		cacheAccountInfo();
		JSONObject jsonList = new JSONObject();
		
		JSONObject contact = new JSONObject();
		contact.put("company", account.getContact().getCompany());
		contact.put("address1", account.getContact().getAddress1());
		contact.put("city", account.getContact().getCity());
		contact.put("state", account.getContact().getState());
		contact.put("zip", account.getContact().getZip());
		contact.put("country", account.getContact().getCountry());
		
		JSONObject JSONCampaignDefaults = new JSONObject();
		JSONCampaignDefaults.put("from_name", campaignDefaults.getFrom_name());
		JSONCampaignDefaults.put("from_email", campaignDefaults.getFrom_email());
		JSONCampaignDefaults.put("subject", campaignDefaults.getSubject());
		JSONCampaignDefaults.put("language", campaignDefaults.getLanguage());
		
		jsonList.put("name",listName);
		jsonList.put("permission_reminder", permission_reminder);
		jsonList.put("email_type_option", email_type_option);
		jsonList.put("contact", contact);
		jsonList.put("campaign_defaults", JSONCampaignDefaults);

		JSONObject jsonNewList = new JSONObject(do_Post(new URL(listendpoint), jsonList.toString(),getApikey()));
		return new MailChimpList(this, jsonNewList);
	}

	/**
	 * Delete a list from your account
	 * @param listID
	 * @throws Exception
	 */
	public void deleteList(String listID) throws Exception{
		do_Delete(new URL(listendpoint +"/"+listID),getApikey());
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
	 * Write all lists to an Excel file
	 * @throws Exception
	 */
	public void writeAllListToExcel(String filepath, boolean show_merge) throws Exception{
		WritableWorkbook workbook = Workbook.createWorkbook(new File(filepath+".xls"));
		WritableFont times16font = new WritableFont(WritableFont.TIMES, 16, WritableFont.BOLD, false);
		WritableCellFormat times16format = new WritableCellFormat (times16font);

		List<MailChimpList> mailChimpLists = getLists();
		int index  = 0;
		for(MailChimpList mailChimpList : mailChimpLists){
			WritableSheet sheet = workbook.createSheet(mailChimpList.getName(), index);

			Label memberIDLabel = new Label(0, 0, "MemberID",times16format);
			Label email_addressLabel = new Label(1,0,"Email Address",times16format);
			Label timestamp_sign_inLabel = new Label(2,0,"Sign up",times16format);
			Label ip_signinLabel = new Label(3,0,"IP Sign up", times16format);
			Label timestamp_opt_inLabel = new Label(4,0,"Opt in",times16format);
			Label ip_optLabel = new Label(5,0,"IP Opt in", times16format);
			Label statusLabel = new Label(6,0,"Status",times16format);
			Label avg_open_rateLabel = new Label(7,0,"Avg. open rate",times16format);
			Label avg_click_rateLabel = new Label(8,0,"Avg. click rate",times16format);


			sheet.addCell(memberIDLabel);
			sheet.addCell(email_addressLabel);
			sheet.addCell(timestamp_sign_inLabel);
			sheet.addCell(ip_signinLabel);
			sheet.addCell(timestamp_opt_inLabel);
			sheet.addCell(ip_optLabel);
			sheet.addCell(statusLabel);
			sheet.addCell(avg_open_rateLabel);
			sheet.addCell(avg_click_rateLabel);

			List<Member> members = mailChimpList.getMembers(0,0);
			int merge_field_count = 0;

			if (show_merge){
				int last_column = 9;

				Iterator<Entry<String, Object>> iter = members.get(0).getMergeFields().entrySet().iterator();
				while (iter.hasNext()) {
					Entry<String, Object> pair = iter.next();
					sheet.addCell(new Label(last_column,0,pair.getKey(),times16format));
					iter.remove(); // avoids a ConcurrentModificationException
					last_column++;
					merge_field_count++;
				}
			}


			for(int i = 0 ; i < members.size();i++)
			{
				Member member = members.get(i);
				sheet.addCell(new Label(0,i+1,member.getId()));
				sheet.addCell(new Label(1,i+1,member.getEmailAddress()));
				sheet.addCell(new Label(2,i+1,member.getTimestampSignup() != null ? member.getTimestampSignup().toString() : ""));
				sheet.addCell(new Label(3,i+1,member.getIpSignup() != null ? member.getIpSignup() : ""));
				sheet.addCell(new Label(4,i+1,member.getTimestampOpt() != null ? member.getTimestampOpt().toString() : ""));
				sheet.addCell(new Label(5,i+1,member.getIpOpt() != null ? member.getIpOpt() : ""));
				sheet.addCell(new Label(6,i+1,member.getStatus().getStringRepresentation()));
				sheet.addCell(new Number(7,i+1,member.getStats().getAvgOpenRate()));
				sheet.addCell(new Number(8,i+1,member.getStats().getAvgClickRate()));

				if (show_merge){
					//add merge fields values
					int last_index = 9;
					Iterator<Entry<String, Object>> iter_member = member.getMergeFields().entrySet().iterator();
					while (iter_member.hasNext()) {
						Entry<String, Object> pair = iter_member.next();
						sheet.addCell(new Label(last_index,i+1,pair.getValue().toString()));
						iter_member.remove(); // avoids a ConcurrentModificationException
						last_index++;

					}
				}
			}

			CellView cell;

			int column_count = 9 + merge_field_count;
			for(int x=0;x<column_count;x++)
			{
				cell=sheet.getColumnView(x);
				cell.setAutosize(true);
				sheet.setColumnView(x, cell);
			}
			index++;
		}
		workbook.write();
		workbook.close();
		System.out.println("Writing to excel - done");
	}

    /**
     * Get campaign folders from MailChimp
     * @return List containing the first 100 campaign folders
     */
    public List<CampaignFolder> getCampaignFolders() throws Exception{
        return getCampaignFolders(100,0);
    }

    /**
     * Get campaign folders from MailChimp with pagination
     * @param count Number of campaign folders to return
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
     */
    public List<Campaign> getCampaigns() throws Exception {
    	return getCampaigns(100,0);
    }

    /**
     * Get campaigns from mailchimp account with pagination
     * @param count Number of campaigns to return
     * @param offset Zero based offset
     * @return List containing campaigns
     * @throws Exception
     */
    public List<Campaign> getCampaigns(int count, int offset) throws Exception {
    	List<Campaign> campaigns = new ArrayList<Campaign>();
    	// parse response
    	JSONObject jsonCampaigns = new JSONObject(do_Get(new URL(campaignendpoint+ "?offset=" + offset + "&count=" + count),getApikey()));
    	JSONArray campaignsArray = jsonCampaigns.getJSONArray("campaigns");
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
		
		campaign.put("type", type.getStringRepresentation());
		campaign.put("recipients", recipients);
		campaign.put("settings", jsonSettings);
		
		campaign = new JSONObject(do_Post(new URL(campaignendpoint), campaign.toString(), getApikey()));
		return new Campaign(this, campaign);
	}

	public Campaign createCampaign(CampaignType type, CampaignRecipients mailRecipients, CampaignSettings settings) throws Exception{
		
		JSONObject campaign = new JSONObject();
		JSONObject recipients = mailRecipients.getJsonRepresentation();
		
		JSONObject jsonSettings = settings.getJsonRepresentation();
		
		campaign.put("type", type.getStringRepresentation());
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
     * Get template folders from MailChimp
     * @return List containing the first 100 template folders
     */
	public List<TemplateFolder> getTemplateFolders() throws Exception{
        return getTemplateFolders(100,0);
	}

    /**
     * Get template folders from MailChimp with pagination
	 * @param count Number of templates to return
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
	 */
	public List<Template> getTemplates() throws Exception{
		return getTemplates(100,0);
	}

	/**
	 * Get templates from mailchimp account with pagination
	 * @param count Number of templates to return
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
	 */
	public List<Automation> getAutomations() throws Exception{
		return getAutomations(100,0);
	}
	
	/**
	 * Get a list of Automations with pagination
	 * @param count Number of templates to return
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
	 * @param count Number of emails to return
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
