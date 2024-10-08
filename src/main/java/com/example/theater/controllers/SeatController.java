package com.example.theater.controllers;

import com.example.theater.models.BookedSeat;
import com.example.theater.repositories.AppUserRepo;
import com.example.theater.repositories.BookedSeatRepo;
import com.example.theater.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Controller
public class SeatController {

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private BookedSeatRepo bookedSeatRepo;

    // Thêm biến toàn cục để lưu trữ thông tin vé đã đặt
    private String movieTitle;
    private String time;
    private String date;

    private final Set<Integer> bookedSeats = new TreeSet<>(); // test

    @GetMapping("/booking")
    public String booking(@RequestParam("title") String title, Model model) {
        movieTitle = title;
        time = "09:00";
        date = LocalDate.now().toString();
        model.addAttribute("movie", movieRepository.findByTitle(title));
        model.addAttribute("localDate", LocalDate.now());
        model.addAttribute("localTime", "09:00");

        // Truy vấn tất cả vé đã đặt của 1 bộ phim trong 1 ngày giờ cụ thể
        bookedSeats.clear();
        bookedSeats.addAll(bookedSeatRepo.findAllSeatNoBy(title, "09:00", LocalDate.now().toString()));
        model.addAttribute("bookedSeats", bookedSeats);
        return "booking";
    }

    @PostMapping("/select")
    public String index(@RequestParam("title") String title, @RequestParam("localTime") String localTime,
    @RequestParam("localDate") String localDate, Model model) {
        movieTitle = title;
        time = localTime;
        date = localDate;
        model.addAttribute("title", title);
        model.addAttribute("localTime", localTime);
        model.addAttribute("localDate", localDate);
        model.addAttribute("movie", movieRepository.findByTitle(title));

        // Truy vấn tất cả vé đã đặt của 1 bộ phim trong 1 ngày giờ cụ thể
        bookedSeats.clear();
        bookedSeats.addAll(bookedSeatRepo.findAllSeatNoBy(title, localTime, localDate));
        model.addAttribute("bookedSeats", bookedSeats);
        return "booking";
    }

    @PostMapping("/book-seat")
    public String bookSeat(@RequestParam(value = "selectedSeats", required = false) List<Integer> selectedSeats,@RequestParam("title") String title, Model model) throws UnsupportedEncodingException {
        if(selectedSeats != null) {
            for(int selectedSeat : selectedSeats) {
                if(bookedSeatRepo.existsBySeatNoAndMovieTitleAndTimeAndDate(selectedSeat, title, time, date)) { // kiểm tra có người nhanh tay hơn
                    return "redirect:/booking?title=" + URLEncoder.encode(title, "UTF-8");
                }
                // Lưu thông tin vé bao gồm tên phim, ngày giờ, số ghế vào cơ sở dữ liệu
                BookedSeat bookedSeat = new BookedSeat(movieTitle, time, date, selectedSeat, SecurityContextHolder.getContext().getAuthentication().getName());
                bookedSeatRepo.save(bookedSeat);
            }
            bookedSeats.clear();
            bookedSeats.addAll(selectedSeats);
            model.addAttribute("allSelectedSeats", selectedSeats);
        }
        else{
            return "redirect:/booking?title=" + URLEncoder.encode(title, "UTF-8"); // chưa chọn ghế nào
        }
        model.addAttribute("movie", movieRepository.findByTitle(title));
        model.addAttribute("bookedSeats", bookedSeats);
        return "bill";
    }
}

