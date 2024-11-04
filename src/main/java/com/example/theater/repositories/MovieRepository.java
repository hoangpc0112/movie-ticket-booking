package com.example.theater.repositories;

import com.example.theater.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository <Movie, Long> {
    Movie findByTitle (String title);

    @Query ("SELECT m FROM Movie m " +
            "WHERE m.nowShowing = :nowShowing " +
            "ORDER BY STR_TO_DATE(m.releaseDate, '%d/%m/%Y') ASC")
    List <Movie> getAllMoviesByNowShowing (boolean nowShowing);

    @Query (value = "SELECT * FROM movie " +
            "WHERE (title REGEXP CONCAT('\\\\b', :keyword, '\\\\b') OR " +
            "director REGEXP CONCAT('\\\\b', :keyword, '\\\\b') OR " +
            "genre REGEXP CONCAT('\\\\b', :keyword, '\\\\b') OR " +
            "actors REGEXP CONCAT('\\\\b', :keyword, '\\\\b') OR " +
            "language REGEXP CONCAT('\\\\b', :keyword, '\\\\b') OR " +
            "keywords LIKE CONCAT('%', :keyword, '%')) " +
            "AND now_showing = :nowShowing " +
            "ORDER BY STR_TO_DATE(release_date, '%d/%m/%Y') ASC",
            nativeQuery = true)
    List <Movie> getAllMoviesByKeyWordAndNowShowing (String keyword, boolean nowShowing);

    @Query ("SELECT m FROM Movie m " +
            "WHERE m.genre LIKE CONCAT('%', :genre, '%') " +
            "AND m.nowShowing = :nowShowing " +
            "ORDER BY STR_TO_DATE(m.releaseDate, '%d/%m/%Y') ASC")
    List <Movie> getAllMoviesByGenreAndNowShowing (String genre, boolean nowShowing);
}
