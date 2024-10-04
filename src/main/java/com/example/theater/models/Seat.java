package com.example.theater.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Seat {
    private long id;
    private int row;
    private int number;
    private boolean booked;
}
