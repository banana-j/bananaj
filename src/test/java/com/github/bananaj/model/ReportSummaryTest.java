package com.github.bananaj.model;

import static org.junit.Assert.assertEquals;

import org.json.JSONObject;
import org.junit.Test;

import com.github.bananaj.model.ReportSummary;

public class ReportSummaryTest {

	@Test
	public void testReportSummary() {
		JSONObject jsonObj = new JSONObject("{\"opens\":23,\"unique_opens\":3,\"open_rate\":0.375,\"clicks\":0,\"subscriber_clicks\":0,\"click_rate\":0,\"ecommerce\":{\"total_orders\":0,\"total_spent\":0,\"total_revenue\":0}}");
		ReportSummary r = new ReportSummary(jsonObj);
		assertEquals(r.getOpens(), 23);
		assertEquals(r.getUniqueOpens(), 3);
		assertEquals(r.getClicks(), 0);
		assertEquals(r.getSubscriberClicks(), 0);
		assertEquals(r.getEcommerce().getTotalOrders(), 0);
	}

}
