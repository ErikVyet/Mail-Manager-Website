package com.example.mailmanagerwebsite.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mailmanagerwebsite.dto.DetailDTO;
import com.example.mailmanagerwebsite.dto.FolderDTO;
import com.example.mailmanagerwebsite.dto.TokenDTO;
import com.example.mailmanagerwebsite.dto.UserDTO;
import com.example.mailmanagerwebsite.service.DetailService;
import com.example.mailmanagerwebsite.service.FolderService;
import com.example.mailmanagerwebsite.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/mail")
public class MailController {

    protected final DetailService detailService;
    protected final FolderService folderService;
    protected final TokenService tokenService;

    public MailController(DetailService detailService, FolderService folderService, TokenService tokenService) {
        this.detailService = detailService;
        this.folderService = folderService;
        this.tokenService = tokenService;
    }

    @PostMapping("/folder")
    public ResponseEntity<Map<Integer, DetailDTO>> getMailsByFolder(@RequestBody Map<String, Integer> body, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        int activeFolderId = body.get("activeFolderId");
        int activeNavIndex = body.get("activeNavIndex");

        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        Optional<FolderDTO> optFolderDTO = this.folderService.getFolder(activeFolderId, userDTO.getId());
        if (!optFolderDTO.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<TokenDTO> optActiveFolderIdToken = this.tokenService.getToken("activeFolderId", userDTO.getId());
        if (!optActiveFolderIdToken.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        TokenDTO activeFolderIdToken = optActiveFolderIdToken.get();
        activeFolderIdToken.setToken(String.valueOf(activeFolderId));
        activeFolderIdToken.setExpiry(LocalDateTime.now().plusDays(7));

        Optional<TokenDTO> optActiveNavIndexToken = this.tokenService.getToken("activeNavIndex", userDTO.getId());
        if (!optActiveNavIndexToken.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        TokenDTO activeNavIndexToken = optActiveNavIndexToken.get();
        activeNavIndexToken.setToken(String.valueOf(activeNavIndex));
        activeNavIndexToken.setExpiry(LocalDateTime.now().plusDays(7));

        this.tokenService.updateToken(activeFolderIdToken, false);
        this.tokenService.updateToken(activeNavIndexToken, false);

        session.setAttribute("activeFolderId", activeFolderId);
        session.setAttribute("activeNavIndex", activeNavIndex);

        List<DetailDTO> detailDTOs = detailService.getDetailsByFolderId(userDTO.getId(), activeFolderId);
        Map<Integer, DetailDTO> json = new HashMap<>();
        for (DetailDTO detailDTO : detailDTOs) {
            json.put(detailDTO.getEmail().getId(), detailDTO);
        }
        return ResponseEntity.ok(json);
    }

    @GetMapping("/starred")
    public ResponseEntity<Map<Integer, DetailDTO>> getStarredMails(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UserDTO userDTO = (UserDTO) session.getAttribute("user");

        Optional<TokenDTO> optActiveFolderIdToken = this.tokenService.getToken("activeFolderId", userDTO.getId());
        if (!optActiveFolderIdToken.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        TokenDTO activeFolderIdToken = optActiveFolderIdToken.get();
        activeFolderIdToken.setToken("-1");
        activeFolderIdToken.setExpiry(LocalDateTime.now().plusDays(7));

        Optional<TokenDTO> optActiveNavIndexToken = this.tokenService.getToken("activeNavIndex", userDTO.getId());
        if (!optActiveNavIndexToken.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        TokenDTO activeNavIndexToken = optActiveNavIndexToken.get();
        activeNavIndexToken.setToken("1");
        activeNavIndexToken.setExpiry(LocalDateTime.now().plusDays(7));

        this.tokenService.updateToken(activeFolderIdToken, false);
        this.tokenService.updateToken(activeNavIndexToken, false);

        session.setAttribute("activeFolderId", -1);
        session.setAttribute("activeNavIndex", 1);

        List<DetailDTO> detailDTOs = detailService.getStarredMails(userDTO.getId());
        Map<Integer, DetailDTO> json = new HashMap<>();
        for (DetailDTO detailDTO : detailDTOs) {
            json.put(detailDTO.getEmail().getId(), detailDTO);
        }
        return ResponseEntity.ok(json);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<Integer, DetailDTO>> getAllMails(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UserDTO userDTO = (UserDTO) session.getAttribute("user");

        Optional<TokenDTO> optActiveFolderIdToken = this.tokenService.getToken("activeFolderId", userDTO.getId());
        if (!optActiveFolderIdToken.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        TokenDTO activeFolderIdToken = optActiveFolderIdToken.get();
        activeFolderIdToken.setToken("-2");
        activeFolderIdToken.setExpiry(LocalDateTime.now().plusDays(7));

        Optional<TokenDTO> optActiveNavIndexToken = this.tokenService.getToken("activeNavIndex", userDTO.getId());
        if (!optActiveNavIndexToken.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        TokenDTO activeNavIndexToken = optActiveNavIndexToken.get();
        activeNavIndexToken.setToken("4");
        activeNavIndexToken.setExpiry(LocalDateTime.now().plusDays(7));

        this.tokenService.updateToken(activeFolderIdToken, false);
        this.tokenService.updateToken(activeNavIndexToken, false);

        session.setAttribute("activeFolderId", -2);
        session.setAttribute("activeNavIndex", 4);

        List<DetailDTO> detailDTOs = detailService.getAllMails(userDTO.getId());
        Map<Integer, DetailDTO> json = new HashMap<>();
        for (DetailDTO detailDTO : detailDTOs) {
            json.put(detailDTO.getEmail().getId(), detailDTO);
        }
        return ResponseEntity.ok(json);
    }

}