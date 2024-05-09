package com.github.bananaj.model.automation;

import org.json.JSONObject;

import com.github.bananaj.utils.JSONObjectCheck;

/**
 * Automation workflow settings
 *
 */
public class AutomationSettings {

	private String title;
	private String fromName;
	private String replyTo;
	private Boolean useConversation;
	private String toName;
	private boolean authenticate;
	private boolean autoFooter;
	private boolean inlineCss;

	/**
	 * Construct AutomationSettings for automation creation operation. 
	 * @param fromName
	 * @param toName
	 */
	public AutomationSettings(String fromName, String toName) {
		super();
		this.fromName = fromName;
		this.toName = toName;
	}

	public AutomationSettings(JSONObject settings) {
		JSONObjectCheck jObj = new JSONObjectCheck(settings);
		this.title = jObj.getString("title");
		this.fromName = jObj.getString("from_name");
		this.replyTo = jObj.getString("reply_to");
		this.useConversation = jObj.getBoolean("use_conversation");
		this.toName = jObj.getString("to_name");
		this.authenticate = jObj.getBoolean("authenticate");
		this.autoFooter = jObj.getBoolean("auto_footer");
		this.inlineCss = jObj.getBoolean("inline_css");
	}

	public AutomationSettings() {

	}

	/**
	 * The title of the Automation
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * The title of the Automation
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * The ‘from’ name for the Automation (not an email address)
	 */
	public String getFromName() {
		return fromName;
	}

	/**
	 * The ‘from’ name for the Automation (not an email address)
	 * @param fromName
	 */
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	/**
	 * The reply-to email address for the Automation
	 */
	public String getReplyTo() {
		return replyTo;
	}

	/**
	 * The reply-to email address for the Automation
	 * @param replyTo
	 */
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	/**
	 * Whether to use Mailchimp’s Conversations feature to manage out-of-office replies
	 */
	public Boolean isUseConversation() {
		return useConversation;
	}

	/**
	 * The Automation’s custom ‘To’ name, typically the first name merge field
	 */
	public String getToName() {
		return toName;
	}

	/**
	 * Whether Mailchimp authenticated the Automation. Defaults to true
	 */
	public boolean isAuthenticate() {
		return authenticate;
	}

	/**
	 * Whether to automatically append Mailchimp’s default footer to the Automation
	 */
	public boolean isAutoFooter() {
		return autoFooter;
	}

	/**
	 * Whether to automatically inline the CSS included with the Automation content
	 */
	public boolean isInlineCss() {
		return inlineCss;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
	public JSONObject getJsonRepresentation() throws Exception {
		JSONObject jsonObj = new JSONObject();

		if (title != null) {
			jsonObj.put("title", title);
		}
		if (fromName != null) {
			jsonObj.put("from_name", fromName);
		}
		if (replyTo != null) {
			jsonObj.put("reply_to", replyTo);
		}

		return jsonObj;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return 
				"Settings:" + System.lineSeparator() +
				"    Title: " + getTitle() + System.lineSeparator() +
				"    From Name: " + getFromName() + System.lineSeparator() +
				"    Reply To: " + getReplyTo() + System.lineSeparator() +
				"    Use Conversation: " + isUseConversation() + System.lineSeparator() +
				"    To Name: " + getToName() + System.lineSeparator() +
				"    Authenticate: " + isAuthenticate() + System.lineSeparator() +
				"    Auto Footer: " + isAutoFooter() + System.lineSeparator() +
				"    Inline CSS: " + isInlineCss();
	}
	
}
