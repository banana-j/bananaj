/**
 * @author alexanderweiss
 * @date 06.12.2015
 */
package model.campaign;

import java.net.URL;

import org.json.JSONObject;

import connection.MailChimpConnection;
import exceptions.CampaignSettingsException;
import exceptions.EmailException;
import utils.EmailValidator;

/**
 * Class for representing settings for a campaign, including subject, from name, reply-to address, and more.
 * @author alexanderweiss
 *
 */
public class CampaignSettings {
	
	// Campaign Settings 
	private String subject_line;
	//private String preview_text; // The preview text for the campaign.
	private String title;
	private String to_name;
	private String from_name;
	private String reply_to;
	private int template_id;
	private Boolean auto_footer;
	private Boolean use_conversation;
	private Boolean authenticate;
	private Boolean timewarp;
	private Boolean auto_tweet;
	private Boolean fb_comments;
	private Boolean drag_and_drop;
	private Boolean inline_css;
	//private Boolean auto_tweet;  // Automatically tweet a link to the campaign archive page when the campaign is sent.
	//List<> auto_fb_post;  // An array of Facebook page ids to auto-post to.
	private String folder_id;
	// Convince references
	private String campaignId;
	private MailChimpConnection connection;
	
	public CampaignSettings(String subject_line, String title, String from_name, String reply_to, String campaignId, MailChimpConnection connection) {
		this.subject_line = subject_line;
		this.title = title;
		this.from_name = from_name;
		this.reply_to = reply_to;
		this.campaignId = campaignId;
		this.connection = connection;
	}

	public CampaignSettings(MailChimpConnection connection, String campaignId, JSONObject settings) {
		subject_line = getString(settings, "subject_line");
		title = getString(settings, "title");
		to_name = getString(settings, "to_name");
		from_name = getString(settings, "from_name");
		reply_to = getString(settings, "reply_to");
		template_id = settings.getInt("template_id");
		auto_footer = getBoolean(settings, "auto_footer");
		use_conversation = getBoolean(settings, "use_conversation");
		authenticate = getBoolean(settings, "authenticate");
		timewarp = getBoolean(settings, "timewarp");
		auto_tweet = getBoolean(settings, "auto_tweet");
		fb_comments = getBoolean(settings, "fb_comments");
		drag_and_drop = getBoolean(settings, "drag_and_drop");
		inline_css = getBoolean(settings, "inline_css");
		folder_id = getString(settings, "folder_id");
		this.campaignId = campaignId;
		this.connection = connection;
	}
	
	private CampaignSettings(Builder b) throws CampaignSettingsException{

		if(b.subject_line == null){
			throw new CampaignSettingsException("You need to provide a 'subject line' for a campaign setting");
		} else {
			this.subject_line = b.subject_line;
		}

		if(b.title == null){
			throw new CampaignSettingsException("You need to provide a 'title' for a campaign setting");
		} else {
			this.title = b.title;
		}

		this.to_name = b.to_name;
		
		if(b.from_name == null){
			throw new CampaignSettingsException("You need to provide a 'From name' for a campaign setting");
		} else {
			this.from_name = b.from_name;
		}

		if(b.reply_to == null){
			throw new CampaignSettingsException("You need to provide a 'Reply to email address' for a campaign setting");
		} else {
			this.reply_to = b.reply_to;
		}

		this.reply_to = b.reply_to;
		this.template_id = b.template_id;
		this.auto_footer = b.auto_footer;
		this.use_conversation = b.use_conversation;
		this.authenticate = b.authenticate;
		this.timewarp = b.timewarp;
		this.auto_tweet = b.auto_tweet;
		this.fb_comments = b.fb_comments;
		this.drag_and_drop = b.drag_and_drop;
		this.inline_css = b.inline_css;
		this.folder_id = b.folder_id;
		this.campaignId = b.campaignId;
		this.connection = b.connection;
	}

	private String getString(JSONObject settings, String key) {
		if (settings.has(key)) {
			return settings.getString(key);
		}
		return null;
	}
	
	private Boolean getBoolean(JSONObject settings, String key) {
		if (settings.has(key)) {
			return settings.getBoolean(key);
		}
		return null;
	}
	
