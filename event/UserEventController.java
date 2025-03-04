package com.sadeem.smap.controller;

import com.sadeem.smap.dto.UserDto;
import com.sadeem.smap.service.UserService;
import com.sadeem.smap.util.EventResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Locale;

@RestController
@RequestMapping("/api/users")
public class UserEventController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Create a new user.
     */
    @PostMapping("/add")
    public ResponseEntity<EventResult> addUser(
            @Valid @RequestBody UserDto userDto,
            BindingResult bindingResult,
            Locale locale) {
        if (bindingResult.hasErrors()) {
            String errorMessage = messageSource.getMessage("invalid_input", null, locale);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new EventResult("error", errorMessage));
        }
        try {
            EventResult result = userService.createUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }

    /**
     * Update an existing user.
     */
    @PutMapping("/edit")
    public ResponseEntity<EventResult> editUser(
            @Valid @RequestBody UserDto userDto,
            BindingResult bindingResult,
            Locale locale) {
        if (bindingResult.hasErrors()) {
            String errorMessage = messageSource.getMessage("invalid_input", null, locale);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new EventResult("error", errorMessage));
        }
        try {
            EventResult result = userService.updateUser(userDto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }

    /**
     * Delete a user by ID.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<EventResult> deleteUser(@PathVariable Long id, Locale locale) {
        try {
            EventResult result = userService.deleteUser(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }

    /**
     * Delete all users.
     */
    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAllUsers(Locale locale) {
        try {
            userService.deleteAllUsers();
            String successMessage = messageSource.getMessage("users.deleted.successfully", null, locale);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }

    /**
     * Upload a user profile picture.
     */
    @PostMapping("/upload-picture/{id}")
    public ResponseEntity<EventResult> uploadPicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            Locale locale) {
        try {
            userService.uploadProfilePicture(id, file);
            String successMessage = messageSource.getMessage("picture.uploaded.successfully", null, locale);
            return ResponseEntity.ok(new EventResult("success", successMessage));
        } catch (IOException e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }
}