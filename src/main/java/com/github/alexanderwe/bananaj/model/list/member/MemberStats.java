package com.github.alexanderwe.bananaj.model.list.member;

import org.json.JSONObject;

/**
 * Open and click rates for this subscriber.
 */
public class MemberStats {

	private double avgOpenRate;
	private double avgClickRate;
	private EcommerceData ecommerceData;

	public MemberStats() {
		ecommerceData = new EcommerceData();
	}

	public MemberStats(JSONObject stats) {
		avgOpenRate = stats.getDouble("avg_open_rate");
		avgClickRate = stats.getDouble("avg_click_rate");
		ecommerceData = stats.has("stats") ? new EcommerceData(stats.getJSONObject("stats")) : null;
	}

	/**
	 * @return A subscriber’s average open rate.
	 */
	public double getAvgOpenRate() {
		return avgOpenRate;
	}

	/**
	 * @return A subscriber’s average clickthrough rate.
	 */
	public double getAvgClickRate() {
		return avgClickRate;
	}

	/**
	 * @return Ecommerce stats for the list member if the list is attached to a store.
	 */
	public EcommerceData getEcommerceData() {
		return ecommerceData;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return 
				"Stats:" + System.lineSeparator() +
				"    Avg Open Rate: " + getAvgOpenRate() + System.lineSeparator() +
				"    Avg Click Rate: " + getAvgClickRate() + 
				(getEcommerceData() != null ? System.lineSeparator() + getEcommerceData().toString() : "");
	}


	/**
	 * Ecommerce stats for the list member if the list is attached to a store.
	 */
	public class EcommerceData {
		private double totalRevenue;
		private double numberOfOrders;
		private String currencyCode;

		public EcommerceData() {
			
		}
		
		public EcommerceData(JSONObject stats) {
			totalRevenue = stats.getDouble("total_revenue");
			numberOfOrders = stats.getDouble("number_of_orders");
			currencyCode = stats.getString("currency_code");
		}

		/**
		 * @return The total revenue the list member has brought in.
		 */
		public double getTotalRevenue() {
			return totalRevenue;
		}

		/**
		 * @return The total number of orders placed by the list member.
		 */
		public double getNumberOfOrders() {
			return numberOfOrders;
		}

		/**
		 * @return The three-letter ISO 4217 code for the currency that the store accepts
		 */
		public String getCurrencyCode() {
			return currencyCode;
		}
	
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return 
					"Ecommerce:" + System.lineSeparator() +
					"    Total Revenue: " + getTotalRevenue() + System.lineSeparator() +
					"    Orders: " + getNumberOfOrders() + System.lineSeparator() +
					"    Currency Code: " + getCurrencyCode();
		}
	}
}
