package model.list.mergefield;

import model.MailchimpObject;
import org.json.JSONObject;

/**
 * Created by Alexander on 09.08.2016.
 */
public class MergeField extends MailchimpObject {


    private String id;
    private String tag;
    private String name;
    private String type;
    private boolean isRequired;
    private String default_value;
    private boolean isPublic;
    private String listId;
    private MergeFieldOptions mergeFieldOptions;


    public MergeField(String id, String tag, String name, String type, boolean isRequired, String default_value, boolean isPublic, String listId, MergeFieldOptions mergeFieldOptions, JSONObject jsonResponse) {
        super(id, jsonResponse);
        this.tag = tag;
        this.name = name;
        this.type = type;
        this.isRequired = isRequired;
        this.default_value = default_value;
        this.isPublic = isPublic;
        this.listId = listId;
        this.mergeFieldOptions = mergeFieldOptions;
    }


    public String getTag() {
        return tag;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean getRequired() {
        return isRequired;
    }

    public String getDefault_value() {
        return default_value;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public String getListId() {
        return listId;
    }

    public MergeFieldOptions getMergeFieldOptions() {
        return mergeFieldOptions;
    }


    @Override
    public String toString(){
        return "ID: " + super.getId() + System.lineSeparator() +
                "Type: " + this.type + System.lineSeparator() +
                "Tag: " + this.tag + System.lineSeparator() +
                "Options: " + this.mergeFieldOptions;
    }

}
