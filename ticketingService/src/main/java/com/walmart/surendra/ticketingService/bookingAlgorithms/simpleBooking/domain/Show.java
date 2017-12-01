package com.walmart.surendra.ticketingService.bookingAlgorithms.simpleBooking.domain;

import com.walmart.surendra.ticketingService.domain.Seat;
import com.walmart.surendra.ticketingService.domain.SeatHold;
import com.walmart.surendra.ticketingService.domain.SeatPosition;
import com.walmart.surendra.ticketingService.domain.comparators.SeatPositionComparator;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by Surendra Raut on 11/28/2017.
 */
public class Show {

    private static final int ROW_START = 1;

    private static final int COL_START = 1;

    private int maxRows;

    private int maxCols;

    private TreeMap<SeatPosition, Seat> availableSeats;

    private HashMap<Integer, SeatHold> onHoldBooking;

    private HashMap<Integer, SeatHold> reservedBooking;

    public Show(int rows, int cols) {
        this.maxRows = rows;
        this.maxCols = cols;

        availableSeats = new TreeMap<SeatPosition, Seat>(new SeatPositionComparator());
        for(Integer count1 = ROW_START; count1 <= rows; count1++) {
            for(Integer count2 = COL_START; count2 <= cols; count2++) {
                availableSeats.put(new SeatPosition(count1, count2), new Seat(count1, count2));
            }
        }

        onHoldBooking = new HashMap<Integer, SeatHold>();
        reservedBooking = new HashMap<Integer, SeatHold>();
    }

    public TreeMap<SeatPosition, Seat> getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(TreeMap<SeatPosition, Seat> availableSeats) {
        this.availableSeats = availableSeats;
    }

    public HashMap<Integer, SeatHold> getOnHoldBooking() {
        return onHoldBooking;
    }

    public void setOnHoldBooking(HashMap<Integer, SeatHold> onHoldBooking) {
        this.onHoldBooking = onHoldBooking;
    }

    public HashMap<Integer, SeatHold> getReservedBooking() {
        return reservedBooking;
    }

    public void setReservedBooking(HashMap<Integer, SeatHold> reservedBooking) {
        this.reservedBooking = reservedBooking;
    }
}
