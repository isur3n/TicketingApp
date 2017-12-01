package com.walmart.surendra.ticketingService.util;

import com.walmart.surendra.ticketingService.enums.TicketingServiceError;
import com.walmart.surendra.ticketingService.exceptions.TicketingServiceException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Surendra Raut on 11/30/2017.
 */
public class TestLockUtils {

    @Test
    public void testOnlyUnlockCombo1() {
        try {
            LockUtils.unLockCombo1();
        } catch (TicketingServiceException e) {
            Assert.assertEquals(e.getErrorId(), TicketingServiceError.UNLOCK_FAILED);
        }
    }

    @Test
    public void testOnlyUnlockCombo2() {
        try {
            LockUtils.unLockCombo2();
        } catch (TicketingServiceException e) {
            Assert.assertEquals(e.getErrorId(), TicketingServiceError.UNLOCK_FAILED);
        }
    }

    @Test
    public void testOnlyUnlockCombo3() {
        try {
            LockUtils.unLockCombo3();
        } catch (TicketingServiceException e) {
            Assert.assertEquals(e.getErrorId(), TicketingServiceError.UNLOCK_FAILED);
        }
    }

    @Test
    public void testLockUnlockCombo1() {
        try {
            LockUtils.lockCombo1();
        }
        catch (TicketingServiceException e) {
        }

        try {
            LockUtils.unLockCombo1();
        } catch (TicketingServiceException e) {
            Assert.fail();
        }
    }

    @Test
    public void testLockUnlockCombo2() {
        try {
            LockUtils.lockCombo2();
        }
        catch (TicketingServiceException e) {
        }

        try {
            LockUtils.unLockCombo2();
        } catch (TicketingServiceException e) {
            Assert.fail();
        }
    }

    @Test
    public void testLockUnlockCombo3() {
        try {
            LockUtils.lockCombo3();
        }
        catch (TicketingServiceException e) {
        }

        try {
            LockUtils.unLockCombo3();
        } catch (TicketingServiceException e) {
            Assert.fail();
        }
    }

    @Test
    public void testWhileCombo1Locked() {
        try {
            LockUtils.lockCombo1();
        }
        catch (TicketingServiceException e) {
        }

        // Now try to order a lock on Combo2
        try {
            LockUtils.lockCombo2();
        } catch (TicketingServiceException e) {
            Assert.assertEquals(e.getErrorId(), TicketingServiceError.UNLOCK_FAILED);
        }

        // Now try to order a lock on Combo2
        try {
            LockUtils.lockCombo3();
        } catch (TicketingServiceException e) {
            Assert.fail();
        }

        // Now try to order a lock on Combo2
        try {
            LockUtils.unLockCombo3();
        } catch (TicketingServiceException e) {
            Assert.fail();
        }

        try {
            LockUtils.unLockCombo1();
        }
        catch (TicketingServiceException e) {
        }
    }

    @Test
    public void testWhileCombo2Locked() {
        try {
            LockUtils.lockCombo2();
        }
        catch (TicketingServiceException e) {
        }

        // Now try to order a lock on Combo2
        try {
            LockUtils.lockCombo1();
        } catch (TicketingServiceException e) {
            Assert.assertEquals(e.getErrorId(), TicketingServiceError.LOCKING_TIMED_OUT);
        }

        // Now try to order a lock on Combo2
        try {
            LockUtils.lockCombo3();
        } catch (TicketingServiceException e) {
            Assert.fail();
        }

        // Now try to order a lock on Combo2
        try {
            LockUtils.unLockCombo3();
        } catch (TicketingServiceException e) {
            Assert.assertEquals(e.getErrorId(), TicketingServiceError.LOCKING_TIMED_OUT);
        }

        try {
            LockUtils.unLockCombo2();
        }
        catch (TicketingServiceException e) {
        }
    }

    @Test
    public void testWhileCombo3Locked() {
        try {
            LockUtils.lockCombo3();
        }
        catch (TicketingServiceException e) {
        }

        // Now try to order a lock on Combo2
        try {
            LockUtils.lockCombo1();
        } catch (TicketingServiceException e) {
            Assert.fail();
        }

        // Now try to order a lock on Combo2
        try {
            LockUtils.lockCombo2();
        } catch (TicketingServiceException e) {
            Assert.assertEquals(e.getErrorId(), TicketingServiceError.LOCKING_TIMED_OUT);
        }

        try {
            LockUtils.unLockCombo1();
        }
        catch (TicketingServiceException e) {
        }

        try {
            LockUtils.unLockCombo3();
        } catch (TicketingServiceException e) {
        }
    }
}
