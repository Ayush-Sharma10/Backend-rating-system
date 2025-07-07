package com.rating.rating_system.dto;

import lombok.Data;

@Data
public class RatingFilterRequest {
    private Integer ambiance;
    private Integer food;
    private Integer service;
    private Integer cleanliness;
    private Integer drinks;
    private String email; // optional, filter by user
}
