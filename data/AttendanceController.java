package com.sadeem.smap.controller;

import com.sadeem.smap.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.Locale;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    /**
     * Get attendance data for a specific month.
     */
    @GetMapping("/data")
    public ResponseEntity<String> getAttendanceData(
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) String month,
            Locale locale) {
        YearMonth yearMonth = YearMonth.parse(month); // Parse "yyyy-MM"
        return ResponseEntity.ok(attendanceService.getAttendanceData(departmentId, yearMonth));
    }

    /**
     * Get vacation data for a specific month.
     */
    @GetMapping("/vacation")
    public ResponseEntity<String> getVacationData(
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) String month,
            Locale locale) {
        YearMonth yearMonth = YearMonth.parse(month); // Parse "yyyy-MM"
        return ResponseEntity.ok(attendanceService.getVacationData(departmentId, yearMonth));
    }

    /**
     * Get overtime data for a specific month.
     */
    @GetMapping("/overtime")
    public ResponseEntity<String> getOvertimeData(
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) String month,
            Locale locale) {
        YearMonth yearMonth = YearMonth.parse(month); // Parse "yyyy-MM"
        return ResponseEntity.ok(attendanceService.getOvertimeData(departmentId, yearMonth));
    }

    /**
     * Save attendance record.
     */
    @PostMapping("/save")
    public ResponseEntity<Void> saveAttendance(@RequestParam String workerId, @RequestParam String date) {
        attendanceService.saveAttendance(workerId, date);
        return ResponseEntity.ok().build();
    }

    /**
     * Save overtime record.
     */
    @PostMapping("/overtime/save")
    public ResponseEntity<Void> saveOvertime(@RequestParam String workerId, @RequestParam String hours, @RequestParam String date) {
        attendanceService.saveOvertime(workerId, Integer.parseInt(hours), date);
        return ResponseEntity.ok().build();
    }

    /**
     * Save permission record.
     */
    @PostMapping("/permission/save")
    public ResponseEntity<Void> savePermission(@RequestParam String workerId, @RequestParam String date) {
        attendanceService.savePermission(workerId, date);
        return ResponseEntity.ok().build();
    }

    /**
     * Save vacation record.
     */
    @PostMapping("/vacation/save")
    public ResponseEntity<Void> saveVacation(@RequestParam String workerId, @RequestParam String date) {
        attendanceService.saveVacation(workerId, date);
        return ResponseEntity.ok().build();
    }
}