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
        this.unique_email_id = unique_email_id;
        this.list_id = list_id;
        this.action = action;
        this.timestamp = timestamp;
        this.url = url;
        this.type = type;
        this.campaign_id = campaign_id;
        this.title = title;
    }

    /**
     * Actions like unsub do not have a title
     * @param unique_email_id
     * @param list_id
     * @param action
     * @param timestamp
     * @param campaign_id
     */
    public MemberActivity(String unique_email_id, String list_id, String action, String timestamp, String campaign_id){
        this.unique_email_id = unique_email_id;
        this.list_id = list_id;
        this.action = action;
        this.timestamp = timestamp;
        this.url = url;
        this.type = type;
        this.campaign_id = campaign_id;
    }

    public String getUnique_email_id() {
        return unique_email_id;
    }

    public String getList_id() {
        return list_id;
    }

    public String getAction() {
        return action;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public String getCampaign_id() {
        return campaign_id;
    }

    public String getTitle() {
        return title;
    }

    public String getParent_campaign() {
        return parent_campaign;
    }

    @Override
    public String toString(){
        return "Title: " +this.title +
                " Action: " + this.action +
                " Timestamp: " + this.timestamp +System.lineSeparator();
    }
}
