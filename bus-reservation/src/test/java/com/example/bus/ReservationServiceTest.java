package com.example.bus;

import com.example.bus.model.*;
import com.example.bus.service.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationServiceTest {
    @Test
    void happyPathReserve() {
        PricingService pricing = new PricingService();
        SeatInventoryService inv = new SeatInventoryService();
        ReservationService svc = new ReservationService(pricing, inv);
        ReservationService.Availability avail = svc.checkAvailability(Location.A, Location.C, 3);
        assertEquals(3, avail.seats.size());
        assertEquals(300, avail.totalPrice);
        assertTrue(svc.reserve(Location.A, Location.C, 3, 300).isPresent());
    }

    @Test
    void priceMismatchConflict() {
        ReservationService svc = new ReservationService(new PricingService(), new SeatInventoryService());
        try {
            svc.reserve(Location.A, Location.C, 2, 999);
            fail("Expected BusReservationException");
        } catch (com.example.bus.exception.BusReservationException expected) {
            // ok
        }
    }
}
