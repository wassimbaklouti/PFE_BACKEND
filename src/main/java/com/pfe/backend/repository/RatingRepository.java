package com.pfe.backend.repository;

import com.pfe.backend.domain.Rating;
import com.pfe.backend.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends MongoRepository<Rating, String> {
    Optional<Rating> findByUsernameAndHandymanUsername(String username, String handymanUsername);
    List<Rating> findByHandymanUsername(String handymanUsername);

}
