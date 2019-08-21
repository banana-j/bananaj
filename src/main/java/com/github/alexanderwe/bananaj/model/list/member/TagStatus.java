/**
 * @author weljoda
 * @date 28.03.2019
 */

package com.github.alexanderwe.bananaj.model.list.member;

/**
 * @author weljoda
 *
 */
public enum TagStatus {
	ACTIVE("active"), INACTIVE("inactive");

	private String stringRepresentation;
	
	TagStatus(String stringRepresentation ){
		setStringRepresentation(stringRepresentation);
	}

	/**
	 * @return the stringRepresentation
	 */
	public String getStringRepresentation() {
		return stringRepresentation;
	}

	/**
	 * @param stringRepresentation the stringRepresentation to set
	 */
	private void setStringRepresentation(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}
}
