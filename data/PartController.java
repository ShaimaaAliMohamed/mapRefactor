package com.sadeem.smap.controller;

import com.sadeem.smap.dto.PartDto;
import com.sadeem.smap.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

@RestController
@RequestMapping("/api/parts")
public class PartController {

    @Autowired
    private PartService partService;

    /**
     * Get all parts.
     */
    @GetMapping
    public ResponseEntity<Iterable<PartDto>> getAllParts() {
        return ResponseEntity.ok(partService.getAllParts());
    }

    /**
     * Get part by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PartDto> getPartById(@PathVariable Long id, Locale locale) {
        return partService.getPartById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new part.
     */
    @PostMapping("/add")
    public ResponseEntity<Void> createPart(@RequestBody PartDto partDto, @RequestParam("file") MultipartFile file, Locale locale) throws IOException {
        partService.createPart(partDto, file);
        return ResponseEntity.status(201).build();
    }

    /**
     * Update an existing part.
     */
    @PutMapping("/edit")
    public ResponseEntity<Void> updatePart(@RequestBody PartDto partDto, @RequestParam(value = "file", required = false) MultipartFile file, Locale locale) throws IOException {
        partService.updatePart(partDto, file);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a part by ID.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePart(@PathVariable Long id, Locale locale) {
        partService.deletePart(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete all parts.
     */
    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAllParts(Locale locale) {
        partService.deleteAllParts();
        return ResponseEntity.noContent().build();
    }
}