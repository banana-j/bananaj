/**
 * @author alexanderweiss
 * @date 12.12.2015
 */
package model.report;


/**
 * Class for representing the opens of a campaing
 * @author alexanderweiss
 *
 */
public class Open {

	private int opens_total;
	private int unique_opens;
	private double open_rate;
	private String last_open;
	
	
	public Open(int opens_total, int unique_opens, double open_rate, String last_open) {
		setOpens_total(opens_total);
		setUnique_opens(unique_opens);
		setOpen_rate(open_rate);
		setLast_open(last_open);
	}


	/**
	 * @return the opens_total
	 */
	public int getOpens_total() {
		return opens_total;
	}


	/**
	 * @param opens_total the opens_total to set
	 */
	public void setOpens_total(int opens_total) {
		this.opens_total = opens_total;
	}


	/**
	 * @return the unique_opens
	 */
	public int getUnique_opens() {
		return unique_opens;
	}


	/**
	 * @param unique_opens the unique_opens to set
	 */
	public void setUnique_opens(int unique_opens) {
		this.unique_opens = unique_opens;
	}


	/**
	 * @return the open_rate
	 */
	public double getOpen_rate() {
		return open_rate;
	}


	/**
	 * @param open_rate the open_rate to set
	 */
	public void setOpen_rate(double open_rate) {
		this.open_rate = open_rate;
	}


	/**
	 * @return the last_open
	 */
	public String getLast_open() {
		return last_open;
	}


	/**
	 * @param last_open the last_open to set
	 */
	public void setLast_open(String last_open) {
		this.last_open = last_open;
	}

}
