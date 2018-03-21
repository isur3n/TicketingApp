package com.walmart.surendra.ticketingApp.ticketingService.bookingAlgorithms.closeGroupBooking.service;

import com.walmart.surendra.ticketingApp.ticketingService.bookingAlgorithms.closeGroupBooking.domain.ShowTime;
import com.walmart.surendra.ticketingApp.ticketingService.domain.SeatHold;
import com.walmart.surendra.ticketingApp.ticketingService.interfaces.TicketService;
import com.walmart.surendra.ticketingApp.ticketingService.util.EmailUtil;

/**
 * Created by Surendra Raut on 11/26/2017.
 */
public class CloseGroupService implements TicketService {

    private ShowTime movieShow;

    public CloseGroupService() {}

    public CloseGroupService(ShowTime showtime) {
        movieShow = showtime;
    }

    /**
     * The number of seats in the venue that are neither held nor reserved
     *
     * @return the number of tickets available in the venue
     */
    public int numSeatsAvailable() {
        return movieShow.getAvailableSeats().size();
    }

    /**
     * Find and hold then best available seats for a customer
     *
     * @param numSeats the number of seats to find and hold
     * @param customerEmail unique identifier for the customer
     * @return a SeatHold object identifying the specific seats and related
    information
     */
    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {

        /**
         * Perform validation of provided data.
         */
        if(numSeats <= 0) {
            //ToDo: Throw an exception
            return null;
        }
        else if(EmailUtil.isEmailIdValid(customerEmail) == false) {
            //ToDo: Throw an exception
            return null;
        }

        return movieShow.holdBestSeats(numSeats, customerEmail);
    }
    /**
     * Commit seats held for a specific customer
     *
     * @param seatHoldId the seat hold identifier
     * @param customerEmail the email address of the customer to which the seat hold is assigned
     * @return a reservation confirmation code
     */
    public String reserveSeats(int seatHoldId, String customerEmail) {
        return "";
    }
}
