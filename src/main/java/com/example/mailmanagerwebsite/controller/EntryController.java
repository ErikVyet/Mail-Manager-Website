package com.example.mailmanagerwebsite.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.mailmanagerwebsite.dto.UserDTO;
import com.example.mailmanagerwebsite.dto.UserLoginRequest;
import com.example.mailmanagerwebsite.dto.UserRegisterRequest;
import com.example.mailmanagerwebsite.service.UserService;
import com.example.mailmanagerwebsite.validation.ValidationOrder;

import jakarta.servlet.http.HttpSession;

@Controller
public class EntryController {

    private final UserService userService;

    public EntryController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("userLoginRequest", new UserLoginRequest());
        return "login";
    }

    @PostMapping("/loginSubmit")
    public String loginSubmit(@Validated(ValidationOrder.class) UserLoginRequest userLoginRequest, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("userLoginRequest", userLoginRequest);
            return "login";
        }
        Optional<UserDTO> user = userService.getUser(userLoginRequest);
        if (!user.isPresent()) {
            model.addAttribute("userLoginRequest", userLoginRequest);
            model.addAttribute("error", "User not found");
            return "login";
        }
        session.setAttribute("user", user.get());
        return "redirect:/index";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userRegisterRequest", new UserRegisterRequest());
        return "register";
    }

    @PostMapping("/registerSubmit")
    public String registerSubmit(@Validated(ValidationOrder.class) UserRegisterRequest userRegisterRequest, BindingResult result, Model model, HttpSession session) {
        if (!userRegisterRequest.getPassword().equals(userRegisterRequest.getConfirmPassword())) {
            model.addAttribute("userRegisterRequest", userRegisterRequest);
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }
        if (result.hasErrors()) {
            model.addAttribute("userRegisterRequest", userRegisterRequest);
            return "register";
        }
        Optional<UserDTO> user = userService.createUser(userRegisterRequest);
        if (!user.isPresent()) {
            model.addAttribute("userRegisterRequest", userRegisterRequest);
            model.addAttribute("error", "User already exists");
            return "register";
        }
        session.setAttribute("user", user.get());
        return "redirect:/index";
    }

    @GetMapping("/recovery")
    public String recoveryPage(Model model) {
        
        return "recovery";
    }

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        return "index";
    }

}