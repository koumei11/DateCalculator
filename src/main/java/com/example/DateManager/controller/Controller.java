package com.example.DateManager.controller;

import com.example.DateManager.NotExecuteAnyMoreException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class Controller {

    @ExceptionHandler(NotExecuteAnyMoreException.class)
    public String handleException(NotExecuteAnyMoreException e, Model model){
        model.addAttribute("message", e);
        return "error/ErrorPage";
    }
}
