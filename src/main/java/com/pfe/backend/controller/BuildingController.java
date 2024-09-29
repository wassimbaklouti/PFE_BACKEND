package com.pfe.backend.controller;

import com.pfe.backend.domain.Building;
import com.pfe.backend.domain.Reservation;
import com.pfe.backend.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buildings")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    // Existing endpoints

    @PostMapping("/create")
    public ResponseEntity<Building> createBuilding(@RequestBody Building building) {
        return ResponseEntity.ok(buildingService.createBuilding(building));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Building> getBuildingById(@PathVariable String id) {
        return ResponseEntity.ok(buildingService.getBuildingById(id));
    }

    @GetMapping
    public ResponseEntity<List<Building>> getAllBuildings() {
        return ResponseEntity.ok(buildingService.getAllBuildings());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Building> updateBuilding(@PathVariable String id, @RequestBody Building buildingDetails) {
        return ResponseEntity.ok(buildingService.updateBuilding(id, buildingDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable String id) {
        buildingService.deleteBuilding(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/address")
    public ResponseEntity<List<Building>> getBuildingsByAddress(@RequestParam String address) {
        return ResponseEntity.ok(buildingService.getBuildingsByAddress(address));
    }

    @GetMapping("/type")
    public ResponseEntity<List<Building>> getBuildingsByType(@RequestParam String type) {
        return ResponseEntity.ok(buildingService.getBuildingsByType(type));
    }

    @GetMapping("/area")
    public ResponseEntity<List<Building>> getBuildingsByAreaBetween(@RequestParam double minArea, @RequestParam double maxArea) {
        return ResponseEntity.ok(buildingService.getBuildingsByAreaBetween(minArea, maxArea));
    }

    @GetMapping("/price")
    public ResponseEntity<List<Building>> getBuildingsByPriceBetween(@RequestParam double minPrice, @RequestParam double maxPrice) {
        return ResponseEntity.ok(buildingService.getBuildingsByPriceBetween(minPrice, maxPrice));
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<Building>> getBuildingsByRoomsBetween(@RequestParam int minRooms, @RequestParam int maxRooms) {
        return ResponseEntity.ok(buildingService.getBuildingsByRoomsBetween(minRooms, maxRooms));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Building>> getBuildingsByOwnerId(@PathVariable String ownerId) {
        return ResponseEntity.ok(buildingService.getBuildingsByOwnerId(ownerId));
    }

    @GetMapping("/owner/username/{username}")
    public ResponseEntity<List<Building>> getBuildingsByOwnerUsername(@PathVariable String username) {
        return ResponseEntity.ok(buildingService.getBuildingsByOwnerUsername(username));
    }

    // New endpoints for reservation handling

    // Add a reservation to a specific building
    @PostMapping("/{buildingId}/reservation")
    public ResponseEntity<Building> addReservation(@PathVariable String buildingId, @RequestBody Reservation reservation) {
        Building updatedBuilding = buildingService.addReservation(buildingId, reservation);
        return ResponseEntity.ok(updatedBuilding);
    }

    // Get all reservations for a specific building
    @GetMapping("/{buildingId}/reservations")
    public ResponseEntity<List<Reservation>> getReservations(@PathVariable String buildingId) {
        List<Reservation> reservations = buildingService.getReservations(buildingId);
        return ResponseEntity.ok(reservations);
    }
}
