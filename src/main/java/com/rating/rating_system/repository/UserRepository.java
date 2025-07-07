package com.rating.rating_system.repository;

import com.rating.rating_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // ✅ Check if email already exists (used in registration)
    boolean existsByEmail(String email);

    // ✅ Find user by email (used in login)
    Optional<User> findByEmail(String email);  // changed return type to Optional<User>
}
