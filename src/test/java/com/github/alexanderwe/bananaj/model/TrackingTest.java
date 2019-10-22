package com.github.alexanderwe.bananaj.model;

import static org.junit.Assert.assertEquals;

import org.json.JSONObject;
import org.junit.Test;

public class TrackingTest {

	@Test
	public void testTracking() {
		JSONObject jsonObj = new JSONObject("{\"opens\":true,\"html_clicks\":true,\"text_clicks\":false,\"goal_tracking\":false,\"ecomm360\":false,\"google_analytics\":\"\",\"clicktale\":\"N\"}");
		Tracking t = new Tracking(jsonObj);
		assertEquals(t.getClicktale(), "N");
		assertEquals(t.getGoogleAnalytics(), "");
		assertEquals(t.isOpens(), Boolean.TRUE);
		assertEquals(t.isHtmlClicks(), Boolean.TRUE);
		assertEquals(t.isTextClicks(), Boolean.FALSE);
		assertEquals(t.isGoalTracking(), Boolean.FALSE);
		assertEquals(t.isEcomm360(), Boolean.FALSE);
	}

}
