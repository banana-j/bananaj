package com.github.alexanderwe.bananaj.model.list.interests;

import org.json.JSONObject;

/**
 * Manage interests for a specific Mailchimp audience list. Assign subscribers
 * to interests to group them together. Interests are referred to as ‘group
 * names’ in the Mailchimp application.
 *
 */
public class Interest {
	private String categoryId;
    private String listId;
    private String id;
    private String name;
    private String subscriberCount;
    private Integer displayOrder;

    /**
	 * Construct class given a Mailchimp JSON object
     * 
     * @param jsonObj
     */
	public Interest(JSONObject jsonObj) {
		categoryId = jsonObj.getString("category_id");
		listId = jsonObj.getString("list_id");
        id = jsonObj.getString("id");
        name = jsonObj.getString("name");
        subscriberCount = jsonObj.getString("subscriber_count");
        displayOrder = jsonObj.getInt("display_order");
	}
	
    /**
     *  {@link Interest.Builder} model for local construction
     * @see Interest.Builder
     * @param b
     */
    public Interest(Builder b) {
		this.categoryId = b.categoryId;
		this.listId = b.listId;
		this.id = b.id;
		this.name = b.name;
		this.subscriberCount = b.subscriberCount;
		this.displayOrder = b.displayOrder;
    }
    
    /**
     * @return The id for the interest category.
     */
	public String getCategoryId() {
		return categoryId;
	}

	/**
	 * @return The ID for the list that this interest belongs to.
	 */
	public String getListId() {
		return listId;
	}

	/**
	 * @return The ID for the interest.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return The name of the interest. This can be shown publicly on a subscription form.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The number of subscribers associated with this interest.
	 */
	public String getSubscriberCount() {
		return subscriberCount;
	}

	/**
	 * @return The display order for interests.
	 */
	public Integer getDisplayOrder() {
		return displayOrder;
	}

	@Override
    public String toString(){
        return  
				"Interest:" + System.lineSeparator() +
        		"    ID: " + this.getId() +  System.lineSeparator() +
                "    List ID: " + this.getListId() + System.lineSeparator() +
                "    Category Id: " + this.getCategoryId() + System.lineSeparator() +
                "    Name: " + this.getName() +  System.lineSeparator() +
                "    Subscriber Count: " + this.getSubscriberCount() + System.lineSeparator() +
                "    Display Order: " +  this.getDisplayOrder();
    }
	
	/**
	 * Builder for {@link Interest}
	 */
	public static class Builder {
		private String categoryId;
	    private String listId;
	    private String id;
	    private String name;
	    private String subscriberCount;
	    private Integer displayOrder;
		
        public Builder categoryId(String s) {
            this.categoryId = s;
            return this;
        }
        
        public Builder listId(String s) {
            this.listId = s;
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
        
        public Builder subscriberCount(String s) {
            this.subscriberCount = s;
            return this;
        }
        
        public Builder displayOrder(int i) {
            this.displayOrder = new Integer(i);
            return this;
        }

        public Interest build() {
            return new Interest(this);
        }
	}
}
