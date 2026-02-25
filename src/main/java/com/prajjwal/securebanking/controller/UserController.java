package com.prajjwal.securebanking.controller;

import com.prajjwal.securebanking.dto.ChangePasswordRequestDto;
import com.prajjwal.securebanking.dto.UserResponse;
import com.prajjwal.securebanking.model.Account;
import com.prajjwal.securebanking.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank/user")
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> profile(Authentication authentication) {
        String username = authentication.getName();

        UserResponse response = userService.getProfile(username);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequestDto request,
                                                 Authentication authentication) {
        String username = authentication.getName();
        userService.changePassword(username, request);

        return ResponseEntity.ok("Password update Successfully");
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts(Authentication authentication) {
        String username = authentication.getName();

        return ResponseEntity.ok(userService.getListOfAccounts(username));
    }
}