package com.github.bananaj.connection;

import java.time.ZonedDateTime;

import org.json.JSONObject;

import com.github.bananaj.utils.JSONObjectCheck;

/**
 * The API root resource links to all other resources available in the API. Also includes details about the Mailchimp user account.
 */
public class Account {

	private MailChimpConnection connection;
	private String id;
	private String loginId;
	private String accountName;
	private String email;
	private String firstName;
	private String lastName;
	private String username;
	private String avatarUrl;
	private String role;
	private ZonedDateTime memberSince;
	private PricingPlanType pricingPlanType;
	private ZonedDateTime firstPayment;
	private String accountTimezone;
	private String accountIndustry;
	private Contact contact;
	private Boolean proEnabled;
	private ZonedDateTime lastLogin;
	private Integer totalSubscribers;
	private IndustryStats industryStats;

	public Account(MailChimpConnection connection, JSONObject account) {
		JSONObjectCheck jObj = new JSONObjectCheck(account);
		this.connection = connection;
		this.id = jObj.getString("account_id");
		this.loginId = jObj.getString("login_id");
		this.accountName = jObj.getString("account_name");
		this.email = jObj.getString("email");
		this.firstName = jObj.getString("first_name");
		this.lastName = jObj.getString("last_name");
		this.username = jObj.getString("username");
		this.avatarUrl = jObj.getString("avatar_url");
		this.role = jObj.getString("role");
		this.memberSince = jObj.getISO8601Date("member_since");
		this.pricingPlanType = jObj.getEnum(PricingPlanType.class, "pricing_plan_type");
		this.firstPayment = jObj.getISO8601Date("first_payment");
		this.accountTimezone = account.getString("account_timezone");
		this.accountIndustry = jObj.getString("account_industry");
		this.proEnabled = jObj.getBoolean("pro_enabled");
		this.lastLogin = jObj.getISO8601Date("last_login");
		this.totalSubscribers = jObj.getInt("total_subscribers");
		if (account.has("contact")) {
			contact = new Contact(account.getJSONObject("contact"));
		}
		if (account.has("industry_stats")) {
			industryStats = new IndustryStats(account.getJSONObject("industry_stats"));
		}
	}

	/**
	 * @return the {@link #connection}
	 */
	public MailChimpConnection getConnection() {
		return connection;
	}

	/**
	 * @return The Mailchimp account ID, used for features like list subscribe forms.
	 */
	public String getId() {
		return id;
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
	 * @see <a href="https://mailchimp.com/help/manage-user-levels-in-your-account/" target="MailchimpAPIDoc">user role</a> 
	 */
	public String getRole() {
		return role;
	}

	/**
	 * The date and time that the account was created
	 */
	public ZonedDateTime getMemberSince() {
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
	public ZonedDateTime getFirstPayment() {
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
	 * Legacy - whether the account includes Mailchimp Pro.
	 * @see <a href="https://mailchimp.com/help/about-mailchimp-pro/" target="MailchimpAPIDoc">Mailchimp Pro</a> 
	 */
	public Boolean isProEnabled() {
		return proEnabled;
	}

	/**
	 * The date and time of the last login for this account 
	 */
	public ZonedDateTime getLastLogin() {
		return lastLogin;
	}

	/**
	 * The total number of subscribers across all lists in the account
	 */
	public Integer getTotalSubscribers() {
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
	 * @see <a href="https://mailchimp.com/resources/research/email-marketing-benchmarks/?utm_source=mc-api&utm_medium=docs&utm_campaign=apidocs" target="MailchimpAPIDoc">average campaign statistics</a> 
	 */
	public IndustryStats getIndustryStats() {
		return industryStats;
	}

	@Override
	public String toString() {
		return "Id: " + getId() + System.lineSeparator() +
				"Email: " + getEmail() + System.lineSeparator() +
				"Account name: " + getAccountName() + System.lineSeparator() +
				"Priceing Plan: " + getPricingPlanType() + System.lineSeparator() +
				"Contact: " + getContact().toString() + System.lineSeparator() +
				"User: " + getFirstName() + " " + getLastName() + " <" + getUsername() +">" + System.lineSeparator() +
				"Last Login: " + getLastLogin() + System.lineSeparator() +
				"Total subscribers: " + getTotalSubscribers() +
				(getAccountIndustry() != null ? System.lineSeparator() + "Industry: " + getAccountIndustry() : "") +
				(getIndustryStats() != null ? System.lineSeparator() + getIndustryStats().toString() : "");
	}

	public enum PricingPlanType {

		MONTHLY("monthly"), 
		PAY_AS_YOU_GO("pay_as_you_go"), 
		FOREVER_FREE("forever_free");

		private String stringRepresentation;

		PricingPlanType(String stringRepresentation ) {
			setStringRepresentation(stringRepresentation);
		}

		@Override
		public String toString() {
			return stringRepresentation;
		}

		/**
		 * @param stringRepresentation Set the stringRepresentation for the enum constant.
		 */
		private void setStringRepresentation(String stringRepresentation) {
			this.stringRepresentation = stringRepresentation;
		}
	}

	/**
	 * Information about the account contact.
	 *
	 */
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

		public Contact(JSONObject contact) {
			JSONObjectCheck jObj = new JSONObjectCheck(contact);
			this.company = jObj.getString("company");
			this.address1 = jObj.getString("addr1");
			this.address2 = jObj.getString("addr2");
			this.city = jObj.getString("city");
			this.state = jObj.getString("state");
			this.zip = jObj.getString("zip");
			this.country = jObj.getString("country");
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

	/**
	 * The average campaign statistics for all campaigns in the account’s specified industry
	 * @see <a href="https://mailchimp.com/resources/research/email-marketing-benchmarks/?utm_source=mc-api&utm_medium=docs&utm_campaign=apidocs" target="MailchimpAPIDoc">average campaign statistics</a> 
	 */
	public class IndustryStats {
		private Double openRate;
		private Double bounceRate;
		private Double clickRate;

		public IndustryStats() {

		}

		public IndustryStats(JSONObject stats) {
			JSONObjectCheck jObj = new JSONObjectCheck(stats);
			this.openRate = jObj.getDouble("open_rate");
			this.bounceRate = jObj.getDouble("bounce_rate");
			this.clickRate = jObj.getDouble("click_rate");
		}

		/**
		 * The average unique open rate for all campaigns in the account’s specified industry
		 */
		public Double getOpenRate() {
			return openRate;
		}

		/**
		 * The average bounce rate for all campaigns in the account’s specified industry
		 */
		public Double getBounceRate() {
			return bounceRate;
		}

		/**
		 * The average unique click rate for all campaigns in the account’s specified industry
		 */
		public Double getClickRate() {
			return clickRate;
		}

		@Override
		public String toString() {
			return
					"Industry Stats:" + System.lineSeparator() +
					"    Open Rate: " + getOpenRate() + System.lineSeparator() +
					"    Click Rate: " + getClickRate() + System.lineSeparator() +
					"    Bounce Rate: " + getBounceRate();
		}
	}
}