package com.pfe.backend.service;

import com.pfe.backend.domain.Building;
import com.pfe.backend.domain.Reservation;
import com.pfe.backend.domain.User;
import com.pfe.backend.exception.domain.NotAnImageFileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BuildingService {

    Building createBuilding(String type, String address, int rooms, double price, double area, String city, MultipartFile image, String ownerUsername) throws NotAnImageFileException,IOException;

    Building getBuildingById(String id);

    List<Building> getAllBuildings();

    Building updateBuilding(String id, String type, String address, int rooms,double price, double area, String city, MultipartFile image, String ownerUsername) throws IOException, NotAnImageFileException;

    void deleteBuilding(String id);

    List<Building> getBuildingsByAddress(String address);

    List<Building> getBuildingsByType(String type);

    List<Building> getBuildingsByAreaBetween(double minArea, double maxArea);

    List<Building> getBuildingsByPriceBetween(double minPrice, double maxPrice);

    List<Building> getBuildingsByRoomsBetween(int minRooms, int maxRooms);

    List<Building> getBuildingsByOwnerId(String ownerId);

    List<Building> getBuildingsByOwnerUsername(String username);
    Building addReservation(String buildingId, Reservation reservation);
    List<Reservation> getReservations(String buildingId);
    void checkReservationConflict(Building building, Reservation newReservation);
}
