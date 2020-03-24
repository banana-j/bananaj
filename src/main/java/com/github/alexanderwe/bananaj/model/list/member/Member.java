/**
 * @author alexanderweiss
 * @date 06.11.2015
 */
package com.github.alexanderwe.bananaj.model.list.member;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.alexanderwe.bananaj.connection.MailChimpConnection;
import com.github.alexanderwe.bananaj.exceptions.TransportException;
import com.github.alexanderwe.bananaj.model.list.MailChimpList;
import com.github.alexanderwe.bananaj.utils.DateConverter;
import com.github.alexanderwe.bananaj.utils.MD5;


/**
 * Object for representing a mailchimp member
 * @author alexanderweiss
 *
 */
public class Member {

	private MailChimpList mailChimpList;
	private String id;
	private String emailAddress;
	private String uniqueEmailId;
	private EmailType emailType;
	private MemberStatus status;
	private String unsubscribeReason;
	private Map<String, Object> mergeFields;
	private Map<String, Boolean> interest;
	private MemberStats stats;
	private String ipSignup;
	private LocalDateTime timestampSignup;
	private String ipOpt;
	private LocalDateTime timestampOpt;
	private int rating;
	private LocalDateTime lastChanged;
	private String language;
	private boolean vip;
	private String emailClient;
	//private MemberLocation location;
	//private List<MemberMarketingPermissions> marketingPermissions;
	private LastNote lastNote;
	private int tagsCount;
	private List<MemberTag> tags;
	private String listId;
	
	private MemberStatus statusIfNew;

	public Member(MailChimpList mailChimpList, JSONObject member) {
		parse(mailChimpList, member);
	}

	public Member(Builder b) {
		mailChimpList = b.mailChimpList;
		emailAddress = b.emailAddress;
		id = Member.subscriberHash(emailAddress);
		emailType = b.emailType;
		status = b.status;
		mergeFields = b.mergeFields;
		interest = b.interest;
		ipSignup = b.ipSignup;
		timestampSignup = b.timestampSignup;
		ipOpt = b.ipOpt;
		timestampOpt = b.timestampOpt;
		language = b.language;
		vip = b.vip;
		//location = b.location;
		//marketing_permissions = b.marketingPermissions;
		//last_note = b.lastNote;
		tags = b.tags;
		listId = mailChimpList.getId();
		statusIfNew = b.statusIfNew;
	}

	public Member() {

	}

	/**
	 * Parse a JSON representation of a member into this.
	 * @param member
	 */
	public void parse(MailChimpList mailChimpList, JSONObject member) {
		this.mailChimpList = mailChimpList;
        id = member.getString("id");
		emailAddress = member.getString("email_address");
		uniqueEmailId = member.getString("unique_email_id");
		emailType =  EmailType.valueOf(member.getString("email_type").toUpperCase());
		status = MemberStatus.valueOf(member.getString("status").toUpperCase());
		unsubscribeReason = member.has("unsubscribe_reason") ? member.getString("unsubscribe_reason") : null;
		
		mergeFields = new HashMap<String, Object>();
		if (member.has("merge_fields")) {
			final JSONObject mergeFieldsObj = member.getJSONObject("merge_fields");
			for(String key : mergeFieldsObj.keySet()) {
				mergeFields.put(key, mergeFieldsObj.get(key));
			}
		}
		
		interest = new HashMap<String, Boolean>();
		if (member.has("interests")) {
			final JSONObject interests = member.getJSONObject("interests");
			for(String key : interests.keySet()) {
				interest.put(key, interests.getBoolean(key));
			}
		}
		
		stats = new MemberStats(member.getJSONObject("stats"));
		ipSignup = member.getString("ip_signup");
		timestampSignup = DateConverter.getInstance().createDateFromISO8601(member.getString("timestamp_signup"));
		rating = member.getInt("member_rating");
		ipOpt = member.getString("ip_opt");
		timestampOpt = DateConverter.getInstance().createDateFromISO8601(member.getString("timestamp_opt"));
		lastChanged = DateConverter.getInstance().createDateFromISO8601(member.getString("last_changed"));
		language = member.getString("language");
		vip = member.getBoolean("vip");
		emailClient = member.has("email_client") ? member.getString("email_client") : null;
		//location
		//marketing_permissions
		lastNote = member.has("last_note") ? new LastNote(member.getJSONObject("last_note")) : null;

		tagsCount = member.getInt("tags_count");
		tags = new ArrayList<MemberTag>(tagsCount);
		final JSONArray tagsArray = member.getJSONArray("tags");
		for(int i = 0; i < tagsArray.length(); i++) {
			tags.add(new MemberTag(tagsArray.getJSONObject(i)));
		}

		listId = member.getString("list_id");
	}

