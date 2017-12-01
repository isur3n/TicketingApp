package com.walmart.surendra.ticketingApp.ticketingAPI.apiRequests;

/**
 * Created by Surendra Raut on 11/30/2017.
 */
public class ReserveSeatsRequest {

    private int seatHoldId;

    private String customerEmail;

    public int getSeatHoldId() {
        return seatHoldId;
    }

    public void setSeatHoldId(int seatHoldId) {
        this.seatHoldId = seatHoldId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
