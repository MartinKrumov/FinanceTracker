package com.financetracker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/")
    public ModelAndView hello() {
        return new ModelAndView("home/index");
    }

    @GetMapping("/home")
    public ModelAndView home(){
        return new ModelAndView("home/home");
    }
}
