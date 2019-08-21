package com.github.alexanderwe.bananaj.model.filemanager;

public enum FileType {
	
	IMAGE("image"), FILE("file");

	private String stringRepresentation;
	
	FileType(String stringRepresentation ) {
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
