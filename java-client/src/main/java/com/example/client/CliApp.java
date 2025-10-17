package com.example.client;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CliApp {
    public static void main(String[] args) throws Exception {
        Config cfg = new Config();
        ApiClient api = new ApiClient(cfg.getBaseUrl());
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        // Interactive continuous loop only (requires terminal)
        String lastOrigin = null;
        String lastDestination = null;
        int lastCount = 0;
        Integer lastTotalPrice = null;
        while (true) {
            System.out.println();
            System.out.println("=== Bus Reservation CLI ===");
            System.out.println("1) Check availability");
            System.out.println("2) Reserve (uses last availability price)");
            System.out.println("3) Exit");
            System.out.print("Select option: ");
            String opt = in.readLine();
            if (opt == null) return;
            opt = opt.trim();
            if (opt.equals("3")) {
                System.out.println("Bye.");
                return;
            } else if (opt.equals("1")) {
                System.out.print("Origin (A/B/C/D): ");
                String origin = in.readLine().trim().toUpperCase();
                System.out.print("Destination (A/B/C/D): ");
                String destination = in.readLine().trim().toUpperCase();
                System.out.print("Passengers (1-40): ");
                int count = Integer.parseInt(in.readLine().trim());
                try {
                    JsonNode avail = api.checkAvailability(origin, destination, count);
                    System.out.println("Availability: " + avail.toPrettyString());
                    int totalPrice = avail.get("totalPrice").asInt();
                    lastOrigin = origin;
                    lastDestination = destination;
                    lastCount = count;
                    lastTotalPrice = totalPrice;
                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            } else if (opt.equals("2")) {
                if (lastTotalPrice == null) {
                    System.out.println("Please check availability first.");
                    continue;
                }
                try {
                    JsonNode res = api.reserve(lastOrigin, lastDestination, lastCount, lastTotalPrice);
                    System.out.println("Reservation: " + res.toPrettyString());
                    // reset last price to prevent accidental re-submission
                    lastTotalPrice = null;
                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            } else {
                System.out.println("Unknown option. Try again.");
            }
        }
    }
}
