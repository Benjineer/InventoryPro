package com.alamu817group4.inventorypro.services;

import com.alamu817group4.inventorypro.configurations.JwtService;
import com.alamu817group4.inventorypro.configurations.properties.AuthenticationProperties;
import com.alamu817group4.inventorypro.dtos.AuthenticationRequest;
import com.alamu817group4.inventorypro.dtos.AuthenticationResponse;
import com.alamu817group4.inventorypro.entities.Token;
import com.alamu817group4.inventorypro.entities.User;
import com.alamu817group4.inventorypro.exceptions.InventoryProServerException;
import com.alamu817group4.inventorypro.repositories.TokenRepository;
import com.alamu817group4.inventorypro.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final AuthenticationProperties authenticationProperties;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);

        saveUserToken(user, jwt);

        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse refreshToken(String refreshToken) {

        var refreshJwt = refreshToken.substring(7);
        var userEmail = jwtService.extractUsername(refreshJwt);

        var user = this.userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!jwtService.isTokenValid(refreshJwt, user)) {
            throw new InventoryProServerException("Invalid token");
        }

        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshJwt)
                .build();
    }

    private void saveUserToken(User user, String jwt) {
        tokenRepository.save(Token.builder()
                .username(user.getUsername())
                .jwt(jwt)
                .revoked(false)
                .ttlMinutes(authenticationProperties.getJwt().getTokenExpiryTimeMinutes())
                .build());
    }

    private void revokeAllUserTokens(User user) {
        tokenRepository.findAllById(Collections.singletonList(user.getUsername()))
                .forEach(token -> {
                    token.setRevoked(true);
                    tokenRepository.save(token);
                });
    }

}
