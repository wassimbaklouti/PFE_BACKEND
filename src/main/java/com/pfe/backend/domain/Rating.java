package com.pfe.backend.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Document(collection = "ratings")
public class Rating {
    @Id
    private String id;

    private String username;          // The user who rates
    private String handymanUsername;  // The handyman being rated
    private int rate;                 // The rating (e.g., 1-5 stars)

    // Getters and Setters

    public Rating(String id, String username, String handymanUsername, int rate) {
        this.id = id;
        this.username = username;
        this.handymanUsername = handymanUsername;
        this.rate = rate;
    }

    public Rating() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHandymanUsername() {
        return handymanUsername;
    }

    public void setHandymanUsername(String handymanUsername) {
        this.handymanUsername = handymanUsername;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}

