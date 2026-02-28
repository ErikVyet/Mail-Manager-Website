package com.example.mailmanagerwebsite.service;

import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mailmanagerwebsite.dto.TokenDTO;
import com.example.mailmanagerwebsite.model.Token;
import com.example.mailmanagerwebsite.model.User;
import com.example.mailmanagerwebsite.repository.TokenRepository;
import com.example.mailmanagerwebsite.repository.UserRepository;

@Service
public class TokenService {

    protected final TokenRepository tokenRepository;
    protected final UserRepository userRepository;

    public TokenService(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Optional<TokenDTO> getToken(String value) {
        if (value != null) {
            Optional<Token> opt = tokenRepository.findByValue(DigestUtils.sha256Hex(value));
            if (opt.isPresent()) {
                Token token = opt.get();
                return Optional.of(new TokenDTO(
                    token.getId(),
                    token.getName(),
                    token.getValue(),
                    token.getCreated(),
                    token.getExpiry(),
                    token.getUser().getId()
                ));
            }
        }
        return Optional.empty();
    }

    public Optional<TokenDTO> getToken(String name, int userId) {
        if (name != null) {
            Optional<Token> opt = this.tokenRepository.findByNameAndUserId(name, userId);
            if (opt.isPresent()) {
                Token token = opt.get();
                return Optional.of(new TokenDTO(
                    token.getId(),
                    token.getName(),
                    token.getValue(),
                    token.getCreated(),
                    token.getExpiry(),
                    token.getUser().getId()
                ));
            }
        }
        return Optional.empty();
    }

    @Transactional
    public boolean createToken(TokenDTO tokenDTO, boolean secured) {
        System.out.println(secured);
        System.out.println(tokenDTO.getToken());
        Token token = new Token(
            tokenDTO.getName(),
            secured ? DigestUtils.sha256Hex(tokenDTO.getToken()) : tokenDTO.getToken(),
            tokenDTO.getCreated(),
            tokenDTO.getExpiry()
        );
        Optional<User> user = userRepository.findById(tokenDTO.getUserId());
        if (user.isPresent()) {
            token.setUser(user.get());
            try {
                this.tokenRepository.save(token);
                return true;
            }
            catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
        return false;
    }

    @Transactional
    public boolean updateToken(TokenDTO tokenDTO, boolean secured) {
        Optional<Token> opt = this.tokenRepository.findById(tokenDTO.getId());
        if (opt.isPresent()) {
            Token token = opt.get();
            token.setValue(secured ? DigestUtils.sha256Hex(tokenDTO.getToken()) : tokenDTO.getToken());
            token.setExpiry(tokenDTO.getExpiry());
            try {
                this.tokenRepository.save(token);
                return true;
            }
            catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
        return false;
    }

}