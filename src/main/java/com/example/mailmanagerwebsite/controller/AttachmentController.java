package com.example.mailmanagerwebsite.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mailmanagerwebsite.dto.AttachmentDTO;
import com.example.mailmanagerwebsite.dto.UserDTO;
import com.example.mailmanagerwebsite.service.AttachmentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    protected final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> getAttachmentsByEmailId(@RequestBody Map<String, Integer> body, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            UserDTO userDTO = (UserDTO) session.getAttribute("user");
            int emailId = body.get("mailId");
            List<AttachmentDTO> attachmentDTOs = this.attachmentService.getAttachments(emailId);
            Map<String, Object> json = new HashMap<>();
            json.put("attachments", attachmentDTOs);
            json.put("success", true);
            return ResponseEntity.ok(json);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteAttachment(@RequestBody Map<String, Integer> body, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            UserDTO userDTO = (UserDTO) session.getAttribute("user");
            int attachmentId = body.get("attachmentId");
            Map<String, Object> json = new HashMap<>();
            json.put("success", this.attachmentService.deleteAttachment(attachmentId));
            return ResponseEntity.ok(json);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}