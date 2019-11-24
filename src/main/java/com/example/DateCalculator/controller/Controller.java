package com.example.DateCalculator.controller;

import com.example.DateCalculator.TaskNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class Controller {

    @ExceptionHandler(TaskNotFoundException.class)
    public String handleException(TaskNotFoundException e, Model model){
        model.addAttribute("message", e);
        return "error/ErrorPage";
    }
}
