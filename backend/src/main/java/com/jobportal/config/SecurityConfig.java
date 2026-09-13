package com.jobportal.config;

import com.jobportal.security.JwtFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration c
    ) throws Exception {
        return c.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())

                .cors(c -> c.configurationSource(cors()))

                .sessionManagement(s ->
                        s.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(a -> a

                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/refresh",
                                "/api/auth/logout",
                                "/api/auth/register/user",
                                "/api/auth/register/recruiter",
                                "/api/jobs/public/**",
                                "/api/companies/approved",
                                "/api/companies/*"
                        ).permitAll()

                        .requestMatchers(
                                "/api/auth/register/admin"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                "/api/admin/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                "/api/recruiter/**"
                        ).hasRole("RECRUITER")

                        .requestMatchers(
                                "/api/user/**"
                        ).hasRole("USER")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }

    @Bean
    CorsConfigurationSource cors() {

        CorsConfiguration c = new CorsConfiguration();

        c.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:3000",

                // Replace this with your actual Vercel URL
                "https://YOUR-FRONTEND.vercel.app"
        ));

        c.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
        ));

        c.setAllowedHeaders(List.of("*"));

        // Required for HttpOnly cookies
        c.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource s =
                new UrlBasedCorsConfigurationSource();

        s.registerCorsConfiguration("/**", c);

        return s;
    }
}
