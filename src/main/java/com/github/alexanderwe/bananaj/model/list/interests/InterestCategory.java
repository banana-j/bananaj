package com.github.alexanderwe.bananaj.model.list.interests;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.connection.MailChimpConnection;
import com.github.alexanderwe.bananaj.model.MailchimpObject;

public class InterestCategory extends MailchimpObject {
    private String list_id;
    private String title;
    private Integer display_order;
    private InterestCategoryType type;
    private MailChimpConnection connection;

	public InterestCategory(String id, String list_id, String title, Integer display_order,
			InterestCategoryType type, MailChimpConnection connection, JSONObject jsonRepresentation) {
		super(id, jsonRepresentation);
		this.list_id = list_id;
		this.title = title;
		this.display_order = display_order;
		this.type = type;
		this.connection = connection;
	}

    /**
     * Used when created a Segment locally with the Builder class
     * @see Builder
     * @param b
     */
    public InterestCategory(Builder b) {
		super(b.id, b.jsonRepresentation);
		this.list_id = b.list_id;
		this.title = b.title;
		this.display_order = b.display_order;
		this.type = b.type;
    }
    
    public String getList_id() {
		return list_id;
	}

	public String getTitle() {
		return title;
	}

	public Integer getDisplay_order() {
		return display_order;
	}

	public InterestCategoryType getType() {
		return type;
	}

	public static InterestCategory build(MailChimpConnection connection, JSONObject jsonRepresentation) {
		String id = jsonRepresentation.getString("id");
		String list_id = jsonRepresentation.getString("list_id");
		String title = jsonRepresentation.getString("title");
		Integer display_order = jsonRepresentation.getInt("display_order");
		InterestCategoryType type = InterestCategoryType.fromValue(jsonRepresentation.getString("type"));
		return new InterestCategory(id, list_id, title, display_order, type, connection, jsonRepresentation);
	}
	
	@Override
    public String toString(){
        return  "ID: " + this.getId() +  System.lineSeparator() +
                "Title: " + this.getTitle() +  System.lineSeparator() +
                "Type: " + this.getType() + System.lineSeparator() +
                "List ID: " + this.getList_id() + System.lineSeparator() +
                "Display Order: " +  this.getDisplay_order() + System.lineSeparator();
    }
	
	public static class Builder {
	    private String list_id;
	    private String id;
	    private String title;
	    private Integer display_order;
	    private InterestCategoryType type;
        private JSONObject jsonRepresentation = new JSONObject();
		
        public Builder list_id(String s) {
            this.list_id = s;
            jsonRepresentation.put("list_id", s);
            return this;
        }
        
        public Builder id(String s) {
            this.id = s;
            jsonRepresentation.put("id", s);
            return this;
        }

        public Builder title(String s) {
            this.title = s;
            jsonRepresentation.put("title", s);
            return this;
        }
        
        public Builder display_order(int i) {
            this.display_order = new Integer(i);
            jsonRepresentation.put("display_order", i);
            return this;
        }
        
        public Builder type(InterestCategoryType type) {
            this.type = type;
            jsonRepresentation.put("type", type.value());
            return this;
        }

        public Builder type(String type) {
            this.type = InterestCategoryType.fromValue(type);
            jsonRepresentation.put("type", this.type.value());
            return this;
        }
        
        public InterestCategory build() {
            return new InterestCategory(this);
        }
	}
}
