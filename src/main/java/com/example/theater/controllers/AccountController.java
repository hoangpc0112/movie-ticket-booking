package com.example.theater.controllers;

import com.example.theater.models.AppUser;
import com.example.theater.models.RegisterDTO;
import com.example.theater.repositories.AppUserRepo;
import com.example.theater.repositories.BookedSeatRepo;
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

@Controller
public class AccountController {

    @Autowired
    private AppUserRepo userRepo;

    @Autowired
    private BookedSeatRepo bookedSeatRepo;

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

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            var bCryptEncoder = new BCryptPasswordEncoder();

            AppUser user = new AppUser();
            user.setUsername(registerDTO.getUsername());
            user.setPassword(bCryptEncoder.encode(registerDTO.getPassword()));

            userRepo.save(user);

            model.addAttribute("registerDTO", new RegisterDTO());
            model.addAttribute("success", true);
        }
        catch (Exception e) {
            bindingResult.addError(new FieldError("registerDTO", "username", e.getMessage()));
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
}
