package com.sadeem.smap.controller;

import com.sadeem.smap.service.UserService;
import com.sadeem.smap.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Get all users (general route).
     */
    @GetMapping
    public ResponseEntity<Iterable<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Get a paginated list of users.
     */
    @GetMapping("/list")
    public ResponseEntity<Page<UserDto>> getUsersPaginated(Pageable pageable) {
        return ResponseEntity.ok(userService.getUsersPaginated(pageable));
    }

    /**
     * Get a specific user by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id, Locale locale) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}