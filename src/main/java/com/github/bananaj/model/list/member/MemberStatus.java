/**
 * @author alexanderweiss
 * @date 12.11.2015
 */
package com.github.bananaj.model.list.member;

/**
 * Possible status of a member of a list
 * @author alexanderweiss
 *
 */
public enum MemberStatus {

	PENDING("pending"),
	SUBSCRIBED("subscribed"),
	UNSUBSCRIBED("unsubscribed"),
	CLEANED("cleaned"),
	TRANSACTIONAL("transactional"),
	ARCHIVED("archived");
	
	
	private String stringRepresentation;
	
	MemberStatus(String stringRepresentation ) {
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
