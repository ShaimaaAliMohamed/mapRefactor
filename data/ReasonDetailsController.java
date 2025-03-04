package com.sadeem.smap.controller;

import com.sadeem.smap.dto.ReasonDetailsDto;
import com.sadeem.smap.service.ReasonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/reason-details")
public class ReasonDetailsController {

    @Autowired
    private ReasonDetailsService reasonDetailsService;

    /**
     * Get all reason details.
     */
    @GetMapping
    public ResponseEntity<Iterable<ReasonDetailsDto>> getAllReasonDetails() {
        return ResponseEntity.ok(reasonDetailsService.getAllReasonDetails());
    }

    /**
     * Get paginated list of reason details.
     */
    @GetMapping("/list")
    public ResponseEntity<Page<ReasonDetailsDto>> getReasonDetailsPaginated(Pageable pageable) {
        return ResponseEntity.ok(reasonDetailsService.getReasonDetailsPaginated(pageable));
    }

    /**
     * Get reason detail by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReasonDetailsDto> getReasonDetailById(@PathVariable Long id, Locale locale) {
        return reasonDetailsService.getReasonDetailById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new reason detail.
     */
    @PostMapping("/add")
    public ResponseEntity<Void> createReasonDetail(@RequestBody ReasonDetailsDto reasonDetailsDto, Locale locale) {
        reasonDetailsService.createReasonDetail(reasonDetailsDto);
        return ResponseEntity.status(201).build();
    }

    /**
     * Update an existing reason detail.
     */
    @PutMapping("/edit")
    public ResponseEntity<Void> updateReasonDetail(@RequestBody ReasonDetailsDto reasonDetailsDto, Locale locale) {
        reasonDetailsService.updateReasonDetail(reasonDetailsDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a reason detail by ID (soft delete).
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReasonDetail(@PathVariable Long id, Locale locale) {
        reasonDetailsService.deleteReasonDetail(id);
        return ResponseEntity.noContent().build();
    }
}