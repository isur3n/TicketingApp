package com.walmart.surendra.ticketingApp.ticketingService.domain;

import com.walmart.surendra.ticketingApp.ticketingService.util.PropUtils;

/**
 * Created by Surendra Raut on 11/26/2017.
 */
public class SeatPosition {

    private static final int SEAT_ROW_MULTIPLIER =  Integer.valueOf(
            PropUtils.getAppPropertyValue(PropUtils.SEAT_ROW_MULTIPLIER, true));

    private int rowId;

    private int colId;

    public SeatPosition(int rowId, int colId) {
        this.rowId = rowId;
        this.colId = colId;
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public int getColId() {
        return colId;
    }

    public void setColId(int colId) {
        this.colId = colId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        SeatPosition position = (SeatPosition) o;
        if (rowId != position.rowId) {
            return false;
        }
        return colId == position.colId;
    }

    @Override
    public int hashCode() {
        return rowId * SEAT_ROW_MULTIPLIER + colId;
    }
}
