package com.walmart.surendra.ticketingService.domain.comparators;

import com.walmart.surendra.ticketingService.domain.SeatPosition;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Surendra Raut on 11/26/2017.
 */
public class SeatPositionComparator implements Comparator<SeatPosition>, Serializable {

    public int compare(SeatPosition pos1, SeatPosition pos2) {
        int value = 0;

        if(pos1.getRowId() > pos2.getRowId()) {
            value = 1;
        }
        else if(pos1.getRowId() < pos2.getRowId()) {
            value = -1;
        }
        else if(pos1.getColId() > pos2.getColId()) {
            value = 1;
        }
        else if(pos1.getColId() < pos2.getColId()) {
            value = -1;
        }

        return value;
    }
}
