/**
 * @author alexanderweiss
 * @date 06.11.2015
 */
package com.github.alexanderwe.bananaj.model.list;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.alexanderwe.bananaj.connection.MailChimpConnection;
import com.github.alexanderwe.bananaj.exceptions.EmailException;
import com.github.alexanderwe.bananaj.exceptions.FileFormatException;
import com.github.alexanderwe.bananaj.model.MailchimpObject;
import com.github.alexanderwe.bananaj.model.list.interests.Interest;
import com.github.alexanderwe.bananaj.model.list.interests.InterestCategory;
import com.github.alexanderwe.bananaj.model.list.member.Member;
import com.github.alexanderwe.bananaj.model.list.member.MemberStatus;
import com.github.alexanderwe.bananaj.model.list.member.TagStatus;
import com.github.alexanderwe.bananaj.model.list.mergefield.MergeField;
import com.github.alexanderwe.bananaj.model.list.mergefield.MergeFieldOptions;
import com.github.alexanderwe.bananaj.model.list.segment.AbstractCondition;
import com.github.alexanderwe.bananaj.model.list.segment.ConditionType;
import com.github.alexanderwe.bananaj.model.list.segment.DoubleCondition;
import com.github.alexanderwe.bananaj.model.list.segment.IPGeoInCondition;
import com.github.alexanderwe.bananaj.model.list.segment.IntegerCondition;
import com.github.alexanderwe.bananaj.model.list.segment.MatchType;
import com.github.alexanderwe.bananaj.model.list.segment.OpCondition;
import com.github.alexanderwe.bananaj.model.list.segment.Operator;
import com.github.alexanderwe.bananaj.model.list.segment.Options;
import com.github.alexanderwe.bananaj.model.list.segment.Segment;
import com.github.alexanderwe.bananaj.model.list.segment.SegmentType;
import com.github.alexanderwe.bananaj.model.list.segment.StringArrayCondition;
import com.github.alexanderwe.bananaj.model.list.segment.StringCondition;
import com.github.alexanderwe.bananaj.utils.DateConverter;
import com.github.alexanderwe.bananaj.utils.EmailValidator;
import com.github.alexanderwe.bananaj.utils.FileInspector;

import jxl.Cell;
import jxl.CellType;
import jxl.CellView;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


/**
 * Class for representing a mailchimp list.
 * 
 * Note that SUBSCRIBED/UNSUBSCRIBED/CLEANED counts are adjusted for add and
 * deletes member operations. Due to transitional or unknown member status these
 * counts are not guaranteed to be accurate after add or delete operations.
 * 
 * @author alexanderweiss
 *
 */
public class MailChimpList extends MailchimpObject {

	private int webId;				// The ID used in the Mailchimp web application. View this list in your Mailchimp account at https://{dc}.admin.mailchimp.com/lists/members/?id={web_id}
	private String name;			// The name of the list
	private ListContact contact;	// Contact information displayed in campaign footers to comply with international spam laws
	private String permissionReminder;	// The permission reminder for the list
	private boolean useArchiveBar;	// Whether campaigns for this list use the Archive Bar in archives by default
	private ListCampaignDefaults campaignDefaults;	// Default values for campaigns created for this list
	private String notifyOnSubscribe;	// The email address to send subscribe notifications to
	private String notifyOnUnsubscribe; // The email address to send unsubscribe notifications to 
	private LocalDateTime dateCreated;	// The date and time that this list was created
	private int listRating;			// An auto-generated activity score for the list (0-5)
	private boolean emailTypeOption;	// Whether the list supports multiple formats for emails. When set to true, subscribers can choose whether they want to receive HTML or plain-text emails. When set to false, subscribers will receive HTML emails, with a plain-text alternative backup.
	private String subscribeUrlShort;	// EepURL shortened version of this list’s subscribe form
	private String subscribeUrlLong;	// The full version of this list’s subscribe form (host will vary)
	private String beamerAddress;	// The list’s Email Beamer address
	private ListVisibility visibility;	// Whether this list is public or private (pub, prv)
	private boolean doubleOptin;	// Whether or not to require the subscriber to confirm subscription via email
	private boolean hasWelcome;		// Whether or not this list has a welcome automation connected. Welcome Automations: welcomeSeries, singleWelcome, emailFollowup
	private boolean marketingPermissions;	// Whether or not the list has marketing permissions (eg. GDPR) enabled
	//private List<?> modules;		// Any list-specific modules installed for this list.
	private ListStats stats;		// Stats for the list. Many of these are cached for at least five minutes.
	private MailChimpConnection connection;
	

	public MailChimpList(String id, String name,LocalDateTime dateCreated, ListStats stats,  MailChimpConnection connection, JSONObject jsonRepresentation) {
		super(id,jsonRepresentation);
		this.name = name;
		this.dateCreated = dateCreated;
		if (stats == null) {
			this.stats = new ListStats();
		} else {
			this.stats = stats;
		}
		this.connection = connection;
	}

