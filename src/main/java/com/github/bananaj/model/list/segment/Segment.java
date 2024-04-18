package com.github.bananaj.model.list.segment;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.exceptions.SegmentException;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.model.ModelIterator;
import com.github.bananaj.model.list.member.Member;
import com.github.bananaj.utils.DateConverter;
import com.github.bananaj.utils.URLHelper;

/**
 * Manage segments and tags for a Mailchimp list. A segment is a section of your
 * audience that includes only those subscribers who share specific common field
 * information.
 * 
 * Created by alexanderweiss on 04.02.16.
 */
public class Segment implements JSONParser {

	private int id;
    private String name;
    private int memberCount;
    private SegmentType type;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private SegmentOptions options;
    private String listId;
    private MailChimpConnection connection;

    public Segment() {
    	
    }
    
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

    /**
	 * Parse a JSON representation of segment into this.
	 * @param connection 
	 * @param jsonObj
     */
	public void parse(MailChimpConnection connection, JSONObject jsonObj) {
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
	 * @throws IOException
	 * @throws Exception 
     */
    public void addMember(Member member) throws IOException, Exception {
        if (!this.getType().equals(SegmentType.STATIC)){
            throw new SegmentException();
        }
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("email_address", member.getEmailAddress());
        connection.do_Post(new URL(connection.getListendpoint()+"/"+this.getListId()+"/segments/"+this.getId()+"/members"), jsonObj.toString(), connection.getApikey());
    }

    /**
     * Get members in this list
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @throws IOException
	 * @throws Exception 
     */
    public Iterable<Member> getMembers(int pageSize, int pageNumber) throws IOException, Exception {
		final String baseURL = URLHelper.join(getConnection().getListendpoint(), "/", this.getListId(), "/segments/", Integer.toString(this.getId()), "/members");
		return new ModelIterator<Member>(Member.class, baseURL, getConnection(), pageSize, pageNumber);
    }

    /**
     * Get members in this list
	 * @throws IOException
	 * @throws Exception 
     */
    public Iterable<Member> getMembers() throws IOException, Exception {
		final String baseURL = URLHelper.join(getConnection().getListendpoint(), "/", this.getListId(), "/segments/", Integer.toString(this.getId()), "/members");
		return new ModelIterator<Member>(Member.class, baseURL, getConnection());
    }

    /**
     * Remove a member from this segment, only STATIC segments allowed
     * @param member
	 * @throws IOException
	 * @throws Exception 
     */
    public void removeMember(Member member) throws IOException, Exception {
        if (!this.getType().equals(SegmentType.STATIC)) {
            throw new SegmentException();
        }
        connection.do_Delete(new URL(connection.getListendpoint()+"/"+this.getListId()+"/segments/"+this.getId()+"/members/"+member.getId()),connection.getApikey());
    }

    /**
     * Update a segment with a local segment
     * @param updatedSegment
	 * @throws IOException
	 * @throws Exception 
     */
    public void update(Segment updatedSegment) throws IOException, Exception {
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
	 * @throws IOException
	 * @throws Exception 
	 */
	public void updateMembers(String [] membersToAdd, String [] membersToRemove) throws IOException, Exception {
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
	 * @throws IOException
	 * @throws Exception 
	 */
	public void updateMembers(String [] emails) throws IOException, Exception {
		JSONObject json = new JSONObject();
		json.put("name", getName());
		json.put("static_segment", emails);
		String results = connection.do_Patch(new URL(connection.getListendpoint()+"/"+getListId()+"/segments/"+getId()), json.toString(), connection.getApikey());
		parse(connection, new JSONObject(results));  // update this object with current data
	}
	
	/**
	 * Delete the segment
	 * @throws IOException
	 * @throws Exception 
	 */
	public void delete() throws IOException, Exception {
		connection.do_Delete(new URL(connection.getListendpoint()+"/"+getListId()+"/segments/"+getId()), connection.getApikey());
	}
	
	/**
	 * Update segment via a PATCH operation. Member fields will be freshened.
	 * @throws IOException
	 * @throws Exception 
	 */
	public void update() throws IOException, Exception {
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
