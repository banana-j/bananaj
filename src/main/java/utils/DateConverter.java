package utils;


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
    public LocalDateTime createDateFromISO8601(String dateString){
        ZonedDateTime zdt = ZonedDateTime.parse(dateString);
        return zdt.toLocalDateTime();
    }

    /* Convert a date formatted in IS8601 to a normal java date
     * @param dateString
     * @return Date
     */
    public LocalDateTime createDateFromNormal(String dateString) {
        String formatIn = "yyyy-MM-dd HH:mm:ss";
        return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(formatIn));
    }
}
