package com.pfe.backend.repository;

import com.pfe.backend.domain.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LikeRepository extends MongoRepository<Like, String> {
    List<Like> findByPostId(String postId); // To get all likes for a specific post
}
