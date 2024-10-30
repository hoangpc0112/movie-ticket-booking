package com.example.theater.repositories;

import com.example.theater.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository <Movie, Long> {
    Movie findByTitle (String title);

    List <Movie> getAllMoviesByNowShowing (boolean nowShowing);

    @Query (value = "SELECT * FROM movie " +
            "WHERE (lower(title) REGEXP CONCAT('\\\\b', lower(:keyword), '\\\\b') OR " +
            "lower(director) REGEXP CONCAT('\\\\b', lower(:keyword), '\\\\b') OR " +
            "lower(genre) REGEXP CONCAT('\\\\b', lower(:keyword), '\\\\b') OR " +
            "lower(actors) REGEXP CONCAT('\\\\b', lower(:keyword), '\\\\b') OR " +
            "lower(language) REGEXP CONCAT('\\\\b', lower(:keyword), '\\\\b')) " +
            "AND now_showing = :nowShowing",
            nativeQuery = true)
    List <Movie> getAllMoviesByKeyWordAndNowShowing (String keyword, boolean nowShowing);

    @Query ("select m from Movie m where m.genre like concat('%', :genre, '%') and m.nowShowing = :nowShowing ")
    List <Movie> getAllMoviesByGenreAAndNowShowing (String genre, boolean nowShowing);
}
