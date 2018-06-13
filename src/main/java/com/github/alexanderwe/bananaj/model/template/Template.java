/**
 * @author alexanderweiss
 * @date 19.11.2015
 */
package com.github.alexanderwe.bananaj.model.template;

import java.net.URL;
import java.time.LocalDateTime;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.connection.MailChimpConnection;
import com.github.alexanderwe.bananaj.model.MailchimpObject;
import com.github.alexanderwe.bananaj.utils.DateConverter;

public class Template extends MailchimpObject  {

	private String templateName;
	private TemplateType templateType;
	private String shareUrl;
	private LocalDateTime dateCreated;
	private String folder_id;
	private MailChimpConnection connection;
	private String html;
	
	public Template(int id, String templateName, TemplateType templateType, String shareUrl, LocalDateTime dateCreated, String folder_id, MailChimpConnection connection, JSONObject jsonRepresentation) {
		super(String.valueOf(id),jsonRepresentation);
		this.templateName = templateName;
		this.templateType = templateType;
		this.shareUrl = shareUrl;
		this.dateCreated = dateCreated;
		this.folder_id = folder_id;
		this.connection = connection;
	}

	public Template(MailChimpConnection connection, JSONObject jsonTemplate) {
		super(String.valueOf(jsonTemplate.getInt("id")), jsonTemplate);
		this.templateName = jsonTemplate.getString("name");
		this.templateType = TemplateType.valueOf(jsonTemplate.getString("type").toUpperCase());
		this.shareUrl = jsonTemplate.getString("share_url");
		this.dateCreated = DateConverter.getInstance().createDateFromISO8601(jsonTemplate.getString("date_created"));
		this.folder_id = jsonTemplate.has("folder_id") ? jsonTemplate.getString("folder_id") : null;
		this.connection = connection;
	}
	
	public Template(Builder b){
		this.templateName = b.templateName;
		this.folder_id = b.folder_id;
		this.html = b.html;
	}


	/**
	 * Change the name of this template
	 * @param name
	 * @throws Exception
	 */
	public void changeName(String name) throws Exception{
		JSONObject changedTemplate = new JSONObject();
		changedTemplate.put("name",name);
		this.getConnection().do_Patch(new URL(this.getConnection().getTemplateendpoint()+"/"+this.getId()), changedTemplate.toString(),this.getConnection().getApikey() );
		this.templateName = name;
	}

	/**
	 * Change the html content of this template
	 * @param html
	 * @throws Exception
	 */
	public void changeHTML(String html) throws Exception{
		JSONObject changedTemplate = new JSONObject();
		changedTemplate.put("html",html);
		this.getConnection().do_Patch(new URL(this.getConnection().getTemplateendpoint()+"/"+this.getId()), changedTemplate.toString(),this.getConnection().getApikey() );
	}

	/**
	 * Change the folder of this template
	 * @param folder_id
	 * @throws Exception
	 */
	public void changeFolder(String folder_id) throws Exception{
		JSONObject changedTemplate = new JSONObject();
		changedTemplate.put("folder_id",folder_id);
		this.getConnection().do_Patch(new URL(this.getConnection().getTemplateendpoint()+"/"+this.getId()), changedTemplate.toString(),this.getConnection().getApikey() );
		this.folder_id = folder_id;
	}

	/**
	 * Overwrite this template with a new one
	 * Sets new name, content, and folder
	 * @param template
	 */
	public void overwrite(Template template) throws Exception{
		JSONObject changedTemplate = new JSONObject();
		changedTemplate.put("name",template.getTemplateName() != null ? template.getTemplateName(): "");
		changedTemplate.put("folder_id",template.getFolder_id() != null ? template.getFolder_id() : "");
		changedTemplate.put("html",template.getHtml() != null ? template.getHtml(): "");
		this.getConnection().do_Patch(new URL(this.getConnection().getTemplateendpoint()+"/"+this.getId()), changedTemplate.toString(),this.getConnection().getApikey() );
		this.templateName = template.getTemplateName();
		this.folder_id = template.getFolder_id();
		this.html = template.getHtml();
	}

	/**
	 * @return the templateType
	 */
	public TemplateType getTemplateType() {
		return templateType;
	}

	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * @return the shareUrl
	 */
	public String getShareUrl() {
		return shareUrl;
	}

	/**
	 * @return the dateCreated
	 */
	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	/**
	 * @return the folder ID the template is currently in
	 */
	public String getFolder_id() {
		return folder_id;
	}

	/**
	 * @return the html content of this template. Is not set, when template is received from MailChimp servers
	 */
	public String getHtml() {
		return html;
	}

	/**
	 * @return the com.github.alexanderwe.bananaj.connection to MailChimp
	 */
	public MailChimpConnection getConnection() {
		return connection;
	}

	@Override
	public String toString(){
		return "Name: " + this.getId() + "-" + this.getTemplateName() + System.lineSeparator() +
				"Type: " + this.getTemplateType().getStringRepresentation() + System.lineSeparator() +
				"Share url: "+  this.getShareUrl() +  System.lineSeparator()+
				"Date created: " + this.getDateCreated() + System.lineSeparator();
	}

	public static class Builder {
		private String templateName;
		private String folder_id;
		private String html;

		public Template.Builder withName(String name) {
			this.templateName = name;
			return this;
		}

		public Template.Builder inFolder(String folder_id) {
			this.folder_id = folder_id;
			return this;
		}

		public Template.Builder withHTML(String html) {
			this.html = html;
			return this;
		}


		public Template build() {
			return new Template(this);
		}
	}
}
