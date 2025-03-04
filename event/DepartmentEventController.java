package com.sadeem.smap.controller;

import com.sadeem.smap.dto.DepartmentDto;
import com.sadeem.smap.service.DepartmentService;
import com.sadeem.smap.util.EventResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

@RestController
@RequestMapping("/api/departments")
public class DepartmentEventController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Create a new department.
     */
    @PostMapping("/add")
    public ResponseEntity<EventResult> addDepartment(
            @Valid @RequestBody DepartmentDto departmentDto,
            BindingResult bindingResult,
            Locale locale) {
        if (bindingResult.hasErrors()) {
            String errorMessage = messageSource.getMessage("invalid_input", null, locale);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new EventResult("error", errorMessage));
        }
        try {
            EventResult result = departmentService.createDepartment(departmentDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }

    /**
     * Update an existing department.
     */
    @PutMapping("/edit")
    public ResponseEntity<EventResult> editDepartment(
            @Valid @RequestBody DepartmentDto departmentDto,
            BindingResult bindingResult,
            Locale locale) {
        if (bindingResult.hasErrors()) {
            String errorMessage = messageSource.getMessage("invalid_input", null, locale);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new EventResult("error", errorMessage));
        }
        try {
            EventResult result = departmentService.updateDepartment(departmentDto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }

    /**
     * Delete a department by ID.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<EventResult> deleteDepartment(@PathVariable Long id, Locale locale) {
        try {
            EventResult result = departmentService.deleteDepartment(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }

    /**
     * Delete all departments.
     */
    @DeleteMapping("/delete-all")
    public ResponseEntity<EventResult> deleteAllDepartments(Locale locale) {
        try {
            departmentService.deleteAllDepartments();
            String successMessage = messageSource.getMessage("departments.deleted.successfully", null, locale);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }
}