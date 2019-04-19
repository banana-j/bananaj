/**
 * @author alexanderweiss
 * @date 06.11.2015
 */
package com.github.alexanderwe.bananaj.model.list.member;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.alexanderwe.bananaj.connection.MailChimpConnection;
import com.github.alexanderwe.bananaj.exceptions.EmailException;
import com.github.alexanderwe.bananaj.model.list.MailChimpList;
import com.github.alexanderwe.bananaj.utils.DateConverter;
import com.github.alexanderwe.bananaj.utils.EmailValidator;
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
	//private String unsubscribe_reason;
	private Map<String, String> mergeFields;
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
	//private MemberLastNote lastNote;
	private int tagsCount;
	private Map<String, TagStatus> tags = new HashMap<String, TagStatus>();
	private String listId;
	private MemberStatus statusIfNew;
	private List<MemberActivity> activities;
	private MailChimpConnection connection;

	public Member(MailChimpList mailChimpList, JSONObject member) {
		parse(mailChimpList, member);
	}

	public Member(Builder b) {
		id = b.id;
		mailChimpList = b.mailChimpList;
		emailAddress = b.emailAddress;
		uniqueEmailId = b.uniqueEmailId;
		emailType = b.emailType;
		status = b.status;
		//unsubscribe_reason = b.unsubscribeReason;
		mergeFields = b.mergeFields;
		interest = b.memberInterest;
		stats = b.stats;
		ipSignup = b.ipSignup;
		timestampSignup = b.timestampSignup;
		ipOpt = b.ipOpt;
		timestampOpt = b.timestampOpt;
		rating = b.memberRating;
		lastChanged = b.lastChanged;
		language = b.language;
		vip = b.vip;
		emailClient = b.emailClient;
		//location = b.location;
		//marketing_permissions = b.marketingPermissions;
		//last_note = b.lastNote;
		tagsCount = b.tagsCount;
		tags = b.tags;
		listId = b.listId;
		statusIfNew = b.statusIfNew;
		activities = b.memberActivities;
		connection = b.connection;
	}

	public Member() {

	}

	/**
	 * Parse a JSON representation of a member into this.
	 * @param member
	 */
	public void parse(MailChimpList mailChimpList, JSONObject member) {
		final JSONArray tagsArray = member.getJSONArray("tags");

        id = member.getString("id");
		Map<String, String> merge_fields = new HashMap<String, String>();
		if (member.has("merge_fields")) {
			final JSONObject memberMergeTags = member.getJSONObject("merge_fields");
			Iterator<String> mergeTagsI = memberMergeTags.keys();
			while(mergeTagsI.hasNext()) {
				String key = mergeTagsI.next();
				Object value = memberMergeTags.get(key);
				merge_fields.put(key, value.toString());
			}
		}

		Map<String, Boolean> memberInterest = new HashMap<String, Boolean>();
		if (member.has("interests")) {
			final JSONObject interests = member.getJSONObject("interests");
			Iterator<String> interestsI = interests.keys();
			while(interestsI.hasNext()) {
				String key = interestsI.next();
				boolean value = interests.getBoolean(key);
				memberInterest.put(key,value);
			}
		}

		emailAddress = member.getString("email_address");
		uniqueEmailId = member.getString("unique_email_id");
		emailType =  EmailType.valueOf(member.getString("email_type").toUpperCase());
		status = MemberStatus.valueOf(member.getString("status").toUpperCase());
		mergeFields = merge_fields;
		interest = memberInterest;
		stats = new MemberStats(member.getJSONObject("stats"));
		ipSignup = member.getString("ip_signup");
		timestampSignup = DateConverter.getInstance().createDateFromISO8601(member.getString("timestamp_signup"));
		rating = member.getInt("member_rating");
		ipOpt = member.getString("ip_opt");
		timestampOpt = DateConverter.getInstance().createDateFromISO8601(member.getString("timestamp_opt"));
		lastChanged = DateConverter.getInstance().createDateFromISO8601(member.getString("last_changed"));
		language = member.getString("language");

		tagsCount = member.getInt("tags_count");
		Map<String, TagStatus> memberTags = new HashMap<String, TagStatus>(tagsCount);
		for(int i = 0; i < tagsArray.length(); i++) {
			memberTags.put(tagsArray.getJSONObject(i).getString("name"), TagStatus.ACTIVE);
		}
		tags = memberTags;
		listId = member.getString("list_id");

		if(member.has("status_if_new")) {
			String value = member.getString("status_if_new");
			if (value.length() > 0) {
				status = MemberStatus.valueOf(member.getString("status_if_new").toUpperCase());
			}
		}

		this.mailChimpList = mailChimpList;
		this.connection = mailChimpList.getConnection();
	}

	/**
	 * Update the mailChimpList of this member
	 * @param listId
	 * @throws Exception 
	 */
	public void changeList(String listId) throws Exception {
		JSONObject updateMember = new JSONObject();
		updateMember.put("list_id", listId);
		getConnection().do_Patch(new URL("https://"+ mailChimpList.getConnection().getServer()+".api.mailchimp.com/3.0/lists/"+ getMailChimpList().getId()+"/members/"+getId()), updateMember.toString(),connection.getApikey());
		mailChimpList = getConnection().getList(listId);
		this.listId = listId;
	}

	/**
	 * Update the email Address of this member
	 * @param emailAddress
	 * @throws Exception
	 */
	public void changeEmailAddress(String emailAddress) throws Exception {

		EmailValidator validator = EmailValidator.getInstance();
		if (validator.validate(emailAddress)) {
			JSONObject updateMember = new JSONObject();
			updateMember.put("email_Address", emailAddress);
			getConnection().do_Patch(new URL("https://"+ mailChimpList.getConnection().getServer()+".api.mailchimp.com/3.0/lists/"+ getMailChimpList().getId()+"/members/"+getId()), updateMember.toString(),connection.getApikey());
			this.emailAddress = emailAddress;
		} else {
			throw new EmailException(emailAddress);
		}
	}

	/**
	 * Update the email address of this member
	 * @param status
	 * @throws Exception
	 */
	public void changeMemberStatus(MemberStatus status) throws Exception {
		JSONObject updateMember = new JSONObject();
		updateMember.put("status", status.getStringRepresentation());
		getConnection().do_Patch(new URL("https://"+ mailChimpList.getConnection().getServer()+".api.mailchimp.com/3.0/lists/"+ getMailChimpList().getId()+"/members/"+getId()), updateMember.toString(),connection.getApikey());
		this.status = status;
	}

    /**
	 * @return The MD5 hash of the lowercase version of the list member’s email address.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Email address for a subscriber
	 * @return 
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * An identifier for the address across all of Mailchimp
	 * @return 
	 */
	public String getUniqueEmailId() {
		return uniqueEmailId;
	}

	/**
	 * Type of email this member asked to get (‘html’ or ‘text’)
	 * @return
	 */
	public EmailType getEmailType() {
		return emailType;
	}

	/**
	 * Type of email this member asked to get (‘html’ or ‘text’). You must call
	 * {@link MailChimpList#updateMember(Member)} or
	 * {@link MailChimpList#addOrUpdateMember(Member)} for any changes to take
	 * effect.
	 * 
	 * @param emailType
	 */
	public void setEmailType(EmailType emailType) {
		this.emailType = emailType;
	}

	/**
	 * Subscriber’s current status 
	 * @return 
	 */
	public MemberStatus getStatus() {
		return status;
	}

	/**
	 * Subscriber’s current status. You must call
	 * {@link MailChimpList#updateMember(Member)} or
	 * {@link MailChimpList#addOrUpdateMember(Member)} for any changes to take
	 * effect.
	 */
	public void setStatus(MemberStatus status) {
		this.status = status;
	}

	/**
	 * Subscriber’s status. This value is required only when calling {@link MailChimpList#addOrUpdateMember(Member)}.
	 * @return the status_if_new
	 */
	public MemberStatus getStatusIfNew() {
		return statusIfNew;
	}

	/**
	 * Set the status for a new member created through a call to {@link MailChimpList#addOrUpdateMember(Member)} for any changes to take effect.
	 * @param statusIfNew
	 */
	public void setStatusIfNew(MemberStatus statusIfNew) {
		this.statusIfNew = statusIfNew;
	}

	/**
	 * Audience merge tags that corresponds to the data in an audience field.
	 * @return a Map of all merge field name value pairs
	 */
	public Map<String, String> getMergeFields() {
		return mergeFields;
	}

	/**
	 * Add or change an audience merge tags that corresponds to the data in an
	 * audience field. You must call {@link MailChimpList#updateMember(Member)} or
	 * {@link MailChimpList#addOrUpdateMember(Member)} for any changes to take
	 * effect.
	 * 
	 * @param key
	 * @param value
	 * @return the previous value associated with key, or null if there was none.)
	 */
	public String putMergeFields(String key, String value) {
		return mergeFields.put(key, value);
	}

	/**
	 * The members collection of interests. 
	 * @return the member interests. The map key is the interest/segment identifier and value is the subscription boolean.
	 */
	public Map<String, Boolean> getInterest() {
		return interest;
	}

	/**
	 * Open and click rates for this subscriber
	 * @return
	 */
	public MemberStats getStats() {
		return stats;
	}

	/**
	 * IP address the subscriber signed up from
	 * @return 
	 */
	public String getIpSignup() {
		return ipSignup;
	}

	/**
	 * The date and time the subscriber signed up for the list
	 * @return 
	 */
	public LocalDateTime getTimestampSignup() {
		return timestampSignup;
	}

	/**
	 * The IP address the subscriber used to confirm their opt-in status
	 * @return 
	 */
	public String getIpOpt() {
		return ipOpt;
	}

	/**
	 * The date and time the subscribe confirmed their opt-in status
	 * @return 
	 */
	public LocalDateTime getTimestampOpt() {
		return timestampOpt;
	}

	/**
	 * Star rating for this member, between 1 and 5
	 * @return
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
	 * @return
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * VIP status for subscriber
	 * @return
	 */
	public boolean isVip() {
		return vip;
	}

	/**
	 * The list member’s email client
	 * @return
	 */
	public String getEmailClient() {
		return emailClient;
	}

	/**
	 * The number of tags applied to this member
	 * @return
	 */
	public int getTagsCount() {
		return tagsCount;
	}

	/**
	 * @return a Map of all tags
	 * @throws Exception 
	 * @see fetchTags()
	 */
	public Map<String, TagStatus> getTags() throws Exception {
		if (tagsCount > 0 && tags.size() == 0) {
			fetchTags();
		}
		return tags;
	}

	/**
	 * @return all tags that are active
	 * @throws Exception 
	 * @see fetchTags()
	 */
	public Set<String> getActiveTags() throws Exception {
		if (tagsCount > 0 && tags.size() == 0) {
			fetchTags();
		}
		Set<String> tags = new HashSet<>();
		this.tags.keySet().forEach( tag -> {
			if(this.tags.get(tag).equals(TagStatus.ACTIVE)) {
				tags.add(tag);
			}
		});
		return tags;
	}

	/**
	 * Fetches all tags from mailchimp
	 * @return a Map of all tags
	 */
	private Map<String, TagStatus> fetchTags() throws Exception {
		final JSONObject tags = new JSONObject(getConnection().do_Get(new URL("https://"+mailChimpList.getConnection().getServer()+".api.mailchimp.com/3.0/lists/"+mailChimpList.getId()+"/members/"+getId()+"/tags"),connection.getApikey()));
		final JSONArray tagsArray = tags.getJSONArray("tags");

		for (int i = 0 ; i < tagsArray.length();i++)
		{
			String tag = tagsArray.getString(i);
			this.tags.put(tag, TagStatus.ACTIVE);
		}
		tagsCount = this.tags.size();

		return this.tags;
	}

	/**
	 * Sends all cached tag changes to mailchimp
	 */
	public void updateTags() throws Exception {
		JSONObject tags = new JSONObject();
		JSONArray tagsArray = new JSONArray();
		for(String key: this.tags.keySet()) {
			JSONObject tag = new JSONObject();
			tag.put("name", key);
			tag.put("status", this.tags.get(key).toString());
			tagsArray.put(tag);
		}
		tags.put("tags", tagsArray);

		getConnection().do_Post(new URL("https://"+this.mailChimpList.getConnection().getServer()+".api.mailchimp.com/3.0/lists/"+this.mailChimpList.getId()+"/members/"+this.getId()+"/tags"),tags.toString(),connection.getApikey());
	}

	/**
	 * Add a tag. You must call {@link #updateTags()} for changes to take effect.
	 * @param tag
	 */
	public void addTag(String tag) {
		tags.put(tag, TagStatus.ACTIVE);
		tagsCount = tags.size();
	}

	/**
	 * Remove a tag. You must call {@link #updateTags()} for changes to take effect.
	 * @param tag
	 */
	public void removeTag(String tag) {
		tags.put(tag, TagStatus.INACTIVE);
		tagsCount = tags.size();
	}	

	/**
	 * The list id
	 * @return
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
	 * Set the member activities for this specific member
	 * @param unique_email_id
	 * @param listID
	 * @throws Exception
	 */
	private void setActivities(String listID) throws Exception{
		List<MemberActivity> activities = new ArrayList<MemberActivity>();

		final JSONObject activity = new JSONObject(getConnection().do_Get(new URL("https://"+mailChimpList.getConnection().getServer()+".api.mailchimp.com/3.0/lists/"+mailChimpList.getId()+"/members/"+getId()+"/activity"),connection.getApikey()));
		final JSONArray activityArray = activity.getJSONArray("activity");

		for (int i = 0 ; i < activityArray.length();i++)
		{
			try{
				final JSONObject activityDetail = activityArray.getJSONObject(i);
				MemberActivity memberActivity = new MemberActivity(uniqueEmailId, mailChimpList.getId(), activityDetail.getString("action"),activityDetail.getString("timestamp"), activityDetail.getString("campaign_id"), activityDetail.getString("title"));
				activities.add(memberActivity);
			} catch (JSONException jsone){
				final JSONObject activityDetail = activityArray.getJSONObject(i);
				MemberActivity memberActivity = new MemberActivity(uniqueEmailId, mailChimpList.getId(), activityDetail.getString("action"),activityDetail.getString("timestamp"), activityDetail.getString("campaign_id"));
				activities.add(memberActivity);
			}

		}

		this.activities = activities;
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
	 * @return the member activities
	 */
	public List<MemberActivity> getActivities() {
		if (activities == null) {
			try {
				// cache member activity
				synchronized(this) {
					if (activities == null) {
						setActivities(mailChimpList.getId());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return activities;
	}

	/**
	 * @return the MailChimp com.github.alexanderwe.bananaj.connection
	 */
	public MailChimpConnection getConnection() {
		return connection;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 * @return
	 */
	public JSONObject getJsonRepresentation() throws Exception {
		JSONObject json = new JSONObject();
		json.put("email_address", getEmailAddress());
		if (getEmailType() != null) {
			json.put("email_type", getEmailType().getStringRepresentation());
		}
		json.put( "status", getStatus().getStringRepresentation());

		{
			JSONObject mergeFields = new JSONObject();
			Map<String, String> mergeFieldsMap = getMergeFields();
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

		if (getTagsCount() > 0) {
			JSONObject tags = new JSONObject();
			JSONArray tagsArray = new JSONArray();
			for(String key: getTags().keySet()) {
				JSONObject tag = new JSONObject();
				tag.put("name", key);
				tag.put("status", getTags().get(key).toString());
				tagsArray.put(tag);
			}
			tags.put("tags", tagsArray);
		}

		if (getIpSignup() != null) {
			json.put("ip_signup", getIpSignup());
		}

		if (getTimestampSignup() != null) {
			json.put("timestamp_signup", DateConverter.toNormal(getTimestampSignup()));
		}
		if (getIpOpt() != null) {
			json.put( "ip_opt", getIpOpt());
		}
		if (getTimestampOpt() != null) {
			json.put("timestamp_opt", DateConverter.toNormal(getTimestampOpt()));
		}
		return json;
	}

	@Override
	public String toString(){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("    Merge Fields:").append(System.lineSeparator());
		for (Entry<String, String> pair : getMergeFields().entrySet()) {
			stringBuilder.append("        ").append(pair.getKey()).append(": ").append(pair.getValue()).append(System.lineSeparator());
		}
		if (tags != null && tags.size() > 0) {
			stringBuilder.append("    Tags:").append(System.lineSeparator());
			for (Entry<String, TagStatus> pair : tags.entrySet()) {
				stringBuilder.append("        ").append(pair.getKey()).append(": ").append(pair.getValue().getStringRepresentation()).append(System.lineSeparator());
			}
		}
		if (interest != null && interest.size() > 0) {
			stringBuilder.append("    Interests:").append(System.lineSeparator());
			for (Entry<String, Boolean> pair : interest.entrySet()) {
				stringBuilder.append("        ").append(pair.getKey()).append(": ").append(pair.getValue().toString()).append(System.lineSeparator());
			}
		}

		return 
				"Member:" + System.lineSeparator() +
				"    Id: " + getId() + System.lineSeparator() +
				"    Email Address: " + getEmailAddress() + System.lineSeparator() +
				"    Unique Email Id: " + getUniqueEmailId() + System.lineSeparator() +
				"    Email Type: " + getEmailType().getStringRepresentation() + System.lineSeparator() +
				"    Status: " + getStatus().getStringRepresentation() + System.lineSeparator() +
				"    List Id: " + getListId() + System.lineSeparator() +
				"    Signup Timestamp: " + getTimestampSignup() + System.lineSeparator() +
				"    Signup IP: " + getIpSignup() + System.lineSeparator() +
				"    Opt-in IP: " + getIpOpt() + System.lineSeparator() +
				"    Opt-in Timestamp: " + getTimestampOpt() + System.lineSeparator() +
				"    Member Rating: " + getRating() + System.lineSeparator() +
				"    Last Changed: " + getLastChanged() + System.lineSeparator() +
				"    Language: " + getLanguage() + System.lineSeparator() +
				"    VIP: " + isVip() + System.lineSeparator() +
				"    Email Client: " + getEmailClient() + System.lineSeparator() +
				//getStats().toString() + System.lineSeparator() +
				stringBuilder.toString() +
				"_________________________________________________";
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
		private String id;
		private MailChimpList mailChimpList;
		private String emailAddress;
		private String uniqueEmailId;
		private EmailType emailType;
		private MemberStatus status;
		//private String unsubscribe_reason;
		private Map<String, String> mergeFields;
		private Map<String, Boolean> memberInterest;
		private MemberStats stats;
		private String ipSignup;
		private LocalDateTime timestampSignup;
		private String ipOpt;
		private LocalDateTime timestampOpt;
		private int memberRating;
		private LocalDateTime lastChanged;
		private String language;
		private boolean vip;
		private String emailClient;
		//private MemberLocation location;
		//private List<MemberMarketingPermissions> marketing_permissions;
		//private MemberLastNote last_note;
		private int tagsCount;
		private Map<String, TagStatus> tags = new HashMap<String, TagStatus>();
		private String listId;
		private MemberStatus statusIfNew;
		private List<MemberActivity> memberActivities;
		private MailChimpConnection connection;

		public Member build() {
			return new Member(this);
		}

		public Builder list(MailChimpList mailChimpList) {
			this.mailChimpList = mailChimpList;
			this.listId = mailChimpList.getId();
			this.connection = mailChimpList.getConnection();
			return this;
		}

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder emailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
			this.id = Member.subscriberHash(emailAddress);
			return this;
		}

		public Builder uniqueEmailId(String uniqueEmailId) {
			this.uniqueEmailId = uniqueEmailId;
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

		public Builder mergeFields(Map<String, String> mergeFields) {
			this.mergeFields = mergeFields;
			return this;
		}

		public Builder memberInterest(Map<String, Boolean> memberInterest) {
			this.memberInterest = memberInterest;
			return this;
		}

		public Builder stats(MemberStats stats) {
			this.stats = stats;
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

		public Builder memberRating(int memberRating) {
			this.memberRating = memberRating;
			return this;
		}

		public Builder lastChanged(LocalDateTime lastChanged) {
			this.lastChanged = lastChanged;
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

		public Builder emailClient(String emailClient) {
			this.emailClient = emailClient;
			return this;
		}

		public Builder tags(Map<String, TagStatus> tags) {
			this.tags = tags;
			this.tagsCount = tags.size();
			return this;
		}

		public Builder listId(String listId) {
			this.listId = listId;
			return this;
		}

		public Builder statusIfNew(MemberStatus statusIfNew) {
			this.statusIfNew = statusIfNew;
			return this;
		}

		public Builder memberActivities(List<MemberActivity> memberActivities) {
			this.memberActivities = memberActivities;
			return this;
		}

		public Builder connection(MailChimpConnection connection) {
			this.connection = connection;
			return this;
		}
	}
}
