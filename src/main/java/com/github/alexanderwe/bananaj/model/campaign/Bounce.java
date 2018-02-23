/**
 * @author alexanderweiss
 * @date 20.11.2015
 */
package model.campaign;

/**
 * Class representing a bounce for a campaign
 * @author alexanderweiss
 *
 */
public class Bounce {

	private int hard_bounces;
	private int soft_bounces;
	private int syntax_error_bounces;
	
	public Bounce(int hard_bounces, int soft_bounces, int syntax_error_bounces) {
		this.hard_bounces = hard_bounces;
		this.soft_bounces = soft_bounces;
		this.syntax_error_bounces = syntax_error_bounces;
	}

	/**
	 * @return the hard_bounces
	 */
	public int getHard_bounces() {
		return hard_bounces;
	}

	/**
	 * @return the soft_bounces
	 */
	public int getSoft_bounces() {
		return soft_bounces;
	}

	/**
	 * @return the syntax_error_bounces
	 */
	public int getSyntax_error_bounces() {
		return syntax_error_bounces;
	}

}
