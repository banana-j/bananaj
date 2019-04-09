package com.github.alexanderwe.bananaj.utils;


import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Validator for E-Mail addresses. Replacement for deprecated apache commons EmailValidator.
 * Created by alexanderweiss on 27.12.16.
 */
public class DateConverter {

    private static DateConverter instance = null;

    protected DateConverter () {

    }

    public static DateConverter getInstance(){
        if(instance == null){
            instance = new DateConverter();
        }
        return instance;
    }

    /**
     * Convert a date formatted in IS8601 to a normal java date
     * @param dateString
     * @return Date
     */
    public LocalDateTime createDateFromISO8601(String dateString) {
    	if (dateString == null || dateString.length() == 0) {
    		return null;
    	}

		// mailchimp sometimes returns a year offset rather than a properly formated ISO
		// 8601 such as '-001-11-30T00:00:00+00:00'
    	if (dateString.startsWith("-")) {
    		int yearOffset = Integer.parseInt(dateString.substring(0, 4));
    		int adjustedYear = LocalDateTime.now().getYear() + yearOffset;
    		String adjustedDate = "" + adjustedYear + dateString.substring(4);
    		return ZonedDateTime.parse(adjustedDate).toLocalDateTime();
    	}
    	return ZonedDateTime.parse(dateString).toLocalDateTime();
    }

    /* Convert a date formatted in IS8601 to a normal java date
     * @param dateString
     * @return Date
     */
    public LocalDateTime createDateFromNormal(String dateString) {
        String formatIn = "yyyy-MM-dd HH:mm:ss";
        return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(formatIn));
    }
    
    /**
	 * Mailchimp does not handle correct ISO8601 dates when sent to the API. It
	 * requires the "T" to be a space, and will reject a date that includes the
	 * timezone (the +HH:MM on the end).
	 * 
	 * @param ldt
	 * @return
	 */
    public static String toNormal(LocalDateTime ldt) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd' 'HH:mm:ss");
    	return ldt.format(formatter);
    	
//    	ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
//    	return zdt.toString();

//        Date ts = Date.from(zdt.toInstant());
//
//		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
//		String timeStamp = dateFormatGmt.format(new Date());
//		return timeStamp;
    }
}
