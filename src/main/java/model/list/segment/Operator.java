package model.list.segment;

/**
 * Created by alexanderweiss on 27.12.16.
 */
public enum Operator {

    IS("is"),NOT("not"), NOTCONTAIN("notcontain"),CONTAINS("contains"),STARTS("starts"), ENDS("ends"), GREATER("greater"),LESS("less");

    private String stringRepresentation;

    Operator(String stringRepresentation ){
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
