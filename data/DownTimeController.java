package com.sadeem.smap.controller;

import com.sadeem.smap.dto.DownTimeReportDto;
import com.sadeem.smap.dto.ReasonHourDto;
import com.sadeem.smap.service.DownTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/downtime")
public class DownTimeController {

    @Autowired
    private DownTimeService downTimeService;

    /**
     * Get downtime report for a specific month.
     */
    @GetMapping("/report")
    public ResponseEntity<List<DownTimeReportDto>> getDownTimeReport(
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) String month,
            Locale locale) {
        return ResponseEntity.ok(downTimeService.getDownTimeReport(departmentId, month));
    }

    /**
     * Get machine downtime report for a specific month.
     */
    @GetMapping("/machine-report")
    public ResponseEntity<List<DownTimeReportDto>> getMachineDownTimeReport(
            @RequestParam(required = false) String month,
            Locale locale) {
        return ResponseEntity.ok(downTimeService.getMachineDownTimeReport(month));
    }

    /**
     * Get worker downtime report for a specific month.
     */
    @GetMapping("/worker-report")
    public ResponseEntity<List<DownTimeReportDto>> getWorkerDownTimeReport(
            @RequestParam(required = false) String month,
            Locale locale) {
        return ResponseEntity.ok(downTimeService.getWorkerDownTimeReport(month));
    }

    /**
     * Mark downtime as solved.
     */
    @PostMapping("/solved")
    public ResponseEntity<Void> markAsSolved(@RequestParam Long id, Locale locale) {
        downTimeService.markAsSolved(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Mark downtime as seen.
     */
    @PostMapping("/seen")
    public ResponseEntity<Void> markAsSeen(@RequestParam Long id, Locale locale) {
        downTimeService.markAsSeen(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Calculate reason hours.
     */
    @GetMapping("/reason-hours")
    public ResponseEntity<List<ReasonHourDto>> calculateReasonHours(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String departmentId,
            Locale locale) {
        return ResponseEntity.ok(downTimeService.calculateReasonHours(startDate, endDate, departmentId));
    }
}