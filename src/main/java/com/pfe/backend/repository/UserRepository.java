package com.pfe.backend.repository;

import com.pfe.backend.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    User findUserByUsername(String username);
    Optional<User> findByUsername(String username);

    User findUserByEmail(String email);

    @Query(value = "{}", fields = "{email : 1}")
    List<String> getEmails();

    long countByIsActive(boolean isActive);

    long countByIsNotLocked(boolean isNotLocked);

    List<User> findByRoleAndExpertise(String role, String expertise);

    long countByRoleAndExpertise(String role, String expertise);

    List<User> findByRole(String role);

}
