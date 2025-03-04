package com.sadeem.smap.controller;

import com.sadeem.smap.dto.ReasonDto;
import com.sadeem.smap.service.ReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/reasons")
public class ReasonController {

    @Autowired
    private ReasonService reasonService;

    /**
     * Get all reasons.
     */
    @GetMapping
    public ResponseEntity<Iterable<ReasonDto>> getAllReasons() {
        return ResponseEntity.ok(reasonService.getAllReasons());
    }

    /**
     * Get paginated list of reasons.
     */
    @GetMapping("/list")
    public ResponseEntity<Page<ReasonDto>> getReasonsPaginated(Pageable pageable) {
        return ResponseEntity.ok(reasonService.getReasonsPaginated(pageable));
    }

    /**
     * Get reason by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReasonDto> getReasonById(@PathVariable Long id, Locale locale) {
        return reasonService.getReasonById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new reason.
     */
    @PostMapping("/add")
    public ResponseEntity<Void> createReason(@RequestBody ReasonDto reasonDto, Locale locale) {
        reasonService.createReason(reasonDto);
        return ResponseEntity.status(201).build();
    }

    /**
     * Update an existing reason.
     */
    @PutMapping("/edit")
    public ResponseEntity<Void> updateReason(@RequestBody ReasonDto reasonDto, Locale locale) {
        reasonService.updateReason(reasonDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a reason by ID (soft delete).
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReason(@PathVariable Long id, Locale locale) {
        reasonService.deleteReason(id);
        return ResponseEntity.noContent().build();
    }
}