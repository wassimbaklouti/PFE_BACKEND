package com.pfe.backend.repository;

import com.pfe.backend.domain.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    // Remplacez "user" par "userId"
    List<Post> findByUsername(String username);
}
