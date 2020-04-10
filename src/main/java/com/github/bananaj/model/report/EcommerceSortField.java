package com.github.bananaj.model.report;

public enum EcommerceSortField {

	TITLE("title"),
	TOTAL_REVENUE("total_revenue"),
	TOTAL_PURCHASED("total_purchased");
	
	private String stringRepresentation;

	EcommerceSortField(String stringRepresentation ) {
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
