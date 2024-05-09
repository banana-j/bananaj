package com.github.bananaj.model.campaign;

import java.net.URL;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.utils.JSONObjectCheck;

/**
 * Created by alexanderweiss on 10.08.2016.
 */
public class CampaignFolder implements JSONParser {

	private String id;
    private String name;
    private Integer count;
    private MailChimpConnection connection;

    public CampaignFolder() {
    	
    }
    
    public CampaignFolder(MailChimpConnection connection, JSONObject jsonCampaignFolder) {
        parse(connection, jsonCampaignFolder);
    }
    
    public void parse(MailChimpConnection connection, JSONObject campaignfolder) {
		JSONObjectCheck jObj = new JSONObjectCheck(campaignfolder);
        this.connection = connection;
        id = jObj.getString("id");
        this.name = jObj.getString("name");
        this.count = jObj.getInt("count");
    }
    
    /**
     * Update a campaign folder
     * @throws Exception 
     */
    public void update() throws Exception {
		JSONObject json = getJsonRepresentation();
		String results = getConnection().do_Patch(new URL(connection.getCampaignfolderendpoint()+"/"+this.getId()), json.toString(), connection.getApikey());
		parse(connection, new JSONObject(results));  // update with current data
    }

    /**
     * Delete a template folder
     * @throws Exception
     */
    public void delete() throws Exception {
    	connection.do_Delete(new URL(connection.getCampaignfolderendpoint() +"/"+getId()), connection.getApikey());
    }
    
    /**
	 * @return A string that uniquely identifies this campaign folder.
	 */
	public String getId() {
		return id;
	}

    /**
     * The name of the folder
     */
    public String getName() {
        return name;
    }

    /**
     * The name of the folder
     * @param name
     */
    public void setName(String name) {
		this.name = name;
	}

	/**
     * The number of campaigns in the folder
     */
    public Integer getCount() {
        return count;
    }

	/**
	 * @return the MailChimp {@link #connection}
	 */
	public MailChimpConnection getConnection() {
		return connection;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
	private JSONObject getJsonRepresentation() throws Exception {
		JSONObject json = new JSONObject();
		json.put("name", getName());
		return json;
	}
	
    @Override
    public String toString(){
        return "ID: " + getId() + System.lineSeparator() +
                "Name: " + getName() + System.lineSeparator() +
                "Count: " + getCount();
    }
}
