/**
 * @author alexanderweiss
 * @date 15.11.2015
 */
package connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import model.filemanager.FileManagerFolder;
import org.json.JSONArray;
import org.json.JSONObject;

import model.automation.Automation;
import model.automation.AutomationStatus;
import model.campaign.Campaign;
import model.campaign.CampaignDefaults;
import model.campaign.CampaignSettings;
import model.campaign.CampaignStatus;
import model.campaign.CampaignType;
import model.list.List;
import model.list.member.Member;
import model.template.Template;
import model.template.TemplateType;
import jxl.CellView;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


/**
 * Class for the connection to mailchimp servers. Used to get lists from mailchimp account.
 * @author alexanderweiss
 *
 */
public class MailchimpConnection {

	private String server;
	private String apikey;
	private String APIENDPOINT;
	private String LISTENDPOINT;
	private String CAMPAIGNENDPOINT;
	private String TEMPLATEENDPOINT;
	private String AUTOMATIONENDPOINT;
	private String FILEMANAGERFOLDERENDPOINT;
	private String FILESENDPOINT;
	private Account account;
	
	public MailchimpConnection(String apikey){
		
		setServer(apikey.split("-")[1]);
		setApikey(apikey);
		setAPIENDPOINT("https://"+server+".api.mailchimp.com/3.0/");
		setLISTENDPOINT("https://"+server+".api.mailchimp.com/3.0/lists");
		setCAMPAIGNENDPOINT("https://"+server+".api.mailchimp.com/3.0/campaigns");
		setTEMPLATEENDPOINT("https://"+server+".api.mailchimp.com/3.0/templates");
		setAUTOMATIONENDPOINT("https://"+server+".api.mailchimp.com/3.0/automations");
		setFILEMANAGERFOLDERENDPOINT("https://"+server+".api.mailchimp.com/3.0/file-manager/folders");
		setFILESENDPOINT("https://"+server+".api.mailchimp.com/3.0/file-manager/files");
		try {
			setAccount();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public String do_Get(URL url) throws Exception{
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("Authorization",this.getApikey());

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode+"\n");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
    public String do_Post(URL url, String post_string) throws Exception{

        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestProperty("Authorization", getApikey());
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        // Indicate that we want to write to the HTTP request body
        con.setDoOutput(true);
        con.setRequestMethod("POST");

        // Writing the post data to the HTTP request body
        BufferedWriter httpRequestBodyWriter = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
        httpRequestBodyWriter.write(post_string);
        httpRequestBodyWriter.close();

        // Reading from the HTTP response body
        Scanner httpResponseScanner = new Scanner(con.getInputStream());
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = httpResponseScanner.nextLine()) != null) {
            response.append(inputLine);
        }
        httpResponseScanner.close();
        return response.toString();
    }
    public String do_Post(URL url) throws Exception{
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        // optional default is GET
        con.setRequestMethod("POST");

        //add request header
        con.setRequestProperty("Authorization",getApikey());

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Response Code : " + responseCode+"\n");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
    public String do_Delete(URL url) throws Exception{
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");
        con.setRequestProperty("Authorization", getApikey());

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'DELETE' request to URL : " + url);
        System.out.println("Response Code : " + responseCode+"\n");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
    /**
	 * Get all lists in your account
	 * @return Arraylist containing all lists
	 * @throws Exception
	 */
	public ArrayList<List> getLists() throws Exception{
		ArrayList<List> lists = new ArrayList<List>();
		// parse response
		JSONObject jsonLists = new JSONObject(do_Get(new URL(LISTENDPOINT)));
		JSONArray listsArray = jsonLists.getJSONArray("lists");
		for( int i = 0; i< listsArray.length();i++)
		{
			JSONObject listDetail = listsArray.getJSONObject(i);
			JSONObject listStats = listDetail.getJSONObject("stats");

			List list = new List(listDetail.getString("id"),listDetail.getString("name"),listStats.getInt("member_count"),this.createDateFromISO8601(listDetail.getString("date_created")),this,listDetail);
			lists.add(list);
		}
		return lists;
	}

    /**
	 * Get a specific mailchimp list
	 * @return a Mailchimp list object
	 * @throws Exception
	 */
	public List getList(String listID) throws Exception{
		JSONObject list = new JSONObject(do_Get(new URL(LISTENDPOINT+"/"+listID)));
		JSONObject listStats = list.getJSONObject("stats");
		return new List(list.getString("id"),list.getString("name"),listStats.getInt("member_count"),this.createDateFromISO8601(list.getString("date_created")),this,list);
	}


	/**
	 * Create a new list in your mailchimp account
	 * @param listName
	 */
	public void createList(String listName, String permission_reminder, boolean email_type_option, CampaignDefaults campaignDefaults) throws Exception{
		URL url = new URL(LISTENDPOINT);		
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
		
		
	    do_Post(new URL(LISTENDPOINT), jsonList.toString());
	}

	/**
	 * Delete a list from your account
	 * @param listID
	 * @throws Exception
	 */
	public void deleteList(String listID) throws Exception{
        do_Delete(new URL(LISTENDPOINT+"/"+listID));
	}

	/**
	 * Write all lists to an Excel file
	 * @throws Exception
	 */
	public void writeAllListToExcel() throws Exception{
		WritableWorkbook workbook = Workbook.createWorkbook(new File("listdata_allLists.xls"));
		WritableFont times16font = new WritableFont(WritableFont.TIMES, 16, WritableFont.BOLD, false);
		WritableCellFormat times16format = new WritableCellFormat (times16font);


		ArrayList<List> lists = getLists();
		int index  = 0;
		for(List list:lists){
			WritableSheet sheet = workbook.createSheet(list.getName(), index);

			Label memberIDLabel = new Label(0, 0, "MemberID",times16format);
			Label fnameLabel = new Label(1, 0, "Vorname",times16format);
			Label lnameLabel = new Label(2,0,"Nachname",times16format);
			Label email_addressLabel = new Label(3,0,"Email Addresse",times16format);
			Label timestamp_sign_inLabel = new Label(4,0,"Sign up",times16format);
			Label timestapm_opt_inLabel = new Label(5,0,"Opt in",times16format);
			Label statusLabel = new Label(6,0,"Status",times16format);
			Label avg_open_rateLabel = new Label(7,0,"Avg. open rate",times16format);
			Label avg_click_rateLabel = new Label(8,0,"Avg. click rate",times16format);

			sheet.addCell(memberIDLabel);
			sheet.addCell(fnameLabel);
			sheet.addCell(lnameLabel);
			sheet.addCell(email_addressLabel);
			sheet.addCell(timestamp_sign_inLabel);
			sheet.addCell(timestapm_opt_inLabel);
			sheet.addCell(statusLabel);
			sheet.addCell(avg_open_rateLabel);
			sheet.addCell(avg_click_rateLabel);

			ArrayList<Member> members = list.getMembers();
			for(int i = 0 ; i<members.size();i++)
			{
				Member member = members.get(i);
				sheet.addCell(new Label(0,i+1,member.getId()));
				sheet.addCell(new Label(1,i+1,member.getFNAME()));
				sheet.addCell(new Label(2,i+1,member.getLNAME()));
				sheet.addCell(new Label(3,i+1,member.getEmail_address()));
				sheet.addCell(new Label(4,i+1,member.getTimestamp_signup()));
				sheet.addCell(new Label(5,i+1,member.getTimestamp_opt()));
				sheet.addCell(new Label(6,i+1,member.getStatus().getStringRepresentation()));
				sheet.addCell(new Number(7,i+1,member.getAvg_open_rate()));
				sheet.addCell(new Number(8,i+1,member.getAvg_click_rate()));
			}

			CellView cell;
			for(int x=0;x<9;x++)
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
	 * Get all camapaigns from mailchimp account
	 * @return Arraylist containing all campaigns
	 * @throws Exception
	 */
	public ArrayList<Campaign> getCampaigns() throws Exception{
		ArrayList<Campaign> campaigns = new ArrayList<Campaign>();
		// parse response
		JSONObject jsonCampaigns = new JSONObject(do_Get(new URL(CAMPAIGNENDPOINT)));
		JSONArray campaignsArray = jsonCampaigns.getJSONArray("campaigns");
		for( int i = 0; i< campaignsArray.length();i++)
		{
			JSONObject campaignDetail = campaignsArray.getJSONObject(i);
			JSONObject campaignSettings = campaignDetail.getJSONObject("settings");
			JSONObject recipients = campaignDetail.getJSONObject("recipients");
			String campaignType = campaignDetail.getString("type");
			String campaignStatus = campaignDetail.getString("status");


			Campaign campaign = new Campaign(campaignDetail.getString("id"),campaignSettings.getString("title"),getList(recipients.getString("list_id")),this.translateStringIntoCampaignType(campaignType),this.translateStringIntoCampaignStatus(campaignStatus),this,campaignDetail);
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
	public Campaign getCampaign(String campaignID) throws Exception{
		URL url = new URL(CAMPAIGNENDPOINT+"/"+campaignID);
		// parse response
		JSONObject campaign = new JSONObject(do_Get(new URL(CAMPAIGNENDPOINT+"/"+campaignID)));
		JSONObject recipients = campaign.getJSONObject("recipients");
		JSONObject campaignSettings = campaign.getJSONObject("settings");
		String campaignType = campaign.getString("type");
		String campaignStatus = campaign.getString("status");

		return new Campaign(campaign.getString("id"), campaignSettings.getString("title"),getList(recipients.getString("list_id")),this.translateStringIntoCampaignType(campaignType),this.translateStringIntoCampaignStatus(campaignStatus),this,campaign);
	}

	/**
	 * Create a new campaign in your mailchimp account
	 * @param type
	 * @param list
	 * @param settings
	 */
	public void createCampaign(CampaignType type, List list, CampaignSettings settings) throws Exception{
		URL url = new URL(CAMPAIGNENDPOINT);		
		
		JSONObject campaign = new JSONObject();
		
		JSONObject recipients = new JSONObject();
		recipients.put("list_id", list.getId());
		
		JSONObject jsonSettings = new JSONObject();
		jsonSettings.put("subject_line", settings.getSubject_line());
		jsonSettings.put("from_name", settings.getFrom_name());
		jsonSettings.put("reply_to", settings.getReply_to());
	
		
		campaign.put("type", type.getStringRepresentation());
		campaign.put("recipients", recipients);
		campaign.put("settings", jsonSettings);
		
        do_Post(new URL(CAMPAIGNENDPOINT), campaign.toString());
	}

	/**
	 * Delete a campaign from mailchimp account
	 * @param campaignID
	 * @throws Exception
	 */
	public void deleteCampaign(String campaignID) throws Exception{
		do_Delete(new URL(CAMPAIGNENDPOINT+"/"+campaignID));
	}

	/**
	 * Get all templates from mailchimp account
	 * @return Arraylist containing all templates
	 * @throws Exception
	 */
	public ArrayList<Template> getTemplates() throws Exception{
		ArrayList<Template> templates = new ArrayList<Template>();

		JSONObject jsonTemplates = new JSONObject(do_Get(new URL(TEMPLATEENDPOINT)));
		JSONArray templatesArray = jsonTemplates.getJSONArray("templates");
		for( int i = 0; i< templatesArray.length();i++)
		{
			JSONObject templatesDetail = templatesArray.getJSONObject(i);


			Template template = new Template(templatesDetail.getInt("id"),templatesDetail.getString("name"),translateStringIntoTemplateType(templatesDetail.getString("type")),templatesDetail.getString("share_url"),createDateFromNormal(templatesDetail.getString("date_created")),templatesDetail);
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
		JSONObject jsonTemplate = new JSONObject(do_Get(new URL(TEMPLATEENDPOINT+"/" +id)));
		Template template = new Template(jsonTemplate.getInt("id"),jsonTemplate.getString("name"),translateStringIntoTemplateType(jsonTemplate.getString("type")),jsonTemplate.getString("share_url"),createDateFromNormal(jsonTemplate.getString("date_created")),jsonTemplate);
		return template;
	}

	/**
	 * Delete a specific template
	 * @param id
	 * @throws Exception
	 */
	public void deleteTemplate(String id) throws Exception {
		do_Delete(new URL(TEMPLATEENDPOINT+"/" +id));
	}
	
	
	/**
	 * Get all autmations from mailchimp account
	 * @return Arraylist containing all automations
	 * @throws Exception
	 */
	public ArrayList<Automation> getAutomations() throws Exception{
		ArrayList<Automation> automations = new ArrayList<Automation>();

		JSONObject jsonAutomations = new JSONObject(do_Get(new URL(AUTOMATIONENDPOINT)));
		JSONArray automationsArray = jsonAutomations.getJSONArray("automations");
		for( int i = 0; i< automationsArray.length();i++)
		{
			JSONObject automationDetail = automationsArray.getJSONObject(i);
			JSONObject recipients = automationDetail.getJSONObject("recipients");

			Automation automation = new Automation(automationDetail.getString("id"),createDateFromISO8601(automationDetail.getString("create_time")),createDateFromISO8601(automationDetail.getString("start_time")),translateStringIntoAutomationStatus(automationDetail.getString("status")),automationDetail.getInt("emails_sent"),getList(recipients.getString("list_id")),automationDetail);
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
		JSONObject jsonAutomation = new JSONObject(do_Get(new URL(AUTOMATIONENDPOINT+"/"+id)));
		JSONObject recipients = jsonAutomation.getJSONObject("recipients");
		Automation automation = new Automation(jsonAutomation.getString("id"),createDateFromISO8601(jsonAutomation.getString("create_time")),createDateFromISO8601(jsonAutomation.getString("start_time")),translateStringIntoAutomationStatus(jsonAutomation.getString("status")),jsonAutomation.getInt("emails_sent"),getList(recipients.getString("list_id")),jsonAutomation);
		
		return automation;
	}

	/**
	 * Get all file manager folders in mailchimp account account
	 * @return
	 * @throws Exception
     */
	public ArrayList<FileManagerFolder> getFileManagerFolders() throws Exception{
		ArrayList<FileManagerFolder> fileManagerFolders = new ArrayList<FileManagerFolder>();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		JSONObject jsonFileManagerFolders = new JSONObject(do_Get(new URL(FILEMANAGERFOLDERENDPOINT)));
		JSONArray folderArray = jsonFileManagerFolders.getJSONArray("folders");
		for( int i = 0; i< folderArray.length();i++)
		{
			JSONObject folderDetail = folderArray.getJSONObject(i);
			FileManagerFolder folder = new FileManagerFolder(folderDetail.getInt("id"),folderDetail.getString("name"),folderDetail.getInt("file_count"),folderDetail.getString("created_at"), folderDetail.getString("created_by"),folderDetail,this);
			fileManagerFolders.add(folder);
		}
		return fileManagerFolders;
	}

	/**
	 * Convert a string containing the type of a campaign in CompaignType enum
	 * @param campaignType
	 * @return
	 */
	private CampaignType translateStringIntoCampaignType(String campaignType){
		switch(campaignType){
		case "regular": return CampaignType.REGULAR;
		case "plaintext": return CampaignType.PLAINTEXT;
		case "absplit": return CampaignType.ABSPLIT;
		case "rss":  return CampaignType.RSS;
		case "variate": return CampaignType.VARIATE;
		default:return null;
		}
	}

	/**
	 * Convert a string containing the type of a tempalte in TemplateType enum
	 * @param templateType
	 * @return
	 */
	private TemplateType translateStringIntoTemplateType(String templateType){
		switch(templateType){
		case "user": return TemplateType.USER;
		case "base": return TemplateType.BASE;
		case "gallery": return TemplateType.GALLERY;
		default:return null;
		}
	}

	/**
	 * Convert a string containing the status of a campaign in CompaignStatus enum
	 * @param campaignStatus
	 * @return
	 */
	private CampaignStatus translateStringIntoCampaignStatus(String campaignStatus){
		switch(campaignStatus){
		case "save": return CampaignStatus.SAVE;
		case "paused": return CampaignStatus.PAUSED;
		case "schedule": return CampaignStatus.SCHEDULE;
		case "sending":  return CampaignStatus.SENDING;
		case "sent": return CampaignStatus.SENT;
		default:return null;
		}
	}
	
	/**
	 * Convert a string containing the status of a automation in AutomationStatus enum
	 * @param automationStatus
	 * @return
	 */
	private AutomationStatus translateStringIntoAutomationStatus(String automationStatus){

		switch(automationStatus){
		case "save": return AutomationStatus.SAVE;
		case "paused": return AutomationStatus.PAUSED;
		case "sending":  return AutomationStatus.SENDING;
		default:return null;
		}
	}

	/**
	 * Convert a date formatted in IS8601 to a normal java date
	 * @param dateString
	 * @return Date
	 */
	private Date createDateFromISO8601(String dateString){
		Date date;
		try{
			date = javax.xml.bind.DatatypeConverter.parseDateTime(dateString).getTime();
		}catch(IllegalArgumentException iae){
			date = null;
		}
		return date;
	}
	
	private Date createDateFromNormal(String dateString){
	    Date date;
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	    try {
	        date = df.parse(dateString);
	    } catch (ParseException e) {
	        throw new RuntimeException("Failed to parse date: ", e);
	    }

	    return date;
	}

	/**
	 * @return the server
	 */
	public String getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	private void setServer(String server) {
		this.server = server;
	}

	/**
	 * @return the apikey
	 */
	public String getApikey() {
		return apikey;
	}

	/**
	 * @param apikey the apikey to set
	 */
	private void setApikey(String apikey) {
		this.apikey = "apikey "+apikey;
	}

	/**
	 * @return the apiendpoint
	 */
	public String getAPIENDPOINT() {
		return APIENDPOINT;
	}

	/**
	 * @param apiendpoint the apiendpoint to set
	 */
	private void setAPIENDPOINT(String apiendpoint) {
		APIENDPOINT = apiendpoint;
	}

	/**
	 * @return the lISTENDPOINT
	 */
	public String getLISTENDPOINT() {
		return LISTENDPOINT;
	}

	/**
	 * @param listendpoint the listendpoint to set
	 */
	private void setLISTENDPOINT(String listendpoint) {
		LISTENDPOINT = listendpoint;
	}


	/**
	 * @return the campaignendpoint
	 */
	public String getCAMPAIGNENDPOINT() {
		return CAMPAIGNENDPOINT;
	}


	/**
	 * @param campaignendpoint the campaignendpoint to set
	 */
	private void setCAMPAIGNENDPOINT(String campaignendpoint) {
		CAMPAIGNENDPOINT = campaignendpoint;
	}


	/**
	 * @return the templateendpoint
	 */
	public String getTEMPLATEENDPOINT() {
		return TEMPLATEENDPOINT;
	}

	/**
	 * @param templateendpoint the templateendpoint to set
	 */
	private void setTEMPLATEENDPOINT(String templateendpoint) {
		TEMPLATEENDPOINT = templateendpoint;
	}
	
	/**
	 * @return the automationendpoint
	 */
	public String getAUTOMATIONENDPOINT(){
		return AUTOMATIONENDPOINT;
	}
	
	/**
	 * 
	 * @param automationendpoint the automationendpoint to set
	 * @return
	 */
	private void setAUTOMATIONENDPOINT(String automationendpoint){
		this.AUTOMATIONENDPOINT = automationendpoint;
	}


	/**
	 *
	 * @return the filemanagerfolderendpoint
     */
	public String getFILEMANAGERFOLDERENDPOINT() {
		return FILEMANAGERFOLDERENDPOINT;
	}

	/**
	 * The filemanagerendpoint to set
	 * @param FILEMANAGERFOLDERENDPOINT
     */
	public void setFILEMANAGERFOLDERENDPOINT(String FILEMANAGERFOLDERENDPOINT) {
		this.FILEMANAGERFOLDERENDPOINT = FILEMANAGERFOLDERENDPOINT;
	}


	public String getFILESENDPOINT() {
		return FILESENDPOINT;
	}

	public void setFILESENDPOINT(String FILESENDPOINT) {
		this.FILESENDPOINT = FILESENDPOINT;
	}


	/**
	 * @return the account
	 */
	public Account getAccount() {
		return this.account;
	}

	/**
	 * Set the account, of this connection. 
	 */
	private void setAccount() throws Exception {
		Account account;
		JSONObject jsonAPIROOT = new JSONObject(do_Get(new URL(APIENDPOINT)));
		JSONObject contact = jsonAPIROOT.getJSONObject("contact");
		account = new Account(this,contact.getString("company"),contact.getString("addr1"), contact.getString("city"), contact.getString("state"),contact.getString("zip"), contact.getString("country"));
		this.account = account;
	}
}
