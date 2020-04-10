/**
 * @author alexanderweiss
 * @date 07.12.2015
 */
package com.github.bananaj.model.list;

import org.json.JSONObject;

/**
 * Class for representing a growth history of a mailChimpList
 * @author alexanderweiss
 *
 */
public class GrowthHistory {

	//	private MailChimpList mailChimpList;
	private String id;
	private String month;
	private Integer existing;	// (deprecated)
	private Integer imports;	// (deprecated)
	private Integer optins;	// (deprecated)
	private int subscribed;
	private int unsubscribed;
	private int reconfirm;
	private int cleaned;
	private int pending;
	private int deleted;
	private int transactional;

	public GrowthHistory(JSONObject jsonObj) {
		id = jsonObj.getString("list_id");
		month = jsonObj.getString("month");
		existing = jsonObj.has("existing") ? jsonObj.getInt("existing") : null;
		imports = jsonObj.has("imports") ? jsonObj.getInt("imports") : null;
		optins = jsonObj.has("optins") ? jsonObj.getInt("optins") : null;
		subscribed = jsonObj.getInt("subscribed");
		unsubscribed = jsonObj.getInt("unsubscribed");
		reconfirm = jsonObj.getInt("reconfirm");
		cleaned = jsonObj.getInt("cleaned");
		pending = jsonObj.getInt("pending");
		deleted = jsonObj.getInt("deleted");
		transactional = jsonObj.getInt("transactional");
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
	 * @deprecated
	 */
	public Integer getExisting() {
		return existing;
	}


	/**
	 * @deprecated
	 */
	public Integer getImports() {
		return imports;
	}


	/**
	 * @deprecated
	 */
	public Integer getOptins() {
		return optins;
	}


	/**
	 * @return Newly subscribed members on the list for a specific month.
	 */
	public int getSubscribed() {
		return subscribed;
	}


	/**
	 * @return Newly unsubscribed members on the list for a specific month.
	 */
	public int getUnsubscribed() {
		return unsubscribed;
	}


	/**
	 * @return Newly reconfirmed members on the list for a specific month.
	 */
	public int getReconfirm() {
		return reconfirm;
	}


	/**
	 * @return Newly cleaned (hard-bounced) members on the list for a specific month.
	 */
	public int getCleaned() {
		return cleaned;
	}


	/**
	 * @return Pending members on the list for a specific month.
	 */
	public int getPending() {
		return pending;
	}


	/**
	 * @return Newly deleted members on the list for a specific month.
	 */
	public int getDeleted() {
		return deleted;
	}


	/**
	 * @return Subscribers that have been sent transactional emails via Mandrill.
	 */
	public int getTransactional() {
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
