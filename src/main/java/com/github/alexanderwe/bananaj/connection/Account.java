package com.github.alexanderwe.bananaj.connection;

import java.time.LocalDateTime;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.model.MailchimpObject;
import com.github.alexanderwe.bananaj.utils.DateConverter;

/**
 * Class for representing your mailchimp account
 */
public class Account extends MailchimpObject{

	private MailChimpConnection connection;
	private String loginId;
	private String accountName;
	private String email;
	private String firstName;
	private String lastName;
	private String username;
	private String avatarUrl;
	private String role;
	private LocalDateTime memberSince;
	private PricingPlanType pricingPlanType;
	private LocalDateTime firstPayment;
	private String accountTimezone;
	private String accountIndustry;
	private Contact contact;
	private boolean proEnabled;
	private LocalDateTime lastLogin;
	private int totalSubscribers;
	private IndustryStats industryStats;

	public Account(MailChimpConnection connection, JSONObject jsonObj) {
		super(jsonObj.getString("account_id"), jsonObj);
		this.connection = connection;
		this.loginId = jsonObj.getString("login_id");
		this.accountName = jsonObj.getString("account_name");
		this.email = jsonObj.getString("email");
		this.firstName = jsonObj.getString("first_name");
		this.lastName = jsonObj.getString("last_name");
		this.username = jsonObj.getString("username");
		this.avatarUrl = jsonObj.getString("avatar_url");
		this.role = jsonObj.getString("role");
		this.memberSince = DateConverter.getInstance().createDateFromISO8601(jsonObj.getString("member_since"));
		this.pricingPlanType = PricingPlanType.valueOf(jsonObj.getString("pricing_plan_type").toUpperCase());
		if (jsonObj.has("first_payment")) {
			this.firstPayment = DateConverter.getInstance().createDateFromISO8601(jsonObj.getString("first_payment"));
		}
		this.accountTimezone = jsonObj.getString("account_timezone");
		this.accountIndustry = jsonObj.getString("account_industry");
		this.proEnabled = jsonObj.getBoolean("pro_enabled");
		this.lastLogin = DateConverter.getInstance().createDateFromISO8601(jsonObj.getString("last_login"));
		this.totalSubscribers = jsonObj.getInt("total_subscribers");
		contact = new Contact(jsonObj.getJSONObject("contact"));

		if (jsonObj.has("industry_stats")) {
			industryStats = new IndustryStats(jsonObj.getJSONObject("industry_stats"));
		}
	}

	/**
	 * @return the {@link #connection}
	 */
	public MailChimpConnection getConnection() {
		return connection;
	}

	/**
	 * The ID associated with the user who owns this API key. If you can login to
	 * multiple accounts, this ID will be the same for each account.
	 */
	public String getLoginId() {
		return loginId;
	}

	/**
	 * The name of the account
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * The account email address
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * The first name tied to the account
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * The last name tied to the account
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * The username tied to the account
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * URL of the avatar for the user
	 */
	public String getAvatarUrl() {
		return avatarUrl;
	}

	/**
	 * The user role for the account
	 */
	public String getRole() {
		return role;
	}

	/**
	 * The date and time that the account was created
	 */
	public LocalDateTime getMemberSince() {
		return memberSince;
	}

	/**
	 * The type of pricing plan the account is on
	 */
	public PricingPlanType getPricingPlanType() {
		return pricingPlanType;
	}

	/**
	 * Date of first payment for monthly plans
	 */
	public LocalDateTime getFirstPayment() {
		return firstPayment;
	}

	/**
	 * The timezone currently set for the account
	 */
	public String getAccountTimezone() {
		return accountTimezone;
	}

	/**
	 * The user-specified industry associated with the account
	 */
	public String getAccountIndustry() {
		return accountIndustry;
	}

	/**
	 * Whether the account includes Mailchimp Pro
	 */
	public boolean isProEnabled() {
		return proEnabled;
	}

	/**
	 * The date and time of the last login for this account 
	 */
	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	/**
	 * The total number of subscribers across all lists in the account
	 */
	public int getTotalSubscribers() {
		return totalSubscribers;
	}

	/**
	 * Information about the account contact
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * The average campaign statistics for all campaigns in the account’s specified industry
	 */
	public IndustryStats getIndustryStats() {
		return industryStats;
	}

	@Override
	public String toString() {
		return this.getId() + System.lineSeparator() +
				this.getEmail() + System.lineSeparator() +
				this.getAccountName() + System.lineSeparator() +
				this.getContact().toString() + System.lineSeparator() +
				"Last Login: " + this.lastLogin + System.lineSeparator() +
				"Total subscribers: " + this.totalSubscribers;
	}

	public enum PricingPlanType {

		MONTHLY("monthly"), PAY_AS_YOU_GO("pay_as_you_go"), FOREVER_FREE("forever_free");

		private String stringRepresentation;

		PricingPlanType(String stringRepresentation ) {
			setStringRepresentation(stringRepresentation);
		}

		/**
		 * @return the stringRepresentation
		 */
		public String getStringRepresentation() {
			return stringRepresentation;
		}

		/**
		 * @param stringRepresentation the stringRepresentation to set
		 */
		private void setStringRepresentation(String stringRepresentation) {
			this.stringRepresentation = stringRepresentation;
		}
	}

	public class Contact {
		private String company;
		private String address1;
		private String address2;
		private String city;
		private String state;
		private String zip;
		private String country;

		public Contact() {

		}

		public Contact(JSONObject jsonObj) {
			this.company = jsonObj.getString("company");
			this.address1 = jsonObj.getString("addr1");
			this.address2 = jsonObj.getString("addr2");
			this.city = jsonObj.getString("city");
			this.state = jsonObj.getString("state");
			this.zip = jsonObj.getString("zip");
			this.country = jsonObj.getString("country");
		}

		/**
		 * The company name for the account
		 */
		public String getCompany() {
			return company;
		}

		/**
		 * The street address for the account contact
		 */
		public String getAddress1() {
			return address1;
		}

		/**
		 * The street address for the account contact
		 */
		public String getAddress2() {
			return address2;
		}

		/**
		 * The city for the account contact
		 */
		public String getCity() {
			return city;
		}

		/**
		 * The state for the account contact
		 */
		public String getState() {
			return state;
		}

		/**
		 * The zip code for the account contact
		 */
		public String getZip() {
			return zip;
		}

		/**
		 * The country for the account contact
		 */
		public String getCountry() {
			return country;
		}

		@Override
		public String toString() {
			return 
					getCompany() + System.lineSeparator() +
					getAddress1() + System.lineSeparator() +
					(getAddress2().length() > 0 ? getAddress2() + System.lineSeparator() : "")+
					getCity() + " " + getState() + " " + getZip() + " " + getCountry();
		}
	}

	public class IndustryStats {
		private double open_rate;
		private double bounce_rate;
		private double click_rate;

		public IndustryStats() {

		}

		public IndustryStats(JSONObject jsonObj) {
			this.open_rate = jsonObj.getDouble("open_rate");
			this.bounce_rate = jsonObj.getDouble("bounce_rate");
			this.click_rate = jsonObj.getDouble("click_rate");
		}

		/**
		 * The average unique open rate for all campaigns in the account’s specified industry
		 */
		public double getOpen_rate() {
			return open_rate;
		}

		/**
		 * The average bounce rate for all campaigns in the account’s specified industry
		 */
		public double getBounce_rate() {
			return bounce_rate;
		}

		/**
		 * The average unique click rate for all campaigns in the account’s specified industry
		 */
		public double getClick_rate() {
			return click_rate;
		}
	}
}