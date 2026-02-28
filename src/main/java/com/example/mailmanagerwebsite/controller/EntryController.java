package com.example.mailmanagerwebsite.controller;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.mailmanagerwebsite.dto.DetailDTO;
import com.example.mailmanagerwebsite.dto.FolderDTO;
import com.example.mailmanagerwebsite.dto.TokenDTO;
import com.example.mailmanagerwebsite.dto.UserDTO;
import com.example.mailmanagerwebsite.dto.UserLoginRequest;
import com.example.mailmanagerwebsite.dto.UserRecoveryRequest;
import com.example.mailmanagerwebsite.dto.UserRegisterRequest;
import com.example.mailmanagerwebsite.service.DetailService;
import com.example.mailmanagerwebsite.service.FolderService;
import com.example.mailmanagerwebsite.service.TokenService;
import com.example.mailmanagerwebsite.service.UserService;
import com.example.mailmanagerwebsite.validation.ValidationOrder;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class EntryController {

    protected final UserService userService;
    protected final TokenService tokenService;
    protected final FolderService folderService;
    protected final DetailService detailService;

    public EntryController(UserService userService, TokenService tokenService, FolderService folderService, DetailService detailService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.folderService = folderService;
        this.detailService = detailService;
    }

    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    @GetMapping("/login")
    public String loginPage(@CookieValue(required = false) String remember, Model model, HttpSession session) {
        if (remember != null) {
            Optional<TokenDTO> optToken = tokenService.getToken(remember);
            if (optToken.isPresent()) {
                TokenDTO tokenDTO = optToken.get();
                if (tokenDTO.getExpiry().isAfter(LocalDateTime.now())) {
                    Optional<UserDTO> optDTO = userService.getUserForLogin(tokenDTO.getUserId());
                    session.setAttribute("user", optDTO.get());
                    session.setMaxInactiveInterval(0);
                    return "redirect:/index";
                }
            }
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
        Optional<UserDTO> opt = userService.getUserForLogin(userLoginRequest);
        if (!opt.isPresent()) {
            model.addAttribute("userLoginRequest", userLoginRequest);
            model.addAttribute("error", "User not found");
            return "login";
        }
        if (userLoginRequest.isRemember()) {
            TokenDTO tokenDTO;
            SecureRandom random = new SecureRandom();
            do {
                byte[] token = new byte[64];
                random.nextBytes(token);

                tokenDTO = new TokenDTO(
                    "remember",
                    Base64.getEncoder().withoutPadding().encodeToString(token),
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(7),
                    opt.get().getId()
                );
            } while (!tokenService.createToken(tokenDTO, true));

            Cookie cookie = new Cookie("remember",  tokenDTO.getToken());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 7);
            cookie.setSecure(true);
            response.addCookie(cookie);
        }
        else {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("remember")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
        session.setAttribute("user", opt.get());
        session.setMaxInactiveInterval(0);
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
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        try {
            UserDTO userDTO = (UserDTO) session.getAttribute("user");
            model.addAttribute("user", userDTO);

            List<FolderDTO> systemFolderDTOs = this.folderService.getSystemFolders(userDTO.getId());
            model.addAttribute("systemFolders", systemFolderDTOs);

            List<FolderDTO> customFolderDTOs = this.folderService.getCustomFolders(userDTO.getId());
            model.addAttribute("customFolders", customFolderDTOs);

            String activeFolderId;
            String activeNavIndex;

            Optional<TokenDTO> optActiveFolderIdToken = this.tokenService.getToken("activeFolderId", userDTO.getId());
            Optional<TokenDTO> optActiveNavIndexToken = this.tokenService.getToken("activeNavIndex", userDTO.getId());
            if (optActiveFolderIdToken.isPresent() && optActiveNavIndexToken.isPresent()) {
                TokenDTO activeFolderIdToken = optActiveFolderIdToken.get();
                TokenDTO activeNavIndexToken = optActiveNavIndexToken.get();

                activeFolderIdToken.setExpiry(LocalDateTime.now().plusDays(7));
                activeNavIndexToken.setExpiry(LocalDateTime.now().plusDays(7));

                this.tokenService.updateToken(activeFolderIdToken, false);
                this.tokenService.updateToken(activeNavIndexToken, false);

                activeFolderId = activeFolderIdToken.getToken();
                activeNavIndex = activeNavIndexToken.getToken();
            }
            else {
                TokenDTO activeFolderIdToken = new TokenDTO();
                activeFolderIdToken.setName("activeFolderId");
                activeFolderIdToken.setToken(systemFolderDTOs.get(0).getId() + "");
                activeFolderIdToken.setCreated(LocalDateTime.now());
                activeFolderIdToken.setExpiry(LocalDateTime.now().plusDays(7));
                activeFolderIdToken.setUserId(userDTO.getId());

                TokenDTO activeNavIndexToken = new TokenDTO();
                activeNavIndexToken.setName("activeNavIndex");
                activeNavIndexToken.setToken("0");
                activeNavIndexToken.setCreated(LocalDateTime.now());
                activeNavIndexToken.setExpiry(LocalDateTime.now().plusDays(7));
                activeNavIndexToken.setUserId(userDTO.getId());

                this.tokenService.createToken(activeFolderIdToken, false);
                this.tokenService.createToken(activeNavIndexToken, false);

                activeFolderId = systemFolderDTOs.get(0).getId() + "";
                activeNavIndex = "0";
            }
            session.setAttribute("activeFolderId", activeFolderId);
            session.setAttribute("activeNavIndex", activeNavIndex);
            List<DetailDTO> detailDTOs = this.detailService.getDetailsByFolderId(userDTO.getId(), Integer.parseInt(activeFolderId));
            model.addAttribute("details", detailDTOs);

            return "index";
        }
        catch(Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            model.addAttribute("userLoginRequest", new UserLoginRequest());
            return "login";
        }
    }

}