	/**
	 * Change the subject line of this campaign
	 * @param newSubject
	 * @throws Exception
	 */
	public void changeSubjectLine(String newSubject) throws Exception{
		JSONObject updatedCampaign = new JSONObject();
		JSONObject updatedSettings = new JSONObject();
		updatedSettings.put("subject", newSubject);
		updatedCampaign.put("settings", updatedSettings);
		this.getConnection().do_Patch(new URL(this.getConnection().getCampaignendpoint()+"/"+this.getCampaignId()),updatedCampaign.toString(),this.getConnection().getApikey());
		this.subject_line = newSubject;
	}

	/**
	 * Change the title of this campaign
	 * @param newTitle
	 * @throws Exception
	 */
	public void changeTitle(String newTitle) throws Exception{
		JSONObject updatedCampaign = new JSONObject();
		JSONObject updatedSettings = new JSONObject();
		updatedSettings.put("title", newTitle);
		updatedCampaign.put("settings", updatedSettings);
		this.getConnection().do_Patch(new URL(this.getConnection().getCampaignendpoint()+"/"+this.getCampaignId()),updatedCampaign.toString(),this.getConnection().getApikey());
		this.title = newTitle;
	}

	/**
	 * Change the from name of this campaign
	 * @param newFrom
	 * @throws Exception
	 */
	public void changeFrom(String newFrom) throws Exception{
		JSONObject updatedCampaign = new JSONObject();
		JSONObject updatedSettings = new JSONObject();
		updatedSettings.put("from_name", newFrom);
		updatedCampaign.put("settings", updatedSettings);
		this.getConnection().do_Patch(new URL(this.getConnection().getCampaignendpoint()+"/"+this.getCampaignId()),updatedCampaign.toString(),this.getConnection().getApikey());
		this.from_name = newFrom;
	}

	/**
	 * Change the reply to email address of this campaign
	 * @param newReplyToMail
	 * @throws Exception
	 */
	public void changeReplyTo(String newReplyToMail) throws Exception{
		if(EmailValidator.getInstance().validate(newReplyToMail)){
			JSONObject updatedCampaign = new JSONObject();
			JSONObject updatedSettings = new JSONObject();
			updatedSettings.put("reply_to", newReplyToMail);
			updatedCampaign.put("settings", updatedSettings);
			this.getConnection().do_Patch(new URL(this.getConnection().getCampaignendpoint()+"/"+this.getCampaignId()),updatedCampaign.toString(),this.getConnection().getApikey());
			this.reply_to = newReplyToMail;
		} else {
			throw new EmailException(newReplyToMail);
		}
	}

	/**
	 * The subject line for the campaign.
	 * @return the subject_line
	 */
	public String getSubject_line() {
		return subject_line;
	}

	/**
	 * The title of the campaign.
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * The campaign’s custom ‘To’ name. Typically the first name merge field.
	 * @return the to_name
	 */
	public String getTo_name() {
		return to_name;
	}

	/**
	 * The ‘from’ name on the campaign (not an email address).
	 * @return the from_name
	 */
	public String getFrom_name() {
		return from_name;
	}

	/**
	 * The reply-to email address for the campaign. Note: while this field is not required for campaign creation, it is required for sending.
	 * @return the reply_to
	 */
	public String getReply_to() {
		return reply_to;
	}

	/**
	 * The id of the template to use.
	 * @return the template_id
	 */
	public int getTemplate_id() {
		return template_id;
	}

	/**
	 * Automatically append MailChimp’s default footer to the campaign.
	 * @return the auto_footer
	 */
	public Boolean getAuto_footer() {
		return auto_footer;
	}

	/**
	 * Use MailChimp Conversation feature to manage out-of-office replies.
	 * @return the use_conversation
	 */
	public Boolean getUse_conversation() {
		return use_conversation;
	}

	/**
	 * Whether MailChimp authenticated the campaign. Defaults to true.
	 * @return the authenticate
	 */
	public Boolean getAuthenticate() {
		return authenticate;
	}

	/**
	 * @return the timewarp
	 */
	public Boolean getTimewarp() {
		return timewarp;
	}

	/**
	 * @return the auto_tweet
	 */
	public Boolean getAuto_tweet() {
		return auto_tweet;
	}

	/**
	 * Allows Facebook comments on the campaign (also force-enables the Campaign Archive toolbar). Defaults to true.
	 * @return the fb_comments
	 */
	public Boolean getFb_comments() {
		return fb_comments;
	}

