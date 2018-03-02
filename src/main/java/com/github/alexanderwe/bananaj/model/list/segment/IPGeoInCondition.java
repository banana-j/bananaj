package com.github.alexanderwe.bananaj.model.list.segment;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.exceptions.ConditionException;

/**
 * Segment option condition condition_type for "IPGeoIn"
 */
public class IPGeoInCondition implements AbstractCondition {

    private Operator op;
    private String field;
    private String lng;
    private String lat;
    private Integer value;
    private String addr;

    /**
     * Used when created a Condition locally with the Builder class
     * @see Builder
     * @param b
     */

    public IPGeoInCondition(Builder b) throws ConditionException{
    	this.op = b.op;
    	this.field = b.field;
    	this.lng = b.lng;
    	this.lat = b.lat;
    	this.addr = b.addr;
    	this.value = b.value;
    }

    public String getField() {
        return field;
    }

    public Operator getOp() {
        return op;
    }

    public String getLng() {
        return lng;
    }

    public Integer getValue() {
        return value;
    }

	public String getLat() {
		return lat;
	}

	public String getAddr() {
		return addr;
	}

	@Override
    public JSONObject getJsonRepresentation(){
        JSONObject condition = new JSONObject();
        condition.put("op", getOp().value());
        condition.put("field", getField());
        condition.put("value", getValue());

        return condition;
    }

    @Override
    public String toString(){
        return "Field: " + getField() + System.lineSeparator() +
                "Operator: " + getOp().value() +  System.lineSeparator() +
                "Lat: " + getLat() + System.lineSeparator() +
                "Lng: " + getLng() + System.lineSeparator() +
                "Value: " + getValue() + System.lineSeparator() +
                "Addr: " + getAddr() + System.lineSeparator();
    }

    public static class Builder {
        private Operator op;
        private String field;
        private String lng;
        private String lat;
        private Integer value;
        private String addr;

        public Builder field(String field) {
            this.field = field;
            return this;
        }

        public Builder operator(Operator op) {
            this.op = op;
            return this;
        }

        public Builder lng(String lng) {
            this.lng = lng;
            return this;
        }

        public Builder lat(String lat) {
            this.lat = lat;
            return this;
        }

       public Builder value(Integer value) {
            this.value = value;
            return this;
        }

       public Builder addr(String addr) {
           this.addr = addr;
           return this;
       }

        public IPGeoInCondition build() {
            try {
                return new IPGeoInCondition(this);
            } catch (ConditionException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
