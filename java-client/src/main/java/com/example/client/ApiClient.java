package com.example.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class ApiClient {
    private final String baseUrl;
    private final HttpClient http;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
    }

    public JsonNode checkAvailability(String origin, String destination, int count) throws Exception {
        String url = String.format("%s/api/v1/availability?origin=%s&destination=%s&count=%d",
                baseUrl, origin, destination, count);
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().timeout(Duration.ofSeconds(10)).build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() != 200) throw new RuntimeException("Availability failed: " + res.statusCode() + " body=" + res.body());
        return MAPPER.readTree(res.body());
    }

    public JsonNode reserve(String origin, String destination, int count, int priceConfirmation) throws Exception {
        ObjectNode payload = MAPPER.createObjectNode();
        payload.put("origin", origin);
        payload.put("destination", destination);
        payload.put("count", count);
        payload.put("priceConfirmation", priceConfirmation);
        HttpRequest req = HttpRequest.newBuilder(URI.create(baseUrl + "/api/v1/reservations"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(MAPPER.writeValueAsString(payload)))
                .timeout(Duration.ofSeconds(10))
                .build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() != 201) throw new RuntimeException("Reserve failed: " + res.statusCode() + " body=" + res.body());
        return MAPPER.readTree(res.body());
    }
}