	public MailChimpList(MailChimpConnection connection, JSONObject jsonList) {
		super(jsonList.getString("id"), jsonList);
		this.webId = jsonList.getInt("web_id");
		this.name = jsonList.getString("name");
		this.contact = new ListContact(jsonList.getJSONObject("contact"));
		this.permissionReminder = jsonList.getString("permission_reminder");
		this.useArchiveBar = jsonList.getBoolean("use_archive_bar");
		this.campaignDefaults = new ListCampaignDefaults(jsonList.getJSONObject("campaign_defaults"));
		this.notifyOnSubscribe = jsonList.getString("notify_on_subscribe");
		this.notifyOnUnsubscribe = jsonList.getString("notify_on_unsubscribe");
		this.dateCreated = DateConverter.getInstance().createDateFromISO8601(jsonList.getString("date_created"));
		this.listRating = jsonList.getInt("list_rating");
		this.emailTypeOption = jsonList.getBoolean("email_type_option");
		this.subscribeUrlShort = jsonList.getString("subscribe_url_short");
		this.subscribeUrlLong = jsonList.getString("subscribe_url_long");
		this.beamerAddress = jsonList.getString("beamer_address");
		this.visibility = ListVisibility.valueOf(jsonList.getString("visibility").toUpperCase());
		this.doubleOptin = jsonList.getBoolean("double_optin");
		this.hasWelcome = jsonList.getBoolean("has_welcome");
		this.marketingPermissions = jsonList.getBoolean("marketing_permissions");
		// TODO: this.modules = jsonList.getJSONArray("modules");
		this.stats = new ListStats(jsonList.getJSONObject("stats"));
		this.connection = connection;
	}

	/**
	 * Get information about members in this list with pagination
	 * @param count Number of members to return or 0 to return all members
	 * @param offset Zero based offset
	 * @return List of members
	 * @throws Exception
	 */
	public List<Member> getMembers(int count, int offset) throws Exception {

		ArrayList<Member> members = new ArrayList<Member>();
		final JSONObject list;
		if(count != 0){
			list = new JSONObject(getConnection().do_Get(new URL("https://"+connection.getServer()+".api.mailchimp.com/3.0/lists/"+this.getId()+"/members?count="+count+"&offset="+offset),connection.getApikey()));
		} else {
			list = new JSONObject(getConnection().do_Get(new URL("https://"+connection.getServer()+".api.mailchimp.com/3.0/lists/"+this.getId()+"/members?count="+(this.getStats().getTotalMemberCount()+1000)+"&offset="+offset),connection.getApikey()));
		}

		final JSONArray membersArray = list.getJSONArray("members");


		for (int i = 0 ; i < membersArray.length();i++)
		{
			final JSONObject memberDetail = membersArray.getJSONObject(i);
	    	Member member = new Member(this, memberDetail);
			members.add(member);
		}
		return members;
	}

	/**
	 * Get information about a specific list member, including a currently
	 * subscribed, unsubscribed, or bounced member.
	 * 
	 * @param subscriberHash The MD5 hash of the lowercase version of the list member’s email address.
	 * @return
	 * @throws Exception
	 */
	public Member getMember(String subscriberHash) throws Exception{
		final JSONObject member = new JSONObject(getConnection().do_Get(new URL("https://"+connection.getServer()+".api.mailchimp.com/3.0/lists/"+getId()+"/members/"+subscriberHash),connection.getApikey()));
    	return new Member(this, member);
	}
	
	/**
	 *  Add a member with the minimum of information
	 * @param status Subscriber’s current status
	 * @param emailAddress Email address for a subscriber
	 * @return The newly created member
	 * @throws Exception
	 */
	public Member addMember(MemberStatus status, String emailAddress) throws Exception {
		JSONObject json = new JSONObject();
		json.put("email_address", emailAddress);
		json.put("status", status.getStringRepresentation());

		String results = getConnection().do_Post(new URL(connection.getListendpoint()+"/"+this.getId()+"/members"),json.toString(),connection.getApikey());
		Member member = new Member(this, new JSONObject(results));
		incramentMemberCounts(member);
        return member;
	}

	/**
	 * Add a new member to the list
	 * @param member
	 * @return The newly added member
	 * @throws Exception
	 */
	public Member addMember(Member member) throws Exception {
		JSONObject json = memberToJson(member);
		
		String results = getConnection().do_Post(new URL(connection.getListendpoint()+"/"+this.getId()+"/members"),json.toString(),connection.getApikey());
		Member newMember = new Member(this, new JSONObject(results)); 
		incramentMemberCounts(newMember);
        return newMember;
	}
	
	/**
	 * Add a member with first and last name
	 * @param status Subscriber’s current status
	 * @param emailAddress Email address for a subscriber
	 * @param merge_fields_values
	 * @return The newly added member
	 * @throws Exception
	 */
	public Member addMember(MemberStatus status, String emailAddress, HashMap<String, Object> merge_fields_values) throws Exception{
		URL url = new URL(connection.getListendpoint()+"/"+this.getId()+"/members");
		
		JSONObject json = new JSONObject();
		JSONObject merge_fields = new JSONObject();

		Iterator<Entry<String, Object>> it = merge_fields_values.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> pair = it.next();
			it.remove(); // avoids a ConcurrentModificationException
			merge_fields.put(pair.getKey(), pair.getValue());
		}
		