	/**
	 * Remove this list member
	 * @throws URISyntaxException 
	 * @throws TransportException 
	 * @throws MalformedURLException 
	 */
	public void delete() throws MalformedURLException, TransportException, URISyntaxException {
		getConnection().do_Delete(new URL(getConnection().getListendpoint()+"/"+getMailChimpList().getId()+"/members/"+getId()), getConnection().getApikey());
	}
	
	/**
	 * Permanently delete this list member
	 * @throws MalformedURLException
	 * @throws TransportException
	 * @throws URISyntaxException
	 */
	public void deletePermanent() throws MalformedURLException, TransportException, URISyntaxException {
		getConnection().do_Post(new URL(getConnection().getListendpoint()+"/"+getMailChimpList().getId()+"/members/"+getId()+"/actions/delete-permanent"), getConnection().getApikey());
	}
	
	/**
	 * Change this subscribers email address.
	 * @param emailAddress
	 * @throws URISyntaxException 
	 * @throws TransportException 
	 * @throws MalformedURLException 
	 */
	public void changeEmailAddress(String emailAddress) throws MalformedURLException, TransportException, URISyntaxException {
		JSONObject updateMember = new JSONObject();
		updateMember.put("email_address", emailAddress);
		String results = getConnection().do_Patch(new URL(getConnection().getListendpoint()+"/"+getMailChimpList().getId()+"/members/"+getId()), updateMember.toString(), getConnection().getApikey());
		parse(mailChimpList, new JSONObject(results));  // update member object with current data
	}

	/**
	 * Change the status of the subscriber.
	 * @param status
	 * @throws URISyntaxException 
	 * @throws TransportException 
	 * @throws MalformedURLException 
	 */
	public void changeStatus(MemberStatus status) throws MalformedURLException, TransportException, URISyntaxException {
		JSONObject updateMember = new JSONObject();
		updateMember.put("status", status.getStringRepresentation());
		String results = getConnection().do_Patch(new URL(getConnection().getListendpoint()+"/"+ getMailChimpList().getId()+"/members/"+getId()), updateMember.toString(), getConnection().getApikey());
		parse(mailChimpList, new JSONObject(results));  // update member object with current data
	}

	/**
	 * Update subscriber via a PATCH operation. Member fields will be freshened
	 * from MailChimp.
	 * @throws URISyntaxException 
	 * @throws TransportException 
	 * @throws MalformedURLException 
	 */
	public void update() throws MalformedURLException, TransportException, URISyntaxException {
		mailChimpList.updateMember(this);
	}
	
	/**
	 * Add or update a list member via a PUT operation. When a new member is added
	 * and no status_if_new has been specified SUBSCRIBED will be used. Member
	 * fields will be freshened from mailchimp.
	 * @throws URISyntaxException 
	 * @throws TransportException 
	 * @throws MalformedURLException 
	 * 
	 */
	public void addOrUpdate() throws MalformedURLException, TransportException, URISyntaxException {
		mailChimpList.addOrUpdateMember(this);
	}
	
