package com.example.springlab.controller;

import com.example.springlab.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public record TokenRequest(String username, String password) {}

    public record TokenResponse(String accessToken, String tokenType) {}

    @PostMapping("/token")
    public TokenResponse token(@RequestBody TokenRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        UsernamePasswordAuthenticationToken.unauthenticated(
                                request.username(),
                                request.password()
                        )
                );

        String token = jwtService.generateToken(
                authentication.getName()
        );

        return new TokenResponse(token, "Bearer");
    }
}