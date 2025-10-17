package com.example.bus.api;

import com.example.bus.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.IOException;
import java.util.*;

import static com.example.bus.api.JsonUtil.MAPPER;

public class ReservationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonNode body;
        try {
            body = MAPPER.readTree(req.getInputStream());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            MAPPER.writeValue(resp.getWriter(), Collections.singletonMap("error", "Invalid JSON"));
            return;
        }
        String originParam = optText(body, "origin");
        String destinationParam = optText(body, "destination");
        int count = body.has("count") ? body.get("count").asInt(-1) : -1;
        int priceConfirmation = body.has("priceConfirmation") ? body.get("priceConfirmation").asInt(-1) : -1;

        Location origin = Location.fromString(originParam);
        Location destination = Location.fromString(destinationParam);
        if (origin == null || destination == null || origin == destination || count < 1 || count > 40 || priceConfirmation < 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            MAPPER.writeValue(resp.getWriter(), Collections.singletonMap("error", "Invalid parameters"));
            return;
        }

        try {
            Optional<Reservation> res = AppContext.RESERVATION.reserve(origin, destination, count, priceConfirmation);
            Reservation r = res.get();
            Map<String, Object> out = new LinkedHashMap<>();
            out.put("reservationId", r.getId());
            out.put("seatIds", r.getSeatIds());
            out.put("origin", r.getOrigin().name());
            out.put("destination", r.getDestination().name());
            out.put("totalPrice", r.getTotalPrice());
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            MAPPER.writeValue(resp.getWriter(), out);
        } catch (com.example.bus.exception.BusReservationException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            MAPPER.writeValue(resp.getWriter(), Collections.singletonMap("error", e.getMessage()));
        }
    }

    private static String optText(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull() ? node.get(field).asText() : null;
    }
}
