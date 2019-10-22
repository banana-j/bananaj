package com.github.alexanderwe.bananaj.model.list;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;

import com.github.alexanderwe.bananaj.model.list.interests.Interest;
import com.github.alexanderwe.bananaj.model.list.interests.InterestCategory;
import com.github.alexanderwe.bananaj.model.list.interests.InterestCategory.Builder;
import com.github.alexanderwe.bananaj.model.list.interests.InterestCategoryType;
import com.github.alexanderwe.bananaj.model.list.segment.MatchType;
import com.github.alexanderwe.bananaj.model.list.segment.Options;
import com.github.alexanderwe.bananaj.model.list.segment.Segment;
import com.github.alexanderwe.bananaj.model.list.segment.SegmentType;

public class MailChimpListTest {

	@Test
	public void testMailChimpList() {
		JSONObject jsonObj = new JSONObject("{\"id\":\"abc0be64d2\",\"web_id\":282000,\"name\":\"Test Analytics\",\"contact\":{\"company\":\"ABC Corp\",\"address1\":\"1000 W Bay Rd Ste 500\",\"address2\":\"\",\"city\":\"Chandler\",\"state\":\"AZ\",\"zip\":\"85226-2496\",\"country\":\"US\",\"phone\":\"5553387000\"},\"permission_reminder\":\"You are receiving this email as a partner in Testing initiative.\",\"use_archive_bar\":true,\"campaign_defaults\":{\"from_name\":\"Analytics Robot\",\"from_email\":\"no.spam@gmail.com\",\"subject\":\"\",\"language\":\"en\"},\"notify_on_subscribe\":\"\",\"notify_on_unsubscribe\":\"\",\"date_created\":\"2019-06-06T21:19:33+00:00\",\"list_rating\":0,\"email_type_option\":false,\"subscribe_url_short\":\"http://eepurl.com/gub30A\",\"subscribe_url_long\":\"https://us3.list-manage.com/subscribe?u=c00f3ed44928e2d1d4f00000d&id=aaa1cc11a1\",\"beamer_address\":\"us3-4ca796b90c-7644bc000a@inbound.mailchimp.com\",\"visibility\":\"pub\",\"double_optin\":false,\"has_welcome\":false,\"marketing_permissions\":false,\"modules\":[],\"stats\":{\"member_count\":34,\"unsubscribe_count\":1,\"cleaned_count\":0,\"member_count_since_send\":1,\"unsubscribe_count_since_send\":1,\"cleaned_count_since_send\":0,\"campaign_count\":0,\"campaign_last_sent\":\"\",\"merge_field_count\":4,\"avg_sub_rate\":0,\"avg_unsub_rate\":0,\"target_sub_rate\":0,\"open_rate\":0,\"click_rate\":0,\"last_sub_date\":\"2019-06-13T22:52:25+00:00\",\"last_unsub_date\":\"2019-06-06T22:37:51+00:00\"},\"_links\":[{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Response.json\"},{\"rel\":\"parent\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists.json\"},{\"rel\":\"update\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1\",\"method\":\"PATCH\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Response.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/PATCH.json\"},{\"rel\":\"batch-sub-unsub-members\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1\",\"method\":\"POST\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/BatchPOST-Response.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/BatchPOST.json\"},{\"rel\":\"delete\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1\",\"method\":\"DELETE\"},{\"rel\":\"abuse-reports\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/abuse-reports\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Abuse/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Abuse.json\"},{\"rel\":\"activity\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/activity\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Activity/Response.json\"},{\"rel\":\"clients\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/clients\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Clients/Response.json\"},{\"rel\":\"growth-history\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/growth-history\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Growth/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Growth.json\"},{\"rel\":\"interest-categories\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/interest-categories\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/InterestCategories/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/InterestCategories.json\"},{\"rel\":\"members\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/members\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Members.json\"},{\"rel\":\"merge-fields\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/merge-fields\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/MergeFields/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/MergeFields.json\"},{\"rel\":\"segments\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/segments\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Segments.json\"},{\"rel\":\"webhooks\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/webhooks\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Webhooks/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Webhooks.json\"},{\"rel\":\"signup-forms\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/signup-forms\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/SignupForms/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/SignupForms.json\"},{\"rel\":\"locations\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/locations\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Locations/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Locations.json\"}]}");
		MailChimpList list = new MailChimpList(null, jsonObj);
		assertEquals(list.getId(), "abc0be64d2");
		assertEquals(list.getWebId(), 282000);
		assertEquals(list.getName(), "Test Analytics");
		assertEquals(list.getContact().getPhone(), "5553387000");
	}

