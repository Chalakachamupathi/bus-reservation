package com.example.bus.service;

import com.example.bus.model.Location;

public class PricingService {
    // Returns price per passenger in whole rupees
    public int price(Location origin, Location destination) {
        if (origin == null || destination == null) throw new IllegalArgumentException("origin/destination required");
        if (origin == destination) throw new IllegalArgumentException("origin != destination");
        int segments = Math.abs(destination.ordinal() - origin.ordinal());
        switch (segments) {
            case 1: return 50;
            case 2: return 100;
            case 3: return 150;
            default: throw new IllegalArgumentException("invalid route");
        }
    }
}
