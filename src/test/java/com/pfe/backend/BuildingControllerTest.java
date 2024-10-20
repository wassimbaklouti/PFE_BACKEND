package com.pfe.backend;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.pfe.backend.controller.BuildingController;
import com.pfe.backend.domain.Building;
import com.pfe.backend.domain.Reservation;
import com.pfe.backend.service.BuildingService;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuildingControllerTest {

    @InjectMocks
    private BuildingController buildingController;

    @Mock
    private BuildingService buildingService;

    @Mock
    private Building mockBuilding;

    @Mock
    private MultipartFile mockImageFile;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // Test for createBuilding
    @Test
    public void testCreateBuilding_Success() throws Exception {
        when(buildingService.createBuilding(anyString(), anyString(), anyInt(), anyDouble(), anyDouble(), anyString(), eq(mockImageFile), anyString()))
            .thenReturn(mockBuilding);

        ResponseEntity<Building> response = buildingController.createBuilding(
            "maison", "123 Street", 5, 500000, 2500.50, "Paris", "owner1", mockImageFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBuilding, response.getBody());
    }

    // Test for getBuildingById
    @Test
    public void testGetBuildingById_Success() {
        when(buildingService.getBuildingById(anyString())).thenReturn(mockBuilding);

        ResponseEntity<Building> response = buildingController.getBuildingById("building1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBuilding, response.getBody());
    }

    // Test for getAllBuildings
    @Test
    public void testGetAllBuildings_Success() {
        List<Building> mockBuildingList = Arrays.asList(mockBuilding);
        when(buildingService.getAllBuildings()).thenReturn(mockBuildingList);

        ResponseEntity<List<Building>> response = buildingController.getAllBuildings();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBuildingList, response.getBody());
    }

    // Test for updateBuilding
    @Test
    public void testUpdateBuilding_Success() throws Exception {
        when(buildingService.updateBuilding(anyString(), anyString(), anyString(), anyInt(), anyDouble(), anyDouble(), anyString(), eq(mockImageFile), anyString()))
            .thenReturn(mockBuilding);

        ResponseEntity<Building> response = buildingController.updateBuilding(
            "building1", "maison", "123 Street", 5, 500000, 2500.50, "Paris", "owner1", mockImageFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBuilding, response.getBody());
    }

    // Test for deleteBuilding
    @Test
    public void testDeleteBuilding_Success() {
        ResponseEntity<Void> response = buildingController.deleteBuilding("building1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    // Test for getBuildingsByAddress
    @Test
    public void testGetBuildingsByAddress_Success() {
        List<Building> mockBuildingList = Arrays.asList(mockBuilding);
        when(buildingService.getBuildingsByAddress(anyString())).thenReturn(mockBuildingList);

        ResponseEntity<List<Building>> response = buildingController.getBuildingsByAddress("123 Street");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBuildingList, response.getBody());
    }

    // Test for getBuildingsByType
    @Test
    public void testGetBuildingsByType_Success() {
        List<Building> mockBuildingList = Arrays.asList(mockBuilding);
        when(buildingService.getBuildingsByType(anyString())).thenReturn(mockBuildingList);

        ResponseEntity<List<Building>> response = buildingController.getBuildingsByType("maison");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBuildingList, response.getBody());
    }

    // Test for getBuildingsByAreaBetween
    @Test
    public void testGetBuildingsByAreaBetween_Success() {
        List<Building> mockBuildingList = Arrays.asList(mockBuilding);
        when(buildingService.getBuildingsByAreaBetween(anyDouble(), anyDouble())).thenReturn(mockBuildingList);

        ResponseEntity<List<Building>> response = buildingController.getBuildingsByAreaBetween(2000.0, 3000.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBuildingList, response.getBody());
    }

    // Test for getBuildingsByPriceBetween
    @Test
    public void testGetBuildingsByPriceBetween_Success() {
        List<Building> mockBuildingList = Arrays.asList(mockBuilding);
        when(buildingService.getBuildingsByPriceBetween(anyDouble(), anyDouble())).thenReturn(mockBuildingList);

        ResponseEntity<List<Building>> response = buildingController.getBuildingsByPriceBetween(400000.0, 600000.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBuildingList, response.getBody());
    }

    // Test for getBuildingsByRoomsBetween
    @Test
    public void testGetBuildingsByRoomsBetween_Success() {
        List<Building> mockBuildingList = Arrays.asList(mockBuilding);
        when(buildingService.getBuildingsByRoomsBetween(anyInt(), anyInt())).thenReturn(mockBuildingList);

        ResponseEntity<List<Building>> response = buildingController.getBuildingsByRoomsBetween(3, 6);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBuildingList, response.getBody());
    }

    // Test for getBuildingsByOwnerId
    @Test
    public void testGetBuildingsByOwnerId_Success() {
        List<Building> mockBuildingList = Arrays.asList(mockBuilding);
        when(buildingService.getBuildingsByOwnerId(anyString())).thenReturn(mockBuildingList);

        ResponseEntity<List<Building>> response = buildingController.getBuildingsByOwnerId("owner1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBuildingList, response.getBody());
    }

    // Test for getBuildingsByOwnerUsername
    @Test
    public void testGetBuildingsByOwnerUsername_Success() {
        List<Building> mockBuildingList = Arrays.asList(mockBuilding);
        when(buildingService.getBuildingsByOwnerUsername(anyString())).thenReturn(mockBuildingList);

        ResponseEntity<List<Building>> response = buildingController.getBuildingsByOwnerUsername("owner1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBuildingList, response.getBody());
    }
    // Test for addReservation
    @Test
    public void testAddReservation_Success() {
        // Mock Reservation object
        Reservation mockReservation = mock(Reservation.class);

        // Mock buildingService.addReservation() to return a mock Building object
        when(buildingService.addReservation(anyString(), any(Reservation.class))).thenReturn(mockBuilding);

        // Call the controller method
        ResponseEntity<Building> response = buildingController.addReservation("building1", mockReservation);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBuilding, response.getBody());
    }

    // Test for getReservations
    @Test
    public void testGetReservations_Success() {
        // Mock a list of reservations
        List<Reservation> mockReservationList = Arrays.asList(mock(Reservation.class), mock(Reservation.class));

        // Mock buildingService.getReservations() to return the mock list
        when(buildingService.getReservations(anyString())).thenReturn(mockReservationList);

        // Call the controller method
        ResponseEntity<List<Reservation>> response = buildingController.getReservations("building1");

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockReservationList, response.getBody());
    }

    // Test for checkReservationConflict when there is no conflict
    @Test
    public void testCheckReservationConflict_NoConflict() {
        // Mock Reservation object
        Reservation mockReservation = mock(Reservation.class);

        // Mock buildingService.getBuildingById() to return a mock Building object
        when(buildingService.getBuildingById(anyString())).thenReturn(mockBuilding);

        // Mock buildingService.checkReservationConflict() to not throw any exception
        // This means no conflict
        doNothing().when(buildingService).checkReservationConflict(any(Building.class), any(Reservation.class));

        // Call the controller method
        ResponseEntity<String> response = buildingController.checkReservationConflict("building1", mockReservation);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("No conflict found. You can proceed with the reservation.", response.getBody());
    }

    // Test for checkReservationConflict when there is a conflict
    @Test
    public void testCheckReservationConflict_ConflictFound() {
        // Mock Reservation object
        Reservation mockReservation = mock(Reservation.class);

        // Mock buildingService.getBuildingById() to return a mock Building object
        when(buildingService.getBuildingById(anyString())).thenReturn(mockBuilding);

        // Mock buildingService.checkReservationConflict() to throw an exception indicating a conflict
        doThrow(new RuntimeException("Reservation conflict detected")).when(buildingService).checkReservationConflict(any(Building.class), any(Reservation.class));

        // Call the controller method
        ResponseEntity<String> response = buildingController.checkReservationConflict("building1", mockReservation);

        // Verify the response
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Reservation conflict detected", response.getBody());
    }

}
