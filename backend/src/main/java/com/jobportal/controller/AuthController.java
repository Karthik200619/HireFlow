package com.jobportal.controller;

import com.jobportal.dto.AuthDtos.*;
import com.jobportal.dto.UserDtos;
import com.jobportal.entity.User;
import com.jobportal.repository.UserRepository;
import com.jobportal.security.JwtService;
import com.jobportal.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService service;
    private final UserRepository users;
    private final AuthenticationManager auth;
    private final JwtService jwt;
    private final com.jobportal.security.TokenBlacklist blacklist;

    public AuthController(
            UserService service,
            UserRepository users,
            AuthenticationManager auth,
            JwtService jwt,
            com.jobportal.security.TokenBlacklist blacklist
    ) {
        this.service = service;
        this.users = users;
        this.auth = auth;
        this.jwt = jwt;
        this.blacklist = blacklist;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDtos.UserResponse> me(
            org.springframework.security.core.Authentication authentication
    ) {
        var u = users.findByEmail(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(service.response(u));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest r,
            HttpServletResponse response
    ) {
        auth.authenticate(
                new UsernamePasswordAuthenticationToken(
                        r.email(),
                        r.password()
                )
        );

        var u = users.findByEmail(r.email()).orElseThrow();

        String access = jwt.access(
                u.getEmail(),
                u.getRole().name()
        );

        String refresh = jwt.refresh(
                u.getEmail(),
                u.getRole().name()
        );

        setCookies(response, access, refresh);

        return ResponseEntity.ok(
                AuthResponse.of(null, null, u)
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @CookieValue(
                    name = "HF_REFRESH",
                    required = false
            ) String cookie,
            @RequestBody(required = false) TokenRequest ignored,
            HttpServletResponse response
    ) {
        String t = cookie;

        if (
                t == null
                        || t.isBlank()
                        || blacklist.contains(t)
                        || !"refresh".equals(jwt.type(t))
        ) {
            throw new BadCredentialsException(
                    "Invalid refresh token"
            );
        }

        var u = users.findByEmail(jwt.email(t)).orElseThrow();

        String access = jwt.access(
                u.getEmail(),
                u.getRole().name()
        );

        setCookies(response, access, t);

        return ResponseEntity.ok(
                AuthResponse.of(null, null, u)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(
                    name = "HF_REFRESH",
                    required = false
            ) String refresh,
            HttpServletResponse response
    ) {
        try {
            if (refresh != null) {
                blacklist.add(
                        refresh,
                        jwt.claims(refresh)
                                .getExpiration()
                                .toInstant()
                );
            }
        } catch (Exception ignored) {
        }

        clearCookies(response);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register/user")
    public ResponseEntity<AuthResponse> registerUser(
            @Valid @RequestBody RegisterRequest r,
            HttpServletResponse response
    ) {
        var u = service.register(
                r,
                User.Role.USER
        );

        setCookies(
                response,
                jwt.access(
                        u.getEmail(),
                        u.getRole().name()
                ),
                jwt.refresh(
                        u.getEmail(),
                        u.getRole().name()
                )
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        AuthResponse.of(
                                null,
                                null,
                                u
                        )
                );
    }

    @PostMapping("/register/recruiter")
    public ResponseEntity<AuthResponse> registerRecruiter(
            @Valid @RequestBody RegisterRequest r,
            HttpServletResponse response
    ) {
        var u = service.register(
                r,
                User.Role.RECRUITER
        );

        setCookies(
                response,
                jwt.access(
                        u.getEmail(),
                        u.getRole().name()
                ),
                jwt.refresh(
                        u.getEmail(),
                        u.getRole().name()
                )
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        AuthResponse.of(
                                null,
                                null,
                                u
                        )
                );
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponse> registerAdmin(
            @Valid @RequestBody RegisterRequest r,
            HttpServletResponse response
    ) {
        var u = service.register(
                r,
                User.Role.ADMIN
        );

        setCookies(
                response,
                jwt.access(
                        u.getEmail(),
                        u.getRole().name()
                ),
                jwt.refresh(
                        u.getEmail(),
                        u.getRole().name()
                )
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        AuthResponse.of(
                                null,
                                null,
                                u
                        )
                );
    }

    /*
     * Production cookies:
     * Secure = true
     * SameSite = None
     *
     * Required because frontend is on Vercel
     * and backend is on Render.
     */
    private void setCookies(
            HttpServletResponse response,
            String access,
            String refresh
    ) {
        response.addHeader(
                "Set-Cookie",
                ResponseCookie.from(
                                "HF_ACCESS",
                                access
                        )
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .sameSite("None")
                        .maxAge(900)
                        .build()
                        .toString()
        );

        response.addHeader(
                "Set-Cookie",
                ResponseCookie.from(
                                "HF_REFRESH",
                                refresh
                        )
                        .httpOnly(true)
                        .secure(true)
                        .path("/api/auth")
                        .sameSite("None")
                        .maxAge(604800)
                        .build()
                        .toString()
        );
    }

    private void clearCookies(
            HttpServletResponse response
    ) {
        response.addHeader(
                "Set-Cookie",
                ResponseCookie.from(
                                "HF_ACCESS",
                                ""
                        )
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .sameSite("None")
                        .maxAge(0)
                        .build()
                        .toString()
        );

        response.addHeader(
                "Set-Cookie",
                ResponseCookie.from(
                                "HF_REFRESH",
                                ""
                        )
                        .httpOnly(true)
                        .secure(true)
                        .path("/api/auth")
                        .sameSite("None")
                        .maxAge(0)
                        .build()
                        .toString()
        );
    }
}
