package com.example.bus;

import com.example.bus.model.JourneyDirection;
import com.example.bus.model.Location;
import com.example.bus.service.SeatInventoryService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class SeatInventoryServiceTest {
    @Test
    void reserveMarksSegmentsOccupied() {
        SeatInventoryService inv = new SeatInventoryService();
        List<String> seats = inv.findAvailableSeats(JourneyDirection.OUTBOUND, Location.A, Location.C, 2);
        assertEquals(2, seats.size());
        assertTrue(inv.reserveSeats(JourneyDirection.OUTBOUND, Location.A, Location.C, seats));
        // Now same seats should not be available for overlapping A->B
        List<String> overlap = inv.findAvailableSeats(JourneyDirection.OUTBOUND, Location.A, Location.B, 2);
        // they may still find other seats, but must not include the same ones
        for (String s : seats) {
            assertFalse(overlap.contains(s));
        }
    }
}
