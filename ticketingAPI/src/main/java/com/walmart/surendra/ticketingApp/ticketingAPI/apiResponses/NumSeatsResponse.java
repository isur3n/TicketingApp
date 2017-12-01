package com.walmart.surendra.ticketingApp.ticketingAPI.apiResponses;

/**
 * Created by Surendra Raut on 11/30/2017.
 */
public class NumSeatsResponse {

    int numSeats;

    String errorMessage;

    public int getNumSeats() {
        return numSeats;
    }

    public void setNumSeats(int numSeats) {
        this.numSeats = numSeats;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
