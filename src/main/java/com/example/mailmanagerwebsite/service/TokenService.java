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

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public TokenService(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Optional<TokenDTO> getToken(String value) {
        Optional<Token> opt = tokenRepository.findByValue(DigestUtils.sha256Hex(value));
        if (opt.isPresent()) {
            Token token = opt.get();
            TokenDTO tokenDTO = new TokenDTO(
                token.getValue(),
                token.getCreated(),
                token.getExpiry(),
                token.getUser().getId()
            );
            return Optional.of(tokenDTO);
        }
        return Optional.empty();
    }

    @Transactional
    public boolean createToken(TokenDTO tokenDTO) {
        Token token = new Token(
            DigestUtils.sha256Hex(tokenDTO.getToken()),
            tokenDTO.getCreated(),
            tokenDTO.getExpiry()
        );
        Optional<User> user = userRepository.findById(tokenDTO.getUserId());
        if (user.isPresent()) {
            token.setUser(user.get());
            tokenRepository.save(token);
            return true;
        }
        return false;
    }

}