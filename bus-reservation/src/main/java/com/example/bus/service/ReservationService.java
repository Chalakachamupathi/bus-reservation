package com.example.bus.service;

import com.example.bus.model.*;

import java.time.Instant;
import com.example.bus.exception.BusReservationException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationService {
    private final PricingService pricingService;
    private final SeatInventoryService inventoryService;
    private final AtomicLong seq = new AtomicLong(1);

    public ReservationService(PricingService pricingService, SeatInventoryService inventoryService) {
        this.pricingService = pricingService;
        this.inventoryService = inventoryService;
    }

    public static class Availability {
        public final List<String> seats;
        public final int totalPrice;
        public Availability(List<String> seats, int totalPrice) { this.seats = seats; this.totalPrice = totalPrice; }
    }

    public Availability checkAvailability(Location origin, Location destination, int count) {
        JourneyDirection direction = computeDirection(origin, destination);
        List<String> seats = inventoryService.findAvailableSeats(direction, origin, destination, count);
        int pricePer = pricingService.price(origin, destination);
        return new Availability(seats, pricePer * count);
    }

    public Optional<Reservation> reserve(Location origin, Location destination, int count, int priceConfirmation) {
        JourneyDirection direction = computeDirection(origin, destination);
        int pricePer = pricingService.price(origin, destination);
        int total = pricePer * count;
        if (total != priceConfirmation) throw new BusReservationException("Price confirmation mismatch; expected " + total);
        List<String> seats = inventoryService.findAvailableSeats(direction, origin, destination, count);
        if (seats.size() < count) throw new BusReservationException("Insufficient seats for request");
        boolean ok = inventoryService.reserveSeats(direction, origin, destination, seats);
        if (!ok) throw new BusReservationException("Seats became unavailable during reservation");
        String id = String.format("R-%06d", seq.getAndIncrement());
        return Optional.of(new Reservation(id, direction, origin, destination, seats, total, Instant.now()));
    }

    private JourneyDirection computeDirection(Location origin, Location destination) {
        return origin.ordinal() < destination.ordinal() ? JourneyDirection.OUTBOUND : JourneyDirection.RETURN;
    }
}
