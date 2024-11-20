package com.example.theater.controllers;

import com.example.theater.entities.*;
import com.example.theater.repositories.*;
import jakarta.servlet.http.HttpServletRequest;
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

@Controller
public class TicketController {

    private final List <Integer> bookedSeats = new ArrayList <>();

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BillRepository billRepository;

    private String movieTitle;
    private String showTime;
    private String showDate;
    private String errorReport = "";

    public AppUser getLoggedUser () {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserRepository.findByUsername(username);
    }

    @GetMapping ("/details")
    public String test (@RequestParam ("title") String title, Model model) {
        model.addAttribute("errorReport", errorReport);
        Movie movie = movieRepository.findByTitle(title);
        model.addAttribute("movie", movie);
        model.addAttribute("comments", movie.getComments());
        errorReport = "";
        return "details";
    }

    @PostMapping ("/details")
    public String details (@RequestParam String title, @RequestParam String content) {
        Movie movie = movieRepository.findByTitle(title);
        commentRepository.save(new Comment(SecurityContextHolder.getContext().getAuthentication().getName(), content.trim(), movie));
        return "redirect:/details?title=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
    }

    @GetMapping ("/booking")
    public String booking (@RequestParam ("title") String title, Model model) {
        if (!movieRepository.findByTitle(title).isNowShowing()) {
            errorReport = "Xin lỗi quý khách, phim hiện tại chưa chiếu.";
            return "redirect:/details?title=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
        }
        movieTitle = title;
        if (LocalTime.now().isBefore(LocalTime.of(9, 0))) {
            showTime = "09:00";
            showDate = LocalDate.now().format(dateFormatter);
        }
        else if (LocalTime.now().isBefore(LocalTime.of(12, 0))) {
            showTime = "12:00";
            showDate = LocalDate.now().format(dateFormatter);
        }
        else if (LocalTime.now().isBefore(LocalTime.of(15, 0))) {
            showTime = "15:00";
            showDate = LocalDate.now().format(dateFormatter);
        }
        else if (LocalTime.now().isBefore(LocalTime.of(18, 0))) {
            showTime = "18:00";
            showDate = LocalDate.now().format(dateFormatter);
        }
        else if (LocalTime.now().isBefore(LocalTime.of(21, 0))) {
            showTime = "21:00";
            showDate = LocalDate.now().format(dateFormatter);
        }
        else {
            showTime = "09:00";
            showDate = LocalDate.now().plusDays(1).format(dateFormatter);
        }
        model.addAttribute("movie", movieRepository.findByTitle(title));
        model.addAttribute("localDate", LocalDate.parse(showDate, dateFormatter));
        model.addAttribute("localTime", showTime);
        model.addAttribute("errorReport", errorReport);
        errorReport = "";

        bookedSeats.clear();
        bookedSeats.addAll(ticketRepository.findAllSeatNoBy(title, showTime, showDate));
        model.addAttribute("bookedSeats", bookedSeats);
        return "booking";
    }

    @GetMapping ("/select")
    public String select (@RequestParam ("title") String title, @RequestParam ("localTime") String localTime, @RequestParam ("localDate") String localDate, Model model) {
        if (!movieRepository.findByTitle(title).isNowShowing()) {
            errorReport = "Xin lỗi quý khách, phim hiện tại chưa chiếu.";
            return "redirect:/details?title=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
        }
        movieTitle = title;
        showTime = localTime;
        showDate = LocalDate.parse(localDate).format(dateFormatter);
        if (LocalDate.now().isAfter(LocalDate.parse(showDate, dateFormatter)) || (LocalDate.now().equals(LocalDate.parse(showDate, dateFormatter)) && LocalTime.now().isAfter(LocalTime.parse(showTime)))) {
            errorReport = "Xin lỗi, bạn đã chọn một thời gian chiếu đã qua. Vui lòng chọn một thời gian khác.";
            return "redirect:/booking?title=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
        }
        model.addAttribute("title", title);
        model.addAttribute("localTime", localTime);
        model.addAttribute("localDate", LocalDate.parse(showDate, dateFormatter));
        model.addAttribute("movie", movieRepository.findByTitle(title));
        model.addAttribute("errorReport", errorReport);
        errorReport = "";

        bookedSeats.clear();
        bookedSeats.addAll(ticketRepository.findAllSeatNoBy(title, localTime, showDate));
        model.addAttribute("bookedSeats", bookedSeats);
        return "booking";
    }

