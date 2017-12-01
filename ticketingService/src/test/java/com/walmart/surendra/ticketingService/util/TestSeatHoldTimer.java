package com.walmart.surendra.ticketingService.util;

import com.walmart.surendra.ticketingService.exceptions.TicketingServiceException;
import com.walmart.surendra.ticketingService.interfaces.TicketCanceler;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Surendra Raut on 11/30/2017.
 */
public class TestSeatHoldTimer implements TicketCanceler {

    private static final int expiryMilliSeconds = Integer.valueOf(
            PropUtils.getAppPropertyValue(PropUtils.EXPIRY_MILLI_SECONDS, true));

    private int seatHoldId = -1;

    public void cancelSeatHold(int seatHoldId) throws TicketingServiceException {
        this.seatHoldId = seatHoldId;
    }

    @Test
    public void testTimerExpiry() {
        int dummySeatHoldId = 10001;
        SeatHoldTimer sTimer = new SeatHoldTimer(dummySeatHoldId, this);
        Thread timerThread = new Thread(sTimer);
        timerThread.start();

        try {
            Thread.sleep(expiryMilliSeconds + 1000);
        } catch (InterruptedException e) {}

        Assert.assertEquals(seatHoldId, dummySeatHoldId);
    }

    @Test
    public void testTimerCancel() {
        int dummySeatHoldId = 10002;
        SeatHoldTimer sTimer = new SeatHoldTimer(dummySeatHoldId, this);
        Thread timerThread = new Thread(sTimer);
        timerThread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        timerThread.interrupt();

        try {
            timerThread.join();
        } catch (InterruptedException e) {}

        Assert.assertEquals(seatHoldId, -1);
    }
}
