package com.epam.finaltask.restcontroller;

import com.epam.finaltask.config.JwtService;
import com.epam.finaltask.dto.AuthResponse;
import com.epam.finaltask.dto.LoginRequest;
import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Auth endpoints")
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthenticationRestController(AuthenticationManager authenticationManager,
                                        JwtService jwtService,
                                        UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "User login",
            description = "Authenticates user by username and password and returns JWT access/refresh tokens."
    )
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        String accessToken = jwtService.generateToken(auth);
        String refreshToken = jwtService.generateRefreshToken(auth);

        UserDTO user = userService.getUserByUsername(request.username());

        AuthResponse response = new AuthResponse(accessToken, refreshToken, user);

        return ResponseEntity.ok(response);
    }
}
