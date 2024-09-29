package com.pfe.backend.domain;

import java.util.Date;

public class Reservation {

    private String userId; // ID of the user making the reservation
    private Date entryDate; // Entry date for the reservation
    private Date exitDate;  // Exit date for the reservation

    public Reservation(String userId, Date entryDate, Date exitDate) {
        this.userId = userId;
        this.entryDate = entryDate;
        this.exitDate = exitDate;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Date getExitDate() {
        return exitDate;
    }

    public void setExitDate(Date exitDate) {
        this.exitDate = exitDate;
    }
}
