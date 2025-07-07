package com.rating.rating_system.util;

import io.jsonwebtoken.Claims;  // Classes from the JJWT library for creating and parsing JWTs
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey; // A cryptographic key used to sign JWTs
import java.nio.charset.StandardCharsets; //For encoding the secret key properly.
import java.util.Date; //Used for setting token expiration and issue times.

@Component
public class JwtUtil {

    private final String SECRET = "my-super-secret-key-that-is-very-long-for-jwt-signing-purposes-1234567890";
    private SecretKey secretKey;

    private final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    @PostConstruct // method ko automatically run krwane k liye jaise hi bean bne
    public void init() {
        secretKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)); // secret key ko cryptographic key m convert krta h
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS512) //Sign the token using HS512 algorithm and your secret key.
                .compact();
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }
    // validate token
    private Claims getClaims(String token) {
        return Jwts //Returns the JWT claims, which is the payload data (subject, expiration, etc.)
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
}
