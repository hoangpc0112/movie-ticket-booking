package com.example.theater.controllers;

import com.example.theater.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SeatController {

    @Autowired
    private MovieRepository movieRepository;

    @PostMapping("/select")
    public String index(@RequestParam("localTime") String localTime,@RequestParam("localDate") String localDate, @RequestParam("title") String title, Model model) {
        model.addAttribute("localTime", localTime);
        model.addAttribute("localDate", localDate);
        model.addAttribute("movie", movieRepository.findByTitle(title));
        return "booking";
    }
}

