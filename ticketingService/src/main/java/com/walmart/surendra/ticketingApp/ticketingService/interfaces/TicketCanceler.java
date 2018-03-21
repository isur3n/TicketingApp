package com.walmart.surendra.ticketingApp.ticketingService.interfaces;

import com.walmart.surendra.ticketingApp.ticketingService.exceptions.TicketingServiceException;

/**
 * Created by Surendra Raut on 11/29/2017.
 */
public interface TicketCanceler {

    void cancelSeatHold(int seatHoldId) throws TicketingServiceException;
}
