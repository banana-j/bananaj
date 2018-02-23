package com.github.alexanderwe.bananaj.model.campaign;

import com.github.alexanderwe.bananaj.model.MailchimpObject;
import org.json.JSONObject;

/**
 * Created by alexanderweiss on 10.08.2016.
 */
public class CampaignFolder extends MailchimpObject{


    private String name;
    private int count;


    public CampaignFolder(String id, String name, int count, JSONObject jsonResponse) {
        super(id, jsonResponse);
        this.name = name;
        this.count = count;
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
