package com.github.alexanderwe.bananaj.model.list.segment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by alexanderweiss on 04.02.16.
 */
public class Options {

    private MatchType matchType;
    private List<AbstractCondition> conditions;


    public Options(){

    }

    public Options (MatchType matchType, ArrayList<AbstractCondition> conditions){
        setMatchType(matchType);
        setConditions(conditions);
    }


    public void addCondition(AbstractCondition condition){
        this.conditions.add(condition);
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public List<AbstractCondition> getConditions() {
        return conditions;
    }

    public void setConditions(ArrayList<AbstractCondition> conditions) {
        this.conditions = conditions;
    }

    /**
     * Helper method to convert JSON for mailchimp PUT/PATCH/POST operations
     * @return
     */
    public JSONObject getJsonRepresentation(){
        JSONObject options = new JSONObject();
        options.put("match", this.getMatchType().value());

        JSONArray conditions = new JSONArray();

        for(AbstractCondition condition: this.getConditions()){
            conditions.put(condition.getJsonRepresentation());
        }

        options.put("conditions",conditions);
        System.out.println(options);
        return options;
    }

    @Override
    public String toString(){
        return "Match type: " + this.getMatchType().value() +  System.lineSeparator() +
                "Conditions: " + this.getConditions() + System.lineSeparator();
    }


}
