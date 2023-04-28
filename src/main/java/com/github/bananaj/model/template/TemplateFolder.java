package com.github.bananaj.model.template;

import java.net.URL;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.model.JSONParser;

/**
 * Class for representing a template folder
 * Created by alexanderweiss on 10.08.2016.
 */
public class TemplateFolder implements JSONParser {

	private String id;
    private String name;
    private int count;
	private MailChimpConnection connection;

	public TemplateFolder() {
		
	}
	
    public TemplateFolder(MailChimpConnection connection, JSONObject jsonObj) {
    	parse(connection, jsonObj);
    }

    public void parse(MailChimpConnection connection, JSONObject jsonObj) {
        id = jsonObj.getString("id");
        name = jsonObj.getString("name");
        count = jsonObj.getInt("count");
        this.connection = connection;
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
	 * Delete template folder
	 * @throws Exception
	 */
	public void delete() throws Exception {
		getConnection().do_Delete(new URL(getConnection().getTemplateendpoint()+"/"+getId()), getConnection().getApikey() );
	}
	
    /**
     * 
     * @return The name of the folder.
     */
    public String getName() {
        return name;
    }

    /**
	 * @return A string that uniquely identifies this template folder.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param name the template name to set. You must call {@link #update()} for change to take effect.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
     * 
     * @return The number of templates in the folder.
     */
    public int getCount() {
        return count;
    }

	/**
	 * @return the connection
	 */
	public MailChimpConnection getConnection() {
		return connection;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
	public JSONObject getJsonRepresentation() throws Exception {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", getName());
		return jsonObj;
	}

    @Override
    public String toString(){
        return "Id: " + getId() + " Name: " + getName() + "Count: " + getCount();
    }
}
