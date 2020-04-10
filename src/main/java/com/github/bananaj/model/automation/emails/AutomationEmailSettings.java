package com.github.bananaj.model.automation.emails;

import org.json.JSONObject;

public class AutomationEmailSettings {
	String	subjectLine;	// Campaign Subject Line	
	String	previewText;	// Campaign Preview Text	
	String	title;	// Campaign Title	
	String	fromName;	// From Name	
	String	replyTo;	// Reply To Address	
	boolean	authenticate;	// Authentication	
	boolean	autoFooter;	// Auto-Footer	
	boolean	inlineCss;	// Inline CSS	
	boolean	autoTweet;	// Auto-Tweet	
	// List<String> autoFbPost;	// Auto Post to Facebook	
	boolean	fbComments;	// Facebook Comments	
	int	templateId;	// Template ID	
	boolean	dragAndDrop;	// Drag And Drop Campaign	

	public AutomationEmailSettings(JSONObject jsonObj) {
		this.subjectLine = jsonObj.getString("subject_line");
		if (jsonObj.has("preview_text")) {
			this.previewText = jsonObj.getString("preview_text");
		}
		this.title = jsonObj.getString("title");
		this.fromName = jsonObj.getString("from_name");
		this.replyTo = jsonObj.getString("reply_to");
		this.authenticate = jsonObj.getBoolean("authenticate");
		this.autoFooter = jsonObj.getBoolean("auto_footer");
		this.inlineCss = jsonObj.getBoolean("inline_css");
		this.autoTweet = jsonObj.getBoolean("auto_tweet");
		//this.autoFbPost = jsonObj.getJSONArray("auto_fb_post");
		this.fbComments = jsonObj.getBoolean("fb_comments");
		this.templateId = jsonObj.getInt("template_id");
		this.dragAndDrop = jsonObj.getBoolean("drag_and_drop");
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
	public boolean isAuthenticate() {
		return authenticate;
	}

	/**
	 * Automatically append Mailchimp’s default footer to the campaign
	 */
	public boolean isAutoFooter() {
		return autoFooter;
	}

	/**
	 * Automatically inline the CSS included with the campaign content
	 */
	public boolean isInlineCss() {
		return inlineCss;
	}

	/**
	 * Automatically tweet a link to the campaign archive page when the campaign is sent
	 */
	public boolean isAutoTweet() {
		return autoTweet;
	}

	/**
	 * Allows Facebook comments on the campaign (also force-enables the Campaign Archive toolbar). Defaults to true.
	 */
	public boolean isFbComments() {
		return fbComments;
	}

	/**
	 * The id for the template used in this campaign
	 */
	public int getTemplateId() {
		return templateId;
	}

	/**
	 * Whether the campaign uses the drag-and-drop editor
	 */
	public boolean isDragAndDrop() {
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
