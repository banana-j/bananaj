/**
 * @author alexanderweiss
 * @date 06.11.2015
 */
package com.github.bananaj.model.list;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidParameterException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.exceptions.EmailException;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.model.ModelIterator;
import com.github.bananaj.model.SortDirection;
import com.github.bananaj.model.list.interests.Interest;
import com.github.bananaj.model.list.interests.InterestCategory;
import com.github.bananaj.model.list.member.Member;
import com.github.bananaj.model.list.member.MemberNote;
import com.github.bananaj.model.list.member.MemberStatus;
import com.github.bananaj.model.list.member.MemberTag;
import com.github.bananaj.model.list.mergefield.MergeField;
import com.github.bananaj.model.list.segment.Segment;
import com.github.bananaj.model.list.segment.SegmentOptions;
import com.github.bananaj.model.list.segment.SegmentType;
import com.github.bananaj.model.report.AbuseReport;
import com.github.bananaj.model.report.Report;
import com.github.bananaj.utils.DateConverter;
import com.github.bananaj.utils.EmailValidator;
import com.github.bananaj.utils.URLHelper;


/**
 * Your Mailchimp list, also known as your audience, is where you store and manage all of your contacts.
 * 
 * @see <a href="https://mailchimp.com/developer/marketing/api/lists/">Lists/Audiences</a> 
 */
public class MailChimpList implements JSONParser {

	private String id;				// A string that uniquely identifies this list.
	private int webId;				// The ID used in the Mailchimp web application. View this list in your Mailchimp account at https://{dc}.admin.mailchimp.com/lists/members/?id={web_id}
	private String name;			// The name of the list
	private ListContact contact;	// Contact information displayed in campaign footers to comply with international spam laws
	private String permissionReminder;	// The permission reminder for the list
	private boolean useArchiveBar;	// Whether campaigns for this list use the Archive Bar in archives by default
	private ListCampaignDefaults campaignDefaults;	// Default values for campaigns created for this list
	private String notifyOnSubscribe;	// The email address to send subscribe notifications to
	private String notifyOnUnsubscribe; // The email address to send unsubscribe notifications to 
	private ZonedDateTime dateCreated;	// The date and time that this list was created
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
	

	public MailChimpList() {
		
	}
	
	public MailChimpList(MailChimpConnection connection, JSONObject jsonList) {
		parse(connection, jsonList);
	}
	
	public MailChimpList(Builder b) {
        connection = b.connection;
    	name = b.name;
    	contact = b.contact;
    	permissionReminder = b.permissionReminder;
    	useArchiveBar = b.useArchiveBar;
    	campaignDefaults = b.campaignDefaults;
    	notifyOnSubscribe = b.notifyOnSubscribe;
    	notifyOnUnsubscribe = b.notifyOnUnsubscribe; 
    	emailTypeOption = b.emailTypeOption;
    	visibility = b.visibility;
    	doubleOptin = b.doubleOptin;
    	marketingPermissions = b.marketingPermissions;
	}
	
	public void parse(MailChimpConnection connection, JSONObject jsonList) {
		id = jsonList.getString("id");
		webId = jsonList.getInt("web_id");
		name = jsonList.getString("name");
		contact = new ListContact(jsonList.getJSONObject("contact"));
		permissionReminder = jsonList.getString("permission_reminder");
		useArchiveBar = jsonList.getBoolean("use_archive_bar");
		campaignDefaults = new ListCampaignDefaults(jsonList.getJSONObject("campaign_defaults"));
		notifyOnSubscribe = jsonList.getString("notify_on_subscribe");
		notifyOnUnsubscribe = jsonList.getString("notify_on_unsubscribe");
		dateCreated = DateConverter.fromISO8601(jsonList.getString("date_created"));
		listRating = jsonList.getInt("list_rating");
		emailTypeOption = jsonList.getBoolean("email_type_option");
		subscribeUrlShort = jsonList.getString("subscribe_url_short");
		subscribeUrlLong = jsonList.getString("subscribe_url_long");
		beamerAddress = jsonList.getString("beamer_address");
		visibility = ListVisibility.valueOf(jsonList.getString("visibility").toUpperCase());
		doubleOptin = jsonList.getBoolean("double_optin");
		hasWelcome = jsonList.getBoolean("has_welcome");
		marketingPermissions = jsonList.getBoolean("marketing_permissions");
		// TODO: modules = jsonList.getJSONArray("modules");
		stats = new ListStats(jsonList.getJSONObject("stats"));
		this.connection = connection;
	}


	/**
	 * Get information about abuse reports. An abuse complaint occurs when your
	 * recipient reports an email as spam in their mail program.
	 * 
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @return List of abuse reports
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<AbuseReport> getAbuseReports(int pageSize, int pageNumber) throws IOException, Exception {
		Objects.requireNonNull(getConnection(), "MailChimpConnection");
		final String baseURL = URLHelper.join(getConnection().getListendpoint(),"/",getId(),"/abuse-reports");
		return new ModelIterator<AbuseReport>(AbuseReport.class, baseURL, getConnection(), pageSize, pageNumber);
	}
	
	/**
	 * Get information about abuse reports. An abuse complaint occurs when your
	 * recipient reports an email as spam in their mail program.
	 * 
	 * @return List of abuse reports
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<AbuseReport> getAbuseReports() throws IOException, Exception {
		Objects.requireNonNull(getConnection(), "MailChimpConnection");
		final String baseURL = URLHelper.join(getConnection().getListendpoint(),"/",getId(),"/abuse-reports");
		return new ModelIterator<AbuseReport>(AbuseReport.class, baseURL, getConnection());
	}

	/**
	 * Get details about a specific abuse report. An abuse complaint occurs when
	 * your recipient reports an email as spam in their mail program.
	 * 
	 * @param reportId
	 * @return Details about a specific abuse report
	 * @throws IOException
	 * @throws Exception
	 */
	public AbuseReport getAbuseReport(int reportId) throws IOException, Exception {
		final JSONObject report = new JSONObject(connection.do_Get(URLHelper.url(connection.getListendpoint(),"/",getId(),"/abuse-reports/",Integer.toString(reportId)), connection.getApikey()));
    	return new AbuseReport(report);
	}
	
