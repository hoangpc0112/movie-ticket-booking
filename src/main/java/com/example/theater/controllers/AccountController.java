package com.example.theater.controllers;

import com.example.theater.DTOs.RegisterDTO;
import com.example.theater.entities.AppUser;
import com.example.theater.repositories.AppUserRepository;
import com.example.theater.repositories.BillRepository;
import com.example.theater.repositories.TicketRepository;
import com.example.theater.services.MailSenderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
public class AccountController {

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @Autowired
    private AppUserRepository userRepo;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private BillRepository billRepository;

    @GetMapping ("/register")
    public String register (Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        model.addAttribute("success", false);
        return "register";
    }

    @PostMapping ("/register")
    public String register (@Valid @ModelAttribute ("registerDTO") RegisterDTO registerDTO, BindingResult bindingResult, Model model, HttpSession session) {
        if (registerDTO.getUsername().contains(" ")) {
            bindingResult.addError(new FieldError("registerDTO", "username", "Username không được chứa dấu cách."));
        }

        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            bindingResult.addError(new FieldError("registerDTO", "confirmPassword", "Mật khẩu và mật khẩu xác nhận lại không giống nhau."));
        }
        AppUser appUser = userRepo.findByUsername(registerDTO.getUsername());
        if (appUser != null) {
            bindingResult.addError(new FieldError("registerDTO", "username", "Username đã có người sử dụng."));
        }
        appUser = userRepo.findByEmail(registerDTO.getEmail());
        if (appUser != null) {
            bindingResult.addError(new FieldError("registerDTO", "email", "Email đã có người sử dụng."));
        }
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            String otp = generateOtp();
            mailSenderService.sendMail(registerDTO.getEmail(), "Mã tạo tài khoản cho OOP16", "Mã tạo tài khoản của bạn là: " + otp + ".");

            session.setAttribute("otp", otp);
            session.setAttribute("tempUser", registerDTO);
            model.addAttribute("success", true);

            return "verify-email";
        } catch (Exception e) {
            bindingResult.addError(new FieldError("registerDTO", "email", e.getMessage()));
            return "register";
        }
    }

    @PostMapping ("/verify-email")
    public String verifyEmail (@RequestParam ("otp") String otp, HttpSession session, Model model) {
        // System.out.println(otp);

        String sessionOtp = (String) session.getAttribute("otp");
        RegisterDTO registerDTO = (RegisterDTO) session.getAttribute("tempUser");

        if (sessionOtp != null && sessionOtp.equals(otp)) {
            try {
                var bCryptEncoder = new BCryptPasswordEncoder();
                AppUser user = new AppUser();
                user.setEmail(registerDTO.getEmail());
                user.setUsername(registerDTO.getUsername());
                user.setPassword(bCryptEncoder.encode(registerDTO.getPassword()));
                user.setEmailOtp("");
                userRepo.save(user);

                session.removeAttribute("otp");
                session.removeAttribute("tempUser");

                model.addAttribute("registerDTO", new RegisterDTO());
                model.addAttribute("success", true);
                return "register";
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
                return "verify-email";
            }
        }
        else {
            model.addAttribute("error", "Mã OTP không hợp lệ.");
            return "verify-email";
        }
    }

    @GetMapping ("/login")
    public String login () {
        return "login";
    }

    @GetMapping ("/profile")
    public String profile (Model model, @RequestParam (value = "successChangePassword", required = false, defaultValue = "false") String successChangePassword, @RequestParam (value = "cancelTicket", required = false, defaultValue = "false") String cancelTicket, @RequestParam (name = "expiredTicket", required = false) String expiredTicket, @RequestParam (name = "updateProfile", required = false) String updateProfile) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("orderHistory", billRepository.findByUser(currentUser));
        model.addAttribute("user", userRepo.findByUsername(currentUser));
        model.addAttribute("successChangePassword", successChangePassword);
        model.addAttribute("cancelTicket", cancelTicket);
        model.addAttribute("expiredTicket", expiredTicket);
        model.addAttribute("updateProfile", updateProfile);
        return "profile";
    }

    public String generateOtp () {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void resetOtp (AppUser user) {
        user.setEmailOtp("");
        userRepo.save(user);
    }

    @GetMapping ("/forgot-password")
    public String forgotPassword (Model model) {
        model.addAttribute("success", false);
        return "forgot-password";
    }

    @PostMapping ("/forgot-password")
    public String forgotPassword (@RequestParam ("email") String email, Model model) {
        if (userRepo.existsByEmail(email)) {
            String otp = generateOtp();
            AppUser user = userRepo.findByEmail(email);
            user.setEmailOtp(otp);
            userRepo.save(user);
            scheduledExecutorService.schedule(() -> resetOtp(user), 5, TimeUnit.MINUTES);
            mailSenderService.sendMail(email, "OTP cho OOP16", "Mã OTP của bạn là: " + otp + ".\n" + "OTP sẽ hết hạn trong 5 phút.");
            model.addAttribute("email", email);
            return "otp-check";
        }
        else {
            model.addAttribute("error", "Email không đúng, vui lòng thử lại.");
            return "forgot-password";
        }
    }

    @GetMapping ("/change-password")
    public String changePassword (@RequestParam ("email") String email, @RequestParam ("otp") String otp, Model model) {
        AppUser user = userRepo.findByEmail(email);
        model.addAttribute("email", email);
        if (user.getEmailOtp().isEmpty()) {
            model.addAttribute("error", "Mã OTP đã hết hạn, vui lòng thử lại.");
            return "otp-check";
        }
        if (!user.getEmailOtp().equals(otp)) {
            model.addAttribute("error", "OTP không đúng, vui lòng thử lại.");
            return "otp-check";
        }
        return "change-password";
    }

    @PostMapping ("/change-password")
    public String changePasswordProcess (@RequestParam ("email") String email, @RequestParam ("password") String password, @RequestParam ("confirmPassword") String confirmPassword, HttpServletRequest request, Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu và mật khẩu xác nhận không trùng khớp.");
            model.addAttribute("email", email);
            return "change-password";
        }
        else if (password.length() < 6) {
            model.addAttribute("error", "Mật khẩu phải có tối thiểu 6 ký tự.");
            model.addAttribute("email", email);
            return "change-password";
        }

        var bCryptEncoder = new BCryptPasswordEncoder();
        AppUser appUser = userRepo.findByEmail(email);
        appUser.setPassword(bCryptEncoder.encode(password));
        userRepo.save(appUser);
        model.addAttribute("successChangePassword", true);

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("SPRING_SECURITY_CONTEXT") != null) {
            return "redirect:/profile?successChangePassword=true";
        }
        return "login";
    }

    @GetMapping ("/user-manual")
    public String userManual () {
        return "user-manual";
    }

    @PostMapping ("/update-profile")
    public String updateProfile (@RequestParam (value = "firstName", required = false) String firstName, @RequestParam (value = "lastName", required = false) String lastName, @RequestParam (value = "age", required = false) String age, @RequestParam (value = "address", required = false) String address, Model model) {
        AppUser user = userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAge(age);
        user.setAddress(address);
        userRepo.save(user);
        return "redirect:/profile?updateProfile=true";
    }
}