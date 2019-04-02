package com.github.alexanderwe.bananaj.model.list.segment;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.alexanderwe.bananaj.connection.MailChimpConnection;
import com.github.alexanderwe.bananaj.exceptions.SegmentException;
import com.github.alexanderwe.bananaj.model.MailchimpObject;
import com.github.alexanderwe.bananaj.model.list.member.Member;
import com.github.alexanderwe.bananaj.model.list.member.MemberStatus;

/**
 * Created by alexanderweiss on 04.02.16.
 */
public class Segment extends MailchimpObject {

    private String name;
    private SegmentType type;
    private String list_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private int member_count;
    private Options options;
    private MailChimpConnection connection;

    public Segment(int id, String name, String list_id, SegmentType type, LocalDateTime created_at, LocalDateTime updated_at, int member_count, Options options, MailChimpConnection connection, JSONObject jsonRepresentation){
        super(String.valueOf(id), jsonRepresentation);
        this.name = name;
        this.list_id = list_id;
        this.type = type;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.member_count = member_count;
        this.options = options;
        this.connection = connection;
    }

    /**
     * Used when created a Segment locally with the Builder class
     * @see Builder
     * @param b
     */
    public Segment(Builder b){
        this.name = b.name;
        this.type = b.type;
        setJSONRepresentation(b.jsonRepresentation);
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
        getConnection().do_Post(new URL(connection.getListendpoint()+"/"+this.getList_id()+"/segments/"+this.getId()+"/members"),member.getJSONRepresentation().toString(),connection.getApikey());
    }

    /**
     * Get all members in this list
     * @param count x first members
     * @param offset skip x first members
     * @return
     * @throws Exception
     */
    public ArrayList<Member> getMembers(int count, int offset) throws Exception{

        ArrayList<Member> members = new ArrayList<Member>();
        final JSONObject list;
        if(count != 0){
            list = new JSONObject(getConnection().do_Get(new URL("https://"+connection.getServer()+".api.mailchimp.com/3.0/lists/"+this.getList_id()+"/segments/"+this.getId()+"/members?count="+count+"&offset="+offset),connection.getApikey()));
        } else {
            list = new JSONObject(getConnection().do_Get(new URL("https://"+connection.getServer()+".api.mailchimp.com/3.0/lists/"+this.getList_id()+"/segments/"+this.getId()+"/members?count="+this.getMember_count()+"&offset="+offset),connection.getApikey()));
        }

        final JSONArray membersArray = list.getJSONArray("members");


        for (int i = 0 ; i < membersArray.length();i++)
        {
            final JSONObject memberDetail = membersArray.getJSONObject(i);
            final JSONObject memberMergeTags = memberDetail.getJSONObject("merge_fields");
            final JSONObject memberStats = memberDetail.getJSONObject("stats");

            HashMap<String, String> merge_fields = new HashMap<String, String>();

            Iterator<String> a = memberMergeTags.keys();
            while(a.hasNext()) {
                String key = a.next();
                // loop to get the dynamic key
                String value = memberMergeTags.getString(key);
                merge_fields.put(key, value);
            }
            Member member = new Member(memberDetail.getString("id"),connection.getList(this.getList_id()),merge_fields, null,memberDetail.getString("unique_email_id"), memberDetail.getString("email_address"), MemberStatus.valueOf(memberDetail.getString("status").toUpperCase()),memberDetail.getString("timestamp_signup"),memberDetail.getString("ip_signup"),memberDetail.getString("timestamp_opt"),memberDetail.getString("ip_opt"),memberStats.getDouble("avg_open_rate"),memberStats.getDouble("avg_click_rate"),memberDetail.getString("last_changed"),this.getConnection(),memberDetail);
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
        getConnection().do_Delete(new URL(connection.getListendpoint()+"/"+this.getList_id()+"/segments/"+this.getId()+"/members/"+member.getId()),connection.getApikey());
    }

    /**
     * Update a segment with a local segment
     * @param updatedSegment
     * @throws Exception
     */
    public void update(Segment updatedSegment) throws Exception{
        getConnection().do_Patch(new URL(connection.getListendpoint()+"/"+this.getList_id()+"/segments/"+this.getId()),updatedSegment.getJSONRepresentation().toString(),connection.getApikey());
    }


    public String getName() {
        return name;
    }

    public SegmentType getType() {
        return type;
    }

    public String getList_id() {
        return list_id;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public int getMember_count() {
        return member_count;
    }

    public Options getOptions() {
        return options;
    }

    public MailChimpConnection getConnection() {
        return connection;
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

    public static class Builder {
        private String name;
        private SegmentType type;
        private JSONObject jsonRepresentation = new JSONObject();

        public Builder name(String s) {
            this.name = s;
            jsonRepresentation.put("name", s);
            return this;
        }

        public Builder type(SegmentType type) {
            this.type = type;
            jsonRepresentation.put("type", type.value());
            return this;
        }
        
        public Segment build() {
            return new Segment(this);
        }
    }
}
