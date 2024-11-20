package com.example.theater.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table (name = "comments")
public class Comment {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column (nullable = false)
    private String commenter;

    @Column (columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn (name = "movie_id", referencedColumnName = "id")
    private Movie movie;

    public Comment (String commenter, String content, Movie movie) {
        this.commenter = commenter;
        this.content = content;
        this.movie = movie;
    }
}
