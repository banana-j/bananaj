package model.list.member;

/**
 * Created by alexanderweiss on 20.01.16.
 */
public class MemberActivity {

    String unique_email_id;
    String list_id;
    String action;
    String timestamp;
    String url;
    String type;
    String campaign_id;
    String title;
    String parent_campaign;

    public MemberActivity(String unique_email_id, String list_id, String action, String timestamp, String campaign_id, String title){
        setUnique_email_id(unique_email_id);
        setList_id(list_id);
        setAction(action);
        setTimestamp(timestamp);
        setUrl(url);
        setType(type);
        setCampaign_id(campaign_id);
        setTitle(title);
        setParent_campaign(parent_campaign);
    }



    public String getUnique_email_id() {
        return unique_email_id;
    }

    public void setUnique_email_id(String unique_email_id) {
        this.unique_email_id = unique_email_id;
    }

    public String getList_id() {
        return list_id;
    }

    public void setList_id(String list_id) {
        this.list_id = list_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCampaign_id() {
        return campaign_id;
    }

    public void setCampaign_id(String campaign_id) {
        this.campaign_id = campaign_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParent_campaign() {
        return parent_campaign;
    }

    public void setParent_campaign(String parent_campaign) {
        this.parent_campaign = parent_campaign;
    }

    @Override
    public String toString(){
        return "Title: " +this.title +
                " Action: " + this.action +
                " Timestamp: " + this.timestamp +System.lineSeparator();
    }
}
