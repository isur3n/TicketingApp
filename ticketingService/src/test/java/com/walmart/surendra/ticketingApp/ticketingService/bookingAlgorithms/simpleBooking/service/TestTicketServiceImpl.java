package com.walmart.surendra.ticketingApp.ticketingService.bookingAlgorithms.simpleBooking.service;

import com.walmart.surendra.ticketingApp.ticketingService.bookingAlgorithms.simpleBooking.domain.Show;
import com.walmart.surendra.ticketingApp.ticketingService.domain.SeatHold;
import com.walmart.surendra.ticketingApp.ticketingService.enums.TicketingServiceError;
import com.walmart.surendra.ticketingApp.ticketingService.exceptions.TicketingServiceException;
import com.walmart.surendra.ticketingApp.ticketingService.util.PropUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Surendra Raut on 11/30/2017.
 */
public class TestTicketServiceImpl {

    private static SimpleGroupService ts;

    private static Show movieShow;

    public TestTicketServiceImpl() {
        int rows = Integer.valueOf(PropUtils.getAppPropertyValue(PropUtils.SHOW_ROWS, true));
        int cols = Integer.valueOf(PropUtils.getAppPropertyValue(PropUtils.SHOW_COLS, true));

        movieShow = new Show(rows, cols);
        ts = new SimpleGroupService(movieShow);
    }

    @Before
    public void setUp() {
        /*System.out.println("Before - Available " + movieShow.getAvailableSeats().size() +
                " On Hold Reservations " + movieShow.getOnHoldBooking().size() +
                " Confirmed reservations " + movieShow.getReservedBooking().size());*/
    }

    @After
    public void tearDown() {
        /*System.out.println("After - Available " + movieShow.getAvailableSeats().size() +
                " On Hold Reservations " + movieShow.getOnHoldBooking().size() +
                " Confirmed reservations " + movieShow.getReservedBooking().size());*/
    }

    public void testNumSeatAvailable(int expected) {
        int numSeats = -1;

        try {
            numSeats = ts.numSeatsAvailable();
        }
        catch(TicketingServiceException e) {
            Assert.fail();
        }
        catch(Exception e) {
            Assert.fail();
        }

        Assert.assertEquals(numSeats, expected);
    }

    public SeatHold findNewSeatHold(int seatsToHold, String emailId) {
        SeatHold newHold = null;
        try {
            newHold = ts.findAndHoldSeats(seatsToHold, emailId);
        }
        catch(TicketingServiceException e) {
            Assert.fail();
        }
        Assert.assertEquals(emailId, newHold.getBookingEmailId());
        Assert.assertEquals(seatsToHold, newHold.getSeatCollection().size());
        Assert.assertEquals(true, newHold.getExpiryTimer().isAlive());

        return newHold;
    }

    @Test
    public void testNumSeatAvailable1() {
        testNumSeatAvailable(20);
    }

    @Test
    public void testHoldAndReserve() {
        SeatHold hold1 = findNewSeatHold(4, "surendra.raut@gmail.com");
        testNumSeatAvailable(16);

        try {
            String confirm1 = ts.reserveSeats(hold1.getReservationId(), hold1.getBookingEmailId());
            Assert.assertEquals(confirm1, String.valueOf(hold1.getReservationId()));
        } catch (TicketingServiceException e) {
            Assert.fail();
        }
    }

    @Test
    public void testHoldAndExpiryReserveError() {
        SeatHold hold1 = findNewSeatHold(6, "surendra.raut@gmail.com");
        testNumSeatAvailable(14);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {}

        try {
            String confirm1 = ts.reserveSeats(hold1.getReservationId(), hold1.getBookingEmailId());
            Assert.fail();
        } catch (TicketingServiceException e) {
            Assert.assertEquals(e.getErrorId(), TicketingServiceError.SEATHOLD_ID_EXPIRED);
        }
    }

    @Test
    public void testHoldDupeEmail() {
        SeatHold hold1 = findNewSeatHold(4, "surendra.raut@gmail.com");
        testNumSeatAvailable(16);

        try {
            SeatHold newHold = ts.findAndHoldSeats(6, "surendra.raut@gmail.com");
            Assert.fail();
        }
        catch(TicketingServiceException e) {
            Assert.assertEquals(e.getErrorId(), TicketingServiceError.CUST_EMAIL_ID_IN_USE);
        }
    }

    @Test
    public void testInvalidNumSeats() {
        try {
            SeatHold newHold = ts.findAndHoldSeats(-5, "surendra.raut@gmail.com");
            Assert.fail();
        }
        catch(TicketingServiceException e) {
            Assert.assertEquals(e.getErrorId(), TicketingServiceError.INVALID_NUM_SEATS);
        }
    }

