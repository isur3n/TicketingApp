package com.walmart.surendra.ticketingService.bookingAlgorithms.simpleBooking.service;

import com.walmart.surendra.ticketingService.bookingAlgorithms.simpleBooking.domain.Show;
import com.walmart.surendra.ticketingService.domain.*;
import com.walmart.surendra.ticketingService.enums.TicketingServiceError;
import com.walmart.surendra.ticketingService.exceptions.TicketingServiceException;
import com.walmart.surendra.ticketingService.interfaces.TicketService;
import com.walmart.surendra.ticketingService.interfaces.TicketCanceler;
import com.walmart.surendra.ticketingService.util.EmailUtil;
import com.walmart.surendra.ticketingService.util.LockUtils;
import com.walmart.surendra.ticketingService.util.PropUtils;
import com.walmart.surendra.ticketingService.util.SeatHoldTimer;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Surendra Raut on 11/26/2017.
 *
 * This ticketing service implements simplest ticket booking applications
 */
public class TicketServiceImpl implements TicketService, TicketCanceler {

    private static final Logger logger = Logger.getLogger(TicketServiceImpl.class);

    private static final int RESERVATION_ID_SEQ_START = Integer.valueOf(
            PropUtils.getAppPropertyValue(PropUtils.RESERVATION_ID_SEQ_START, true));

    private Integer nextReservationId = RESERVATION_ID_SEQ_START;

    private Show show;

    public TicketServiceImpl(Show show) {
        this.show = show;
    }

    /**
     * The number of seats in the venue that are neither held nor reserved
     *
     * @return the number of tickets available in the venue
     */
    public int numSeatsAvailable()
            throws TicketingServiceException {
        int seatsAvailable = -1;

        LockUtils.lockCombo1();
        seatsAvailable = show.getAvailableSeats().size();
        LockUtils.unLockCombo1();

        return seatsAvailable;
    }

    /**
     * Find and hold then best available seats for a customer
     *
     * @param numSeats the number of seats to find and hold
     * @param customerEmail unique identifier for the customer
     * @return a SeatHold object identifying the specific seats and related
    information
     */
    public SeatHold findAndHoldSeats(int numSeats, String customerEmail)
            throws TicketingServiceException {
        logger.info("Requested seats to hold " + numSeats + " Customer email id " + customerEmail);
        /* Perform priliminary validation of provided data. */
        if(numSeats <= 0) {
            logger.error("Invalid number of seats selected " + numSeats);
            throw new TicketingServiceException(TicketingServiceError.INVALID_NUM_SEATS);
        }
        else if(customerEmail == null || "".equals(customerEmail)) {
            logger.error("Empty customer email id.");
            throw new TicketingServiceException(TicketingServiceError.EMPTY_EMAIL_ID);
        }
        else if(EmailUtil.isEmailIdValid(customerEmail) == false) {
            logger.error("Invalid customer email id.");
            throw new TicketingServiceException(TicketingServiceError.INVALID_EMAIL_ID);
        }

        SeatHold newHoldReservation = null;

        // First try to acquire locks
        LockUtils.lockCombo2();

        try {
            /* Perform request validation based on given data. */
            int availableSeatCount = show.getAvailableSeats().size();
            if (availableSeatCount < numSeats) {
                logger.error("Insufficient seats available. Available " + availableSeatCount + ", Requested " + numSeats);
                throw new TicketingServiceException(TicketingServiceError.INSUFFICIENT_SEATS_AVAILABLE);
            }

            /* Make sure customer email id has not been used in prior holds or reservations */
            if (isCustomerEmailInUse(customerEmail) == true) {
                logger.error("Customer email id already in use " + customerEmail);
                throw new TicketingServiceException(TicketingServiceError.CUST_EMAIL_ID_IN_USE);
            }

            /* Since all validations are passed, let's book tickets */
            int seatsToBook = numSeats;
            ArrayList<Seat> onHoldSeats = new ArrayList<Seat>();
            TreeMap<SeatPosition, Seat> availableSeats = show.getAvailableSeats();
            Iterator<Map.Entry<SeatPosition, Seat>> iterator = availableSeats.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<SeatPosition, Seat> ent = iterator.next();
                Seat seat = ent.getValue();
                if (seat == null) {
                    logger.error("Empty seat found in available seats pool " + ent.getValue().toString());
                    throw new TicketingServiceException(TicketingServiceError.UNEXPECTED);
                }

                onHoldSeats.add(seat);
                iterator.remove();

                if (--seatsToBook <= 0) {
                    break;
                }
            }

            /* Let's create a SeahHold object and make sure it has all info added to it. */
            newHoldReservation = createSeatHold();
            newHoldReservation.setBookingEmailId(customerEmail);
            newHoldReservation.setSeatCollection(onHoldSeats);
            /* Also add it to OnHoldList */
            show.getOnHoldBooking().put(newHoldReservation.getReservationId(), newHoldReservation);

            /* Start expiry timer which will cancel hold after few seconds */
            Thread holdExpiry = new Thread(new SeatHoldTimer(newHoldReservation.getReservationId(), this));
            newHoldReservation.setExpiryTimer(holdExpiry);
            holdExpiry.start();
        }
        catch(TicketingServiceException e) {
            throw e;
        }
        catch(Exception e) {
            logger.error("Unexpected exception occurred " + e.getMessage());
            throw new TicketingServiceException(TicketingServiceError.UNEXPECTED);
        }
        finally {
            LockUtils.unLockCombo2();
        }

