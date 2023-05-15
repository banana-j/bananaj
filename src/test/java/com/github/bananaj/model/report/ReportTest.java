package com.github.bananaj.model.report;

import static org.junit.Assert.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.json.JSONObject;
import org.junit.Test;

import com.github.bananaj.model.report.AbuseReport;
import com.github.bananaj.model.report.AdviceReport;
import com.github.bananaj.model.report.DomainPerformance;
import com.github.bananaj.model.report.DomainStats;
import com.github.bananaj.model.report.EcommerceProductActivity;
import com.github.bananaj.model.report.OpenReport;
import com.github.bananaj.model.report.OpenReportMember;
import com.github.bananaj.model.report.Report;
import com.github.bananaj.utils.DateConverter;

public class ReportTest {

	@Test
	public void testReport() {
		JSONObject jsonObj = new JSONObject("{\"id\":\"024a680c00\",\"campaign_title\":\"TEST #15 2018\",\"type\":\"regular\",\"list_id\":\"0d12345876\",\"list_is_active\":true,\"list_name\":\"Test FAY\",\"subject_line\":\"Implementation for TEST on March 14, 2019\",\"preview_text\":\"\",\"emails_sent\":9,\"abuse_reports\":0,\"unsubscribed\":0,\"send_time\":\"2019-03-15T14:35:05+00:00\",\"bounces\":{\"hard_bounces\":1,\"soft_bounces\":0,\"syntax_errors\":0},\"forwards\":{\"forwards_count\":1,\"forwards_opens\":0},\"opens\":{\"opens_total\":23,\"unique_opens\":3,\"open_rate\":0.375,\"last_open\":\"2019-05-10T23:14:40+00:00\"},\"clicks\":{\"clicks_total\":0,\"unique_clicks\":0,\"unique_subscriber_clicks\":0,\"click_rate\":0,\"last_click\":\"\"},\"facebook_likes\":{\"recipient_likes\":0,\"unique_likes\":0,\"facebook_likes\":0},\"industry_stats\":{\"type\":\"Education and Training\",\"open_rate\":0.1786604351339173835100382348173297941684722900390625,\"click_rate\":0.025020219652535570509233053826392279006540775299072265625,\"bounce_rate\":0.00925411689963563134642132723683971562422811985015869140625,\"unopen_rate\":0.8120854479664469938171578178298659622669219970703125,\"unsub_rate\":0.00180306368394134879963586204354442088515497744083404541015625,\"abuse_rate\":0.00014923690314063057875924867001771190189174376428127288818359375},\"list_stats\":{\"sub_rate\":0,\"unsub_rate\":0,\"open_rate\":40.44943820224718677991404547356069087982177734375,\"click_rate\":2.272727272727272929131459022755734622478485107421875},\"timeseries\":[{\"timestamp\":\"2019-03-15T14:00:00+00:00\",\"emails_sent\":9,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T15:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":1,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T16:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T17:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T18:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T19:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T20:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T21:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T22:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T23:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T00:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T01:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T02:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T03:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T04:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T05:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T06:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T07:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T08:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T09:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T10:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T11:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T12:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T13:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0}],\"ecommerce\":{\"total_orders\":0,\"total_spent\":0,\"total_revenue\":0,\"currency_code\":\"USD\"},\"delivery_status\":{\"enabled\":false},\"_links\":[]}");
		Report report = new Report(null,jsonObj);
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
		JSONObject jsonObj = new JSONObject("{\"id\":\"024a680c00\",\"campaign_title\":\"TEST #15 2018\",\"type\":\"regular\",\"list_id\":\"0d12345876\",\"list_is_active\":true,\"list_name\":\"Test FAY\",\"subject_line\":\"Implementation for TEST on March 14, 2019\",\"preview_text\":\"\",\"emails_sent\":9,\"abuse_reports\":0,\"unsubscribed\":0,\"send_time\":\"2019-03-15T14:35:05+00:00\",\"bounces\":{\"hard_bounces\":1,\"soft_bounces\":0,\"syntax_errors\":0},\"forwards\":{\"forwards_count\":1,\"forwards_opens\":0},\"opens\":{\"opens_total\":23,\"unique_opens\":3,\"open_rate\":0.375,\"last_open\":\"2019-05-10T23:14:40+00:00\"},\"clicks\":{\"clicks_total\":0,\"unique_clicks\":0,\"unique_subscriber_clicks\":0,\"click_rate\":0,\"last_click\":\"\"},\"facebook_likes\":{\"recipient_likes\":0,\"unique_likes\":0,\"facebook_likes\":0},\"list_stats\":{\"sub_rate\":0,\"unsub_rate\":0,\"open_rate\":40.44943820224718677991404547356069087982177734375,\"click_rate\":2.272727272727272929131459022755734622478485107421875},\"timeseries\":[{\"timestamp\":\"2019-03-15T14:00:00+00:00\",\"emails_sent\":9,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T15:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":1,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T16:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T17:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T18:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T19:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T20:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T21:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T22:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-15T23:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T00:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T01:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T02:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T03:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T04:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T05:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T06:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T07:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T08:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T09:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T10:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T11:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T12:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0},{\"timestamp\":\"2019-03-16T13:00:00+00:00\",\"emails_sent\":0,\"unique_opens\":0,\"recipients_clicks\":0}],\"ecommerce\":{\"total_orders\":0,\"total_spent\":0,\"total_revenue\":0,\"currency_code\":\"USD\"},\"delivery_status\":{\"enabled\":false},\"_links\":[]}");
		Report report = new Report(null,jsonObj);
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
		OpenReport rpt = new OpenReport(null, null);
		rpt.parseEntities(jsonObj);
		assertEquals("f32bbb4333", rpt.getCampaignId());
		assertEquals(new Integer(5), rpt.getTotalOpens());
		assertEquals(new Integer(4), rpt.getTotalItems());
		assertTrue("Expected OpenReportMembers to have entries", rpt.getMembers().iterator().hasNext());
	}
	
	@Test
	public void testReport_OpenReport_Member() {
		JSONObject jsonObj = new JSONObject("{\"campaign_id\":\"f32bbb4333\",\"list_id\":\"dbbcce6ad3\",\"list_is_active\":true,\"contact_status\":\"subscribed\",\"email_id\":\"53b9e632ccb57cc123f2d2d04f9440aa\",\"email_address\":\"jeanne.tester@gmail.com\",\"merge_fields\":{\"FNAME\":\"Jeanne\",\"LNAME\":\"Tester\",\"ADDRESS\":\"1234\",\"PHONE\":\"555\"},\"vip\":false,\"opens_count\":1,\"opens\":[{\"timestamp\":\"2023-04-03T14:51:14+00:00\"}],\"_links\":[]}");
		OpenReportMember rpt = new OpenReportMember(jsonObj);
		assertEquals("f32bbb4333", rpt.getCampaignId());
		assertEquals("dbbcce6ad3", rpt.getListId());
		assertEquals("53b9e632ccb57cc123f2d2d04f9440aa", rpt.getEmailId());
		assertEquals("jeanne.tester@gmail.com", rpt.getEmailAddress());
		assertEquals(1, rpt.getOpens().size());
		assertEquals(DateConverter.fromISO8601("2023-04-03T14:51:14+00:00"), rpt.getOpens().get(0));
		assertEquals(4, rpt.getMergeFields().size());
		assertEquals("Jeanne", rpt.getMergeFields().get("FNAME"));
		assertEquals(1, rpt.getOpensCount());
		assertEquals("subscribed", rpt.getContactStatus());
		assertEquals(true, rpt.isListIsActive());
		assertEquals(false, rpt.isVip());
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

	@Test
	public void testReport_ReportSentTo() {
		JSONObject jsonObj = new JSONObject("{\"email_id\":\"53b9e632ccb57cc123f2d2d04f9440aa\",\"email_address\":\"jeanne.tester@gmail.com\",\"merge_fields\":{\"FNAME\":\"Jeanne\",\"LNAME\":\"Tester\",\"ADDRESS\":\"1234\",\"PHONE\":\"555\"},\"vip\":false,\"status\":\"sent\",\"open_count\":1,\"last_open\":\"2023-04-03T14:51:14+00:00\",\"absplit_group\":\"\",\"gmt_offset\":0,\"campaign_id\":\"f32bbb4333\",\"list_id\":\"dbbcce6ad3\",\"list_is_active\":true,\"_links\":[]}");
		ReportSentTo rpt = new ReportSentTo(jsonObj);
		assertEquals("f32bbb4333", rpt.getCampaignId());
		assertEquals("dbbcce6ad3", rpt.getListId());
		assertEquals("53b9e632ccb57cc123f2d2d04f9440aa", rpt.getEmailId());
		assertEquals("jeanne.tester@gmail.com", rpt.getEmailAddress());
		assertEquals("", rpt.getAbSplitGroup());
		assertEquals(0, rpt.getGmtOffset());
		assertEquals(DateConverter.fromISO8601("2023-04-03T14:51:14+00:00"), rpt.getLastOpen());
		assertEquals(4, rpt.getMergeFields().size());
		assertEquals("Jeanne", rpt.getMergeFields().get("FNAME"));
		assertEquals(1, rpt.getOpenCount());
		assertEquals("sent", rpt.getStatus());
		assertEquals(true, rpt.isListIsActive());
		assertEquals(false, rpt.isVip());
	}

	@Test
	public void testReport_ClickReport() {
		JSONObject jsonObj = new JSONObject("{\"id\":\"8885558880\",\"url\":\"https://mcusercontent.com/0123456789abcdef000000000/images/ffffffff-0000-a9b2-1070-0c4547aa215c.png\",\"total_clicks\":1,\"click_percentage\":0.058823529411764705,\"unique_clicks\":1,\"unique_click_percentage\":0.058823529411764705,\"last_click\":\"2023-05-11T19:33:10+00:00\",\"campaign_id\":\"f32bbb4333\",\"_links\":[]}");
		ClickReport rpt = new ClickReport(jsonObj);

		assertEquals("f32bbb4333", rpt.getCampaignId());
		assertEquals("8885558880", rpt.getId());
		assertEquals("https://mcusercontent.com/0123456789abcdef000000000/images/ffffffff-0000-a9b2-1070-0c4547aa215c.png", rpt.getUrl());
		assertEquals(1, rpt.getTotalClicks().intValue());
		assertEquals(1, rpt.getUniqueClicks().intValue());
		assertEquals(DateConverter.fromISO8601("2023-05-11T19:33:10+00:00"), rpt.getLastClick());
	}

	@Test
	public void testReport_ClickReportMember() {
		JSONObject jsonObj = new JSONObject("{\"email_id\":\"53b9e632ccb57cc123f2d2d04f9440aa\",\"email_address\":\"jeanne.tester@gmail.com\",\"merge_fields\":{\"FNAME\":\"Jeanne\",\"LNAME\":\"Tester\",\"ADDRESS\":\"1234\",\"PHONE\":\"555\"},\"vip\":false,\"clicks\":1,\"campaign_id\":\"f32bbb4333\",\"url_id\":\"8885558880\",\"list_id\":\"dbbcce6ad3\",\"list_is_active\":true,\"contact_status\":\"subscribed\",\"_links\":[]}");
		ClickReportMember rpt = new ClickReportMember(jsonObj);
		assertEquals("8885558880", rpt.getUrlId());
		assertEquals("f32bbb4333", rpt.getCampaignId());
		assertEquals("dbbcce6ad3", rpt.getListId());
		assertEquals("53b9e632ccb57cc123f2d2d04f9440aa", rpt.getEmailId());
		assertEquals("jeanne.tester@gmail.com", rpt.getEmailAddress());
		assertEquals(4, rpt.getMergeFields().size());
		assertEquals("Jeanne", rpt.getMergeFields().get("FNAME"));
		assertEquals(1, rpt.getClicks());
		assertEquals("subscribed", rpt.getContactStatus());
		assertEquals(true, rpt.isListIsActive());
		assertEquals(false, rpt.isVip());
	}

	@Test
	public void testReport_ReportLocation() {
//		JSONObject jsonObj = new JSONObject("");
//		ReportLocation rpt = new ReportLocation(jsonObj);
//		assertEquals("8885558880", rpt.getCountryCode());
//		assertEquals("f32bbb4333", rpt.getRegion());
//		assertEquals("dbbcce6ad3", rpt.getRegionName());
//		assertEquals(4, rpt.getOpens());
	}

}
