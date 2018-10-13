/**
 * @author alexanderweiss
 * @date 06.11.2015
 */
package com.github.alexanderwe.bananaj.model.list.member;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.alexanderwe.bananaj.connection.MailChimpConnection;
import com.github.alexanderwe.bananaj.exceptions.EmailException;
import com.github.alexanderwe.bananaj.model.MailchimpObject;
import com.github.alexanderwe.bananaj.model.list.MailChimpList;
import com.github.alexanderwe.bananaj.utils.EmailValidator;


/**
 * Object for representing a mailchimp member
 * @author alexanderweiss
 *
 */
public class Member extends MailchimpObject{

	private MailChimpList mailChimpList;
    private HashMap<String, String> merge_fields;
	private String unique_email_id;
	private String email_address;
	private MemberStatus status_if_new;
	private EmailType email_type;
	private MemberStatus status;
	private String timestamp_signup;
	private String timestamp_opt;
	private String ip_signup;
	private String ip_opt;
	private double avg_open_rate;
	private double avg_click_rate;
	private String last_changed;
	private List<MemberActivity> memberActivities;
	private HashMap<String, Boolean> memberInterest;
	private MailChimpConnection connection;


	public Member(MailChimpList mailChimpList, JSONObject member) {
        super(member.getString("id"), member);
    	final JSONObject memberMergeTags = member.getJSONObject("merge_fields");
    	final JSONObject memberStats = member.getJSONObject("stats");
    	final JSONObject interests = member.has("interests") ? member.getJSONObject("interests") : null;

    	HashMap<String, String> merge_fields = new HashMap<String, String>();
    	if (memberMergeTags != null) {
    		Iterator<String> mergeTagsI = memberMergeTags.keys();
    		while(mergeTagsI.hasNext()) {
    			String key = mergeTagsI.next();
    			// loop to get the dynamic key
    			String value = memberMergeTags.get(key).toString();
    			merge_fields.put(key, value);
    		}
    	}

    	HashMap<String, Boolean> memberInterest = new HashMap<String, Boolean>();
    	if (interests != null) {
			Iterator<String> interestsI = interests.keys();
			while(interestsI.hasNext()) {
				String key = interestsI.next();
				boolean value = interests.getBoolean(key);
				memberInterest.put(key,value);
			}
		}
    	
		this.mailChimpList = mailChimpList;
        this.merge_fields = merge_fields;
        this.unique_email_id = member.getString("unique_email_id");
        this.email_address = member.getString("email_address");
        if(member.has("status_if_new")) {
        	String value = member.getString("status_if_new");
        	if (value.length() > 0) {
        		this.status = MemberStatus.valueOf(member.getString("status_if_new").toUpperCase());
        	}
        }
        this.email_type =  EmailType.fromValue(member.getString("email_type"));
        this.status = MemberStatus.valueOf(member.getString("status").toUpperCase());
        this.timestamp_signup = member.getString("timestamp_signup");
        this.timestamp_opt = member.getString("timestamp_opt");
        this.ip_signup = member.getString("ip_signup");
        this.ip_opt = member.getString("ip_opt");
        this.avg_open_rate = memberStats.getDouble("avg_open_rate");
        this.avg_click_rate = memberStats.getDouble("avg_click_rate");
        this.last_changed = member.getString("last_changed");
        this.memberInterest = memberInterest;
        this.connection = mailChimpList.getConnection();
	}
	
	public Member(String id, MailChimpList mailChimpList, HashMap<String, String> merge_fields, String unique_email_id, String email_address, MemberStatus status, String timestamp_signup, String ip_signup, String timestamp_opt, String ip_opt, double avg_open_rate, double avg_click_rate, String last_changed, MailChimpConnection connection, JSONObject jsonRepresentation){
        super(id,jsonRepresentation);
        this.mailChimpList = mailChimpList;
        this.merge_fields = merge_fields;
        this.unique_email_id = unique_email_id;
        this.email_address = email_address;
        this.status = status;
        this.timestamp_signup = timestamp_signup;
        this.timestamp_opt = timestamp_opt;
        this.ip_signup = ip_signup;
        this.ip_opt = ip_opt;
        this.avg_open_rate = avg_open_rate;
        this.avg_click_rate = avg_click_rate;
        this.last_changed = last_changed;
    	this.memberInterest = new HashMap<String, Boolean>();
        this.connection = connection;
	}

