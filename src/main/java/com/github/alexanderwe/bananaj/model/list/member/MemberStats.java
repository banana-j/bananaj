package com.github.alexanderwe.bananaj.model.list.member;

import org.json.JSONObject;

public class MemberStats {

	private double avgOpenRate;
	private double avgClickRate;
	private EcommerceData ecommerceData;

	public MemberStats() {
		ecommerceData = new EcommerceData();
	}

	public MemberStats(JSONObject stats) {
		this.avgOpenRate = stats.getDouble("avg_open_rate");
		this.avgClickRate = stats.getDouble("avg_click_rate");
		if (stats.has("stats")) {
			this.ecommerceData = new EcommerceData(stats.getJSONObject("stats"));
		} else {
			this.ecommerceData = new EcommerceData();
		}
	}

	/**
	 * A subscriber’s average open rate
	 * @return
	 */
	public double getAvgOpenRate() {
		return avgOpenRate;
	}

	/**
	 * A subscriber’s average clickthrough rate
	 * @return
	 */
	public double getAvgClickRate() {
		return avgClickRate;
	}

	/**
	 * Ecommerce stats for the list member if the list is attached to a store
	 * @return
	 */
	public EcommerceData getEcommerceData() {
		return ecommerceData;
	}


	public class EcommerceData {
		private double totalRevenue;
		private double numberOfOrders;
		private String currencyCode;

		public EcommerceData() {
			
		}
		
		public EcommerceData(JSONObject stats) {
			this.totalRevenue = stats.getDouble("total_revenue");
			this.numberOfOrders = stats.getDouble("number_of_orders");
			this.currencyCode = stats.getString("currency_code");
		}

		/**
		 * The total revenue the list member has brought in
		 * @return
		 */
		public double getTotalRevenue() {
			return totalRevenue;
		}

		/**
		 * The total number of orders placed by the list member
		 * @return
		 */
		public double getNumberOfOrders() {
			return numberOfOrders;
		}

		/**
		 * The three-letter ISO 4217 code for the currency that the store accepts
		 * @return
		 */
		public String getCurrencyCode() {
			return currencyCode;
		}
		
	}
}
