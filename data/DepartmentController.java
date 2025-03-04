package com.sadeem.smap.controller;

import com.sadeem.smap.service.DepartmentService;
import com.sadeem.smap.dto.DepartmentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Get all departments (general route).
     */
    @GetMapping
    public ResponseEntity<Iterable<DepartmentDto>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    /**
     * Get a paginated list of departments.
     */
    @GetMapping("/list")
    public ResponseEntity<Page<DepartmentDto>> getDepartmentsPaginated(Pageable pageable) {
        return ResponseEntity.ok(departmentService.getDepartmentsPaginated(pageable));
    }

    /**
     * Get a specific department by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable Long id, Locale locale) {
        return departmentService.getDepartmentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new department.
     */
    @PostMapping
    public ResponseEntity<DepartmentDto> createDepartment(@RequestBody DepartmentDto departmentDto, Locale locale) {
        DepartmentDto createdDepartment = departmentService.createDepartment(departmentDto);
        String successMessage = messageSource.getMessage("department.added.successfully", null, locale);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
    }

    /**
     * Update an existing department.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDto departmentDto, Locale locale) {
        DepartmentDto updatedDepartment = departmentService.updateDepartment(id, departmentDto);
        if (updatedDepartment != null) {
            String successMessage = messageSource.getMessage("department.edited.successfully", null, locale);
            return ResponseEntity.ok(updatedDepartment);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Delete a department by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id, Locale locale) {
        departmentService.deleteDepartment(id);
        String successMessage = messageSource.getMessage("department.deleted.successfully", null, locale);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete all departments.
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAllDepartments(Locale locale) {
        departmentService.deleteAllDepartments();
        String successMessage = messageSource.getMessage("departments.deleted.successfully", null, locale);
        return ResponseEntity.noContent().build();
    }
}