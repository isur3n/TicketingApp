package com.walmart.surendra.ticketingApp.ticketingAPI.apiResponses;

/**
 * Created by Surendra Raut on 11/30/2017.
 */
public class FindHoldResponse {

    private int seatHoldId;

    private String errorMessage;

    public int getSeatHoldId() {
        return seatHoldId;
    }

    public void setSeatHoldId(int seatHoldId) {
        this.seatHoldId = seatHoldId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
