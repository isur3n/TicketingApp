package com.walmart.surendra.ticketingApp.ticketingService.bookingAlgorithms.simpleBooking.domain;

import com.walmart.surendra.ticketingApp.ticketingService.domain.Seat;
import com.walmart.surendra.ticketingApp.ticketingService.domain.SeatHold;
import com.walmart.surendra.ticketingApp.ticketingService.domain.SeatPosition;
import com.walmart.surendra.ticketingApp.ticketingService.domain.comparators.SeatPositionComparator;
import com.walmart.surendra.ticketingApp.ticketingService.util.PropUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by Surendra Raut on 11/28/2017.
 */
@Service
public class Show {

    private static final int ROW_START = 1;

    private static final int COL_START = 1;

    private int maxRows;

    private int maxCols;

    private TreeMap<SeatPosition, Seat> availableSeats;

    private HashMap<Integer, SeatHold> onHoldBooking;

    private HashMap<Integer, SeatHold> reservedBooking;

    public Show() {
        int rows = Integer.valueOf(PropUtils.getAppPropertyValue(PropUtils.SHOW_ROWS, true));
        int cols = Integer.valueOf(PropUtils.getAppPropertyValue(PropUtils.SHOW_COLS, true));

        availableSeats = new TreeMap<SeatPosition, Seat>(new SeatPositionComparator());
        for(Integer count1 = ROW_START; count1 <= rows; count1++) {
            for(Integer count2 = COL_START; count2 <= cols; count2++) {
                availableSeats.put(new SeatPosition(count1, count2), new Seat(count1, count2));
            }
        }

        onHoldBooking = new HashMap<Integer, SeatHold>();
        reservedBooking = new HashMap<Integer, SeatHold>();
    }

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
