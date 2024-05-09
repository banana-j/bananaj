package com.github.bananaj.model.list;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.utils.JSONObjectCheck;

/**
 * Class for representing a growth history of a mailChimpList
 * @author alexanderweiss
 *
 */
public class GrowthHistory implements JSONParser {

	//	private MailChimpList mailChimpList;
	private String id;
	private String month;
	private Integer existing;	// (deprecated)
	private Integer imports;	// (deprecated)
	private Integer optins;	// (deprecated)
	private Integer subscribed;
	private Integer unsubscribed;
	private Integer reconfirm;
	private Integer cleaned;
	private Integer pending;
	private Integer deleted;
	private Integer transactional;

	public GrowthHistory(JSONObject jsonObj) {
		parse(null, jsonObj);
	}

	@Override
	public void parse(MailChimpConnection connection, JSONObject entity) {
		JSONObjectCheck jObj = new JSONObjectCheck(entity);
		id = jObj.getString("list_id");
		month = jObj.getString("month");
		existing = jObj.getInt("existing");
		imports = jObj.getInt("imports");
		optins = jObj.getInt("optins");
		subscribed = jObj.getInt("subscribed");
		unsubscribed = jObj.getInt("unsubscribed");
		reconfirm = jObj.getInt("reconfirm");
		cleaned = jObj.getInt("cleaned");
		pending = jObj.getInt("pending");
		deleted = jObj.getInt("deleted");
		transactional = jObj.getInt("transactional");
	}

	/**
	 * @return The list id for the growth activity report.
	 */
	public String getId() {
		return id;
	}


	/**
	 * @return The month that the growth history is describing.
	 */
	public String getMonth() {
		return month;
	}


	/**
	 * @deprecated Deprecated in the Mailchimp API
	 */
	public Integer getExisting() {
		return existing;
	}


	/**
	 * @deprecated Deprecated in the Mailchimp API
	 */
	public Integer getImports() {
		return imports;
	}


	/**
	 * @deprecated Deprecated in the Mailchimp API
	 */
	public Integer getOptins() {
		return optins;
	}


	/**
	 * @return Newly subscribed members on the list for a specific month.
	 */
	public Integer getSubscribed() {
		return subscribed;
	}


	/**
	 * @return Newly unsubscribed members on the list for a specific month.
	 */
	public Integer getUnsubscribed() {
		return unsubscribed;
	}


	/**
	 * @return Newly reconfirmed members on the list for a specific month.
	 */
	public Integer getReconfirm() {
		return reconfirm;
	}


	/**
	 * @return Newly cleaned (hard-bounced) members on the list for a specific month.
	 */
	public Integer getCleaned() {
		return cleaned;
	}


	/**
	 * @return Pending members on the list for a specific month.
	 */
	public Integer getPending() {
		return pending;
	}


	/**
	 * @return Newly deleted members on the list for a specific month.
	 */
	public Integer getDeleted() {
		return deleted;
	}


	/**
	 * @return Subscribers that have been sent transactional emails via Mandrill.
	 */
	public Integer getTransactional() {
		return transactional;
	}

	@Override
	public String toString(){
		return "Growth History Id: " + getId() + " " + getMonth() + System.lineSeparator() +
				"    Subsribed: " + getSubscribed() + System.lineSeparator() +
				"    Unsubscribed: " + getUnsubscribed() + System.lineSeparator() +
				"    reconfirm: " + getReconfirm() + System.lineSeparator() +
				"    cleaned: " + getCleaned() + System.lineSeparator() +
				"    pending: " + getPending() + System.lineSeparator() +
				"    deleted: " + getDeleted() + System.lineSeparator() +
				"    transactional: " + getTransactional();
	}

}