		json.put("status", status.getStringRepresentation());
		json.put("email_address", emailAddress);
		json.put("merge_fields", merge_fields);
		String results = getConnection().do_Post(url,json.toString(),connection.getApikey());
		Member member = new Member(this, new JSONObject(results)); 
		incramentMemberCounts(member);
        return member;
	}

	/**
	 * Update list member via a PATCH operation. Member fields will be freshened
	 * from MailChimp.
	 * 
	 * @param member
	 * @throws Exception
	 */
	public void updateMember(Member member) throws Exception {
		JSONObject json = memberToJson(member);

		String results = getConnection().do_Patch(new URL(connection.getListendpoint()+"/"+this.getId()+"/members/"+member.getId()),json.toString(),connection.getApikey());
		member.parse(this, new JSONObject(results));  // update member object with current data
		incramentMemberCounts(member);
	}

	/**
	 * Add or update a list member via a PUT operation. When a new member is added
	 * and no status_if_new has been specified SUBSCRIBED will be used. Member
	 * fields will be freshened from mailchimp.
	 * 
	 * @param member
	 * @throws Exception
	 */
	public void addOrUpdateMember(Member member) throws Exception {
		JSONObject json = memberToJson(member);
		
		if (member.getStatus_if_new() != null) {
			json.put("status_if_new", member.getStatus_if_new().getStringRepresentation());
		} else {
			json.put("status_if_new", MemberStatus.SUBSCRIBED.getStringRepresentation());
		}
		
		String results = getConnection().do_Put(new URL(connection.getListendpoint()+"/"+this.getId()+"/members/"+member.getId()),json.toString(),connection.getApikey());
		member.parse(this, new JSONObject(results));  // update member object with current data
		if (member.getId() == null &&  member.getUnique_email_id() == null) {
			incramentMemberCounts(member);	// assume a new member was created because member did not specify either id
		}
	}
	
	/**
	 * Helper method to convert a Member object into JSON for PUT/PATCH/POST operations
	 * @param member
	 * @return
	 */
	private JSONObject memberToJson(Member member) {
		JSONObject json = new JSONObject();
		json.put("email_address", member.getEmail_address());
		if (member.getEmail_type() != null) {
			json.put("email_type", member.getEmail_type().value());
		}
		json.put( "status", member.getStatus().getStringRepresentation());

		{
			JSONObject mergeFields = new JSONObject();
			HashMap<String, String> mergeFieldsMap = member.getMerge_fields();
			for (String key : mergeFieldsMap.keySet()) {
				mergeFields.put(key, mergeFieldsMap.get(key));
			}
			json.put("merge_fields", mergeFields);
		}
		
		{
			JSONObject interests = new JSONObject();
			HashMap<String, Boolean> interestsMap = member.getInterest();
			for (String key : interestsMap.keySet()) {
				interests.put(key, interestsMap.get(key));
			}
			json.put("interests",interests);
		}
		// TODO: Add vip, location, and marketing_permissions to JSON
		json.put("ip_signup", member.getIp_signup());
		json.put("timestamp_signup", member.getTimestamp_signup());
		json.put( "ip_opt", member.getIp_opt());
		json.put("timestamp_opt", member.getTimestamp_opt());
		return json;
	}

	private void incramentMemberCounts(Member member) {
		switch (member.getStatus()) {
		case SUBSCRIBED:
		case PENDING:
			stats.setMemberCount(stats.getMemberCount() + 1);
			break;
		case UNSUBSCRIBED:
			stats.setUnsubscribeCount(stats.getUnsubscribeCount() + 1);
			break;
		case CLEANED:
			stats.setCleanedCount(stats.getCleanedCount() + 1);
			break;
		case TRANSACTIONAL:
			break;
		}
	}

	public void importMembersFromFile(File file) throws FileFormatException, IOException{
		//TODO fully implement read from xls
		String extension = FileInspector.getInstance().getExtension(file);

		if(extension.equals(".xls")|| extension.equals(".xlsx")){
			Workbook w;
			try {
				w = Workbook.getWorkbook(file);
				// Get the first sheet
				Sheet sheet = w.getSheet(0);
				// Loop over first 10 column and lines

				for (int j = 0; j < sheet.getColumns(); j++) {
					for (int i = 0; i < sheet.getRows(); i++) {
						Cell cell = sheet.getCell(j, i);
						CellType type = cell.getType();
						if (type == CellType.LABEL) {
							System.out.println("I got a label "
									+ cell.getContents());
						}

						if (type == CellType.NUMBER) {
							System.out.println("I got a number "
									+ cell.getContents());
						}

					}
				}
			} catch (BiffException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Delete a member from list
	 * @param memberID
	 * @throws Exception
	 */
	public void deleteMemberFromList(String memberID) throws Exception{
		getConnection().do_Delete(new URL(connection.getListendpoint()+"/"+getId()+"/members/"+memberID),connection.getApikey());
		// we don't know the status for this member so assume subscribed when adjusting counts.
		if (stats.getMemberCount() > 0) {
			stats.setMemberCount(stats.getMemberCount() - 1);
		}
	}
	
	public void delteMemberFromList(Member member) throws Exception {
		getConnection().do_Delete(new URL(connection.getListendpoint()+"/"+getId()+"/members/"+member.getId()),connection.getApikey());
		
		switch (member.getStatus()) {
		case SUBSCRIBED:
		case PENDING:
			stats.setMemberCount(stats.getMemberCount() - 1);
			break;
		case UNSUBSCRIBED:
			stats.setUnsubscribeCount(stats.getUnsubscribeCount() - 1);
			break;
		case CLEANED:
			stats.setCleanedCount(stats.getCleanedCount() - 1);
			break;
		case TRANSACTIONAL:
			break;
		}
	}
	
	/**
	 * Get the growth history of this list
	 * @return a growth history
	 * @throws Exception
	 */
	public GrowthHistory getGrowthHistory() throws Exception{
		final JSONObject growth_history = new JSONObject(getConnection().do_Get(new URL(connection.getListendpoint()+"/"+this.getId()+"/growth-history"),connection.getApikey()));
    	final JSONArray history = growth_history.getJSONArray("history");
    	final JSONObject historyDetail = history.getJSONObject(0);
    	
    	return new GrowthHistory(this, historyDetail.getString("month"), historyDetail.getInt("existing"), historyDetail.getInt("imports"), historyDetail.getInt("optins"));
	}

	/**
	 * Get interest categories for list. These correspond to ‘group titles’ in the MailChimp application.
	 * @param count Number of templates to return
	 * @param offset Zero based offset
	 * @return List of interest categories
	 * @throws Exception
	 */
	public List<InterestCategory> getInterestCategories(int count, int offset) throws Exception {
		ArrayList<InterestCategory> categories = new ArrayList<InterestCategory>();
		JSONObject list = new JSONObject(getConnection().do_Get(new URL(connection.getListendpoint()+"/"+this.getId()+"/interest-categories?count="+count+"&offset="+offset),connection.getApikey()));
		JSONArray categoryArray = list.getJSONArray("categories");

		for (int i = 0 ; i < categoryArray.length();i++)
		{
			final JSONObject jsonCategory = categoryArray.getJSONObject(i);
			InterestCategory category = InterestCategory.build(connection, jsonCategory);
			categories.add(category);

		}
		return categories;
	}
	
	public InterestCategory getInterestCategory(String interestCategoryId) throws Exception {
		JSONObject jsonCategory = new JSONObject(connection.do_Get(new URL(connection.getListendpoint()+"/"+this.getId()+"/interest-categories/"+interestCategoryId) ,connection.getApikey()));
		return InterestCategory.build(connection, jsonCategory);
	}
	
	/**
	 * Get interests for this list. Interests are referred to as ‘group names’ in the MailChimp application. 
	 * @param interestCategoryId
	 * @param count Number of members to return or 0 to return all members
	 * @param offset Zero based offset
	 * @return List of interests for this list
	 * @throws Exception
	 */
	public List<Interest> getInterests(String interestCategoryId, int count, int offset) throws Exception {
		ArrayList<Interest> interests = new ArrayList<Interest>();
		JSONObject list = new JSONObject(connection.do_Get(new URL(connection.getListendpoint()+"/"+this.getId()+"/interest-categories/"+interestCategoryId+"/interests?count="+count+"&offset="+offset) ,connection.getApikey()));
		JSONArray interestArray = list.getJSONArray("interests");

		for (int i = 0 ; i < interestArray.length();i++)
		{
			final JSONObject jsonInterest = interestArray.getJSONObject(i);
			Interest interest = Interest.build(jsonInterest);
			interests.add(interest);

		}
		return interests;
	}
	
	public Interest getInterest(String interestCategoryId, String interestId) throws Exception {
		JSONObject jsonInterests = new JSONObject(connection.do_Get(new URL(connection.getListendpoint()+"/"+this.getId()+"/interest-categories/"+interestCategoryId+"/interests/"+interestId) ,connection.getApikey()));
		return Interest.build(jsonInterests);
	}
	
	/**
	 * Get all segments of this list. A segment is a section of your list that includes only those subscribers who share specific common field information.
	 * @param count Number of templates to return
	 * @param offset Zero based offset
	 * @return List containing segments
	 * @throws Exception
     */
	public List<Segment> getSegments(int count, int offset) throws Exception {
        ArrayList<Segment> segments = new ArrayList<Segment>();
		JSONObject jsonSegments = new JSONObject(connection.do_Get(new URL(connection.getListendpoint()+"/"+this.getId()+"/segments?offset=" + offset + "&count=" + count) ,connection.getApikey()));

		final JSONArray segmentsArray = jsonSegments.getJSONArray("segments");

		for (int i = 0; i<segmentsArray.length(); i++){
			final JSONObject segmentDetail = segmentsArray.getJSONObject(i);
			Segment segment = getSegment(segmentDetail);
			segments.add(segment);
		}

        return segments;
    }

	/**
	 * Get a specific segment of this list
	 * @param segmentID
	 * @return
	 * @throws Exception
	 */
	public Segment getSegment(String segmentID) throws Exception {
		JSONObject jsonSegment = new JSONObject(connection.do_Get(new URL(connection.getListendpoint()+"/"+this.getId()+"/segments/"+segmentID) ,connection.getApikey()));
		return getSegment(jsonSegment);
	}

	private Segment getSegment(JSONObject jsonSegment) {
		Options options = null;
		SegmentType segmenttype = SegmentType.fromValue(jsonSegment.getString("type"));

		if (segmenttype == SegmentType.SAVED) {  // STATIC and FUZZY segments don't have conditions
			//Extract options and conditions
			ArrayList<AbstractCondition> conditions = null;
			conditions = new ArrayList<>();
			MatchType matchType = MatchType.fromValue(jsonSegment.getJSONObject("options").getString("match"));

			JSONArray jsonConditions = jsonSegment.getJSONObject("options").getJSONArray("conditions");
			for (int i = 0; i<jsonConditions.length();i++){
				JSONObject jsonCondition = jsonConditions.getJSONObject(i);

				ConditionType conditiontype = ConditionType.fromValue(jsonCondition.getString("condition_type"));
				switch(conditiontype) {
			    case AIM:
			    case AUTOMATION:
			    case CONVERSATION:
			    case EMAIL_CLIENT:
			    case LANGUAGE:
			    case SIGNUP_SOURCE:
			    case SURVEY_MONKEY:
			    case ECOMM_CATEGORY:
			    case ECOMM_STORE:
			    case GOAL_ACTIVITY:
			    case IP_GEO_COUNTRY_STATE:
			    case SOCIAL_AGE:
			    case SOCIAL_GENDER:
			    case SOCIAL_NETWORK_MEMBER:
			    case SOCIAL_NETWORK_FOLLOW:
			    case ADDRESS_MERGE:
			    case BIRTHDAY_MERGE:
			    case DATE_MERGE:
			    case TEXT_MERGE:
			    case SELECT_MERGE:
			    case EMAIL_ADDRESS:
					conditions.add( new StringCondition.Builder()
							.conditionType(conditiontype)
							.field(jsonCondition.getString("field"))
							.operator(Operator.fromValue(jsonCondition.getString("op")))
							.value(jsonCondition.getString("value"))
							.build());
					break;
					
			    case ECOMM_SPENT:
			    case IP_GEO_ZIP:
					conditions.add( new IntegerCondition.Builder()
							.conditionType(conditiontype)
							.field(jsonCondition.getString("field"))
							.operator(Operator.fromValue(jsonCondition.getString("op")))
							.value(jsonCondition.getInt("value"))
							.build());
					break;
					
			    case CAMPAIGN_POLL:
			    case MEMBER_RATING:
			    case ECOMM_NUMBER:
			    case FUZZY_SEGMENT:
			    case STATIC_SEGMENT:
			    case SOCIAL_INFLUENCE:
					conditions.add( new DoubleCondition.Builder()
							.conditionType(conditiontype)
							.field(jsonCondition.getString("field"))
							.operator(Operator.fromValue(jsonCondition.getString("op")))
							.value(jsonCondition.getDouble("value"))
							.build());
					break;
					
			    case DATE:
			    case GOAL_TIMESTAMP:
			    case ZIP_MERGE:
					conditions.add( new StringCondition.Builder()
							.conditionType(conditiontype)
							.field(jsonCondition.getString("field"))
							.operator(Operator.fromValue(jsonCondition.getString("op")))
							.extra(jsonCondition.getString("extra"))
							.value(jsonCondition.getString("value"))
							.build());
					break;
					
			    	
			    case MANDRILL:
			    case VIP:
			    case ECOMM_PURCHASED:
			    case IP_GEO_UNKNOWN:
					conditions.add( new OpCondition.Builder()
							.conditionType(conditiontype)
							.field(jsonCondition.getString("field"))
							.operator(Operator.fromValue(jsonCondition.getString("op")))
							.build());
					break;
					
			    	
			    case INTERESTS:
					List<String> values = null;
			    	if (jsonCondition.has("value")) {
						JSONArray jsonArray = jsonCondition.getJSONArray("value");
						values = new ArrayList<String>(jsonArray.length());
						for (int j=0; j<jsonArray.length(); j++) {
							values.add( jsonArray.getString(j) );
						}
			    	}
					conditions.add( new StringArrayCondition.Builder()
							.conditionType(conditiontype)
							.field(jsonCondition.getString("field"))
							.operator(Operator.fromValue(jsonCondition.getString("op")))
							.value(values)
							.build());
					break;
					
			    case IP_GEO_IN_ZIP:
					conditions.add( new IntegerCondition.Builder()
							.conditionType(conditiontype)
							.field(jsonCondition.getString("field"))
							.operator(Operator.fromValue(jsonCondition.getString("op")))
							.extra(jsonCondition.getInt("extra"))
							.value(jsonCondition.getInt("value"))
							.build());
					break;
					
			    case IP_GEO_IN:
					conditions.add( new IPGeoInCondition.Builder()
							.conditionType(conditiontype)
							.field(jsonCondition.getString("field"))
							.operator(Operator.fromValue(jsonCondition.getString("op")))
							.lng(jsonCondition.getString("lng"))
							.lat(jsonCondition.getString("lat"))
							.value(jsonCondition.getInt("value"))
							.addr(jsonCondition.getString("addr"))
							.build());
					break;
				}
			}
			options = new Options(matchType,conditions);
		}

		return new Segment(
				jsonSegment.getInt("id"),
				jsonSegment.getString("name"),
				jsonSegment.getString("list_id"),
				SegmentType.valueOf(jsonSegment.getString("type").toUpperCase()),
				DateConverter.getInstance().createDateFromISO8601(jsonSegment.getString("created_at")),
				DateConverter.getInstance().createDateFromISO8601(jsonSegment.getString("updated_at")),
				jsonSegment.getInt("member_count"),
				options,
				this.getConnection(),
				jsonSegment);
	}

	/**
	 * Add a segment to the list
	 * @param name
	 * @throws Exception
	 */
	public void addSegment(String name,Options option) throws Exception {
		JSONObject segment = new JSONObject();
		segment.put("name", name);

		segment.put("options",option.getJsonRepresentation());
		System.out.println(segment.toString());

		getConnection().do_Post(new URL(connection.getListendpoint()+"/"+this.getId()+"/segments"),segment.toString(),connection.getApikey());
	}


	/**
	 * Add a static segment with a name and predefined emails to this list.
	 * Every E-Mail address which is not present on the list itself will be ignored and not added to the static segment.
	 * @param name
	 * @param emails
	 * @throws Exception
	 */
	public void addStaticSegment(String name, String [] emails) throws Exception {
		JSONObject segment = new JSONObject();
		segment.put("name", name);
		for (String email : emails){
			if(!EmailValidator.getInstance().validate(email)){
				throw new EmailException(email);
			}
		}
		segment.put("static_segment", emails);
		getConnection().do_Post(new URL(connection.getListendpoint()+"/"+this.getId()+"/segments"),segment.toString(),connection.getApikey());

	}

	/**
	 * Delete a specific segment
	 * @param segmentId
	 * @throws Exception
	 */
	public void deleteSegment(String segmentId) throws Exception{
		getConnection().do_Delete(new URL(connection.getListendpoint()+"/"+this.getId()+"/segments/"+segmentId),connection.getApikey());
	}

	/**
	 * Get a list of all merge fields of this list
	 * @return
	 * @throws Exception
	 */
	public List<MergeField> getMergeFields() throws Exception {
		ArrayList<MergeField> mergeFields = new ArrayList<MergeField>();
		URL url = new URL(connection.getListendpoint()+"/"+this.getId()+"/merge-fields?offset=0&count=100"); // Note: Mailchimp currently supports a maximim of 80 merge fields

		JSONObject merge_fields = new JSONObject(connection.do_Get(url,connection.getApikey()));
		final JSONArray mergeFieldsArray = merge_fields.getJSONArray("merge_fields");

		for (int i = 0 ; i < mergeFieldsArray.length(); i++) {
			final JSONObject mergeFieldDetail = mergeFieldsArray.getJSONObject(i);

			final JSONObject mergeFieldOptionsJSON = mergeFieldDetail.getJSONObject("options");
			MergeFieldOptions mergeFieldOptions = new MergeFieldOptions();

			switch(mergeFieldDetail.getString("type")){
				case "address":mergeFieldOptions.setDefault_country(mergeFieldOptionsJSON.getInt("default_country"));break;
				case "phone":mergeFieldOptions.setPhone_format(mergeFieldOptionsJSON.getString("phone_format"));break;
				case "date":mergeFieldOptions.setDate_format(mergeFieldOptionsJSON.getString("date_format"));break;
				case "birthday":mergeFieldOptions.setDate_format(mergeFieldOptionsJSON.getString("date_format"));break;
				case "text":mergeFieldOptions.setSize(mergeFieldOptionsJSON.getInt("size"));break;
				case "radio":
					JSONArray mergeFieldOptionChoicesRadio = mergeFieldOptionsJSON.getJSONArray("choices");
					ArrayList<String> choicesRadio = new ArrayList<String>();
					for (int j = 0; j < mergeFieldOptionChoicesRadio.length(); j++){
						choicesRadio.add((String )mergeFieldOptionChoicesRadio.get(j));
					}
					mergeFieldOptions.setChoices(choicesRadio);
					break;
				case "dropdown":
					JSONArray mergeFieldOptionChoicesDropdown = mergeFieldOptionsJSON.getJSONArray("choices");
					ArrayList<String> choicesDropdown = new ArrayList<String>();
					for (int j = 0; j < mergeFieldOptionChoicesDropdown.length(); j++){
						choicesDropdown.add((String )mergeFieldOptionChoicesDropdown.get(j));
					}
					mergeFieldOptions.setChoices(choicesDropdown);
					break;
			}


			MergeField mergeField = new MergeField(
					String.valueOf(mergeFieldDetail.getInt("merge_id")),
					mergeFieldDetail.getString("tag"),
					mergeFieldDetail.getString("name"),
					mergeFieldDetail.getString("type"),
					mergeFieldDetail.getBoolean("required"),
					mergeFieldDetail.getString("default_value"),
					mergeFieldDetail.getBoolean("public"),
					mergeFieldDetail.getString("list_id"),
					mergeFieldOptions,
					mergeFieldDetail
			);
			mergeFields.add(mergeField);
		}
		return mergeFields;
	}

	/**
	 * Get a specific merge field of this list
	 * @param mergeFieldID
	 * @return
	 */
	public MergeField getMergeField(String mergeFieldID) throws Exception{
		URL url = new URL(connection.getListendpoint()+"/"+this.getId()+"/merge-fields/"+mergeFieldID);
		JSONObject mergeFieldJSON = new JSONObject(connection.do_Get(url,connection.getApikey()));

		final JSONObject mergeFieldOptionsJSON = mergeFieldJSON.getJSONObject("options");
		MergeFieldOptions mergeFieldOptions = new MergeFieldOptions();

		switch(mergeFieldJSON.getString("type")){
			case "address":mergeFieldOptions.setDefault_country(mergeFieldOptionsJSON.getInt("default_country"));break;
			case "phone":mergeFieldOptions.setPhone_format(mergeFieldOptionsJSON.getString("phone_format"));break;
			case "date":mergeFieldOptions.setDate_format(mergeFieldOptionsJSON.getString("date_format"));break;
			case "birthday":mergeFieldOptions.setDate_format(mergeFieldOptionsJSON.getString("date_format"));break;
			case "text":mergeFieldOptions.setSize(mergeFieldOptionsJSON.getInt("size"));break;
			case "radio":
				JSONArray mergeFieldOptionChoicesRadio = mergeFieldOptionsJSON.getJSONArray("choices");
				ArrayList<String> choicesRadio = new ArrayList<String>();
				for (int j = 0; j < mergeFieldOptionChoicesRadio.length(); j++){
					choicesRadio.add((String )mergeFieldOptionChoicesRadio.get(j));
				}
				mergeFieldOptions.setChoices(choicesRadio);
				break;
			case "dropdown":
				JSONArray mergeFieldOptionChoicesDropdown = mergeFieldOptionsJSON.getJSONArray("choices");
				ArrayList<String> choicesDropdown = new ArrayList<String>();
				for (int j = 0; j < mergeFieldOptionChoicesDropdown.length(); j++){
					choicesDropdown.add((String )mergeFieldOptionChoicesDropdown.get(j));
				}
				mergeFieldOptions.setChoices(choicesDropdown);
				break;
		}




		 return new MergeField(
				String.valueOf(mergeFieldJSON.getInt("merge_id")),
				mergeFieldJSON.getString("tag"),
				mergeFieldJSON.getString("name"),
				mergeFieldJSON.getString("type"),
				mergeFieldJSON.getBoolean("required"),
				mergeFieldJSON.getString("default_value"),
				mergeFieldJSON.getBoolean("public"),
				mergeFieldJSON.getString("list_id"),
				mergeFieldOptions,
				mergeFieldJSON
		);
	}

	public void addMergeField(MergeField mergeFieldtoAdd){

	}


	public void deleteMergeField(String mergeFieldID) throws Exception{
		connection.do_Delete(new URL(connection.getListendpoint()+"/"+this.getId()+"/merge-fields/"+mergeFieldID),connection.getApikey());
	}
	/**
	 * Writes the data of this list to an excel file in current directory. Define whether to show merge fields or not
	 * @param show_merge
	 * @throws Exception
	 */
	public void writeToExcel(String filepath,boolean show_merge) throws Exception{
		List<Member> members = this.getMembers(0,0);
		int merge_field_count = 0;
		WritableWorkbook workbook;


		if(filepath.contains(".xls")){
			workbook = Workbook.createWorkbook(new File(filepath));
		}else{
			workbook = Workbook.createWorkbook(new File(filepath+".xls"));
		}

		WritableSheet sheet = workbook.createSheet(this.getName(), 0);
		
		
		WritableFont times16font = new WritableFont(WritableFont.TIMES, 16, WritableFont.BOLD, false); 
		WritableCellFormat times16format = new WritableCellFormat (times16font); 
		
		Label memberIDLabel = new Label(0, 0, "MemberID",times16format);
		Label email_addressLabel = new Label(1,0,"Email Address",times16format);
		Label timestamp_sign_inLabel = new Label(2,0,"Sign up",times16format);
		Label ip_signinLabel = new Label(3,0,"IP Sign up", times16format);
		Label timestamp_opt_inLabel = new Label(4,0,"Opt in",times16format);
		Label ip_optLabel = new Label(5,0,"IP Opt in", times16format);
		Label statusLabel = new Label(6,0,"Status",times16format);
		Label avg_open_rateLabel = new Label(7,0,"Avg. open rate",times16format);
		Label avg_click_rateLabel = new Label(8,0,"Avg. click rate",times16format);
		

		sheet.addCell(memberIDLabel);
		sheet.addCell(email_addressLabel);
		sheet.addCell(timestamp_sign_inLabel);
		sheet.addCell(ip_signinLabel);
		sheet.addCell(timestamp_opt_inLabel);
		sheet.addCell(ip_optLabel);
		sheet.addCell(statusLabel);
		sheet.addCell(avg_open_rateLabel);
		sheet.addCell(avg_click_rateLabel);

		if (show_merge){
			int last_column = 9;

			Iterator<Entry<String, String>> iter = members.get(0).getMerge_fields().entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, String> pair = iter.next();
				sheet.addCell(new Label(last_column,0,pair.getKey(),times16format));
				iter.remove(); // avoids a ConcurrentModificationException
				last_column++;
				merge_field_count++;
			}
		}


		for(int i = 0 ; i < members.size();i++)
		{
			Member member = members.get(i);
			sheet.addCell(new Label(0,i+1,member.getId()));
			sheet.addCell(new Label(1,i+1,member.getEmail_address()));
			sheet.addCell(new Label(2,i+1,member.getTimestamp_signup()));
			sheet.addCell(new Label(3,i+1,member.getIp_signup()));
			sheet.addCell(new Label(4,i+1,member.getTimestamp_opt()));
			sheet.addCell(new Label(5,i+1,member.getIp_opt()));
			sheet.addCell(new Label(6,i+1,member.getStatus().getStringRepresentation()));
			sheet.addCell(new Number(7,i+1,member.getAvg_open_rate()));
			sheet.addCell(new Number(8,i+1,member.getAvg_click_rate()));

			if (show_merge){
				//add merge fields values
				int last_index = 9;
				Iterator<Entry<String, String>> iter_member = member.getMerge_fields().entrySet().iterator();
				while (iter_member.hasNext()) {
					Entry<String, String> pair = iter_member.next();
					sheet.addCell(new Label(last_index,i+1,pair.getValue()));
					iter_member.remove(); // avoids a ConcurrentModificationException
					last_index++;

				}
			}
		}

		CellView cell;

		int column_count = 9 + merge_field_count;
		for(int x=0;x<column_count;x++)
		{
			cell=sheet.getColumnView(x);
			cell.setAutosize(true);
			sheet.setColumnView(x, cell);
		}


		workbook.write(); 
		workbook.close();

		System.out.println("Writing to excel - done");
	}

	/**
	 * The ID used in the Mailchimp web application. View this list in your Mailchimp account at https://{dc}.admin.mailchimp.com/lists/members/?id={web_id}
	 * @return
	 */
	public int getWebId() {
		return webId;
	}

	/**
	 * The name of the list
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Contact information displayed in campaign footers to comply with international spam laws
	 * @return
	 */
	public ListContact getContact() {
		return contact;
	}

	/**
	 * The permission reminder for the list
	 * @return
	 */
	public String getPermissionReminder() {
		return permissionReminder;
	}

	/**
	 * Whether campaigns for this list use the Archive Bar in archives by default
	 * @return
	 */
	public boolean isUseArchiveBar() {
		return useArchiveBar;
	}

	/**
	 * Default values for campaigns created for this list
	 * @return
	 */
	public ListCampaignDefaults getCampaignDefaults() {
		return campaignDefaults;
	}

	/**
	 * The email address to send subscribe notifications to
	 * @return
	 */
	public String getNotifyOnSubscribe() {
		return notifyOnSubscribe;
	}

	/**
	 * The email address to send unsubscribe notifications to
	 * @return
	 */
	public String getNotifyOnUnsubscribe() {
		return notifyOnUnsubscribe;
	}

	/**
	 * The date and time that this list was created.
	 * @return the dateCreated
	 */
	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	/**
	 * An auto-generated activity score for the list (0-5)
	 * @return
	 */
	public int getListRating() {
		return listRating;
	}

	/**
	 * Whether the list supports multiple formats for emails. When set to true,
	 * subscribers can choose whether they want to receive HTML or plain-text
	 * emails. When set to false, subscribers will receive HTML emails, with a
	 * plain-text alternative backup.
	 * 
	 * @return
	 */
	public boolean isEmailTypeOption() {
		return emailTypeOption;
	}

	/**
	 * EepURL shortened version of this list’s subscribe form
	 * @return
	 */
	public String getSubscribeUrlShort() {
		return subscribeUrlShort;
	}

	/**
	 * The full version of this list’s subscribe form (host will vary)
	 * @return
	 */
	public String getSubscribeUrlLong() {
		return subscribeUrlLong;
	}

	/**
	 * The list’s Email Beamer address
	 * @return
	 */
	public String getBeamerAddress() {
		return beamerAddress;
	}

	/**
	 * Whether this list is public or private
	 * @return
	 */
	public ListVisibility getVisibility() {
		return visibility;
	}

	/**
	 * Whether or not to require the subscriber to confirm subscription via email
	 * @return
	 */
	public boolean isDoubleOptin() {
		return doubleOptin;
	}

	/**
	 * Whether or not this list has a welcome automation connected. Welcome Automations: welcomeSeries, singleWelcome, emailFollowup.
	 * @return
	 */
	public boolean isHasWelcome() {
		return hasWelcome;
	}

	/**
	 * Whether or not the list has marketing permissions (eg. GDPR) enabled
	 * @return
	 */
	public boolean isMarketingPermissions() {
		return marketingPermissions;
	}

	/**
	 * Stats for the list. Many of these are cached for at least five minutes.
	 * @return the list stats
	 */
	public ListStats getStats() {
		return stats;
	}

	/**
	 *
	 * @return the MailChimp com.github.alexanderwe.bananaj.connection
	 */
	public MailChimpConnection getConnection(){
		return this.connection;
	}

	@Override
	public String toString(){
		return this.getId() + " " + this.name + " " + this.stats.getTotalMemberCount() + System.lineSeparator() +
				"Date created: " + this.getDateCreated() + System.lineSeparator();
	}

}