    @Test
    public void testEmptyEmailId() {
        try {
            SeatHold newHold = ts.findAndHoldSeats(5, null);
            Assert.fail();
        }
        catch(TicketingServiceException e) {
            Assert.assertEquals(e.getErrorId(), TicketingServiceError.EMPTY_EMAIL_ID);
        }
    }

    @Test
    public void testInSufficientSeats() {
        SeatHold hold1 = findNewSeatHold(4, "surendra.raut@gmail.com");
        testNumSeatAvailable(16);

        try {
            SeatHold newHold = ts.findAndHoldSeats(25, "surendra.raut@aol.com");
            Assert.fail();
        }
        catch(TicketingServiceException e) {
            Assert.assertEquals(e.getErrorId(), TicketingServiceError.INSUFFICIENT_SEATS_AVAILABLE);
        }
    }

    @Test
    public void testInvalidSeatHoldId() {
        try {
            String confirm = ts.reserveSeats(99, "surendra.raut@gmail.com");
            Assert.fail();
        } catch (TicketingServiceException e) {
            Assert.assertEquals(e.getErrorId(), TicketingServiceError.INVALID_SEATHOLD_ID);
        }
    }

    @Test
    public void testInvalidEmailId1() {
        SeatHold hold1 = findNewSeatHold(4, "surendra.raut@gmail.com");
        testNumSeatAvailable(16);
        try {
            String confirm = ts.reserveSeats(10001, null);
            Assert.fail();
        } catch (TicketingServiceException e) {
            Assert.assertEquals(e.getErrorId(), TicketingServiceError.INVALID_EMAIL_ID);
        }
    }

    @Test
    public void testInvalidEmailId2() {
        SeatHold hold1 = findNewSeatHold(4, "surendra.raut@gmail.com");
        testNumSeatAvailable(16);
        try {
            String confirm = ts.reserveSeats(10001, "surendra.raut@gmail");
            Assert.fail();
        } catch (TicketingServiceException e) {
            Assert.assertEquals(e.getErrorId(), TicketingServiceError.INVALID_EMAIL_ID);
        }
    }

    @Test
    public void testSeatHoldEmailMismatch() {
        SeatHold hold1 = findNewSeatHold(4, "surendra.raut@gmail.com");
        testNumSeatAvailable(16);
        try {
            String confirm = ts.reserveSeats(10001, "surendra.raut@aol.com");
            Assert.fail();
        } catch (TicketingServiceException e) {
            Assert.assertEquals(e.getErrorId(), TicketingServiceError.SEATHOLD_ID_MATCH_EMAIL_MISMATCH);
        }
    }

    @Test
    public void testAlreadyReserved() {
        SeatHold hold1 = findNewSeatHold(4, "surendra.raut@gmail.com");
        testNumSeatAvailable(16);

        try {
            String confirm = ts.reserveSeats(hold1.getReservationId(), "surendra.raut@gmail.com");
        } catch (TicketingServiceException e) {
            Assert.fail();
        }

        try {
            String confirm = ts.reserveSeats(hold1.getReservationId(), "surendra.raut@gmail.com");
            Assert.fail();
        } catch (TicketingServiceException e) {
            Assert.assertEquals(e.getErrorId(), TicketingServiceError.SEATHOLD_ID_ALREADY_RESERVED);
        }
    }

    @Test
    public void testCancelHoldInvalidHoldId() {
        SeatHold hold1 = findNewSeatHold(4, "surendra.raut@gmail.com");

        try {
            ts.cancelSeatHold(99);
            Assert.fail();
        } catch (TicketingServiceException e) {
            Assert.assertEquals(e.getErrorId(), TicketingServiceError.INVALID_SEATHOLD_ID);
        }
    }

    @Test
    public void testCancelReservedHold() {
        SeatHold hold1 = findNewSeatHold(4, "surendra.raut@gmail.com");

        try {
            String confirm1 = ts.reserveSeats(hold1.getReservationId(), hold1.getBookingEmailId());
            Assert.assertEquals(confirm1, String.valueOf(hold1.getReservationId()));
        } catch (TicketingServiceException e) {
            Assert.fail();
        }

        try {
            ts.cancelSeatHold(hold1.getReservationId());
            Assert.fail();
        } catch (TicketingServiceException e) {
            Assert.assertEquals(e.getErrorId(), TicketingServiceError.SEATHOLD_ID_EXPIRED_OR_RESERVED);
        }
    }

    @Test
    public void testCancelExpiredHold() {
        SeatHold hold1 = findNewSeatHold(4, "surendra.raut@gmail.com");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {}

        try {
            ts.cancelSeatHold(hold1.getReservationId());
            Assert.fail();
        } catch (TicketingServiceException e) {
            Assert.assertEquals(e.getErrorId(), TicketingServiceError.SEATHOLD_ID_EXPIRED_OR_RESERVED);
        }
    }
}
