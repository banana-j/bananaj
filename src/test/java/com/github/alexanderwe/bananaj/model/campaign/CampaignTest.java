package com.github.alexanderwe.bananaj.model.campaign;

import static org.junit.Assert.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import com.github.alexanderwe.bananaj.model.list.segment.ConditionType;
import com.github.alexanderwe.bananaj.model.list.segment.MatchType;

public class CampaignTest {

	@Test
	public void testCampaign() throws Exception {
		JSONObject jsonObj = new JSONObject("{\"id\":\"012abcde01\",\"web_id\":1767757,\"type\":\"regular\",\"create_time\":\"2019-03-15T14:34:59+00:00\",\"archive_url\":\"http://eepurl.com/gkRad1\",\"long_archive_url\":\"https://us3.campaign-archive.com/?u=003f3ec44928e2d1d3d506a22&id=025b945e22\",\"status\":\"sent\",\"emails_sent\":9,\"send_time\":\"2019-03-15T14:35:05+00:00\",\"content_type\":\"html\",\"needs_block_refresh\":false,\"has_logo_merge_tag\":false,\"resendable\":true,\"recipients\":{\"list_id\":\"0a05001000\",\"list_is_active\":true,\"list_name\":\"TEST_LIST\",\"segment_text\":\"\",\"recipient_count\":9},\"settings\":{\"subject_line\":\"Implementation Insights for TEST on March 14, 2019\",\"title\":\"TEST #15 2018\",\"from_name\":\"Sue Tester, Ph.D.\",\"reply_to\":\"Sue.Tester@pearson.com\",\"use_conversation\":false,\"to_name\":\"*|FNAME|* *|LNAME|*\",\"folder_id\":\"aa1a88812a\",\"authenticate\":true,\"auto_footer\":false,\"inline_css\":false,\"auto_tweet\":false,\"fb_comments\":true,\"timewarp\":false,\"template_id\":0,\"drag_and_drop\":false},\"tracking\":{\"opens\":true,\"html_clicks\":true,\"text_clicks\":false,\"goal_tracking\":false,\"ecomm360\":false,\"google_analytics\":\"\",\"clicktale\":\"N\"},\"report_summary\":{\"opens\":23,\"unique_opens\":3,\"open_rate\":0.375,\"clicks\":0,\"subscriber_clicks\":0,\"click_rate\":0,\"ecommerce\":{\"total_orders\":0,\"total_spent\":0,\"total_revenue\":0}},\"delivery_status\":{\"enabled\":false},\"_links\":[{\"rel\":\"parent\",\"href\":\"https://us3.api.mailchimp.com/3.0/campaigns\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Campaigns/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Campaigns.json\"},{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/campaigns/025b945a01\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Campaigns/Response.json\"},{\"rel\":\"delete\",\"href\":\"https://us3.api.mailchimp.com/3.0/campaigns/025b945a01\",\"method\":\"DELETE\"},{\"rel\":\"send\",\"href\":\"https://us3.api.mailchimp.com/3.0/campaigns/025b945a01/actions/send\",\"method\":\"POST\"},{\"rel\":\"cancel_send\",\"href\":\"https://us3.api.mailchimp.com/3.0/campaigns/025b945a01/actions/cancel-send\",\"method\":\"POST\"},{\"rel\":\"feedback\",\"href\":\"https://us3.api.mailchimp.com/3.0/campaigns/025b945a01/feedback\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Campaigns/Feedback/CollectionResponse.json\"},{\"rel\":\"content\",\"href\":\"https://us3.api.mailchimp.com/3.0/campaigns/025b945a01/content\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Campaigns/Content/Response.json\"},{\"rel\":\"send_checklist\",\"href\":\"https://us3.api.mailchimp.com/3.0/campaigns/025b945a01/send-checklist\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Campaigns/Checklist/Response.json\"},{\"rel\":\"pause\",\"href\":\"https://us3.api.mailchimp.com/3.0/campaigns/025b945a01/actions/pause\",\"method\":\"POST\"},{\"rel\":\"resume\",\"href\":\"https://us3.api.mailchimp.com/3.0/campaigns/025b945a01/actions/resume\",\"method\":\"POST\"},{\"rel\":\"replicate\",\"href\":\"https://us3.api.mailchimp.com/3.0/campaigns/025b945a01/actions/replicate\",\"method\":\"POST\"},{\"rel\":\"create_resend\",\"href\":\"https://us3.api.mailchimp.com/3.0/campaigns/025b945a01/actions/create-resend\",\"method\":\"POST\"}]}");
		Campaign campaign = new Campaign(null, jsonObj);
		assertEquals(campaign.getId(), "012abcde01");
		assertEquals(campaign.getType(), CampaignType.REGULAR);
		assertEquals(campaign.getStatus(), CampaignStatus.SENT);
		assertEquals(campaign.getRecipients().getListId(), "0a05001000");
		assertEquals(campaign.getRecipients().getListName(), "TEST_LIST");
		assertEquals(campaign.getReportSummary().getOpens(), 23);

		// TODO: JSONObject json = campaign.getJsonRepresentation();
	}

	@Test
	public void testCampaignSegmentOptsJSONObject() {
		JSONObject jsonObj = new JSONObject("{\"saved_segment_id\":40229,\"match\":\"any\",\"conditions\":[{\"condition_type\":\"Interests\",\"field\":\"interests-6dc9e2022a\",\"op\":\"interestcontains\",\"value\":[\"66af3e0301\"]}]}");
		CampaignSegmentOpts segopts = new CampaignSegmentOpts(jsonObj);
		assertEquals(segopts.getSavedSegmentId().intValue(), 40229);
		assertEquals(segopts.getMatch(), MatchType.ANY);
		assertEquals(segopts.getConditions().size(), 1);
		assertEquals(segopts.getConditions().get(0).getConditionType(), ConditionType.INTERESTS);
		
		JSONObject json = segopts.getJsonRepresentation();
		assertEquals(json.getString("match"), MatchType.ANY.getStringRepresentation());
		assertEquals(json.getInt("saved_segment_id"), 40229);
		JSONArray jsonarr = json.getJSONArray("conditions");
		JSONObject cond = (JSONObject)jsonarr.get(0);
		assertEquals(cond.getString("condition_type"), ConditionType.INTERESTS.value());
	}
}
