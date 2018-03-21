package com.walmart.surendra.ticketingApp.ticketingService.exceptions;

import com.walmart.surendra.ticketingApp.ticketingService.enums.TicketingServiceError;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * Created by Surendra Raut on 11/27/2017.
 */
public class TicketingServiceException extends Exception {

    private static final Logger logger = Logger.getLogger(TicketingServiceException.class);

    private static HashMap<TicketingServiceError, String> errorMessageMap;

    static {
        errorMessageMap = new HashMap<TicketingServiceError, String>();
        errorMessageMap.put(TicketingServiceError.LOCKING_INTERRUPTED, "Locking was interrupted");
        errorMessageMap.put(TicketingServiceError.UNEXPECTED, "Unexception exception occcured.");
        errorMessageMap.put(TicketingServiceError.INVALID_NUM_SEATS, "Invalid number of seats entered.");
        errorMessageMap.put(TicketingServiceError.EMPTY_EMAIL_ID, "Empty email id entered.");
        errorMessageMap.put(TicketingServiceError.INVALID_EMAIL_ID, "Invalid email id entered.");
        errorMessageMap.put(TicketingServiceError.INSUFFICIENT_SEATS_AVAILABLE, "Insufficient seats available.");
        errorMessageMap.put(TicketingServiceError.CUST_EMAIL_ID_IN_USE, "Customer email id already in use.");
        errorMessageMap.put(TicketingServiceError.INVALID_SEATHOLD_ID, "Invalid seat hold id entered.");
        errorMessageMap.put(TicketingServiceError.SEATHOLD_ID_MATCH_EMAIL_MISMATCH, "SeatHold id matched but Email id mismatched.");
        errorMessageMap.put(TicketingServiceError.SEATHOLD_ID_ALREADY_RESERVED, "SeatHold id is already reserved.");
        errorMessageMap.put(TicketingServiceError.SEATHOLD_ID_EXPIRED, "SeatHold id is expired.");
        errorMessageMap.put(TicketingServiceError.SEATHOLD_ID_EXPIRED_OR_RESERVED, "SeatHold id is expired or already reserved.");
        errorMessageMap.put(TicketingServiceError.LOCKING_TIMED_OUT, "Locking attempt timed out.");
        errorMessageMap.put(TicketingServiceError.UNLOCK_FAILED, "Unlock failed. Consistency may have been compromised");
    }


    private TicketingServiceError errorId;

    public TicketingServiceException(TicketingServiceError errorId) {
        super();
        this.errorId = errorId;
    }

    public TicketingServiceError getErrorId() {
        return errorId;
    }

    public String getErrorMessage() {
        String errMessage = errorMessageMap.get(errorId);

        if(errMessage == null || "".equals(errMessage)) {
            logger.warn("Error message not found for " + errorId);
        }

        return errMessage;
    }
}
