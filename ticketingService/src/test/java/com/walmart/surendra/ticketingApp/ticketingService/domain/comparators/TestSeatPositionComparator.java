package com.walmart.surendra.ticketingApp.ticketingService.domain.comparators;

import com.walmart.surendra.ticketingApp.ticketingService.domain.SeatPosition;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Surendra Raut on 11/30/2017.
 */
public class TestSeatPositionComparator {

    @Test
    public void testCompareSame() {
        SeatPositionComparator cmp = new SeatPositionComparator();

        // Both positions are equal
        SeatPosition pos1 = new SeatPosition(5, 6);
        SeatPosition pos2 = new SeatPosition(5, 6);
        Assert.assertEquals(cmp.compare(pos1, pos2), 0);
    }

    @Test
    public void testCompareGreater() {
        SeatPositionComparator cmp = new SeatPositionComparator();

        // First position greater than second
        SeatPosition pos1 = new SeatPosition(5, 6);
        SeatPosition pos2 = new SeatPosition(1, 9);
        Assert.assertEquals(cmp.compare(pos1, pos2), 1);
    }

    @Test
    public void testCompareSmaller() {
        SeatPositionComparator cmp = new SeatPositionComparator();

        // First position smaller than second
        SeatPosition pos1 = new SeatPosition(3,1);
        SeatPosition pos2 = new SeatPosition(4,3);
        Assert.assertEquals(cmp.compare(pos1, pos2), -1);
    }
}
