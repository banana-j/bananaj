package com.github.alexanderwe.bananaj.model.report;

import org.json.JSONObject;

public class EcommerceProductActivity {
	private String title;
	private String sku;
	private String imageUrl;
	private Double totalRevenue;
	private Double totalPurchased;
	private String currencyCode;
	private Integer recommendationTotal;
	private Integer recommendationPurchased;

	public EcommerceProductActivity(JSONObject jsonObj) {
		title = jsonObj.getString("title");
		sku = jsonObj.getString("sku");
		imageUrl = jsonObj.getString("image_url");
		totalRevenue = jsonObj.getDouble("total_revenue");
		totalPurchased = jsonObj.getDouble("total_purchased");
		currencyCode = jsonObj.getString("currency_code");
		recommendationTotal = jsonObj.getInt("recommendation_total");
		recommendationPurchased = jsonObj.getInt("recommendation_purchased");
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
