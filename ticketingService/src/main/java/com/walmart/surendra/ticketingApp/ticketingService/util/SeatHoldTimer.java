package com.walmart.surendra.ticketingApp.ticketingService.util;

import com.walmart.surendra.ticketingApp.ticketingService.interfaces.TicketCanceler;
import org.apache.log4j.Logger;

/**
 * Created by Surendra Raut on 11/29/2017.
 */
public class SeatHoldTimer implements Runnable {

    private static final Logger logger = Logger.getLogger(SeatHoldTimer.class);

    private int seatHoldId;

    private static final int expiryMilliSeconds = Integer.valueOf(
            PropUtils.getAppPropertyValue(PropUtils.EXPIRY_MILLI_SECONDS, true));

    private TicketCanceler tc;

    public SeatHoldTimer(int seatHoldId, TicketCanceler tc) {
        this.seatHoldId = seatHoldId;
        this.tc = tc;
    }

    public void run() {
        try {
            Thread.sleep(expiryMilliSeconds);

            /** Call cancel seat hold after timer expiry of course only if thread is not interrupted.
             * It is being done in same block as even if thread is
             * interuupted, we want to ignore it. */
            if(Thread.currentThread().isInterrupted() == false) {
                logger.info("Thread with seatHoldId " + seatHoldId + " expired. Proceeding with cancelSeatHold");
                tc.cancelSeatHold(seatHoldId);
            }
            else {
                logger.info("Thread with seatHoldId " + seatHoldId + " was interrupted. Aborting cancelSeatHold");
            }
        }
        catch(InterruptedException e) {
            /** Do nothing */
            logger.info("Thread with seatHoldId " + seatHoldId + " was interrupted before expiry.");
        }
        catch(Exception e) {
            /** Do nothing */
            logger.info("Thread with seatHoldId " + seatHoldId + "; UnExpected error occured.");
        }
    }
}
