package com.github.alexanderwe.bananaj.model.report;

import static org.junit.Assert.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.json.JSONObject;
import org.junit.Test;

public class ReportTest {

	@Test
	public void testReport() {
		JSONObject jsonObj = new JSONObject("{\"id\":\"024a680c00\",\"campaign_title\":\"TEST #15 2018\",\"type\":\"regular\",\"list_id\":\"0d12345876\",\"list_is_active\":true,\"list_name\":\"Test FAY\",\"subject_line\":\"Implementation for TEST on March 14, 2019\",\"preview_text\":\"\",\"emails_sent\":9,\"abuse_reports\":0,\"unsubscribed\":0,\"send_time\":\"2019-03-15T14:35:05+00:00\",\"bounces\":{\"hard_bounces\":1,\"soft_bounces\":0,\"syntax_errors\":0},\"forwards\":{\"forwards_count\":1,\"forwards_opens\":0},\"opens\":{\"opens_total\":23,\"unique_opens\":3,\"open_rate\":0.375,\"last_open\":\"2019-05-10T23:14:40+00:00\"},\"clicks\":{\"clicks_total\":0,\"unique_clicks\":0,\"unique_subscriber_clicks\":0,\"click_rate\":0,\"last_click\":\"\"},\"facebook_likes\":{\"recipient_likes\":0,\"unique_likes\":0,\"facebook_likes\":0},\"industry_stats\":{\"type\":\"Education and Training\",\"open_rate\":0.1786604351339173835100382348173297941684722900390625,\"click_rate\":0.025020219652535570509233053826392279006540775299072265625,\"bounce_rate\":0.00925411689963563134642132723683971562422811985015869140625,\"unopen_rate\":0.8120854479664469938171578178298659622669219970703125,\"unsub_rate\":0.00180306368394134879963586204354442088515497744083404541015625,\"abuse_rate\":0.00014923690314063057875924867001771190189174376428127288818359375},\"list_stats\":{\"sub_rate\":0,\"unsub_rate\":0,\"open_rate\":40.44943820224718677991404547356069087982177734375,\"click_rate\":2.272727272727272929131459022755734622478485107421875},\"timeseries\":[{\"timestamp\":\"2019-03-15T14:00:00+00:00\",\"emails_sent\":9,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T15:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":1,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T16:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T17:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T18:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T19:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T20:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T21:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T22:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T23:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T00:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T01:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T02:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T03:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T04:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T05:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T06:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T07:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T08:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T09:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T10:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T11:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T12:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T13:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0}],\"ecommerce\":{\"total_orders\":0,\"total_spent\":0,\"total_revenue\":0,\"currency_code\":\"USD\"},\"delivery_status\":{\"enabled\":false},\"_links\":[{\"rel\":\"parent\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Reports.json\"},{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/Response.json\"},{\"rel\":\"campaign\",\"href\":\"https://us3.api.mailchimp.com/3.0/campaigns/123a456b78\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Campaigns/Response.json\"},{\"rel\":\"sub-reports\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/sub-reports\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/Sub/Response.json\"},{\"rel\":\"abuse-reports\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/abuse-reports\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/Abuse/CollectionResponse.json\"},{\"rel\":\"advice\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/advice\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/Advice/Response.json\"},{\"rel\":\"open-details\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/open-details\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/OpenDetails/CollectionResponse.json\"},{\"rel\":\"click-details\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/click-details\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/ClickDetails/CollectionResponse.json\"},{\"rel\":\"domain-performance\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/domain-performance\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/DomainPerformance/Response.json\"},{\"rel\":\"eepurl\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/eepurl\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/Eepurl/CollectionResponse.json\"},{\"rel\":\"email-activity\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/email-activity\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/EmailActivity/CollectionResponse.json\"},{\"rel\":\"locations\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/locations\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/Locations/Response.json\"},{\"rel\":\"sent-to\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/sent-to\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/SentTo/CollectionResponse.json\"},{\"rel\":\"unsubscribed\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/unsubscribed\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/Unsubs/CollectionResponse.json\"}]}");
		Report report = new Report(jsonObj);
		assertEquals("024a680c00", report.getId());
		assertEquals("TEST #15 2018", report.getCampaignTitle());
		assertEquals("0d12345876", report.getListId());
		assertEquals(0, report.getAbuseReport());
		assertEquals("Implementation for TEST on March 14, 2019", report.getSubjectLine());
		assertEquals(9, report.getEmailsSent());
		assertEquals(1, report.getForwards().getCount());
		assertEquals(23, report.getOpens().getOpensTotal());
		assertNotNull("Expeced IndustryStats found null", report.getIndustryStats());
		report.toString();
	}

	@Test
	public void testReport_minimal() {
		// #42 JSONObject["industry_stats"] not found
		// industry_stats may not appear in the results when stats are not configured in the account
		JSONObject jsonObj = new JSONObject("{\"id\":\"024a680c00\",\"campaign_title\":\"TEST #15 2018\",\"type\":\"regular\",\"list_id\":\"0d12345876\",\"list_is_active\":true,\"list_name\":\"Test FAY\",\"subject_line\":\"Implementation for TEST on March 14, 2019\",\"preview_text\":\"\",\"emails_sent\":9,\"abuse_reports\":0,\"unsubscribed\":0,\"send_time\":\"2019-03-15T14:35:05+00:00\",\"bounces\":{\"hard_bounces\":1,\"soft_bounces\":0,\"syntax_errors\":0},\"forwards\":{\"forwards_count\":1,\"forwards_opens\":0},\"opens\":{\"opens_total\":23,\"unique_opens\":3,\"open_rate\":0.375,\"last_open\":\"2019-05-10T23:14:40+00:00\"},\"clicks\":{\"clicks_total\":0,\"unique_clicks\":0,\"unique_subscriber_clicks\":0,\"click_rate\":0,\"last_click\":\"\"},\"facebook_likes\":{\"recipient_likes\":0,\"unique_likes\":0,\"facebook_likes\":0},\"list_stats\":{\"sub_rate\":0,\"unsub_rate\":0,\"open_rate\":40.44943820224718677991404547356069087982177734375,\"click_rate\":2.272727272727272929131459022755734622478485107421875},\"timeseries\":[{\"timestamp\":\"2019-03-15T14:00:00+00:00\",\"emails_sent\":9,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T15:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":1,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T16:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T17:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T18:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T19:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T20:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T21:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T22:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T23:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T00:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T01:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T02:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T03:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T04:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T05:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T06:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T07:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T08:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T09:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T10:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T11:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T12:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T13:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0}],\"ecommerce\":{\"total_orders\":0,\"total_spent\":0,\"total_revenue\":0,\"currency_code\":\"USD\"},\"delivery_status\":{\"enabled\":false},\"_links\":[{\"rel\":\"parent\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Reports.json\"},{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/Response.json\"},{\"rel\":\"campaign\",\"href\":\"https://us3.api.mailchimp.com/3.0/campaigns/123a456b78\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Campaigns/Response.json\"},{\"rel\":\"sub-reports\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/sub-reports\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/Sub/Response.json\"},{\"rel\":\"abuse-reports\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/abuse-reports\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/Abuse/CollectionResponse.json\"},{\"rel\":\"advice\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/advice\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/Advice/Response.json\"},{\"rel\":\"open-details\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/open-details\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/OpenDetails/CollectionResponse.json\"},{\"rel\":\"click-details\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/click-details\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/ClickDetails/CollectionResponse.json\"},{\"rel\":\"domain-performance\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/domain-performance\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/DomainPerformance/Response.json\"},{\"rel\":\"eepurl\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/eepurl\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/Eepurl/CollectionResponse.json\"},{\"rel\":\"email-activity\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/email-activity\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/EmailActivity/CollectionResponse.json\"},{\"rel\":\"locations\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/locations\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/Locations/Response.json\"},{\"rel\":\"sent-to\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/sent-to\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/SentTo/CollectionResponse.json\"},{\"rel\":\"unsubscribed\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports/123a456b78/unsubscribed\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Reports/Unsubs/CollectionResponse.json\"}]}");
		Report report = new Report(jsonObj);
		assertEquals("024a680c00", report.getId());
		assertEquals("TEST #15 2018", report.getCampaignTitle());
		assertEquals("0d12345876", report.getListId());
		assertEquals(0, report.getAbuseReport());
		assertEquals("Implementation for TEST on March 14, 2019", report.getSubjectLine());
		assertEquals(9, report.getEmailsSent());
		assertEquals(1, report.getForwards().getCount());
		assertEquals(23, report.getOpens().getOpensTotal());
		assertNull("Expeced null for IndustryStats", report.getIndustryStats());
		report.toString();
	}

	@Test
	public void testReport_abuse_report() {
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
	
	@Test
	public void testReport_OpenReport() {
		JSONObject jsonObj = new JSONObject("{\"members\":[{\"campaign_id\":\"f32bbb4333\",\"list_id\":\"dbbcce6ad3\",\"list_is_active\":true,\"contact_status\":\"subscribed\",\"email_id\":\"53b9e632ccb57cc123f2d2d04f9440aa\",\"email_address\":\"jeanne.tester@gmail.com\",\"merge_fields\":{\"FNAME\":\"Jeanne\",\"LNAME\":\"Tester\",\"ADDRESS\":\"\",\"PHONE\":\"\"},\"vip\":false,\"opens_count\":1,\"opens\":[{\"timestamp\":\"2019-11-05T01:48:56+00:00\"}],\"_links\":[]},{\"campaign_id\":\"f32bbb4333\",\"list_id\":\"dbbcce6ad3\",\"list_is_active\":true,\"contact_status\":\"subscribed\",\"email_id\":\"ef9925ae9f7f581443530123d62e53bb\",\"email_address\":\"mark.tester@gmail.com\",\"merge_fields\":{\"FNAME\":\"Mark\",\"LNAME\":\"Tester\",\"ADDRESS\":\"\",\"PHONE\":\"\"},\"vip\":false,\"opens_count\":2,\"opens\":[{\"timestamp\":\"2019-11-04T23:57:40+00:00\"},{\"timestamp\":\"2019-11-05T15:29:37+00:00\"}],\"_links\":[]},{\"campaign_id\":\"f32bbb4333\",\"list_id\":\"dbbcce6ad3\",\"list_is_active\":true,\"contact_status\":\"subscribed\",\"email_id\":\"c82262d08d2b3a2346a2da1af1705bcc\",\"email_address\":\"mark.tester@gmail.com\",\"merge_fields\":{\"FNAME\":\"Mark\",\"LNAME\":\"Tester\",\"ADDRESS\":\"\",\"PHONE\":\"\"},\"vip\":false,\"opens_count\":1,\"opens\":[{\"timestamp\":\"2019-11-05T19:54:07+00:00\"}],\"_links\":[]},{\"campaign_id\":\"f32bbb4333\",\"list_id\":\"dbbcce6ad3\",\"list_is_active\":true,\"contact_status\":\"subscribed\",\"email_id\":\"5b68d1a1345a3f7523e433e482b0e2dd\",\"email_address\":\"marianne.tester@gmail.com\",\"merge_fields\":{\"FNAME\":\"Marianne\",\"LNAME\":\"Tester\",\"ADDRESS\":{\"addr1\":\"1313 Mocking\",\"addr2\":\"Suite 200\",\"city\":\"Phoenix\",\"state\":\"AZ\",\"zip\":\"85001\",\"country\":\"US\"},\"PHONE\":\"\"},\"vip\":false,\"opens_count\":1,\"opens\":[{\"timestamp\":\"2019-11-04T21:40:11+00:00\"}],\"_links\":[]}],\"campaign_id\":\"f32bbb4333\",\"total_opens\":5,\"total_items\":4,\"_links\":[]}");
		OpenReport rpt = new OpenReport(jsonObj);
		assertEquals("f32bbb4333", rpt.getCampaignId());
		assertEquals(5, rpt.getTotalOpens());
		assertEquals(4, rpt.getTotalItems());
		assertEquals(4, rpt.getMembers().size());
	}
	
	@Test
	public void testReport_OpenReport_Member() {
		JSONObject jsonObj = new JSONObject("{\"campaign_id\":\"f32bbb4333\",\"list_id\":\"dbbcce6ad3\",\"list_is_active\":true,\"contact_status\":\"subscribed\",\"email_id\":\"53b9e632ccb57cc123f2d2d04f9440aa\",\"email_address\":\"jeanne.tester@gmail.com\",\"merge_fields\":{\"FNAME\":\"Jeanne\",\"LNAME\":\"Tester\",\"ADDRESS\":\"\",\"PHONE\":\"\"},\"vip\":false,\"opens_count\":1,\"opens\":[{\"timestamp\":\"2019-11-05T01:48:56+00:00\"}],\"_links\":[]}");
		OpenReportMember rpt = new OpenReportMember(jsonObj);
		assertEquals("f32bbb4333", rpt.getCampaignId());
		assertEquals("dbbcce6ad3", rpt.getListId());
		assertEquals("53b9e632ccb57cc123f2d2d04f9440aa", rpt.getEmailId());
		assertEquals("jeanne.tester@gmail.com", rpt.getEmailAddress());
		assertEquals(1, rpt.getOpensCount());
		assertEquals(4, rpt.getMergeFields().size());
		assertEquals(1, rpt.getOpens().size());
	}
	
	@Test
	public void testReport_advice_report() {
		JSONObject jsonObj = new JSONObject("{\"type\":\"advice-neutralstat\",\"message\":\"Your open rate was <strong>32%</strong> higher than your industry average, but <strong>-43.3%</strong> lower than this list's average.\"}");
		AdviceReport advice = new AdviceReport(jsonObj);
		assertEquals("advice-neutralstat", advice.getType());
		assertEquals("Your open rate was <strong>32%</strong> higher than your industry average, but <strong>-43.3%</strong> lower than this list's average.", advice.getMessage());
	}

	@Test
	public void testReport_DomainPerformance() {
		JSONObject jsonObj = new JSONObject("{\"domains\":[{\"domain\":\"gmail.com\",\"emails_sent\":1,\"bounces\":0,\"opens\":0,\"clicks\":0,\"unsubs\":0,\"delivered\":1,\"emails_pct\":0.5,\"bounces_pct\":0,\"opens_pct\":0,\"clicks_pct\":0,\"unsubs_pct\":0},{\"domain\":\"q.com\",\"emails_sent\":1,\"bounces\":0,\"opens\":1,\"clicks\":1,\"unsubs\":0,\"delivered\":1,\"emails_pct\":0.5,\"bounces_pct\":0,\"opens_pct\":1,\"clicks_pct\":1,\"unsubs_pct\":0}],\"total_sent\":2,\"campaign_id\":\"1d90377b31\",\"total_items\":2,\"_links\":[]}");
		DomainPerformance domains = new DomainPerformance(jsonObj);
		assertEquals("1d90377b31", domains.getCampaignId());
		assertEquals(2, domains.getTotalItems(), 2);
		assertEquals(2, domains.getTotalSent(), 2);
		assertEquals(2, domains.getDomains().size());
		DomainStats ds = domains.getDomains().get(1);
		assertEquals("q.com",ds.getDomain());
		assertEquals(1,ds.getEmailsSent());
		assertEquals(0,ds.getBounces());
		assertEquals(1,ds.getOpens());
		assertEquals(1,ds.getClicks());
		assertEquals(0,ds.getUnsubs());
		assertEquals(1,ds.getDelivered());
	}

	@Test
	public void testReport_EcommerceProductActivity() {
		JSONObject jsonObj = new JSONObject("{\"title\":\"Snacks\",\"sku\":\"030001\",\"image_url\":\"http://domain.com/image/030001.gif\",\"total_revenue\":10.29,\"total_purchased\":25.30,\"currency_code\":\"usd\",\"recommendation_total\":0,\"recommendation_purchased\":0,\"_links\":[]}");
		EcommerceProductActivity prdActivity = new EcommerceProductActivity(jsonObj);
		assertEquals("Snacks", prdActivity.getTitle());
		assertEquals("030001", prdActivity.getSku());
		assertEquals("http://domain.com/image/030001.gif", prdActivity.getImageUrl());
	}

}
