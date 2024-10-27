package com.example.theater.controllers;

import com.example.theater.entities.Movie;
import com.example.theater.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TheaterController {

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping ( "/now-showing" )
    public String nowShowing ( Model model ) {
        model.addAttribute( "movieList", movieRepository.getAllMoviesByNowShowing( true ) );
        return "now-showing";
    }

    @GetMapping ( "/coming-soon" )
    public String comingSoon ( Model model ) {
        model.addAttribute( "movieList", movieRepository.getAllMoviesByNowShowing( false ) );
        return "coming-soon";
    }

    @GetMapping ( "/search" )
    public String search ( @RequestParam ( "keyword" ) String keyword, Model model ) {
        model.addAttribute( "keyword", keyword );
        model.addAttribute( "nowShowingMovieList", movieRepository.getAllMoviesByKeyWordAndNowShowing( keyword, true ) );
        model.addAttribute( "comingSoonMovieList", movieRepository.getAllMoviesByKeyWordAndNowShowing( keyword, false ) );
        return "search";
    }

    @GetMapping ( "/about-us" )
    public String aboutUs () {
        return "about-us";
    }

    @GetMapping ( "/movie-input" )
    public String movieInput () {
        return "movie-input";
    }

    @PostMapping ( "/movie-input" )
    public String movieInputProcess ( @RequestParam String title, @RequestParam String posterUrl, @RequestParam String description, @RequestParam String releaseDate, @RequestParam String nowShowing, @RequestParam String trailerUrl, @RequestParam String genre, @RequestParam String director, @RequestParam String actors, @RequestParam String duration, @RequestParam String language, @RequestParam String rated, @RequestParam String bannerUrl ) {
        movieRepository.save( new Movie( title.trim(), posterUrl.trim(), description.trim(), releaseDate.trim(), nowShowing.trim(), trailerUrl.trim(), genre.trim(), director.trim(), actors.trim(), duration.trim(), language.trim(), rated.trim(), bannerUrl.trim() ) );
        return "movie-input";
    }
}
