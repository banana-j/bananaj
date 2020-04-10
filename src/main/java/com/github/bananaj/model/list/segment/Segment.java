package com.github.bananaj.model.list.segment;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.exceptions.SegmentException;
import com.github.bananaj.exceptions.TransportException;
import com.github.bananaj.model.list.member.Member;
import com.github.bananaj.utils.DateConverter;

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
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private SegmentOptions options;
    private String listId;
    private MailChimpConnection connection;

	/**
	 * Construct class given a Mailchimp JSON object
	 * @param connection
	 * @param jsonObj
	 */
	public Segment(MailChimpConnection connection, JSONObject jsonObj) {
		parse(connection, jsonObj);
	}
	
    /**
     * {@link Segment.Builder} model for local construction
     * @see Segment.Builder
     * @param b
     */
    public Segment(Builder b) {
        this.name = b.name;
        this.type = b.type;
        this.connection = b.connection;
    }

	private void parse(MailChimpConnection connection, JSONObject jsonObj) {
        this.connection = connection;
		
        id = jsonObj.getInt("id");
        name = jsonObj.getString("name");
		listId = jsonObj.getString("list_id");
		type = SegmentType.valueOf(jsonObj.getString("type").toUpperCase());
		createdAt = DateConverter.fromISO8601(jsonObj.getString("created_at"));
		updatedAt = DateConverter.fromISO8601(jsonObj.getString("updated_at"));
		memberCount = jsonObj.getInt("member_count");
		options = null;

		// Static segments (tags) and fuzzy segments don’t have conditions
		if ((type != SegmentType.STATIC && type != SegmentType.FUZZY) && jsonObj.has("options")) {
			options = new SegmentOptions(jsonObj.getJSONObject("options"));
		}
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
        connection.do_Post(new URL(connection.getListendpoint()+"/"+this.getListId()+"/segments/"+this.getId()+"/members"), jsonObj.toString(), connection.getApikey());
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
            list = new JSONObject(connection.do_Get(new URL(connection.getListendpoint()+"/"+this.getListId()+"/segments/"+this.getId()+"/members?count="+count+"&offset="+offset),connection.getApikey()));
        } else {
            list = new JSONObject(connection.do_Get(new URL(connection.getListendpoint()+"/"+this.getListId()+"/segments/"+this.getId()+"/members?count="+this.getMemberCount()+"&offset="+offset),connection.getApikey()));
        }

        final JSONArray membersArray = list.getJSONArray("members");


        for (int i = 0 ; i < membersArray.length();i++)
        {
            final JSONObject memberDetail = membersArray.getJSONObject(i);
            Member member = new Member(connection, memberDetail);
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
        connection.do_Delete(new URL(connection.getListendpoint()+"/"+this.getListId()+"/segments/"+this.getId()+"/members/"+member.getId()),connection.getApikey());
    }

    /**
     * Update a segment with a local segment
     * @param updatedSegment
     * @throws Exception
     */
    public void update(Segment updatedSegment) throws Exception {
    	connection.do_Patch(new URL(connection.getListendpoint()+"/"+this.getListId()+"/segments/"+this.getId()),updatedSegment.getJSONRepresentation().toString(),connection.getApikey());
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
	 * @param name Change the name of the segment. You must call {@link #update()}
	 *             for changes to take effect.
	 */
	public void setName(String name) {
		this.name = name;
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
    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    /**
	 * @return The date and time the segment was last updated.
	 */
    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * The conditions of the segment. Static and fuzzy segments don't have conditions.
	 * @return The conditions of the segment. Static segments (tags) and fuzzy segments don’t have conditions.
	 */
    public SegmentOptions getOptions() {
        return options;
    }

    /**
     * The conditions of the segment. Static and fuzzy segments don't have conditions.
	 * @param options Change segment options. You must call {@link #update()} for changes to take effect.
	 */
	public void setOptions(SegmentOptions options) {
		this.options = options;
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
	protected JSONObject getJSONRepresentation() {
		JSONObject json = new JSONObject();

		json.put("name", getName());
		if (getOptions() != null) {
			json.put("options", getOptions().getJsonRepresentation());
		}
		return json;
	}
	
	/**
	 * Batch add/remove list members to static segment
	 * 
	 * @param membersToAdd    Members to add to the static segment. An array of
	 *                        emails to be used for a static segment. Any emails
	 *                        provided that are not present on the list will be
	 *                        ignored.
	 * @param membersToRemove Members to remove from the static segment. An array of
	 *                        emails to be used for a static segment. Any emails
	 *                        provided that are not present on the list will be
	 *                        ignored.
	 * @throws MalformedURLException
	 * @throws TransportException
	 * @throws URISyntaxException
	 */
	public void updateMembers(String [] membersToAdd, String [] membersToRemove) throws MalformedURLException, TransportException, URISyntaxException {
		JSONObject json = new JSONObject();
		if (membersToAdd != null) {
			json.put("members_to_add", membersToAdd);
		}
		if (membersToRemove != null) {
			json.put("members_to_remove", membersToRemove);
		}
		connection.do_Post(new URL(connection.getListendpoint()+"/"+getListId()+"/segments/"+getId()), json.toString(), connection.getApikey());
		// TODO: return response object members_added / members_removed
	}
	
	/**
	 * Update segment via a PATCH operation. Member fields will be freshened.
	 * @param emails An array of emails to be used for a static segment. Any emails provided that are not present on the list will be ignored. Passing an empty array for an existing static segment will reset that segment and remove all members. This field cannot be provided with the options field.
	 * @throws MalformedURLException
	 * @throws TransportException
	 * @throws URISyntaxException
	 */
	public void updateMembers(String [] emails) throws MalformedURLException, TransportException, URISyntaxException {
		JSONObject json = new JSONObject();
		json.put("name", getName());
		json.put("static_segment", emails);
		String results = connection.do_Patch(new URL(connection.getListendpoint()+"/"+getListId()+"/segments/"+getId()), json.toString(), connection.getApikey());
		parse(connection, new JSONObject(results));  // update this object with current data
	}
	
	/**
	 * Delete the segment
	 * @throws URISyntaxException 
	 * @throws TransportException 
	 * @throws MalformedURLException 
	 */
	public void delete() throws MalformedURLException, TransportException, URISyntaxException {
		connection.do_Delete(new URL(connection.getListendpoint()+"/"+getListId()+"/segments/"+getId()), connection.getApikey());
	}
	
	/**
	 * Update segment via a PATCH operation. Member fields will be freshened.
	 * @throws MalformedURLException
	 * @throws TransportException
	 * @throws URISyntaxException
	 */
	public void update() throws MalformedURLException, TransportException, URISyntaxException {
		String results = connection.do_Patch(new URL(connection.getListendpoint()+"/"+getListId()+"/segments/"+getId()), getJSONRepresentation().toString(), connection.getApikey());
		parse(connection, new JSONObject(results));  // update this object with current data
	}
	
    @Override
    public String toString() {
        return  
				"Segment:" + System.lineSeparator() +
        		"    Id: " + getId() +  System.lineSeparator() +
                "    Name: " + getName() +  System.lineSeparator() +
                "    Type: " + getType().toString() + System.lineSeparator() +
                "    List ID: " + getListId() + System.lineSeparator() +
                "    Created at: " + DateConverter.toLocalString(getCreatedAt()) + System.lineSeparator() +
                "    Updated at: " + DateConverter.toLocalString(getUpdatedAt()) +  System.lineSeparator() +
                "    Member count: " +  getMemberCount() +
                (getOptions() != null ? System.lineSeparator() + getOptions().toString() : "");
    }

    /**
     * Builder for {@link Segment}
     *
     */
    public static class Builder {
		private MailChimpConnection connection;
        private String name;
        private SegmentType type;

        public Builder connection(MailChimpConnection connection) {
            this.connection = connection;
            return this;
        }
        
        public Builder name(String s) {
            this.name = s;
            return this;
        }

        public Builder type(SegmentType type) {
            this.type = type;
            return this;
        }
        
        public Segment build() {
            return new Segment(this);
        }
    }
}
