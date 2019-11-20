package com.github.alexanderwe.bananaj.model.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.json.JSONObject;
import org.junit.Test;

import com.github.alexanderwe.bananaj.model.list.interests.Interest;
import com.github.alexanderwe.bananaj.model.list.interests.InterestCategory;
import com.github.alexanderwe.bananaj.model.list.interests.InterestCategoryType;
import com.github.alexanderwe.bananaj.model.list.mergefield.MergeField;
import com.github.alexanderwe.bananaj.model.list.mergefield.MergeFieldType;
import com.github.alexanderwe.bananaj.model.list.segment.MatchType;
import com.github.alexanderwe.bananaj.model.list.segment.SegmentOptions;
import com.github.alexanderwe.bananaj.model.list.segment.Segment;
import com.github.alexanderwe.bananaj.model.list.segment.SegmentType;
import com.github.alexanderwe.bananaj.model.report.AbuseReport;

public class MailChimpListTest {

	@Test
	public void testMailChimpList() {
		JSONObject jsonObj = new JSONObject("{\"id\":\"abc0be64d2\",\"web_id\":282000,\"name\":\"Test Analytics\",\"contact\":{\"company\":\"ABC Corp\",\"address1\":\"1000 W Bay Rd Ste 500\",\"address2\":\"\",\"city\":\"Chandler\",\"state\":\"AZ\",\"zip\":\"85226-2496\",\"country\":\"US\",\"phone\":\"5553387000\"},\"permission_reminder\":\"You are receiving this email as a partner in Testing initiative.\",\"use_archive_bar\":true,\"campaign_defaults\":{\"from_name\":\"Analytics Robot\",\"from_email\":\"no.spam@gmail.com\",\"subject\":\"\",\"language\":\"en\"},\"notify_on_subscribe\":\"\",\"notify_on_unsubscribe\":\"\",\"date_created\":\"2019-06-06T21:19:33+00:00\",\"list_rating\":0,\"email_type_option\":false,\"subscribe_url_short\":\"http://eepurl.com/gub30A\",\"subscribe_url_long\":\"https://us3.list-manage.com/subscribe?u=c00f3ed44928e2d1d4f00000d&id=aaa1cc11a1\",\"beamer_address\":\"us3-4ca796b90c-7644bc000a@inbound.mailchimp.com\",\"visibility\":\"pub\",\"double_optin\":false,\"has_welcome\":false,\"marketing_permissions\":false,\"modules\":[],\"stats\":{\"member_count\":34,\"unsubscribe_count\":1,\"cleaned_count\":0,\"member_count_since_send\":1,\"unsubscribe_count_since_send\":1,\"cleaned_count_since_send\":0,\"campaign_count\":0,\"campaign_last_sent\":\"\",\"merge_field_count\":4,\"avg_sub_rate\":0,\"avg_unsub_rate\":0,\"target_sub_rate\":0,\"open_rate\":0,\"click_rate\":0,\"last_sub_date\":\"2019-06-13T22:52:25+00:00\",\"last_unsub_date\":\"2019-06-06T22:37:51+00:00\"},\"_links\":[{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Response.json\"},{\"rel\":\"parent\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists.json\"},{\"rel\":\"update\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1\",\"method\":\"PATCH\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Response.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/PATCH.json\"},{\"rel\":\"batch-sub-unsub-members\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1\",\"method\":\"POST\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/BatchPOST-Response.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/BatchPOST.json\"},{\"rel\":\"delete\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1\",\"method\":\"DELETE\"},{\"rel\":\"abuse-reports\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/abuse-reports\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Abuse/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Abuse.json\"},{\"rel\":\"activity\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/activity\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Activity/Response.json\"},{\"rel\":\"clients\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/clients\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Clients/Response.json\"},{\"rel\":\"growth-history\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/growth-history\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Growth/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Growth.json\"},{\"rel\":\"interest-categories\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/interest-categories\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/InterestCategories/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/InterestCategories.json\"},{\"rel\":\"members\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/members\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Members/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Members.json\"},{\"rel\":\"merge-fields\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/merge-fields\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/MergeFields/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/MergeFields.json\"},{\"rel\":\"segments\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/segments\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Segments.json\"},{\"rel\":\"webhooks\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/webhooks\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Webhooks/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Webhooks.json\"},{\"rel\":\"signup-forms\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/signup-forms\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/SignupForms/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/SignupForms.json\"},{\"rel\":\"locations\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa1cc11a1/locations\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Locations/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Locations.json\"}]}");
		MailChimpList list = new MailChimpList(null, jsonObj);
		assertEquals("abc0be64d2", list.getId());
		assertEquals(282000, list.getWebId());
		assertEquals("Test Analytics", list.getName());
		assertEquals("5553387000", list.getContact().getPhone());
	}

