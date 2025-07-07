package com.rating.rating_system.controller;

import com.rating.rating_system.dto.RatingFilterRequest;
import com.rating.rating_system.model.Rating;
import com.rating.rating_system.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private RatingRepository ratingRepository;

    // ✅ Admin filters ratings
    @PostMapping("/filter-ratings")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  // ✅ Ensure ROLE_ prefix matches JWT authority
    public ResponseEntity<List<Rating>> filterRatings(@RequestBody RatingFilterRequest filter) {
        logRequest("filter-ratings");

        List<Rating> filtered = ratingRepository.filterRatings(
                filter.getAmbiance(),
                filter.getFood(),
                filter.getService(),
                filter.getCleanliness(),
                filter.getDrinks(),
                filter.getEmail()
        );

        return ResponseEntity.ok(filtered);
    }

    // ✅ Admin generates average ratings report
    @GetMapping("/report")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> getAverageRatingsReport() {
        logRequest("report");

        List<Object[]> results = ratingRepository.getAverageRatings();

        if (results.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "No ratings found."));
        }

        Object[] averages = results.get(0);

        double avgAmbiance = averages[0] != null ? ((Number) averages[0]).doubleValue() : 0.0;
        double avgFood = averages[1] != null ? ((Number) averages[1]).doubleValue() : 0.0;
        double avgService = averages[2] != null ? ((Number) averages[2]).doubleValue() : 0.0;
        double avgCleanliness = averages[3] != null ? ((Number) averages[3]).doubleValue() : 0.0;
        double avgDrinks = averages[4] != null ? ((Number) averages[4]).doubleValue() : 0.0;

        double overallAverage = (avgAmbiance + avgFood + avgService + avgCleanliness + avgDrinks) / 5.0;

        Map<String, Object> report = new HashMap<>();
        report.put("Average Ambiance", avgAmbiance);
        report.put("Average Food", avgFood);
        report.put("Average Service", avgService);
        report.put("Average Cleanliness", avgCleanliness);
        report.put("Average Drinks", avgDrinks);
        report.put("Overall Average", overallAverage);

        return ResponseEntity.ok(report);
    }

    // ✅ Debug log helper
    private void logRequest(String endpoint) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

    }
}
