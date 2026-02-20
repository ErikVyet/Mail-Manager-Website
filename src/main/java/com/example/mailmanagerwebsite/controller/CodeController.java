package com.example.mailmanagerwebsite.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mailmanagerwebsite.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/code")
public class CodeController {

    private final UserService userService;

    public CodeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get")
    public ResponseEntity<Map<String, String>> getCode(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS.value()).build();
        }
        String code = "";
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            if (random.nextBoolean()) {
                code += random.nextInt(10);
            }
            else {
                code += (char) (random.nextInt(26) + 'A');
            }
        }
        session = request.getSession();
        session.setAttribute("code", code);
        session.setMaxInactiveInterval(60);

        Map<String, String> json = new HashMap<>();
        json.put("code", code);

        return ResponseEntity.ok(json);
    }

    @PostMapping("/getForRecovery")
    public ResponseEntity<Map<String, String>> getCodeForRecovery(@RequestBody String vmail, HttpServletRequest request) {
        Map<String, String> json = new HashMap<>();
        if (!userService.isExisted(vmail)) {
            json.put("status", "error");
            json.put("message", "Vmail not found");
            return ResponseEntity.ok(json);
        }
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS.value()).build();
        }
        String code = "";
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            if (random.nextBoolean()) {
                code += random.nextInt(10);
            }
            else {
                code += (char) (random.nextInt(26) + 'A');
            }
        }
        session = request.getSession();
        session.setAttribute("code", code);
        session.setMaxInactiveInterval(60);

        json.put("code", code);

        return ResponseEntity.ok(json);
    }
}