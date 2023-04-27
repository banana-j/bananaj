package com.github.bananaj.model.report;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.model.JSONParser;

public class EcommerceProductActivity implements JSONParser {
	private String title;
	private String sku;
	private String imageUrl;
	private Double totalRevenue;
	private Double totalPurchased;
	private String currencyCode;
	private Integer recommendationTotal;
	private Integer recommendationPurchased;

	public EcommerceProductActivity() {

	}

	public EcommerceProductActivity(JSONObject jsonObj) {
		parse(null, jsonObj);
	}

	@Override
	public void parse(MailChimpConnection connection, JSONObject entity) {
		title = entity.getString("title");
		sku = entity.getString("sku");
		imageUrl = entity.getString("image_url");
		totalRevenue = entity.getDouble("total_revenue");
		totalPurchased = entity.getDouble("total_purchased");
		currencyCode = entity.getString("currency_code");
		recommendationTotal = entity.getInt("recommendation_total");
		recommendationPurchased = entity.getInt("recommendation_purchased");
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the sku
	 */
	public String getSku() {
		return sku;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @return the totalRevenue
	 */
	public Double getTotalRevenue() {
		return totalRevenue;
	}

	/**
	 * @return the totalPurchased
	 */
	public Double getTotalPurchased() {
		return totalPurchased;
	}

	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * @return the recommendationTotal
	 */
	public Integer getRecommendationTotal() {
		return recommendationTotal;
	}

	/**
	 * @return the recommendationPurchased
	 */
	public Integer getRecommendationPurchased() {
		return recommendationPurchased;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return
				"Ecommerce Product Activity: " + System.lineSeparator() +
				"    Title: " + getTitle() + System.lineSeparator() +
				"    SKU: " + getSku() + System.lineSeparator() +
				"    Image URL: " + getImageUrl() + System.lineSeparator() +
				"    Total Revenue: " + getTotalRevenue() + System.lineSeparator() +
				"    Total Purchased: " + getTotalPurchased() + System.lineSeparator() +
				"    Currency Code: " + getCurrencyCode() + System.lineSeparator() +
				"    Recommendation Total: " + getRecommendationTotal() + System.lineSeparator() +
				"    Recommendation Purchased: " + getRecommendationPurchased(); 
	}

}
