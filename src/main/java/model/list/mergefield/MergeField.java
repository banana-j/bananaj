package model.list.mergefield;

import model.MailchimpObject;
import org.json.JSONObject;
import org.omg.CORBA.SystemException;

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
        setTag(tag);
        setName(name);
        setType(type);
        setRequired(isRequired);
        setDefault_value(default_value);
        setPublic(isPublic);
        setListId(listId);
        setMergeFieldOptions(mergeFieldOptions);
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getRequired() {
        return isRequired;
    }

    public void setRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }

    public String getDefault_value() {
        return default_value;
    }

    public void setDefault_value(String default_value) {
        this.default_value = default_value;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        isPublic = isPublic;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }


    @Override
    public String toString(){
        return "ID: " + super.getId() + System.lineSeparator() +
                "Type: " + this.type + System.lineSeparator() +
                "Tag: " + this.tag + System.lineSeparator() +
                "Options: " + this.mergeFieldOptions;
    }

    public MergeFieldOptions getMergeFieldOptions() {
        return mergeFieldOptions;
    }

    public void setMergeFieldOptions(MergeFieldOptions mergeFieldOptions) {
        this.mergeFieldOptions = mergeFieldOptions;
    }
}
