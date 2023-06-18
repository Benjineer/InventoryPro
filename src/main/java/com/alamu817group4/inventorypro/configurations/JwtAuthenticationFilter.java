package com.alamu817group4.inventorypro.configurations;

import com.alamu817group4.inventorypro.dtos.ResponseObject;
import com.alamu817group4.inventorypro.entities.Token;
import com.alamu817group4.inventorypro.repositories.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws IOException {
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userEmail;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwt);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                var isTokenValid = tokenRepository.findById(userEmail)
                        .map(token -> token.getTtlMinutes() > 0 && !token.isRevoked())
                        .orElse(false);
                if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception ex) {

            if (ex instanceof JwtException) {
                logger.warn("Could not authorize the access_token", ex);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            } else {
                logger.error("Could not authorize the access_token", ex);
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(ResponseObject.builder()
                    .status(ResponseObject.ResponseStatus.FAILED)
                    .message(ex.getMessage())
                    .build()));
        }
    }
}
