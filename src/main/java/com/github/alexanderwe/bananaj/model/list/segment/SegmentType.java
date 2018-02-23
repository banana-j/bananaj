package com.github.alexanderwe.bananaj.model.list.segment;

/**
 * Created by alexanderweiss on 04.02.16.
 */
public enum SegmentType {

    STATIC("static"), FUZZY("fuzzy"), ABSPLIT("absplit"), SAVED("saved");

    private String stringRepresentation;

    SegmentType(String stringRepresentation ){
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
    public void setStringRepresentation(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }

}
