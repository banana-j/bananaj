/**
 * @author alexanderweiss
 * @date 19.11.2015
 */
package model.template;

import java.util.Date;

import org.json.JSONObject;

import model.MailchimpObject;

public class Template extends MailchimpObject  {

	private String templateName;
	private TemplateType templateType;
	private String shareUrl;
	private Date dateCreated;
	
	public Template(int id, String templateName, TemplateType templateType,String shareUrl, Date dateCreated, JSONObject jsonRepresentation) {
		super(String.valueOf(id),jsonRepresentation);
		setTemplateType(templateType);
		setTemplateName(templateName);
		setShareUrl(shareUrl);
		setDateCreated(dateCreated);
	}

	/**
	 * @return the templateType
	 */
	public TemplateType getTemplateType() {
		return templateType;
	}

	/**
	 * @param templateType the templateType to set
	 */
	public void setTemplateType(TemplateType templateType) {
		this.templateType = templateType;
	}

	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * @param templateName the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	@Override
	public String toString(){
		return "Name: " + this.getId() + "-" + this.getTemplateName() + System.lineSeparator() +
				"Type: " + this.getTemplateType().getStringRepresentation() + System.lineSeparator() +
				"Share url: "+  this.getShareUrl() +  System.lineSeparator()+
				"Date created: " + this.getDateCreated() + System.lineSeparator();
	}

	/**
	 * @return the shareUrl
	 */
	public String getShareUrl() {
		return shareUrl;
	}

	/**
	 * @param shareUrl the shareUrl to set
	 */
	private void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	/**
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated the dateCreated to set
	 */
	private void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

}
