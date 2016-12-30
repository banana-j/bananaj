/**
 * @author alexanderweiss
 * @date 20.11.2015
 */
package model.report;

import java.time.LocalDateTime;
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
	private LocalDateTime last_click;

	public Click(int clicks_total, int unique_clicks, int unique_subsciber_clicks, double click_rate, LocalDateTime last_click) {
		this.clicks_total = clicks_total;
		this.unique_clicks = unique_clicks;
		this.unique_subscriber_clicks = unique_subsciber_clicks;
		this.click_rate = click_rate;
		this.last_click = last_click;
	}

	/**
	 * @return the clicks_total
	 */
	public int getClicks_total() {
		return clicks_total;
	}

	/**
	 * @return the unique_clicks
	 */
	public int getUnique_clicks() {
		return unique_clicks;
	}

	/**
	 * @return the unique_subscriber_clicks
	 */
	public int getUnique_subscriber_clicks() {
		return unique_subscriber_clicks;
	}

	/**
	 * @return the click_rate
	 */
	public double getClick_rate() {
		return click_rate;
	}

	/**
	 * @return the last_click
	 */
	public LocalDateTime getLast_click() {
		return last_click;
	}


}
