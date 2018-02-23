/**
 * @author alexanderweiss
 * @date 20.11.2015
 */
package com.github.alexanderwe.bananaj.model.report;

/**
 * Class for representing a forward in a campaign
 * @author alexanderweiss
 *
 */
public class Forward{

	private int count;
	private int forwards_open;
	
	public Forward(int count, int forward_open) {
		this.count = count;
		this.forwards_open = forward_open;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @return the forwards_open
	 */
	public int getForwards_open() {
		return forwards_open;
	}
}
