package com.sadeem.smap.controller;

import com.sadeem.smap.dto.UnitDto;
import com.sadeem.smap.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/measurements")
public class MeasurementController {

    @Autowired
    private MeasurementService measurementService;

    /**
     * Get all units of measurement.
     */
    @GetMapping
    public ResponseEntity<Iterable<UnitDto>> getAllUnits() {
        return ResponseEntity.ok(measurementService.getAllUnits());
    }

    /**
     * Get paginated list of units.
     */
    @GetMapping("/list")
    public ResponseEntity<Page<UnitDto>> getUnitsPaginated(Pageable pageable) {
        return ResponseEntity.ok(measurementService.getUnitsPaginated(pageable));
    }

    /**
     * Get unit by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UnitDto> getUnitById(@PathVariable Long id, Locale locale) {
        return measurementService.getUnitById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new unit of measurement.
     */
    @PostMapping("/add")
    public ResponseEntity<Void> createUnit(@RequestBody UnitDto unitDto, Locale locale) {
        measurementService.createUnit(unitDto);
        return ResponseEntity.status(201).build();
    }

    /**
     * Update an existing unit of measurement.
     */
    @PutMapping("/edit")
    public ResponseEntity<Void> updateUnit(@RequestBody UnitDto unitDto, Locale locale) {
        measurementService.updateUnit(unitDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a unit of measurement by ID.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUnit(@PathVariable Long id, Locale locale) {
        measurementService.deleteUnit(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete all units of measurement.
     */
    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAllUnits(Locale locale) {
        measurementService.deleteAllUnits();
        return ResponseEntity.noContent().build();
    }
}