/**
 * @author weljoda
 * @date 28.03.2019
 */

package com.github.bananaj.model.list.member;

/**
 * @author weljoda
 *
 */
public enum TagStatus {
	ACTIVE("active"), 
	INACTIVE("inactive");

	private String stringRepresentation;

	TagStatus(String stringRepresentation ) {
		setStringRepresentation(stringRepresentation);
	}

	@Override
	public String toString() {
		return stringRepresentation;
	}

	/**
	 * @param stringRepresentation Set the stringRepresentation for the enum constant.
	 */
	private void setStringRepresentation(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}

}
