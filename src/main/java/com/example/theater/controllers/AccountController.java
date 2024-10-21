package com.example.theater.controllers;

import com.example.theater.entities.AppUser;
import com.example.theater.DTOs.RegisterDTO;
import com.example.theater.repositories.AppUserRepo;
import com.example.theater.repositories.BookedSeatRepo;
import com.example.theater.services.MailSenderService;
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

    @Autowired
    private AppUserRepo userRepo;

    @Autowired
    private BookedSeatRepo bookedSeatRepo;

    @Autowired
    private MailSenderService mailSenderService;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        model.addAttribute("success", false);
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerDTO") RegisterDTO registerDTO, BindingResult bindingResult, Model model) {
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            bindingResult.addError(new FieldError("registerDTO", "confirmPassword", "Mật khẩu và mật khẩu xác nhận lại không giống nhau."));
        }

        AppUser appUser = userRepo.findByUsername(registerDTO.getUsername());
        if (appUser != null) {
            bindingResult.addError(new FieldError("registerDTO", "username", "Username đã có người sử dụng"));
        }

        appUser = userRepo.findByEmail(registerDTO.getEmail());
        if(appUser != null) {
            bindingResult.addError(new FieldError("registerDTO", "email", "Email đã có người sử dụng"));
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            var bCryptEncoder = new BCryptPasswordEncoder();

            AppUser user = new AppUser();
            user.setEmail(registerDTO.getEmail());
            user.setUsername(registerDTO.getUsername());
            user.setPassword(bCryptEncoder.encode(registerDTO.getPassword()));
            user.setEmailOtp("");

            userRepo.save(user);

            model.addAttribute("registerDTO", new RegisterDTO());
            model.addAttribute("success", true);
        }
        catch (Exception e) {
            bindingResult.addError(new FieldError("registerDTO", "email", e.getMessage()));
        }

        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("orderHistory", bookedSeatRepo.findByUser(currentUser));
        model.addAttribute("user", userRepo.findByUsername(currentUser));
        return "profile";
    }

    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void resetOtp(AppUser user) {
        user.setEmailOtp("");
        userRepo.save(user);
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        model.addAttribute("success", false);
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, Model model) {
        if (userRepo.existsByEmail(email)) {
            String otp = generateOtp();
            AppUser user = userRepo.findByEmail(email);
            user.setEmailOtp(otp);
            userRepo.save(user);
            scheduledExecutorService.schedule(() -> resetOtp(user), 5, TimeUnit.MINUTES);
            mailSenderService.sendMail(email, "OTP cho OOP16", "Mã OTP của bạn là: " + otp + ".\n" + "OTP sẽ hết hạn trong 5 phút.");
            model.addAttribute("email", email);
            return "otp-check";
        } else {
            model.addAttribute("error", "Email không đúng, vui lòng thử lại.");
            return "forgot-password";
        }
    }


    @GetMapping("/change-password")
    public String changePassword(@RequestParam("email") String email, @RequestParam("otp") String otp, Model model) {
        AppUser user = userRepo.findByEmail(email);
        model.addAttribute("email", email);
        if(user.getEmailOtp().isEmpty()) {
            model.addAttribute("error", "Mã OTP đã hết hạn, vui lòng thử lại.");
            return "otp-check";
        }
        if (!user.getEmailOtp().equals(otp)) {
            model.addAttribute("error", "OTP không đúng, vui lòng thử lại.");
            return "otp-check";
        }
        return "change-password";
    }


    @PostMapping("/change-password")
    public String changePasswordProcess(@RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword, Model model) {
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
        return "login";
    }

}
