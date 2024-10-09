package com.example.theater.repositories;

import com.example.theater.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    public Movie findByTitle(String title);

    public List<Movie> getAllMoviesByNowShowing(boolean nowShowing);

    @Query("select m from Movie m where (lower(m.title) like lower(concat('%', :keyword, '%')) or "
            + "lower(m.director) like lower(concat('%', :keyword, '%')) or " + "lower(m.genre) like lower(concat('%', :keyword, '%')) or "
            + "lower(m.actors) like lower(concat('%', :keyword, '%')) or " + "lower(m.language) like lower(concat('%', :keyword, '%'))) "
            + "and m.nowShowing = :nowShowing")
    public List<Movie> getAllMoviesByKeyWordAndNowShowing(String keyword, boolean nowShowing);
}
