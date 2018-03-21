package com.walmart.surendra.ticketingApp.ticketingService.domain;

import java.util.ArrayList;

/**
 * Created by Surendra Raut on 11/25/2017.
 */
public class SeatHold {

    private Integer reservationId;

    private ArrayList<Seat> seatCollection;

    private String bookingEmailId;

    private String confirmationId;

    private Thread expiryTimer;

    public SeatHold(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public ArrayList<Seat> getSeatCollection() {
        return seatCollection;
    }

    public void setSeatCollection(ArrayList<Seat> seatCollection) {
        this.seatCollection = seatCollection;
    }

    public String getBookingEmailId() {
        return bookingEmailId;
    }

    public void setBookingEmailId(String bookingEmailId) {
        this.bookingEmailId = bookingEmailId;
    }

    public String getConfirmationId() {
        return confirmationId;
    }

    public void setConfirmationId(String confirmationId) {
        this.confirmationId = confirmationId;
    }

    public Thread getExpiryTimer() {
        return expiryTimer;
    }

    public void setExpiryTimer(Thread expiryTimer) {
        this.expiryTimer = expiryTimer;
    }
}