	/**
	 * Add or remove tags from this list member. If a tag that does not exist is passed in and set as ‘active’, a new tag will be created.
	 * 
	 * @param tagName The name of the tag.
	 * @param status The status for the tag on the member, pass in active to add a tag or inactive to remove it.
	 * @throws URISyntaxException 
	 * @throws TransportException 
	 * @throws MalformedURLException 
	 */
	public void applyTag(String tagName, TagStatus status) throws MalformedURLException, TransportException, URISyntaxException {
		Map<String, TagStatus> tagsMap = new HashMap<String, TagStatus>(1);
		tagsMap.put(tagName, status);
		applyTags(tagsMap);
	}
	
	/**
	 * Add or remove tags in bulk from this list member. If a tag that does not exist is passed in and set as ‘active’, a new tag will be created.
	 * @param tagsMap
	 * @throws URISyntaxException 
	 * @throws TransportException 
	 * @throws MalformedURLException 
	 */
	public void applyTags(Map<String, TagStatus> tagsMap) throws MalformedURLException, TransportException, URISyntaxException {
		JSONObject tagObj = new JSONObject();
		JSONArray tagsArray = new JSONArray();
		for(Entry<String, TagStatus> e : tagsMap.entrySet()) {
			tagsArray.put(new JSONObject()
					.put("name", e.getKey())
					.put("status", e.getValue().getStringRepresentation()));
			
			Optional<MemberTag> optional = tags.stream()
					.filter(t -> e.getKey().equals(t.getName()))
					.findFirst();
			
			if (optional.isPresent() && e.getValue() == TagStatus.INACTIVE) {
				tags.remove(optional.get());
			} else if (!optional.isPresent() && e.getValue() == TagStatus.ACTIVE) {
				tags.add(new MemberTag(e.getKey()));
			}
		}
		tagObj.put("tags",tagsArray);
		getConnection().do_Post(new URL(getConnection().getListendpoint()+"/"+mailChimpList.getId()+"/members/"+getId()+"/tags"), tagObj.toString(), getConnection().getApikey());
	}
	
	/**
	 * @param tagName A tag name to check for
	 * @return true if the member has the specified tag name.
	 */
	public boolean hasTag(String tagName) {
		Optional<MemberTag> optional = tags.stream()
				.filter(t -> tagName.equals(t.getName()))
				.findFirst();
		return optional.isPresent();
	}
	
	/**
	 * Get the last 50 events of a member’s activity, including opens, clicks, and unsubscribes.
	 * 
	 * @return the member activities
	 * @throws URISyntaxException 
	 * @throws TransportException 
	 * @throws MalformedURLException 
	 * @throws JSONException 
	 */
	public List<MemberActivity> getActivities() throws JSONException, MalformedURLException, TransportException, URISyntaxException {
		List<MemberActivity> activities = new ArrayList<MemberActivity>();

		final JSONObject activity = new JSONObject(getConnection().do_Get(new URL(getConnection().getListendpoint()+"/"+mailChimpList.getId()+"/members/"+getId()+"/activity?count=50&offset=0"), getConnection().getApikey()));
		//String email_id = activity.getString("email_id");
		//String list_id = activity.getString("list_id");
		//int total_items = activity.getInt("total_items");
		final JSONArray activityArray = activity.getJSONArray("activity");

		for (int i = 0 ; i < activityArray.length();i++)
		{
			activities.add(new MemberActivity(activityArray.getJSONObject(i)));
		}

		return activities;
	}

