package com.github.alexanderwe.bananaj.model.filemanager;

public enum FileType {
	
	IMAGE("image"), 
	FILE("file");

	private String stringRepresentation;
	
	FileType(String stringRepresentation ) {
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
