package com.alamu817group4.inventorypro.configurations;

import com.alamu817group4.inventorypro.configurations.properties.AuthenticationProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

  private final RSAKeyGenerator rsaKeyGenerator;

  private final AuthenticationProperties authenticationProperties;
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  public String generateToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails
  ) {
    return createToken(extraClaims, userDetails, authenticationProperties.getJwt().getTokenExpiryTimeMinutes());
  }

  public String generateRefreshToken(
      UserDetails userDetails
  ) {
    return createToken(new HashMap<>(), userDetails, authenticationProperties.getJwt().getRefreshTokenExpiryTimeMinutes());
  }

  private String createToken(
          Map<String, Object> extraClaims,
          UserDetails userDetails,
          long expiration
  ) {
    Instant now = Instant.now();
    return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plus(expiration, ChronoUnit.MINUTES)))
            .signWith(rsaKeyGenerator.getPrivateKey(), SignatureAlgorithm.RS512)
            .compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date(System.currentTimeMillis()));
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(rsaKeyGenerator.getPublicKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}
