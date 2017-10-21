/**
 * @author alexanderweiss
 * @date 12.12.2015
 */
package model.report;

/**
 * Class for representing the opens of a campaign
 * @author alexanderweiss
 *
 */
public class Open {

	private int opens_total;
	private int unique_opens;
	private double open_rate;
	private String last_open;
	
	
	public Open(int opens_total, int unique_opens, double open_rate, String last_open) {
		this.opens_total = opens_total;
		this.unique_opens = unique_opens;
		this.open_rate = open_rate;
		this.last_open = last_open;
	}

	/**
	 * @return the opens_total
	 */
	public int getOpens_total() {
		return opens_total;
	}

	/**
	 * @return the unique_opens
	 */
	public int getUnique_opens() {
		return unique_opens;
	}

	/**
	 * @return the open_rate
	 */
	public double getOpen_rate() {
		return open_rate;
	}

	/**
	 * @return the last_open
	 */
	public String getLast_open() {
		return last_open;
	}
}
