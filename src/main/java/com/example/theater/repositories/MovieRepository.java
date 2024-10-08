package com.example.theater.repositories;

import com.example.theater.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    public Movie findByTitle(String title);

    public List<Movie> getAllMoviesByNowShowing(boolean nowShowing);
}
