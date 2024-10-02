package com.example.theater.models;

public class Movie {
    private String title;
    private String posterUrl;
    private String description;
    private String releaseDate;

    public Movie(String title, String posterUrl, String description, String releaseDate) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.description = description;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