	public Member() {

	}

	/**
	 * Update the mailChimpList of this member
	 * @param listId
	 * @throws Exception 
	 */
	public void changeList(String listId) throws Exception{
		JSONObject updateMember = new JSONObject();
		updateMember.put("list_id", listId);
        this.getConnection().do_Patch(new URL("https://"+ mailChimpList.getConnection().getServer()+".api.mailchimp.com/3.0/lists/"+ getMailChimpList().getId()+"/members/"+getId()), updateMember.toString(),connection.getApikey());
		this.mailChimpList = this.getConnection().getList(listId);
	}
	
	/**
	 * Update the email Address of this member
	 * @param emailAddress
	 * @throws Exception
	 */
	public void changeEmailAddress(String emailAddress) throws Exception{
		
		EmailValidator validator = EmailValidator.getInstance();
		if (validator.validate(emailAddress)) {
			JSONObject updateMember = new JSONObject();
			updateMember.put("email_Address", emailAddress);
            this.getConnection().do_Patch(new URL("https://"+ mailChimpList.getConnection().getServer()+".api.mailchimp.com/3.0/lists/"+ getMailChimpList().getId()+"/members/"+getId()), updateMember.toString(),connection.getApikey());
			this.email_address = emailAddress;
		} else {
		   throw new EmailException(emailAddress);
		}
	}

	/**
	 * Update the email address of this member
	 * @param status
	 * @throws Exception
	 */
	public void changeMemberStatus(MemberStatus status) throws Exception{
		JSONObject updateMember = new JSONObject();
		updateMember.put("status", status.getStringRepresentation());
		this.getConnection().do_Patch(new URL("https://"+ mailChimpList.getConnection().getServer()+".api.mailchimp.com/3.0/lists/"+ getMailChimpList().getId()+"/members/"+getId()), updateMember.toString(),connection.getApikey());
		this.status = status;
	}

	/**
	 * @return the unique_email_id
	 */
	public String getUnique_email_id() {
		return unique_email_id;
	}

	/**
	 * @return the email_Address
	 */
	public String getEmail_address() {
		return email_address;
	}

	/**
	 * @return the status
	 */
	public MemberStatus getStatus() {
		return status;
	}

	public void setStatus(MemberStatus status) {
		this.status = status;
	}

	/**
	 * 
	 * @return the status_if_new
	 */
	public MemberStatus getStatus_if_new() {
		return status_if_new;
	}

	/**
	 * Set the status_if_new when creating a new member
	 * @param status_if_new
	 */
	public void setStatus_if_new(MemberStatus status_if_new) {
		this.status_if_new = status_if_new;
	}

	public EmailType getEmail_type() {
		return email_type;
	}

	public void setEmail_type(EmailType email_type) {
		this.email_type = email_type;
	}

	/**
	 * @return the timestamp_signup
	 */
	public String getTimestamp_signup() {
		return timestamp_signup;
	}

	/**
	 * @return the timestamp_opt
	 */
	public String getTimestamp_opt() {
		return timestamp_opt;
	}

	/**
	 * @return the avg_open_rate
	 */
	public double getAvg_open_rate() {
		return avg_open_rate;
	}

	/**
	 * @return the avg_click_rate
	 */
	public double getAvg_click_rate() {
		return avg_click_rate;
	}

	/**
	 * @return the listId
	 */
	public MailChimpList getMailChimpList() {
		return mailChimpList;
	}

	/**
	 * @return the last_changed date
	 */
	public String getLast_changed() {
		return last_changed;
	}

