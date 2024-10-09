package com.example.theater.controllers;

import com.example.theater.models.BookedSeat;
import com.example.theater.repositories.BookedSeatRepo;
import com.example.theater.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Controller
public class SeatController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private BookedSeatRepo bookedSeatRepo;

    // Thêm biến toàn cục để lưu trữ thông tin vé đã đặt
    private String movieTitle;
    private String time;
    private String date;

    // lưu lỗi
    private String errorReport = "";

    private final Set<Integer> bookedSeats = new TreeSet<>(); // test

    @GetMapping("/booking")
    public String booking(@RequestParam("title") String title, Model model) {
        movieTitle = title;
        time = "09:00";
        date = LocalDate.now().toString();
        model.addAttribute("movie", movieRepository.findByTitle(title));
        model.addAttribute("localDate", LocalDate.now());
        model.addAttribute("localTime", "09:00");
        model.addAttribute("errorReport", errorReport);

        // Truy vấn tất cả vé đã đặt của 1 bộ phim trong 1 ngày giờ cụ thể
        bookedSeats.clear();
        bookedSeats.addAll(bookedSeatRepo.findAllSeatNoBy(title, "09:00", LocalDate.now().toString()));
        model.addAttribute("bookedSeats", bookedSeats);
        return "booking";
    }

    @PostMapping("/select")
    public String index(@RequestParam("title") String title, @RequestParam("localTime") String localTime, @RequestParam("localDate") String localDate,
            Model model) {
        movieTitle = title;
        time = localTime;
        date = localDate;
        model.addAttribute("title", title);
        model.addAttribute("localTime", localTime);
        model.addAttribute("localDate", localDate);
        model.addAttribute("movie", movieRepository.findByTitle(title));
        model.addAttribute("errorReport", errorReport);

        // Truy vấn tất cả vé đã đặt của 1 bộ phim trong 1 ngày giờ cụ thể
        bookedSeats.clear();
        bookedSeats.addAll(bookedSeatRepo.findAllSeatNoBy(title, localTime, localDate));
        model.addAttribute("bookedSeats", bookedSeats);
        return "booking";
    }

    @PostMapping("/book-seat")
    public String bookSeat(@RequestParam(value = "selectedSeats", required = false) List<Integer> selectedSeats, @RequestParam("title") String title,
            Model model) {
        if (selectedSeats == null || selectedSeats.isEmpty()) {
            errorReport = "Vui lòng chọn ghế.";
            return "redirect:/booking?title=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
        }
        List<Integer> unavailableSeats = new ArrayList<>();
        for (int selectedSeat : selectedSeats) {
            if (bookedSeatRepo.existsBySeatNoAndMovieTitleAndTimeAndDate(selectedSeat, title, time, date)) { // kiểm tra có người nhanh tay hơn
                unavailableSeats.add(selectedSeat);
            }
        }
        errorReport = "Ghế ";
        if(unavailableSeats.size() > 0) {
            for (int unavailableSeat : unavailableSeats) {
                if (unavailableSeat <= 20) {
                    errorReport += "A";
                }
                else if (unavailableSeat <= 40) {
                    errorReport += "B";
                }
                else if (unavailableSeat <= 60) {
                    errorReport += "C";
                }
                else if (unavailableSeat <= 80) {
                    errorReport += "D";
                }
                else if (unavailableSeat <= 100) {
                    errorReport += "E";
                }
                else {
                    errorReport += "F";
                }
                errorReport += (unavailableSeat % 20 == 0 ? 20 : unavailableSeat % 20);
                if (unavailableSeats.indexOf(unavailableSeat) != unavailableSeats.size() - 1) {
                    errorReport += ", ";
                }
            }
            errorReport += " đã có người đặt trước, vui lòng chọn ghế khác.";
            return "redirect:/booking?title=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
        }
        for (int selectedSeat : selectedSeats) {
            // Lưu thông tin vé bao gồm tên phim, ngày giờ, số ghế vào cơ sở dữ liệu
            BookedSeat bookedSeat =
                    new BookedSeat(movieTitle, time, date, selectedSeat, SecurityContextHolder.getContext().getAuthentication().getName());
            bookedSeatRepo.save(bookedSeat);
        }
        errorReport = "";
        bookedSeats.clear();
        bookedSeats.addAll(selectedSeats);
        model.addAttribute("allSelectedSeats", selectedSeats);
        model.addAttribute("movie", movieRepository.findByTitle(title));
        model.addAttribute("bookedSeats", bookedSeats);
        model.addAttribute("showTime", time);
        model.addAttribute("showDate", date);
        return "bill";
    }
}
