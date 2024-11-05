package com.example.theater.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table (name = "movie")
public class Movie {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column (nullable = false)
    private String title;

    @Column (columnDefinition = "text", nullable = false)
    private String description;

    @Column (nullable = false)
    private String releaseDate;

    @Column (nullable = false)
    private boolean nowShowing;

    @Column (nullable = false)
    private String trailerUrl;

    @Column (nullable = false)
    private String genre;

    @Column (nullable = false)
    private String director;

    @Column (nullable = false)
    private String actors;

    @Column (nullable = false)
    private int duration;

    @Column (nullable = false)
    private String language;

    @Column (nullable = false)
    private String rated;

    @Column (nullable = false)
    private boolean bannerUrl;

    @Column (nullable = false)
    private String keywords;

    public Movie (String title, String description, String releaseDate, String nowShowing, String trailerUrl, String genre, String director, String actors, String duration, String language, String rated, boolean bannerUrl, String keywords) {
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.nowShowing = Boolean.parseBoolean(nowShowing);
        this.trailerUrl = trailerUrl;
        this.genre = genre;
        this.director = director;
        this.actors = actors;
        this.duration = Integer.parseInt(duration);
        this.language = language;
        this.rated = rated;
        this.bannerUrl = bannerUrl;
        this.keywords = keywords;
    }
}
