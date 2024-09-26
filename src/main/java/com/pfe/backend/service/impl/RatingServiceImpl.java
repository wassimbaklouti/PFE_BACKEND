package com.pfe.backend.service.impl;


import com.pfe.backend.domain.Rating;
import com.pfe.backend.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl {
    @Autowired
    private RatingRepository ratingRepository;

    public Rating addRating(String username, String handymanUsername, int rate) {
        Optional<Rating> existingRating = ratingRepository.findByUsernameAndHandymanUsername(username, handymanUsername);
        if (existingRating.isPresent()) {
            throw new RuntimeException("User has already rated this handyman.");
        }
        Rating rating = new Rating();
        rating.setUsername(username);
        rating.setHandymanUsername(handymanUsername);
        rating.setRate(rate);
        return ratingRepository.save(rating);
    }

    public Rating updateRating(String username, String handymanUsername, int newRate) {
        Rating rating = ratingRepository.findByUsernameAndHandymanUsername(username, handymanUsername)
                .orElseThrow(() -> new RuntimeException("Rating not found."));
        rating.setRate(newRate);
        return ratingRepository.save(rating);
    }

    public void deleteRating(String username, String handymanUsername) {
        Rating rating = ratingRepository.findByUsernameAndHandymanUsername(username, handymanUsername)
                .orElseThrow(() -> new RuntimeException("Rating not found."));
        ratingRepository.delete(rating);
    }

    public int getRating(String username, String handymanUsername) {
        Rating rating = ratingRepository.findByUsernameAndHandymanUsername(username, handymanUsername)
                .orElseThrow(() -> new RuntimeException("Rating not found."));
        return rating.getRate();
    }

    public double getOverallRating(String handymanUsername) {
        List<Rating> ratings = ratingRepository.findByHandymanUsername(handymanUsername);
        if (ratings.isEmpty()) {
            return 0.0; // Return 0 if there are no ratings
        }

        double total = ratings.stream().mapToInt(Rating::getRate).sum();
        return total / ratings.size(); // Calculate the average rating
    }
}