	@Test
	public void testMailChimpList_InterestCategory() {
		JSONObject jsonObj = new JSONObject("{\"list_id\":\"aaa6be6111\",\"id\":\"51d15ef0e9\",\"title\":\"Test Discipline\",\"display_order\":0,\"type\":\"checkboxes\",\"_links\":[{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/interest-categories/51d15ef0e9\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/InterestCategories/Response.json\"},{\"rel\":\"parent\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/interest-categories\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/InterestCategories/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/InterestCategories.json\"},{\"rel\":\"update\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/interest-categories/51d15ef0e9\",\"method\":\"PATCH\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/InterestCategories/Response.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/InterestCategories/PATCH.json\"},{\"rel\":\"delete\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/interest-categories/51d15ef0e9\",\"method\":\"DELETE\"},{\"rel\":\"interests\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/interest-categories/51d15ef0e9/interests\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Interests/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Interests.json\"}]}");
		// TODO: refactor for consistency with other classes - use constructor and parse
		InterestCategory cat = InterestCategory.build(null, jsonObj);
		assertEquals(cat.getId(), "51d15ef0e9");
		// TODO: refactor  should be called getListId()
		assertEquals(cat.getList_id(), "aaa6be6111");
		assertEquals(cat.getTitle(), "Test Discipline");
		assertEquals(cat.getDisplay_order(), new Integer(0));
		assertEquals(cat.getType(), InterestCategoryType.CHECKBOXES);
	}
	
	@Test
	public void testMailChimpList_Interest() {
		JSONObject jsonObj = new JSONObject("{\"category_id\":\"51d15ef0e9\",\"list_id\":\"aaa6be6111\",\"id\":\"1ecfb1d267\",\"name\":\"Arts and Music\",\"subscriber_count\":\"2\",\"display_order\":1,\"_links\":[{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/interest-categories/51d15ef0e9/interests/1ecfb1d267\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Interests/Response.json\"},{\"rel\":\"parent\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/interest-categories/51d15ef0e9/interests\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Interests/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Interests.json\"},{\"rel\":\"update\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/interest-categories/51d15ef0e9/interests/1ecfb1d267\",\"method\":\"PATCH\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Interests/Response.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Interests/PATCH.json\"},{\"rel\":\"delete\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/interest-categories/51d15ef0e9/interests/1ecfb1d267\",\"method\":\"DELETE\"}]}");
		Interest interest = new Interest(jsonObj);
		assertEquals(interest.getId(), "1ecfb1d267");
		assertEquals(interest.getCategoryId(), "51d15ef0e9");
		assertEquals(interest.getListId(), "aaa6be6111");
		assertEquals(interest.getName(), "Arts and Music");
		assertEquals(interest.getDisplayOrder(), new Integer(1));
		// TODO: should return int
		assertEquals(interest.getSubscriberCount(), "2");
	}
	
