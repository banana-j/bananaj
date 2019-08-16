/**
 * @author alexanderweiss
 * @date 06.12.2015
 */
package com.github.alexanderwe.bananaj.model.campaign;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.exceptions.CampaignSettingsException;

/**
 * Class for representing settings for a campaign, including subject, from name, reply-to address, and more.
 * @author alexanderweiss
 *
 */
public class CampaignSettings {
	
	// Campaign Settings 
	private String subjectLine;
	//private String preview_text; // The preview text for the campaign.
	private String title;
	private String toName;
	private String fromName;
	private String replyTo;
	private int templateId;
	private Boolean autoFooter;
	private Boolean useConversation;
	private Boolean authenticate;
	private Boolean timewarp;
	private Boolean autoTweet;
	private Boolean fbComments;
	private Boolean dragAndDrop;
	private Boolean inlineCss;
	//private Boolean auto_tweet;  // Automatically tweet a link to the campaign archive page when the campaign is sent.
	//List<> auto_fb_post;  // An array of Facebook page ids to auto-post to.
	private String folderId;
	
	public CampaignSettings(JSONObject settings) {
		subjectLine = getString(settings, "subject_line");
		title = getString(settings, "title");
		toName = getString(settings, "to_name");
		fromName = getString(settings, "from_name");
		replyTo = getString(settings, "reply_to");
		templateId = settings.getInt("template_id");
		autoFooter = getBoolean(settings, "auto_footer");
		useConversation = getBoolean(settings, "use_conversation");
		authenticate = getBoolean(settings, "authenticate");
		timewarp = getBoolean(settings, "timewarp");
		autoTweet = getBoolean(settings, "auto_tweet");
		fbComments = getBoolean(settings, "fb_comments");
		dragAndDrop = getBoolean(settings, "drag_and_drop");
		inlineCss = getBoolean(settings, "inline_css");
		folderId = getString(settings, "folder_id");
	}
	
