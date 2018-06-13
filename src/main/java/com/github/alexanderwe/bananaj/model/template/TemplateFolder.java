package com.github.alexanderwe.bananaj.model.template;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.model.MailchimpObject;

/**
 * Class for representing a template folder
 * Created by alexanderweiss on 10.08.2016.
 */
public class TemplateFolder extends MailchimpObject{

    private String name;
    private int count;


    public TemplateFolder(String id, String name, int count, JSONObject jsonTemplateFolder) {
        super(id, jsonTemplateFolder);
        this.name = name;
        this.count = count;
    }

    public TemplateFolder(JSONObject jsonTemplateFolder) {
        super(jsonTemplateFolder.getString("id"), jsonTemplateFolder);
        this.name = jsonTemplateFolder.getString("name");
        this.count = jsonTemplateFolder.getInt("count");
    }

    public String getName() {
        return name;
    }

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
