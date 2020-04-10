package com.github.bananaj.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validator for E-Mail addresses. Replacement for deprecated apache commons EmailValidator.
 * Created by alexanderweiss on 27.12.16.
 */
public class EmailValidator {

    private static EmailValidator instance = null;
    private static final String emailRegex  = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"; // RFC 5322 Internet Message Format characters allowed

    protected EmailValidator () {

    }

    public static EmailValidator getInstance(){
        if(instance == null){
            instance = new EmailValidator();
        }
        return instance;
    }

    public boolean validate(String email){
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
