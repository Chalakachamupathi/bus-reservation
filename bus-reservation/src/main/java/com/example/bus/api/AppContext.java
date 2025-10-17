package com.example.bus.api;

import com.example.bus.service.*;

public final class AppContext {
    public static final PricingService PRICING = new PricingService();
    public static final SeatInventoryService INVENTORY = new SeatInventoryService();
    public static final ReservationService RESERVATION = new ReservationService(PRICING, INVENTORY);
    private AppContext() {}
}
