package com.example.mailmanagerwebsite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EntryController {

    @GetMapping("/entry")
    public String entry() {
        return "entry";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/setting")
    public String setting() {
        return "setting";
    }

}
