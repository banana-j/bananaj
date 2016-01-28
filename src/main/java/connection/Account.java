package connection;

	/**
	 * Class for representing your mailchimp account
	 */
	 public class Account {

		private MailchimpConnection connection;
		private String apiKey;
		private String company;
		private String address1;
		private String city;
		private String state;
		private String zip;
		private String country;

		public Account(MailchimpConnection connection, String company, String address1, String city, String state, String zip, String country) {
			setConnection(connection);
			setCompany(company);
			setAddress1(address1);
			setCity(city);
			setState(state);
			setZip(zip);
			setCountry(country);
			setApiKey(connection.getApikey());
		}

		/**
		 * @return the connection
		 */
		public MailchimpConnection getConnection() {
			return connection;
		}

		/**
		 * @param connection the connection to set
		 */
		public void setConnection(MailchimpConnection connection) {
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
		public String getApiKey() {
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
					this.city + System.lineSeparator() +
					this.state + System.lineSeparator() +
					this.zip + System.lineSeparator();
		}
		

	}