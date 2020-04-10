package com.github.bananaj.model.list;

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

	public ListContact(Builder b) {
		company = b.company;
		address1 = b.address1;
		address2 = b.address2;
		city = b.city;
		state = b.state;
		zip = b.zip;
		country = b.country;
		phone = b.phone;
	}

	/**
	 * The company name for the list
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * The street address for the list contact
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * The street address for the list contact
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * The city for the list contact
	 */
	public String getCity() {
		return city;
	}

	/**
	 * The state for the list contact
	 */
	public String getState() {
		return state;
	}

	/**
	 * The postal or zip code for the list contact
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * A two-character ISO3166 country code. Defaults to US if invalid.
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * The phone number for the list contact
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @return the jsonRepresentation
	 */
	protected JSONObject getJSONRepresentation() {
		JSONObject json = new JSONObject();

		json.put("company", company);
		json.put("address1", address1);
		json.put("address2", address2);
		json.put("city", city);
		json.put("state", state);
		json.put("zip", zip);
		json.put("country", country);
		json.put("phone", phone);
		
		return json;
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

    /**
     * Builder for {@link ListContact}
     *
     */
    public static class Builder {
    	private String company;		// The company name for the list
    	private String address1;	// The street address for the list contact
    	private String address2;	// The street address for the list contact
    	private String city;		// The city for the list contact
    	private String state;		// The state for the list contact
    	private String zip;			// The postal or zip code for the list contact
    	private String country = "US";		// A two-character ISO3166 country code. Defaults to US if invalid
    	private String phone;		// The phone number for the list contact
		/**
		 * @param company The company name for the list.
		 */
		public void company(String company) {
			this.company = company;
		}
		/**
		 * @param address1 The street address for the list contact.
		 */
		public void address1(String address1) {
			this.address1 = address1;
		}
		/**
		 * @param address2 The street address for the list contact.
		 */
		public void address2(String address2) {
			this.address2 = address2;
		}
		/**
		 * @param city The city for the list contact.
		 */
		public void city(String city) {
			this.city = city;
		}
		/**
		 * @param state The state for the list contact.
		 */
		public void state(String state) {
			this.state = state;
		}
		/**
		 * @param zip The postal or zip code for the list contact.
		 */
		public void zip(String zip) {
			this.zip = zip;
		}
		/**
		 * @param country A two-character ISO3166 country code. Defaults to US if invalid.
		 */
		public void country(String country) {
			this.country = country;
		}
		/**
		 * @param phone The phone number for the list contact.
		 */
		public void phone(String phone) {
			this.phone = phone;
		}
    	
    	public ListContact build() {
    		return new ListContact(this);
    	}
    }
}