	@Test
	public void testMailChimpList_InterestCategory() {
		JSONObject jsonObj = new JSONObject("{\"list_id\":\"aaa6be6111\",\"id\":\"51d15ef0e9\",\"title\":\"Test Discipline\",\"display_order\":0,\"type\":\"checkboxes\",\"_links\":[{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/interest-categories/51d15ef0e9\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/InterestCategories/Response.json\"},{\"rel\":\"parent\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/interest-categories\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/InterestCategories/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/InterestCategories.json\"},{\"rel\":\"update\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/interest-categories/51d15ef0e9\",\"method\":\"PATCH\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/InterestCategories/Response.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/InterestCategories/PATCH.json\"},{\"rel\":\"delete\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/interest-categories/51d15ef0e9\",\"method\":\"DELETE\"},{\"rel\":\"interests\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/interest-categories/51d15ef0e9/interests\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Interests/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Interests.json\"}]}");
		InterestCategory cat = new InterestCategory(null, jsonObj);
		assertEquals("51d15ef0e9", cat.getId());
		assertEquals("aaa6be6111", cat.getListId());
		assertEquals("Test Discipline", cat.getTitle());
		assertEquals(new Integer(0), cat.getDisplayOrder());
		assertEquals(InterestCategoryType.CHECKBOXES, cat.getType());
	}
	
	@Test
	public void testMailChimpList_Interest() {
		JSONObject jsonObj = new JSONObject("{\"category_id\":\"51d15ef0e9\",\"list_id\":\"aaa6be6111\",\"id\":\"1ecfb1d267\",\"name\":\"Arts and Music\",\"subscriber_count\":\"2\",\"display_order\":1,\"_links\":[{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/interest-categories/51d15ef0e9/interests/1ecfb1d267\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Interests/Response.json\"},{\"rel\":\"parent\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/interest-categories/51d15ef0e9/interests\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Interests/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Interests.json\"},{\"rel\":\"update\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/interest-categories/51d15ef0e9/interests/1ecfb1d267\",\"method\":\"PATCH\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Interests/Response.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Interests/PATCH.json\"},{\"rel\":\"delete\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/interest-categories/51d15ef0e9/interests/1ecfb1d267\",\"method\":\"DELETE\"}]}");
		Interest interest = new Interest(null, jsonObj);
		assertEquals("1ecfb1d267", interest.getId());
		assertEquals("51d15ef0e9", interest.getCategoryId());
		assertEquals("aaa6be6111", interest.getListId());
		assertEquals("Arts and Music", interest.getName());
		assertEquals(new Integer(1), interest.getDisplayOrder());
		assertEquals(2, interest.getSubscriberCount());
	}
	
	@Test
	public void testMailChimpList_Segment_saved() {
		JSONObject jsonObj = new JSONObject("{\"id\":67233,\"name\":\"Arts and Music\",\"member_count\":2,\"type\":\"saved\",\"created_at\":\"2019-06-06T21:50:52+00:00\",\"updated_at\":\"2019-06-06T21:50:52+00:00\",\"options\":{\"match\":\"any\",\"conditions\":[{\"condition_type\":\"Interests\",\"field\":\"interests-51d15ef0e9\",\"op\":\"interestcontains\",\"value\":[\"1ecfb1d267\"]}]},\"list_id\":\"aaa6be6111\",\"_links\":[{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/segments/67233\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/Response.json\"},{\"rel\":\"parent\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/segments\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Segments.json\"},{\"rel\":\"delete\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/segments/67233\",\"method\":\"DELETE\"},{\"rel\":\"update\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/segments/67233\",\"method\":\"PATCH\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/Response.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/PATCH.json\"},{\"rel\":\"members\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/segments/67233/members\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/Members/Response.json\"}]}");
		Segment segment = new Segment(null, jsonObj);
		assertEquals(67233, segment.getId());
		assertEquals("aaa6be6111", segment.getListId());
		assertEquals(2, segment.getMemberCount());
		assertEquals("Arts and Music", segment.getName());
		assertEquals(SegmentType.SAVED, segment.getType());
		
		SegmentOptions opts = segment.getOptions();
		assertEquals(MatchType.ANY, opts.getMatch());
		assertEquals(1, opts.getConditions().size());
	}
	
	@Test
	public void testMailChimpList_Segment_static() {
		JSONObject jsonObj = new JSONObject("{\"id\":67669,\"name\":\"Literacy\",\"member_count\":5,\"type\":\"static\",\"created_at\":\"2019-06-14T17:48:10+00:00\",\"updated_at\":\"2019-06-14T17:48:10+00:00\",\"list_id\":\"aaa6be6111\",\"_links\":[{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/segments/67669\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/Response.json\"},{\"rel\":\"parent\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/segments\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Segments.json\"},{\"rel\":\"delete\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/segments/67669\",\"method\":\"DELETE\"},{\"rel\":\"update\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/segments/67669\",\"method\":\"PATCH\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/Response.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/PATCH.json\"},{\"rel\":\"members\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/aaa6be6111/segments/67669/members\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Segments/Members/Response.json\"}]}");
		Segment segment = new Segment(null, jsonObj);
		assertEquals(67669, segment.getId());
		assertEquals("aaa6be6111", segment.getListId());
		assertEquals(5, segment.getMemberCount());
		assertEquals("Literacy", segment.getName());
		assertEquals(SegmentType.STATIC, segment.getType());
		
		assertNull(segment.getOptions());
	}
	