	/**
	 * @return the drag_and_drop
	 */
	public Boolean getDrag_and_drop() {
		return drag_and_drop;
	}

	/**
	 * Automatically inline the CSS included with the campaign content.
	 * @return the inline_css
	 */
	public Boolean getInline_css() {
		return inline_css;
	}

	/**
	 * If the campaign is listed in a folder, the id for that folder.
	 * @return the folder_id
	 */
	public String getFolder_id() {
		return folder_id;
	}

	public MailChimpConnection getConnection() {
		return this.connection;
	}

	public void setConnection(MailChimpConnection connection) {
		this.connection = connection;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	/**
	 * CampaignSettings builder pattern. 
	 *
	 */
	public static class Builder {
		private String subject_line;
		//private String preview_text; // The preview text for the campaign.
		private String title;
		private String to_name;
		private String from_name;
		private String reply_to;
		private int template_id;
		private Boolean auto_footer;
		private Boolean use_conversation;
		private Boolean authenticate;
		private Boolean timewarp;
		private Boolean auto_tweet;
		private Boolean fb_comments;
		private Boolean drag_and_drop;
		private Boolean inline_css;
		//private Boolean auto_tweet;  // Automatically tweet a link to the campaign archive page when the campaign is sent.
		//List<> auto_fb_post;  // An array of Facebook page ids to auto-post to.
		private String folder_id;
		private String campaignId;
		private MailChimpConnection connection;

		public Builder (MailChimpConnection connection) {
			this.connection = connection;
		}
		
		public Builder (CampaignSettings settings) {
			this.subject_line = settings.subject_line;
			this.title = settings.title;
			this.to_name = settings.to_name;
			this.from_name = settings.from_name;
			this.reply_to = settings.reply_to;
			this.template_id = settings.template_id;
			this.auto_footer = settings.auto_footer;
			this.use_conversation = settings.use_conversation;
			this.authenticate = settings.authenticate;
			this.timewarp = settings.timewarp;
			this.auto_tweet = settings.auto_tweet;
			this.fb_comments = settings.fb_comments;
			this.drag_and_drop = settings.drag_and_drop;
			this.inline_css = settings.inline_css;
			this.folder_id = settings.folder_id;
			this.campaignId = settings.campaignId;
			this.connection = settings.connection;
		}

		public Builder () {
			
		}
		
		public Builder subject_line(String subject_line) {
			this.subject_line = subject_line;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder to_name(String to_name) {
			this.to_name = to_name;
			return this;
		}

		public Builder from_name(String from_name) {
			this.from_name = from_name;
			return this;
		}

		public Builder reply_to(String reply_to) {
			this.reply_to = reply_to;
			return this;
		}

		public Builder template_id(int template_id) {
			this.template_id = template_id;
			return this;
		}

		public Builder auto_footer(Boolean auto_footer) {
			this.auto_footer = auto_footer;
			return this;
		}
		
		public Builder use_conversation(Boolean use_conversation) {
			this.use_conversation = use_conversation;
			return this;
		}
		
		public Builder authenticate(Boolean authenticate) {
			this.authenticate = authenticate;
			return this;
		}
		
		public Builder timewarp(Boolean timewarp) {
			this.timewarp = timewarp;
			return this;
		}
		
		public Builder auto_tweet(Boolean auto_tweet) {
			this.auto_tweet = auto_tweet;
			return this;
		}
		
		public Builder fb_comments(Boolean fb_comments) {
			this.fb_comments = fb_comments;
			return this;
		}
		
		public Builder drag_and_drop(Boolean drag_and_drop) {
			this.drag_and_drop = drag_and_drop;
			return this;
		}
		
		public Builder inline_css(Boolean inline_css) {
			this.inline_css = inline_css;
			return this;
		}
		
		public Builder folder_id(String folder_id) {
			this.folder_id = folder_id;
			return this;
		}
		
		public Builder connection(String campaignId) {
			this.campaignId = campaignId;
			return this;
		}

		public Builder connection(MailChimpConnection connection) {
			this.connection = connection;
			return this;
		}

		
		public CampaignSettings build() {
			try {
				return new CampaignSettings(this);
			} catch (CampaignSettingsException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
