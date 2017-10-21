/**
 * @author alexanderweiss
 * @date 15.11.2015
 */
package connection;

import jxl.CellView;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.Number;
import model.automation.Automation;
import model.automation.AutomationStatus;
import model.campaign.*;
import model.list.MailChimpList;
import model.list.member.Member;
import model.template.Template;
import model.template.TemplateFolder;
import model.template.TemplateType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.DateConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


/**
 * Class for the connection to mailchimp servers. Used to get lists from mailchimp account.
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
		try {
			setAccount();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
	 * Get all lists in your account
	 * @return Arraylist containing all lists
	 * @throws Exception
	 */
	public ArrayList<MailChimpList> getLists() throws Exception{
		ArrayList<MailChimpList> mailChimpLists = new ArrayList<MailChimpList>();
		// parse response
		JSONObject jsonLists = new JSONObject(do_Get(new URL(listendpoint),getApikey()));
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
		URL url = new URL(listendpoint);
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

		ArrayList<MailChimpList> mailChimpLists = getLists();
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

			ArrayList<Member> members = mailChimpList.getMembers(0,0);
			int merge_field_count = 0;

			if (show_merge){
				int last_column = 9;

				Iterator iter = members.get(0).getMerge_fields().entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry pair = (Map.Entry)iter.next();
					sheet.addCell(new Label(last_column,0,(String)pair.getKey(),times16format));
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
					Iterator iter_member = member.getMerge_fields().entrySet().iterator();
					while (iter_member.hasNext()) {
						Map.Entry pair = (Map.Entry)iter_member.next();
						sheet.addCell(new Label(last_index,i+1,(String)pair.getValue()));
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
     * Get all template folders from MailChimp
     * @return
     */
    public ArrayList<CampaignFolder> getCampaignFolders() throws Exception{
        ArrayList<CampaignFolder> campaignFolders = new ArrayList<>();
        JSONObject campaignFoldersResponse = new JSONObject(do_Get(new URL(campaignfolderendpoint), getApikey()));

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
	 * Get all camapaigns from mailchimp account
	 * @return Arraylist containing all campaigns
	 * @throws Exception
	 *  * TODO add campaignsettings
	 */
	public ArrayList<Campaign> getCampaigns() throws Exception{
		ArrayList<Campaign> campaigns = new ArrayList<Campaign>();
		// parse response
		JSONObject jsonCampaigns = new JSONObject(do_Get(new URL(campaignendpoint),getApikey()));
		JSONArray campaignsArray = jsonCampaigns.getJSONArray("campaigns");
		for( int i = 0; i< campaignsArray.length();i++)
		{
			JSONObject campaignDetail = campaignsArray.getJSONObject(i);
			JSONObject campaignSettings = campaignDetail.getJSONObject("settings");
			JSONObject recipients = campaignDetail.getJSONObject("recipients");
			String campaignType = campaignDetail.getString("type");
			String campaignStatus = campaignDetail.getString("status");

			Campaign campaign;
			try{
				campaign = new Campaign(campaignDetail.getString("id"),
						getList(recipients.getString("list_id")),
						CampaignType.valueOf(campaignType.toUpperCase()),
						CampaignStatus.valueOf(campaignStatus.toUpperCase()),
						new CampaignSettings(campaignSettings.getString("subject_line"),
								campaignSettings.getString("title"),
								campaignSettings.getString("from_name"),
								campaignSettings.getString("reply_to"),
								campaignDetail.getString("id"),
								this),
						this,
						campaignDetail);
			}catch (FileNotFoundException fnfe){ // If list to campaign is deleted then just a null reference to list is added
                campaign = new Campaign(campaignDetail.getString("id"),
						null,
						CampaignType.valueOf(campaignType.toUpperCase()),
						CampaignStatus.valueOf(campaignStatus.toUpperCase()),
						new CampaignSettings(campaignSettings.getString("subject_line"),
								campaignSettings.getString("title"),
								campaignSettings.getString("from_name"),
								campaignSettings.getString("reply_to"),
								campaignDetail.getString("id"),
								this),
						this,
						campaignDetail);
			}
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
	public Campaign getCampaign(String campaignID) throws Exception{
		JSONObject campaign = new JSONObject(do_Get(new URL(campaignendpoint +"/"+campaignID),getApikey()));
		JSONObject recipients = campaign.getJSONObject("recipients");
		JSONObject campaignSettings = campaign.getJSONObject("settings");
		String campaignType = campaign.getString("type");
		String campaignStatus = campaign.getString("status");

        try{
            return new Campaign(campaign.getString("id"),
					getList(recipients.getString("list_id")),
					CampaignType.valueOf(campaignType.toUpperCase()),
					CampaignStatus.valueOf(campaignStatus.toUpperCase()),
					new CampaignSettings(campaignSettings.getString("subject_line"),
							campaignSettings.getString("title"),
							campaignSettings.getString("from_name"),
							campaignSettings.getString("reply_to"),
							campaign.getString("id"),
							this),
					this,
					campaign);
        }catch(FileNotFoundException fnfe){
            return new Campaign(campaign.getString("id"),
					getList(recipients.getString("list_id")),
					CampaignType.valueOf(campaignType.toUpperCase()),
					CampaignStatus.valueOf(campaignStatus.toUpperCase()),
					new CampaignSettings(campaignSettings.getString("subject_line"),
							campaignSettings.getString("title"),
							campaignSettings.getString("from_name"),
							campaignSettings.getString("reply_to"),
							campaign.getString("id"),
							this),
					this,
					campaign);
        }
	}

	/**
	 * Create a new campaign in your mailchimp account
	 * @param type
	 * @param mailChimpList
	 * @param settings
	 */
	public void createCampaign(CampaignType type, MailChimpList mailChimpList, CampaignSettings settings) throws Exception{
		
		JSONObject campaign = new JSONObject();
		
		JSONObject recipients = new JSONObject();
		recipients.put("list_id", mailChimpList.getId());
		
		JSONObject jsonSettings = new JSONObject();
		jsonSettings.put("subject_line", settings.getSubject_line());
		jsonSettings.put("from_name", settings.getFrom_name());
		jsonSettings.put("reply_to", settings.getReply_to());
	
		
		campaign.put("type", type.getStringRepresentation());
		campaign.put("recipients", recipients);
		campaign.put("settings", jsonSettings);
		
        do_Post(new URL(campaignendpoint), campaign.toString(),getApikey());
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
     * Get all template folders from MailChimp
     * @return
     */
	public ArrayList<TemplateFolder> getTemplateFolders() throws Exception{
        ArrayList<TemplateFolder> templateFolders = new ArrayList<>();
        JSONObject templateFoldersResponse = new JSONObject(do_Get(new URL(templatefolderendpoint), getApikey()));

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
	 * Get all templates from mailchimp account
	 * @return Arraylist containing all templates
	 * @throws Exception
	 */
	public ArrayList<Template> getTemplates() throws Exception{
		ArrayList<Template> templates = new ArrayList<Template>();

		JSONObject jsonTemplates = new JSONObject(do_Get(new URL(templateendpoint),getApikey()));
		JSONArray templatesArray = jsonTemplates.getJSONArray("templates");
		for( int i = 0; i< templatesArray.length();i++)
		{
			JSONObject templatesDetail = templatesArray.getJSONObject(i);

			Template template;
			try{
				template = new Template(templatesDetail.getInt("id"),
						templatesDetail.getString("name"),
						TemplateType.valueOf(templatesDetail.getString("type").toUpperCase()),
						templatesDetail.getString("share_url"),
						DateConverter.getInstance().createDateFromISO8601(templatesDetail.getString("date_created")),
						templatesDetail.getString("folder_id"),
						this,templatesDetail);
			} catch (JSONException jsone){
				template = new Template(templatesDetail.getInt("id"),
						templatesDetail.getString("name"),
						TemplateType.valueOf(templatesDetail.getString("type").toUpperCase()),
						templatesDetail.getString("share_url"),
						DateConverter.getInstance().createDateFromISO8601(templatesDetail.getString("date_created")),
						null,
						this,templatesDetail);
			}
			templates.add(template);
		}
		return templates;
	}

	/**
	 * Get a template fom mailchimp account
	 * @param id
	 * @return a template object
	 * @throws Exception
	 */
	public Template getTemplate(String id) throws Exception{
		JSONObject jsonTemplate = new JSONObject(do_Get(new URL(templateendpoint +"/" +id),getApikey()));
		Template template;

		try{
			template = new Template(jsonTemplate.getInt("id"),
					jsonTemplate.getString("name"),
					TemplateType.valueOf(jsonTemplate.getString("type").toUpperCase()),
					jsonTemplate.getString("share_url"),
					DateConverter.getInstance().createDateFromISO8601(jsonTemplate.getString("date_created")),
					jsonTemplate.getString("folder_id"),
					this,jsonTemplate);
		} catch (JSONException jsone){ //No folder_id found
			template = new Template(jsonTemplate.getInt("id"),
					jsonTemplate.getString("name"),
					TemplateType.valueOf(jsonTemplate.getString("type").toUpperCase()),
					jsonTemplate.getString("share_url"),
					DateConverter.getInstance().createDateFromISO8601(jsonTemplate.getString("date_created")),
					null,
					this,jsonTemplate);
		}
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
	 * Get all autmations from mailchimp account
	 * @return Arraylist containing all automations
	 * @throws Exception
	 */
	public ArrayList<Automation> getAutomations() throws Exception{
		ArrayList<Automation> automations = new ArrayList<Automation>();

		JSONObject jsonAutomations = new JSONObject(do_Get(new URL(automationendpoint),getApikey()));
		JSONArray automatationsArray = jsonAutomations.getJSONArray("automations");
		for( int i = 0; i< automatationsArray.length();i++)
		{
			JSONObject automationDetail = automatationsArray.getJSONObject(i);
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
		Automation automation = new Automation(jsonAutomation.getString("id"),DateConverter.getInstance().createDateFromISO8601(jsonAutomation.getString("create_time")),DateConverter.getInstance().createDateFromISO8601(jsonAutomation.getString("start_time")),AutomationStatus.valueOf(jsonAutomation.getString("status").toUpperCase()),jsonAutomation.getInt("emails_sent"),getList(recipients.getString("list_id")),jsonAutomation);
		
		return automation;
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
	 */
	public Account getAccount() {
		return this.account;
	}

	/**
	 * Set the account of this connection.
	 */
	private void setAccount() throws Exception {
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
