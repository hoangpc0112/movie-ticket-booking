package com.example.theater.controllers;

import com.example.theater.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TheaterController {

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/now-showing")
    public String nowShowing(Model model) {
        model.addAttribute("movieList", movieRepository.getAllMoviesByNowShowing(true));
        return "now-showing";
    }

    @GetMapping("/coming-soon")
    public String comingSoon(Model model) {
        model.addAttribute("movieList", movieRepository.getAllMoviesByNowShowing(false));
        return "coming-soon";
    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        model.addAttribute("nowShowingMovieList", movieRepository.getAllMoviesByKeyWordAndNowShowing(keyword, true));
        model.addAttribute("comingSoonMovieList", movieRepository.getAllMoviesByKeyWordAndNowShowing(keyword, false));
        return "search";
    }
}
