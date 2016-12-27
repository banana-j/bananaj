package connection;

import model.MailchimpObject;
import org.json.JSONObject;

import java.util.Date;

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
	private Date last_login;
	private int subscriber_count;

	public Account(MailChimpConnection connection, String id, String account_name, String company, String address1, String address2, String city, String state, String zip, String country, Date last_login, int subscriber_count, JSONObject jsonrepresentation) {
		super(id, jsonrepresentation);
		setConnection(connection);
		setAccount_name(account_name);
		setCompany(company);
		setAddress1(address1);
		setAddress2(address2);
		setCity(city);
		setState(state);
		setZip(zip);
		setCountry(country);
		setLast_login(last_login);
		setSubscriber_count(subscriber_count);
		setApiKey(connection.getApikey());
	}


	/**
	 * @return the connection
	 */
	public MailChimpConnection getConnection() {
		return connection;
	}

	/**
	 * @param connection the connection to set
	 */
	public void setConnection(MailChimpConnection connection) {
		this.connection = connection;
	}

	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the apiKey
	 */
	protected String getApiKey() {
		return apiKey;
	}

	/**
	 * @param apiKey the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
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

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public Date getLast_login() {
	return last_login;
}

	public void setLast_login(Date last_login) {
	this.last_login = last_login;
}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public int getSubscriber_count() {
		return subscriber_count;
	}

	public void setSubscriber_count(int subscriber_count) {
		this.subscriber_count = subscriber_count;
	}
}