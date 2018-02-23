/**
 * @author alexanderweiss
 * @date 06.11.2015
 */
package model.list.member;

import connection.MailChimpConnection;
import exceptions.EmailException;
import model.MailchimpObject;
import model.list.MailChimpList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.EmailValidator;

import java.net.URL;
import java.util.*;


/**
 * Object for representing a mailchimp member
 * @author alexanderweiss
 *
 */
public class Member extends MailchimpObject{

	private MailChimpList mailChimpList;
    private HashMap<String, Object> merge_fields;
	private String unique_email_id;
	private String email_address;
	private MemberStatus status;
	private String timestamp_signup;
	private String timestamp_opt;
	private String ip_signup;
	private String ip_opt;
	private double avg_open_rate;
	private double avg_click_rate;
	private String last_changed;
	private List<MemberActivity> memberActivities;
	private MailChimpConnection connection;


	public Member(String id, MailChimpList mailChimpList, HashMap<String, Object> merge_fields, String unique_email_id, String email_address, MemberStatus status, String timestamp_signup, String ip_signup, String timestamp_opt, String ip_opt, double avg_open_rate, double avg_click_rate, String last_changed, MailChimpConnection connection, JSONObject jsonRepresentation){
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
        this.connection = connection;

		try{
			setMemberActivities(unique_email_id, mailChimpList.getId());
		}catch (Exception e){
			e.printStackTrace();
		}
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
	 * @return the member activities
	 */
	public List<MemberActivity> getMemberActivities(){
		return this.memberActivities;
	}

	/**
	 * @return the MailChimp connection
	 */
	public MailChimpConnection getConnection() {
		return connection;
	}

	/**
	 * @return a HashMap of all merge fields
	 */
    public HashMap<String, Object> getMerge_fields() {
        return merge_fields;
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
		Iterator it = getMerge_fields().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
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
