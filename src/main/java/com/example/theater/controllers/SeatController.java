package com.example.theater.controllers;

import com.example.theater.models.Seat;
import com.example.theater.repositories.MovieRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class SeatController {

    @Autowired
    private MovieRepository movieRepository;

    private final List<Integer> bookedSeats = new ArrayList<>(); // test

    @GetMapping("/booking")
    public String booking(@RequestParam("title") String title, Model model) {
        model.addAttribute("movie", movieRepository.findByTitle(title));
        model.addAttribute("localDate", LocalDate.now());
        model.addAttribute("localTime", "09:00");
        model.addAttribute("bookedSeats", bookedSeats);
        return "booking";
    }

    @PostMapping("/select")
    public String index(@RequestParam("localTime") String localTime,@RequestParam("localDate") String localDate, @RequestParam("title") String title, Model model) {
        model.addAttribute("localTime", localTime);
        model.addAttribute("localDate", localDate);
        model.addAttribute("movie", movieRepository.findByTitle(title));
        model.addAttribute("bookedSeats", bookedSeats);
        return "booking";
    }

    @PostMapping("/book-seat")
    public String bookSeat(@RequestParam(value = "selectedSeats", required = false) List<Integer> selectedSeats, Model model) {
        if(selectedSeats != null) {
            bookedSeats.addAll(selectedSeats);
            model.addAttribute("allSelectedSeats", selectedSeats.size());
        }
        else {
            model.addAttribute("allSelectedSeats", 0);
        }
        model.addAttribute("bookedSeats", bookedSeats);
        return "test";
    }
}

