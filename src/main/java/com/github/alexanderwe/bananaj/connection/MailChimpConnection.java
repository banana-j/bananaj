package com.github.alexanderwe.bananaj.connection;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import jxl.CellView;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import com.github.alexanderwe.bananaj.model.automation.Automation;
import com.github.alexanderwe.bananaj.model.automation.AutomationStatus;
import com.github.alexanderwe.bananaj.model.campaign.Campaign;
import com.github.alexanderwe.bananaj.model.campaign.CampaignDefaults;
import com.github.alexanderwe.bananaj.model.campaign.CampaignFolder;
import com.github.alexanderwe.bananaj.model.campaign.CampaignRecipients;
import com.github.alexanderwe.bananaj.model.campaign.CampaignSettings;
import com.github.alexanderwe.bananaj.model.campaign.CampaignType;
import com.github.alexanderwe.bananaj.model.list.MailChimpList;
import com.github.alexanderwe.bananaj.model.list.member.Member;
import com.github.alexanderwe.bananaj.model.template.Template;
import com.github.alexanderwe.bananaj.model.template.TemplateFolder;
import com.github.alexanderwe.bananaj.model.template.TemplateType;
import com.github.alexanderwe.bananaj.utils.DateConverter;

/**
 * Class for the com.github.alexanderwe.bananaj.connection to mailchimp servers. Used to get lists from mailchimp account.
 * @author alexanderweiss
 *
 */
public class MailChimpConnection extends Connection{

	private String server;
	private String apikey;
	private final String apiendpoint;
	private final String listendpoint;
	private final String campaignfolderendpoint;
	private final String campaignendpoint;
	private final String templatefolderendpoint;
	private final String templateendpoint;
	private final String automationendpoint;
	private final String filemanagerfolderendpoint;
	private final String filesendpoint;
	private Account account;
	