	// TODO: Add support for Activity -- Get recent daily, aggregated activity stats for your list. For example, view unsubscribes, signups, total emails sent, opens, clicks, and more, for up to 180 days.
//	public List<ListActivity> getActivity() {
//		final JSONObject list = new JSONObject(connection.do_Get(URLHelper.url(connection.getListendpoint(),"/",getId(),"/activity"), connection.getApikey()));
//		final JSONArray rptArray = list.getJSONArray("activity");
//		ArrayList<ListActivity> reports = new ArrayList<ListActivity>(rptArray.length());
//		for (int i = 0 ; i < rptArray.length();i++)
//		{
//			final JSONObject rptDetail = rptArray.getJSONObject(i);
//			ListActivity report = new ListActivity(rptDetail);
//			reports.add(report);
//		}
//		return reports;
//	}

	// TODO: Add support for Clients -- Get information about the most popular email clients for subscribers in a specific Mailchimp list.
//	public List<Clients> getActivity() {
//		final JSONObject list = new JSONObject(connection.do_Get(URLHelper.url(connection.getListendpoint(),"/",getId(),"/clients"), connection.getApikey()));
//		final JSONArray rptArray = list.getJSONArray("clients");
//		ArrayList<Clients> reports = new ArrayList<Clients>(rptArray.length());
//		for (int i = 0 ; i < rptArray.length();i++)
//		{
//			final JSONObject rptDetail = rptArray.getJSONObject(i);
//			Clients report = new Clients(rptDetail);
//			reports.add(report);
//		}
//		return reports;
//	}

	// TODO: Add support for Locations -- Get the locations (countries) that the list's subscribers have been tagged to based on geocoding their IP address.
//	public List<Locations> getActivity() {
//		final JSONObject list = new JSONObject(connection.do_Get(URLHelper.url(connection.getListendpoint(),"/",getId(),"/locations"), connection.getApikey()));
//		final JSONArray rptArray = list.getJSONArray("locations");
//		ArrayList<Locations> reports = new ArrayList<Locations>(rptArray.length());
//		for (int i = 0 ; i < rptArray.length();i++)
//		{
//			final JSONObject rptDetail = rptArray.getJSONObject(i);
//			Locations report = new Locations(rptDetail);
//			reports.add(report);
//		}
//		return reports;
//	}

	// TODO: Add support for Preview Segment -- Provide conditions to preview segment.
	// TODO: Add support for Signup Forms - Manage list signup forms.
	// TODO: Add support for Webhooks -- Manage webhooks for a specific Mailchimp list.

	//
	// Members -- Manage members of a specific Mailchimp list, including currently subscribed, unsubscribed, and bounced members.
	//
	// TODO: Members > Events -- Use the Events endpoint to collect website or in-app actions and trigger targeted automations.
	// TODO: Members > Member Activity -- Get details about subscribers' recent activity.
	// TODO: Members > Member Goals -- Get information about recent goal events for a specific list member.
	
	/**
	 * Get information about members in this list with pagination
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @return List of members
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<Member> getMembers(int pageSize, int pageNumber) throws IOException, Exception {
		Objects.requireNonNull(getConnection(), "MailChimpConnection");
		final String baseURL = URLHelper.join(getConnection().getListendpoint(),"/",getId(),"/members");
		return new ModelIterator<Member>(Member.class, baseURL, getConnection(), pageSize, pageNumber);
	}

	/**
	 * Get members iterator
	 * 
	 * Checked exceptions, including IOException and JSONException, are
	 * warped in a RuntimeException to reduce the need for boilerplate code inside
	 * of lambdas.
	 * 
	 * @return Member iterator
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<Member> getMembers() throws IOException, Exception {
		Objects.requireNonNull(getConnection(), "MailChimpConnection");
		final String baseURL = URLHelper.join(getConnection().getListendpoint(),"/",getId(),"/members");
		return new ModelIterator<Member>(Member.class, baseURL, getConnection());
	}

	/**
	 * Get information about a specific list member, including a currently
	 * subscribed, unsubscribed, or bounced member.
	 * 
	 * @param subscriber The member's email address or subscriber hash
	 * @throws IOException
	 * @throws Exception 
	 */
	public Member getMember(String subscriber) throws IOException, Exception {
		final JSONObject member = new JSONObject(getConnection().do_Get(URLHelper.url(getConnection().getListendpoint(),"/",
				getId(),"/members/",Member.subscriberHash(subscriber)),connection.getApikey()));
		return new Member(connection, member);
	}
	
	/**
	 * Add a member with the minimum of information
	 * 
	 * @param status       Subscriber’s current status
	 * @param emailAddress Email address for a subscriber
	 * @return The newly created member
	 * @throws IOException
	 * @throws Exception 
	 */
	public Member addMember(MemberStatus status, String emailAddress) throws IOException, Exception {
		JSONObject json = new JSONObject();
		json.put("email_address", emailAddress);
		json.put("status", status.toString());

		String results = getConnection().do_Post(URLHelper.url(connection.getListendpoint(), "/", getId(), "/members"),
				json.toString(), connection.getApikey());
		Member member = new Member(connection, new JSONObject(results));
		return member;
	}

	/**
	 * Add a new member to the list. Member fields will be freshened from mailchimp.
	 * 
	 * @param member
	 * @return The member with fields freshened from mailchimp.
	 * @throws IOException
	 * @throws Exception 
	 */
	public Member addMember(Member member) throws IOException, Exception {
		JSONObject json = member.getJsonRepresentation();
		String results = connection.do_Post(URLHelper.url(connection.getListendpoint(),"/",getId(),"/members"), json.toString(), connection.getApikey());
		member.parse(connection, new JSONObject(results));
        return member;
	}
	
