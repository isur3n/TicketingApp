package com.walmart.surendra.ticketingApp.ticketingService.util;

import org.apache.log4j.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Surendra Raut on 11/28/2017.
 */
public class EmailUtil {
    private static final Logger logger = Logger.getLogger(EmailUtil.class);

    private static final String EMAIL_ID_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern EMAIL_ID_PATTERN = Pattern.compile(EMAIL_ID_REGEX);

    /**
     * Private constructor forces it to be a static class
     */
    private EmailUtil() {
    }

    /**
     * Method isEmailIdValid will match pre determined pattern to check if email id is valid or not.
     * It makes sure pattern is matched against entire string than just a sub string.
     * This ensures there is only 1 valid email address present.
     *
     * @param emailId String passed to validate
     * @return boolean true if whole string is valid email address; false otherwise
     */
    public static boolean isEmailIdValid(String emailId) {
        logger.debug("EmailID to verify " + emailId);
        boolean value = false;

        if(emailId == null || "".equals(emailId)) {
            logger.debug("EmailID is empty/null");
            return value;
        }

        Matcher emailIdMatcher = EMAIL_ID_PATTERN.matcher(emailId);
        value = emailIdMatcher.matches();
        logger.debug("EmailId is valid? " + value);

        return value;
    }

    public static void sendEmail(String confirmationId, String emailId) {
        logger.debug("EmailID  confirmatioID " + confirmationId + " to emailId " + emailId);
    }
}
