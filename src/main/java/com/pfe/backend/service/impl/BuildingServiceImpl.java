package com.pfe.backend.service.impl;


import com.pfe.backend.domain.Building;
import com.pfe.backend.repository.BuildingRepository;
import com.pfe.backend.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuildingServiceImpl implements BuildingService {

    @Autowired
    private BuildingRepository buildingRepository;

    @Override
    public Building createBuilding(Building building) {
        return buildingRepository.save(building);
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
    public Building updateBuilding(String id, Building buildingDetails) {
        Building existingBuilding = getBuildingById(id);

        existingBuilding.setType(buildingDetails.getType());
        existingBuilding.setAddress(buildingDetails.getAddress());
        existingBuilding.setRooms(buildingDetails.getRooms());
        existingBuilding.setPrice(buildingDetails.getPrice());
        existingBuilding.setArea(buildingDetails.getArea());
        existingBuilding.setOwner(buildingDetails.getOwner());

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

}
