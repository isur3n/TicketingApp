package com.walmart.surendra.ticketingApp.ticketingAPI.apiRequests;

/**
 * Created by Surendra Raut on 11/30/2017.
 */
public class FindHoldRequest {

    private int numSeats;

    private String customerEmail;

    public int getNumSeats() {
        return numSeats;
    }

    public void setNumSeats(int numSeats) {
        this.numSeats = numSeats;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
