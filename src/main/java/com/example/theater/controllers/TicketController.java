package com.example.theater.controllers;

import com.example.theater.entities.*;
import com.example.theater.repositories.*;
import com.example.theater.services.VNPAYService;
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

    @Autowired
    private VNPAYService vnPayService;

    private List <Food> foodList = new ArrayList <>();
    private List <Ticket> ticketList = new ArrayList <>();
    private Bill bill = new Bill();

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
            errorReport = "Xin lỗi, quý khách đã chọn một thời gian chiếu không hợp lệ. Vui lòng chọn một thời gian khác.";
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
        bill.setTotalPrice(bill.getTotalPrice() - 50000); // Trừ tiền vé vừa huỷ
        billRepository.save(bill);

        return "redirect:/profile?cancelTicket=true";
    }

    // Chuyển hướng người dùng đến cổng thanh toán VNPAY
    @PostMapping ("/submitOrder")
    public String submidOrder (@RequestParam String title,
                               @RequestParam (required = false) List <Integer> selectedSeats,
                               @RequestParam (required = false) String[] food,
                               @RequestParam (required = false) Integer popcornQty,
                               @RequestParam (required = false) Integer drinkQty,
                               @RequestParam (required = false) Integer comboQty,
                               @RequestParam ("amount") String orderTotal,
                               @RequestParam (value = "orderInfo") String orderInfo,
                               HttpServletRequest request) {
        if (!movieRepository.findByTitle(title).isNowShowing()) {
            errorReport = "Xin lỗi quý khách, phim hiện tại chưa chiếu.";
            return "redirect:/details?title=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
        }
        if (selectedSeats == null || selectedSeats.isEmpty()) {
            errorReport = "Quý khách vui lòng chọn ghế trước khi thanh toán.";
            if (request.getHeader("Referer").contains("select")) {
                return "redirect:/select?title=" + URLEncoder.encode(title, StandardCharsets.UTF_8) + "&localTime=" + showTime + "&localDate=" + LocalDate.parse(showDate, dateFormatter);
            }
            else {
                return "redirect:/booking?title=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
            }
        }
        errorReport = "Ghế ";
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

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(request, Integer.parseInt(orderTotal), orderInfo, baseUrl);

        List <Food> foods = new ArrayList <>();
        List <Ticket> tickets = new ArrayList <>();

        if (food != null) {
            for (String item : food) {
                switch (item) {
                    case "popcorn":
                        int popcornQuantity = (popcornQty != null) ? popcornQty : 1;
                        foods.add(new Food("Bỏng ngô", popcornQuantity));
                        break;
                    case "drink":
                        int drinkQuantity = (drinkQty != null) ? drinkQty : 1;
                        foods.add(new Food("Đồ uống", drinkQuantity));
                        break;
                    case "combo":
                        int comboQuantity = (comboQty != null) ? comboQty : 1;
                        foods.add(new Food("Combo: Bỏng ngô + Đồ uống", comboQuantity));
                        break;
                    default:
                        break;
                }
            }
        }

        for (int selectedSeat : selectedSeats) {
            Ticket ticket = new Ticket(title, showTime, showDate, selectedSeat, getSeatLabel(selectedSeat));
            tickets.add(ticket);
        }

        foodList = foods;
        ticketList = tickets;

        return "redirect:" + vnpayUrl;
    }

    // Sau khi hoàn tất thanh toán, VNPAY sẽ chuyển hướng trình duyệt về URL này
    @GetMapping ("/vnpay-payment-return")
    public String paymentCompleted (HttpServletRequest request, Model model) {
        int paymentStatus = vnPayService.orderReturn(request);

        String paymentTime = request.getParameter("vnp_PayDate");
        String totalPrice = request.getParameter("vnp_Amount");

        if (paymentStatus == 1) {
            bill = new Bill(Integer.parseInt(totalPrice) / 100, LocalDateTime.parse(paymentTime, DateTimeFormatter.ofPattern("yyyyMMddHHmmss")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), getLoggedUser(), ticketList, foodList);

            for (Ticket ticket : ticketList) {
                ticket.setBill(bill);
            }
            for (Food foodItem : foodList) {
                foodItem.setBill(bill);
            }

            billRepository.save(bill);
            ticketRepository.saveAll(ticketList);
            foodRepository.saveAll(foodList);
        }

        model.addAttribute("bill", bill);

        return paymentStatus == 1 ? "bill" : "orderFailed";
    }
}