package com.example.theater.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table ( name = "movie" )
public class Movie {

    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private long id;

    @Column ( nullable = false )
    private String title;

    @Column ( nullable = false )
    private String posterUrl;

    @Column ( columnDefinition = "varchar(1000)", nullable = false )
    private String description;

    @Column ( nullable = false )
    private String releaseDate;

    @Column ( nullable = false )
    private boolean nowShowing;

    @Column ( nullable = false )
    private String trailerUrl;

    @Column ( nullable = false )
    private String genre;

    @Column ( nullable = false )
    private String director;

    @Column ( nullable = false )
    private String actors;

    @Column ( nullable = false )
    private int duration;

    @Column ( nullable = false )
    private String language;

    @Column ( nullable = false )
    private String rated;
}
