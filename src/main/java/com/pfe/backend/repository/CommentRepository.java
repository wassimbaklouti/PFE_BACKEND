package com.pfe.backend.repository;

import com.pfe.backend.domain.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByPostId(String postId); // To get all comments for a specific post
}
