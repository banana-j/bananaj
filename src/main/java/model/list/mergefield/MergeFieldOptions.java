package model.list.mergefield;

import java.util.ArrayList;

/**
 *  Class for representing merge field options.
 * Created by Alexander on 09.08.2016.
 */
public class MergeFieldOptions {

    private int default_country;
    private String phone_format;
    private String date_format;
    private ArrayList<String> choices;
    private int size;

    /**
     * Default constructor
     */
    public MergeFieldOptions(){
    }

    public MergeFieldOptions(int default_country, String phone_format, String date_format, ArrayList<String> choices, int size){
        this.default_country = default_country;
        this.phone_format = phone_format;
        this.date_format = date_format;
        this.choices = choices;
        this.size = size;
    }

    public int getDefault_country() {
        return default_country;
    }

    public String getPhone_format() {
        return phone_format;
    }

    public String getDate_format() {
        return date_format;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public int getSize() {
        return size;
    }

    public void setPhone_format(String phone_format) {
        this.phone_format = phone_format;
    }

    public void setDate_format(String date_format) {
        this.date_format = date_format;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString(){
        return "Default_Country: " + this.default_country + System.lineSeparator() +
                "Phone_Format: " + this.phone_format + System.lineSeparator() +
                "Date_Format: " + this.date_format + System.lineSeparator() +
                "Choices: " + this.choices + System.lineSeparator() +
                "Size: " + this.size;
    }

    public void setDefault_country(int default_country) {
        this.default_country = default_country;
    }
}
