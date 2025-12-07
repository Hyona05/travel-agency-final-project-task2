package com.epam.finaltask.restcontroller;

import com.epam.finaltask.dto.ApiResponse;
import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management endpoints")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register new user",
            description = "Creates a new user account"
    )
    public ResponseEntity<ApiResponse<UserDTO>> register(@RequestBody UserDTO userDTO) {
        UserDTO created = userService.register(userDTO);
        return ResponseEntity.ok(ApiResponse.ok("User registered successfully", created));
    }

    @PatchMapping("/{username}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(
            summary = "Update user profile",
            description = "Updates user data by username (phone, balance, password, etc.)"
    )
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable String username,
            @RequestBody UserDTO userDTO
    ) {
        UserDTO updated = userService.updateUser(username, userDTO);
        return ResponseEntity.ok(ApiResponse.ok("User updated successfully", updated));
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(
            summary = "Get user by username",
            description = "Returns user information by username"
    )
    public ResponseEntity<ApiResponse<UserDTO>> getUser(@PathVariable String username) {
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(ApiResponse.ok(user));
    }
}
