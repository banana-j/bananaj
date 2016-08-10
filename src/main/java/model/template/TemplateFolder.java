package model.template;

import model.MailchimpObject;
import org.json.JSONObject;

/**
 * Class for representing a template folder
 * Created by alexanderweiss on 10.08.2016.
 */
public class TemplateFolder extends MailchimpObject{

    private String name;
    private int count;


    public TemplateFolder(String id, String name, int count, JSONObject jsonResponse) {
        super(id, jsonResponse);
        setName(name);
        setCount(count);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString(){
        return "ID: " + super.getId() + System.lineSeparator() +
                "Name: " + this.getName() + System.lineSeparator() +
                "Count: " + this.getCount();
    }
}
