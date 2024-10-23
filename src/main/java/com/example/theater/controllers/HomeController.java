package com.example.theater.controllers;

import com.example.theater.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping ( "/" )
    public String home ( Model model ) {
        model.addAttribute( "nowShowingMovieList", movieRepository.getAllMoviesByNowShowing( true ) );
        model.addAttribute( "comingSoonMovieList", movieRepository.getAllMoviesByNowShowing( false ) );
        return "home";
    }
}
