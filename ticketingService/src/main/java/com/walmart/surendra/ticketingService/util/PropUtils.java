package com.walmart.surendra.ticketingService.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Surendra Raut on 11/29/2017.
 */
public class PropUtils {

    private static final Logger logger = Logger.getLogger(PropUtils.class);

    private static final String PROPERTIES_FILE = "ticketingService.properties";

    private static final Properties prop = new Properties();

    static {
        try {
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILE));
        }
        catch(IOException e) {
            logger.error("Could not load properties from " + PROPERTIES_FILE);
        }
    }

    private PropUtils() {}

    public static void set() {

    }

    public static String getAppPropertyValue(String key, boolean essential) {
        logger.debug("Finding property " + key + " Eseential " + essential);
        String propValue = prop.getProperty(key);

        if(propValue == null && essential == true) {
            logger.fatal("Property key " + key + " NOT found. Can't proceed further & hence exiting ...");
            System.exit(-1);
        }

        return propValue;
    }

    // All properties key constants below
    public static final String SHOW_ROWS = "showRows";

    public static final String SHOW_COLS = "showCols";

    public static final String EXPIRY_MILLI_SECONDS = "expiryMilliSeconds";

    public static final String RESERVATION_ID_SEQ_START = "reservationIdSeqStart";

    public static final String SEAT_ROW_MULTIPLIER = "seatRowMultiplier";

    public static final String EMAIL_ID_REGEX = "emailIdRegEx";

    public static final String LOCK_WAIT_MILLI_SECONDS = "lockWaitMilliSeconds";
}
