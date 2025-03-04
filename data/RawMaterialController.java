package com.sadeem.smap.controller;

import com.sadeem.smap.dto.RawMaterialDto;
import com.sadeem.smap.service.RawMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/materials")
public class RawMaterialController {

    @Autowired
    private RawMaterialService rawMaterialService;

    /**
     * Get all raw materials.
     */
    @GetMapping
    public ResponseEntity<Iterable<RawMaterialDto>> getAllRawMaterials() {
        return ResponseEntity.ok(rawMaterialService.getAllRawMaterials());
    }

    /**
     * Get paginated list of raw materials.
     */
    @GetMapping("/list")
    public ResponseEntity<Page<RawMaterialDto>> getRawMaterialsPaginated(Pageable pageable) {
        return ResponseEntity.ok(rawMaterialService.getRawMaterialsPaginated(pageable));
    }

    /**
     * Get raw material by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RawMaterialDto> getRawMaterialById(@PathVariable Long id, Locale locale) {
        return rawMaterialService.getRawMaterialById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new raw material.
     */
    @PostMapping("/add")
    public ResponseEntity<Void> createRawMaterial(@RequestBody RawMaterialDto rawMaterialDto, Locale locale) {
        rawMaterialService.createRawMaterial(rawMaterialDto);
        return ResponseEntity.status(201).build();
    }

    /**
     * Update an existing raw material.
     */
    @PutMapping("/edit")
    public ResponseEntity<Void> updateRawMaterial(@RequestBody RawMaterialDto rawMaterialDto, Locale locale) {
        rawMaterialService.updateRawMaterial(rawMaterialDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a raw material by ID.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRawMaterial(@PathVariable Long id, Locale locale) {
        rawMaterialService.deleteRawMaterial(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete all raw materials.
     */
    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAllRawMaterials(Locale locale) {
        rawMaterialService.deleteAllRawMaterials();
        return ResponseEntity.noContent().build();
    }
}