	/**
	 * Get recent notes for this list member.
	 * @param count Number of items to return
	 * @param offset Zero based offset
	 * @throws URISyntaxException 
	 * @throws TransportException 
	 * @throws MalformedURLException 
	 * @throws JSONException 
	 */
	public List<MemberNote> getNotes(int count, int offset) throws JSONException, MalformedURLException, TransportException, URISyntaxException {
		List<MemberNote> notes = new ArrayList<MemberNote>();

		final JSONObject noteObj = new JSONObject(getConnection().do_Get(new URL(getConnection().getListendpoint()+"/"+mailChimpList.getId()+"/members/"+getId()+"/notes?count="+count+"&offset="+offset), getConnection().getApikey()));
		//String email_id = noteObj.getString("email_id");
		//String list_id = noteObj.getString("list_id");
		//int total_items = noteObj.getInt("total_items");
		final JSONArray noteArray = noteObj.getJSONArray("notes");

		for (int i = 0 ; i < noteArray.length();i++)
		{
			notes.add(new MemberNote(noteArray.getJSONObject(i)));
		}

		return notes;
	}
	
	/**
	 * Get a specific note for the member
	 * @param noteId The id for the note.
	 * @throws JSONException
	 * @throws MalformedURLException
	 * @throws TransportException
	 * @throws URISyntaxException
	 */
	public MemberNote getNote(int noteId) throws JSONException, MalformedURLException, TransportException, URISyntaxException {
		final JSONObject noteObj = new JSONObject(getConnection().do_Get(new URL(getConnection().getListendpoint()+"/"+mailChimpList.getId()+"/members/"+getId()+"/notes/"+noteId), getConnection().getApikey()));
		return new MemberNote(noteObj);
	}
	
	/**
	 * Delete a note
	 * @param noteId The id for the note to delete.
	 * @throws MalformedURLException
	 * @throws TransportException
	 * @throws URISyntaxException
	 */
	public void deleteNote(int noteId) throws MalformedURLException, TransportException, URISyntaxException {
		getConnection().do_Delete(new URL(getConnection().getListendpoint()+"/"+mailChimpList.getId()+"/members/"+getId()+"/notes/"+noteId), getConnection().getApikey());
	}
	
	/**
	 * Add a new note to this subscriber.
	 * @param note The content of the note. Note length is limited to 1,000 characters.
	 * @throws JSONException
	 * @throws MalformedURLException
	 * @throws TransportException
	 * @throws URISyntaxException
	 */
	public MemberNote createNote(String note) throws JSONException, MalformedURLException, TransportException, URISyntaxException {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("note", note);
		final JSONObject noteObj = new JSONObject(getConnection().do_Post(new URL(getConnection().getListendpoint()+"/"+mailChimpList.getId()+"/members/"+getId()+"/notes"), jsonObj.toString(), getConnection().getApikey()));
		return new MemberNote(noteObj);
	}
	
	/**
	 * Update a specific note for this list member.
	 * @param noteId The id for the note to update.
	 * @param note The new content for the note. Note length is limited to 1,000 characters.
	 * @throws JSONException
	 * @throws MalformedURLException
	 * @throws TransportException
	 * @throws URISyntaxException
	 */
	public MemberNote updateNote(int noteId, String note) throws JSONException, MalformedURLException, TransportException, URISyntaxException {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("note", note);
		final JSONObject noteObj = new JSONObject(getConnection().do_Patch(new URL(getConnection().getListendpoint()+"/"+mailChimpList.getId()+"/members/"+getId()+"/notes/"+noteId), jsonObj.toString(), getConnection().getApikey()));
		return new MemberNote(noteObj);
	}
	
    /**
	 * @return The MD5 hash of the lowercase version of the list member’s email address.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Email address for this subscriber
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Change this subscribers email address. You must call {@link #update()},
	 * {@link #addOrUpdate()},
	 * {@link MailChimpList#addOrUpdateMember(Member)}, or
	 * {@link MailChimpList#updateMember(Member)} for changes to take effect.
	 * 
	 * @param emailAddress The new Email address for this subscriber.
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * An identifier for the address across all of Mailchimp
	 */
	public String getUniqueEmailId() {
		return uniqueEmailId;
	}

	/**
	 * Type of email this member asked to get (‘html’ or ‘text’)
	 */
	public EmailType getEmailType() {
		return emailType;
	}

