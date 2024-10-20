package com.pfe.backend;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pfe.backend.controller.RatingController;
import com.pfe.backend.domain.Rating;
import com.pfe.backend.service.impl.RatingServiceImpl;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RatingControllerTest {

    @Mock
    private RatingServiceImpl ratingService;

    @InjectMocks
    private RatingController ratingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test addRating
    @Test
    public void testAddRating_Success() {
        // Mock Rating object
        Rating mockRating = mock(Rating.class);

        // Mock the ratingService.addRating() method
        when(ratingService.addRating(anyString(), anyString(), anyInt())).thenReturn(mockRating);

        // Create a mock request object
        Rating request = new Rating("1", "user1", "handyman1", 5);

        // Call the controller method
        ResponseEntity<Rating> response = ratingController.addRating(request);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRating, response.getBody());
    }

    // Test updateRating
    @Test
    public void testUpdateRating_Success() {
        // Mock Rating object
        Rating mockRating = mock(Rating.class);

        // Mock the ratingService.updateRating() method
        when(ratingService.updateRating(anyString(), anyString(), anyInt())).thenReturn(mockRating);

        // Create a mock request object
        Rating request = new Rating("1", "user1", "handyman1", 4);

        // Call the controller method
        ResponseEntity<Rating> response = ratingController.updateRating(request);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRating, response.getBody());
    }

    // Test deleteRating
    @Test
    public void testDeleteRating_Success() {
        // Call the controller method
        Rating request = new Rating("1", "user1", "handyman1", 5);

        // Mock the service method to do nothing
        doNothing().when(ratingService).deleteRating(anyString(), anyString());

        // Call the controller method
        ResponseEntity<Void> response = ratingController.deleteRating(request);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        // Verify the service method was called
        verify(ratingService, times(1)).deleteRating(anyString(), anyString());
    }

    // Test getRating
    @Test
    public void testGetRating_Success() {
        // Mock the ratingService.getRating() method to return an integer value
        when(ratingService.getRating(anyString(), anyString())).thenReturn(5);

        // Call the controller method
        ResponseEntity<Integer> response = ratingController.getRating("user1", "handyman1");

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody());
    }

    // Test getOverallRating
    @Test
    public void testGetOverallRating_Success() {
        // Mock the ratingService.getOverallRating() method to return a double value
        when(ratingService.getOverallRating(anyString())).thenReturn(4.5);

        // Call the controller method
        ResponseEntity<Double> response = ratingController.getOverallRating("handyman1");

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(4.5, response.getBody());
    }
}
