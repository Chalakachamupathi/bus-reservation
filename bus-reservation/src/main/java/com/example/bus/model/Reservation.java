package com.example.bus.model;

import java.time.Instant;
import java.util.List;

public class Reservation {
    private final String id;
    private final JourneyDirection direction;
    private final Location origin;
    private final Location destination;
    private final List<String> seatIds;
    private final int totalPrice;
    private final Instant createdAt;

    public Reservation(String id, JourneyDirection direction, Location origin, Location destination, List<String> seatIds, int totalPrice, Instant createdAt) {
        this.id = id;
        this.direction = direction;
        this.origin = origin;
        this.destination = destination;
        this.seatIds = seatIds;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public JourneyDirection getDirection() { return direction; }
    public Location getOrigin() { return origin; }
    public Location getDestination() { return destination; }
    public List<String> getSeatIds() { return seatIds; }
    public int getTotalPrice() { return totalPrice; }
    public Instant getCreatedAt() { return createdAt; }
}
