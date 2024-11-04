package com.example.theater.controllers;

import com.example.theater.entities.Ticket;
import com.example.theater.entities.Comment;
import com.example.theater.entities.Movie;
import com.example.theater.repositories.TicketRepository;
import com.example.theater.repositories.CommentRepository;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Controller
public class TicketController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CommentRepository commentRepository;

    // Thêm biến toàn cục để lưu trữ thông tin vé đã đặt
    private String movieTitle;
    private String showTime;
    private String showDate;

    // lưu lỗi
    private String errorReport = "";

    private final Set <Integer> bookedSeats = new TreeSet <>(); // test

    @GetMapping ("/details")
    public String test (@RequestParam ("title") String title, Model model) {
        model.addAttribute("errorReport", errorReport);
        Movie movie = movieRepository.findByTitle(title);
        model.addAttribute("movie", movie);
        model.addAttribute("comments", commentRepository.findAllByMovie(movie));
        errorReport = "";
        return "details";
    }

    @PostMapping ("/details")
    public String details (@RequestParam String title, @RequestParam String content) {
        Movie movie = movieRepository.findByTitle(title);
        commentRepository.save(new Comment(movie, SecurityContextHolder.getContext().getAuthentication().getName(), content.trim()));
        return "redirect:/details?title=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
    }

    @GetMapping ("/booking")
    public String booking (@RequestParam ("title") String title, Model model) {
        if (!movieRepository.findByTitle(title).isNowShowing()) { // người dùng cố gắng truy cập vào phần đặt vé của phim sắp chiếu
            errorReport = "Xin lỗi quý khách, phim hiện tại chưa chiếu.";
            return "redirect:/details?title=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
        }
        movieTitle = title;
        // time = "09:00";
        // System.out.println(LocalTime.now());
        if (LocalTime.now().isBefore(LocalTime.of(9, 0))) {
            showTime = "09:00";
            showDate = LocalDate.now().toString();
        }
        else if (LocalTime.now().isBefore(LocalTime.of(12, 0))) {
            showTime = "12:00";
            showDate = LocalDate.now().toString();
        }
        else if (LocalTime.now().isBefore(LocalTime.of(15, 0))) {
            showTime = "15:00";
            showDate = LocalDate.now().toString();
        }
        else if (LocalTime.now().isBefore(LocalTime.of(18, 0))) {
            showTime = "18:00";
            showDate = LocalDate.now().toString();
        }
        else if (LocalTime.now().isBefore(LocalTime.of(21, 0))) {
            showTime = "21:00";
            showDate = LocalDate.now().toString();
        }
        else {
            showTime = "09:00";
            showDate = LocalDate.now().plusDays(1).toString();
            // System.out.println(date);
        }
        model.addAttribute("movie", movieRepository.findByTitle(title));
        model.addAttribute("localDate", showDate);
        model.addAttribute("localTime", showTime);
        model.addAttribute("errorReport", errorReport);
        errorReport = "";

        // Truy vấn tất cả vé đã đặt của 1 bộ phim trong 1 ngày giờ cụ thể
        bookedSeats.clear();
        bookedSeats.addAll(ticketRepository.findAllSeatNoBy(title, showTime, showDate));
        model.addAttribute("bookedSeats", bookedSeats);
        return "booking";
    }

    @GetMapping ("/select")
    public String select (@RequestParam ("title") String title, @RequestParam ("localTime") String localTime, @RequestParam ("localDate") String localDate, Model model) {
        if (!movieRepository.findByTitle(title).isNowShowing()) { // người dùng cố gắng truy cập vào phần đặt vé của phim sắp chiếu
            errorReport = "Xin lỗi quý khách, phim hiện tại chưa chiếu.";
            return "redirect:/details?title=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
        }
        errorReport = "";
        movieTitle = title;
        showTime = localTime;
        showDate = localDate;
        // System.out.println(showTime + " " + showDate);
        if (LocalDate.now().isAfter(LocalDate.parse(showDate)) || (LocalDate.now().toString().equals(showDate) && LocalTime.now().isAfter(LocalTime.parse(showTime)))) {
            errorReport = "Xin lỗi, bạn đã chọn một thời gian chiếu đã qua. Vui lòng chọn một thời gian khác.";
            return "redirect:/booking?title=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
        }
        errorReport = "";
        model.addAttribute("title", title);
        model.addAttribute("localTime", localTime);
        model.addAttribute("localDate", localDate);
        model.addAttribute("movie", movieRepository.findByTitle(title));
        model.addAttribute("errorReport", errorReport);

        // Truy vấn tất cả vé đã đặt của 1 bộ phim trong 1 ngày giờ cụ thể
        bookedSeats.clear();
        bookedSeats.addAll(ticketRepository.findAllSeatNoBy(title, localTime, localDate));
        model.addAttribute("bookedSeats", bookedSeats);
        return "booking";
    }

    @GetMapping ("/bill")
    public String bookSeat (@RequestParam (value = "selectedSeats", required = false) List <Integer> selectedSeats, @RequestParam ("title") String title, Model model) {
        if (!movieRepository.findByTitle(title).isNowShowing()) { // người dùng cố gắng truy cập vào phần đặt vé của phim sắp chiếu
            errorReport = "Xin lỗi quý khách, phim hiện tại chưa chiếu.";
            return "redirect:/details?title=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
        }
        if (selectedSeats == null || selectedSeats.isEmpty()) {
            errorReport = "Vui lòng chọn ghế.";
            return "redirect:/booking?title=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
        }
        errorReport = "";
        List <Integer> unavailableSeats = new ArrayList <>();
        for (int selectedSeat : selectedSeats) {
            if (ticketRepository.existsBySeatNoAndMovieTitleAndTimeAndDate(selectedSeat, title, showTime, showDate)) { // kiểm tra có người nhanh tay hơn
                unavailableSeats.add(selectedSeat);
            }
        }
        errorReport = "Ghế ";
        if (!unavailableSeats.isEmpty()) {
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
        errorReport = "";
        String bookTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
        for (int selectedSeat : selectedSeats) {
            // Lưu thông tin vé bao gồm tên phim, ngày giờ, số ghế vào cơ sở dữ liệu
            Ticket ticket = new Ticket(movieTitle, showTime, LocalDate.parse(showDate).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), selectedSeat, SecurityContextHolder.getContext().getAuthentication().getName(), bookTime);
            ticketRepository.save(ticket);
        }
        bookedSeats.clear();
        bookedSeats.addAll(selectedSeats);
        model.addAttribute("allSelectedSeats", selectedSeats);
        model.addAttribute("movie", movieRepository.findByTitle(title));
        model.addAttribute("bookedSeats", bookedSeats);
        model.addAttribute("showTime", showTime);
        model.addAttribute("showDate", LocalDate.parse(showDate).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        model.addAttribute("bookTime", bookTime);
        return "bill";
    }

    @PostMapping ("/cancel-ticket")
    public String cancelTicket (@RequestParam ("seatId") String seatId) {
        Ticket seat = ticketRepository.findById(Long.parseLong(seatId));

        // không cho huỷ vé nếu đã qua tgian chiếu
        if (LocalDate.now().isAfter(LocalDate.parse(seat.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"))) || (LocalDate.now().equals(LocalDate.parse(seat.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"))) && LocalTime.now().isAfter(LocalTime.parse(seat.getTime())))) {
            return "redirect:/profile?expiredTicket=true";
        }

        ticketRepository.delete(seat);
        return "redirect:/profile?cancelTicket=true";
    }
}
