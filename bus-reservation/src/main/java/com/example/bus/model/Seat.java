package com.example.bus.model;

import java.util.Objects;

public class Seat {
    private final int row; // 1..10
    private final char label; // A..D

    public Seat(int row, char label) {
        if (row < 1 || row > 10) throw new IllegalArgumentException("row must be 1..10");
        if (label < 'A' || label > 'D') throw new IllegalArgumentException("label must be A..D");
        this.row = row;
        this.label = label;
    }

    public int getRow() { return row; }
    public char getLabel() { return label; }

    public String getId() { return row + String.valueOf(label); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seat)) return false;
        Seat seat = (Seat) o;
        return row == seat.row && label == seat.label;
    }

    @Override
    public int hashCode() { return Objects.hash(row, label); }

    @Override
    public String toString() { return getId(); }
}
