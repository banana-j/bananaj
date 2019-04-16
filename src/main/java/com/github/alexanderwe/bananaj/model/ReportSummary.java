package com.github.alexanderwe.bananaj.model;

import org.json.JSONObject;

public class ReportSummary {

	private int opens;
	private int uniqueOpens;
	private double openRate;
	private int clicks;
	private int subscriberClicks;
	private double clickRate;
	private Ecommerce ecommerce;

	public ReportSummary(JSONObject jsonObj) {
		opens = jsonObj.getInt("opens");
		uniqueOpens = jsonObj.getInt("unique_opens");
		openRate = jsonObj.getDouble("open_rate");
		clicks = jsonObj.getInt("clicks");
		subscriberClicks = jsonObj.getInt("subscriber_clicks");
		clickRate = jsonObj.getDouble("click_rate");
		
		if (jsonObj.has("ecommerce")) {
			ecommerce = new Ecommerce(jsonObj.getJSONObject("ecommerce"));
		}
	}

	public ReportSummary() {

	}

	/**
	 * The total number of opens for a campaign
	 * @return
	 */
	public int getOpens() {
		return opens;
	}

	/**
	 * The number of unique opens
	 * @return
	 */
	public int getUniqueOpens() {
		return uniqueOpens;
	}

	/**
	 * The number of unique opens divided by the total number of successful deliveries
	 * @return
	 */
	public double getOpenRate() {
		return openRate;
	}

	/**
	 * The total number of clicks for a campaign
	 * @return
	 */
	public int getClicks() {
		return clicks;
	}

	/**
	 * The number of unique clicks
	 * @return
	 */
	public int getSubscriberClicks() {
		return subscriberClicks;
	}

	/**
	 * The number of unique clicks divided by the total number of successful deliveries
	 * @return
	 */
	public double getClickRate() {
		return clickRate;
	}

	/**
	 * E-Commerce stats for a campaign
	 * @return null or E-Commerce stats
	 */
	public Ecommerce getEcommerce() {
		return ecommerce;
	}

	@Override
	public String toString() {
		return 
				"Opens: " + opens + System.lineSeparator() +
				"Unique Opens: " + uniqueOpens + System.lineSeparator() +
				"Open Rate: " + openRate + System.lineSeparator() +
				"Clicks: " + clicks + System.lineSeparator() +
				"Subscriber Clicks: " + subscriberClicks + System.lineSeparator() +
				"click Rate: " + clickRate + 
				(ecommerce != null ? System.lineSeparator() + ecommerce.toString() : "");
	}

	public class Ecommerce {
		private int totalOrders;
		private double totalSpent;
		private double totalRevenue;

		public Ecommerce() {
			
		}
		
		public Ecommerce(JSONObject jsonObj) {
			this.totalOrders = jsonObj.getInt("total_orders");
			this.totalSpent = jsonObj.getDouble("total_spent");
			this.totalRevenue = jsonObj.getDouble("total_revenue");
		}

		/**
		 * The total orders for a campaign
		 * @return
		 */
		public int getTotalOrders() {
			return totalOrders;
		}

		/**
		 * The total spent for a campaign. Calculated as the sum of all order totals with no deductions.
		 * @return
		 */
		public double getTotalSpent() {
			return totalSpent;
		}

		/**
		 * The total revenue for a campaign. Calculated as the sum of all order totals minus shipping and tax totals.
		 * @return
		 */
		public double getTotalRevenue() {
			return totalRevenue;
		}

		@Override
		public String toString() {
			return 
					"Total Orders: " + totalOrders + System.lineSeparator() +
					"Total Spent: " + totalSpent + System.lineSeparator() +
					"Total Revenue: " + totalRevenue;
		}

	}
}
