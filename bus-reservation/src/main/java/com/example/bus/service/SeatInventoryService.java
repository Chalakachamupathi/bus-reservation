package com.example.bus.service;

import com.example.bus.model.JourneyDirection;
import com.example.bus.model.Location;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SeatInventoryService {
    private static final int ROWS = 10;
    private static final char[] LABELS = new char[]{'A','B','C','D'};

    // For each direction, for each seatId, a boolean[3] indicating occupancy per segment (0:A-B,1:B-C,2:C-D)
    private final Map<JourneyDirection, Map<String, boolean[]>> occupancyByDirection = new EnumMap<>(JourneyDirection.class);

    public SeatInventoryService() {
        for (JourneyDirection dir : JourneyDirection.values()) {
            Map<String, boolean[]> map = new ConcurrentHashMap<>();
            for (int r = 1; r <= ROWS; r++) {
                for (char l : LABELS) {
                    map.put(r + String.valueOf(l), new boolean[3]);
                }
            }
            occupancyByDirection.put(dir, map);
        }
    }

    public static int[] segmentRange(Location origin, Location destination) {
        int start = Math.min(origin.ordinal(), destination.ordinal());
        int endExclusive = Math.max(origin.ordinal(), destination.ordinal());
        return new int[]{start, endExclusive}; // segments indexes 0..2 map to A-B,B-C,C-D
    }

    public synchronized List<String> findAvailableSeats(JourneyDirection direction, Location origin, Location destination, int count) {
        if (count < 1) return Collections.emptyList();
        int[] range = segmentRange(origin, destination);
        List<String> result = new ArrayList<>();
        for (int r = 1; r <= ROWS && result.size() < count; r++) {
            for (char l : LABELS) {
                if (result.size() >= count) break;
                String seatId = r + String.valueOf(l);
                if (isSeatFree(direction, seatId, range[0], range[1])) {
                    result.add(seatId);
                }
            }
        }
        return result;
    }

    public synchronized boolean reserveSeats(JourneyDirection direction, Location origin, Location destination, List<String> seatIds) {
        int[] range = segmentRange(origin, destination);
        // re-check availability atomically
        for (String seatId : seatIds) {
            if (!isSeatFree(direction, seatId, range[0], range[1])) {
                return false;
            }
        }
        // mark occupied
        for (String seatId : seatIds) {
            markOccupied(direction, seatId, range[0], range[1]);
        }
        return true;
    }

    private boolean isSeatFree(JourneyDirection direction, String seatId, int startSeg, int endSegExclusive) {
        boolean[] occ = occupancyByDirection.get(direction).get(seatId);
        for (int i = startSeg; i < endSegExclusive; i++) {
            if (occ[i]) return false;
        }
        return true;
    }

    private void markOccupied(JourneyDirection direction, String seatId, int startSeg, int endSegExclusive) {
        boolean[] occ = occupancyByDirection.get(direction).get(seatId);
        for (int i = startSeg; i < endSegExclusive; i++) {
            occ[i] = true;
        }
    }
}
