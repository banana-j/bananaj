package model.list.segment;

import org.json.JSONObject;

/**
 * Created by alexanderweiss on 27.12.16.
 */
public class Condition {


    private String field;
    private Operator operator;
    private String value;

    /**
     * Used when created a Condition locally with the Builder class
     * @see Builder
     * @param b
     */
    public Condition(Builder b){
        this.setOp(b.operator);
        this.setField(b.field);
        this.setValue(b.value);
    }


    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Operator getOp() {
        return operator;
    }

    public void setOp(Operator operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public JSONObject getJsonRepresentation(){
        JSONObject condition = new JSONObject();
        condition.put("op", this.getOp().getStringRepresentation());
        condition.put("field", this.getField());
        condition.put("value", this.getValue());

        return condition;
    }

    @Override
    public String toString(){
        return "Field: " + this.getField() + System.lineSeparator() +
                "Operator: " + this.getOp().getStringRepresentation() +  System.lineSeparator() +
                "Value: " + this.getValue();
    }

    public static class Builder {
        private String field;
        private Operator operator;
        private String value;

        public Builder field(String s) {
            this.field = s;
            return this;
        }

        public Builder operator(Operator op) {
            this.operator = op;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Condition build() {
            return new Condition(this);
        }
    }

}
