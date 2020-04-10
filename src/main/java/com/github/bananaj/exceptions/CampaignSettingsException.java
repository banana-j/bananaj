package com.github.bananaj.exceptions;

/**
 * Created by alexanderweiss on 02.01.17.
 */

public class CampaignSettingsException extends Exception {

    public CampaignSettingsException()  {
        super("Invalid email address.");
    }

    public CampaignSettingsException(String message) {
        super(message);
    }

    public CampaignSettingsException(Throwable cause) {
        super(cause);
    }

    public CampaignSettingsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CampaignSettingsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