	public MailChimpConnection(String apikey){
		this.server = apikey.split("-")[1];
		this.apikey = "apikey "+apikey;
		this.apiendpoint = "https://"+server+".api.mailchimp.com/3.0/";
		this.listendpoint = "https://"+server+".api.mailchimp.com/3.0/lists";
		this.campaignfolderendpoint =  "https://"+server+".api.mailchimp.com/3.0/campaign-folders";
		this.campaignendpoint ="https://"+server+".api.mailchimp.com/3.0/campaigns";
		this.templatefolderendpoint = "https://"+server+".api.mailchimp.com/3.0/template-folders";
		this.templateendpoint = "https://"+server+".api.mailchimp.com/3.0/templates";
		this.automationendpoint = "https://"+server+".api.mailchimp.com/3.0/automations";
		this.filemanagerfolderendpoint = "https://"+server+".api.mailchimp.com/3.0/file-manager/folders";
		this.filesendpoint = "https://"+server+".api.mailchimp.com/3.0/file-manager/files";
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
			JSONObject listDetail = listsArray.getJSONObject(i);
			JSONObject listStats = listDetail.getJSONObject("stats");

			MailChimpList mailChimpList = new MailChimpList(listDetail.getString("id"),listDetail.getString("name"),listStats.getInt("member_count"),DateConverter.getInstance().createDateFromISO8601(listDetail.getString("date_created")),this,listDetail);
			mailChimpLists.add(mailChimpList);
		}
		return mailChimpLists;
	}
	
	/**
	 * Get a specific mailchimp list
	 * @return a Mailchimp list object
	 * @throws Exception
	 */
	public MailChimpList getList(String listID) throws Exception{
		JSONObject list = new JSONObject(do_Get(new URL(listendpoint +"/"+listID),getApikey()));
		JSONObject listStats = list.getJSONObject("stats");
		return new MailChimpList(list.getString("id"),list.getString("name"),listStats.getInt("member_count"),DateConverter.getInstance().createDateFromISO8601(list.getString("date_created")),this,list);
	}


	/**
	 * Create a new list in your mailchimp account
	 * @param listName
	 */
	public void createList(String listName, String permission_reminder, boolean email_type_option, CampaignDefaults campaignDefaults) throws Exception{
		setAccount();
		JSONObject jsonList = new JSONObject();
		
		JSONObject contact = new JSONObject();
		contact.put("company", account.getCompany());
		contact.put("address1", account.getAddress1());
		contact.put("city", account.getCity());
		contact.put("state", account.getState());
		contact.put("zip", account.getZip());
		contact.put("country", account.getCountry());
		
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

		do_Post(new URL(listendpoint), jsonList.toString(),getApikey());
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

				Iterator<Entry<String, String>> iter = members.get(0).getMerge_fields().entrySet().iterator();
				while (iter.hasNext()) {
					Entry<String, String> pair = iter.next();
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
				sheet.addCell(new Label(1,i+1,member.getEmail_address()));
				sheet.addCell(new Label(2,i+1,member.getTimestamp_signup()));
				sheet.addCell(new Label(3,i+1,member.getIp_signup()));
				sheet.addCell(new Label(4,i+1,member.getTimestamp_opt()));
				sheet.addCell(new Label(5,i+1,member.getIp_opt()));
				sheet.addCell(new Label(6,i+1,member.getStatus().getStringRepresentation()));
				sheet.addCell(new Number(7,i+1,member.getAvg_open_rate()));
				sheet.addCell(new Number(8,i+1,member.getAvg_click_rate()));

				if (show_merge){
					//add merge fields values
					int last_index = 9;
					Iterator<Entry<String, String>> iter_member = member.getMerge_fields().entrySet().iterator();
					while (iter_member.hasNext()) {
						Entry<String, String> pair = iter_member.next();
						sheet.addCell(new Label(last_index,i+1,pair.getValue()));
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
    		CampaignFolder campaignFolder = new CampaignFolder(campaignFolderJSON.getString("id"),
    				campaignFolderJSON.getString("name"),
    				campaignFolderJSON.getInt("count"),
    				campaignFolderJSON);
    		campaignFolders.add(campaignFolder);
    	}
    	return campaignFolders;
    }

    /**
     * Get a specific template folder
     * @param folder_id
     * @return
     */
    public CampaignFolder getCampaignFolder(String folder_id) throws Exception{

    	JSONObject campaignFoldersResponse = new JSONObject(do_Get(new URL(campaignfolderendpoint +"/"+folder_id), getApikey()));

    	return new CampaignFolder(campaignFoldersResponse.getString("id"),
    			campaignFoldersResponse.getString("name"),
    			campaignFoldersResponse.getInt("count"),
    			campaignFoldersResponse);
    }

    /**
     * Add a template folder with a specific name
     * @param name
     */
    public void addCampaignFolder(String name) throws Exception{
    	JSONObject campaignFolder = new JSONObject();
    	campaignFolder.put("name", name);
    	do_Post(new URL(campaignfolderendpoint), campaignFolder.toString(), getApikey());
    }

    /**
     * Delete a specific template folder
     * @param folder_id
     */
    public void deleteCampaignFolder(String folder_id) throws Exception{
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
     *  * TODO add campaignsettings
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
	 * TODO add campaignsettings
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
		
		JSONObject jsonSettings = new JSONObject();
		put(jsonSettings, "subject_line", settings.getSubject_line());
		put(jsonSettings, "title", settings.getTitle());
		put(jsonSettings, "to_name", settings.getTo_name());
		put(jsonSettings, "from_name", settings.getFrom_name());
		put(jsonSettings, "reply_to", settings.getReply_to());
		if(settings.getTemplate_id() != 0 ) {
			jsonSettings.put("template_id", settings.getTemplate_id());
		}
		put(jsonSettings, "auto_footer", settings.getAuto_footer());
		put(jsonSettings, "use_conversation", settings.getUse_conversation());
		put(jsonSettings, "authenticate", settings.getAuthenticate());
		put(jsonSettings, "timewarp", settings.getTimewarp());
		put(jsonSettings, "auto_tweet", settings.getAuto_tweet());
		put(jsonSettings, "fb_comments", settings.getFb_comments());
		put(jsonSettings, "drag_and_drop", settings.getDrag_and_drop());
		put(jsonSettings, "inline_css", settings.getInline_css());
		put(jsonSettings, "folder_id", settings.getFolder_id());
		
		campaign.put("type", type.getStringRepresentation());
		campaign.put("recipients", recipients);
		campaign.put("settings", jsonSettings);
		
		campaign = new JSONObject(do_Post(new URL(campaignendpoint), campaign.toString(), getApikey()));
		return new Campaign(this, campaign);
	}

	public Campaign createCampaign(CampaignType type, CampaignRecipients mailRecipients, CampaignSettings settings) throws Exception{
		
		JSONObject campaign = new JSONObject();
		JSONObject recipients = mailRecipients.getJsonRepresentation();
		
		JSONObject jsonSettings = new JSONObject();
		put(jsonSettings, "subject_line", settings.getSubject_line());
		put(jsonSettings, "title", settings.getTitle());
		put(jsonSettings, "to_name", settings.getTo_name());
		put(jsonSettings, "from_name", settings.getFrom_name());
		put(jsonSettings, "reply_to", settings.getReply_to());
		if(settings.getTemplate_id() != 0 ) {
			jsonSettings.put("template_id", settings.getTemplate_id());
		}
		put(jsonSettings, "auto_footer", settings.getAuto_footer());
		put(jsonSettings, "use_conversation", settings.getUse_conversation());
		put(jsonSettings, "authenticate", settings.getAuthenticate());
		put(jsonSettings, "timewarp", settings.getTimewarp());
		put(jsonSettings, "auto_tweet", settings.getAuto_tweet());
		put(jsonSettings, "fb_comments", settings.getFb_comments());
		put(jsonSettings, "drag_and_drop", settings.getDrag_and_drop());
		put(jsonSettings, "inline_css", settings.getInline_css());
		put(jsonSettings, "folder_id", settings.getFolder_id());
		
		campaign.put("type", type.getStringRepresentation());
		campaign.put("recipients", recipients);
		campaign.put("settings", jsonSettings);
		
		campaign = new JSONObject(do_Post(new URL(campaignendpoint), campaign.toString(), getApikey()));
		return new Campaign(this, campaign);
	}
	
	private JSONObject put(JSONObject settings, String key, String value) {
		if (value != null) {
			return settings.put(key, value);
		}
		return settings;
	}

	private JSONObject put(JSONObject settings, String key, Boolean value) {
		if (value != null) {
			return settings.put(key, value);
		}
		return settings;
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

        JSONArray templateFoldersJSON = templateFoldersResponse.getJSONArray("folders");

        for(int i = 0 ; i < templateFoldersJSON.length(); i++){
            JSONObject templateFolderJSON = templateFoldersJSON.getJSONObject(i);
            TemplateFolder templateFolder = new TemplateFolder(templateFolderJSON.getString("id"),
                    templateFolderJSON.getString("name"),
                    templateFolderJSON.getInt("count"),
                    templateFolderJSON);
            templateFolders.add(templateFolder);
        }
        return templateFolders;
	}

    /**
     * Get a specific template folder
     * @param folder_id
     * @return
     */
    public TemplateFolder getTemplateFolder(String folder_id) throws Exception{

        JSONObject templateFoldersResponse = new JSONObject(do_Get(new URL(templatefolderendpoint +"/"+folder_id), getApikey()));

        return new TemplateFolder(templateFoldersResponse.getString("id"),
                templateFoldersResponse.getString("name"),
                templateFoldersResponse.getInt("count"),
                templateFoldersResponse);
    }

    /**
     * Add a template folder with a specific name
     * @param name
     */
    public void addTemplateFolder(String name) throws Exception{
        JSONObject templateFolder = new JSONObject();
        templateFolder.put("name", name);
        do_Post(new URL(templatefolderendpoint), templateFolder.toString(), getApikey());
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
			JSONObject templatesDetail = templatesArray.getJSONObject(i);

			Template template = new Template(templatesDetail.getInt("id"),
					templatesDetail.getString("name"),
					TemplateType.valueOf(templatesDetail.getString("type").toUpperCase()),
					templatesDetail.getString("share_url"),
					DateConverter.getInstance().createDateFromISO8601(templatesDetail.getString("date_created")),
					templatesDetail.has("folder_id") ? templatesDetail.getString("folder_id") : null, 
					this,
					templatesDetail);
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
		Template template = new Template(jsonTemplate.getInt("id"),
				jsonTemplate.getString("name"),
				TemplateType.valueOf(jsonTemplate.getString("type").toUpperCase()),
				jsonTemplate.getString("share_url"),
				DateConverter.getInstance().createDateFromISO8601(jsonTemplate.getString("date_created")),
				jsonTemplate.has("folder_id") ? jsonTemplate.getString("folder_id") : null,
				this,
				jsonTemplate);
		return template;
	}

	/**
	 * Add a template to your MailChimp account
	 * @param name
	 * @param html
	 * @throws Exception
	 */
	public void addTemplate(String name, String html) throws Exception{
		JSONObject templateJSON = new JSONObject();
		templateJSON.put("name", name);
		templateJSON.put("html", html);
		do_Post(new URL(templateendpoint +"/"), templateJSON.toString(),getApikey());
	}

	/**
	 * Add a template to a specific folder to your MailChimp Account
	 * @param name
	 * @param folder_id
	 * @param html
	 * @throws Exception
	 */
	public void addTemplate(String name, String folder_id, String html) throws Exception{
		JSONObject templateJSON = new JSONObject();
		templateJSON.put("name", name);
		templateJSON.put("folder_id", folder_id);
		templateJSON.put("html", html);
		do_Post(new URL(templateendpoint +"/"), templateJSON.toString(),getApikey());
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
	 * Get automations from mailchimp account
	 * @return List containing the first 100 automations
	 * @throws Exception
	 */
	public List<Automation> getAutomations() throws Exception{
		return getAutomations(100,0);
	}
	
	/**
	 * Get all automations from mailchimp account with pagination
	 * @param count Number of templates to return
	 * @param offset Zero based offset
	 * @return List containing automations
	 * @throws Exception
	 */
	public List<Automation> getAutomations(int count, int offset) throws Exception{
		List<Automation> automations = new ArrayList<Automation>();

		JSONObject jsonAutomations = new JSONObject(do_Get(new URL(automationendpoint + "?offset=" + offset + "&count=" + count),getApikey()));
		JSONArray automationsArray = jsonAutomations.getJSONArray("automations");
		for( int i = 0; i< automationsArray.length();i++)
		{
			JSONObject automationDetail = automationsArray.getJSONObject(i);
			JSONObject recipients = automationDetail.getJSONObject("recipients");

			Automation automation = new Automation(automationDetail.getString("id"), DateConverter.getInstance().createDateFromISO8601(automationDetail.getString("create_time")),DateConverter.getInstance().createDateFromISO8601(automationDetail.getString("start_time")),AutomationStatus.valueOf(automationDetail.getString("status").toUpperCase()),automationDetail.getInt("emails_sent"),getList(recipients.getString("list_id")),automationDetail);
			automations.add(automation);
		}
		return automations;
	}
	
	/**
	 * Get an specific automation
	 * @param id
	 * @return an Automation object
	 * @throws Exception
	 */
	public Automation getAutomation(String id) throws Exception{
		JSONObject jsonAutomation = new JSONObject(do_Get(new URL(automationendpoint +"/"+id),getApikey()));
		JSONObject recipients = jsonAutomation.getJSONObject("recipients");
		return new Automation(jsonAutomation.getString("id"),DateConverter.getInstance().createDateFromISO8601(jsonAutomation.getString("create_time")),DateConverter.getInstance().createDateFromISO8601(jsonAutomation.getString("start_time")),AutomationStatus.valueOf(jsonAutomation.getString("status").toUpperCase()),jsonAutomation.getInt("emails_sent"),getList(recipients.getString("list_id")),jsonAutomation);
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
		return this.apikey;
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

	/**
	 * @return the account
	 * @throws Exception 
	 */
	public Account getAccount() throws Exception {
		setAccount();
		return account;
	}

	/**
	 * Set the account of this com.github.alexanderwe.bananaj.connection.
	 */
	private void setAccount() throws Exception {
		if (account == null) {
			synchronized(this) {
				if (account == null) {
					Account account;
					JSONObject jsonAPIROOT = new JSONObject(do_Get(new URL(apiendpoint),getApikey()));
					JSONObject contact = jsonAPIROOT.getJSONObject("contact");
					account = new Account(this, jsonAPIROOT.getString("account_id"),
							jsonAPIROOT.getString("account_name"),
							contact.getString("company"),
							contact.getString("addr1"),
							contact.getString("addr2"),
							contact.getString("city"),
							contact.getString("state"),
							contact.getString("zip"),
							contact.getString("country"),
							DateConverter.getInstance().createDateFromISO8601(jsonAPIROOT.getString("last_login")),
							jsonAPIROOT.getInt("total_subscribers"),
							jsonAPIROOT);
					this.account = account;
				}
			}
		}
	}
}
