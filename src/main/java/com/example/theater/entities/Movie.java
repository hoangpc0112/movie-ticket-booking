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
@Table (name = "movies")
public class Movie {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column (nullable = false)
    private String title;

    @Column (nullable = false)
    private String posterUrl;

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
    private String bannerUrl;

    @Column (nullable = false)
    private String keywords;

    @OneToMany (mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List <Comment> comments;

    public Movie (String title, String posterUrl, String description, String releaseDate, String nowShowing, String trailerUrl, String genre, String director, String actors, String duration, String language, String rated, String bannerUrl, String keywords) {
        this.title = title;
        this.posterUrl = posterUrl;
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
