package com.github.alexanderwe.bananaj.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

public class UtilsTest {

	@Test
	public void testMD5() {
		assertEquals(MD5.getMD5("The quick brown fox jumps over the lazy dog"), "9e107d9d372bb6826bd81d3542a419d6");
		assertEquals(MD5.getMD5("The quick brown fox jumps over the lazy dog."), "e4d909c290d0fb1ca068ffaddf22cbd0");
		assertEquals(MD5.getMD5(""), "d41d8cd98f00b204e9800998ecf8427e");
		assertEquals(MD5.getMD5("abcdefghijklmnopqrstuvwxyzABC123!@#$%^&*()-_=+;:'\",./<>?"), "f9c7908008047e93a05c0841fc4c8613");
	}

	@Test
	public void testEmailValidator() {
		EmailValidator v = EmailValidator.getInstance();
		assertFalse("Empty string", v.validate(""));
		assertFalse("test", v.validate("test"));
		assertFalse("@", v.validate("@"));
		assertFalse("test@", v.validate("test@"));
		//assertTrue("test@io", v.validate("test@io"));
		assertFalse("@io", v.validate("@io"));
		assertFalse("@iana.org", v.validate("@iana.org"));
		assertTrue("test@iana.org", v.validate("test@iana.org"));
		assertTrue("test@nominet.org.uk", v.validate("test@nominet.org.uk"));
		assertTrue("test@about.museum", v.validate("test@about.museum"));
		assertTrue("a@iana.org", v.validate("a@iana.org"));
		//assertFalse("test@e.com", v.validate("test@e.com")); // no DNS so should fail - we don't validate DNS records.
		//assertFalse("test@iana.a", v.validate("test@iana.a")); // no DNS so should fail - we don't validate DNS records.
		assertTrue("test.test@iana.org", v.validate("test.test@iana.org"));
		assertFalse(".test@iana.org", v.validate(".test@iana.org"));
		assertFalse("test.@iana.org", v.validate("test.@iana.org"));
		assertFalse("test..iana.org", v.validate("test..iana.org"));
		assertFalse("test_exa-mple.com", v.validate("test_exa-mple.com"));
		//assertTrue("!#$%&amp;`*+/=?^`{|}~@iana.org", v.validate("!#$%&amp;`*+/=?^`{|}~@iana.org"));
		assertFalse("test\\@test@iana.org", v.validate("test\\@test@iana.org"));
		assertTrue("123@iana.org", v.validate("123@iana.org"));
		assertTrue("test@123.com", v.validate("test@123.com"));
		//assertTrue("test@iana.123", v.validate("test@iana.123"));
		//assertTrue("test@255.255.255.255", v.validate("test@255.255.255.255"));
		assertTrue("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghiklm@iana.org", v.validate("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghiklm@iana.org"));
		assertTrue("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghiklmn@iana.org", v.validate("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghiklmn@iana.org"));
		//assertFalse("test@abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghikl.com", v.validate("test@abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghikl.com")); //   we don't validate DNS records.
		assertTrue("test@mason-dixon.com", v.validate("test@mason-dixon.com"));
		//assertFalse("test@-iana.org", v.validate("test@-iana.org"));
		//assertFalse("test@iana-.com", v.validate("test@iana-.com"));
		assertTrue("test@c--n.com", v.validate("test@c--n.com"));
		assertFalse("test@.iana.org", v.validate("test@.iana.org"));
		// ... many more ...
	}

	@Test
	public void testDateConverter() {
		ZonedDateTime ld1 = DateConverter.fromISO8601("2019-03-15T14:34:59+00:00");
		ZonedDateTime ld2 = ZonedDateTime.of(2019, 03, 15, 14, 34, 59, 0, ZoneId.of("+00:00"));
		assertEquals("2019-03-15T14:34:59+00:00", DateConverter.toISO8601UTC(ld1));
		assertEquals(DateConverter.toISO8601UTC(ld1), DateConverter.toISO8601UTC(ld2));
		ld2 = ZonedDateTime.of(2019, 03, 15, 14, 34, 59, 0, ZoneId.of("UTC"));
		assertEquals(DateConverter.toISO8601UTC(ld1), DateConverter.toISO8601UTC(ld2));

		// Time offset to UTC test
		assertEquals("2019-03-15T19:04:59+00:00", DateConverter.toISO8601UTC(DateConverter.fromISO8601("2019-03-15T14:04:59-05:00")));

		// Mailchimp sometimes reports a year offset. Note: as of 2019-10-31 no longer seeing this
		ld1 = DateConverter.fromISO8601("-001-11-30T00:00:00+00:00");
		ld2 = ZonedDateTime.of(ZonedDateTime.now().getYear() - 1, 11, 30, 0, 0, 0, 0, ZoneId.of("UTC"));
		assertEquals(DateConverter.toISO8601UTC(ld1), DateConverter.toISO8601UTC(ld2));
	}

}
