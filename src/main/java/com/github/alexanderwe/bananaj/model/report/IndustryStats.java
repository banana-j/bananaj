/**
 * @author alexanderweiss
 * @date 13.12.2015
 */
package com.github.alexanderwe.bananaj.model.report;


/**
 * Class for representing the average campaign statistics for your industry
 * @author alexanderweiss
 *
 */
public class IndustryStats {

	private String type;
	private double open_rate;
	private double click_rate;
	private double bounce_rate;
	private double unopen_rate;
	private double unsub_rate;
	private double abuse_rate;
	
	
	public IndustryStats(String type, double open_rate, double click_rate, double bounce_rate, double unopen_rate, double unsub_rate, double absue_rate) {
		this.type = type;
		this.open_rate = open_rate;
		this.click_rate = click_rate;
		this.bounce_rate = bounce_rate;
		this.unopen_rate = unopen_rate;
		this.unsub_rate = unsub_rate;
		this.abuse_rate = absue_rate;
	}


	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the open_rate
	 */
	public double getOpen_rate() {
		return open_rate;
	}

	/**
	 * @return the click_rate
	 */
	public double getClick_rate() {
		return click_rate;
	}

	/**
	 * @return the bounce_rate
	 */
	public double getBounce_rate() {
		return bounce_rate;
	}

	/**
	 * @return the unopen_rate
	 */
	public double getUnopen_rate() {
		return unopen_rate;
	}

	/**
	 * @return the unsub_rate
	 */
	public double getUnsub_rate() {
		return unsub_rate;
	}

	/**
	 * @return the abuse_rate
	 */
	public double getAbuse_rate() {
		return abuse_rate;
	}
}
