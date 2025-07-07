package com.rating.rating_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RatingReportResponse {
    private double ambiance;
    private double food;
    private double service;
    private double cleanliness;
    private double drinks;
    private double overallAverage;
}
