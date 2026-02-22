package com.example.mailmanagerwebsite.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.mailmanagerwebsite.dto.UserDTO;
import com.example.mailmanagerwebsite.dto.UserLoginRequest;
import com.example.mailmanagerwebsite.dto.UserRecoveryRequest;
import com.example.mailmanagerwebsite.dto.UserRegisterRequest;
import com.example.mailmanagerwebsite.service.UserService;
import com.example.mailmanagerwebsite.validation.ValidationOrder;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class EntryController {

    private final UserService userService;

    public EntryController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    @GetMapping("/login")
    public String loginPage(@CookieValue(required = false) String id, Model model, HttpSession session) {
        if (id != null) {
            Optional<UserDTO> optDTO = userService.getUserForLogin(Integer.parseInt(id));
            session.setAttribute("user", optDTO.get());
            return "redirect:/index";
        }
        model.addAttribute("userLoginRequest", new UserLoginRequest());
        return "login";
    }

    @PostMapping("/loginSubmit")
    public String loginSubmit(@Validated(ValidationOrder.class) UserLoginRequest userLoginRequest, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("userLoginRequest", userLoginRequest);
            return "login";
        }
        Optional<UserDTO> user = userService.getUserForLogin(userLoginRequest);
        if (!user.isPresent()) {
            model.addAttribute("userLoginRequest", userLoginRequest);
            model.addAttribute("error", "User not found");
            return "login";
        }
        if (userLoginRequest.isRemember()) {
            Cookie cookie = new Cookie("id", user.get().getId() + "");
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 7);
            cookie.setSecure(false);
            response.addCookie(cookie);
        }
        else {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("id")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
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
        if (result.hasErrors()) {
            model.addAttribute("userRegisterRequest", userRegisterRequest);
            return "register";
        }
        if (session.getAttribute("code") == null || !session.getAttribute("code").equals(userRegisterRequest.getCode())) {
            model.addAttribute("userRegisterRequest", userRegisterRequest);
            model.addAttribute("registerError", "Invalid verification code");
            return "register";
        }
        if (!userRegisterRequest.getPassword().equals(userRegisterRequest.getConfirmPassword())) {
            model.addAttribute("userRegisterRequest", userRegisterRequest);
            model.addAttribute("registerError", "Passwords do not match");
            return "register";
        }
        Optional<UserDTO> user = userService.createUser(userRegisterRequest);
        if (!user.isPresent()) {
            model.addAttribute("userRegisterRequest", userRegisterRequest);
            model.addAttribute("registerError", "User already exists");
            return "register";
        }
        session.removeAttribute("code");
        model.addAttribute("userLoginRequest", new UserLoginRequest(userRegisterRequest.getVmail(), "", false));
        return "login";
    }

    @GetMapping("/recovery")
    public String recoveryPage(Model model) {
        model.addAttribute("userRecoveryRequest", new UserRecoveryRequest());
        return "recovery";
    }

    @PostMapping("/recoverySubmit")
    public String recoverySubmit(@Validated(ValidationOrder.class) UserRecoveryRequest userRecoveryRequest, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("userRecoveryRequest", userRecoveryRequest);
            return "recovery";
        }
        if (!userService.isExisted(userRecoveryRequest.getVmail())) {
            model.addAttribute("userRecoveryRequest", userRecoveryRequest);
            model.addAttribute("recoveryError", "User not found");
            return "recovery";
        }
        if (session.getAttribute("code") == null || !session.getAttribute("code").equals(userRecoveryRequest.getCode())) {
            model.addAttribute("userRecoveryRequest", userRecoveryRequest);
            model.addAttribute("recoveryError", "Invalid verification code");
            return "recovery";
        }
        if (!userRecoveryRequest.getPassword().equals(userRecoveryRequest.getConfirmPassword())) {
            model.addAttribute("userRecoveryRequest", userRecoveryRequest);
            model.addAttribute("recoveryError", "Passwords do not match");
            return "recovery";
        }
        if (!userService.updateUserPassword(userRecoveryRequest)) {
            model.addAttribute("userRecoveryRequest", userRecoveryRequest);
            model.addAttribute("recoveryError", "User not found");
            return "recovery";
        }
        session.removeAttribute("code");
        model.addAttribute("userLoginRequest", new UserLoginRequest(userRecoveryRequest.getVmail(), "", false));
        return "login";
    }

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        return "index";
    }

}