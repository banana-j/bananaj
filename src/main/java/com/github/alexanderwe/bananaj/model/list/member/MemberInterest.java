package com.github.alexanderwe.bananaj.model.list.member;

public class MemberInterest {
    String interest_id;
    boolean subscribed;

	private MemberInterest() {
	}

	public MemberInterest(String interest_id, boolean subscribed) {
		this.interest_id = interest_id;
		this.subscribed = subscribed;
	}

	public String getInterest_id() {
		return interest_id;
	}

	public boolean isSubscribed() {
		return subscribed;
	}
	
    @Override
    public String toString(){
        return "interest_id: " +this.interest_id +
                " subscribed: " + this.subscribed + System.lineSeparator();
    }
}
