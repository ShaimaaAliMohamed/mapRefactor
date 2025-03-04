package com.sadeem.smap.controller;

import com.sadeem.smap.dto.JobDto;
import com.sadeem.smap.service.JobService;
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
@RequestMapping("/api/jobs")
public class JobEventController {

    @Autowired
    private JobService jobService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Create a new job.
     */
    @PostMapping("/add")
    public ResponseEntity<EventResult> addJob(
            @Valid @RequestBody JobDto jobDto,
            BindingResult bindingResult,
            Locale locale) {
        if (bindingResult.hasErrors()) {
            String errorMessage = messageSource.getMessage("invalid_input", null, locale);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new EventResult("error", errorMessage));
        }
        try {
            EventResult result = jobService.createJob(jobDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }

    /**
     * Update an existing job.
     */
    @PutMapping("/edit")
    public ResponseEntity<EventResult> editJob(
            @Valid @RequestBody JobDto jobDto,
            BindingResult bindingResult,
            Locale locale) {
        if (bindingResult.hasErrors()) {
            String errorMessage = messageSource.getMessage("invalid_input", null, locale);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new EventResult("error", errorMessage));
        }
        try {
            EventResult result = jobService.updateJob(jobDto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }

    /**
     * Delete a job by ID.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<EventResult> deleteJob(@PathVariable Long id, Locale locale) {
        try {
            EventResult result = jobService.deleteJob(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }

    /**
     * Delete all jobs.
     */
    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAllJobs(Locale locale) {
        try {
            jobService.deleteAllJobs();
            String successMessage = messageSource.getMessage("jobs.deleted.successfully", null, locale);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }
}