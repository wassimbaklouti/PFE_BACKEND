package com.pfe.backend.repository;

import com.pfe.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    @Query("SELECT u.email FROM User u")
    List<String> getEmails();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = :isActive")
    long countByIsActive(@Param("isActive") boolean isActive);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.isNotLocked = :isNotLocked")
    long countByIsNotLocked(@Param("isNotLocked") boolean isNotLocked);
}
