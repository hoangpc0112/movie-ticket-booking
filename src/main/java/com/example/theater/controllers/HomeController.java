package com.example.theater.controllers;

import com.example.theater.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    MovieService movieService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("movieList", movieService.getAllMovie());
        return "home";
    }
}
