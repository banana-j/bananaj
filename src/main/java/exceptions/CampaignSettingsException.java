package exceptions;

/**
 * Created by alexanderweiss on 02.01.17.
 */

public class CampaignSettingsException extends Exception {

    public CampaignSettingsException()  {
        super("Invalid email address.");
    }

    public CampaignSettingsException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public CampaignSettingsException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    public CampaignSettingsException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public CampaignSettingsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }
}
