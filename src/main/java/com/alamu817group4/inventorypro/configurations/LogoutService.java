package com.alamu817group4.inventorypro.configurations;

import com.alamu817group4.inventorypro.entities.Token;
import com.alamu817group4.inventorypro.repositories.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        var storedToken = tokenRepository.findById(jwt)
                .orElse(Token.builder().build());
        storedToken.setRevoked(true);
        tokenRepository.save(storedToken);
        SecurityContextHolder.clearContext();
    }
}
