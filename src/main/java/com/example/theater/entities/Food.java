package com.example.theater.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table (name = "foods")
public class Food {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column (nullable = false)
    private String food_name;

    @Column (nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn (name = "bill_id", referencedColumnName = "id")
    private Bill bill;

    public Food (String food_name, int quantity) {
        this.food_name = food_name;
        this.quantity = quantity;
    }
}
