package com.example.theater.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table (name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column (nullable = false)
    private String movieTitle;

    @Column (nullable = false)
    private String time;

    @Column (nullable = false)
    private String date;

    @Column (nullable = false)
    private int seatNo;

    @Column (nullable = false)
    private String seatLabel;

    @ManyToOne
    @JoinColumn (name = "bill_id", referencedColumnName = "id")
    private Bill bill;

    public Ticket (String movieTitle, String time, String date, int seatNo, String seatLabel) {
        this.movieTitle = movieTitle;
        this.time = time;
        this.date = date;
        this.seatNo = seatNo;
        this.seatLabel = seatLabel;
    }
}