package com.example.theater.repositories;

import com.example.theater.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository <Movie, Long> {
    Movie findByTitle (String title);

    List <Movie> getAllMoviesByNowShowing (boolean nowShowing);

    @Query (value = "SELECT * FROM movie " +
            "WHERE (title REGEXP CONCAT('\\\\b', :keyword, '\\\\b') OR " +
            "director REGEXP CONCAT('\\\\b', :keyword, '\\\\b') OR " +
            "genre REGEXP CONCAT('\\\\b', :keyword, '\\\\b') OR " +
            "actors REGEXP CONCAT('\\\\b', :keyword, '\\\\b') OR " +
            "language REGEXP CONCAT('\\\\b', :keyword, '\\\\b')) " +
            "AND now_showing = :nowShowing",
            nativeQuery = true)
    List <Movie> getAllMoviesByKeyWordAndNowShowing (String keyword, boolean nowShowing);

    @Query ("select m from Movie m where m.genre like concat('%', :genre, '%') and m.nowShowing = :nowShowing ")
    List <Movie> getAllMoviesByGenreAAndNowShowing (String genre, boolean nowShowing);
}
