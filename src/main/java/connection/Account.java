package connection;

import model.MailchimpObject;
import org.json.JSONObject;

import java.time.LocalDateTime;

/**
 * Class for representing your mailchimp account
 */
 public class Account extends MailchimpObject{

	private MailChimpConnection connection;
	private String apiKey;
	private String account_name;
	private String company;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String country;
	private LocalDateTime last_login;
	private int subscriber_count;

	public Account(MailChimpConnection connection, String id, String account_name, String company, String address1, String address2, String city, String state, String zip, String country, LocalDateTime last_login, int subscriber_count, JSONObject jsonrepresentation) {
		super(id, jsonrepresentation);
		this.connection = connection;
		this.account_name = account_name;
		this.company = company;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;
		this.last_login = last_login;
		this.subscriber_count = subscriber_count;
		this.apiKey = getApiKey();
	}

	/**
	 * @return the connection
	 */
	public MailChimpConnection getConnection() {
		return connection;
	}

	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}


	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @return the apiKey
	 */
	protected String getApiKey() {
		return apiKey;
	}

	public String getAddress2() {
		return address2;
	}

	public LocalDateTime getLast_login() {
	return last_login;
}

	public String getAccount_name() {
		return account_name;
	}

	public int getSubscriber_count() {
		return subscriber_count;
	}

	@Override
	public String toString(){
		return this.company + System.lineSeparator() +
				this.address1 + System.lineSeparator() +
				this.address2 + System.lineSeparator() +
				this.zip + System.lineSeparator() +
				this.city + System.lineSeparator() +
				this.state + System.lineSeparator() +
				"Last Login: " + this.last_login + System.lineSeparator() +
				"Total subscribers: " + this.subscriber_count;
	}
}