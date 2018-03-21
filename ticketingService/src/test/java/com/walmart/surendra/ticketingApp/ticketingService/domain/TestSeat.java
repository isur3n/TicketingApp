package com.walmart.surendra.ticketingApp.ticketingService.domain;

import com.walmart.surendra.ticketingApp.ticketingService.util.PropUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Surendra Raut on 11/30/2017.
 */
public class TestSeat {

    @Test
    public void testEqualsValid() {
        Seat seat1 = new Seat(4,5);
        Seat seat2 = new Seat(0,0);
        seat2.setPosition(new SeatPosition(4, 5));
        Assert.assertEquals(seat1.equals(seat2), true);
    }

    @Test
    public void testEqualsInvalid() {
        Seat seat1 = new Seat(4,5);
        Seat seat2 = new Seat(3,2);
        Assert.assertEquals(seat1.equals(seat2), false);
    }

    @Test
    public void testHashCodeValid() {
        int row = 4;
        int col = 5;
        int hasCode = row * Integer.valueOf(PropUtils.getAppPropertyValue(PropUtils.SEAT_ROW_MULTIPLIER, true)) + col;
        Seat seat1 = new Seat(row, col);
        Assert.assertEquals(seat1.hashCode(), hasCode);
    }

    @Test
    public void testHashCodeInvalid() {
        int row = 4;
        int col = 5;
        int hasCode = 99;
        Seat seat1 = new Seat(row, col);
        Assert.assertNotEquals(seat1.hashCode(), hasCode);
    }
}
