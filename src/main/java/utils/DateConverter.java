package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validator for E-Mail addresses. Replacement for deprecated apache commons EmailValidator.
 * Created by alexanderweiss on 27.12.16.
 */
public class DateConverter {

    private static DateConverter instance = null;
    private static final String emailRegex  = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"; // RFC 5322 Internet Message Format characters allowed

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
    public Date createDateFromISO8601(String dateString){
        Date date;
        try {
            date = javax.xml.bind.DatatypeConverter.parseDateTime(dateString).getTime();
        } catch(IllegalArgumentException iae) {
            date = null;
        }

        return date;
    }

    /* Convert a date formatted in IS8601 to a normal java date
     * @param dateString
     * @return Date
     */
    public Date createDateFromNormal(String dateString){
        Date date;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse date: ", e);
        }

        return date;
    }
}