	@Test
	public void testMailChimpList_MergeField() {
		JSONObject jsonObj = new JSONObject("{\"merge_id\":3,\"tag\":\"ADDRESS\",\"name\":\"Address\",\"type\":\"address\",\"required\":false,\"default_value\":\"\",\"public\":false,\"display_order\":4,\"options\":{\"default_country\":164},\"help_text\":\"\",\"list_id\":\"a70b3a068a\",\"_links\":[{\"rel\":\"self\",\"href\":\"https://us6.api.mailchimp.com/3.0/lists/a70b3a068a/merge-fields/3\",\"method\":\"GET\",\"targetSchema\":\"https://us6.api.mailchimp.com/schema/3.0/Definitions/Lists/MergeFields/Response.json\"},{\"rel\":\"parent\",\"href\":\"https://us6.api.mailchimp.com/3.0/lists/a70b3a068a/merge-fields\",\"method\":\"GET\",\"targetSchema\":\"https://us6.api.mailchimp.com/schema/3.0/Definitions/Lists/MergeFields/CollectionResponse.json\",\"schema\":\"https://us6.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/MergeFields.json\"},{\"rel\":\"update\",\"href\":\"https://us6.api.mailchimp.com/3.0/lists/a70b3a068a/merge-fields/3\",\"method\":\"PATCH\",\"targetSchema\":\"https://us6.api.mailchimp.com/schema/3.0/Definitions/Lists/MergeFields/Response.json\",\"schema\":\"https://us6.api.mailchimp.com/schema/3.0/Definitions/Lists/MergeFields/PATCH.json\"},{\"rel\":\"delete\",\"href\":\"https://us6.api.mailchimp.com/3.0/lists/a70b3a068a/merge-fields/3\",\"method\":\"DELETE\"}]}");
		MergeField field = new MergeField(null, jsonObj);
		assertEquals(new Integer(3), field.getId());
		assertEquals("ADDRESS", field.getTag());
		assertEquals("Address", field.getName());
		assertEquals(MergeFieldType.ADDRESS, field.getType());
		assertEquals(Boolean.FALSE, field.getRequired());
		assertEquals("", field.getDefaultValue());
		assertEquals(Boolean.FALSE, field.getIsPublic());
		assertEquals(new Integer(4), field.getDisplayOrder());
		assertNotNull("Missing expected options", field.getOptions());
		assertEquals(164, field.getOptions().getDefaultCountry());
		assertEquals("", field.getHelpText());
		assertEquals("a70b3a068a", field.getListId());
	}
	
	@Test
	public void testMailChimpList_abuse_report() {
		JSONObject jsonObj = new JSONObject("{\"id\":1486,\"campaign_id\": \"42694e9e57\",\"list_id\":\"a70b3a068a\",\"email_id\":\"1986e2ad5e507dd4cd5b91a6058837d4\",\"email_address\":\"mr.test@gmail.com\",\"merge_fields\":{\"FNAME\":\"John\",\"LNAME\":\"Smith\",\"ADDRESS\":\"123 Mocking Bird Ln\",\"PHONE\":\"555-1234\"},\"vip\":true,\"date\":\"2019-04-04T23:39:59+00:00\",\"_links\":[]}");
		AbuseReport abuse = new AbuseReport(jsonObj);
		assertEquals(1486, abuse.getId());
		assertEquals("42694e9e57", abuse.getCampaignId());
		assertEquals("a70b3a068a", abuse.getListId());
		assertEquals("1986e2ad5e507dd4cd5b91a6058837d4", abuse.getEmailId());
		assertEquals("mr.test@gmail.com", abuse.getEmailAddress());
		assertEquals(4, abuse.getMergeFields().size());
		assertEquals(true, abuse.isVip());
		assertEquals(ZonedDateTime.of(2019, 4, 4, 23, 39, 59, 0, ZoneId.of("+00:00")), abuse.getDate());
	}
	
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
	
	@Test
	public void testMailChimpList_growth_history() {
		JSONObject jsonObj = new JSONObject("{\"list_id\":\"abcb3d065c\",\"month\":\"2019-10\",\"existing\":0,\"imports\":0,\"optins\":0,\"subscribed\":3,\"unsubscribed\":0,\"reconfirm\":0,\"cleaned\":0,\"pending\":0,\"deleted\":0,\"transactional\":0,\"_links\":[{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/abcb3d065c/growth-history/2019-10\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Growth/Response.json\"},{\"rel\":\"parent\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists/abcb3d065c/growth-history\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Lists/Growth/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists/Growth.json\"}]}");
		GrowthHistory history = new GrowthHistory(jsonObj);
		assertEquals("abcb3d065c", history.getId());
		assertEquals("2019-10", history.getMonth());
		assertEquals(3, history.getSubscribed());
		assertEquals(0, history.getUnsubscribed());
	}
	
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
