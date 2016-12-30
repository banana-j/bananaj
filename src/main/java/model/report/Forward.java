/**
 * @author alexanderweiss
 * @date 20.11.2015
 */
package model.report;

/**
 * Class for representing a forward in a campaign
 * @author alexanderweiss
 *
 */
public class Forward{

	private int count;
	private int fowards_open;
	
	public Forward(int count, int forward_open) {
		this.count = count;
		this.fowards_open = forward_open;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @return the fowards_open
	 */
	public int getFowards_open() {
		return fowards_open;
	}
}
