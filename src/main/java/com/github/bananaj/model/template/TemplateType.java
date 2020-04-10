/**
 * @author alexanderweiss
 * @date 19.11.2015
 */
package com.github.bananaj.model.template;

public enum TemplateType {

	USER("user"),
	BASE("base"),
	GALLERY("gallery");

	private String stringRepresentation;

	TemplateType(String stringRepresentation ) {
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
