package com.example.bus.api;

import com.example.bus.model.*;
import com.example.bus.service.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.IOException;
import java.util.*;

import static com.example.bus.api.JsonUtil.MAPPER;

public class AvailabilityServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String originParam = req.getParameter("origin");
        String destinationParam = req.getParameter("destination");
        String countParam = req.getParameter("count");

        Location origin = Location.fromString(originParam);
        Location destination = Location.fromString(destinationParam);
        int count = safeInt(countParam, -1);
        if (origin == null || destination == null || origin == destination || count < 1 || count > 40) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            MAPPER.writeValue(resp.getWriter(), Collections.singletonMap("error", "Invalid parameters"));
            return;
        }

        ReservationService.Availability availability = AppContext.RESERVATION.checkAvailability(origin, destination, count);
        if (availability.seats.size() < count) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            MAPPER.writeValue(resp.getWriter(), Collections.singletonMap("error", "Insufficient seats"));
            return;
        }
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("availableSeatIds", availability.seats);
        out.put("totalPrice", availability.totalPrice);
        resp.setContentType("application/json");
        MAPPER.writeValue(resp.getWriter(), out);
    }

    private int safeInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }
}
