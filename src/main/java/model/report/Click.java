/**
 * @author alexanderweiss
 * @date 20.11.2015
 */
package model.report;

import java.util.Date;

/**
 * Class for representing clicks for a campaign
 * @author alexanderweiss
 *
 */
public class Click {
	
	private int clicks_total;
	private int unique_clicks;
	private int unique_subscriber_clicks;
	private double click_rate;
	private Date last_click;

	public Click(int clicks_total, int unique_clicks, int unique_subsciber_clicks, double click_rate, Date last_click) {
		setClicks_total(clicks_total);
		setUnique_clicks(unique_clicks);
		setUnique_subscriber_clicks(unique_subsciber_clicks);
		setClick_rate(click_rate);
		setLast_click(last_click);
	}

	/**
	 * @return the clicks_total
	 */
	public int getClicks_total() {
		return clicks_total;
	}

	/**
	 * @param clicks_total the clicks_total to set
	 */
	private void setClicks_total(int clicks_total) {
		this.clicks_total = clicks_total;
	}

	/**
	 * @return the unique_clicks
	 */
	public int getUnique_clicks() {
		return unique_clicks;
	}

	/**
	 * @param unique_clicks the unique_clicks to set
	 */
	private void setUnique_clicks(int unique_clicks) {
		this.unique_clicks = unique_clicks;
	}

	/**
	 * @return the unique_subscriber_clicks
	 */
	public int getUnique_subscriber_clicks() {
		return unique_subscriber_clicks;
	}

	/**
	 * @param unique_subscriber_clicks the unique_subscriber_clicks to set
	 */
	private void setUnique_subscriber_clicks(int unique_subscriber_clicks) {
		this.unique_subscriber_clicks = unique_subscriber_clicks;
	}

	/**
	 * @return the click_rate
	 */
	public double getClick_rate() {
		return click_rate;
	}

	/**
	 * @param click_rate the click_rate to set
	 */
	private void setClick_rate(double click_rate) {
		this.click_rate = click_rate;
	}

	/**
	 * @return the last_click
	 */
	public Date getLast_click() {
		return last_click;
	}

	/**
	 * @param last_click the last_click to set
	 */
	private void setLast_click(Date last_click) {
		this.last_click = last_click;
	}

}