	@Test
	public void testMailChimpList_Segment_saved() {
		JSONObject jsonObj = new JSONObject("{\"id\":67233,\"name\":\"Arts and Music\",\"member_count\":2,\"type\":\"saved\",\"created_at\":\"2019-06-06T21:50:52+00:00\",\"updated_at\":\"2019-06-06T21:50:52+00:00\",\"options\":{\"match\":\"any\",\"conditions\":[{\"condition_type\":\"Interests\",\"field\":\"interests-51d15ef0e9\",\"op\":\"interestcontains\",\"value\":[\"1ecfb1d267\"]}]},\"list_id\":\"aaa6be6111\",\"_links\":[{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/segments/67233\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/Response.json\"},{\"rel\":\"parent\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/segments\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Segments.json\"},{\"rel\":\"delete\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/segments/67233\",\"method\":\"DELETE\"},{\"rel\":\"update\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/segments/67233\",\"method\":\"PATCH\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/Response.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/PATCH.json\"},{\"rel\":\"members\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/segments/67233/members\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/Members/Response.json\"}]}");
		Segment segment = new Segment(null, jsonObj);
		assertEquals(segment.getId(), 67233);
		assertEquals(segment.getListId(), "aaa6be6111");
		assertEquals(segment.getMemberCount(), 2);
		assertEquals(segment.getName(), "Arts and Music");
		assertEquals(segment.getType(), SegmentType.SAVED);
		
		Options opts = segment.getOptions();
		assertEquals(opts.getMatch(), MatchType.ANY);
		assertEquals(opts.getConditions().size(), 1);
	}
	
	@Test
	public void testMailChimpList_Segment_static() {
		JSONObject jsonObj = new JSONObject("{\"id\":67669,\"name\":\"Literacy\",\"member_count\":5,\"type\":\"static\",\"created_at\":\"2019-06-14T17:48:10+00:00\",\"updated_at\":\"2019-06-14T17:48:10+00:00\",\"list_id\":\"aaa6be6111\",\"_links\":[{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/segments/67669\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/Response.json\"},{\"rel\":\"parent\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/segments\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Segments.json\"},{\"rel\":\"delete\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/segments/67669\",\"method\":\"DELETE\"},{\"rel\":\"update\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/segments/67669\",\"method\":\"PATCH\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/Response.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/PATCH.json\"},{\"rel\":\"members\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/segments/67669/members\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/Members/Response.json\"}]}");
		Segment segment = new Segment(null, jsonObj);
		assertEquals(segment.getId(), 67669);
		assertEquals(segment.getListId(), "aaa6be6111");
		assertEquals(segment.getMemberCount(), 5);
		assertEquals(segment.getName(), "Literacy");
		assertEquals(segment.getType(), SegmentType.STATIC);
		
		assertNull(segment.getOptions());
	}
	
	// TODO:
//	@Test
//	public void testMailChimpList_MergeField() {
//		JSONObject jsonObj = new JSONObject("");
//	}
	
	// TODO:
//	@Test
//	public void testMailChimpList_abuse-reports() {
//		JSONObject jsonObj = new JSONObject("");
//	}
	
	// TODO:
//	@Test
//	public void testMailChimpList_activity() {
//		JSONObject jsonObj = new JSONObject("");
//	}
	
	// TODO:
//	@Test
//	public void testMailChimpList_clients() {
//		JSONObject jsonObj = new JSONObject("");
//	}
	
	// TODO:
//	@Test
//	public void testMailChimpList_growth-history() {
//		JSONObject jsonObj = new JSONObject("");
//	}
	
	// TODO:
//	@Test
//	public void testMailChimpList_locations() {
//		JSONObject jsonObj = new JSONObject("");
//	}
	
	
	// TODO:
//	@Test
//	public void testMailChimpList_preview-segment() {
//		JSONObject jsonObj = new JSONObject("");
//	}
	
	
	// TODO:
//	@Test
//	public void testMailChimpList_signup-forms() {
//		JSONObject jsonObj = new JSONObject("");
//	}
	
	
	// TODO:
//	@Test
//	public void testMailChimpList_webhooks() {
//		JSONObject jsonObj = new JSONObject("");
//	}
	
}