	/**
	 * Add a member with first and last name.
	 * 
	 * @param status              Subscriber’s current status
	 * @param emailAddress        Email address for a subscriber
	 * @param merge_fields_values
	 * @return The newly added member
	 * @throws IOException
	 * @throws Exception 
	 */
	public Member addMember(MemberStatus status, String emailAddress, HashMap<String, Object> merge_fields_values)
			throws IOException, Exception {
		URL url = URLHelper.url(connection.getListendpoint(), "/", getId(), "/members");

		JSONObject json = new JSONObject();
		JSONObject merge_fields = new JSONObject();

		Iterator<Entry<String, Object>> it = merge_fields_values.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> pair = it.next();
			it.remove(); // avoids a ConcurrentModificationException
			merge_fields.put(pair.getKey(), pair.getValue());
		}

		json.put("status", status.toString());
		json.put("email_address", emailAddress);
		json.put("merge_fields", merge_fields);
		String results = getConnection().do_Post(url, json.toString(), connection.getApikey());
		Member member = new Member(connection, new JSONObject(results));
		return member;
	}

	/**
	 * Update list subscriber via a PATCH operation. Member fields will be freshened
	 * from MailChimp.
	 * 
	 * @param member
	 * @return The member with fields freshened from mailchimp.
	 * @throws IOException
	 * @throws Exception 
	 */
	public Member updateMember(Member member) throws IOException, Exception {
		JSONObject json = member.getJsonRepresentation();

		String results = getConnection().do_Patch(
				URLHelper.url(connection.getListendpoint(), "/", getId(), "/members/", member.getId()), json.toString(),
				connection.getApikey());
		member.parse(connection, new JSONObject(results)); // update member object with current data
		return member;
	}

	/**
	 * Add or update a list member via a PUT operation. When a new member is added
	 * and no status_if_new has been specified SUBSCRIBED will be used. Member
	 * fields will be freshened from Milchimp.
	 * 
	 * Note that if an existing member (previously archived or otherwise) is updated
	 * member tags will not be applied. Use
	 * {@link com.github.bananaj.model.list.member.Member#applyTags(java.util.Map)}
	 * or
	 * {@link com.github.bananaj.model.list.member.Member#applyTag(String, com.github.bananaj.model.list.member.TagStatus)}
	 * 
	 * @param member
	 * @return The member with fields freshened from mailchimp.
	 * @throws IOException
	 * @throws Exception 
	 */
	public Member addOrUpdateMember(Member member) throws IOException, Exception {
		JSONObject json = member.getJsonRepresentation();

		if (member.getStatusIfNew() == null) {
			json.put("status_if_new", MemberStatus.SUBSCRIBED.toString());
		}

		String results = getConnection().do_Put(
				URLHelper.url(connection.getListendpoint(), "/", getId(), "/members/", member.getId()), json.toString(),
				connection.getApikey());
		member.parse(getConnection(), new JSONObject(results)); // update member object with current data
		return member;
	}

	/**
	 * Delete a member from list.
	 * 
	 * @param memberID
	 * @throws IOException
	 * @throws Exception 
	 */
	public void deleteMember(String memberID) throws IOException, Exception {
		getConnection().do_Delete(URLHelper.url(connection.getListendpoint(), "/", getId(), "/members/", memberID),
				connection.getApikey());
	}

	/**
	 * Permanently delete a member for list.
	 * 
	 * @param memberID
	 * @throws IOException
	 * @throws Exception 
	 */
	public void deleteMemberPermanent(String memberID) throws IOException, Exception {
		getConnection().do_Post(URLHelper.url(getConnection().getListendpoint(), "/", getId(), "/members/", memberID,
				"/actions/delete-permanent"), getConnection().getApikey());
	}

	//
	// Members > Member Tags -- Manage all the tags that have been assigned to a contact.
	//
	
	/**
	 * Get paginated tags for the specified audience member.
	 * 
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @param subscriber     The member's email address or subscriber hash
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<MemberTag> getMemberTags(int pageSize, int pageNumber, String subscriber) throws IOException, Exception {
		final String baseURL = URLHelper.join(getConnection().getListendpoint(),"/",getId(),"/members/", 
				Member.subscriberHash(subscriber), "/tags");
		return new ModelIterator<MemberTag>(MemberTag.class, baseURL, getConnection(), pageSize, pageNumber);
	}

	/**
	 * Get tags iterator for the specified audience member.
	 * 
	 * Checked exceptions, including IOException and JSONException, are
	 * warped in a RuntimeException to reduce the need for boilerplate code inside
	 * of lambdas.
	 * 
	 * @param subscriber The member's email address or subscriber hash
	 * @return MemberTag iterator
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<MemberTag> getMemberTags(String subscriber) throws IOException, Exception{
		final String baseURL = URLHelper.join(getConnection().getListendpoint(),"/",getId(),"/members/", 
				Member.subscriberHash(subscriber), "/tags");
		return new ModelIterator<MemberTag>(MemberTag.class, baseURL, getConnection());
	}

	//
	// Members > Member Notes -- Manage recent notes for a specific list member.
	//
	
	/**
	 * Get recent notes for this list member.
	 * 
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @param subscriber     The member's email address or subscriber hash
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<MemberNote> getMemberNotes(int pageSize, int pageNumber, String subscriber) throws IOException, Exception {
		final String baseURL = URLHelper.join(getConnection().getListendpoint(),"/",getId(),"/members/",
				Member.subscriberHash(subscriber),"/notes");
		return new ModelIterator<MemberNote>(MemberNote.class, baseURL, getConnection(), pageSize, pageNumber);
	}
	
	/**
	 * Get iterator for recent notes for the specified member.
	 * 
	 * Checked exceptions, including IOException and JSONException, are
	 * warped in a RuntimeException to reduce the need for boilerplate code inside
	 * of lambdas.
	 * 
	 * @param subscriber     The member's email address or subscriber hash
	 * @return MemberNote iterator
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<MemberNote> getMemberNotes(String subscriber) throws IOException, Exception {
		final String baseURL = URLHelper.join(getConnection().getListendpoint(),"/",getId(),"/members/",
				Member.subscriberHash(subscriber),"/notes");
		return new ModelIterator<MemberNote>(MemberNote.class, baseURL, getConnection());
	}

	/**
	 * Get a specific note for the member
	 * 
	 * @param subscriber The member's email address or subscriber hash
	 * @param noteId         The id for the note.
	 * @throws IOException
	 * @throws Exception 
	 */
	public MemberNote getMemberNote(String subscriber, int noteId) throws IOException, Exception {
		final JSONObject noteObj = new JSONObject(getConnection().do_Get(URLHelper.url(
				getConnection().getListendpoint(), "/", getId(), "/members/", Member.subscriberHash(subscriber), 
				"/notes/", Integer.toString(noteId)),
				getConnection().getApikey()));
		return new MemberNote(noteObj);

	}

	//
	// Growth History -- View a summary of the month-by-month growth activity 
	//                   for the list/audience.
	//
	//
	
	/**
	 * Get a summary of the month-by-month growth activity for this list/audience.
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @param dir Optional, determines the order direction for sorted results.
	 * @return Summary of the month-by-month growth activity for this list/audience.
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<GrowthHistory> getGrowthHistory(int pageSize, int pageNumber, SortDirection dir) throws IOException, Exception {
		final String baseURL = URLHelper.join(connection.getListendpoint(), "/", getId(), "/growth-history",
				"?sort_field=month&sort_dir=",(dir != null ? dir.toString() : SortDirection.DESC.toString()));
		return new ModelIterator<GrowthHistory>(GrowthHistory.class, baseURL, connection, pageSize, pageNumber);
	}

	/**
	 * Get a summary of the month-by-month growth activity for this list/audience.
	 * @param dir Optional, determines the order direction for sorted results.
	 * @return Summary of the month-by-month growth activity for this list/audience.
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<GrowthHistory> getGrowthHistory(SortDirection dir) throws IOException, Exception {
		final String baseURL = URLHelper.join(connection.getListendpoint(), "/", getId(), "/growth-history",
				"?sort_field=month&sort_dir=",(dir != null ? dir.toString() : SortDirection.DESC.toString()));
		return new ModelIterator<GrowthHistory>(GrowthHistory.class, baseURL, connection);
	}

	/**
	 * Get a summary of a specific list's growth activity for a specific month and year.
	 * @param month Optional, determines the order direction for sorted results.
	 * @return Summary of the month-by-month growth activity for this list/audience.
	 * @throws IOException
	 * @throws Exception 
	 */
	public GrowthHistory getGrowthHistoryByMonth(String month) throws IOException, Exception {
		URL url = URLHelper.url(connection.getListendpoint(), "/", getId(), "/growth-history", "/", month);
		JSONObject jsonReport = new JSONObject(connection.do_Get(url, connection.getApikey()));
		GrowthHistory report = new GrowthHistory(jsonReport);
		return report;
}


	//
	// Interest Categories -- Manage interest categories for a specific list. Interest categories 
	//                        organize interests, which are used to group subscribers based on their 
	//                        preferences. These correspond to 'group titles' in the Mailchimp application.
	//
	
	/**
	 * Get interest categories for list. These correspond to ‘group titles’ in the
	 * MailChimp application.
	 * 
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @return List of interest categories
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<InterestCategory> getInterestCategories(int pageSize, int pageNumber) throws IOException, Exception {
		final String baseURL = URLHelper.join(getConnection().getListendpoint(),"/",getId(),"/interest-categories");
		return new ModelIterator<InterestCategory>(InterestCategory.class, baseURL, getConnection(), pageSize, pageNumber);
	}

	/**
	 * Get interest categories iterator for list. These correspond to ‘group titles’ in the
	 * MailChimp application.
	 * 
	 * Checked exceptions, including IOException and JSONException, are
	 * warped in a RuntimeException to reduce the need for boilerplate code inside
	 * of lambdas.
	 * 
	 * @return InterestCategory iterator
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<InterestCategory> getInterestCategories() throws IOException, Exception {
		final String baseURL = URLHelper.join(getConnection().getListendpoint(),"/",getId(),"/interest-categories");
		return new ModelIterator<InterestCategory>(InterestCategory.class, baseURL, getConnection());
	}

	/**
	 * Get the interest category details given its Id.
	 * 
	 * @param interestCategoryId
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public InterestCategory getInterestCategory(String interestCategoryId) throws IOException, Exception {
		JSONObject jsonCategory = new JSONObject(connection.do_Get(
				URLHelper.url(connection.getListendpoint(), "/", getId(), "/interest-categories/", interestCategoryId),
				connection.getApikey()));
		return new InterestCategory(connection, jsonCategory);
	}

	/**
	 * Remove an interest category from list.
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	public InterestCategory addInrestCategory(InterestCategory category) throws IOException, Exception {
		JSONObject json = category.getJsonRepresentation();
		String results = getConnection().do_Post(
				URLHelper.url(getConnection().getListendpoint(), "/", getId(), "/interest-categories"), json.toString(),
				getConnection().getApikey());
		return new InterestCategory(connection, new JSONObject(results));
	}

	/**
	 * Remove an interest category from list.
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	public void deleteInrestCategory(String categoryId) throws IOException, Exception {
		getConnection().do_Delete(
				URLHelper.url(getConnection().getListendpoint(), "/", getId(), "/interest-categories/", categoryId),
				getConnection().getApikey());
	}

	//
	// Interest Categories > Interests 
	//     Manage interests for a specific Mailchimp list. Assign subscribers to interests 
	//     to group them together. Interests are referred to as 'group names' in the 
	//     Mailchimp application.
	//
	
	/**
	 * Get interests for this list. Interests are referred to as ‘group names’ in
	 * the MailChimp application.
	 * 
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @param interestCategoryId
	 * @return List of interests for this list
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<Interest> getInterests(int pageSize, int pageNumber, String interestCategoryId) throws IOException, Exception {
		final String baseURL = URLHelper.join(getConnection().getListendpoint(), "/", getId(), "/interest-categories/",
				interestCategoryId, "/interests");
		return new ModelIterator<Interest>(Interest.class, baseURL, getConnection(), pageSize, pageNumber);
	}

	/**
	 * Get interests iterator for this list. Interests are referred to as ‘group
	 * names’ in the MailChimp application.
	 * 
	 * Checked exceptions, including IOException and JSONException, are
	 * warped in a RuntimeException to reduce the need for boilerplate code inside
	 * of lambdas.
	 * 
	 * @param interestCategoryId
	 * @return Interest iterator
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<Interest> getInterests(String interestCategoryId) throws IOException, Exception {
		final String baseURL = URLHelper.join(getConnection().getListendpoint(), "/", getId(), "/interest-categories/",
				interestCategoryId, "/interests");
		return new ModelIterator<Interest>(Interest.class, baseURL, getConnection());
	}

	/**
	 * Get a specific interests for this list. Interests are referred to as ‘group
	 * names’ in the MailChimp application.
	 * 
	 * @param interestCategoryId
	 * @param interestId
	 * @return The requested interest
	 * @throws IOException
	 * @throws Exception
	 */
	public Interest getInterest(String interestCategoryId, String interestId)
			throws IOException, Exception {
		JSONObject jsonInterests = new JSONObject(connection.do_Get(URLHelper.url(connection.getListendpoint(), "/", getId(),
				"/interest-categories/", interestCategoryId, "/interests/", interestId), connection.getApikey()));
		return new Interest(connection, jsonInterests);
	}

	//
	// Segments -- Manage segments and tags for a specific Mailchimp list. 
	//             A segment is a section of your list that includes only those 
	//             subscribers who share specific common field information. Tags 
	//             are labels you create to help organize your contacts.
	//
	
	/**
	 * Get all segments of this list. A segment is a section of your list that
	 * includes only those subscribers who share specific common field information.
	 * 
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @return List containing segments
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<Segment> getSegments(int pageSize, int pageNumber) throws IOException, Exception {
		final String baseURL = URLHelper.join(getConnection().getListendpoint(),"/",getId(),"/segments");
		return new ModelIterator<Segment>(Segment.class, baseURL, getConnection(), pageSize, pageNumber);
	}

	/**
	 * Get segments iterator for this list. A segment is a section of your list that
	 * includes only those subscribers who share specific common field information.
	 * 
	 * Checked exceptions, including IOException and JSONException, are
	 * warped in a RuntimeException to reduce the need for boilerplate code inside
	 * of lambdas.
	 * 
	 * @return Segment iterator
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<Segment> getSegments() throws IOException, Exception {
		final String baseURL = URLHelper.join(getConnection().getListendpoint(),"/",getId(),"/segments");
		return new ModelIterator<Segment>(Segment.class, baseURL, getConnection());
	}

	/**
	 * Get segments iterator for this list. A segment is a section of your list that
	 * includes only those subscribers who share specific common field information.
	 * 
	 * Checked exceptions, including IOException and JSONException, are
	 * warped in a RuntimeException to reduce the need for boilerplate code inside
	 * of lambdas.
	 *
	 * @param type   Limit results based on segment type
	 * @return Segment iterator
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<Segment> getSegments(SegmentType type) throws IOException, Exception {
		final String baseURL = URLHelper.join(getConnection().getListendpoint(),"/",getId(),"/segments","?","type=",type.toString());
		return new ModelIterator<Segment>(Segment.class, baseURL, getConnection());
	}

	/**
	 * Get a specific segment of this list
	 * 
	 * @param segmentID
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws JSONException
	 */
	public Segment getSegment(String segmentID)
			throws JSONException, IOException, Exception {
		String response = connection.do_Get(
				URLHelper.url(connection.getListendpoint(), "/", getId(), "/segments/", segmentID),
				connection.getApikey());
		JSONObject jsonSegment = new JSONObject(response);
		return new Segment(getConnection(), jsonSegment);
	}

	/**
	 * Add a segment to the list
	 * 
	 * @param name   The name of the segment.
	 * @param option The conditions of the segment. Static and fuzzy segments don't
	 *               have conditions.
	 * @return The newly created segment
	 * @throws IOException
	 * @throws Exception 
	 */
	public Segment addSegment(String name, SegmentOptions option) throws IOException, Exception {
		JSONObject segment = new JSONObject();
		segment.put("name", name);
		if (option != null) {
			segment.put("options", option.getJsonRepresentation());
		}

		String response = connection.do_Post(URLHelper.url(connection.getListendpoint(), "/", getId(), "/segments"),
				segment.toString(), connection.getApikey());
		JSONObject jsonSegment = new JSONObject(response);
		return new Segment(getConnection(), jsonSegment);
	}

	/**
	 * Add a static segment with a name and predefined emails to this list. Every
	 * E-Mail address which is not present on the list itself will be ignored and
	 * not added to the static segment.
	 * 
	 * @param name   The name of the segment.
	 * @param emails An array of emails to be used for a static segment. Any emails
	 *               provided that are not present on the list will be ignored.
	 *               Passing an empty array will create a static segment without any
	 *               subscribers.
	 * @return The newly created segment
	 * @throws IOException
	 * @throws Exception 
	 */
	public Segment addStaticSegment(String name, String[] emails) throws IOException, Exception {
		JSONObject segment = new JSONObject();
		segment.put("name", name);
		for (String email : emails) {
			if (!EmailValidator.getInstance().validate(email)) {
				throw new EmailException(email);
			}
		}
		segment.put("static_segment", emails);
		String response = getConnection().do_Post(URLHelper.url(connection.getListendpoint(), "/", getId(), "/segments"),
				segment.toString(), connection.getApikey());
		JSONObject jsonSegment = new JSONObject(response);
		return new Segment(getConnection(), jsonSegment);
	}

	/**
	 * Delete a specific segment.
	 * 
	 * @param segmentId
	 * @throws IOException
	 * @throws Exception 
	 */
	public void deleteSegment(String segmentId) throws IOException, Exception {
		connection.do_Delete(URLHelper.url(connection.getListendpoint(), "/", getId(), "/segments/", segmentId),
				connection.getApikey());
	}

	//
	// Merge Fields -- Manage merge fields (formerly merge vars) for the list.
	//
	
	/**
	 * Get merge fields for this audience.
	 * Merge fields let you save custom information about contacts, which can then 
	 * be used to personalize campaigns. Each merge field has a corresponding merge 
	 * tag, a string of text like *|FNAME|*. When you send a campaign, Mailchimp 
	 * replaces merge tags with the values stored in the corresponding merge fields 
	 * for each recipient.
	 * 
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<MergeField> getMergeFields(int pageSize, int pageNumber) throws IOException, Exception {
		final String baseURL = URLHelper.join(getConnection().getListendpoint(),"/",getId(),"/merge-fields");
		return new ModelIterator<MergeField>(MergeField.class, baseURL, getConnection(), pageSize, pageNumber);
	}

	/**
	 * Get merge fields for this audience.
	 * Merge fields let you save custom information about contacts, which can then 
	 * be used to personalize campaigns. Each merge field has a corresponding merge 
	 * tag, a string of text like *|FNAME|*. When you send a campaign, Mailchimp 
	 * replaces merge tags with the values stored in the corresponding merge fields 
	 * for each recipient.
	 * 
	 * @return MergeField iterator
	 */
	public Iterable<MergeField> getMergeFields() {
		final String baseURL = URLHelper.join(getConnection().getListendpoint(),"/",getId(),"/merge-fields");
		return new ModelIterator<MergeField>(MergeField.class, baseURL, getConnection());
	}

	/**
	 * Get a specific merge field of this audience.
	 * Merge fields let you save custom information about contacts, which can then 
	 * be used to personalize campaigns. Each merge field has a corresponding merge 
	 * tag, a string of text like *|FNAME|*. When you send a campaign, Mailchimp 
	 * replaces merge tags with the values stored in the corresponding merge fields 
	 * for each recipient.
	 * 
	 * @param mergeFieldID
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws JSONException
	 * @throws MalformedURLException
	 */
	public MergeField getMergeField(String mergeFieldID) throws JSONException, IOException, URISyntaxException, MalformedURLException {
		URL url = URLHelper.url(connection.getListendpoint(),"/",getId(),"/merge-fields","/",mergeFieldID);
		JSONObject mergeFieldJSON = new JSONObject(connection.do_Get(url,connection.getApikey()));
		return new MergeField(connection, mergeFieldJSON);
	}

	/**
	 * Add a merge field to this list/audience
	 * @param mergeFieldtoAdd
	 * @return The new Mailchimp merge field that was added.
	 * @throws JSONException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 */
	public MergeField addMergeField(MergeField mergeFieldtoAdd) throws JSONException, IOException, URISyntaxException, MalformedURLException {
		URL url = URLHelper.url(connection.getListendpoint(),"/",getId(),"/merge-fields");
		JSONObject mergeFieldJSON = new JSONObject(connection.do_Post(url, mergeFieldtoAdd.getJsonRepresentation().toString(), connection.getApikey()));
		return new MergeField(connection, mergeFieldJSON);
	}


	public void deleteMergeField(String mergeFieldID) throws IOException, Exception {
		connection.do_Delete(URLHelper.url(connection.getListendpoint(),"/",getId(),"/merge-fields","/",mergeFieldID), connection.getApikey());
	}

	/**
	 * A string that uniquely identifies this list.
	 */
	public String getId() {
		return id;
	}

	/**
	 * The ID used in the Mailchimp web application. View this list in your Mailchimp 
	 * account at https://{dc}.admin.mailchimp.com/lists/members/?id={web_id}.
	 */
	public int getWebId() {
		return webId;
	}

	/**
	 * The name of the list/audience.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name of the list/audience. You must call {@link #update()}
	 *             for changes to take effect.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Contact information displayed in campaign footers to comply with
	 * international spam laws.
	 */
	public ListContact getContact() {
		return contact;
	}

	/**
	 * @param contact Contact information displayed in campaign footers to comply
	 *                with international spam laws. You must call {@link #update()}
	 *                for changes to take effect.
	 */
	public void setContact(ListContact contact) {
		this.contact = contact;
	}

	/**
	 * The permission reminder for the list.
	 */
	public String getPermissionReminder() {
		return permissionReminder;
	}

	/**
	 * @param permissionReminder The permission reminder for the list. You must call
	 *                           {@link #update()} for changes to take effect.
	 */
	public void setPermissionReminder(String permissionReminder) {
		this.permissionReminder = permissionReminder;
	}

	/**
	 * Whether campaigns for this list use the Archive Bar in archives by default.
	 */
	public boolean isUseArchiveBar() {
		return useArchiveBar;
	}

	/**
	 * @param useArchiveBar Whether campaigns for this list use the Archive Bar in
	 *                      archives by default. You must call {@link #update()} for
	 *                      changes to take effect.
	 */
	public void setUseArchiveBar(boolean useArchiveBar) {
		this.useArchiveBar = useArchiveBar;
	}

	/**
	 * Default values for campaigns created for this list.
	 */
	public ListCampaignDefaults getCampaignDefaults() {
		return campaignDefaults;
	}

	/**
	 * @param campaignDefaults Default values for campaigns created for this list.
	 *                         You must call {@link #update()} for changes to take
	 *                         effect.
	 */
	public void setCampaignDefaults(ListCampaignDefaults campaignDefaults) {
		this.campaignDefaults = campaignDefaults;
	}

	/**
	 * The email address to send subscribe notifications to.
	 */
	public String getNotifyOnSubscribe() {
		return notifyOnSubscribe;
	}

	/**
	 * @param notifyOnSubscribe The email address to send subscribe notifications
	 *                          to. You must call {@link #update()} for changes to
	 *                          take effect.
	 */
	public void setNotifyOnSubscribe(String notifyOnSubscribe) {
		this.notifyOnSubscribe = notifyOnSubscribe;
	}

	/**
	 * The email address to send unsubscribe notifications to.
	 */
	public String getNotifyOnUnsubscribe() {
		return notifyOnUnsubscribe;
	}

	/**
	 * @param notifyOnUnsubscribe The email address to send unsubscribe
	 *                            notifications to. You must call {@link #update()}
	 *                            for changes to take effect.
	 */
	public void setNotifyOnUnsubscribe(String notifyOnUnsubscribe) {
		this.notifyOnUnsubscribe = notifyOnUnsubscribe;
	}

	/**
	 * The date and time that this list was created.
	 * 
	 * @return the dateCreated
	 */
	public ZonedDateTime getDateCreated() {
		return dateCreated;
	}

	/**
	 * An auto-generated activity score for the list (0-5).
	 */
	public int getListRating() {
		return listRating;
	}

	/**
	 * Whether the list supports multiple formats for emails. When set to true,
	 * subscribers can choose whether they want to receive HTML or plain-text
	 * emails. When set to false, subscribers will receive HTML emails, with a
	 * plain-text alternative backup.
	 */
	public boolean isEmailTypeOption() {
		return emailTypeOption;
	}

	/**
	 * @param emailTypeOption Whether the list supports multiple formats for emails.
	 *                        When set to true, subscribers can choose whether they
	 *                        want to receive HTML or plain-text emails. When set to
	 *                        false, subscribers will receive HTML emails, with a
	 *                        plain-text alternative backup. You must call
	 *                        {@link #update()} for changes to take effect.
	 */
	public void setEmailTypeOption(boolean emailTypeOption) {
		this.emailTypeOption = emailTypeOption;
	}

	/**
	 * Mailchimp EepURL shortened version of this list's subscribe form.
	 */
	public String getSubscribeUrlShort() {
		return subscribeUrlShort;
	}

	/**
	 * The full version of this list’s subscribe form (host will vary).
	 */
	public String getSubscribeUrlLong() {
		return subscribeUrlLong;
	}

	/**
	 * The list’s Email Beamer address.
	 */
	public String getBeamerAddress() {
		return beamerAddress;
	}

	/**
	 * Whether this list is public or private.
	 */
	public ListVisibility getVisibility() {
		return visibility;
	}

	/**
	 * @param visibility Whether this list is public or private. You must call
	 *                   {@link #update()} for changes to take effect.
	 */
	public void setVisibility(ListVisibility visibility) {
		this.visibility = visibility;
	}

	/**
	 * Whether or not to require the subscriber to confirm subscription via email.
	 */
	public boolean isDoubleOptin() {
		return doubleOptin;
	}

	/**
	 * @param doubleOptin Whether or not to require the subscriber to confirm
	 *                    subscription via email. You must call {@link #update()}
	 *                    for changes to take effect.
	 */
	public void setDoubleOptin(boolean doubleOptin) {
		this.doubleOptin = doubleOptin;
	}

	/**
	 * Whether or not this list has a welcome automation connected. Welcome
	 * Automations: welcomeSeries, singleWelcome, emailFollowup.
	 */
	public boolean isHasWelcome() {
		return hasWelcome;
	}

	/**
	 * Whether or not the list has marketing permissions (eg. GDPR) enabled.
	 */
	public boolean isMarketingPermissions() {
		return marketingPermissions;
	}

	/**
	 * Stats for the list. Many of these are cached for at least five minutes.
	 * 
	 * @return the list stats
	 */
	public ListStats getStats() {
		return stats;
	}

	/**
	 *
	 * @return the MailChimp com.github.bananaj.connection.
	 */
	public MailChimpConnection getConnection() {
		return connection;
	}

	/**
	 * @return the jsonRepresentation
	 */
	public JSONObject getJSONRepresentation() {
		JSONObject jsonList = new JSONObject();
		
		jsonList.put("name",name);
		jsonList.put("contact", contact.getJSONRepresentation());
		jsonList.put("permission_reminder", permissionReminder);
		jsonList.put("use_archive_bar", useArchiveBar);
		jsonList.put("campaign_defaults", campaignDefaults.getJSONRepresentation());
		jsonList.put("notify_on_subscribe", notifyOnSubscribe);
		jsonList.put("notify_on_unsubscribe", notifyOnUnsubscribe);
		jsonList.put("email_type_option", emailTypeOption);
		jsonList.put("visibility", visibility.toString());
		jsonList.put("double_optin", doubleOptin);
		jsonList.put("marketing_permissions", marketingPermissions);

		return jsonList;
	}
	
	/**
	 * Update list/audience via a PATCH operation. Member fields will be freshened.
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public void update() throws IOException, Exception {
		String results = connection.do_Patch(URLHelper.url(connection.getListendpoint(),"/",getId()), getJSONRepresentation().toString(), connection.getApikey());
		parse(connection, new JSONObject(results));  // update this object with current data
	}
	
	/**
	 * Delete this list/audience from your account
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws IOException
	 * @throws Exception 
	 */
	public void delete() throws IOException, Exception {
		connection.do_Delete(URLHelper.url(connection.getListendpoint(),"/",getId()), connection.getApikey());
	}
	
	@Override
	public String toString() {
		return 
				"Audience:" + System.lineSeparator() +
				"    Id: " + getId() + System.lineSeparator() +
				"    Web Id: " + getWebId() + System.lineSeparator() +
				"    Name: " + getName() + System.lineSeparator() +
				"    Permission Reminder: " + isMarketingPermissions() + System.lineSeparator() +
				"    Use Archive Bar: " + isUseArchiveBar() + System.lineSeparator() +
				"    Notify On Subscribe: " + getNotifyOnSubscribe() + System.lineSeparator() +
				"    Notify On Unsubscribe: " + getNotifyOnUnsubscribe() + System.lineSeparator() +
				"    Created: " + DateConverter.toLocalString(getDateCreated()) + System.lineSeparator() +
				"    List Rating: " + getListRating() + System.lineSeparator() +
				"    Email Type Option: " + isEmailTypeOption() + System.lineSeparator() +
				"    Subscribe URL Short: " + getSubscribeUrlShort() + System.lineSeparator() +
				"    Subscribe URL Long: " + getSubscribeUrlLong() + System.lineSeparator() +
				"    Beamer Address: " + getBeamerAddress() + System.lineSeparator() +
				"    Visibility: " + getVisibility().toString() + System.lineSeparator() +
				"    Double Option: " + isDoubleOptin() + System.lineSeparator() +
				"    Has Welcome: " + isHasWelcome() + System.lineSeparator() +
				"    Marketing Permissions: " + isMarketingPermissions() + System.lineSeparator() +
				getContact().toString() + System.lineSeparator() +
				getCampaignDefaults().toString() + System.lineSeparator() +
				getStats().toString();
	}

    /**
     * Builder for {@link MailChimpList}
     *
     */
    public static class Builder {
        private MailChimpConnection connection;
    	private String name;			// The name of the list
    	private ListContact contact;	// Contact information displayed in campaign footers to comply with international spam laws
    	private String permissionReminder;	// The permission reminder for the list
    	private boolean useArchiveBar = false;	// Whether campaigns for this list use the Archive Bar in archives by default
    	private ListCampaignDefaults campaignDefaults;	// Default values for campaigns created for this list
    	private String notifyOnSubscribe;	// The email address to send subscribe notifications to
    	private String notifyOnUnsubscribe; // The email address to send unsubscribe notifications to 
    	private boolean emailTypeOption = false;	// Whether the list supports multiple formats for emails. When set to true, subscribers can choose whether they want to receive HTML or plain-text emails. When set to false, subscribers will receive HTML emails, with a plain-text alternative backup.
    	private ListVisibility visibility = ListVisibility.PRV;	// Whether this list is public or private (pub, prv)
    	private boolean doubleOptin = false;	// Whether or not to require the subscriber to confirm subscription via email
    	private boolean marketingPermissions = false;	// Whether or not the list has marketing permissions (eg. GDPR) enabled

		/**
		 * @param connection the connection to set
		 */
		public Builder connection(MailChimpConnection connection) {
			this.connection = connection;
			return this;
		}

		/**
		 * @param name The name of the list/audience.
		 */
		public Builder name(String name) {
			this.name = name;
			return this;
		}

		/**
		 * @param contact Contact information displayed in campaign footers to comply
		 *                with international spam laws.
		 */
		public Builder contact(ListContact contact) {
			this.contact = contact;
			return this;
		}

		/**
		 * @param permissionReminder The permission reminder for the list.
		 */
		public Builder permissionReminder(String permissionReminder) {
			this.permissionReminder = permissionReminder;
			return this;
		}

		/**
		 * @param useArchiveBar Whether campaigns for this list use the Archive Bar in
		 *                      archives by default.
		 */
		public Builder useArchiveBar(boolean useArchiveBar) {
			this.useArchiveBar = useArchiveBar;
			return this;
		}

		/**
		 * @param campaignDefaults Default values for campaigns created for this list.
		 */
		public Builder campaignDefaults(ListCampaignDefaults campaignDefaults) {
			this.campaignDefaults = campaignDefaults;
			return this;
		}

		/**
		 * @param notifyOnSubscribe The email address to send subscribe notifications
		 *                          to.
		 */
		public Builder notifyOnSubscribe(String notifyOnSubscribe) {
			this.notifyOnSubscribe = notifyOnSubscribe;
			return this;
		}

		/**
		 * @param notifyOnUnsubscribe The email address to send unsubscribe
		 *                            notifications to.
		 */
		public Builder notifyOnUnsubscribe(String notifyOnUnsubscribe) {
			this.notifyOnUnsubscribe = notifyOnUnsubscribe;
			return this;
		}

		/**
		 * @param emailTypeOption Whether the list supports multiple formats for emails.
		 *                        When set to true, subscribers can choose whether they
		 *                        want to receive HTML or plain-text emails. When set to
		 *                        false, subscribers will receive HTML emails, with a
		 *                        plain-text alternative backup.
		 */
		public Builder emailTypeOption(boolean emailTypeOption) {
			this.emailTypeOption = emailTypeOption;
			return this;
		}

		/**
		 * @param visibility Whether this list is public or private.
		 */
		public Builder visibility(ListVisibility visibility) {
			this.visibility = visibility;
			return this;
		}

		/**
		 * @param doubleOptin Whether or not to require the subscriber to confirm
		 *                    subscription via email.
		 */
		public Builder doubleOptin(boolean doubleOptin) {
			this.doubleOptin = doubleOptin;
			return this;
		}

		/**
		 * @param marketingPermissions Whether or not the list has marketing permissions
		 *                             (eg. GDPR) enabled.
		 */
		public Builder marketingPermissions(boolean marketingPermissions) {
			this.marketingPermissions = marketingPermissions;
			return this;
		}

    	public MailChimpList build() {
			Objects.requireNonNull(name, "name");
			Objects.requireNonNull(contact, "contact");
			Objects.requireNonNull(permissionReminder, "permission_reminder");
			Objects.requireNonNull(campaignDefaults, "campaign_defaults");
			//Objects.requireNonNull(emailTypeOption, "email_type_option");
    		return new MailChimpList(this);
    	}
    }
}
