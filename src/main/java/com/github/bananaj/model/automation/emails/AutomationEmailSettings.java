package com.github.bananaj.model.automation.emails;

import org.json.JSONObject;

import com.github.bananaj.utils.JSONObjectCheck;

public class AutomationEmailSettings {
	String	subjectLine;	// Campaign Subject Line	
	String	previewText;	// Campaign Preview Text	
	String	title;	// Campaign Title	
	String	fromName;	// From Name	
	String	replyTo;	// Reply To Address	
	Boolean	authenticate;	// Authentication	
	Boolean	autoFooter;	// Auto-Footer	
	Boolean	inlineCss;	// Inline CSS	
	Boolean	autoTweet;	// Auto-Tweet	
	// List<String> autoFbPost;	// Auto Post to Facebook	
	Boolean	fbComments;	// Facebook Comments	
	Integer templateId;	// Template ID	
	Boolean	dragAndDrop;	// Drag And Drop Campaign	

	public AutomationEmailSettings(JSONObject settings) {
		JSONObjectCheck jObj = new JSONObjectCheck(settings);
		this.subjectLine = jObj.getString("subject_line");
		this.previewText = jObj.getString("preview_text");
		this.title = jObj.getString("title");
		this.fromName = jObj.getString("from_name");
		this.replyTo = jObj.getString("reply_to");
		this.authenticate = jObj.getBoolean("authenticate");
		this.autoFooter = jObj.getBoolean("auto_footer");
		this.inlineCss = jObj.getBoolean("inline_css");
		this.autoTweet = jObj.getBoolean("auto_tweet");
		//this.autoFbPost = jsonObj.getJSONArray("auto_fb_post");
		this.fbComments = jObj.getBoolean("fb_comments");
		this.templateId = jObj.getInt("template_id");
		this.dragAndDrop = jObj.getBoolean("drag_and_drop");
	}

	public AutomationEmailSettings() {

	}

	/**
	 * The subject line for the campaign
	 */
	public String getSubjectLine() {
		return subjectLine;
	}

	/**
	 * The preview text for the campaign
	 */
	public String getPreviewText() {
		return previewText;
	}

	/**
	 * The title of the campaign
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * The ‘from’ name on the campaign (not an email address)
	 */
	public String getFromName() {
		return fromName;
	}

	/**
	 * The reply-to email address for the campaign
	 */
	public String getReplyTo() {
		return replyTo;
	}

	/**
	 * Whether Mailchimp authenticated the campaign. Defaults to true.
	 */
	public Boolean isAuthenticate() {
		return authenticate;
	}

	/**
	 * Automatically append Mailchimp’s default footer to the campaign
	 */
	public Boolean isAutoFooter() {
		return autoFooter;
	}

	/**
	 * Automatically inline the CSS included with the campaign content
	 */
	public Boolean isInlineCss() {
		return inlineCss;
	}

	/**
	 * Automatically tweet a link to the campaign archive page when the campaign is sent
	 */
	public Boolean isAutoTweet() {
		return autoTweet;
	}

	/**
	 * Allows Facebook comments on the campaign (also force-enables the Campaign Archive toolbar). Defaults to true.
	 */
	public Boolean isFbComments() {
		return fbComments;
	}

	/**
	 * The id for the template used in this campaign
	 */
	public Integer getTemplateId() {
		return templateId;
	}

	/**
	 * Whether the campaign uses the drag-and-drop editor
	 */
	public Boolean isDragAndDrop() {
		return dragAndDrop;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
	protected JSONObject getJsonRepresentation() throws Exception {
		JSONObject json = new JSONObject();

		json.put("subject_line", subjectLine);
		json.put("preview_text", previewText);
		json.put("title", title);
		json.put("from_name", fromName);
		json.put("reply_to", replyTo);

		return json;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return 
				"Settings:" + System.lineSeparator() +
				"    Title: " + getTitle() + System.lineSeparator() +
				"    Subject: " + getSubjectLine() + System.lineSeparator() +
				"    From: " + getFromName() + System.lineSeparator() +
				"    Reply To: " + getReplyTo() + System.lineSeparator() +
				"    Preview: " + getPreviewText() + System.lineSeparator() +
				"    Authenticate: " + isAuthenticate() + System.lineSeparator() +
				"    Auto Footer: " + isAutoFooter() + System.lineSeparator() +
				"    Inline CSS: " + isInlineCss() + System.lineSeparator() +
				"    Auto Tweet: " + isAutoTweet() + System.lineSeparator() +
				"    Facebook Comments: " + isFbComments() + System.lineSeparator() +
				"    Template Id: " + templateId + System.lineSeparator() +
				"    Drag-And-Drop: " + isDragAndDrop();
	}

}
