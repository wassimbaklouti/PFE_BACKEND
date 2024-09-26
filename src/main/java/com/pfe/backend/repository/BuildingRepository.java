package com.pfe.backend.repository;

import com.pfe.backend.domain.Building;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BuildingRepository extends MongoRepository<Building, String> {

    // Requêtes personnalisées
    List<Building> findByAddressContaining(String address);
    List<Building> findByType(String type);
    List<Building> findByAreaBetween(double minArea, double maxArea);
    List<Building> findByPriceBetween(double minPrice, double maxPrice);
    List<Building> findByRoomsBetween(int minRooms, int maxRooms);
    List<Building> findByOwnerUserId(String ownerId);
    List<Building> findByOwnerUsername(String username);
}