        logger.info("Success! SeatHold ID = " + newHoldReservation.getReservationId());
        return newHoldReservation;
    }

    /**
     * Commit seats held for a specific customer
     *
     * @param seatHoldId the seat hold identifier
     * @param customerEmail the email address of the customer to which the seat hold is assigned
     * @return a reservation confirmation code
     */
    public String reserveSeats(int seatHoldId, String customerEmail)
            throws TicketingServiceException {
        logger.info("Reservation started for SeatHold id " + seatHoldId + " Customer Email " + customerEmail);

        /* Make sure mentioned seatHoldId is a valid id */
        if(seatHoldId <= RESERVATION_ID_SEQ_START || seatHoldId > nextReservationId) {
            /* Invalid reservation ID; saved trouble of traversing through 2 maps */
            logger.warn("Invalid seatHoldId [" + seatHoldId + "]");
            throw new TicketingServiceException(TicketingServiceError.INVALID_SEATHOLD_ID);
        } else if (EmailUtil.isEmailIdValid(customerEmail) == false) {
            /* Invalid email ID; Add a warning / throw an exception */
            logger.warn("Invalid customerEmail [" + customerEmail + "]");
            throw new TicketingServiceException(TicketingServiceError.INVALID_EMAIL_ID);
        }

        String confirmationId = null;
        LockUtils.lockCombo3();

        try {
            /* Since ID is valid, look it up in OnHold set */
            SeatHold heldReservation = show.getOnHoldBooking().get(seatHoldId);

            if (heldReservation != null) {
                /* Validate email id from seahHold with customer provided email id */
                if (customerEmail.equals(heldReservation.getBookingEmailId()) == false) {
                    /** email IDs don't match, throw an exception */
                    logger.error("SeatHold ID matched but Email ID mismatched.");
                    throw new TicketingServiceException(TicketingServiceError.SEATHOLD_ID_MATCH_EMAIL_MISMATCH);
                }

                /* First thing to do is cancel expiry times of the thread */
                heldReservation.getExpiryTimer().interrupt();
                logger.info("SeatHoldId " + seatHoldId + " expiry time has been cancelled");

                /* Valid object found & so, let's prepare for confirming this reservation. */
                show.getReservedBooking().put(seatHoldId, heldReservation);
                show.getOnHoldBooking().remove(seatHoldId);

                //ToDo: Change confirmation ID to base32 encoded as it is widely used.
                confirmationId = Integer.toString(seatHoldId);
                heldReservation.setConfirmationId(confirmationId);
            } else if (show.getReservedBooking().containsKey(seatHoldId)) {
                logger.error("SeatHoldId " + seatHoldId + " is already reserved");
                throw new TicketingServiceException(TicketingServiceError.SEATHOLD_ID_ALREADY_RESERVED);
            } else {
                logger.info("SeatHoldId " + seatHoldId + " has expired.");
                throw new TicketingServiceException(TicketingServiceError.SEATHOLD_ID_EXPIRED);
            }
        }
        catch(TicketingServiceException e) {
            throw e;
        }
        catch(Exception e) {
            logger.error("Unexpected exception occurred " + e.getMessage());
            throw new TicketingServiceException(TicketingServiceError.UNEXPECTED);
        }
        finally {
            LockUtils.unLockCombo3();
        }

        logger.info("Confirmation ID " + confirmationId + " has been reserved.");
        return confirmationId;
    }

    /**
     * Checks if customerEmail address is already in use. It will first check it in onHold reservations.
     * If it does not find it there, then it will proceed to check same in confirmed reservations.
     * Anytime it finds email id in use, it return true value. If not, it will return false.
     *
     * @param customerEmail Customer Email ID to be used for reservation
     * @return boolean value indicating email id already in use or not.
     */
    public boolean isCustomerEmailInUse(String customerEmail)
            throws TicketingServiceException {
        if(customerEmail == null || "".equals(customerEmail)) {
            logger.error("Empty customer email id.");
            throw new TicketingServiceException(TicketingServiceError.EMPTY_EMAIL_ID);
        }
        boolean emailFound = (findEmailIdInList(show.getOnHoldBooking().values(), customerEmail))
                ? true
                : findEmailIdInList(show.getReservedBooking().values(), customerEmail);
        return emailFound;
    }

    /**
     * Checks if customerEmail is present in any of SeatHold object present in the Collection object passed.
     *
     * @param list Collection of SeatHold objects.
     * @param customerEmail Customer Email ID to be used for reservation
     * @return boolean value indicating email id already in use or not.
     */
    private boolean findEmailIdInList(Collection<SeatHold> list, String customerEmail) {
        for(SeatHold sh : list) {
            if(sh == null) {
                continue;
            }
            if(sh.getBookingEmailId().equals(customerEmail) == true) {
                return true;
            }
        }
        return false;
    }

    /**
     * Create a SeatHold object by making sure a valid & unique reservationId is assigned.
     *
     * @return new SeatHold object
     */
    private SeatHold createSeatHold() {
        return new SeatHold(++nextReservationId);
    }

    public void cancelSeatHold(int seatHoldId)
            throws TicketingServiceException {
        logger.info("Cancellation of seatHoldId " + seatHoldId + " has begun");

        /* Make sure mentioned seatHoldId is a valid id */
        if(seatHoldId <= RESERVATION_ID_SEQ_START || seatHoldId > nextReservationId) {
            /* Invalid reservation ID; saved trouble of traversing through 2 maps */
            logger.warn("Invalid seatHoldId [" + seatHoldId + "]");
            throw new TicketingServiceException(TicketingServiceError.INVALID_SEATHOLD_ID);
        }

        LockUtils.lockCombo2();

        try {
            /* Check if seatHoldId still exists in hold list. */
            SeatHold heldSeats = show.getOnHoldBooking().remove(seatHoldId);
            if(heldSeats == null) {
                // It means seatHold has been expired already or has been confirmed; Let's find out.
                logger.info("SeatHold is either expired or reserved already");
                throw new TicketingServiceException(TicketingServiceError.SEATHOLD_ID_EXPIRED_OR_RESERVED);
            }

            TreeMap<SeatPosition, Seat> availableSeats = show.getAvailableSeats();
            // Iterate through seats held & add those back in available seats list
            for(Seat seat : heldSeats.getSeatCollection()) {
                availableSeats.put(seat.getPosition(), seat);
            }
        }
        catch(TicketingServiceException e) {
            throw e;
        }
        catch(Exception e) {
            logger.error("Unexpected exception occurred " + e.getMessage());
            throw new TicketingServiceException(TicketingServiceError.UNEXPECTED);
        }
        finally {
            LockUtils.unLockCombo2();
        }
    }
}