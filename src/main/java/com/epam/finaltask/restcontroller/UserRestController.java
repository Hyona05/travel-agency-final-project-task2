package com.epam.finaltask.restcontroller;

import com.epam.finaltask.dto.ApiResponse;
import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserRestController {


    @Autowired
    UserService  userService;

    @PatchMapping("/{id}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> block(@PathVariable String id) {
        UserDTO dto = userService.blockUser(id);
        return ResponseEntity.ok(ApiResponse.ok("User blocked", dto));
    }

    @PatchMapping("/{id}/unblock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> unblock(@PathVariable String id) {
        UserDTO dto = userService.unblockUser(id);
        return ResponseEntity.ok(ApiResponse.ok("User unblocked", dto));
    }

}
