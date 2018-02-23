package com.github.alexanderwe.bananaj.model.list.segment;

import com.github.alexanderwe.bananaj.exceptions.ConditionException;
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

    public Condition(Builder b) throws ConditionException{
        if (b.operator == null) {
            throw new ConditionException("A condition need an operator.");
        } else {
            this.operator = b.operator;
        }

        if (b.field == null) {
            throw new ConditionException("A condition need a field to operate on.");
        } else {
            this.field = b.field;
        }

        if (b.value == null) {
            throw new ConditionException("A condition need a value to compare on.");
        } else {
            this.value = b.value;
        }
    }

    public String getField() {
        return field;
    }

    public Operator getOp() {
        return operator;
    }

    public String getValue() {
        return value;
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
            try {
                return new Condition(this);
            } catch (ConditionException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
