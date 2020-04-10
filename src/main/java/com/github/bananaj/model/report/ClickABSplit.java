package com.github.bananaj.model.report;

import org.json.JSONObject;

public class ClickABSplit {
	private Integer totalClicks;
	private Double clickPercentage;
	private Integer uniqueClicks;
	private Double uniqueClickPercentage;
	private String group;

	protected ClickABSplit(String group, JSONObject jsonObj) {
		this.group = group;
		totalClicks = jsonObj.getInt("total_clicks_"+group);
		clickPercentage = jsonObj.getDouble("click_percentage_"+group);
		uniqueClicks = jsonObj.getInt("unique_clicks_"+group);
		uniqueClickPercentage = jsonObj.getDouble("unique_click_percentage_"+group);
	}

	/**
	 * @return The total number of clicks for Group.
	 */
	public Integer getTotalClicks() {
		return totalClicks;
	}

	/**
	 * @return The percentage of total clicks for Group.
	 */
	public Double getClickPercentage() {
		return clickPercentage;
	}

	/**
	 * @return The number of unique clicks for Group.
	 */
	public Integer getUniqueClicks() {
		return uniqueClicks;
	}

	/**
	 * @return The percentage of unique clicks for Group
	 */
	public Double getUniqueClickPercentage() {
		return uniqueClickPercentage;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return
				"Group "+ group +": " + System.lineSeparator() +
				"    Total Clicks: " + getTotalClicks() + System.lineSeparator() +
				"    Click Percentage: " + getClickPercentage() + System.lineSeparator() +
				"    Unique Clicks: " + getUniqueClicks() + System.lineSeparator() +
				"    Unique Click Percentage: " + getUniqueClickPercentage();
	}

}