	/**
	 * Type of email this member asked to get (‘html’ or ‘text’). You must call
	 * {@link #update()}, {@link #addOrUpdate()},
	 * {@link MailChimpList#addOrUpdateMember(Member)}, or
	 * {@link MailChimpList#updateMember(Member)} for changes to take effect.
	 * 
	 * @param emailType
	 */
	public void setEmailType(EmailType emailType) {
		this.emailType = emailType;
	}

	/**
	 * Subscriber’s current status 
	 */
	public MemberStatus getStatus() {
		return status;
	}

	/**
	 * Subscriber’s current status. You must call {@link #update()},
	 * {@link #addOrUpdate()},
	 * {@link MailChimpList#addOrUpdateMember(Member)}, or
	 * {@link MailChimpList#updateMember(Member)} for changes to take effect.
	 */
	public void setStatus(MemberStatus status) {
		this.status = status;
	}

	/**
	 * @return A subscriber’s reason for unsubscribing.
	 */
	public String getUnsubscribeReason() {
		return unsubscribeReason;
	}

	/**
	 * Subscriber’s status. This value is required only when calling
	 * {@link MailChimpList#addOrUpdateMember(Member)} or {@link #update()}.
	 * 
	 * @return the status_if_new
	 */
	public MemberStatus getStatusIfNew() {
		return statusIfNew;
	}

	/**
	 * Set the status for a new member when created through a call to
	 * {@link MailChimpList#addOrUpdateMember(Member)} or {@link #addOrUpdate()}.
	 * 
	 * @param statusIfNew
	 */
	public void setStatusIfNew(MemberStatus statusIfNew) {
		this.statusIfNew = statusIfNew;
	}

	/**
	 * Audience merge tags that corresponds to the data in an audience field.
	 * @return a Map of all merge field name value pairs
	 */
	public Map<String, Object> getMergeFields() {
		return mergeFields;
	}

	/**
	 * Add or update an audience merge tags that corresponds to the data in an
	 * audience field. You must call {@link #update()},
	 * {@link #addOrUpdate()},
	 * {@link MailChimpList#addOrUpdateMember(Member)}, or
	 * {@link MailChimpList#updateMember(Member)} for changes to take effect.
	 * 
	 * @param key
	 * @param object
	 * @return the previous value associated with key, or null if there was none.
	 */
	public Object putMergeFields(String key, String object) {
		return mergeFields.put(key, object);
	}

	/**
	 * The members collection of interests. 
	 * @return the member interests. The map key is the interest/segment identifier and value is the subscription boolean.
	 */
	public Map<String, Boolean> getInterest() {
		return interest;
	}

	/**
	 * Add or update an interest. You must call {@link #update()},
	 * {@link #addOrUpdate()}, {@link MailChimpList#addOrUpdateMember(Member)}, or
	 * {@link MailChimpList#updateMember(Member)} for changes to take effect.
	 * 
	 * @param id     The interest id
	 * @param active
	 * @return The previous value associated with id, or null if there was none.
	 */
	public Boolean putInterest(String id, boolean active) {
		return interest.put(id, active);
	}
	
	/**
	 * Open and click rates for this subscriber.
	 */
	public MemberStats getStats() {
		return stats;
	}

	/**
	 * IP address the subscriber signed up from.
	 */
	public String getIpSignup() {
		return ipSignup;
	}

	/**
	 * IP address the subscriber signed up from. You must call {@link #update()},
	 * {@link #addOrUpdate()}, {@link MailChimpList#addOrUpdateMember(Member)}, or
	 * {@link MailChimpList#updateMember(Member)} for changes to take effect.
	 * 
	 * @param ipSignup the ipSignup to set
	 */
	public void setIpSignup(String ipSignup) {
		this.ipSignup = ipSignup;
	}

	/**
	 * The date and time the subscriber signed up for the list.
	 */
	public LocalDateTime getTimestampSignup() {
		return timestampSignup;
	}

