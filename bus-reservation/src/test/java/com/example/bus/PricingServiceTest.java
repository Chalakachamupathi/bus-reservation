package com.example.bus;

import com.example.bus.model.Location;
import com.example.bus.service.PricingService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PricingServiceTest {
    @Test
    void pricesBySegments() {
        PricingService s = new PricingService();
        assertEquals(50, s.price(Location.A, Location.B));
        assertEquals(100, s.price(Location.A, Location.C));
        assertEquals(150, s.price(Location.A, Location.D));
        assertEquals(50, s.price(Location.B, Location.C));
        assertEquals(100, s.price(Location.B, Location.D));
        assertEquals(50, s.price(Location.C, Location.D));
        // reverse
        assertEquals(50, s.price(Location.B, Location.A));
        assertEquals(100, s.price(Location.C, Location.A));
        assertEquals(150, s.price(Location.D, Location.A));
    }
}
