/**
 * @author alexanderweiss
 * @date 13.12.2015
 */
package model.report;


/**
 * Class for representing the average campaign statictics for your industry
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
		setType(type);
		setOpen_rate(open_rate);
		setClick_rate(click_rate);
		setBounce_rate(bounce_rate);
		setUnsub_rate(unsub_rate);
		setAbuse_rate(abuse_rate);
	}


	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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


	/**
	 * @return the bounce_rate
	 */
	public double getBounce_rate() {
		return bounce_rate;
	}


	/**
	 * @param bounce_rate the bounce_rate to set
	 */
	public void setBounce_rate(double bounce_rate) {
		this.bounce_rate = bounce_rate;
	}


	/**
	 * @return the unopen_rate
	 */
	public double getUnopen_rate() {
		return unopen_rate;
	}


	/**
	 * @param unopen_rate the unopen_rate to set
	 */
	public void setUnopen_rate(double unopen_rate) {
		this.unopen_rate = unopen_rate;
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
	 * @return the abuse_rate
	 */
	public double getAbuse_rate() {
		return abuse_rate;
	}


	/**
	 * @param abuse_rate the abuse_rate to set
	 */
	public void setAbuse_rate(double abuse_rate) {
		this.abuse_rate = abuse_rate;
	}

}
