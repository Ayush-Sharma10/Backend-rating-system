package com.rating.rating_system.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int ambiance;
    private int food;
    private int service;
    private int cleanliness;
    private int drinks;

    // Link rating to a specific user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
