package model.list.segment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alexanderweiss on 04.02.16.
 */
public class Options {

    private MatchType matchType;
    private ArrayList<Condition> conditions;


    public Options(){

    }

    public Options (MatchType matchType, ArrayList<Condition> conditions){
        setMatchType(matchType);
        setConditions(conditions);
    }


    public void addCondition(Condition condition){
        this.conditions.add(condition);
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public ArrayList<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(ArrayList<Condition> conditions) {
        this.conditions = conditions;
    }

    public JSONObject getJsonRepresentation(){
        JSONObject options = new JSONObject();
        options.put("match", this.getMatchType().getStringRepresentation());

        JSONArray conditions = new JSONArray();

        for(Condition condition: this.getConditions()){
            conditions.put(condition.getJsonRepresentation());
        }

        options.put("conditions",conditions);
        System.out.println(options);
        return options;
    }

    @Override
    public String toString(){
        return "Match type: " + this.getMatchType().getStringRepresentation() +  System.lineSeparator() +
                "Conditions: " + this.getConditions() + System.lineSeparator();
    }


}
