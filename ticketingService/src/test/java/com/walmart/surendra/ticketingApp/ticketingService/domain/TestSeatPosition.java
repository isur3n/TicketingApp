package com.walmart.surendra.ticketingApp.ticketingService.domain;

import com.walmart.surendra.ticketingApp.ticketingService.util.PropUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Surendra Raut on 11/30/2017.
 */
public class TestSeatPosition {

    @Test
    public void testEqualsValid() {
        SeatPosition pos1 = new SeatPosition(2,6);
        SeatPosition pos2 = new SeatPosition(8,9);
        pos2.setRowId(2);
        pos2.setColId(6);
        Assert.assertEquals(pos1.equals(pos2), true);
    }

    @Test
    public void testEqualsInalid() {
        SeatPosition pos1 = new SeatPosition(2,6);
        SeatPosition pos2 = new SeatPosition(8,9);
        Assert.assertEquals(pos1.equals(pos2), false);
    }

    @Test
    public void testHashCodeValid() {
        int row = 6;
        int col = 7;
        int hashCode = row * Integer.valueOf(PropUtils.getAppPropertyValue(PropUtils.SEAT_ROW_MULTIPLIER, true)) + col;
        SeatPosition pos = new SeatPosition(row, col);
        Assert.assertEquals(pos.hashCode(), hashCode);
    }

    @Test
    public void testHashCodeInvalid() {
        int row = 6;
        int col = 7;
        int hashCode = 101;
        SeatPosition pos = new SeatPosition(row, col);
        Assert.assertNotEquals(pos.hashCode(), hashCode);
    }
}
