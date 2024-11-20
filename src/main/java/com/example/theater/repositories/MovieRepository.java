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

    @Query ("SELECT m FROM Movie m " +
            "WHERE (m.title LIKE CONCAT('%', :keyword, '%') " +
            "OR m.director LIKE CONCAT('%', :keyword, '%') " +
            "OR m.genre LIKE CONCAT('%', :keyword, '%') " +
            "OR m.actors LIKE CONCAT('%', :keyword, '%') " +
            "OR m.language LIKE CONCAT('%', :keyword, '%') " +
            "OR m.keywords LIKE CONCAT('%', :keyword, '%')) " +
            "AND m.nowShowing = :nowShowing " +
            "ORDER BY STR_TO_DATE(m.releaseDate, '%d/%m/%Y') ASC")
    List <Movie> getAllMoviesByKeyWordAndNowShowing (String keyword, boolean nowShowing);

    @Query ("SELECT m FROM Movie m " +
            "WHERE m.genre LIKE CONCAT('%', :genre, '%') " +
            "AND m.nowShowing = :nowShowing " +
            "ORDER BY STR_TO_DATE(m.releaseDate, '%d/%m/%Y') ASC")
    List <Movie> getAllMoviesByGenreAndNowShowing (String genre, boolean nowShowing);
}
