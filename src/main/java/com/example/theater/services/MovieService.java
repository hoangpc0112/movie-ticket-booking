package com.example.theater.services;

import com.example.theater.entities.Movie;
import com.example.theater.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateMovies() {
        for (Movie movie : movieRepository.findAll()) { // nếu ngày khởi chiếu sau ngày hôm nay thì false, còn lại true
            movie.setNowShowing(!LocalDate.parse(movie.getReleaseDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).isAfter(LocalDate.now()));
            // System.out.println(movie.getTitle() + " " + movie.isNowShowing());
            movieRepository.save(movie);
        }
    }
}
