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
		setCount(count);
		setFowards_open(forward_open);
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	private void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the fowards_open
	 */
	public int getFowards_open() {
		return fowards_open;
	}

	/**
	 * @param fowards_open the fowards_open to set
	 */
	private void setFowards_open(int fowards_open) {
		this.fowards_open = fowards_open;
	}

}
