package com.walmart.surendra.ticketingService.app;

import com.walmart.surendra.ticketingService.bookingAlgorithms.simpleBooking.domain.Show;
import com.walmart.surendra.ticketingService.bookingAlgorithms.simpleBooking.service.TicketServiceImpl;
import com.walmart.surendra.ticketingService.domain.SeatHold;
import com.walmart.surendra.ticketingService.exceptions.TicketingServiceException;
import com.walmart.surendra.ticketingService.interfaces.TicketService;
import com.walmart.surendra.ticketingService.util.PropUtils;

/**
 * Created by Surendra Raut on 12/1/2017.
 *
 * This demonstrates abilities of ticketing service
 */
    public class demo {

    private static Show movieShow;

    private static TicketService ts;

    static {
        int rows = Integer.valueOf(PropUtils.getAppPropertyValue(PropUtils.SHOW_ROWS, true));
        int cols = Integer.valueOf(PropUtils.getAppPropertyValue(PropUtils.SHOW_COLS, true));

        movieShow = new Show(rows, cols);
        ts = new TicketServiceImpl(movieShow);
    }

    public static void checkAvailableSeats() {
        try {
            System.out.println("Available seats " + ts.numSeatsAvailable());
        }
        catch(TicketingServiceException e) {
            System.out.println("Error while checking seat availability " + e.getErrorMessage());
        }
    }

    public static int findHold(int numSeats, String customerEmail) {
        SeatHold hold = null;

        try {
            hold = ts.findAndHoldSeats(numSeats, customerEmail);
        }
        catch (TicketingServiceException e) {
            System.out.println("Error while holding " + e.getErrorMessage());
        }

        if(hold != null) {
            System.out.println("SeatHold ID " + hold.getReservationId());
            return hold.getReservationId();
        }

        return 0;
    }

    public static String reserve(int seatHoldId, String customerEmail) {
        String confirmId = null;

        try {
            confirmId = ts.reserveSeats(seatHoldId, customerEmail);
        }
        catch (TicketingServiceException e) {
            System.out.println("Error while confirming " + seatHoldId + " " + e.getErrorMessage());
        }

        return confirmId;
    }

    public static void main (String[] args) {

        // Get available seats
        checkAvailableSeats();

        // Find-N-Hold seats
        int hold1 = findHold(3, "fname.lname@company.com");
        String cnf1 = reserve(hold1, "fname.lname@company.com");

        // Get available seats
        checkAvailableSeats();
        int hold2 = findHold(6, "fname_lname@company.com");

        // This will fail
        int hold3 = findHold(6, "fname_lname@company.com");

        try{
            Thread.sleep(5000);
        }
        catch(InterruptedException e) {}

        String cnf2 = reserve(hold2, "fname_lname@company.com");

        // Get available seats
        checkAvailableSeats();

        // Simulate insufficient tickets
        int hold4 = findHold(6, "fname.lname1@company.com");
        int hold5 = findHold(8, "fname.lname2@company.com");
        int hold6 = findHold(3, "fname.lname3@company.com");
        int hold7 = findHold(6, "fname.lname4@company.com");
    }
}
