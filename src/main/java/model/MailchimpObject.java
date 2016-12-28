/**
 * @author alexanderweiss
 * @date 19.11.2015
 */
package model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Super class for all objects retrieved from mailchimp
 * @author alexanderweiss
 *
 */
public class MailchimpObject{

	private String id;
	private JSONObject jsonRepresentation;
	
	public MailchimpObject(String id, JSONObject jsonResponse){
		setId(id);
		setJSONRepresentation(jsonResponse);
	}

	public MailchimpObject () {

	}

	/**
	 * @return the id of this mailchimp object (m5 hash value)
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the jsonRepresentation
	 */
	public JSONObject getJSONRepresentation() {
		return jsonRepresentation;
	}

	/**
	 * @param jsonRepresentation the jsonRepresentation to set
	 * @throws JSONException 
	 */
	public void setJSONRepresentation(JSONObject jsonRepresentation) {
		this.jsonRepresentation = jsonRepresentation;
	}
}
