package com.example.mailmanagerwebsite.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.mailmanagerwebsite.dto.AttachmentDTO;
import com.example.mailmanagerwebsite.dto.DetailDTO;
import com.example.mailmanagerwebsite.dto.EmailDTO;
import com.example.mailmanagerwebsite.dto.FolderDTO;
import com.example.mailmanagerwebsite.dto.TokenDTO;
import com.example.mailmanagerwebsite.dto.UserDTO;
import com.example.mailmanagerwebsite.service.AttachmentService;
import com.example.mailmanagerwebsite.service.DetailService;
import com.example.mailmanagerwebsite.service.EmailService;
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
    protected final EmailService emailService;
    protected final AttachmentService attachmentService;

    public MailController(DetailService detailService, FolderService folderService, TokenService tokenService, EmailService emailService, AttachmentService attachmentService) {
        this.detailService = detailService;
        this.folderService = folderService;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.attachmentService = attachmentService;
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

    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> getEmail(@RequestBody Map<String, Integer> body, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Map<String, Object> json = new HashMap<>();
        try {
            UserDTO userDTO = (UserDTO) session.getAttribute("user");
            int emailId = body.get("mailId");
            Optional<EmailDTO> optEmail = this.emailService.getEmail(emailId);
            if (!optEmail.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            EmailDTO emailDTO = optEmail.get();
            json.put("mail", emailDTO);
            json.put("success", true);
        }
        catch(Exception exception) {
            System.out.println(exception.getMessage());
            json.put("success", false);
        }
        return ResponseEntity.ok(json);
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveDraftEmail(@RequestParam(required = false) Integer mailId, @RequestParam String subject, @RequestParam String body, @RequestParam(required = false) List<MultipartFile> attachments, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Map<String, Object> json = new HashMap<>();
        try {
            UserDTO userDTO = (UserDTO) session.getAttribute("user");
            if (mailId == null) {
                Optional<EmailDTO> optEmail = this.emailService.createEmail(new EmailDTO(subject, body, LocalDateTime.now(), userDTO));
                if (!optEmail.isPresent()) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
                EmailDTO emailDTO = optEmail.get();

                Optional<DetailDTO> optDetail = this.detailService.createDraftDetail(userDTO.getId(), emailDTO.getId());
                if (!optDetail.isPresent()) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }

                List<AttachmentDTO> attachmentDTOs = new ArrayList<>();
                if (attachments != null) {
                    for (MultipartFile attachment : attachments) {
                        AttachmentDTO attachmentDTO = new AttachmentDTO(
                            attachment.getResource().getFilename(),
                            attachment.getBytes(),
                            (int) attachment.getSize(),
                            attachment.getContentType(),
                            LocalDateTime.now(),
                            emailDTO.getId()
                        );
                        attachmentDTOs.add(attachmentDTO);
                    }
                }
                if (!attachmentDTOs.isEmpty()) {
                    if (!this.attachmentService.createAttachments(attachmentDTOs)) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                }
            }
            else {
                EmailDTO emailDTO = new EmailDTO(mailId, subject, body, LocalDateTime.now(), userDTO);
                if (!this.emailService.updateEmail(emailDTO)) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }

                List<AttachmentDTO> attachmentDTOs = new ArrayList<>();
                if (attachments != null) {
                    for (MultipartFile attachment : attachments) {
                        AttachmentDTO attachmentDTO = new AttachmentDTO(
                            attachment.getResource().getFilename(),
                            attachment.getBytes(),
                            (int) attachment.getSize(),
                            attachment.getContentType(),
                            LocalDateTime.now(),
                            mailId
                        );
                        attachmentDTOs.add(attachmentDTO);
                    }
                }
                if (!attachmentDTOs.isEmpty()) {
                    if (!this.attachmentService.createAttachments(attachmentDTOs)) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                }
            }

            json.put("success", true);
        }
        catch(Exception exception) {
            System.out.println(exception.getMessage());
            json.put("success", false);
        }
        return ResponseEntity.ok(json);
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteEmail(@RequestBody Map<String, Integer> body, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Map<String, Object> json = new HashMap<>();
        try {
            UserDTO userDTO = (UserDTO) session.getAttribute("user");
            int emailId = body.get("mailId");
            if (!this.emailService.deleteEmail(emailId)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            json.put("success", true);
        }
        catch(Exception exception) {
            System.out.println(exception.getMessage());
            json.put("success", false);
        }
        return ResponseEntity.ok(json);
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendEmail(@RequestParam(required = false) Integer mailId, @RequestParam String subject, @RequestParam String body, @RequestParam(required = false) List<MultipartFile> attachments, HttpServletRequest request) {
        
        return null;
    }
}