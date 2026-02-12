package com.example.mailmanagerwebsite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EntryController {

    @GetMapping("/entry")
    public String entry(Model model) {
        model.addAttribute("test", "Shit");
        return "entry";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

}
