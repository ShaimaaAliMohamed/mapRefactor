package com.sadeem.smap.controller;

import com.sadeem.smap.dto.AccessoriesDto;
import com.sadeem.smap.service.AccessoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/accessories")
public class AccessoriesController {

    @Autowired
    private AccessoriesService accessoriesService;

    /**
     * Get all accessories.
     */
    @GetMapping
    public ResponseEntity<Iterable<AccessoriesDto>> getAllAccessories() {
        return ResponseEntity.ok(accessoriesService.getAllAccessories());
    }

    /**
     * Get paginated list of accessories.
     */
    @GetMapping("/list")
    public ResponseEntity<Page<AccessoriesDto>> getAccessoriesPaginated(Pageable pageable) {
        return ResponseEntity.ok(accessoriesService.getAccessoriesPaginated(pageable));
    }

    /**
     * Get accessory by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccessoriesDto> getAccessoryById(@PathVariable Long id, Locale locale) {
        return accessoriesService.getAccessoryById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new accessory.
     */
    @PostMapping("/add")
    public ResponseEntity<Void> createAccessory(@RequestBody AccessoriesDto accessoryDto, Locale locale) {
        accessoriesService.createAccessory(accessoryDto);
        return ResponseEntity.status(201).build();
    }

    /**
     * Update an existing accessory.
     */
    @PutMapping("/edit")
    public ResponseEntity<Void> updateAccessory(@RequestBody AccessoriesDto accessoryDto, Locale locale) {
        accessoriesService.updateAccessory(accessoryDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete an accessory by ID.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAccessory(@PathVariable Long id, Locale locale) {
        accessoriesService.deleteAccessory(id);
        return ResponseEntity.noContent().build();
    }
}