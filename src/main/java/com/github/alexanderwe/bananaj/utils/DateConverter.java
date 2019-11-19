package com.github.alexanderwe.bananaj.utils;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Date time conversion functions for conversion and formatting of Mailchimp dates
 */
public class DateConverter {

    private static DateConverter instance = null;

    protected DateConverter () {

    }

    /**
     * Convert a date string in ISO 8601 formant to a date-time with a time-zone in the ISO-8601 calendar system.
     * @param iso8601String
     * @return Date
     */
    public static ZonedDateTime fromISO8601(String iso8601String) {
    	if (iso8601String == null || iso8601String.length() == 0) {
    		return null;
    	}

		// mailchimp sometimes returns a year offset rather than a properly formated ISO
		// 8601 such as '-001-11-30T00:00:00+00:00'
    	if (iso8601String.startsWith("-")) {
    		// Note: as of 2019-10-31 no longer seeing dates with offset year (-001). May have been fixed in Mailchimp API
    		int yearOffset = Integer.parseInt(iso8601String.substring(0, 4));
    		int adjustedYear = LocalDateTime.now().getYear() + yearOffset;
    		String adjustedDate = "" + adjustedYear + iso8601String.substring(4);
    		return ZonedDateTime.parse(adjustedDate);
    	}
    	return ZonedDateTime.parse(iso8601String, DateTimeFormatter.ISO_OFFSET_DATE_TIME).withFixedOffsetZone();
    }

    /**
	 * Convert a ZonedDateTime to a string in universal time coordinates using the
	 * format YYYY-MM-DDThh:mm:ssTZD (eg 1997-07-16T19:20:30+00:00).
	 * 
	 * @param zonedDateTime
	 * @return The string representation of zonedDateTime as YYYY-MM-DDThh:mm:ssTZD
	 *         (eg 1997-07-16T19:20:30+00:00).
	 */
    public static String toISO8601UTC(ZonedDateTime zonedDateTime) {
    	if (zonedDateTime == null) {
    		return "";
    	}
		ZonedDateTime utc = ZonedDateTime.ofInstant(zonedDateTime.toInstant(), ZoneId.of("UTC"));
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssxxxxx");
    	return utc.format(formatter);
    }
    
    /**
     * Converts a ZonedDateTime, offset to UTC, as a local string. 
     * @param zonedDateTime
     * @return Date time string (yyy-mm-dd hh:mm:ss) in UTC time.
     */
    public static String toUTCLocalString(ZonedDateTime zonedDateTime) {
    	if (zonedDateTime == null) {
    		return "";
    	}
		ZonedDateTime utc = ZonedDateTime.ofInstant(zonedDateTime.toInstant(), ZoneId.of("UTC"));
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd' 'HH:mm:ss");
    	return utc.format(formatter);
    }
    
    /**
     * Converts a ZonedDateTime, offset to the system default time zone, as a local string.
     * @param zonedDateTime
     * @return Date time string (yyy-mm-dd hh:mm:ss) in the system time zone
     */
    public static String toLocalString(ZonedDateTime zonedDateTime) {
    	if (zonedDateTime == null) {
    		return "";
    	}
		ZonedDateTime utc = ZonedDateTime.ofInstant(zonedDateTime.toInstant(), ZoneId.systemDefault());
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd' 'HH:mm:ss");
    	return utc.format(formatter);
    }
    
}
