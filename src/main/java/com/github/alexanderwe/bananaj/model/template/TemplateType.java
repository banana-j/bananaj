/**
 * @author alexanderweiss
 * @date 19.11.2015
 */
package com.github.alexanderwe.bananaj.model.template;

public enum TemplateType {


		USER("user"),BASE("base"),GALLERY("gallery");
		
		private String stringRepresentation;
		
		TemplateType(String stringRepresentation ){
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
