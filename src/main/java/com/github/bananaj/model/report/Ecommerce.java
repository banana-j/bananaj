package com.github.bananaj.model.report;

import org.json.JSONObject;

import com.github.bananaj.utils.JSONObjectCheck;

/**
 * E-Commerce stats for a campaign.
 *
 */
public class Ecommerce {
	private Integer totalOrders;
	private Double totalSpent;
	private Double totalRevenue;
	private String currencyCode;

	public Ecommerce() {
		
	}
	
	public Ecommerce(JSONObject jsonObj) {
		JSONObjectCheck jObj = new JSONObjectCheck(jsonObj);
		this.totalOrders = jObj.getInt("total_orders");
		this.totalSpent = jObj.getDouble("total_spent");
		this.totalRevenue = jObj.getDouble("total_revenue");
		this.currencyCode = jObj.getString("currency_code");
	}

	/**
	 * The total orders for a campaign
	 */
	public Integer getTotalOrders() {
		return totalOrders;
	}

	/**
	 * The total spent for a campaign. Calculated as the sum of all order totals with no deductions.
	 */
	public Double getTotalSpent() {
		return totalSpent;
	}

	/**
	 * The total revenue for a campaign. Calculated as the sum of all order totals minus shipping and tax totals.
	 */
	public Double getTotalRevenue() {
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
