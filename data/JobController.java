package com.sadeem.smap.controller;

import com.sadeem.smap.service.JobService;
import com.sadeem.smap.dto.JobDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    /**
     * Get all jobs (general route).
     */
    @GetMapping
    public ResponseEntity<Iterable<JobDto>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    /**
     * Get a paginated list of jobs.
     */
    @GetMapping("/list")
    public ResponseEntity<Page<JobDto>> getJobsPaginated(Pageable pageable) {
        return ResponseEntity.ok(jobService.getJobsPaginated(pageable));
    }

    /**
     * Get a specific job by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobDto> getJobById(@PathVariable Long id, Locale locale) {
        return jobService.getJobById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}