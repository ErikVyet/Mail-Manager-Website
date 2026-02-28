package com.example.mailmanagerwebsite.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mailmanagerwebsite.dto.FolderDTO;
import com.example.mailmanagerwebsite.dto.TokenDTO;
import com.example.mailmanagerwebsite.dto.UserDTO;
import com.example.mailmanagerwebsite.service.FolderService;
import com.example.mailmanagerwebsite.service.TokenService;
import com.example.mailmanagerwebsite.validation.ValidationOrder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/folder")
public class FolderController {

    protected final FolderService folderService;
    protected final TokenService tokenService;

    public FolderController(FolderService folderService, TokenService tokenService) {
        this.folderService = folderService;
        this.tokenService = tokenService;
    }

    @GetMapping("/active")
    public ResponseEntity<Map<String, String>> getActiveFolder(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            UserDTO userDTO = (UserDTO) session.getAttribute("user");
            Optional<TokenDTO> optActiveFolderIdToken = this.tokenService.getToken("activeFolderId", userDTO.getId());
            Optional<TokenDTO> optActiveNavIndexToken = this.tokenService.getToken("activeNavIndex", userDTO.getId());
            Map<String, String> json = new HashMap<>();
            if (optActiveFolderIdToken.isPresent() && optActiveNavIndexToken.isPresent()) {
                TokenDTO activeFolderIdToken = optActiveFolderIdToken.get();
                TokenDTO activeNavIndexToken = optActiveNavIndexToken.get();
                json.put("activeFolderId", activeFolderIdToken.getToken());
                json.put("activeNavIndex", activeNavIndexToken.getToken()); 
            }
            return ResponseEntity.ok(json);
        }
        catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createNewFolder(@Validated(ValidationOrder.class) @RequestBody FolderDTO folderDTO, BindingResult result, HttpServletRequest request) {
        Map<String, String> json = new HashMap<>();
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (result.hasErrors()) {
            json.put("error", result.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            UserDTO userDTO = (UserDTO) session.getAttribute("user");
            if (this.folderService.createFolder(folderDTO, userDTO.getId())) {
                json.put("success", "Folder created successfully");
                json.put("folderId", String.valueOf(folderDTO.getId()));
            }
            else {
                json.put("error", "Folder already exists or you have reached the maximum number of folders");
            }
            return ResponseEntity.ok(json);
        }
        catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteExistedFolder(@RequestBody FolderDTO folderDTO, HttpServletRequest request) {
        Map<String, String> json = new HashMap<>();
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            UserDTO userDTO = (UserDTO) session.getAttribute("user");
            if (this.folderService.deleteFolder(folderDTO.getId(), userDTO.getId())) {
                json.put("success", "Folder deleted successfully");
            }
            else {
                json.put("error", "Folder not found or you don't have permission to delete it");
            }
            return ResponseEntity.ok(json);
        }
        catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}