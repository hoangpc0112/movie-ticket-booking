package com.example.theater.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler (Exception.class)
    public String handleException (HttpServletRequest request, Exception e) {
        System.out.println(e.getLocalizedMessage());
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode != null) {
            System.out.println(statusCode);
        }
        return "redirect:/";
    }
}