	/**
	 * Set the member activities fot this specific member
	 * @param unique_email_id
	 * @param listID
	 * @throws Exception
	 */
	private void setMemberActivities(String unique_email_id, String listID) throws Exception{
		List<MemberActivity> activities = new ArrayList<MemberActivity>();

		final JSONObject activity = new JSONObject(this.getConnection().do_Get(new URL("https://"+this.mailChimpList.getConnection().getServer()+".api.mailchimp.com/3.0/lists/"+this.mailChimpList.getId()+"/members/"+this.getId()+"/activity"),connection.getApikey()));
		final JSONArray activityArray = activity.getJSONArray("activity");

		for (int i = 0 ; i < activityArray.length();i++)
		{
			try{
				final JSONObject activityDetail = activityArray.getJSONObject(i);
				MemberActivity memberActivity = new MemberActivity(this.unique_email_id, this.mailChimpList.getId(), activityDetail.getString("action"),activityDetail.getString("timestamp"), activityDetail.getString("campaign_id"), activityDetail.getString("title"));
				activities.add(memberActivity);
			} catch (JSONException jsone){
				final JSONObject activityDetail = activityArray.getJSONObject(i);
				MemberActivity memberActivity = new MemberActivity(this.unique_email_id, this.mailChimpList.getId(), activityDetail.getString("action"),activityDetail.getString("timestamp"), activityDetail.getString("campaign_id"));
				activities.add(memberActivity);
			}

		}

		this.memberActivities = activities;
	}

	/**
	 * @return the member interests. The map key is the interest/segment identifier and value is the subscription boolean.
	 */
	public HashMap<String, Boolean> getInterest() {
		return memberInterest;
	}

	/**
	 * Add/Update an intrests subscription
	 * @param key
	 * @param subscribe
	 * @return the previous value associated with key, or null if there was none.)
	 */
	public Boolean putInterest(String key, Boolean subscribe) {
		return memberInterest.put(key, subscribe);
	}
	
	/**
	 * @return the member activities
	 */
	public List<MemberActivity> getMemberActivities() {
		if (memberActivities == null) {
			try {
				// cache member activity
				synchronized(this) {
					if (memberActivities == null) {
						setMemberActivities(unique_email_id, mailChimpList.getId());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.memberActivities;
	}

	/**
	 * @return the MailChimp com.github.alexanderwe.bananaj.connection
	 */
	public MailChimpConnection getConnection() {
		return connection;
	}

	/**
	 * @return a HashMap of all merge fields
	 */
    public HashMap<String, String> getMerge_fields() {
        return merge_fields;
    }
    
    /**
     * Add/update a merge field
     * @param key
     * @param value
     * @return the previous value associated with key, or null if there was none.)
     */
	public String putMerge_fields(String key, String value) {
		return merge_fields.put(key, value);
	}

	/**
	 * @return the sign up IP Address
	 */
	public String getIp_signup() {
		return ip_signup;
	}

	/**
	 * @return the opt-in IP Address
	 */
	public String getIp_opt() {
		return ip_opt;
	}

	@Override
	public String toString(){
		StringBuilder stringBuilder = new StringBuilder();
		Iterator<Entry<String, String>> it = getMerge_fields().entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> pair = it.next();
			stringBuilder.append(pair.getKey()).append(": ").append(pair.getValue()).append("\n");
			it.remove(); // avoids a ConcurrentModificationException
		}

		return System.lineSeparator()+"ID: " + this.getId() + "\t"+  System.getProperty("line.separator")
				+ "Unique email Address: " + this.getUnique_email_id() + System.getProperty("line.separator")
				+ "Email address: " + this.getEmail_address() + System.getProperty("line.separator")
				+ "Status: " + this.getStatus().getStringRepresentation() + System.getProperty("line.separator")
				+ "Sign_Up: " + this.getTimestamp_signup() + System.getProperty("line.separator")
				+ "Opt_In: " + this.getTimestamp_opt() + System.lineSeparator()
				+ "Last changed: " + this.getLast_changed() + System.lineSeparator()
				+ stringBuilder.toString()
				+ "_________________________________________________";
	}


}
