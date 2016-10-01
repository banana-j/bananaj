/**
 * @author alexanderweiss
 * @date 13.12.2015
 */
package model.report;


/**
 * Class for representing list stats in a report object
 * @author alexanderweiss
 *
 */
public class ReportListStats {

	private double sub_rate;
	private double unsub_rate;
	private double open_rate;
	private double click_rate;
	
	public ReportListStats(double sub_rate, double unsub_rate, double open_rate, double click_rate) {
		setSub_rate(sub_rate);
		setUnsub_rate(unsub_rate);
		setOpen_rate(open_rate);
		setClick_rate(click_rate);
	}

	/**
	 * @return the sub_rate
	 */
	public double getSub_rate() {
		return sub_rate;
	}

	/**
	 * @param sub_rate the sub_rate to set
	 */
	public void setSub_rate(double sub_rate) {
		this.sub_rate = sub_rate;
	}

	/**
	 * @return the unsub_rate
	 */
	public double getUnsub_rate() {
		return unsub_rate;
	}

	/**
	 * @param unsub_rate the unsub_rate to set
	 */
	public void setUnsub_rate(double unsub_rate) {
		this.unsub_rate = unsub_rate;
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
	 * @return the click_rate
	 */
	public double getClick_rate() {
		return click_rate;
	}

	/**
	 * @param click_rate the click_rate to set
	 */
	public void setClick_rate(double click_rate) {
		this.click_rate = click_rate;
	}

}
