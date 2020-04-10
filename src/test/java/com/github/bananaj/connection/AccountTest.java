package com.github.bananaj.connection;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;

import com.github.bananaj.connection.Account;

public class AccountTest {

	@Test
	public void testAccount() {
		JSONObject jsonObj = new JSONObject("{\"account_id\":\"0123456789abcdef012345678\",\"login_id\":\"01234567\",\"account_name\":\"testman\",\"email\":\"no.spam@gmail.com\",\"first_name\":\"John\",\"last_name\":\"Test\",\"username\":\"no.spam@gmail.com\",\"avatar_url\":\"https://secure.gravatar.com/avatar/00000000000000000000000000000000.jpg?s=300&d=https%3A%2F%2Fcdn-images.mailchimp.com%2Ficons%2Fletter-avatars%2Fg.png\",\"role\":\"admin\",\"member_since\":\"2013-01-18T20:10:25+00:00\",\"pricing_plan_type\":\"pay_as_you_go\",\"first_payment\":\"2013-02-04T22:59:20+00:00\",\"account_timezone\":\"US/Arizona\",\"account_industry\":\"Education and Training\",\"contact\":{\"company\":\"Home\",\"addr1\":\"1000 W May Rd Ste 500\",\"addr2\":\"\",\"city\":\"Chandler\",\"state\":\"AZ\",\"zip\":\"85203-2941\",\"country\":\"US\"},\"pro_enabled\":false,\"last_login\":\"2019-08-16T17:24:02+00:00\",\"total_subscribers\":1592,\"industry_stats\":{\"open_rate\":0.1786604351339173835100382348173297941684722900390625,\"bounce_rate\":0.00925411689963563134642132723683971562422811985015869140625,\"click_rate\":0.025020219652535570509233053826392279006540775299072265625},\"_links\":[{\"rel\":\"self\",\"href\":\"https://us3.api.mailchimp.com/3.0/\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Root.json\"},{\"rel\":\"lists\",\"href\":\"https://us3.api.mailchimp.com/3.0/lists\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Lists/Collection.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Lists.json\"},{\"rel\":\"reports\",\"href\":\"https://us3.api.mailchimp.com/3.0/reports\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Reports/Collection.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Reports.json\"},{\"rel\":\"conversations\",\"href\":\"https://us3.api.mailchimp.com/3.0/conversations\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Conversations/Collection.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Conversations.json\"},{\"rel\":\"campaigns\",\"href\":\"https://us3.api.mailchimp.com/3.0/campaigns\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Campaigns/Collection.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Campaigns.json\"},{\"rel\":\"automations\",\"href\":\"https://us3.api.mailchimp.com/3.0/automations\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Automations/Collection.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Automations.json\"},{\"rel\":\"templates\",\"href\":\"https://us3.api.mailchimp.com/3.0/templates\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Templates/Collection.json\",\"schema\":\"https://us3.api.mailchimp.com/schema/3.0/CollectionLinks/Templates.json\"},{\"rel\":\"file-manager\",\"href\":\"https://us3.api.mailchimp.com/3.0/file-manager\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/FileManager/Namespace.json\"},{\"rel\":\"authorized-apps\",\"href\":\"https://us3.api.mailchimp.com/3.0/authorized-apps\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/AuthorizedApps/Collection.json\"},{\"rel\":\"batches\",\"href\":\"https://us3.api.mailchimp.com/3.0/batches\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Batches/Collection.json\"},{\"rel\":\"template-folders\",\"href\":\"https://us3.api.mailchimp.com/3.0/template-folders\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/TemplateFolders/Collection.json\"},{\"rel\":\"campaign-folders\",\"href\":\"https://us3.api.mailchimp.com/3.0/campaign-folders\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/CampaignFolders/Collection.json\"},{\"rel\":\"ecommerce\",\"href\":\"https://us3.api.mailchimp.com/3.0/ecommerce\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Ecommerce/Namespace.json\"},{\"rel\":\"ping\",\"href\":\"https://us3.api.mailchimp.com/3.0/ping\",\"method\":\"GET\",\"targetSchema\":\"https://us3.api.mailchimp.com/schema/3.0/Ping/Namespace.json\"}]}");
		Account account = new Account(null, jsonObj);
		assertEquals("0123456789abcdef012345678", account.getId());
		assertEquals("testman", account.getAccountName());
		assertEquals("01234567", account.getLoginId());
		assertEquals("Home", account.getContact().getCompany());
		assertEquals("admin", account.getRole());
		assertEquals("no.spam@gmail.com", account.getEmail());
		
		//JSONObject json = account.getJSONRepresentation();
	}

}
