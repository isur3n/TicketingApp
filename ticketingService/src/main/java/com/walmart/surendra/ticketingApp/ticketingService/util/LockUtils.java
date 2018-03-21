package com.walmart.surendra.ticketingApp.ticketingService.util;

import com.walmart.surendra.ticketingApp.ticketingService.enums.TicketingServiceError;
import com.walmart.surendra.ticketingApp.ticketingService.exceptions.TicketingServiceException;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Surendra Raut on 11/30/2017.
 */
public class LockUtils {

    private static final Logger logger = Logger.getLogger(LockUtils.class);

    private static final long LOCK_WAIT_MILLI_SECONDS = Long.valueOf(
            PropUtils.getAppPropertyValue(PropUtils.LOCK_WAIT_MILLI_SECONDS, true));

    private static final ReentrantLock availabilityLock = new ReentrantLock();

    private static final ReentrantLock holdSeatsLock = new ReentrantLock();

    private static final ReentrantLock reservationLock = new ReentrantLock();

    private LockUtils() {
    }

    public static void lockCombo1()
            throws TicketingServiceException {
        logger.debug("Trying to lock Combo1.");
        boolean lockSuccess;

        try{
            lockSuccess = availabilityLock.tryLock(LOCK_WAIT_MILLI_SECONDS, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException e) {
            logger.error("Combo1 locking interrupted.");
            throw new TicketingServiceException(TicketingServiceError.LOCKING_INTERRUPTED);
        }

        if(lockSuccess == false) {
            logger.debug("Combo1 locking timed out.");
            throw new TicketingServiceException(TicketingServiceError.LOCKING_TIMED_OUT);
        }

        logger.debug("Successfully acquired Combo1.");
    }

    public static void unLockCombo1()
            throws TicketingServiceException {
        logger.debug("Trying to unlock Combo1.");
        try {
            availabilityLock.unlock();
        }
        catch(Exception e) {
            logger.fatal("Exception occurred while unlocking hold seats " + e.getClass());
            throw new TicketingServiceException(TicketingServiceError.UNLOCK_FAILED);
        }
    }

    public static void lockCombo2()
            throws TicketingServiceException {
        logger.debug("Trying to lock Combo2.");
        boolean lockSuccess;

        try{
            // Attempt to lock first lock
            lockSuccess = availabilityLock.tryLock(LOCK_WAIT_MILLI_SECONDS, TimeUnit.MILLISECONDS);
            // If lock is successful, proceed to second lock
            if(lockSuccess == true) {
                lockSuccess = holdSeatsLock.tryLock(LOCK_WAIT_MILLI_SECONDS, TimeUnit.MILLISECONDS);
            }
        }
        catch (InterruptedException e) {
            logger.error("Combo2 locking interrupted.");
            throw new TicketingServiceException(TicketingServiceError.LOCKING_INTERRUPTED);
        }

        if(lockSuccess == false) {
            logger.debug("Combo2 locking timed out.");
            throw new TicketingServiceException(TicketingServiceError.LOCKING_TIMED_OUT);
        }

        logger.debug("Successfully acquired Combo2.");
    }

    public static void unLockCombo2()
            throws TicketingServiceException {
        logger.debug("Trying to unlock Combo2.");
        try {
            holdSeatsLock.unlock();
        }
        catch(Exception e) {
            logger.fatal("Exception occurred while unlocking hold seats " + e.getClass());
            throw new TicketingServiceException(TicketingServiceError.UNLOCK_FAILED);
        }
        finally {
            try {
                availabilityLock.unlock();
            }
            catch(Exception e) {
                logger.fatal("Exception occurred while unlocking hold seats " + e.getClass());
                throw new TicketingServiceException(TicketingServiceError.UNLOCK_FAILED);
            }
        }
    }

    public static void lockCombo3()
            throws TicketingServiceException {
        logger.debug("Trying to lock Combo3.");
        boolean lockSuccess;

        try{
            // Attempt to lock first lock
            lockSuccess = holdSeatsLock.tryLock(LOCK_WAIT_MILLI_SECONDS, TimeUnit.MILLISECONDS);
            // If lock is successful, proceed to second lock
            if(lockSuccess == true) {
                lockSuccess = reservationLock.tryLock(LOCK_WAIT_MILLI_SECONDS, TimeUnit.MILLISECONDS);
            }
        }
        catch (InterruptedException e) {
            logger.error("Combo3 locking interrupted.");
            throw new TicketingServiceException(TicketingServiceError.LOCKING_INTERRUPTED);
        }

        if(lockSuccess == false) {
            logger.debug("Combo3 locking attempt timed out.");
            throw new TicketingServiceException(TicketingServiceError.LOCKING_TIMED_OUT);
        }

        logger.debug("Successfully acquired Combo3.");
    }

    public static void unLockCombo3()
            throws TicketingServiceException {
        logger.debug("Trying to unlock Combo3.");
        try {
            reservationLock.unlock();
        }
        catch(Exception e) {
            logger.fatal("Exception occurred while unlocking hold seats " + e.getClass());
            throw new TicketingServiceException(TicketingServiceError.UNLOCK_FAILED);
        }
        finally {
            try {
                holdSeatsLock.unlock();
            }
            catch(Exception e) {
                logger.fatal("Exception occurred while unlocking hold seats " + e.getClass());
                throw new TicketingServiceException(TicketingServiceError.UNLOCK_FAILED);
            }
        }
    }
}
