package com.rating.rating_system.dto;

import lombok.Data;

@Data
public class RatingRequest {
    private int ambiance;
    private int food;
    private int service;
    private int cleanliness;
    private int drinks;
}
