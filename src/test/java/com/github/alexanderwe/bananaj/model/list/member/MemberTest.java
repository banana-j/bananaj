package com.github.alexanderwe.bananaj.model.list.member;

import static org.junit.Assert.*;

import java.util.List;

import org.json.JSONObject;
import org.junit.Test;

public class MemberTest {

	@Test
	public void testMember() {
		JSONObject jsonObj = new JSONObject("{\"id\":\"123456a1189a0f1234e567e892b0e29a\",\"email_address\":\"mr.test@gmail.com\",\"unique_email_id\":\"1234567890\",\"web_id\":123455501,\"email_type\":\"html\",\"status\":\"subscribed\",\"merge_fields\":{\"FNAME\":\"Sue\",\"LNAME\":\"Smith\",\"ADDRESS\":{\"addr1\":\"1000 W May Road\",\"addr2\":\"Suite 500\",\"city\":\"Chandler\",\"state\":\"AZ\",\"zip\":\"85203\",\"country\":\"US\"},\"PHONE\":\"\"},\"interests\":{\"1ecfb1d267\":false,\"7be4b38789\":true,\"0c9ec66eb9\":true,\"657d72c93a\":true,\"29ef97b9a2\":true,\"29a08f16a7\":false,\"ed0b539b24\":false},\"stats\":{\"avg_open_rate\":0,\"avg_click_rate\":0},\"ip_signup\":\"\",\"timestamp_signup\":\"2019-01-09T16:19:25+00:00\",\"ip_opt\":\"64.215.182.118\",\"timestamp_opt\":\"2019-01-09T16:19:25+00:00\",\"member_rating\":2,\"last_changed\":\"2019-06-14T17:54:25+00:00\",\"language\":\"\",\"vip\":false,\"email_client\":\"Gmail\",\"location\":{\"latitude\":33.3162999999999982492226990871131420135498046875,\"longitude\":-111.8310000000000030695446184836328029632568359375,\"gmtoff\":0,\"dstoff\":0,\"country_code\":\"US\",\"timezone\":\"480\"},\"source\":\"List Import\",\"tags_count\":5,\"tags\":[{\"id\":67669,\"name\":\"TEST_BRI\"},{\"id\":67401,\"name\":\"TEST_MIL\"},{\"id\":67277,\"name\":\"TEST_FAY\"},{\"id\":67273,\"name\":\"TEST_HUN\"},{\"id\":67269,\"name\":\"TEST_STV\"}],\"list_id\":\"abc6de12f4\",\"_links\":[{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/abc6de12f4/members/3365d1c1111b3f7523e433e482b0b123\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Response.json\"},{\"rel\":\"parent\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/abc6de12f4/members\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Members.json\"},{\"rel\":\"update\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/abc6de12f4/members/3365d1c1111b3f7523e433e482b0b123\",\"method\":\"PATCH\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Response.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/PATCH.json\"},{\"rel\":\"upsert\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/abc6de12f4/members/3365d1c1111b3f7523e433e482b0b123\",\"method\":\"PUT\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Response.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/PUT.json\"},{\"rel\":\"delete\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/abc6de12f4/members/3365d1c1111b3f7523e433e482b0b123\",\"method\":\"DELETE\"},{\"rel\":\"activity\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/abc6de12f4/members/3365d1c1111b3f7523e433e482b0b123/activity\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Activity/Response.json\"},{\"rel\":\"goals\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/abc6de12f4/members/3365d1c1111b3f7523e433e482b0b123/goals\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Goals/Response.json\"},{\"rel\":\"notes\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/abc6de12f4/members/3365d1c1111b3f7523e433e482b0b123/notes\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Notes/CollectionResponse.json\"},{\"rel\":\"events\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/abc6de12f4/members/3365d1c1111b3f7523e433e482b0b123/events\",\"method\":\"POST\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Events/POST.json\"},{\"rel\":\"delete_permanent\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/abc6de12f4/members/3365d1c1111b3f7523e433e482b0b123/actions/delete-permanent\",\"method\":\"POST\"}]},");
    	Member member = new Member(null, jsonObj);
    	assertEquals(member.getId(), "123456a1189a0f1234e567e892b0e29a");
    	assertEquals(member.getEmailAddress(), "mr.test@gmail.com");
    	assertEquals(member.getUniqueEmailId(), "1234567890");
    	assertEquals(MemberStatus.SUBSCRIBED, member.getStatus());
    	
    	JSONObject json = member.getJsonRepresentation();
    	assertEquals(json.getString("email_address"), "mr.test@gmail.com");
    	member.toString();
	}

	@Test
	public void testMember_MemberTag() {
		JSONObject jsonObj = new JSONObject("{\"id\":48145,\"name\":\"TestTag\"}");
		MemberTag t = new MemberTag(jsonObj);
		assertEquals(t.getId(), new Integer(48145));
		assertEquals(t.getName(), "TestTag");
	}
	
