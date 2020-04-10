package com.github.bananaj.model.automation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.json.JSONObject;
import org.junit.Test;

import com.github.bananaj.model.automation.Automation;
import com.github.bananaj.model.automation.AutomationStatus;
import com.github.bananaj.model.automation.emails.AutomationEmail;
import com.github.bananaj.model.automation.emails.AutomationSubscriber;
import com.github.bananaj.model.automation.emails.AutomationSubscriberQueue;
import com.github.bananaj.model.list.segment.MatchType;

public class AutomationTest {

	@Test
	public void testAutomation() {
		JSONObject jsonObj = new JSONObject("{\"id\":\"2f522d1363\",\"create_time\":\"2019-06-06T18:02:16+00:00\",\"start_time\":\"\",\"status\":\"save\",\"emails_sent\":0,\"recipients\":{\"list_id\":\"a70b3a068a\",\"list_is_active\":true,\"list_name\":\"Analytics Gadgets\"},\"settings\":{\"title\":\"Email your tagged contacts\",\"from_name\":\"Analytics Robot\",\"reply_to\":\"tester@gmail.com\",\"use_conversation\":false,\"to_name\":\"\",\"authenticate\":true,\"auto_footer\":false,\"inline_css\":false},\"tracking\":{\"opens\":true,\"html_clicks\":true,\"text_clicks\":false,\"goal_tracking\":false,\"ecomm360\":false,\"google_analytics\":\"AUTOMATION__0\",\"clicktale\":\"\"},\"trigger_settings\":{\"workflow_type\":\"tagAdd\",\"workflow_title\":\"Tagged Contact\",\"runtime\":{\"days\":[\"sunday\",\"monday\",\"tuesday\",\"wednesday\",\"thursday\",\"friday\",\"saturday\"],\"hours\":{\"type\":\"automation\"}},\"workflow_emails_count\":1},\"_links\":[{\"rel\":\"parent\",\"href\":\"https://us3.api.mailchimp.com/3.0/automations\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Automations/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Automations.json\"},{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/automations/2f522d1363\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Automations/Response.json\"},{\"rel\":\"start-all-emails\",\"href\":\"https://us3.api.mailchimp.com/3.0/automations/2f522d1363/actions/start-all-emails\",\"method\":\"POST\"},{\"rel\":\"pause-all-emails\",\"href\":\"https://us3.api.mailchimp.com/3.0/automations/2f522d1363/actions/pause-all-emails\",\"method\":\"POST\"},{\"rel\":\"emails\",\"href\":\"https://us3.api.mailchimp.com/3.0/automations/2f522d1363/emails\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Automations/Emails/CollectionResponse.json\"},{\"rel\":\"removed-subscribers\",\"href\":\"https://us3.api.mailchimp.com/3.0/automations/2f522d1363/removed-subscribers\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Automations/RemovedSubscribers/CollectionResponse.json\"}]}");
		Automation automation = new Automation(null, jsonObj);
		assertEquals("2f522d1363", automation.getId());
		assertNull("Not started", automation.getStartTime());
		assertEquals(0, automation.getEmailsSent());
		assertEquals(AutomationStatus.SAVE, automation.getStatus());
		assertEquals("a70b3a068a", automation.getRecipients().getListId());
		assertEquals("Analytics Robot", automation.getSettings().getFromName());
		//automation.toString();
	}

