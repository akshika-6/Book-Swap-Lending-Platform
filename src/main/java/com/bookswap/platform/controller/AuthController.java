package com.bookswap.platform.controller;

import com.bookswap.platform.dto.ApiResponse;
import com.bookswap.platform.dto.AuthResponse;
import com.bookswap.platform.dto.LoginRequest;
import com.bookswap.platform.dto.RegisterRequest;
import com.bookswap.platform.model.AppUser;
import com.bookswap.platform.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/auth", "/api/auth"})
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        AppUser user = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("User registered successfully with id: " + user.getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        AppUser user = userService.getUserByEmail(request.email());
        return ResponseEntity.ok(new AuthResponse(
                "Login successful",
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole().name()
        ));
    }
}
