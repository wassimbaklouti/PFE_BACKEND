package com.pfe.backend.controller;

import com.pfe.backend.domain.Building;
import com.pfe.backend.domain.Reservation;
import com.pfe.backend.domain.User;
import com.pfe.backend.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.pfe.backend.constant.FileConstant.*;

@RestController
@RequestMapping({"/api/buildings", "/building"})
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    // Existing endpoints

    @PostMapping(value ="/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Building> createBuilding(
            @RequestParam ("type") String type,
            @RequestParam ("address") String address,
            @RequestParam ("rooms") int rooms,
            @RequestParam ("price") double price,
            @RequestParam ("area") double area,
            @RequestParam ("city") String city,
            @RequestParam ("ownerUsername") String ownerUsername,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        System.out.println("Type: " + type);
        System.out.println("Address: " + address);
        System.out.println("Rooms: " + rooms);
        System.out.println("Price: " + price);
        System.out.println("Area: " + area);
        System.out.println("City: " + city);
        System.out.println("Owner Username: " + ownerUsername);
        System.out.println("Image file: " + (imageFile != null ? imageFile.getOriginalFilename() : "No image file"));
        try{
            Building newBuilding = buildingService.createBuilding(type,address,rooms,price,area,city,imageFile,ownerUsername);
            return ResponseEntity.ok(newBuilding);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }
    @GetMapping(path = "/image/{username}/{fileName}" , produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable ("username") String username ,@PathVariable ("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(BUILDING_FOLDER + username +FORWARD_SLASH +fileName )) ;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Building> getBuildingById(@PathVariable String id) {
        return ResponseEntity.ok(buildingService.getBuildingById(id));
    }

    @GetMapping
    public ResponseEntity<List<Building>> getAllBuildings() {
        return ResponseEntity.ok(buildingService.getAllBuildings());
    }

    @PutMapping(value="/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Building> updateBuilding(@PathVariable String id,@RequestParam ("type") String type,
                                                   @RequestParam ("address") String address,
                                                   @RequestParam ("rooms") int rooms,
                                                   @RequestParam ("price") double price,
                                                   @RequestParam ("area") double area,
                                                   @RequestParam ("city") String city,
                                                   @RequestParam ("ownerUsername") String ownerUsername,
                                                   @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try{
            return ResponseEntity.ok(buildingService.updateBuilding(id, type,address,rooms,price,area,city,imageFile,ownerUsername));

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
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
    @PostMapping("/{buildingId}/check-reservation-conflict")
    public ResponseEntity<String> checkReservationConflict(@PathVariable String buildingId, @RequestBody Reservation reservation) {
        try {
            Building building = buildingService.getBuildingById(buildingId);  // Fetch building
            buildingService.checkReservationConflict(building, reservation);  // Check conflict

            // No conflict found
            return ResponseEntity.ok("No conflict found. You can proceed with the reservation.");
        } catch (RuntimeException e) {
            // Conflict found, return error message
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
