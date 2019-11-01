package com.github.alexanderwe.bananaj.model.report;

import org.json.JSONObject;

public class Ecommerce {
	private int totalOrders;
	private double totalSpent;
	private double totalRevenue;
	private String currencyCode;

	public Ecommerce() {
		
	}
	
	public Ecommerce(JSONObject jsonObj) {
		this.totalOrders = jsonObj.getInt("total_orders");
		this.totalSpent = jsonObj.getDouble("total_spent");
		this.totalRevenue = jsonObj.getDouble("total_revenue");
		this.currencyCode = jsonObj.has("currency_code") ? jsonObj.getString("currency_code") : null;
	}

	/**
	 * The total orders for a campaign
	 */
	public int getTotalOrders() {
		return totalOrders;
	}

	/**
	 * The total spent for a campaign. Calculated as the sum of all order totals with no deductions.
	 */
	public double getTotalSpent() {
		return totalSpent;
	}

	/**
	 * The total revenue for a campaign. Calculated as the sum of all order totals minus shipping and tax totals.
	 */
	public double getTotalRevenue() {
		return totalRevenue;
	}

	/**
	 * The three-letter ISO 4217 code for the currency
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	
	@Override
	public String toString() {
		return 
				"    Ecommerce:" + System.lineSeparator() +
				"        Total Orders: " + getTotalOrders() + System.lineSeparator() +
				"        Total Spent: " + getTotalSpent() + System.lineSeparator() +
				"        Total Revenue: " + getTotalRevenue() + 
				(getCurrencyCode() != null ? System.lineSeparator() + "        Currency Code: " + getCurrencyCode() : "");
	}

}
