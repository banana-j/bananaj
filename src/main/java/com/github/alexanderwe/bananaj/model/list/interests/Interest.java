package com.github.alexanderwe.bananaj.model.list.interests;

import org.json.JSONObject;

public class Interest {
	private String category_id;
    private String list_id;
    private String id;
    private String name;
    private String subscriber_count;
    private Integer display_order;

	public Interest(String id, String list_id, String category_id, String name, String subscriber_count, Integer display_order) {
		this.category_id = category_id;
		this.list_id = list_id;
		this.id = id;
		this.name = name;
		this.subscriber_count = subscriber_count;
		this.display_order = display_order;
	}

    /**
     * Used when created a Segment locally with the Builder class
     * @see Builder
     * @param b
     */
    public Interest(Builder b) {
		this.category_id = b.category_id;
		this.list_id = b.list_id;
		this.id = b.id;
		this.name = b.name;
		this.subscriber_count = b.subscriber_count;
		this.display_order = b.display_order;
    }
    

	public String getCategory_id() {
		return category_id;
	}

	public String getList_id() {
		return list_id;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSubscriber_count() {
		return subscriber_count;
	}

	public Integer getDisplay_order() {
		return display_order;
	}

	public static Interest build(JSONObject jsonRepresentation) {
		String id = jsonRepresentation.getString("id");
		String list_id = jsonRepresentation.getString("list_id");
		String category_id = jsonRepresentation.getString("category_id");
		String name = jsonRepresentation.getString("name");
		String subscriber_count = jsonRepresentation.getString("subscriber_count");
		Integer display_order = jsonRepresentation.getInt("display_order");
		return new Interest(id, list_id, category_id, name, subscriber_count, display_order);
	}
	
	@Override
    public String toString(){
        return  "ID: " + this.getId() +  System.lineSeparator() +
                "List ID: " + this.getList_id() + System.lineSeparator() +
                "Category Id: " + this.getCategory_id() + System.lineSeparator() +
                "Name: " + this.getName() +  System.lineSeparator() +
                "Subscriber Count: " + this.getSubscriber_count() + System.lineSeparator() +
                "Display Order: " +  this.getDisplay_order() + System.lineSeparator();
    }
	
	public static class Builder {
		private String category_id;
	    private String list_id;
	    private String id;
	    private String name;
	    private String subscriber_count;
	    private Integer display_order;
		
        public Builder category_id(String s) {
            this.category_id = s;
            return this;
        }
        
        public Builder list_id(String s) {
            this.list_id = s;
            return this;
        }
        
        public Builder id(String s) {
            this.id = s;
            return this;
        }

        public Builder name(String s) {
            this.name = s;
            return this;
        }
        
        public Builder subscriber_count(String s) {
            this.subscriber_count = s;
            return this;
        }
        
        public Builder display_order(int i) {
            this.display_order = new Integer(i);
            return this;
        }

        public Interest build() {
            return new Interest(this);
        }
	}
}
