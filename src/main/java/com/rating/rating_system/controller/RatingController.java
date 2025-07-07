package com.rating.rating_system.controller;

import com.rating.rating_system.dto.RatingRequest;
import com.rating.rating_system.model.Rating;
import com.rating.rating_system.model.User;
import com.rating.rating_system.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RatingController {

    @Autowired
    private RatingRepository ratingRepository;

    @PostMapping("/rate")
    public ResponseEntity<String> submitRating(@RequestBody RatingRequest request, Authentication authentication) {
        // ✅ Get authenticated user (injected from JWT token)
        User user = (User) authentication.getPrincipal();

        // ✅ Validate each rating field is between 1 and 5
        if (!isValidRating(request.getAmbiance()) ||
                !isValidRating(request.getFood()) ||
                !isValidRating(request.getService()) ||
                !isValidRating(request.getCleanliness()) ||
                !isValidRating(request.getDrinks())) {
            return ResponseEntity.badRequest().body("❌ Ratings must be between 1 and 5.");
        }

        // ✅ Save valid rating
        Rating rating = new Rating();
        rating.setAmbiance(request.getAmbiance());
        rating.setFood(request.getFood());
        rating.setService(request.getService());
        rating.setCleanliness(request.getCleanliness());
        rating.setDrinks(request.getDrinks());
        rating.setUser(user);

        ratingRepository.save(rating);

        return ResponseEntity.ok("✅ Rating submitted successfully!");
    }

    // ✅ Helper method to validate range
    private boolean isValidRating(int value) {
        return value >= 1 && value <= 5;
    }
}
