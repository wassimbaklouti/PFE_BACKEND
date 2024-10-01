package com.pfe.backend.service.impl;

import com.pfe.backend.domain.Building;
import com.pfe.backend.domain.Post;
import com.pfe.backend.domain.Reservation;
import com.pfe.backend.domain.User;
import com.pfe.backend.exception.domain.NotAnImageFileException;
import com.pfe.backend.repository.BuildingRepository;
import com.pfe.backend.repository.UserRepository;
import com.pfe.backend.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.pfe.backend.constant.FileConstant.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.hibernate.bytecode.BytecodeLogger.LOGGER;
import static org.springframework.http.MediaType.*;

@Service
public class BuildingServiceImpl implements BuildingService {

    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Building createBuilding(String type, String address, int rooms,double price, double area, String city, MultipartFile image, String ownerUsername) throws NotAnImageFileException, IOException {
        User owner = userRepository.findUserByUsername(ownerUsername);
        Building newBuilding = new Building( type,  address,  rooms,  price,  area,  owner,  null,  city);

        // Save the post first to generate its ID
        Building savedBuilding = buildingRepository.save(newBuilding);

        // Handle image saving
        savePostImage(savedBuilding, image);

        return savedBuilding;
    }

    private void savePostImage(Building building, MultipartFile profileImage) throws IOException, NotAnImageFileException {
        if (profileImage != null) {
            if ( ! Arrays.asList(IMAGE_JPEG_VALUE , IMAGE_PNG_VALUE , IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
                throw new NotAnImageFileException (profileImage.getOriginalFilename()+ "is not an image file . Please upload an image ");
            }
            Path userFolder = Paths.get(BUILDING_FOLDER + building.getOwner().getUsername()).toAbsolutePath().normalize();
            if ( !Files.exists(userFolder)){
                Files.createDirectories(userFolder);
                LOGGER.info(DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(BUILDING_FOLDER + building.getId() + DOT + JPG_EXTENSION ));
            Files.copy(profileImage.getInputStream(),userFolder.resolve(building.getId()+ DOT + JPG_EXTENSION) ,REPLACE_EXISTING );
            building.setImage_url(setProfileImage(building.getOwner().getUsername(), building.getId()));
            buildingRepository.save(building) ;
            LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
    }

    private String setProfileImage(String username, String id) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(BUILDING_IMAGE_PATH + username + FORWARD_SLASH
                + id +DOT + JPG_EXTENSION).toUriString();
    }

    @Override
    public Building getBuildingById(String id) {
        Optional<Building> building = buildingRepository.findById(id);
        return building.orElseThrow(() -> new RuntimeException("Building not found with id: " + id));
    }

    @Override
    public List<Building> getAllBuildings() {
        return buildingRepository.findAll();
    }

    @Override
    public Building updateBuilding(String id, String type, String address, int rooms,double price, double area, String city, MultipartFile image, String ownerUsername) throws IOException, NotAnImageFileException{
        Building existingBuilding = getBuildingById(id);
        User owner = userRepository.findUserByUsername(ownerUsername);

        existingBuilding.setType(type);
        existingBuilding.setAddress(address);
        existingBuilding.setRooms(rooms);
        existingBuilding.setPrice(price);
        existingBuilding.setArea(area);
        existingBuilding.setCity(city);
        existingBuilding.setImage_url(null);
        existingBuilding.setOwner(owner);
        existingBuilding = buildingRepository.save(existingBuilding);
        savePostImage(existingBuilding, image);
        return buildingRepository.save(existingBuilding);
    }

    @Override
    public void deleteBuilding(String id) {
        Building existingBuilding = getBuildingById(id);
        buildingRepository.delete(existingBuilding);
    }

    @Override
    public List<Building> getBuildingsByAddress(String address) {
        return buildingRepository.findByAddressContaining(address);
    }

    @Override
    public List<Building> getBuildingsByType(String type) {
        return buildingRepository.findByType(type);
    }

    @Override
    public List<Building> getBuildingsByAreaBetween(double minArea, double maxArea) {
        return buildingRepository.findByAreaBetween(minArea, maxArea);
    }

    @Override
    public List<Building> getBuildingsByPriceBetween(double minPrice, double maxPrice) {
        return buildingRepository.findByPriceBetween(minPrice, maxPrice);
    }

    @Override
    public List<Building> getBuildingsByRoomsBetween(int minRooms, int maxRooms) {
        return buildingRepository.findByRoomsBetween(minRooms, maxRooms);
    }

    @Override
    public List<Building> getBuildingsByOwnerId(String ownerId) {
        return buildingRepository.findByOwnerUserId(ownerId);
    }

    @Override
    public List<Building> getBuildingsByOwnerUsername(String username) {
        return buildingRepository.findByOwnerUsername(username);
    }

    // New methods for handling reservations

    @Override
    public Building addReservation(String buildingId, Reservation reservation) {
        Building building = getBuildingById(buildingId);

        // Check if the new reservation conflicts with existing reservations
        for (Reservation existingReservation : building.getReservations()) {
            if (isDateConflict(existingReservation.getEntryDate(), existingReservation.getExitDate(), reservation.getEntryDate(), reservation.getExitDate())) {
                throw new RuntimeException("Reservation conflicts with an existing reservation.");
            }
        }

        // If no conflict, add the reservation
        List<Reservation> reservations = building.getReservations();
        if (reservations == null) {
            reservations = new ArrayList<>();
        }
        reservations.add(reservation);
        building.setReservations(reservations);

        return buildingRepository.save(building);
    }
@Override
    public void checkReservationConflict(Building building, Reservation newReservation) {
        for (Reservation existingReservation : building.getReservations()) {
            if (isDateConflict(existingReservation.getEntryDate(), existingReservation.getExitDate(),
                    newReservation.getEntryDate(), newReservation.getExitDate())) {
                throw new RuntimeException("Reservation conflicts with an existing reservation.");
            }
        }
    }

    @Override
    public List<Reservation> getReservations(String buildingId) {
        Building building = getBuildingById(buildingId);
        return building.getReservations();
    }

    // Helper method to check if two date ranges conflict
    private boolean isDateConflict(Date start1, Date end1, Date start2, Date end2) {
        return !(start1.after(end2) || end1.before(start2));
    }
}