	private CampaignSettings(Builder b) throws CampaignSettingsException{

		if(b.subjectLine == null){
			throw new CampaignSettingsException("You need to provide a 'subject line' for a campaign setting");
		} else {
			this.subjectLine = b.subjectLine;
		}

		if(b.title == null){
			throw new CampaignSettingsException("You need to provide a 'title' for a campaign setting");
		} else {
			this.title = b.title;
		}

		this.toName = b.toName;
		
		if(b.fromName == null){
			throw new CampaignSettingsException("You need to provide a 'From name' for a campaign setting");
		} else {
			this.fromName = b.fromName;
		}

		if(b.replyTo == null){
			throw new CampaignSettingsException("You need to provide a 'Reply to email address' for a campaign setting");
		} else {
			this.replyTo = b.replyTo;
		}

		this.replyTo = b.replyTo;
		this.templateId = b.templateId;
		this.autoFooter = b.autoFooter;
		this.useConversation = b.useConversation;
		this.authenticate = b.authenticate;
		this.timewarp = b.timewarp;
		this.autoTweet = b.autoTweet;
		this.fbComments = b.fbComments;
		this.dragAndDrop = b.dragAndDrop;
		this.inlineCss = b.inlineCss;
		this.folderId = b.folderId;
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
	 * The subject line for the campaign.
	 * @return the subject_line
	 */
	public String getSubjectLine() {
		return subjectLine;
	}

	/**
	 * The subject line for the campaign.
	 * @param subjectLine the subjectLine to set
	 */
	public void setSubjectLine(String subjectLine) {
		this.subjectLine = subjectLine;
	}

	/**
	 * The title of the campaign.
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * The campaign’s custom ‘To’ name. Typically the first name merge field.
	 * @return the to_name
	 */
	public String getToName() {
		return toName;
	}

	/**
	 * @param toName the toName to set
	 */
	public void setToName(String toName) {
		this.toName = toName;
	}

	/**
	 * The ‘from’ name on the campaign (not an email address).
	 * @return the from_name
	 */
	public String getFromName() {
		return fromName;
	}

	/**
	 * @param fromName the fromName to set
	 */
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	/**
	 * The reply-to email address for the campaign. Note: while this field is not required for campaign creation, it is required for sending.
	 * @return the reply_to
	 */
	public String getReplyTo() {
		return replyTo;
	}

	/**
	 * @param replyTo the replyTo to set
	 */
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	/**
	 * The id of the template to use.
	 * @return the template_id
	 */
	public int getTemplateId() {
		return templateId;
	}

	/**
	 * @param templateId the templateId to set
	 */
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	/**
	 * Automatically append MailChimp’s default footer to the campaign.
	 * @return the auto_footer
	 */
	public Boolean getAutoFooter() {
		return autoFooter;
	}

	/**
	 * @param autoFooter the autoFooter to set
	 */
	public void setAutoFooter(Boolean autoFooter) {
		this.autoFooter = autoFooter;
	}

	/**
	 * Use MailChimp Conversation feature to manage out-of-office replies.
	 * @return the use_conversation
	 */
	public Boolean getUseConversation() {
		return useConversation;
	}

	/**
	 * @param useConversation the useConversation to set
	 */
	public void setUseConversation(Boolean useConversation) {
		this.useConversation = useConversation;
	}

	/**
	 * Whether MailChimp authenticated the campaign. Defaults to true.
	 * @return the authenticate
	 */
	public Boolean getAuthenticate() {
		return authenticate;
	}

	/**
	 * @param authenticate the authenticate to set
	 */
	public void setAuthenticate(Boolean authenticate) {
		this.authenticate = authenticate;
	}

	/**
	 * @return the timewarp
	 */
	public Boolean getTimewarp() {
		return timewarp;
	}

	/**
	 * @param timewarp the timewarp to set
	 */
	public void setTimewarp(Boolean timewarp) {
		this.timewarp = timewarp;
	}

	/**
	 * @return the auto_tweet
	 */
	public Boolean getAutoTweet() {
		return autoTweet;
	}

	/**
	 * @param autoTweet the autoTweet to set
	 */
	public void setAutoTweet(Boolean autoTweet) {
		this.autoTweet = autoTweet;
	}

	/**
	 * Allows Facebook comments on the campaign (also force-enables the Campaign Archive toolbar). Defaults to true.
	 * @return the fb_comments
	 */
	public Boolean getFbComments() {
		return fbComments;
	}

	/**
	 * @param fbComments the fbComments to set
	 */
	public void setFbComments(Boolean fbComments) {
		this.fbComments = fbComments;
	}

	/**
	 * @return the drag_and_drop
	 */
	public Boolean getDragAndDrop() {
		return dragAndDrop;
	}

	/**
	 * @param dragAndDrop the dragAndDrop to set
	 */
	public void setDragAndDrop(Boolean dragAndDrop) {
		this.dragAndDrop = dragAndDrop;
	}

	/**
	 * Automatically inline the CSS included with the campaign content.
	 * @return the inline_css
	 */
	public Boolean getInlineCss() {
		return inlineCss;
	}

	/**
	 * @param inlineCss the inlineCss to set
	 */
	public void setInlineCss(Boolean inlineCss) {
		this.inlineCss = inlineCss;
	}

	/**
	 * If the campaign is listed in a folder, the id for that folder
	 * @return the folderId
	 */
	public String getFolderId() {
		return folderId;
	}

	/**
	 * @param folderId the folderId to set
	 */
	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
	public JSONObject getJsonRepresentation() {
		JSONObject jsonSettings = new JSONObject();
		put(jsonSettings, "subject_line", getSubjectLine());
		put(jsonSettings, "title", getTitle());
		put(jsonSettings, "to_name", getToName());
		put(jsonSettings, "from_name", getFromName());
		put(jsonSettings, "reply_to", getReplyTo());
		if(getTemplateId() != 0 ) {
			jsonSettings.put("template_id", getTemplateId());
		}
		put(jsonSettings, "auto_footer", getAutoFooter());
		put(jsonSettings, "use_conversation", getUseConversation());
		put(jsonSettings, "authenticate", getAuthenticate());
		put(jsonSettings, "timewarp", getTimewarp());
		put(jsonSettings, "auto_tweet", getAutoTweet());
		put(jsonSettings, "fb_comments", getFbComments());
		put(jsonSettings, "drag_and_drop", getDragAndDrop());
		put(jsonSettings, "inline_css", getInlineCss());
		put(jsonSettings, "folder_id", getFolderId());

		return jsonSettings;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return
				"Settings:" + System.lineSeparator() +
				"    Title: " + title + System.lineSeparator() +
				"    SubjectLine: " + subjectLine + System.lineSeparator() +
				//"    preview_text: " + preview_text + System.lineSeparator() +
				"    To Name: " + toName + System.lineSeparator() +
				"    From Name: " + fromName + System.lineSeparator() +
				"    Reply To: " + replyTo + System.lineSeparator() +
				"    Template Id: " + templateId + System.lineSeparator() +
				"    Auto Footer: " + autoFooter + System.lineSeparator() +
				"    Use Conversation: " + useConversation + System.lineSeparator() +
				"    Authenticate: " + authenticate + System.lineSeparator() +
				"    Timewarp: " + timewarp + System.lineSeparator() +
				"    Auto Tweet: " + autoTweet + System.lineSeparator() +
				"    FacebooknComments: " + fbComments + System.lineSeparator() +
				"    Drag and Drop: " + dragAndDrop + System.lineSeparator() +
				"    Inline Css: " + inlineCss + System.lineSeparator() +
				//"    auto_tweet: " + auto_tweet + System.lineSeparator() +
				//"    auto_fb_post: " + auto_fb_post + System.lineSeparator() +
				"    Folder Id: " + folderId;
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
	 * CampaignSettings builder pattern. 
	 *
	 */
	public static class Builder {
		private String subjectLine;
		//private String preview_text; // The preview text for the campaign.
		private String title;
		private String toName;
		private String fromName;
		private String replyTo;
		private int templateId;
		private Boolean autoFooter;
		private Boolean useConversation;
		private Boolean authenticate;
		private Boolean timewarp;
		private Boolean autoTweet;
		private Boolean fbComments;
		private Boolean dragAndDrop;
		private Boolean inlineCss;
		//private Boolean auto_tweet;  // Automatically tweet a link to the campaign archive page when the campaign is sent.
		//List<> auto_fb_post;  // An array of Facebook page ids to auto-post to.
		private String folderId;

		public Builder (CampaignSettings settings) {
			this.subjectLine = settings.subjectLine;
			this.title = settings.title;
			this.toName = settings.toName;
			this.fromName = settings.fromName;
			this.replyTo = settings.replyTo;
			this.templateId = settings.templateId;
			this.autoFooter = settings.autoFooter;
			this.useConversation = settings.useConversation;
			this.authenticate = settings.authenticate;
			this.timewarp = settings.timewarp;
			this.autoTweet = settings.autoTweet;
			this.fbComments = settings.fbComments;
			this.dragAndDrop = settings.dragAndDrop;
			this.inlineCss = settings.inlineCss;
			this.folderId = settings.folderId;
		}

		public Builder () {
			
		}
		
		public Builder subjectLine(String subject_line) {
			this.subjectLine = subject_line;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder toName(String toName) {
			this.toName = toName;
			return this;
		}

		public Builder fromName(String from_name) {
			this.fromName = from_name;
			return this;
		}

		public Builder replyTo(String reply_to) {
			this.replyTo = reply_to;
			return this;
		}

		public Builder templateId(int template_id) {
			this.templateId = template_id;
			return this;
		}

		public Builder autoFooter(Boolean auto_footer) {
			this.autoFooter = auto_footer;
			return this;
		}
		
		public Builder useConversation(Boolean use_conversation) {
			this.useConversation = use_conversation;
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
		
		public Builder autoTweet(Boolean auto_tweet) {
			this.autoTweet = auto_tweet;
			return this;
		}
		
		public Builder fbComments(Boolean fb_comments) {
			this.fbComments = fb_comments;
			return this;
		}
		
		public Builder dragAndDrop(Boolean drag_and_drop) {
			this.dragAndDrop = drag_and_drop;
			return this;
		}
		
		public Builder inlineCss(Boolean inline_css) {
			this.inlineCss = inline_css;
			return this;
		}
		
		public Builder folderId(String folder_id) {
			this.folderId = folder_id;
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
