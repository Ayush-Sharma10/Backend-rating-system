package com.rating.rating_system.security;

import com.rating.rating_system.model.User;
import com.rating.rating_system.repository.UserRepository;
import com.rating.rating_system.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {
    //ensure krta hai filter ek hi br run kre per http req

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        String email = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // Remove "Bearer " prefix
            try {
                email = jwtUtil.extractEmail(jwt);
            } catch (ExpiredJwtException | SignatureException | MalformedJwtException e) {
                // catch krta hai agr token expired, tampered, or malformed hai toh
                System.out.println("❌ Invalid JWT Token: " + e.getMessage());
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // SecurityContextHolder.getContext()-Think of it as a container for security-related info about the current request.
            // Authentication object, which contains details about the currently authenticated user
            Optional<User> optionalUser = userRepository.findByEmail(email);
            System.out.println("🔑 Email from token: " + email);
            System.out.println("📋 User exists in DB? " + optionalUser.isPresent());
            optionalUser.ifPresent(u -> System.out.println("👤 DB user email: " + u.getEmail()));


            if (optionalUser.isPresent() && !jwtUtil.isTokenExpired(jwt)) {
                User user = optionalUser.get();

                // ✅ Add role-based authority
                //UsernamePasswordAuthenticationToken A built-in Spring Security class that implements the Authentication interface
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                user, // principal
                                null, // credentials
                                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))//GrantedAuthority
                        );
                // request detail add krta hai jaise IP or session ID
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Spring Security Context mein authentication object save karta hai, taaki user is request ke liye authenticated maana jaaye.
                SecurityContextHolder.getContext().setAuthentication(authToken);



            }
        }
        // pass krdo req to next filter ya controller
        filterChain.doFilter(request, response);
    }
}
