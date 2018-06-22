package com.github.alexanderwe.bananaj.model.campaign;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.model.MailchimpObject;

/**
 * Created by alexanderweiss on 10.08.2016.
 */
public class CampaignFolder extends MailchimpObject{


    private String name;
    private int count;


    public CampaignFolder(String id, String name, int count, JSONObject jsonCampaignFolder) {
        super(id, jsonCampaignFolder);
        this.name = name;
        this.count = count;
    }

    public CampaignFolder(JSONObject jsonCampaignFolder) {
        super(jsonCampaignFolder.getString("id"), jsonCampaignFolder);
        this.name = jsonCampaignFolder.getString("name");
        this.count = jsonCampaignFolder.getInt("count");
    }
    
    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public int getCount() {
        return count;
    }

    @Override
    public String toString(){
        return "ID: " + super.getId() + System.lineSeparator() +
                "Name: " + this.getName() + System.lineSeparator() +
                "Count: " + this.getCount();
    }
}
