package com.example.theater.controllers;

import com.example.theater.models.Movie;
import com.example.theater.repositories.MovieRepository;
import com.example.theater.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TheaterController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/now-showing")
    public String nowShowing(Model model) {
        model.addAttribute("movieList", movieService.getAllMovie());
        return "now-showing";
    }

    @GetMapping("/coming-soon")
    public String comingSoon() {
        return "coming-soon";
    }

    @GetMapping("/test/{id}")
    public String test(@PathVariable("id") long id, Model model) {
        Movie currentMovie = movieRepository.findById(id);
        model.addAttribute("movie", currentMovie);
        return "test";
    }

    @GetMapping("/booking/{id}")
    public String booking(@PathVariable("id") long id, Model model) {
        System.out.println(id);
        Movie currentMovie = movieRepository.findById(id);
        model.addAttribute("movie", currentMovie);
        return "booking";
    }
}
