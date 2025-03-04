package com.sadeem.smap.controller;

import com.sadeem.smap.dto.MachineDto;
import com.sadeem.smap.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/machines")
public class MachineController {

    @Autowired
    private MachineService machineService;

    /**
     * Get all machines.
     */
    @GetMapping
    public ResponseEntity<Iterable<MachineDto>> getAllMachines() {
        return ResponseEntity.ok(machineService.getAllMachines());
    }

    /**
     * Get paginated list of machines.
     */
    @GetMapping("/list")
    public ResponseEntity<Page<MachineDto>> getMachinesPaginated(Pageable pageable) {
        return ResponseEntity.ok(machineService.getMachinesPaginated(pageable));
    }

    /**
     * Get machine by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MachineDto> getMachineById(@PathVariable Long id, Locale locale) {
        return machineService.getMachineById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new machine.
     */
    @PostMapping("/add")
    public ResponseEntity<Void> createMachine(@RequestBody MachineDto machineDto, Locale locale) {
        machineService.createMachine(machineDto);
        return ResponseEntity.status(201).build();
    }

    /**
     * Update an existing machine.
     */
    @PutMapping("/edit")
    public ResponseEntity<Void> updateMachine(@RequestBody MachineDto machineDto, Locale locale) {
        machineService.updateMachine(machineDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a machine by ID (soft delete).
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMachine(@PathVariable Long id, Locale locale) {
        machineService.deleteMachine(id);
        return ResponseEntity.noContent().build();
    }
}