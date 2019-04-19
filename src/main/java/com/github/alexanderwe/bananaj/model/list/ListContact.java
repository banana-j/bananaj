package com.github.alexanderwe.bananaj.model.list;

import org.json.JSONObject;

public class ListContact {
	
	private String company;		// The company name for the list
	private String address1;	// The street address for the list contact
	private String address2;	// The street address for the list contact
	private String city;		// The city for the list contact
	private String state;		// The state for the list contact
	private String zip;			// The postal or zip code for the list contact
	private String country;		// A two-character ISO3166 country code. Defaults to US if invalid
	private String phone;		// The phone number for the list contact

	public ListContact() {

	}

	public ListContact(JSONObject stats) {
		this.company = stats.getString("company");
		this.address1 = stats.getString("address1");
		this.address2 = stats.getString("address2");
		this.city = stats.getString("city");
		this.state = stats.getString("state");
		this.zip = stats.getString("zip");
		this.country = stats.getString("country");
		this.phone = stats.getString("phone");
	}

	/**
	 * The company name for the list
	 * @return
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * The street address for the list contact
	 * @return
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * The street address for the list contact
	 * @return
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * The city for the list contact
	 * @return
	 */
	public String getCity() {
		return city;
	}

	/**
	 * The state for the list contact
	 * @return
	 */
	public String getState() {
		return state;
	}

	/**
	 * The postal or zip code for the list contact
	 * @return
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * A two-character ISO3166 country code. Defaults to US if invalid.
	 * @return
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * The phone number for the list contact
	 * @return
	 */
	public String getPhone() {
		return phone;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return 
				"Contact:" + System.lineSeparator() +
				"    Company: " + getCompany() + System.lineSeparator() +
				"    Phone: " + getPhone() + System.lineSeparator() +
				"    Address: " + getAddress1() + System.lineSeparator() +
				(getAddress2() != null && getAddress2().length()>0 ? "             "+getAddress2() + System.lineSeparator() : "") +
				"             " + getCity() + " " + getState() + " " + getZip() + " " + getCountry();
	}

}