	/**
	 * The date and time the subscriber signed up for the list. You must call
	 * {@link #update()}, {@link #addOrUpdate()},
	 * {@link MailChimpList#addOrUpdateMember(Member)}, or
	 * {@link MailChimpList#updateMember(Member)} for changes to take effect.
	 * 
	 * @param timestampSignup the timestampSignup to set
	 */
	public void setTimestampSignup(LocalDateTime timestampSignup) {
		this.timestampSignup = timestampSignup;
	}

	/**
	 * The IP address the subscriber used to confirm their opt-in status.
	 */
	public String getIpOpt() {
		return ipOpt;
	}

	/**
	 * The IP address the subscriber used to confirm their opt-in status. You must
	 * call {@link #update()}, {@link #addOrUpdate()},
	 * {@link MailChimpList#addOrUpdateMember(Member)}, or
	 * {@link MailChimpList#updateMember(Member)} for changes to take effect.
	 * 
	 * @param ipOpt the ipOpt to set
	 */
	public void setIpOpt(String ipOpt) {
		this.ipOpt = ipOpt;
	}

	/**
	 * The date and time the subscribe confirmed their opt-in status.
	 */
	public LocalDateTime getTimestampOpt() {
		return timestampOpt;
	}

	/**
	 * The date and time the subscribe confirmed their opt-in status. You must call
	 * {@link #update()}, {@link #addOrUpdate()},
	 * {@link MailChimpList#addOrUpdateMember(Member)}, or
	 * {@link MailChimpList#updateMember(Member)} for changes to take effect.
	 * 
	 * @param timestampOpt the timestampOpt to set
	 */
	public void setTimestampOpt(LocalDateTime timestampOpt) {
		this.timestampOpt = timestampOpt;
	}

	/**
	 * Star rating for this member, between 1 and 5
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * @return The date and time the member’s info was last changed
	 */
	public LocalDateTime getLastChanged() {
		return lastChanged;
	}

	/**
	 * If set/detected, the subscriber’s language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * set/change the subscriber’s language. You must call {@link #update()},
	 * {@link #addOrUpdate()}, {@link MailChimpList#addOrUpdateMember(Member)}, or
	 * {@link MailChimpList#updateMember(Member)} for changes to take effect.
	 * 
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * VIP status for subscriber
	 */
	public boolean isVip() {
		return vip;
	}

	/**
	 * Set VIP status for subscriber. You must call {@link #update()},
	 * {@link #addOrUpdate()}, {@link MailChimpList#addOrUpdateMember(Member)}, or
	 * {@link MailChimpList#updateMember(Member)} for changes to take effect.
	 * 
	 * @param vip the vip to set
	 */
	public void setVip(boolean vip) {
		this.vip = vip;
	}

	/**
	 * The list member’s email client
	 */
	public String getEmailClient() {
		return emailClient;
	}

	/**
	 * @return The most recent Note added about this member.
	 */
	public LastNote getLastNote() {
		return lastNote;
	}

	/**
	 * The number of tags applied to this member
	 */
	public int getTagsCount() {
		return tagsCount;
	}

	/**
	 * @return The tags applied to this member.
	 */
	public List<MemberTag> getTags() {
		return tags;
	}


	/**
	 * The list id
	 */
	public String getListId() {
		return listId;
	}

	/**
	 * @return the list
	 */
	public MailChimpList getMailChimpList() {
		return mailChimpList;
	}

	/**
	 * Add/Update an interests subscription
	 * @param key
	 * @param subscribe
	 * @return the previous value associated with key, or null if there was none.)
	 */
	public Boolean putInterest(String key, Boolean subscribe) {
		return interest.put(key, subscribe);
	}

