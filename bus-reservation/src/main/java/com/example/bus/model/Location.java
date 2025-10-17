package com.example.bus.model;

public enum Location {
    A, B, C, D;

    public static Location fromString(String s) {
        if (s == null) return null;
        switch (s.trim().toUpperCase()) {
            case "A": return A;
            case "B": return B;
            case "C": return C;
            case "D": return D;
            default: return null;
        }
    }
}
