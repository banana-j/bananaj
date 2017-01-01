/**
 * @author alexanderweiss
 * @date 06.12.2015
 */
package model.campaign;

import connection.MailChimpConnection;
import exceptions.CampaignSettingsException;
import exceptions.EmailException;
import org.json.JSONObject;
import utils.EmailValidator;

import java.net.URL;

/**
 * Class for representing settings for a campaign
 * @author alexanderweiss
 *
 */
public class CampaignSettings {
	
	private String subject_line;
	private String title;
	private String from_name;
	private String reply_to;
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

	public CampaignSettings(Builder b) throws CampaignSettingsException{

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

		if(b.reply_to == null){
			throw new CampaignSettingsException("You need to provide a 'Reply to email address' for a campaign setting");
		} else {
			this.reply_to = b.reply_to;
		}
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
	 * @return the subject_line
	 */
	public String getSubject_line() {
		return subject_line;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the from_name
	 */
	public String getFrom_name() {
		return from_name;
	}

	/**
	 * @return the reply_to
	 */
	public String getReply_to() {
		return reply_to;
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

	public static class Builder {
		private String subject_line;
		private String title;
		private String from_name;
		private String reply_to;
		private String campaignId;
		private MailChimpConnection connection;

		public Builder subject_line(String subject_line) {
			this.subject_line = subject_line;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
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
