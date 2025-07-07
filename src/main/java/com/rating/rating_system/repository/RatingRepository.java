package com.rating.rating_system.repository;

import com.rating.rating_system.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    // ✅ For fetching all ratings of a user
    List<Rating> findByUserId(Long userId);

    // ✅ Used for filtering ratings in AdminController
    @Query("SELECT r FROM Rating r WHERE " +
            "(:ambiance IS NULL OR r.ambiance = :ambiance) AND " +
            "(:food IS NULL OR r.food = :food) AND " +
            "(:service IS NULL OR r.service = :service) AND " +
            "(:cleanliness IS NULL OR r.cleanliness = :cleanliness) AND " +
            "(:drinks IS NULL OR r.drinks = :drinks) AND " +
            "(:email IS NULL OR r.user.email = :email)")
    List<Rating> filterRatings(
            @Param("ambiance") Integer ambiance,
            @Param("food") Integer food,
            @Param("service") Integer service,
            @Param("cleanliness") Integer cleanliness,
            @Param("drinks") Integer drinks,
            @Param("email") String email
    );

    // ✅ For admin report generation — must return List<Object[]>
    @Query("SELECT AVG(r.ambiance), AVG(r.food), AVG(r.service), AVG(r.cleanliness), AVG(r.drinks) FROM Rating r")
    List<Object[]> getAverageRatings();
}
