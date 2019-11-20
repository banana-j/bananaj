package com.github.alexanderwe.bananaj.model.template;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;

public class TemplateTest {

	@Test
	public void testTemplate() throws Exception {
		JSONObject jsonObj = new JSONObject("{\"id\":123413,\"type\":\"user\",\"name\":\"Test Template\",\"drag_and_drop\":true,\"responsive\":true,\"category\":\"\",\"date_created\":\"2019-07-30T18:19:34+00:00\",\"date_edited\":\"2019-08-06T22:04:05+00:00\",\"created_by\":\"Sue Smith\",\"edited_by\":\"John Tester\",\"active\":true,\"folder_id\":\"11223a6040\",\"thumbnail\":\"http://gallery.com/thumbnail/screen.png\",\"share_url\":\"\",\"_links\":[{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/templates/123413\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Templates/Response.json\"},{\"rel\":\"parent\",\"href\":\"https://us3.api.mailchimp.com/3.0/templates\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Templates/CollectionResponse.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Templates.json\"},{\"rel\":\"delete\",\"href\":\"https://us3.api.mailchimp.com/3.0/templates/123413\",\"method\":\"DELETE\"},{\"rel\":\"default-content\",\"href\":\"https://us3.api.mailchimp.com/3.0/templates/123413/default-content\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Definitions/Templates/Default-Content/Response.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Templates.json\"}]}");
		Template template = new Template(null, jsonObj);
		assertEquals(123413, template.getId());
		assertEquals("Test Template", template.getName());
		assertEquals("", template.getCategory());
		assertEquals("11223a6040", template.getFolderId());
		assertEquals("Sue Smith", template.getCreatedBy());
		assertEquals("John Tester", template.getEditedBy());
		
		JSONObject json = template.getJsonRepresentation();
		assertEquals(json.getString("name"), "Test Template");
	}

}
