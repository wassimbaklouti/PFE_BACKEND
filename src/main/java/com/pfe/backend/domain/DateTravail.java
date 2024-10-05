package com.pfe.backend.domain;

import java.time.LocalTime;

public class DateTravail {
    private LocalTime dateDeb;
    private LocalTime dateFin;

    public DateTravail(LocalTime dateDeb, LocalTime dateFin) {
        this.dateDeb = dateDeb;
        this.dateFin = dateFin;
    }

    // Getters and setters
    public LocalTime getDateDeb() {
        return dateDeb;
    }

    public void setDateDeb(LocalTime dateDeb) {
        this.dateDeb = dateDeb;
    }

    public LocalTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalTime dateFin) {
        this.dateFin = dateFin;
    }
}