	@Test
	public void testMember_MemberNote() {
		JSONObject jsonObj = new JSONObject("{\"id\":29821,\"created_at\":\"2019-10-22T21:36:06+00:00\",\"created_by\":\"John Smith\",\"updated_at\":\"2019-10-22T21:36:06+00:00\",\"note\":\"This is a test note\",\"list_id\":\"d4a27625e0\",\"email_id\":\"3365d1c1111b3f7523e433e482b0b123\",\"_links\":[{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/d4a27625e0/members/3365d1c1111b3f7523e433e482b0b123/notes/29821\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Notes/Response.json\"},{\"rel\":\"parent\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/d4a27625e0/members/3365d1c1111b3f7523e433e482b0b123/notes\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Notes/CollectionResponse.json\"},{\"rel\":\"update\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/d4a27625e0/members/3365d1c1111b3f7523e433e482b0b123/notes/29821\",\"method\":\"PATCH\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Notes/Response.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Notes/PATCH.json\"},{\"rel\":\"delete\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/d4a27625e0/members/3365d1c1111b3f7523e433e482b0b123/notes/29821\",\"method\":\"DELETE\"}]}");
		MemberNote n = new MemberNote(jsonObj);
		assertEquals(n.getId(), 29821);
		assertEquals(n.getNote(), "This is a test note");
		assertEquals(n.getCreatedBy(), "John Smith");
		assertEquals(n.getListId(), "d4a27625e0");
		assertEquals(n.getEmailId(), "3365d1c1111b3f7523e433e482b0b123");
		n.toString();
	}
	
	// #51 Deleting member from list is causing deserialisation error
	@Test
	public void testMemberArchived() {
		JSONObject jsonObj = new JSONObject("{\"id\":\"19f7a458143f3fc7d2f0eb4bb5278e90\",\"email_address\":\"mr.test@gmail.com\",\"unique_email_id\":\"1454205bcc\",\"web_id\":202938833,\"email_type\":\"html\",\"status\":\"archived\",\"merge_fields\":{\"FNAME\":\"Edwin\",\"LNAME\":\"Martin\",\"ADDRESS\":\"\",\"PHONE\":\"\"},\"interests\":{\"ee345b5fa4\":false,\"8374ef08c9\":false,\"180ac7290e\":false,\"03e658ef14\":false,\"7e70c523a5\":false,\"a311a2c2d5\":false,\"0ec1eaad17\":false},\"stats\":{\"avg_open_rate\":0,\"avg_click_rate\":0},\"ip_signup\":\"\",\"timestamp_signup\":\"2020-04-27T14:23:48+00:00\",\"ip_opt\":\"67.2.189.161\",\"timestamp_opt\":\"2020-04-30T02:23:49+00:00\",\"member_rating\":2,\"last_changed\":\"2020-04-30T20:24:05+00:00\",\"language\":\"en\",\"vip\":false,\"email_client\":\"\",\"location\":{\"latitude\":0,\"longitude\":0,\"gmtoff\":0,\"dstoff\":0,\"country_code\":\"\",\"timezone\":\"\"},\"source\":\"API - Generic\",\"tags_count\":0,\"tags\":[],\"list_id\":\"d4a27625e0\",\"_links\":[{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/d4a27625e0/members/19f7a458143f3fc7d2f0eb4bb5278e90\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Response.json\"},{\"rel\":\"parent\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/d4a27625e0/members\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Members.json\"},{\"rel\":\"update\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/d4a27625e0/members/19f7a458143f3fc7d2f0eb4bb5278e90\",\"method\":\"PATCH\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Response.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/PATCH.json\"},{\"rel\":\"upsert\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/d4a27625e0/members/19f7a458143f3fc7d2f0eb4bb5278e90\",\"method\":\"PUT\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Response.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/PUT.json\"},{\"rel\":\"delete\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/d4a27625e0/members/19f7a458143f3fc7d2f0eb4bb5278e90\",\"method\":\"DELETE\"},{\"rel\":\"activity\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/d4a27625e0/members/19f7a458143f3fc7d2f0eb4bb5278e90/activity\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Activity/Response.json\"},{\"rel\":\"goals\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/d4a27625e0/members/19f7a458143f3fc7d2f0eb4bb5278e90/goals\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Goals/Response.json\"},{\"rel\":\"notes\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/d4a27625e0/members/19f7a458143f3fc7d2f0eb4bb5278e90/notes\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Notes/CollectionResponse.json\"},{\"rel\":\"events\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/d4a27625e0/members/19f7a458143f3fc7d2f0eb4bb5278e90/events\",\"method\":\"POST\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/Events/POST.json\"},{\"rel\":\"delete_permanent\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/d4a27625e0/members/19f7a458143f3fc7d2f0eb4bb5278e90/actions/delete-permanent\",\"method\":\"POST\"}]},");
    	Member member = new Member(null, jsonObj);
    	assertEquals("19f7a458143f3fc7d2f0eb4bb5278e90", member.getId());
    	assertEquals("mr.test@gmail.com", member.getEmailAddress());
    	assertEquals("1454205bcc", member.getUniqueEmailId());
    	assertEquals(MemberStatus.ARCHIVED, member.getStatus());
    	
    	JSONObject json = member.getJsonRepresentation();
    	assertEquals("mr.test@gmail.com", json.getString("email_address"));
    	member.toString();
	}
}
