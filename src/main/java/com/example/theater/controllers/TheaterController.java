package com.example.theater.controllers;

import com.example.theater.models.Movie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TheaterController {

    @GetMapping("/now-showing")
    public String nowShowing(Model model) {
        Movie movie = new Movie("Spider man", "https://resizing.flixster.com/-XZAfHZM39UwaGJIFWKAE8fS0ak=/v3/t/assets/p8548776_p_v13_bf.jpg", "A mind-bending thriller", "2010-07-16");
        model.addAttribute("movie", movie);
        return "now-showing";
    }

    @GetMapping("/coming-soon")
    public String comingSoon() {
        return "coming-soon";
    }
}