	/**
	 * @return the MailChimp com.github.alexanderwe.bananaj.connection
	 */
	public MailChimpConnection getConnection() {
		return mailChimpList.getConnection();
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
	public JSONObject getJsonRepresentation() {
		JSONObject json = new JSONObject();
		json.put("email_address", getEmailAddress());
		
		if (getStatusIfNew() != null) {
			// used by PUT 'Add or update a list member'
			json.put("status_if_new", getStatusIfNew().getStringRepresentation());
		}
		
		if (getEmailType() != null) {
			json.put("email_type", getEmailType().getStringRepresentation());
		}
		if (getStatus() != null) {
			json.put( "status", getStatus().getStringRepresentation());
		}

		{
			JSONObject mergeFields = new JSONObject();
			Map<String, Object> mergeFieldsMap = getMergeFields();
			for (String key : mergeFieldsMap.keySet()) {
				mergeFields.put(key, mergeFieldsMap.get(key));
			}
			json.put("merge_fields", mergeFields);
		}

		{
			JSONObject interests = new JSONObject();
			Map<String, Boolean> interestsMap = getInterest();
			for (String key : interestsMap.keySet()) {
				interests.put(key, interestsMap.get(key));
			}
			json.put("interests",interests);
		}

		json.put("language", getLanguage());
		json.put("vip", isVip());
		
		// location
		// marketing_permissions

		if (ipSignup != null && ipSignup.length() > 0) {
			json.put("ip_signup", getIpSignup());
		}

		if (getTimestampSignup() != null) {
			json.put("timestamp_signup", DateConverter.toNormal(getTimestampSignup()));
		}
		
		if (ipOpt != null && ipOpt.length() > 0) {
			json.put( "ip_opt", getIpOpt());
		}
		
		if (getTimestampOpt() != null) {
			json.put("timestamp_opt", DateConverter.toNormal(getTimestampOpt()));
		}
		
		// tags used by POST 'Add a new list member'
		if (tags != null && tags.size() > 0 ) {
			JSONArray tagsArray = new JSONArray();
			for(MemberTag t: tags) {
				tagsArray.put(t.getJsonRepresentation());
			}
			json.put("tags", tagsArray);
		}

		return json;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("    Merge Fields:").append(System.lineSeparator());
		for (Entry<String, Object> pair : getMergeFields().entrySet()) {
			stringBuilder.append("        ").append(pair.getKey()).append(": ").append(pair.getValue()).append(System.lineSeparator());
		}
		if (tags != null && tags.size() > 0) {
			for (MemberTag tagObj : tags) {
				stringBuilder.append("    ").append(tagObj.toString()).append(System.lineSeparator());
			}
		}
		if (interest != null && interest.size() > 0) {
			stringBuilder.append("    Interests:").append(System.lineSeparator());
			for (Entry<String, Boolean> pair : interest.entrySet()) {
				stringBuilder.append("        ").append(pair.getKey()).append(":").append(pair.getValue().toString()).append(System.lineSeparator());
			}
		}

		return 
				"Member:" + System.lineSeparator() +
				"    Id: " + getId() + System.lineSeparator() +
				"    Email: " + getEmailAddress() + System.lineSeparator() +
				"    Email Id: " + getUniqueEmailId() + System.lineSeparator() +
				(getEmailType() != null ? "    Email Type: " + getEmailType().getStringRepresentation() + System.lineSeparator() : "") +
				(getStatus() != null ? "    Status: " + getStatus().getStringRepresentation() + System.lineSeparator() : "") +
				"    List Id: " + getListId() + System.lineSeparator() +
				"    Signup Timestamp: " + getTimestampSignup() + System.lineSeparator() +
				"    Signup IP: " + getIpSignup() + System.lineSeparator() +
				"    Opt-in IP: " + getIpOpt() + System.lineSeparator() +
				"    Opt-in Timestamp: " + getTimestampOpt() + System.lineSeparator() +
				"    Member Rating: " + getRating() + System.lineSeparator() +
				"    Last Changed: " + getLastChanged() + System.lineSeparator() +
				"    Language: " + getLanguage() + System.lineSeparator() +
				"    VIP: " + isVip() + System.lineSeparator() +
				(getEmailClient() != null ? "    Email Client: " + getEmailClient() + System.lineSeparator() : "") +
				(getLastNote() != null ? getLastNote().toString() + System.lineSeparator() : "") +
				//getStats().toString() + System.lineSeparator() +
				stringBuilder.toString();
	}

	/**
	 * Generate mailchimp subscriber hash from email adddress.  
	 * @param emailAddress
	 * @return The MD5 hash of the lowercase version of the email address.
	 */
	public static String subscriberHash(String emailAddress) {
		return MD5.getMD5(emailAddress.toLowerCase());
	}

	public static class Builder {
		private MailChimpList mailChimpList;
		private String emailAddress;
		private EmailType emailType;
		private MemberStatus status;
		private Map<String, Object> mergeFields = new HashMap<String, Object>();;
		private Map<String, Boolean> interest = new HashMap<String, Boolean>();
		private String language;
		private boolean vip;
		//private MemberLocation location;
		//private List<MemberMarketingPermissions> marketing_permissions;
		private String ipSignup;
		private LocalDateTime timestampSignup;
		private String ipOpt;
		private LocalDateTime timestampOpt;
		private List<MemberTag> tags = new ArrayList<MemberTag>();
		private MemberStatus statusIfNew;

		public Member build() {
			return new Member(this);
		}

		public Builder list(MailChimpList mailChimpList) {
			this.mailChimpList = mailChimpList;
			return this;
		}

		public Builder emailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
			return this;
		}

		public Builder emailType(EmailType emailType) {
			this.emailType = emailType;
			return this;
		}

		public Builder status(MemberStatus status) {
			this.status = status;
			return this;
		}

		public Builder mergeFields(Map<String, Object> mergeFields) {
			this.mergeFields = mergeFields;
			return this;
		}
		
		public Builder mergeField(String key, Object value) {
			if (mergeFields == null) {
				mergeFields = new HashMap<String, Object>();
			}
			mergeFields.put(key, value);
			return this;
		}

		/**
		 * Adds a merge field var and value.
		 * 
		 * @param var
		 * @param value
		 */
		public Builder withMergeField(String var, String value) {
			mergeFields.put(var, value);
			return this;
		}

		public Builder memberInterest(Map<String, Boolean> memberInterest) {
			this.interest = memberInterest;
			return this;
		}

		public Builder withInterest(String interestName, boolean active) {
			interest.put(interestName, active);
			return this;
		}

		public Builder ipSignup(String ipSignup) {
			this.ipSignup = ipSignup;
			return this;
		}

		public Builder timestampSignup(LocalDateTime timestampSignup) {
			this.timestampSignup = timestampSignup;
			return this;
		}

		public Builder ipOpt(String ipOpt) {
			this.ipOpt = ipOpt;
			return this;
		}

		public Builder timestampOpt(LocalDateTime timestampOpt) {
			this.timestampOpt = timestampOpt;
			return this;
		}

		public Builder language(String language) {
			this.language = language;
			return this;
		}

		public Builder vip(boolean vip) {
			this.vip = vip;
			return this;
		}

		/**
		 * Add a tags to this member in preparation for creating a mailchimp list
	     * member. See: {@link MailChimpList#addMember(Member)}
	     * 
		 * @param tags
		 */
		public Builder tags(List<MemberTag> tags) {
			this.tags = tags;
			return this;
		}
		
		/**
		 * Add tag(s) to this member in preparation for creating a mailchimp list
	     * member. See: {@link MailChimpList#addMember(Member)}
		 * 
		 * @param tagName
		 */
		public Builder withTag(String tagName) {
			Optional<MemberTag> optional = tags.stream()
					.filter(t -> tagName.equals(t.getName()))
					.findFirst();
			if (!optional.isPresent()) {
				tags.add(new MemberTag(tagName));
			}
			return this;
		}

		public Builder statusIfNew(MemberStatus statusIfNew) {
			this.statusIfNew = statusIfNew;
			return this;
		}
	}
}
