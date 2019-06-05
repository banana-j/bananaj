/**
 * @author alexanderweiss
 * @date 12.11.2015
 */
package com.github.alexanderwe.bananaj.model.list.member;

/**
 * Possible status of a member of a list
 * @author alexanderweiss
 *
 */
public enum MemberStatus {

	PENDING("pending"),SUBSCRIBED("subscribed"),UNSUBSCRIBED("unsubscribed"),CLEANED("cleaned"),TRANSACTIONAL("transactional");
	
	private String stringRepresentation;
	
	MemberStatus(String stringRepresentation ){
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
