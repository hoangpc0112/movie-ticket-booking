package com.example.theater.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table (name = "bills")
public class Bill {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column (nullable = false)
    private String user;

    @Column (nullable = false)
    private int totalPrice;

    @Column (nullable = false)
    private String dateCreated;

    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn (name = "bill_id")
    private List <Ticket> tickets;

    @ElementCollection
    @CollectionTable (name = "bill_foods", joinColumns = @JoinColumn (name = "bill_id"))
    @MapKeyColumn (name = "food_name")
    @Column (name = "quantity")
    private Map <String, Integer> foods;

    public Bill (String user, int totalPrice, String dateCreated, List <Ticket> tickets, Map <String, Integer> foods) {
        this.user = user;
        this.totalPrice = totalPrice;
        this.dateCreated = dateCreated;
        this.tickets = tickets;
        this.foods = foods;
    }
}