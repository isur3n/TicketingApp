package com.walmart.surendra.ticketingApp.ticketingService.domain;

/**
 * Created by Surendra Raut on 11/25/2017.
 */
public class Seat {

    SeatPosition position;

    public Seat(int rowId, int colId) {
        this.position = new SeatPosition(rowId, colId);
    }

    public Seat(SeatPosition position) {
        this.position = position;
    }

    public SeatPosition getPosition() {
        return position;
    }

    public void setPosition(SeatPosition position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Seat seat = (Seat) o;

        return position != null ? position.equals(seat.position) : seat.position == null;
    }

    @Override
    public int hashCode() {
        return position != null ? position.hashCode() : 0;
    }
}
