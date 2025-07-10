package com.rating.rating_system;

import com.rating.rating_system.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)  //  Enables @PreAuthorize, @PostAuthorize, etc.
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;
    //Here’s how I want to secure my HTTP endpoints
    // — which ones need login, which ones don’t, what kind of authentication to use, etc.
    // customize krenge hmare security behaviour ko
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())// Disable this- basically cyber attacks k liye chahiye hota h
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()) //Allow H2 Console to load vrna browser block krdeta h console ko render hone s
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/register",
                                "/api/login",
                                "/h2-console/**"
                        ).permitAll() // ✅ Public endpoints
                        .anyRequest().authenticated() // 🔐 Everything else needs valid JWT token
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); //Add krta hai hmmara custom filter before default Spring filter

        return http.build(); // return krdo hmari configured Security Filter Chain
    }
}
