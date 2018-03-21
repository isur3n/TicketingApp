package com.walmart.surendra.ticketingApp.ticketingService.bookingAlgorithms.closeGroupBooking.domain;

import com.walmart.surendra.ticketingApp.ticketingService.domain.Seat;
import com.walmart.surendra.ticketingApp.ticketingService.domain.SeatHold;
import com.walmart.surendra.ticketingApp.ticketingService.domain.SeatPosition;
import com.walmart.surendra.ticketingApp.ticketingService.enums.SeatMovement;
import com.walmart.surendra.ticketingApp.ticketingService.domain.comparators.SeatPositionComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Surendra Raut on 11/25/2017.
 */
public class ShowTime {

    private static final int ROW_START = 1;

    private static final int COL_START = 1;

    private static final int FIRST_RESERVATION_ID = 10000;

    private int maxRows;

    private int maxCols;

    private Integer nextReservationId = FIRST_RESERVATION_ID;

    private TreeMap<SeatPosition, Seat> availableSeats;

    private HashMap<Integer, SeatHold> onHoldBooking;

    private HashMap<Integer, SeatHold> reservedBooking;

    private final ReentrantLock showTimeLock = new ReentrantLock(true);

    public ShowTime(Integer rows, Integer cols) {
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

    public boolean isValidSeat(SeatPosition position) {
        boolean value = false;

        if(position.getRowId() >= ROW_START
                && position.getRowId() <= maxRows
                && position.getColId() >= COL_START
                && position.getColId() <= maxCols) {
            value = true;
        }

        return value;
    }

    public SeatPosition adjacentSeat(SeatPosition position, SeatMovement move) {
        int rowPosition = position.getRowId();
        int colPosition = position.getColId();

        switch (move) {
            case RIGHT:
                colPosition++;
                break;
            case LEFT:
                colPosition--;
                break;
            case FRONT:
                rowPosition--;
                break;
            case BACK:
                rowPosition++;
                break;
        }

        SeatPosition adcPosition = new SeatPosition(rowPosition, colPosition);
        adcPosition = (isValidSeat(adcPosition)) ? adcPosition : null;
        return adcPosition;
    }

    public SeatHold holdBestSeats(int numSeats, String customerEmail) {
        SeatHold holdObj = new SeatHold(getNextReservationId());

        /** Find out available seats at this time */
        int seatsFree = availableSeats.size();

        ArrayList<Seat> seatsToHold = null;

        if(seatsFree == 0 || seatsFree < numSeats) {
            /** Find out if enough seats are available or not
             */
            //ToDo: Throw an exception instead.
            return null;
        }
        else if(seatsFree == numSeats) {
            /** If exact number of seats are available, we need not go through pain of running
             * algorith to find best seat combination. Hence hold all available seats immediately.
             */
            holdObj = new SeatHold(getNextReservationId());
            seatsToHold = new ArrayList<Seat> (availableSeats.values());
            holdObj.setSeatCollection(seatsToHold);
            availableSeats.clear();
        }
        else {
            ArrayList<SeatPosition> positionsToHold = new ArrayList<SeatPosition>();
            int seatsToCheck = seatsFree - numSeats + 1;
            for(Map.Entry<SeatPosition, Seat> freeSeatSet : availableSeats.entrySet()) {
                /**
                 * Add this seat into a list of Seats to be held and check if there are any
                 * adjacent seats that we can hold along with it.
                 */
                positionsToHold.clear();
                recursiveSeatSearch(numSeats, freeSeatSet.getKey(), positionsToHold);
                seatsToCheck--;
                int numHeldSeats = positionsToHold.size();
                if(seatsToCheck < numSeats) {
                    break;
                }
                else if(numHeldSeats == numSeats) {
                    break;
                }
                else {
                    continue;
                }
            }

            seatsToHold = new ArrayList<Seat>();

            if(positionsToHold.size() == numSeats) {
                /**
                 * If we found exact number of seats we were looking for. Simply iterate through
                 * seat positions, remove those seats from available map & move them to list
                 * which later can be added to SeatHold object.
                 */
                for(SeatPosition position : positionsToHold) {
                    Seat seat = availableSeats.remove(position);
                    if(seat == null) {
                        //ToDo: Throw an exception
                    }
                    seatsToHold.add(seat);
                }
                holdObj.setSeatCollection(seatsToHold);
            }
            else {
                /**
                 * We did not find exact number of seats and hence we will pick first "numSeats" seats
                 * and remove those from availability and add in list which can later be added to a
                 * SeatHold object.
                 */
                int seatsHeld = 0;
                for(Map.Entry<SeatPosition, Seat> seatSet : availableSeats.entrySet()) {
                    Seat seat = availableSeats.remove(seatSet.getKey());
                    if(seat == null) {
                        //ToDo: Throw an exception
                    }
                    seatsToHold.add(seat);
                    if(++seatsHeld == numSeats) {
                        break;
                    }
                }
            }
        }

        if(seatsToHold.size() <= 0) {
            //ToDo: Throw an exception
            return null;
        }

        holdObj.setSeatCollection(seatsToHold);
        return holdObj;
    }

    //ToDo: Investigate if added Seat needs to be removed from available seats to avoid loop. Ideally it should not.
    private void recursiveSeatSearch(int numSeats, SeatPosition currentPos, ArrayList<SeatPosition> positionsToHold) {
        positionsToHold.add(currentPos);

        /* Now, find out if any adjacent seat is free */
        //ToDo: Add condition to check >= condition and throw exception
        if(numSeats == positionsToHold.size()) {
            return;
        }
        SeatPosition rightSeat = adjacentSeat(currentPos, SeatMovement.RIGHT);
        if(rightSeat != null && availableSeats.containsKey(rightSeat) == true
                && positionsToHold.contains(rightSeat) != true) {
            recursiveSeatSearch(numSeats, rightSeat, positionsToHold);
        }

        //ToDo: Add condition to check >= condition and throw exception
        if(numSeats == positionsToHold.size()) {
            return;
        }
        SeatPosition leftSeat = adjacentSeat(currentPos, SeatMovement.LEFT);
        if(leftSeat != null && availableSeats.containsKey(leftSeat) == true
                && positionsToHold.contains(leftSeat) != true) {
            recursiveSeatSearch(numSeats, leftSeat, positionsToHold);
        }

        //ToDo: Add condition to check >= condition and throw exception
        if(numSeats == positionsToHold.size()) {
            return;
        }
        SeatPosition frontSeat = adjacentSeat(currentPos, SeatMovement.FRONT);
        if(frontSeat != null && availableSeats.containsKey(frontSeat) == true
                && positionsToHold.contains(frontSeat) != true) {
            recursiveSeatSearch(numSeats, frontSeat, positionsToHold);
        }

        //ToDo: Add condition to check >= condition and throw exception
        if(numSeats == positionsToHold.size()) {
            return;
        }
        SeatPosition backSeat = adjacentSeat(currentPos, SeatMovement.BACK);
        if(backSeat != null && availableSeats.containsKey(backSeat) == true
                && positionsToHold.contains(backSeat) != true) {
            recursiveSeatSearch(numSeats, backSeat, positionsToHold);
        }
    }

    public Integer getNextReservationId() {
        return nextReservationId++;
    }

    public TreeMap<SeatPosition, Seat> getAvailableSeats() {
        return availableSeats;
    }

    public HashMap<Integer, SeatHold> getOnHoldBooking() {
        return onHoldBooking;
    }

    public HashMap<Integer, SeatHold> getReservedBooking() {
        return reservedBooking;
    }
}
