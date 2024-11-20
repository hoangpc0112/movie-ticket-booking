package com.example.theater.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private int totalPrice;

    @Column (nullable = false)
    private String bookTime;

    @ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn (name = "user_id", referencedColumnName = "id")
    private AppUser user;

    @OneToMany (mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List <Ticket> tickets;

    @OneToMany (mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List <Food> foods;

    public Bill (int totalPrice, String bookTime, AppUser user, List <Ticket> tickets, List <Food> foods) {
        this.totalPrice = totalPrice;
        this.bookTime = bookTime;
        this.user = user;
        this.tickets = tickets;
        this.foods = foods;
    }
}