    @GetMapping ("/bill")
    public String bookSeat (@RequestParam String title,
                            @RequestParam (required = false) List <Integer> selectedSeats,
                            @RequestParam (required = false) String[] food,
                            @RequestParam (required = false) Integer popcornQty,
                            @RequestParam (required = false) Integer drinkQty,
                            @RequestParam (required = false) Integer comboQty,
                            Model model,
                            HttpServletRequest request) {
        if (!movieRepository.findByTitle(title).isNowShowing()) {
            errorReport = "Xin lỗi quý khách, phim hiện tại chưa chiếu.";
            return "redirect:/details?title=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
        }
        if (selectedSeats == null || selectedSeats.isEmpty()) {
            errorReport = "Vui lòng chọn ghế.";
            if (request.getHeader("Referer").contains("select")) {
                return "redirect:/select?title=" + URLEncoder.encode(title, StandardCharsets.UTF_8) + "&localTime=" + showTime + "&localDate=" + LocalDate.parse(showDate, dateFormatter);
            }
            else {
                return "redirect:/booking?title=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
            }
        }
        errorReport = "";
        List <Integer> unavailableSeats = new ArrayList <>();
        for (int selectedSeat : selectedSeats) {
            if (ticketRepository.existsBySeatNoAndMovieTitleAndTimeAndDate(selectedSeat, title, showTime, showDate)) {
                unavailableSeats.add(selectedSeat);
                errorReport += getSeatLabel(selectedSeat) + ", ";
            }
        }
        if (!unavailableSeats.isEmpty()) {
            errorReport = errorReport.substring(0, errorReport.length() - 2);
            errorReport += " đã có người đặt trước, vui lòng chọn ghế khác.";
            if (request.getHeader("Referer").contains("select")) {
                return "redirect:/select?title=" + URLEncoder.encode(title, StandardCharsets.UTF_8) + "&localTime=" + showTime + "&localDate=" + LocalDate.parse(showDate, dateFormatter);
            }
            else {
                return "redirect:/booking?title=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
            }
        }
        errorReport = "";

        String bookTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));

        int totalPrice = selectedSeats.size() * 50;
        List <Food> foods = new ArrayList <>();
        if (food != null) {
            for (String item : food) {
                switch (item) {
                    case "popcorn":
                        int popcornQuantity = (popcornQty != null) ? popcornQty : 1;
                        totalPrice += 30 * popcornQuantity;
                        foods.add(new Food("Bỏng ngô", popcornQuantity));
                        break;
                    case "drink":
                        int drinkQuantity = (drinkQty != null) ? drinkQty : 1;
                        totalPrice += 20 * drinkQuantity;
                        foods.add(new Food("Đồ uống", drinkQuantity));
                        break;
                    case "combo":
                        int comboQuantity = (comboQty != null) ? comboQty : 1;
                        totalPrice += 40 * comboQuantity;
                        foods.add(new Food("Combo: Bỏng ngô + Đồ uống", comboQuantity));
                        break;
                    default:
                        break;
                }
            }
        }

        List <Ticket> tickets = new ArrayList <>();
        for (int selectedSeat : selectedSeats) {
            Ticket ticket = new Ticket(title, showTime, showDate, selectedSeat, getSeatLabel(selectedSeat));
            tickets.add(ticket);
        }

        Bill bill = new Bill(totalPrice, bookTime, getLoggedUser(), tickets, foods);

        for (Ticket ticket : tickets) {
            ticket.setBill(bill);
        }
        for (Food foodItem : foods) {
            foodItem.setBill(bill);
        }

        billRepository.save(bill);
        ticketRepository.saveAll(tickets);
        foodRepository.saveAll(foods);

        model.addAttribute("bill", bill);
        return "bill";
    }

    private String getSeatLabel (int seatNo) {
        if (seatNo <= 20) {
            return "A" + (seatNo % 20 == 0 ? 20 : seatNo % 20);
        }
        else if (seatNo <= 40) {
            return "B" + (seatNo % 20 == 0 ? 20 : seatNo % 20);
        }
        else if (seatNo <= 60) {
            return "C" + (seatNo % 20 == 0 ? 20 : seatNo % 20);
        }
        else if (seatNo <= 80) {
            return "D" + (seatNo % 20 == 0 ? 20 : seatNo % 20);
        }
        else if (seatNo <= 100) {
            return "E" + (seatNo % 20 == 0 ? 20 : seatNo % 20);
        }
        else {
            return "F" + (seatNo % 20 == 0 ? 20 : seatNo % 20);
        }
    }

    @PostMapping ("/cancel-ticket")
    public String cancelTicket (@RequestParam ("seatId") String seatId) {
        Ticket ticket = ticketRepository.findById(Long.parseLong(seatId));

        // không cho huỷ vé nếu đã qua tgian chiếu
        if (LocalDate.now().isAfter(LocalDate.parse(ticket.getDate(), dateFormatter)) || (LocalDate.now().equals(LocalDate.parse(ticket.getDate(), dateFormatter)) && LocalTime.now().isAfter(LocalTime.parse(ticket.getTime())))) {
            return "redirect:/profile?expiredTicket=true";
        }

        ticketRepository.delete(ticket);

        Bill bill = ticket.getBill();
        bill.setTotalPrice(bill.getTotalPrice() - 50); // Trừ tiền vé vừa huỷ
        billRepository.save(bill);

        return "redirect:/profile?cancelTicket=true";
    }
}