	@Test
	public void testAutomationEmail() {
		JSONObject jsonObj = new JSONObject("{\"id\":\"3fbba32116\",\"web_id\":1123680,\"workflow_id\":\"2f5bbd136f\",\"position\":1,\"delay\":{\"type\":\"now\",\"action\":\"tag_add\",\"action_description\":\"a tag is added to the contact. Tag: <b>WelcomePacket</b>\",\"full_description\":\"Immediately after a tag is added to the contact. Tag: <b>WelcomePacket</b>\"},\"create_time\":\"2019-06-06T18:02:16+00:00\",\"start_time\":\"\",\"archive_url\":\"http://eepurl.com/gt12345\",\"status\":\"save\",\"emails_sent\":0,\"send_time\":\"\",\"content_type\":\"template\",\"needs_block_refresh\":false,\"has_logo_merge_tag\":false,\"recipients\":{\"list_id\":\"a70b3a068a\",\"list_is_active\":true,\"list_name\":\"Analytics Gadgets\",\"segment_text\":\"<p class=\\\"!margin--lv0 display--inline\\\">Contacts that match <strong>any</strong> of the following conditions:</p><ol id=\\\"conditions\\\" class=\\\"small-meta text-transform--none\\\"><li class=\\\"margin--lv1 !margin-left-right--lv0\\\">Date Added is within <strong>the last 1 days</strong></li></ol><span>For a total of <strong>0</strong> emails sent.</span>\",\"recipient_count\":0,\"segment_opts\":{\"match\":\"any\",\"conditions\":[{\"condition_type\":\"Date\",\"field\":\"timestamp_opt\",\"op\":\"date_within\",\"value\":\"1\"}]}},\"settings\":{\"subject_line\":\"Welcome to the Portal!\",\"title\":\"Tagged a contact\",\"from_name\":\"Analytics Robot\",\"reply_to\":\"my.tester@gmail.com\",\"authenticate\":true,\"auto_footer\":false,\"inline_css\":false,\"auto_tweet\":false,\"fb_comments\":true,\"template_id\":116,\"drag_and_drop\":true},\"tracking\":{\"opens\":true,\"html_clicks\":true,\"text_clicks\":false,\"goal_tracking\":false,\"ecomm360\":false,\"google_analytics\":\"AUTOMATION__1\",\"clicktale\":\"\"},\"trigger_settings\":{\"workflow_type\":\"\",\"workflow_title\":\"\",\"runtime\":{\"days\":[\"sunday\",\"monday\",\"tuesday\",\"wednesday\",\"thursday\",\"friday\",\"saturday\"],\"hours\":{\"type\":\"automation\"}},\"workflow_emails_count\":0},\"_links\":[{\"rel\":\"parent\",\"href\":\"https://us3.api.mailchimp.com/3.0/automations/2f5bbd136f/emails\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Automations/Emails/CollectionResponse.json\"},{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/automations/2f5bbd136f/emails/3fbba32116\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Automations/Emails/ResponseResponse.json\"},{\"rel\":\"start\",\"href\":\"https://us3.api.mailchimp.com/3.0/automations/2f5bbd136f/emails/3fbba32116/actions/start\",\"method\":\"POST\"},{\"rel\":\"pause\",\"href\":\"https://us3.api.mailchimp.com/3.0/automations/2f5bbd136f/emails/3fbba32116/actions/pause\",\"method\":\"POST\"},{\"rel\":\"queue\",\"href\":\"https://us3.api.mailchimp.com/3.0/automations/2f5bbd136f/emails/3fbba32116/queue\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Automations/Emails/Queue/CollectionResponse.json\"}]}");
		AutomationEmail autoObj = new AutomationEmail(null, jsonObj);
		assertEquals("3fbba32116", autoObj.getId());
		assertNull("Not started", autoObj.getStartTime());
		assertEquals(0, autoObj.getEmailsSent());
		assertEquals(AutomationStatus.SAVE, autoObj.getStatus());
		assertEquals("a70b3a068a", autoObj.getRecipients().getListId());
		assertEquals(MatchType.ANY, autoObj.getRecipients().getSegmentOpts().getMatch());
		assertEquals("Analytics Robot", autoObj.getSettings().getFromName());
		autoObj.toString();
	}

//	@Test
//	public void testAutomationSubscriberQueue() {
//		JSONObject jsonObj = new JSONObject("");
//		AutomationSubscriberQueue autoObj = new AutomationSubscriberQueue(null, jsonObj);
//		assertEquals("2f522d1363", autoObj.getId());
//	}

//	@Test
//	public void testAutomationSubscriber() {
//		JSONObject jsonObj = new JSONObject("");
//		AutomationSubscriber autoObj = new AutomationSubscriber(jsonObj);
//		assertEquals("2f522d1363", autoObj.getId());
//	}

}
