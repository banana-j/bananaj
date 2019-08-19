package com.github.alexanderwe.bananaj.model.list.segment;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.alexanderwe.bananaj.connection.MailChimpConnection;
import com.github.alexanderwe.bananaj.exceptions.SegmentException;
import com.github.alexanderwe.bananaj.model.list.member.Member;
import com.github.alexanderwe.bananaj.utils.DateConverter;

/**
 * Manage segments and tags for a Mailchimp list. A segment is a section of your
 * audience that includes only those subscribers who share specific common field
 * information.
 * 
 * Created by alexanderweiss on 04.02.16.
 */
public class Segment {

	private int id;
    private String name;
    private int memberCount;
    private SegmentType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Options options;
    private String listId;
    private MailChimpConnection connection;
	private JSONObject jsonRepresentation;

	/**
	 * Construct class given a Mailchimp JSON object
	 * @param connection
	 * @param jsonObj
	 */
	public Segment(MailChimpConnection connection, JSONObject jsonObj) {
        this.connection = connection;
		jsonRepresentation = jsonObj;
		
        id = jsonObj.getInt("id");
        name = jsonObj.getString("name");
		listId = jsonObj.getString("list_id");
		type = SegmentType.valueOf(jsonObj.getString("type").toUpperCase());
		createdAt = DateConverter.getInstance().createDateFromISO8601(jsonObj.getString("created_at"));
		updatedAt = DateConverter.getInstance().createDateFromISO8601(jsonObj.getString("updated_at"));
		memberCount = jsonObj.getInt("member_count");
		options = null;

		// Static segments (tags) and fuzzy segments don’t have conditions
		if ((type != SegmentType.STATIC && type != SegmentType.FUZZY) && jsonObj.has("options")) {
			options = new Options(jsonObj.getJSONObject("options"));
		}
	}
	
    /**
     * {@link Segment.Builder} model for local construction
     * @see Segment.Builder
     * @param b
     */
    public Segment(Builder b) {
        this.name = b.name;
        this.type = b.type;
        this.jsonRepresentation = b.jsonRepresentation;
    }

    /**
     * Add a member to this segment, only STATIC segments allowed
     * @param member
     * @throws Exception
     */
    public void addMember(Member member) throws Exception {
        if (!this.getType().equals(SegmentType.STATIC)){
            throw new SegmentException();
        }
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("email_address", member.getEmailAddress());
        getConnection().do_Post(new URL(connection.getListendpoint()+"/"+this.getListId()+"/segments/"+this.getId()+"/members"), jsonObj.toString(), connection.getApikey());
    }

    /**
     * Get all members in this list
     * @param count x first members
     * @param offset skip x first members
     * @throws Exception
     */
    public ArrayList<Member> getMembers(int count, int offset) throws Exception {

        ArrayList<Member> members = new ArrayList<Member>();
        final JSONObject list;
        if(count != 0){
            list = new JSONObject(getConnection().do_Get(new URL("https://"+connection.getServer()+".api.mailchimp.com/3.0/lists/"+this.getListId()+"/segments/"+this.getId()+"/members?count="+count+"&offset="+offset),connection.getApikey()));
        } else {
            list = new JSONObject(getConnection().do_Get(new URL("https://"+connection.getServer()+".api.mailchimp.com/3.0/lists/"+this.getListId()+"/segments/"+this.getId()+"/members?count="+this.getMemberCount()+"&offset="+offset),connection.getApikey()));
        }

        final JSONArray membersArray = list.getJSONArray("members");


        for (int i = 0 ; i < membersArray.length();i++)
        {
            final JSONObject memberDetail = membersArray.getJSONObject(i);
            Member member = new Member(connection.getList(this.getListId()), memberDetail);
            members.add(member);

        }
        return members;
    }

    /**
     * Remove a member from this segment, only STATIC segments allowed
     * @param member
     * @throws Exception
     */
    public void removeMember(Member member) throws Exception {
        if (!this.getType().equals(SegmentType.STATIC)) {
            throw new SegmentException();
        }
        getConnection().do_Delete(new URL(connection.getListendpoint()+"/"+this.getListId()+"/segments/"+this.getId()+"/members/"+member.getId()),connection.getApikey());
    }

    /**
     * Update a segment with a local segment
     * @param updatedSegment
     * @throws Exception
     */
    public void update(Segment updatedSegment) throws Exception {
        getConnection().do_Patch(new URL(connection.getListendpoint()+"/"+this.getListId()+"/segments/"+this.getId()),updatedSegment.getJSONRepresentation().toString(),connection.getApikey());
    }


    /**
	 * @return The unique id for the segment.
	 */
	public int getId() {
		return id;
	}

    /**
	 * @return The name of the segment.
	 */
	public String getName() {
        return name;
    }

    /**
	 * @return The number of active subscribers currently included in the segment.
	 */
    public int getMemberCount() {
        return memberCount;
    }

    /**
	 * @return The type of segment. Static segments are now known as tags.
	 */
    public SegmentType getType() {
        return type;
    }

    /**
	 * @return The date and time the segment was created.
	 */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
	 * @return The date and time the segment was last updated.
	 */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
	 * @return The conditions of the segment. Static segments (tags) and fuzzy segments don’t have conditions.
	 */
    public Options getOptions() {
        return options;
    }

    /**
	 * @return The list id.
	 */
    public String getListId() {
        return listId;
    }

    public MailChimpConnection getConnection() {
        return connection;
    }

	/**
	 * @return the jsonRepresentation
	 */
	public JSONObject getJSONRepresentation() {
		return jsonRepresentation;
	}
	
    @Override
    public String toString() {
        return  
				"Segment:" + System.lineSeparator() +
        		"    Id: " + getId() +  System.lineSeparator() +
                "    Name: " + getName() +  System.lineSeparator() +
                "    Type: " + getType() + System.lineSeparator() +
                "    List ID: " + getListId() + System.lineSeparator() +
                "    Created at: " + getCreatedAt() + System.lineSeparator() +
                "    Updated at: " + getUpdatedAt() +  System.lineSeparator() +
                "    Member count: " +  getMemberCount() +
                (getOptions() != null ? System.lineSeparator() + getOptions().toString() : "");
    }

    /**
     * Builder for {@link Segment}
     *
     */
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
            jsonRepresentation.put("type", type.getStringRepresentation());
            return this;
        }
        
        public Segment build() {
            return new Segment(this);
        }
    }
}
