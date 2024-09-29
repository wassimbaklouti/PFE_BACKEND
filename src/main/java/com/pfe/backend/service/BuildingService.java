package com.pfe.backend.service;

import com.pfe.backend.domain.Building;
import com.pfe.backend.domain.Reservation;

import java.util.List;

public interface BuildingService {

    Building createBuilding(Building building);

    Building getBuildingById(String id);

    List<Building> getAllBuildings();

    Building updateBuilding(String id, Building buildingDetails);

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
}
