package com.example.theater.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table ( name = "comments" )
public class Comment {

    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private long id;

    @ManyToOne
    @JoinColumn ( name = "movie_id", referencedColumnName = "id" )
    private Movie movie;

    @Column ( nullable = false )
    private String commenter;

    @Column ( columnDefinition = "TEXT", nullable = false )
    private String content;

    public Comment ( Movie movie, String commenter, String content ) {
        this.movie = movie;
        this.commenter = commenter;
        this.content = content;
    }
}
