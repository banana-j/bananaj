/**
 * @author alexanderweiss
 * @date 19.11.2015
 */
package com.github.bananaj.model.template;

import java.net.URL;
import java.time.ZonedDateTime;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.utils.DateConverter;
import com.github.bananaj.utils.JSONObjectCheck;

/**
 * Mailchimp template.
 */
public class Template implements JSONParser {

	private Integer id;
	private TemplateType type;
	private String name;
	private Boolean dragAndDrop;
	private Boolean responsive;
	private String category;
	private ZonedDateTime dateCreated;
	private ZonedDateTime dateEdited;
	private String createdBy;
	private String editedBy;
	private Boolean active;
	private String folderId;
	private String thumbnail;
	private String shareUrl;
	private MailChimpConnection connection;
	private String html;

	public Template() {
		
	}
	
	public Template(MailChimpConnection connection, JSONObject jsonTemplate) {
		parse(connection, jsonTemplate);
	}

	public Template(Builder b) {
		this.name = b.templateName;
		this.folderId = b.folder_id;
		this.html = b.html;
	}

	public void parse(MailChimpConnection connection, JSONObject template) {
		JSONObjectCheck jObj = new JSONObjectCheck(template);
		this.connection = connection;
		id = jObj.getInt("id");
		type = jObj.getEnum(TemplateType.class, "type");
		name = jObj.getString("name");
		dragAndDrop = jObj.getBoolean("drag_and_drop");
		responsive = jObj.getBoolean("responsive");
		category = jObj.getString("category");
		dateCreated = jObj.getISO8601Date("date_created");
		dateEdited = jObj.getISO8601Date("date_edited");
		createdBy = jObj.getString("created_by");
		editedBy = jObj.getString("edited_by");
		active = jObj.getBoolean("active");
		folderId = jObj.getString("folder_id");
		thumbnail = jObj.getString("thumbnail");
		shareUrl = jObj.getString("share_url");
		html = null;
	}

	/**
	 * Commit changes to template fields
	 */
	public void update() throws Exception {
		JSONObject jsonObj = getJsonRepresentation();
		String results = getConnection().do_Patch(new URL(getConnection().getTemplateendpoint()+"/"+getId()), jsonObj.toString(), getConnection().getApikey() );
		parse(connection, new JSONObject(results));
	}

	/**
	 * Get the default content for a template
	 * @throws Exception
	 */
	public JSONObject getDefaultContent() throws Exception {
		String results = getConnection().do_Get(new URL(getConnection().getTemplateendpoint()+"/"+getId()+"/default-content"), getConnection().getApikey() );
		return new JSONObject(results);
	}
	
	/**
	 * @return The individual id for the template
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the templateType
	 */
	public TemplateType getType() {
		return type;
	}

	/**
	 * @return the template name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Change the name of the template. You must call {@link #update()} for changes to take effect.
	 * @param name the template name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Whether the template uses the drag and drop editor.
	 */
	public Boolean isDragAndDrop() {
		return dragAndDrop;
	}

	/**
	 * @return Whether the template contains media queries to make it responsive.
	 */
	public Boolean isResponsive() {
		return responsive;
	}

	/**
	 * @return If available, the category the template is listed in.
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @return The date and time the template was created
	 */
	public ZonedDateTime getDateCreated() {
		return dateCreated;
	}

	/**
	 * @return The date and time the template was edited
	 */
	public ZonedDateTime getDateEdited() {
		return dateEdited;
	}

	/**
	 * @return The login name for template’s creator.
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @return The login name who last edited the template.
	 */
	public String getEditedBy() {
		return editedBy;
	}

	/**
	 * User templates are not ‘deleted,’ but rather marked as ‘inactive.’
	 * @return Wether the template is still active.
	 */
	public Boolean isActive() {
		return active;
	}

	/**
	 * @return the folder ID the template is currently in
	 */
	public String getFolderId() {
		return folderId;
	}

	/**
	 * @return If available, the URL for a thumbnail of the template
	 */
	public String getThumbnail() {
		return thumbnail;
	}

	/**
	 * @return The URL used for template sharing.
	 */
	public String getShareUrl() {
		return shareUrl;
	}

	/**
	 * Change the folder of the template. You must call {@link #update()} for changes to take effect.
	 * @param folderId the folderId to set
	 */
	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	/**
	 * Set new html content. You must call {@link #update()} for changes to take effect.
	 * @param html the html to set
	 */
	public void setHtml(String html) {
		this.html = html;
	}

	/**
	 * @return the com.github.bananaj.connection to MailChimp
	 */
	public MailChimpConnection getConnection() {
		return connection;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
	public JSONObject getJsonRepresentation() throws Exception {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name",getName() != null ? getName(): "");
		if (getFolderId() != null ) {
			jsonObj.put("folder_id", getFolderId());
		}
		jsonObj.put("html", html != null ? html : "");
		return jsonObj;
	}

	@Override
	public String toString(){
		return 
				"Template Id: " + getId() + " Name: " + getName() + System.lineSeparator() +
				"    Type: " + getType().toString() + System.lineSeparator() +
				"    DragAndDrop: " + isDragAndDrop() + System.lineSeparator() +
				"    Responsive: " + isResponsive() + System.lineSeparator() +
				"    Category: " + getCategory() + System.lineSeparator() +
				"    Date created: " + DateConverter.toLocalString(getDateCreated()) + System.lineSeparator() +
				"    Created By: " + getCreatedBy() + System.lineSeparator() +
				"    Date edited: " + DateConverter.toLocalString(getDateEdited()) + System.lineSeparator() +
				"    Edited By: " + getEditedBy() + System.lineSeparator() +
				"    Active: " + isActive() + System.lineSeparator() +
				"    Folder Id: " + getFolderId() +  System.lineSeparator()+
				"    Share url: "+  getShareUrl();
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
