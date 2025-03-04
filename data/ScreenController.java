package com.sadeem.smap.controller;

import com.sadeem.smap.dto.ScreenDto;
import com.sadeem.smap.service.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/screens")
public class ScreenController {

    @Autowired
    private ScreenService screenService;

    /**
     * Get all screens.
     */
    @GetMapping
    public ResponseEntity<Iterable<ScreenDto>> getAllScreens() {
        return ResponseEntity.ok(screenService.getAllScreens());
    }

    /**
     * Get paginated list of screens.
     */
    @GetMapping("/list")
    public ResponseEntity<Page<ScreenDto>> getScreensPaginated(Pageable pageable) {
        return ResponseEntity.ok(screenService.getScreensPaginated(pageable));
    }

    /**
     * Get screen by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScreenDto> getScreenById(@PathVariable Long id, Locale locale) {
        return screenService.getScreenById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new screen.
     */
    @PostMapping("/add")
    public ResponseEntity<Void> createScreen(@RequestBody ScreenDto screenDto, Locale locale) {
        screenService.createScreen(screenDto);
        return ResponseEntity.status(201).build();
    }

    /**
     * Update an existing screen.
     */
    @PutMapping("/edit")
    public ResponseEntity<Void> updateScreen(@RequestBody ScreenDto screenDto, Locale locale) {
        screenService.updateScreen(screenDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a screen by ID.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteScreen(@PathVariable Long id, Locale locale) {
        screenService.deleteScreen(id);
        return ResponseEntity.noContent().build();
    }
}