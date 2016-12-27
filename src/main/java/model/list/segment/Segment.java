package model.list.segment;

import connection.MailChimpConnection;
import exceptions.SegmentException;
import model.MailchimpObject;
import model.list.member.Member;
import model.list.member.MemberStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by alexanderweiss on 04.02.16.
 */
public class Segment extends MailchimpObject {

    private String name;
    private SegmentType type;
    private String list_id;
    private Date created_at;
    private Date updated_at;
    private int member_count;
    private Options options;
    private MailChimpConnection connection;

    public Segment(int id, String name, String list_id, SegmentType type, Date created_at, Date updated_at, int member_count, Options options, MailChimpConnection connection, JSONObject jsonRepresentation){
        super(String.valueOf(id), jsonRepresentation);
        setName(name);
        setList_id(list_id);
        setType(type);
        setCreated_at(created_at);
        setUpdated_at(updated_at);
        setMember_count(member_count);
        setOptions(options);
        setConnection(connection);
    }

    /**
     * Constructor for static segment - no options field
     */
    public Segment(int id, String name, String list_id, SegmentType type, Date created_at, Date updated_at, int member_count, MailChimpConnection connection, JSONObject jsonRepresentation){
        super(String.valueOf(id), jsonRepresentation);
        setName(name);
        setList_id(list_id);
        setType(type);
        setCreated_at(created_at);
        setUpdated_at(updated_at);
        setMember_count(member_count);
        setOptions(options);
        setConnection(connection);
    }

    /**
     * Add a member to this segment, only STATIC segments allowed
     * @param member
     * @throws Exception
     */
    public void addMember(Member member) throws Exception{
        if (!this.getType().equals(SegmentType.STATIC)){
            throw new SegmentException();
        }
        getConnection().do_Post(new URL(connection.getLISTENDPOINT()+"/"+this.getList_id()+"/segments/"+this.getId()+"/members"),member.getJSONRepresentation().toString(),connection.getApikey());
    }

    /**
     * Get all members in this list, with all statuses
     * @return
     * @throws Exception
     */
    public ArrayList<Member> getMembers(int count) throws Exception{

        ArrayList<Member> members = new ArrayList<Member>();
        final JSONObject list;
        if(count != 0){
            list = new JSONObject(getConnection().do_Get(new URL("https://"+connection.getServer()+".api.mailchimp.com/3.0/lists/"+this.getList_id()+"/segments/"+this.getId()+"/members?count="+count),connection.getApikey()));
        } else {
            list = new JSONObject(getConnection().do_Get(new URL("https://"+connection.getServer()+".api.mailchimp.com/3.0/lists/"+this.getList_id()+"/segments/"+this.getId()+"/members?count="+this.getMember_count()),connection.getApikey()));
        }

        final JSONArray membersArray = list.getJSONArray("members");


        for (int i = 0 ; i < membersArray.length();i++)
        {
            final JSONObject memberDetail = membersArray.getJSONObject(i);
            final JSONObject memberMergeTags = memberDetail.getJSONObject("merge_fields");
            final JSONObject memberStats = memberDetail.getJSONObject("stats");

            HashMap<String, Object> merge_fields = new HashMap<String, Object>();

            Iterator a = memberMergeTags.keys();
            while(a.hasNext()) {
                String key = (String)a.next();
                // loop to get the dynamic key
                Object value = memberMergeTags.get(key);
                merge_fields.put(key, value);
            }
            Member member = new Member(memberDetail.getString("id"),connection.getList(this.getList_id()),merge_fields,memberDetail.getString("unique_email_id"), memberDetail.getString("email_address"), MemberStatus.valueOf(memberDetail.getString("status").toUpperCase()),memberDetail.getString("timestamp_signup"),memberDetail.getString("ip_signup"),memberDetail.getString("timestamp_opt"),memberDetail.getString("ip_opt"),memberStats.getDouble("avg_open_rate"),memberStats.getDouble("avg_click_rate"),memberDetail.getString("last_changed"),this.getConnection(),memberDetail);
            members.add(member);

        }
        return members;
    }

    /**
     * Remove a member from this segment, only STATIC segments allowed
     * @param member
     * @throws Exception
     */
    public void removeMember(Member member) throws Exception{
        if (!this.getType().equals(SegmentType.STATIC)){
            throw new SegmentException();
        }
        getConnection().do_Delete(new URL(connection.getLISTENDPOINT()+"/"+this.getList_id()+"/segments/"+this.getId()+"/members/"+member.getId()),connection.getApikey());
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SegmentType getType() {
        return type;
    }

    public void setType(SegmentType type) {
        this.type = type;
    }

    public String getList_id() {
        return list_id;
    }

    public void setList_id(String list_id) {
        this.list_id = list_id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public int getMember_count() {
        return member_count;
    }

    public void setMember_count(int member_count) {
        this.member_count = member_count;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    @Override
    public String toString(){
        return  "ID: " + this.getId() +  System.lineSeparator() +
                "Name: " + this.getName() +  System.lineSeparator() +
                "Type: " + this.getType() + System.lineSeparator() +
                "List ID: " + this.getList_id() + System.lineSeparator() +
                "Created at: " + this.getCreated_at() + System.lineSeparator() +
                "Updated at: " + this.getUpdated_at() +  System.lineSeparator() +
                "Member count: " +  this.getMember_count() + System.lineSeparator() +
                "Options :" +this.getOptions() +  System.lineSeparator();
    }

    public MailChimpConnection getConnection() {
        return connection;
    }

    public void setConnection(MailChimpConnection connection) {
        this.connection = connection;
    }
}
